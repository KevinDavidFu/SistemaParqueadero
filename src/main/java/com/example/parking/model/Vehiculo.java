package com.example.parking.model;

import java.time.LocalDateTime;

public class Vehiculo {
    private int id;
    private String placa;
    private String modelo;
    private String tipo;
    private LocalDateTime ingreso;
    private LocalDateTime salida;
    private double totalPagado;
    private boolean activo;

    // Constructor vacío
    public Vehiculo() {}

    // Constructor completo
    public Vehiculo(int id, String placa, String modelo, String tipo, LocalDateTime ingreso,
                    LocalDateTime salida, double totalPagado, boolean activo) {
        this.id = id;
        this.placa = placa;
        this.modelo = modelo;
        this.tipo = tipo;
        this.ingreso = ingreso;
        this.salida = salida;
        this.totalPagado = totalPagado;
        this.activo = activo;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDateTime getIngreso() { return ingreso; }
    public void setIngreso(LocalDateTime ingreso) { this.ingreso = ingreso; }

    public LocalDateTime getSalida() { return salida; }
    public void setSalida(LocalDateTime salida) { this.salida = salida; }

    public double getTotalPagado() { return totalPagado; }
    public void setTotalPagado(double totalPagado) { this.totalPagado = totalPagado; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}