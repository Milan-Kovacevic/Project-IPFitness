package dev.projectfitness.ipfitness.controllers;

import dev.projectfitness.ipfitness.exceptions.ForbiddenException;
import dev.projectfitness.ipfitness.exceptions.UnauthorizedException;
import dev.projectfitness.ipfitness.models.dtos.ProfilePictureName;
import dev.projectfitness.ipfitness.models.dtos.SingleFitnessUser;
import dev.projectfitness.ipfitness.models.requests.FitnessUserInfoRequest;
import dev.projectfitness.ipfitness.models.requests.PasswordChangeRequest;
import dev.projectfitness.ipfitness.security.models.JwtUser;
import dev.projectfitness.ipfitness.services.UserProfileService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("${ipfitness.base-url}/users")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;

    @GetMapping("/{userId}/info")
    public SingleFitnessUser getUserProfileInfo(@PathVariable("userId") Integer userId, Authentication auth) {
        if (auth == null)
            throw new UnauthorizedException();
        JwtUser jwtUser = (JwtUser) auth.getPrincipal();
        if (!jwtUser.getUserId().equals(userId))
            throw new ForbiddenException();
        return userProfileService.getFitnessUserInfo(userId);
    }

    @PutMapping("/{userId}/info")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserProfileInfo(@PathVariable("userId") Integer userId,
                                      @RequestBody @Valid FitnessUserInfoRequest request, Authentication auth) {
        if (auth == null)
            throw new UnauthorizedException();
        JwtUser jwtUser = (JwtUser) auth.getPrincipal();
        if (!jwtUser.getUserId().equals(userId))
            throw new ForbiddenException();
        userProfileService.updateFitnessUserInfo(userId, request);
    }

    @PutMapping("/{userId}/info/avatar")
    @ResponseStatus(HttpStatus.OK)
    public ProfilePictureName changeUserAvatar(@PathVariable("userId") Integer userId,
                                               @ModelAttribute @NotNull MultipartFile avatar, Authentication auth) {
        if (auth == null)
            throw new UnauthorizedException();
        JwtUser jwtUser = (JwtUser) auth.getPrincipal();
        if (!jwtUser.getUserId().equals(userId))
            throw new ForbiddenException();
        return userProfileService.updateFitnessUserAvatar(userId, avatar);
    }

    @PostMapping("/{userId}/info/password")
    @ResponseStatus(HttpStatus.OK)
    public void changeUserPassword(@PathVariable("userId") Integer userId,
                                   @RequestBody @Valid PasswordChangeRequest request, Authentication auth) {
        if (auth == null)
            throw new UnauthorizedException();
        JwtUser jwtUser = (JwtUser) auth.getPrincipal();
        if (!jwtUser.getUserId().equals(userId))
            throw new ForbiddenException();
        userProfileService.changeFitnessUserPassword(userId, request);
    }

}
