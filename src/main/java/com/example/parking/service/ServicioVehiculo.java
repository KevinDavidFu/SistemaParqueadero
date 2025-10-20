package com.example.parking.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import com.example.parking.dao.VehiculoDAO;
import com.example.parking.model.Vehiculo;

public class ServicioVehiculo {

    private final VehiculoDAO vehiculoDAO;

    public ServicioVehiculo() {
        this.vehiculoDAO = new VehiculoDAO();
    }

    // Registrar un nuevo vehículo
    public boolean registrarVehiculo(String placa, String modelo, String tipo) throws SQLException {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(placa);
        vehiculo.setModelo(modelo);
        vehiculo.setTipo(tipo);
        vehiculo.setIngreso(LocalDateTime.now());
        vehiculo.setActivo(true);
        return vehiculoDAO.agregarVehiculo(vehiculo);
    }

    // Listar todos los vehículos
    public List<Vehiculo> listarVehiculos() throws SQLException {
        return vehiculoDAO.obtenerVehiculos();
    }

    // Buscar un vehículo por placa
    public Vehiculo buscarVehiculo(String placa) throws SQLException {
        return vehiculoDAO.buscarPorPlaca(placa);
    }

    // Registrar salida de un vehículo
    public boolean registrarSalida(String placa, double totalPagar) throws SQLException {
        Vehiculo vehiculo = vehiculoDAO.buscarPorPlaca(placa);
        if (vehiculo != null) {
            vehiculo.setSalida(LocalDateTime.now());
            vehiculo.setTotalPagado(totalPagar);
            vehiculo.setActivo(false);
            return vehiculoDAO.actualizarVehiculo(vehiculo);
        }
        return false;
    }

    // Eliminar un vehículo
    public boolean eliminarVehiculo(String placa) throws SQLException {
        return vehiculoDAO.eliminarVehiculo(placa);
    }
}