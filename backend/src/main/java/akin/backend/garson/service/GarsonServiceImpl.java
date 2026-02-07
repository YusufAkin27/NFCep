package akin.backend.garson.service;

import akin.backend.call.dto.response.WaiterCallResponse;
import akin.backend.call.service.CallService;
import akin.backend.user.dto.response.UserResponse;
import akin.backend.user.entity.Garson;
import akin.backend.user.entity.Role;
import akin.backend.user.entity.User;
import akin.backend.user.exception.UserNotFoundException;
import akin.backend.user.exception.UserNotAdminException;
import akin.backend.user.repository.GarsonRepository;
import akin.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GarsonServiceImpl implements GarsonService {

    private final UserRepository userRepository;
    private final GarsonRepository garsonRepository;
    private final CallService callService;

    private User ensureGarson(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (user.getRole() != Role.GARSON) {
            throw new UserNotAdminException();
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getMe(Long garsonUserId) {
        User user = ensureGarson(garsonUserId);
        return UserResponse.from(user);
    }

    @Override
    @Transactional
    public UserResponse setAvailable(Long garsonUserId, boolean available) {
        Garson garson = garsonRepository.findById(garsonUserId).orElseThrow(UserNotFoundException::new);
        garson.setAvailable(available);
        garson = garsonRepository.save(garson);
        return UserResponse.from(garson);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WaiterCallResponse> getCalls(Long garsonUserId) {
        return callService.getCallsForGarson(garsonUserId);
    }

    @Override
    @Transactional
    public WaiterCallResponse acceptCall(Long garsonUserId, Long callId) {
        return callService.acceptCall(garsonUserId, callId);
    }

    @Override
    @Transactional
    public WaiterCallResponse completeCall(Long garsonUserId, Long callId) {
        return callService.completeCall(garsonUserId, callId);
    }
}
