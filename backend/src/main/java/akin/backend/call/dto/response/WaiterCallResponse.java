package akin.backend.call.dto.response;

import akin.backend.call.entity.CallStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaiterCallResponse {

    private Long id;
    private String tableNumber;
    private String tableName;
    private String message;
    private CallStatus status;
    private Long acceptedByUserId;
    private LocalDateTime createdAt;
}
