package akin.backend.call.service;

import akin.backend.call.dto.request.CreateCallRequest;
import akin.backend.call.dto.response.WaiterCallResponse;
import akin.backend.call.entity.CallStatus;
import akin.backend.call.entity.WaiterCall;
import akin.backend.call.exception.CallNotFoundException;
import akin.backend.call.exception.InvalidCallStatusException;
import akin.backend.call.repository.WaiterCallRepository;
import akin.backend.table.exception.TableNotFoundException;
import akin.backend.table.repository.TableRepository;
import akin.backend.user.entity.Role;
import akin.backend.user.entity.User;
import akin.backend.user.exception.UserNotFoundException;
import akin.backend.user.exception.UserNotAdminException;
import akin.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CallServiceImpl implements CallService {

    private final WaiterCallRepository waiterCallRepository;
    private final TableRepository tableRepository;
    private final UserRepository userRepository;

    private static final Set<CallStatus> PENDING_OR_ACCEPTED = Set.of(CallStatus.PENDING, CallStatus.ACCEPTED);

    @Override
    @Transactional
    public WaiterCallResponse createCall(CreateCallRequest request) {
        if (!tableRepository.existsByTableNumber(request.getTableNumber())) {
            throw new TableNotFoundException();
        }
        WaiterCall call = WaiterCall.builder()
                .tableNumber(request.getTableNumber())
                .tableName(request.getTableName())
                .message(request.getMessage())
                .status(CallStatus.PENDING)
                .build();
        call = waiterCallRepository.save(call);
        log.info("Garson çağrısı oluşturuldu: masa {}", call.getTableNumber());
        return toResponse(call);
    }

    @Override
    @Transactional
    public WaiterCallResponse createCallFromMutfak(Long mutfakUserId, CreateCallRequest request) {
        User user = userRepository.findById(mutfakUserId).orElseThrow(UserNotFoundException::new);
        if (user.getRole() != Role.MUTFAK) {
            throw new UserNotAdminException();
        }
        return createCall(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WaiterCallResponse> getCallsForGarson(Long garsonUserId) {
        User user = userRepository.findById(garsonUserId).orElseThrow(UserNotFoundException::new);
        if (user.getRole() != Role.GARSON) {
            throw new UserNotAdminException();
        }
        List<WaiterCall> pendingOrAccepted = waiterCallRepository.findByStatusInOrderByCreatedAtDesc(PENDING_OR_ACCEPTED);
        return pendingOrAccepted.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public WaiterCallResponse acceptCall(Long garsonUserId, Long callId) {
        User garson = userRepository.findById(garsonUserId).orElseThrow(UserNotFoundException::new);
        if (garson.getRole() != Role.GARSON) {
            throw new UserNotAdminException();
        }
        WaiterCall call = waiterCallRepository.findById(callId).orElseThrow(CallNotFoundException::new);
        if (call.getStatus() != CallStatus.PENDING) {
            throw new InvalidCallStatusException();
        }
        call.setStatus(CallStatus.ACCEPTED);
        call.setAcceptedByUserId(garsonUserId);
        call = waiterCallRepository.save(call);
        log.info("Çağrı kabul edildi: id {} garson {}", callId, garson.getUsername());
        return toResponse(call);
    }

    @Override
    @Transactional
    public WaiterCallResponse completeCall(Long garsonUserId, Long callId) {
        User garson = userRepository.findById(garsonUserId).orElseThrow(UserNotFoundException::new);
        if (garson.getRole() != Role.GARSON) {
            throw new UserNotAdminException();
        }
        WaiterCall call = waiterCallRepository.findById(callId).orElseThrow(CallNotFoundException::new);
        if (call.getStatus() != CallStatus.ACCEPTED || !garsonUserId.equals(call.getAcceptedByUserId())) {
            throw new InvalidCallStatusException();
        }
        call.setStatus(CallStatus.COMPLETED);
        call = waiterCallRepository.save(call);
        log.info("Çağrı tamamlandı: id {}", callId);
        return toResponse(call);
    }

    private WaiterCallResponse toResponse(WaiterCall call) {
        return WaiterCallResponse.builder()
                .id(call.getId())
                .tableNumber(call.getTableNumber())
                .tableName(call.getTableName())
                .message(call.getMessage())
                .status(call.getStatus())
                .acceptedByUserId(call.getAcceptedByUserId())
                .createdAt(call.getCreatedAt())
                .build();
    }
}
