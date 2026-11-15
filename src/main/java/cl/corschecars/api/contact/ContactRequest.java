package cl.corschecars.api.contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ContactRequest(
        @NotBlank @Size(max = 120) String nombre,
        @NotBlank @Email @Size(max = 254) String email,
        @NotBlank @Size(max = 2000) String mensaje) {
}
