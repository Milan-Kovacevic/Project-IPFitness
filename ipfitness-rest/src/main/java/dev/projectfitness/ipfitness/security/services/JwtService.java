package dev.projectfitness.ipfitness.security.services;

import dev.projectfitness.ipfitness.models.dtos.VerifiedUser;
import dev.projectfitness.ipfitness.models.entities.TokenEntity;
import dev.projectfitness.ipfitness.models.enums.TokenType;
import dev.projectfitness.ipfitness.repositories.TokenEntityRepository;
import dev.projectfitness.ipfitness.security.models.JwtUser;
import dev.projectfitness.ipfitness.util.Utility;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final TokenEntityRepository tokenEntityRepository;

    @Value("${ipfitness.security.jwt.secret}")
    private String tokenSigningKey;
    @Value("${ipfitness.security.jwt.expiration}")
    private Long tokenExpirationTime;
    @Value("${ipfitness.security.jwt.issuer}")
    private String tokenIssuerName;

    private static final String TOKEN_ID_CLAIM = "tokenId";
    private static final String USER_ID_CLAIM = "userId";

    // Returns generated Jwt token
    public String generateAuthenticationToken(JwtUser userDetails) {
        Map<String, Object> extraTokenClaims = new HashMap<>();
        String tokenId = UUID.randomUUID().toString();
        extraTokenClaims.put(TOKEN_ID_CLAIM, tokenId);
        Date issueDate = Utility.getUtilCurrentDate();
        Date expirationDate = Utility.addToUtilDate(issueDate, tokenExpirationTime);
        String token = generateTokenInternal(extraTokenClaims, userDetails.getUsername(), issueDate, expirationDate);

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setTokenId(tokenId);
        tokenEntity.setUserId(userDetails.getUserId());
        tokenEntity.setTokenType(TokenType.AUTHENTICATE);
        tokenEntity.setValue(token);
        tokenEntityRepository.save(tokenEntity);

        return token;
    }

    // Returns tokenId for generated jwt
    public String generateVerificationToken(Integer userId, String username) {
        Map<String, Object> extraTokenClaims = new HashMap<>();
        String tokenId = UUID.randomUUID().toString();
        extraTokenClaims.put(TOKEN_ID_CLAIM, tokenId);
        extraTokenClaims.put(USER_ID_CLAIM, userId);
        Date issueDate = Utility.getUtilCurrentDate();
        Date expirationDate = Utility.addToUtilDate(issueDate, tokenExpirationTime);
        String token = generateTokenInternal(extraTokenClaims, username, issueDate, expirationDate);

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setTokenId(tokenId);
        tokenEntity.setUserId(userId);
        tokenEntity.setTokenType(TokenType.ACTIVATE);
        tokenEntity.setValue(token);
        tokenEntityRepository.save(tokenEntity);

        return tokenId;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenValid(String token, UserDetails userDetails, TokenType expectedType) {
        final String username = extractUsername(token);
        Date tokenExpirationDate = extractExpirationDate(token);
        String tokenId = extractClaim(token, (claims -> claims.get(TOKEN_ID_CLAIM, String.class)));

        Optional<TokenEntity> tokenOpt = tokenEntityRepository.findById(tokenId);
        if (tokenOpt.isEmpty())
            return false;
        TokenEntity tokenEntity = tokenOpt.get();

        Date currDate = Utility.getUtilCurrentDate();
        boolean isExpired = Utility.compareUtilDates(currDate, tokenExpirationDate) >= 0;
        boolean isAuthToken = tokenEntity.getTokenType().equals(expectedType);
        boolean usernameMatch = userDetails.getUsername().equals(username);
        if (isExpired)
            tokenEntityRepository.delete(tokenEntity);

        return usernameMatch && !isExpired && isAuthToken;
    }

    public Optional<VerifiedUser> verifyForToken(String tokenId) {
        Optional<TokenEntity> tokenOpt = tokenEntityRepository.findById(tokenId);
        if (tokenOpt.isEmpty())
            return Optional.empty();

        TokenEntity tokenEntity = tokenOpt.get();
        if (!tokenEntity.getTokenType().equals(TokenType.ACTIVATE))
            return Optional.empty();

        String token = tokenEntity.getValue();
        Date tokenExpirationDate = extractExpirationDate(token);
        Date currDate = Utility.getUtilCurrentDate();
        boolean isExpired = Utility.compareUtilDates(currDate, tokenExpirationDate) >= 0;
        if (isExpired)
            return Optional.empty();

        Integer userId = extractClaim(token, (claims -> claims.get(USER_ID_CLAIM, Integer.class)));
        VerifiedUser verifiedUser = new VerifiedUser(userId, token);

        tokenEntityRepository.delete(tokenEntity);
        return Optional.of(verifiedUser);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllTokenClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllTokenClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String generateTokenInternal(Map<String, Object> extraClaims, String username, Date issueDate, Date expirationDate) {
        Key signingKey = getSigningKey();
        return Jwts
                .builder()
                .subject(username)
                .issuer(tokenIssuerName)
                .issuedAt(issueDate)
                .expiration(expirationDate)
                .claims(extraClaims)
                .signWith(signingKey)
                .compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(tokenSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
