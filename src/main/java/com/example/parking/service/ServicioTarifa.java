package com.example.parking.service;

import com.example.parking.dao.TarifaDAO;
import com.example.parking.model.Tarifa;
import java.util.List;

public class ServicioTarifa {

    private final TarifaDAO tarifaDAO;

    public ServicioTarifa() {
        this.tarifaDAO = new TarifaDAO();
    }

    /**
     * Registra una nueva tarifa en la base de datos.
     */
    public boolean registrarTarifa(Tarifa tarifa) {
        return tarifaDAO.insertar(tarifa);
    }

    /**
     * Actualiza una tarifa existente.
     */
    public boolean actualizarTarifa(Tarifa tarifa) {
        return tarifaDAO.actualizar(tarifa);
    }

    /**
     * Elimina una tarifa según su ID.
     */
    public boolean eliminarTarifa(int idTarifa) {
        return tarifaDAO.eliminar(idTarifa);
    }

    /**
     * Obtiene una tarifa por su ID.
     */
    public Tarifa obtenerTarifaPorId(int idTarifa) {
        return tarifaDAO.obtenerPorId(idTarifa);
    }

    /**
     * Lista todas las tarifas registradas.
     */
    public List<Tarifa> listarTarifas() {
        return tarifaDAO.listarTodas();
    }
}
