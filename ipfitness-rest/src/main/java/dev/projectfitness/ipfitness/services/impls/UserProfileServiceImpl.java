package dev.projectfitness.ipfitness.services.impls;

import dev.projectfitness.ipfitness.exceptions.BadRequestException;
import dev.projectfitness.ipfitness.exceptions.HttpException;
import dev.projectfitness.ipfitness.exceptions.NotFoundException;
import dev.projectfitness.ipfitness.models.dtos.ProfilePictureName;
import dev.projectfitness.ipfitness.models.dtos.SingleFitnessUser;
import dev.projectfitness.ipfitness.models.entities.FitnessUserEntity;
import dev.projectfitness.ipfitness.models.requests.FitnessUserInfoRequest;
import dev.projectfitness.ipfitness.models.requests.PasswordChangeRequest;
import dev.projectfitness.ipfitness.repositories.FitnessUserEntityRepository;
import dev.projectfitness.ipfitness.services.ActionLoggingService;
import dev.projectfitness.ipfitness.services.StorageAccessService;
import dev.projectfitness.ipfitness.services.UserProfileService;
import dev.projectfitness.ipfitness.util.ActionMessageResolver;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final FitnessUserEntityRepository fitnessUserEntityRepository;
    private final StorageAccessService storageAccessService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final ActionLoggingService actionLoggingService;

    @Override
    public SingleFitnessUser getFitnessUserInfo(Integer userId) {
        FitnessUserEntity entity = fitnessUserEntityRepository.findById(userId).orElseThrow(NotFoundException::new);
        return modelMapper.map(entity, SingleFitnessUser.class);
    }

    @Override
    public void updateFitnessUserInfo(Integer userId, FitnessUserInfoRequest request) {
        FitnessUserEntity entity = fitnessUserEntityRepository.findById(userId).orElseThrow(NotFoundException::new);
        modelMapper.map(request, entity);
        fitnessUserEntityRepository.saveAndFlush(entity);
        actionLoggingService.logSensitiveAction(ActionMessageResolver.resolveUserInfoUpdate(entity));
    }

    @Override
    public ProfilePictureName updateFitnessUserAvatar(Integer userId, MultipartFile avatar) {
        FitnessUserEntity entity = fitnessUserEntityRepository.findById(userId).orElseThrow(NotFoundException::new);
        if (entity.getAvatar() != null) {
            try {
                storageAccessService.deleteFile(entity.getAvatar());
            } catch (IOException e) {
                actionLoggingService.logException(e);
            }
        }
        Optional<String> pictureOpt = storageAccessService.saveFile(avatar);
        if (pictureOpt.isEmpty())
            throw new HttpException();

        String picture = pictureOpt.get();
        entity.setAvatar(picture);
        fitnessUserEntityRepository.saveAndFlush(entity);
        actionLoggingService.logMessage(ActionMessageResolver.resolveUserAvatarUpdate(entity, picture));
        return new ProfilePictureName(picture);
    }

    @Override
    public void changeFitnessUserPassword(Integer userId, PasswordChangeRequest request) {
        FitnessUserEntity entity = fitnessUserEntityRepository.findById(userId).orElseThrow(NotFoundException::new);
        String newPasswordEncrypted = passwordEncoder.encode(request.getNewPassword());
        if (!passwordEncoder.matches(request.getOldPassword(), entity.getPassword()))
            throw new BadRequestException();

        entity.setPassword(newPasswordEncrypted);
        fitnessUserEntityRepository.saveAndFlush(entity);
        actionLoggingService.logSensitiveAction(ActionMessageResolver.resolveUserPasswordChange(entity));
    }
}
