package cl.corschecars.api.contact;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "contact_messages")
public class ContactMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 254)
    private String email;

    @Column(nullable = false, length = 2000)
    private String mensaje;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected ContactMessage() {
    }

    public ContactMessage(String nombre, String email, String mensaje) {
        this.nombre = nombre;
        this.email = email;
        this.mensaje = mensaje;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getMensaje() {
        return mensaje;
    }
}
