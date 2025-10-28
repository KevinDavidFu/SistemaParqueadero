package com.example.parking.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto de transferencia de datos para Tarifa")
public class TarifaDTO {
    
    @Schema(description = "ID único de la tarifa", example = "1")
    private Integer id;
    
    @Schema(description = "Tipo de vehículo", example = "Carro", required = true)
    private String tipo;
    
    @Schema(description = "Precio por hora en COP", example = "5000.0", required = true)
    private Double precioPorHora;
    
    @Schema(description = "Estado de la tarifa", example = "true")
    private Boolean activa;
    
    @Schema(description = "Fecha de creación", example = "2025-01-15T10:30:00")
    private String creadoEn;
    
    // Constructores
    public TarifaDTO() {}
    
    public TarifaDTO(Integer id, String tipo, Double precioPorHora, Boolean activa, String creadoEn) {
        this.id = id;
        this.tipo = tipo;
        this.precioPorHora = precioPorHora;
        this.activa = activa;
        this.creadoEn = creadoEn;
    }
    
    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public Double getPrecioPorHora() { return precioPorHora; }
    public void setPrecioPorHora(Double precioPorHora) { this.precioPorHora = precioPorHora; }
    
    public Boolean getActiva() { return activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }
    
    public String getCreadoEn() { return creadoEn; }
    public void setCreadoEn(String creadoEn) { this.creadoEn = creadoEn; }
}