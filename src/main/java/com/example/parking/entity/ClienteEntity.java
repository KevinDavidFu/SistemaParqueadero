package com.example.parking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal; // <--- IMPORT AÑADIDO

@Entity
@Table(name = "Cliente")
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 20)
    private String documento;

    @Column(length = 20)
    private String telefono;

    @Column(length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cliente", nullable = false, length = 20)
    private TipoCliente tipoCliente = TipoCliente.Eventual;

    // CAMBIO CLAVE: Se usa BigDecimal para poder usar 'scale' y 'precision'.
    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal descuento = BigDecimal.ZERO; // <--- CORREGIDO: Usando BigDecimal

    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;

    @JsonIgnore
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<VehiculoEntity> vehiculos = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        creadoEn = LocalDateTime.now();
        actualizadoEn = LocalDateTime.now();
        if (descuento == null) descuento = BigDecimal.ZERO; // Ajuste para BigDecimal
        if (tipoCliente == null) tipoCliente = TipoCliente.Eventual;
    }

    @PreUpdate
    protected void onUpdate() {
        actualizadoEn = LocalDateTime.now();
    }

    public enum TipoCliente {
        Regular, VIP, Eventual
    }

    public ClienteEntity() {}

    // -------------------------------------------------------------------------
    // GETTERS Y SETTERS CORREGIDOS
    // -------------------------------------------------------------------------
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public TipoCliente getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(TipoCliente tipoCliente) { this.tipoCliente = tipoCliente; }

    public BigDecimal getDescuento() { return descuento; } // <--- CORREGIDO
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; } // <--- CORREGIDO

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }

    public LocalDateTime getActualizadoEn() { return actualizadoEn; }
    public void setActualizadoEn(LocalDateTime actualizadoEn) { this.actualizadoEn = actualizadoEn; }

    public List<VehiculoEntity> getVehiculos() { return vehiculos; }
    public void setVehiculos(List<VehiculoEntity> vehiculos) { this.vehiculos = vehiculos; }
}