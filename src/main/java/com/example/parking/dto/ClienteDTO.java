package com.example.parking.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto de transferencia de datos para Cliente")
public class ClienteDTO {
    
    @Schema(description = "ID único del cliente", example = "1")
    private Integer id;
    
    @Schema(description = "Nombre completo del cliente", example = "Juan Pérez", required = true)
    private String nombre;
    
    @Schema(description = "Documento de identidad", example = "123456789", required = true)
    private String documento;
    
    @Schema(description = "Teléfono de contacto", example = "3001234567")
    private String telefono;
    
    @Schema(description = "Correo electrónico", example = "juan@example.com")
    private String email;
    
    @Schema(description = "Tipo de cliente", example = "Regular", allowableValues = {"Regular", "VIP", "Eventual"})
    private String tipoCliente;
    
    @Schema(description = "Porcentaje de descuento", example = "10.0")
    private Double descuento;
    
    // Constructores
    public ClienteDTO() {}
    
    public ClienteDTO(Integer id, String nombre, String documento, String telefono, 
                     String email, String tipoCliente, Double descuento) {
        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
        this.telefono = telefono;
        this.email = email;
        this.tipoCliente = tipoCliente;
        this.descuento = descuento;
    }
    
    // Getters y Setters
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
    
    public String getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(String tipoCliente) { this.tipoCliente = tipoCliente; }
    
    public Double getDescuento() { return descuento; }
    public void setDescuento(Double descuento) { this.descuento = descuento; }
}