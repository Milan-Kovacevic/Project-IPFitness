package dev.projectfitness.ipfitness.security;

import dev.projectfitness.ipfitness.models.enums.TokenType;
import dev.projectfitness.ipfitness.security.services.JwtService;
import dev.projectfitness.ipfitness.services.ActionLoggingService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ActionLoggingService actionLoggingService;
    private static final String JWT_AUTHORIZATION_HEADER_CONTEXT = "Bearer";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String httpAuthHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String[] authHeaderParts;
        final String jwt;
        final String username;

        if (httpAuthHeader != null) {
            authHeaderParts = httpAuthHeader.split(" ");

            if (authHeaderParts.length == 2 && authHeaderParts[0].equals(JWT_AUTHORIZATION_HEADER_CONTEXT)) {
                try {
                    jwt = authHeaderParts[1];
                    username = jwtService.extractUsername(jwt);

                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        if (jwtService.isTokenValid(jwt, userDetails, TokenType.AUTHENTICATE)) {
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }
                } catch (Exception ex) {
                    actionLoggingService.logException(ex);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
