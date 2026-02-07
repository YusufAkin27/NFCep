package akin.backend.user.service;

import akin.backend.user.dto.request.UpdateProfileRequest;
import akin.backend.user.dto.response.ProfileResponse;
import akin.backend.user.dto.response.UserResponse;
import jakarta.validation.Valid;

public interface UserService {

    UserResponse getProfile(Long userId);

    ProfileResponse updateProfile(Long userId, @Valid UpdateProfileRequest request);



    void deactivateAccount(Long userId);
}
