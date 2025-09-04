package co.com.bancolombia.api.exception.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class ErrorResponse {
    private String errorCode;
    private String title;
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private Object errors;
}
