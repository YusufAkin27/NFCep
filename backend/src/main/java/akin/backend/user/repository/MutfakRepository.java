package akin.backend.user.repository;

import akin.backend.user.entity.Mutfak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MutfakRepository extends JpaRepository<Mutfak, Long> {

    Optional<Mutfak> findByUsername(String username);

    List<Mutfak> findAllByOrderByUsernameAsc();
}
