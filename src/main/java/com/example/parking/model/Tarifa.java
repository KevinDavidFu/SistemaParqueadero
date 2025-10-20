package com.example.parking.model;

import java.time.LocalDateTime;

public class Tarifa {
    private int id;
    private String tipo;
    private double precioPorHora;
    private LocalDateTime creadoEn;

    public Tarifa() {}

    public Tarifa(int id, String tipo, double precioPorHora, LocalDateTime creadoEn) {
        this.id = id;
        this.tipo = tipo;
        this.precioPorHora = precioPorHora;
        this.creadoEn = creadoEn;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getPrecioPorHora() { return precioPorHora; }
    public void setPrecioPorHora(double precioPorHora) { this.precioPorHora = precioPorHora; }

    public LocalDateTime getCreadoEn() { return creadoEn; }
    public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
}