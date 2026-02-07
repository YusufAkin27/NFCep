package akin.backend.user.repository;

import akin.backend.user.entity.Garson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GarsonRepository extends JpaRepository<Garson, Long> {

    Optional<Garson> findByUsername(String username);

    List<Garson> findAllByOrderByUsernameAsc();
}
