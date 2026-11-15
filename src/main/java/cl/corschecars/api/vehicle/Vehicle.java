package cl.corschecars.api.vehicle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 160)
    private String nombre;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false, length = 60)
    private String categoria;

    @Column(nullable = false, length = 1000)
    private String imagen;

    @Column(nullable = false, length = 1000)
    private String descripcion;

    protected Vehicle() {
    }

    public Vehicle(String nombre, BigDecimal precio, String categoria, String imagen, String descripcion) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.imagen = imagen;
        this.descripcion = descripcion;
    }

    public void update(String nombre, BigDecimal precio, String categoria, String imagen, String descripcion) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.imagen = imagen;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getImagen() {
        return imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
