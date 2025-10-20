package com.example.parking.service;

import com.example.parking.dao.VehiculoDAO;
import com.example.parking.model.Vehiculo;

import java.time.LocalDateTime;
import java.util.List;

public class ServicioVehiculo {

    private final VehiculoDAO vehiculoDAO;

    public ServicioVehiculo() {
        this.vehiculoDAO = new VehiculoDAO();
    }

    // Registrar un nuevo vehículo
    public boolean registrarVehiculo(String placa, String tipo) {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(placa);
        vehiculo.setTipo(tipo);
        vehiculo.setHoraEntrada(LocalDateTime.now());
        return vehiculoDAO.agregarVehiculo(vehiculo);
    }

    // Listar todos los vehículos
    public List<Vehiculo> listarVehiculos() {
        return vehiculoDAO.obtenerVehiculos();
    }

    // Buscar un vehículo por placa
    public Vehiculo buscarVehiculo(String placa) {
        return vehiculoDAO.buscarPorPlaca(placa);
    }

    // Registrar salida de un vehículo
    public boolean registrarSalida(String placa, double totalPagar) {
        Vehiculo vehiculo = vehiculoDAO.buscarPorPlaca(placa);
        if (vehiculo != null) {
            vehiculo.setHoraSalida(LocalDateTime.now());
            vehiculo.setTotalPagar(totalPagar);
            return vehiculoDAO.actualizarVehiculo(vehiculo);
        }
        return false;
    }

    // Eliminar un vehículo
    public boolean eliminarVehiculo(String placa) {
        return vehiculoDAO.eliminarVehiculo(placa);
    }
}
