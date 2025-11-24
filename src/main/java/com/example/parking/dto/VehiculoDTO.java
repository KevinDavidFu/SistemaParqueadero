package com.example.parking.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto de transferencia de datos para Vehículo")
public class VehiculoDTO {
    
    @Schema(description = "ID único del vehículo", example = "1")
    private int id;
    
    @Schema(description = "Placa del vehículo", example = "ABC123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String placa;
    
    @Schema(description = "Modelo del vehículo", example = "Toyota Corolla 2020")
    private String modelo;
    
    @Schema(description = "Tipo de vehículo", example = "Carro", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipo;
    
    @Schema(description = "Fecha y hora de ingreso", example = "2025-01-15T10:30:00")
    private String ingreso;
    
    @Schema(description = "Fecha y hora de salida", example = "2025-01-15T14:30:00")
    private String salida;
    
    @Schema(description = "Total pagado por el servicio", example = "15000.00")
    private double totalPagado;
    
    @Schema(description = "Estado del vehículo en el parqueadero", example = "true")
    private boolean activo;

    public VehiculoDTO() {}

    public VehiculoDTO(int id, String placa, String modelo, String tipo, String ingreso, 
                       String salida, double totalPagado, boolean activo) {
        this.id = id;
        this.placa = placa;
        this.modelo = modelo;
        this.tipo = tipo;
        this.ingreso = ingreso;
        this.salida = salida;
        this.totalPagado = totalPagado;
        this.activo = activo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getIngreso() { return ingreso; }
    public void setIngreso(String ingreso) { this.ingreso = ingreso; }

    public String getSalida() { return salida; }
    public void setSalida(String salida) { this.salida = salida; }

    public double getTotalPagado() { return totalPagado; }
    public void setTotalPagado(double totalPagado) { this.totalPagado = totalPagado; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
