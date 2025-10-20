package com.example.parking.service;

import com.example.parking.dao.TarifaDAO;
import com.example.parking.model.Tarifa;

import java.sql.SQLException;
import java.util.List;

public class ServicioTarifa {

    private final TarifaDAO tarifaDAO;

    public ServicioTarifa() {
        this.tarifaDAO = new TarifaDAO();
    }

    // Agregar una nueva tarifa
    public void agregarTarifa(String tipo, double precioPorHora) throws SQLException {
        Tarifa tarifa = new Tarifa();
        tarifa.setTipo(tipo);
        tarifa.setPrecioPorHora(precioPorHora);
        tarifaDAO.agregarTarifa(tarifa);
    }

    // Listar todas las tarifas
    public List<Tarifa> listarTarifas() throws SQLException {
        return tarifaDAO.obtenerTarifas();
    }

    // Actualizar una tarifa existente
    public void actualizarTarifa(int id, String tipo, double precioPorHora) throws SQLException {
        Tarifa tarifa = new Tarifa();
        tarifa.setId(id);
        tarifa.setTipo(tipo);
        tarifa.setPrecioPorHora(precioPorHora);
        tarifaDAO.actualizarTarifa(tarifa);
    }

    // Eliminar una tarifa
    public void eliminarTarifa(int id) throws SQLException {
        tarifaDAO.eliminarTarifa(id);
    }

    // Obtener el precio por hora según el tipo de vehículo
    public double obtenerPrecioPorTipo(String tipo) throws SQLException {
        List<Tarifa> tarifas = tarifaDAO.obtenerTarifas();
        for (Tarifa t : tarifas) {
            if (t.getTipo().equalsIgnoreCase(tipo)) {
                return t.getPrecioPorHora();
            }
        }
        return 0.0; // Si no encuentra el tipo
    }
}