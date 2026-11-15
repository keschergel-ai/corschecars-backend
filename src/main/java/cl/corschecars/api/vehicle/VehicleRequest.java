package cl.corschecars.api.vehicle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record VehicleRequest(
        @NotBlank @Size(max = 160) String nombre,
        @NotNull @PositiveOrZero @Digits(integer = 10, fraction = 2) BigDecimal precio,
        @NotBlank @Size(max = 60) String categoria,
        @NotBlank @Size(max = 1000) String imagen,
        @NotBlank @Size(max = 1000) String descripcion) {
}
