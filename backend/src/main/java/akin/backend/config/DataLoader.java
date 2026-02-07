package akin.backend.config;

import akin.backend.user.entity.Admin;
import akin.backend.user.entity.Garson;
import akin.backend.user.entity.Mutfak;
import akin.backend.user.repository.AdminRepository;
import akin.backend.user.repository.GarsonRepository;
import akin.backend.user.repository.MutfakRepository;
import akin.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements ApplicationRunner {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final GarsonRepository garsonRepository;
    private final MutfakRepository mutfakRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) {
            return;
        }
        createAdmin("admin", "admin123");
        createGarson("garson", "garson123");
        createMutfak("mutfak", "mutfak123");
        log.info("Varsayılan kullanıcılar oluşturuldu: admin/admin123, garson/garson123, mutfak/mutfak123");
    }

    private void createAdmin(String username, String rawPassword) {
        if (userRepository.findByUsername(username).isPresent()) {
            return;
        }
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(rawPassword));
        admin.setEnabled(true);
        adminRepository.save(admin);
    }

    private void createGarson(String username, String rawPassword) {
        if (userRepository.findByUsername(username).isPresent()) {
            return;
        }
        Garson garson = new Garson();
        garson.setUsername(username);
        garson.setPassword(passwordEncoder.encode(rawPassword));
        garson.setEnabled(true);
        garson.setAvailable(false);
        garson.setWorkingToday(true);
        garsonRepository.save(garson);
    }

    private void createMutfak(String username, String rawPassword) {
        if (userRepository.findByUsername(username).isPresent()) {
            return;
        }
        Mutfak mutfak = new Mutfak();
        mutfak.setUsername(username);
        mutfak.setPassword(passwordEncoder.encode(rawPassword));
        mutfak.setEnabled(true);
        mutfak.setWorkingToday(true);
        mutfakRepository.save(mutfak);
    }
}
