package akin.backend.call.repository;

import akin.backend.call.entity.CallStatus;
import akin.backend.call.entity.WaiterCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface WaiterCallRepository extends JpaRepository<WaiterCall, Long> {

    List<WaiterCall> findByStatusInOrderByCreatedAtDesc(Set<CallStatus> statuses);

    List<WaiterCall> findByStatusOrderByCreatedAtDesc(CallStatus status);

    List<WaiterCall> findByAcceptedByUserIdOrderByCreatedAtDesc(Long acceptedByUserId);
}
