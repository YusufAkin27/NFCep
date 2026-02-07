package akin.backend.user.service;

import akin.backend.admin.request.ChangePasswordRequest;
import akin.backend.user.dto.request.UpdateProfileRequest;
import akin.backend.user.dto.response.ProfileResponse;
import akin.backend.user.dto.response.UserResponse;
import akin.backend.user.entity.User;
import akin.backend.user.exception.UserNotFoundException;
import akin.backend.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .roles(Set.of(user.getRole()))
                .enabled(user.isEnabled())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .workingToday(user.getWorkingToday())
                .available(user.getAvailable())
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public ProfileResponse updateProfile(Long userId, @Valid UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        userRepository.save(user);
        return ProfileResponse.fromUser(user);
    }


    @Override
    public void deactivateAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public ResponseEntity<ProfileResponse> changePassword(Long userId, ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null) {
            throw new UserNotFoundException();
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        return new ResponseEntity<>(ProfileResponse.fromUser(user), HttpStatus.OK);
    }
}
