package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@RestControllerAdvice
@Slf4j
public class ExceptionControlHandler {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.parse(LocalDateTime.now().format(formatter));

    /*@ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleValidationException(ValidationException e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }*/

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        String reason = "The required object was not found.";
        log.error(e.getMessage(), e);
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), reason,  e.getMessage(), now);

    }

    /*@ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(Throwable e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }*/

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(ConflictException e) {
        String reason = "For the requested operation the conditions are not met.";
        log.error(e.getMessage(), e);
        return new ErrorResponse(HttpStatus.CONTINUE.value(), reason, e.getMessage(), now);
    }

    /*@ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenException(ForbiddenException e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }*/

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(MethodArgumentTypeMismatchException e) {
        String reason = "Incorrectly made request.";
        log.error(e.getMessage(), e);
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), reason,  e.getMessage(), now);
    }
}