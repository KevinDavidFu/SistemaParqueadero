package com.example.parking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal; // <--- ¡IMPORT AÑADIDO!

@Schema(description = "Objeto de transferencia de datos para Cliente")
public class ClienteDTO {
    
    @Schema(description = "ID único del cliente", example = "1")
    private Integer id;
    
    // ... otros campos ...
    
    @Schema(description = "Tipo de cliente", example = "Regular", allowableValues = {"Regular", "VIP", "Eventual"})
    private String tipoCliente;
    
    @Schema(description = "Porcentaje de descuento", example = "10.0")
    private BigDecimal descuento; // <--- CAMBIADO de Double a BigDecimal
    
    // Constructores
    public ClienteDTO() {}
    
    public ClienteDTO(Integer id, String nombre, String documento, String telefono, 
                      String email, String tipoCliente, BigDecimal descuento) { // <--- CAMBIADO en constructor
        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
        this.telefono = telefono;
        this.email = email;
        this.tipoCliente = tipoCliente;
        this.descuento = descuento;
    }
    
    // Getters y Setters
    // ... (Getters y Setters para campos no modificados) ...
    
    public String getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(String tipoCliente) { this.tipoCliente = tipoCliente; }
    
    public BigDecimal getDescuento() { return descuento; } // <--- CAMBIADO
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; } // <--- CAMBIADO
}