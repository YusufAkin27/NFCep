package akin.backend.call.service;

import akin.backend.call.dto.request.CreateCallRequest;
import akin.backend.call.dto.response.WaiterCallResponse;

import java.util.List;

public interface CallService {

    WaiterCallResponse createCall(CreateCallRequest request);

    WaiterCallResponse createCallFromMutfak(Long mutfakUserId, CreateCallRequest request);

    List<WaiterCallResponse> getCallsForGarson(Long garsonUserId);

    WaiterCallResponse acceptCall(Long garsonUserId, Long callId);

    WaiterCallResponse completeCall(Long garsonUserId, Long callId);
}
