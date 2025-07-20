package pl.odavydiuk.atiperagithub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.odavydiuk.atiperagithub.dto.ApiErrorDTO;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourseNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleGithubUserNotFoundException(ResourseNotFoundException ex) {
        return new ResponseEntity<>(
                new ApiErrorDTO(HttpStatus.NOT_FOUND.value(), ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }
}
