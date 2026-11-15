package cl.corschecars.api.common;

import cl.corschecars.api.auth.EmailAlreadyRegisteredException;
import cl.corschecars.api.auth.InvalidCredentialsException;
import cl.corschecars.api.auth.PasswordTooLongException;
import cl.corschecars.api.vehicle.VehicleNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<ProblemDetail> handleEmailAlreadyRegistered(
            EmailAlreadyRegisteredException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
        problem.setTitle("Correo ya registrado");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ProblemDetail> handleInvalidCredentials(InvalidCredentialsException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
        problem.setTitle("Credenciales inválidas");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }

    @ExceptionHandler(PasswordTooLongException.class)
    public ResponseEntity<ProblemDetail> handlePasswordTooLong(PasswordTooLongException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problem.setTitle("Contraseña inválida");
        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(VehicleNotFoundException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problem.setTitle("Vehículo no encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.putIfAbsent(error.getField(), error.getDefaultMessage()));

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Revisa los campos enviados en la solicitud.");
        problem.setTitle("Solicitud inválida");
        problem.setProperty("errors", errors);
        return ResponseEntity.badRequest().body(problem);
    }
}
