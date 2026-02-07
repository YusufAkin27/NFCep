package akin.backend.admin.service;

import akin.backend.admin.request.ChangePasswordRequest;
import akin.backend.admin.request.CreateMutfakRequest;
import akin.backend.admin.request.CreateWaiterRequest;
import akin.backend.admin.request.SetWorkingTodayRequest;
import akin.backend.admin.response.EmployeeStatisticsResponse;
import akin.backend.user.dto.response.UserResponse;
import akin.backend.user.entity.Role;
import akin.backend.user.entity.User;
import akin.backend.user.entity.WorkDay;
import akin.backend.user.exception.DuplicateUsernameException;
import akin.backend.user.exception.UserNotAdminException;
import akin.backend.user.exception.UserNotFoundException;
import akin.backend.user.repository.UserRepository;
import akin.backend.user.repository.WorkDayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final WorkDayRepository workDayRepository;
    private final PasswordEncoder passwordEncoder;

    private void ensureAdmin(Long userId) {
        User admin = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (!admin.getRoles().contains(Role.ADMIN)) {
            throw new UserNotAdminException();
        }
    }

    private User getGarsonOrThrow(Long garsonId) {
        User user = userRepository.findById(garsonId).orElseThrow(UserNotFoundException::new);
        if (!user.getRoles().contains(Role.GARSON)) {
            throw new UserNotFoundException();
        }
        return user;
    }

    private User getMutfakOrThrow(Long mutfakId) {
        User user = userRepository.findById(mutfakId).orElseThrow(UserNotFoundException::new);
        if (!user.getRoles().contains(Role.MUTFAK)) {
            throw new UserNotFoundException();
        }
        return user;
    }

    @Override
    @Transactional
    public UserResponse createWaiter(Long adminUserId, CreateWaiterRequest request) {
        ensureAdmin(adminUserId);
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateUsernameException();
        }
        User garson = new User();
        garson.setUsername(request.getUsername());
        garson.setPassword(passwordEncoder.encode(request.getPassword()));
        garson.setRoles(Set.of(Role.GARSON));
        garson.setFirstName(request.getFirstName());
        garson.setLastName(request.getLastName());
        garson.setEnabled(true);
        garson.setWorkingToday(true);
        garson.setCreatedAt(LocalDateTime.now());
        userRepository.save(garson);
        log.info("Garson oluşturuldu: {}", garson.getUsername());
        return UserResponse.from(garson);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllGarsons(Long adminUserId) {
        ensureAdmin(adminUserId);
        return userRepository.findByRolesContaining(Role.GARSON).stream()
                .map(UserResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public void changeGarsonPassword(Long adminUserId, Long garsonId, ChangePasswordRequest request) {
        ensureAdmin(adminUserId);
        User garson = getGarsonOrThrow(garsonId);
        garson.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(garson);
        log.info("Garson şifresi güncellendi: {}", garson.getUsername());
    }

    @Override
    @Transactional
    public void deleteGarson(Long adminUserId, Long garsonId) {
        ensureAdmin(adminUserId);
        User garson = getGarsonOrThrow(garsonId);
        userRepository.delete(garson);
        log.info("Garson silindi: {}", garson.getUsername());
    }

    @Override
    @Transactional
    public void setGarsonWorkingToday(Long adminUserId, Long garsonId, SetWorkingTodayRequest request) {
        ensureAdmin(adminUserId);
        User garson = getGarsonOrThrow(garsonId);
        garson.setWorkingToday(request.isWorkingToday());
        if (Boolean.TRUE.equals(request.isWorkingToday())) {
            LocalDate today = LocalDate.now();
            if (!workDayRepository.existsByUserIdAndWorkDate(garsonId, today)) {
                WorkDay workDay = WorkDay.builder().user(garson).workDate(today).build();
                workDayRepository.save(workDay);
            }
        }
        userRepository.save(garson);
        log.info("Garson bugün çalışıyor: {} -> {}", garson.getUsername(), request.isWorkingToday());
    }

    @Override
    @Transactional
    public UserResponse createMutfak(Long adminUserId, CreateMutfakRequest request) {
        ensureAdmin(adminUserId);
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateUsernameException();
        }
        User mutfak = new User();
        mutfak.setUsername(request.getUsername());
        mutfak.setPassword(passwordEncoder.encode(request.getPassword()));
        mutfak.setRoles(Set.of(Role.MUTFAK));
        mutfak.setFirstName(request.getFirstName());
        mutfak.setLastName(request.getLastName());
        mutfak.setEnabled(true);
        mutfak.setWorkingToday(true);
        mutfak.setCreatedAt(LocalDateTime.now());
        userRepository.save(mutfak);
        log.info("Mutfak hesabı oluşturuldu: {}", mutfak.getUsername());
        return UserResponse.from(mutfak);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllMutfak(Long adminUserId) {
        ensureAdmin(adminUserId);
        return userRepository.findByRolesContaining(Role.MUTFAK).stream()
                .map(UserResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public void changeMutfakPassword(Long adminUserId, Long mutfakId, ChangePasswordRequest request) {
        ensureAdmin(adminUserId);
        User mutfak = getMutfakOrThrow(mutfakId);
        mutfak.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(mutfak);
        log.info("Mutfak şifresi güncellendi: {}", mutfak.getUsername());
    }

    @Override
    @Transactional
    public void deleteMutfak(Long adminUserId, Long mutfakId) {
        ensureAdmin(adminUserId);
        User mutfak = getMutfakOrThrow(mutfakId);
        userRepository.delete(mutfak);
        log.info("Mutfak hesabı silindi: {}", mutfak.getUsername());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeStatisticsResponse> getEmployeeStatistics(Long adminUserId) {
        ensureAdmin(adminUserId);
        List<User> garsons = userRepository.findByRolesContaining(Role.GARSON);
        List<User> mutfak = userRepository.findByRolesContaining(Role.MUTFAK);
        List<EmployeeStatisticsResponse> result = new ArrayList<>();
        Stream.concat(garsons.stream(), mutfak.stream())
                .distinct()
                .forEach(user -> {
                    long workDaysCount = workDayRepository.countByUserId(user.getId());
                    result.add(EmployeeStatisticsResponse.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .roles(user.getRoles())
                            .workingToday(user.getWorkingToday())
                            .workDaysCount(workDaysCount)
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .build());
                });
        return result;
    }
}
