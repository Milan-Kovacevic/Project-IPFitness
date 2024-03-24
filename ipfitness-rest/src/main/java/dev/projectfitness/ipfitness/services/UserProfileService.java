package dev.projectfitness.ipfitness.services;

import dev.projectfitness.ipfitness.models.dtos.ProfilePictureName;
import dev.projectfitness.ipfitness.models.dtos.SingleFitnessUser;
import dev.projectfitness.ipfitness.models.requests.FitnessUserInfoRequest;
import dev.projectfitness.ipfitness.models.requests.PasswordChangeRequest;
import org.springframework.web.multipart.MultipartFile;

public interface UserProfileService {
    SingleFitnessUser getFitnessUserInfo(Integer userId);

    void updateFitnessUserInfo(Integer userId, FitnessUserInfoRequest request);

    ProfilePictureName updateFitnessUserAvatar(Integer userId, MultipartFile avatar);

    void changeFitnessUserPassword(Integer userId, PasswordChangeRequest request);
}
