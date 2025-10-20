package com.example.parking.main;

import com.example.parking.dao.TarifaDAO;
import com.example.parking.dao.VehiculoDAO;
import com.example.parking.model.Tarifa;
import com.example.parking.model.Vehiculo;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class TestDAO {
    public static void main(String[] args) {
        TarifaDAO tarifaDAO = new TarifaDAO();
        VehiculoDAO vehiculoDAO = new VehiculoDAO();

        try {
            // Probar agregar tarifa
            Tarifa t = new Tarifa();
            t.setTipo("Carro");
            t.setPrecioPorHora(5000.0);
            tarifaDAO.agregarTarifa(t);
            System.out.println("Tarifa agregada exitosamente");

            // Obtener todas las tarifas
            List<Tarifa> tarifas = tarifaDAO.obtenerTarifas();
            System.out.println("Tarifas en BD:");
            for (Tarifa tarifa : tarifas) {
                System.out.println("ID: " + tarifa.getId() + ", Tipo: " + tarifa.getTipo() + 
                                   ", Precio: " + tarifa.getPrecioPorHora());
            }

            // Probar agregar vehículo
            Vehiculo v = new Vehiculo();
            v.setPlaca("ABC123");
            v.setModelo("Toyota Corolla");
            v.setTipo("Carro");
            v.setIngreso(LocalDateTime.now());
            v.setActivo(true);
            boolean agregado = vehiculoDAO.agregarVehiculo(v);
            System.out.println("Vehículo agregado: " + agregado);

            // Obtener todos los vehículos
            List<Vehiculo> vehiculos = vehiculoDAO.obtenerVehiculos();
            System.out.println("Vehículos en BD:");
            for (Vehiculo x : vehiculos) {
                System.out.println("ID: " + x.getId() + ", Placa: " + x.getPlaca() + 
                                   ", Modelo: " + x.getModelo() + ", Tipo: " + x.getTipo() +
                                   ", Ingreso: " + x.getIngreso() + ", Activo: " + x.isActivo());
            }

        } catch (SQLException e) {
            System.err.println("Error en la operación: " + e.getMessage());
            e.printStackTrace();
        }
    }
}