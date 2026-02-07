package akin.backend.table.repository;

import akin.backend.table.entity.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<Table, Long> {

    Optional<Table> findByTableNumber(String tableNumber);

    boolean existsByTableNumber(String tableNumber);

    List<Table> findAllByOrderByTableNumberAsc();
}
