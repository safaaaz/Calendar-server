package calendar.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(new ApiError(ex.getMessage(), LocalDateTime.now()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ApiError> handleEventNotFound(EventNotFoundException ex) {
        return new ResponseEntity<>(new ApiError(ex.getMessage(), LocalDateTime.now()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissingEventFieldException.class)
    public ResponseEntity<ApiError> handleMissingField(MissingEventFieldException ex) {
        return new ResponseEntity<>(new ApiError(ex.getMessage(), LocalDateTime.now()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PastDateException.class)
    public ResponseEntity<ApiError> handlePastDate(MissingEventFieldException ex) {
        return new ResponseEntity<>(new ApiError(ex.getMessage(), LocalDateTime.now()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidEventDurationException.class)
    public ResponseEntity<ApiError> handleDuration(MissingEventFieldException ex) {
        return new ResponseEntity<>(new ApiError(ex.getMessage(), LocalDateTime.now()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyRegistered.class)
    public ResponseEntity<ApiError> handleUserAlreadyRegistered(UserAlreadyRegistered ex) {
        return new ResponseEntity<>(new ApiError(ex.getMessage(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenNotFound.class)
    public ResponseEntity<ApiError> handleTokenNotFound(TokenNotFound ex) {
        return new ResponseEntity<>(new ApiError(ex.getMessage(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserAlreadyHaveRoleException.class)
    public ResponseEntity<ApiError> handleUserAlreadyHaveRole(UserAlreadyHaveRoleException ex) {
        return new ResponseEntity<>(new ApiError(ex.getMessage(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ApiError> handleInvalidOperation(InvalidOperationException ex) {
        return new ResponseEntity<>(new ApiError(ex.getMessage(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        return new ResponseEntity<>(new ApiError(ex.getMessage(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }
}
