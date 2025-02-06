package ru.practicum.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private Integer status;
    private String reason;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(Integer status, String reason, String message, LocalDateTime timestamp) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = timestamp;
    }

}
