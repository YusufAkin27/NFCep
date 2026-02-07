package akin.backend.garson.service;

import akin.backend.call.dto.response.WaiterCallResponse;
import akin.backend.user.dto.response.UserResponse;

import java.util.List;

public interface GarsonService {

    UserResponse getMe(Long garsonUserId);

    UserResponse setAvailable(Long garsonUserId, boolean available);

    List<WaiterCallResponse> getCalls(Long garsonUserId);

    WaiterCallResponse acceptCall(Long garsonUserId, Long callId);

    WaiterCallResponse completeCall(Long garsonUserId, Long callId);
}
