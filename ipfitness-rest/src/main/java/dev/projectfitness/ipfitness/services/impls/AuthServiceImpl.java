package dev.projectfitness.ipfitness.services.impls;

import dev.projectfitness.ipfitness.exceptions.BadRequestException;
import dev.projectfitness.ipfitness.exceptions.ConflictException;
import dev.projectfitness.ipfitness.exceptions.NotFoundException;
import dev.projectfitness.ipfitness.exceptions.UnauthorizedException;
import dev.projectfitness.ipfitness.models.dtos.LoginResponse;
import dev.projectfitness.ipfitness.models.dtos.VerifiedUser;
import dev.projectfitness.ipfitness.models.entities.FitnessUserEntity;
import dev.projectfitness.ipfitness.models.requests.LoginRequest;
import dev.projectfitness.ipfitness.models.requests.RegistrationRequest;
import dev.projectfitness.ipfitness.repositories.FitnessUserEntityRepository;
import dev.projectfitness.ipfitness.security.models.JwtUser;
import dev.projectfitness.ipfitness.security.services.JwtService;
import dev.projectfitness.ipfitness.services.ActionLoggingService;
import dev.projectfitness.ipfitness.services.AuthService;
import dev.projectfitness.ipfitness.services.EmailSenderService;
import dev.projectfitness.ipfitness.services.StorageAccessService;
import dev.projectfitness.ipfitness.util.ActionMessageResolver;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final FitnessUserEntityRepository fitnessUserEntityRepository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final StorageAccessService storageAccessService;
    private final EmailSenderService emailSenderService;
    private final ActionLoggingService actionLoggingService;

    @Override
    public LoginResponse login(LoginRequest request) {
        LoginResponse response;
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
            FitnessUserEntity fitnessUser = fitnessUserEntityRepository.findById(jwtUser.getUserId()).orElseThrow(NotFoundException::new);
            if (fitnessUser.getActive()) {
                response = modelMapper.map(fitnessUser, LoginResponse.class);
                String token = jwtService.generateAuthenticationToken(jwtUser);
                response.setToken(token);
            } else {
                generateAndSendVerificationToken(fitnessUser.getUserId(), fitnessUser.getUsername(), fitnessUser.getMail());
                response = modelMapper.map(fitnessUser, LoginResponse.class);
            }
            actionLoggingService.logMessage(ActionMessageResolver.resolveSuccessfulLogin(fitnessUser));
        } catch (Exception ex) {
            throw new UnauthorizedException();
        }
        return response;
    }

    @Override
    @Transactional
    public void register(RegistrationRequest request) {
        if (fitnessUserEntityRepository.existsByUsername(request.getUsername()))
            throw new ConflictException();

        FitnessUserEntity entity = modelMapper.map(request, FitnessUserEntity.class);
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        entity.setActive(false);
        entity.setEnabled(true);
        entity.setUserId(null);

        if (request.getPicture() != null) {
            Optional<String> avatar = storageAccessService.saveFile(request.getPicture());
            if (avatar.isEmpty())
                throw new BadRequestException("Avatar is in invalid format");
            entity.setAvatar(avatar.get());
        }
        FitnessUserEntity savedUser = fitnessUserEntityRepository.saveAndFlush(entity);
        actionLoggingService.logMessage(ActionMessageResolver.resolveRegister(savedUser));
        generateAndSendVerificationToken(savedUser.getUserId(), savedUser.getUsername(), savedUser.getMail());
    }

    @Override
    public void verify(String tokenId) {
        Optional<VerifiedUser> userOpt = jwtService.verifyForToken(tokenId);
        if (userOpt.isEmpty())
            throw new NotFoundException();

        int userId = userOpt.get().getUserId();
        Optional<FitnessUserEntity> entityOpt = fitnessUserEntityRepository.findById(userId);
        if (entityOpt.isEmpty())
            throw new NotFoundException();

        FitnessUserEntity entity = entityOpt.get();
        entity.setActive(true);
        fitnessUserEntityRepository.saveAndFlush(entity);
        actionLoggingService.logMessage(ActionMessageResolver.resolveAccountVerification(entity));
    }

    private void generateAndSendVerificationToken(Integer userId, String username, String sendTo) {
        String tokenId = jwtService.generateVerificationToken(userId, username);
        emailSenderService.sendVerificationEmail(sendTo, tokenId);
    }
}
