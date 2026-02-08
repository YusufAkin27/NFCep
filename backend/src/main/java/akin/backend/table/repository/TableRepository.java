package akin.backend.table.repository;

import akin.backend.table.entity.Masa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<Masa, Long> {

    Optional<Masa> findByTableNumber(String tableNumber);

    boolean existsByTableNumber(String tableNumber);

    List<Masa> findAllByOrderByTableNumberAsc();
}
