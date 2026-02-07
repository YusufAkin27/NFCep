package akin.backend.user.repository;

import akin.backend.user.entity.WorkDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface WorkDayRepository extends JpaRepository<WorkDay, Long> {

    Optional<WorkDay> findByUserIdAndWorkDate(Long userId, LocalDate workDate);

    long countByUserId(Long userId);

    boolean existsByUserIdAndWorkDate(Long userId, LocalDate workDate);
}
