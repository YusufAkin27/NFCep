package akin.backend.admin.response;

import akin.backend.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeStatisticsResponse {

    private Long id;
    private String username;
    private Set<Role> roles;
    private Boolean workingToday;
    private long workDaysCount;
    private String firstName;
    private String lastName;
}
