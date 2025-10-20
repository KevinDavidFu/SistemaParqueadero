package com.example.parking.model;

public class Tarifa {
    private int id;
    private String tipoVehiculo; // Carro, Moto, etc.
    private double valorHora;

    public Tarifa() {}

    public Tarifa(int id, String tipoVehiculo, double valorHora) {
        this.id = id;
        this.tipoVehiculo = tipoVehiculo;
        this.valorHora = valorHora;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTipoVehiculo() { return tipoVehiculo; }
    public void setTipoVehiculo(String tipoVehiculo) { this.tipoVehiculo = tipoVehiculo; }

    public double getValorHora() { return valorHora; }
    public void setValorHora(double valorHora) { this.valorHora = valorHora; }

    @Override
    public String toString() {
        return "Tarifa{" +
                "id=" + id +
                ", tipoVehiculo='" + tipoVehiculo + '\'' +
                ", valorHora=" + valorHora +
                '}';
    }
}
