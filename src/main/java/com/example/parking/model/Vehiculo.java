package com.example.parking.model;

import java.time.LocalDateTime;

public class Vehiculo {
    private int id;
    private String placa;
    private String tipo; // Carro, Moto, etc.
    private LocalDateTime fechaEntrada;
    private LocalDateTime fechaSalida;

    public Vehiculo() {}

    public Vehiculo(int id, String placa, String tipo, LocalDateTime fechaEntrada, LocalDateTime fechaSalida) {
        this.id = id;
        this.placa = placa;
        this.tipo = tipo;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDateTime getFechaEntrada() { return fechaEntrada; }
    public void setFechaEntrada(LocalDateTime fechaEntrada) { this.fechaEntrada = fechaEntrada; }

    public LocalDateTime getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(LocalDateTime fechaSalida) { this.fechaSalida = fechaSalida; }

    @Override
    public String toString() {
        return "Vehiculo{" +
                "id=" + id +
                ", placa='" + placa + '\'' +
                ", tipo='" + tipo + '\'' +
                ", fechaEntrada=" + fechaEntrada +
                ", fechaSalida=" + fechaSalida +
                '}';
    }
}
