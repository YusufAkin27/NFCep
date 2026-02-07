package akin.backend.config;

import akin.backend.user.entity.Role;
import akin.backend.user.entity.User;
import akin.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) {
            return;
        }
        createUser("admin", "admin123", Set.of(Role.ADMIN));
        createUser("garson", "garson123", Set.of(Role.GARSON));
        createUser("mutfak", "mutfak123", Set.of(Role.MUTFAK));
        log.info("Varsayılan kullanıcılar oluşturuldu: admin/admin123, garson/garson123, mutfak/mutfak123");
    }

    private void createUser(String username, String rawPassword, Set<Role> roles) {
        if (userRepository.findByUsername(username).isPresent()) {
            return;
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRoles(roles);
        user.setEnabled(true);
        userRepository.save(user);
    }
}
