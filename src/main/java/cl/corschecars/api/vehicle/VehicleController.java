package cl.corschecars.api.vehicle;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/vehiculos")
@Tag(name = "Vehículos", description = "Operaciones CRUD del catálogo de vehículos")
public class VehicleController {
    private final VehicleService service;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar vehículos")
    public List<VehicleResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un vehículo por su id")
    public VehicleResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @Operation(summary = "Crear un vehículo")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<VehicleResponse> create(@Valid @RequestBody VehicleRequest request) {
        VehicleResponse created = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un vehículo")
    @SecurityRequirement(name = "bearerAuth")
    public VehicleResponse update(@PathVariable Long id, @Valid @RequestBody VehicleRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un vehículo")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
