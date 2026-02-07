package akin.backend.admin.service;

import akin.backend.admin.request.ChangePasswordRequest;
import akin.backend.admin.request.CreateMutfakRequest;
import akin.backend.admin.request.CreateWaiterRequest;
import akin.backend.admin.request.SetWorkingTodayRequest;
import akin.backend.admin.response.EmployeeStatisticsResponse;
import akin.backend.user.dto.response.UserResponse;
import akin.backend.user.entity.Garson;
import akin.backend.user.entity.Mutfak;
import akin.backend.user.entity.WorkDay;
import akin.backend.user.exception.DuplicateUsernameException;
import akin.backend.user.exception.UserNotAdminException;
import akin.backend.user.exception.UserNotFoundException;
import akin.backend.user.repository.AdminRepository;
import akin.backend.user.repository.GarsonRepository;
import akin.backend.user.repository.MutfakRepository;
import akin.backend.user.repository.UserRepository;
import akin.backend.user.repository.WorkDayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final GarsonRepository garsonRepository;
    private final MutfakRepository mutfakRepository;
    private final WorkDayRepository workDayRepository;
    private final PasswordEncoder passwordEncoder;

    private void ensureAdmin(Long userId) {
        adminRepository.findById(userId).orElseThrow(UserNotAdminException::new);
    }

    private Garson getGarsonOrThrow(Long garsonId) {
        return garsonRepository.findById(garsonId).orElseThrow(UserNotFoundException::new);
    }

    private Mutfak getMutfakOrThrow(Long mutfakId) {
        return mutfakRepository.findById(mutfakId).orElseThrow(UserNotFoundException::new);
    }

    @Override
    @Transactional
    public UserResponse createWaiter(Long adminUserId, CreateWaiterRequest request) {
        ensureAdmin(adminUserId);
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateUsernameException();
        }
        Garson garson = new Garson();
        garson.setUsername(request.getUsername());
        garson.setPassword(passwordEncoder.encode(request.getPassword()));
        garson.setFirstName(request.getFirstName());
        garson.setLastName(request.getLastName());
        garson.setEnabled(true);
        garson.setWorkingToday(true);
        garson.setAvailable(false);
        garson = garsonRepository.save(garson);
        log.info("Garson oluşturuldu: {}", garson.getUsername());
        return UserResponse.from(garson);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllGarsons(Long adminUserId) {
        ensureAdmin(adminUserId);
        return garsonRepository.findAllByOrderByUsernameAsc().stream()
                .map(UserResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public void changeGarsonPassword(Long adminUserId, Long garsonId, ChangePasswordRequest request) {
        ensureAdmin(adminUserId);
        Garson garson = getGarsonOrThrow(garsonId);
        garson.setPassword(passwordEncoder.encode(request.getNewPassword()));
        garsonRepository.save(garson);
        log.info("Garson şifresi güncellendi: {}", garson.getUsername());
    }

    @Override
    @Transactional
    public void deleteGarson(Long adminUserId, Long garsonId) {
        ensureAdmin(adminUserId);
        Garson garson = getGarsonOrThrow(garsonId);
        garsonRepository.delete(garson);
        log.info("Garson silindi: {}", garson.getUsername());
    }

    @Override
    @Transactional
    public void setGarsonWorkingToday(Long adminUserId, Long garsonId, SetWorkingTodayRequest request) {
        ensureAdmin(adminUserId);
        Garson garson = getGarsonOrThrow(garsonId);
        garson.setWorkingToday(request.isWorkingToday());
        if (Boolean.TRUE.equals(request.isWorkingToday())) {
            LocalDate today = LocalDate.now();
            if (!workDayRepository.existsByUserIdAndWorkDate(garsonId, today)) {
                WorkDay workDay = WorkDay.builder().user(garson).workDate(today).build();
                workDayRepository.save(workDay);
            }
        }
        garsonRepository.save(garson);
        log.info("Garson bugün çalışıyor: {} -> {}", garson.getUsername(), request.isWorkingToday());
    }

    @Override
    @Transactional
    public UserResponse createMutfak(Long adminUserId, CreateMutfakRequest request) {
        ensureAdmin(adminUserId);
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateUsernameException();
        }
        Mutfak mutfak = new Mutfak();
        mutfak.setUsername(request.getUsername());
        mutfak.setPassword(passwordEncoder.encode(request.getPassword()));
        mutfak.setFirstName(request.getFirstName());
        mutfak.setLastName(request.getLastName());
        mutfak.setEnabled(true);
        mutfak.setWorkingToday(true);
        mutfak = mutfakRepository.save(mutfak);
        log.info("Mutfak hesabı oluşturuldu: {}", mutfak.getUsername());
        return UserResponse.from(mutfak);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllMutfak(Long adminUserId) {
        ensureAdmin(adminUserId);
        return mutfakRepository.findAllByOrderByUsernameAsc().stream()
                .map(UserResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public void changeMutfakPassword(Long adminUserId, Long mutfakId, ChangePasswordRequest request) {
        ensureAdmin(adminUserId);
        Mutfak mutfak = getMutfakOrThrow(mutfakId);
        mutfak.setPassword(passwordEncoder.encode(request.getNewPassword()));
        mutfakRepository.save(mutfak);
        log.info("Mutfak şifresi güncellendi: {}", mutfak.getUsername());
    }

    @Override
    @Transactional
    public void deleteMutfak(Long adminUserId, Long mutfakId) {
        ensureAdmin(adminUserId);
        Mutfak mutfak = getMutfakOrThrow(mutfakId);
        mutfakRepository.delete(mutfak);
        log.info("Mutfak hesabı silindi: {}", mutfak.getUsername());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeStatisticsResponse> getEmployeeStatistics(Long adminUserId) {
        ensureAdmin(adminUserId);
        List<EmployeeStatisticsResponse> result = new ArrayList<>();
        Stream.concat(
                garsonRepository.findAll().stream(),
                mutfakRepository.findAll().stream()
        ).forEach(user -> {
            long workDaysCount = workDayRepository.countByUserId(user.getId());
            result.add(EmployeeStatisticsResponse.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .roles(Set.of(user.getRole()))
                    .workingToday(user.getWorkingToday())
                    .workDaysCount(workDaysCount)
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .build());
        });
        return result;
    }
}
