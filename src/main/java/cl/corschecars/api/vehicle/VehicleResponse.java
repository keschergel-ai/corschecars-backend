package cl.corschecars.api.vehicle;

import java.math.BigDecimal;

public record VehicleResponse(
        Long id,
        String nombre,
        BigDecimal precio,
        String categoria,
        String imagen,
        String descripcion) {
    public static VehicleResponse from(Vehicle vehicle) {
        return new VehicleResponse(
                vehicle.getId(),
                vehicle.getNombre(),
                vehicle.getPrecio(),
                vehicle.getCategoria(),
                vehicle.getImagen(),
                vehicle.getDescripcion());
    }
}
