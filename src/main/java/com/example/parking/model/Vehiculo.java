package com.example.parking.model;

public class Vehiculo {
    private int id;
    private String placa;
    private String tipo;
    private String propietario;
    private String horaEntrada;
    private String horaSalida;

    public Vehiculo() {
    }

    public Vehiculo(int id, String placa, String tipo, String propietario, String horaEntrada, String horaSalida) {
        this.id = id;
        this.placa = placa;
        this.tipo = tipo;
        this.propietario = propietario;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
    }

    public Vehiculo(String placa, String tipo, String propietario, String horaEntrada, String horaSalida) {
        this.placa = placa;
        this.tipo = tipo;
        this.propietario = propietario;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    @Override
    public String toString() {
        return "Vehiculo{" +
                "id=" + id +
                ", placa='" + placa + '\'' +
                ", tipo='" + tipo + '\'' +
                ", propietario='" + propietario + '\'' +
                ", horaEntrada='" + horaEntrada + '\'' +
                ", horaSalida='" + horaSalida + '\'' +
                '}';
    }
}
