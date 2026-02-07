package akin.backend.user.dto.response;

import akin.backend.user.entity.Role;
import akin.backend.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String username;
    private Set<Role> roles;
    private boolean enabled;
    private String firstName;
    private String lastName;
    private Boolean workingToday;
    private LocalDateTime createdAt;

    public static UserResponse from(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setRoles(user.getRoles());
        userResponse.setEnabled(user.isEnabled());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setWorkingToday(user.getWorkingToday());
        userResponse.setCreatedAt(user.getCreatedAt());
        return userResponse;
    }
}
