package akin.backend.user.service;

import akin.backend.admin.request.ChangePasswordRequest;
import akin.backend.user.dto.request.UpdateProfileRequest;
import akin.backend.user.dto.response.ProfileResponse;
import akin.backend.user.dto.response.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface UserService {

    UserResponse getProfile(Long userId);

    ProfileResponse updateProfile(Long userId, @Valid UpdateProfileRequest request);


    void deactivateAccount(Long userId);

    ResponseEntity<ProfileResponse> changePassword(Long userId, ChangePasswordRequest changePasswordRequest);

}
