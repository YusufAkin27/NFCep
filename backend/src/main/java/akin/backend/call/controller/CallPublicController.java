package akin.backend.call.controller;

import akin.backend.call.dto.request.CreateCallRequest;
import akin.backend.call.dto.response.WaiterCallResponse;
import akin.backend.call.service.CallService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calls")
@RequiredArgsConstructor
public class CallPublicController {

    private final CallService callService;

    @PostMapping
    public ResponseEntity<WaiterCallResponse> createCall(@RequestBody @Valid CreateCallRequest request) {
        return ResponseEntity.ok(callService.createCall(request));
    }
}
