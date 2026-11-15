package cl.corschecars.api.contact;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contactos")
@Tag(name = "Contacto", description = "Recepción de consultas de clientes")
public class ContactController {
    private final ContactService service;

    public ContactController(ContactService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Enviar una consulta de contacto")
    public ResponseEntity<ContactResponse> create(@Valid @RequestBody ContactRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }
}
