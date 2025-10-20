package com.example.parking.main;

import com.example.parking.dao.TarifaDAO;
import com.example.parking.dao.VehiculoDAO;
import com.example.parking.model.Tarifa;
import com.example.parking.model.Vehiculo;

import java.time.LocalDateTime;
import java.util.List;

public class TestDAO {
    public static void main(String[] args) {
        // Probar TarifaDAO
        TarifaDAO tarifaDAO = new TarifaDAO();
        Tarifa t = new Tarifa();
        t.setTipo("Carro");
        t.setPrecioPorHora(5000);
        System.out.println("Agregar tarifa: " + tarifaDAO.agregarTarifa(t));

        List<Tarifa> tarifas = tarifaDAO.obtenerTarifas();
        System.out.println("Listado de tarifas:");
        for (Tarifa x : tarifas) {
            System.out.println(x.getId() + " - " + x.getTipo() + " - " + x.getPrecioPorHora());
        }

        // Probar VehiculoDAO
        VehiculoDAO vehiculoDAO = new VehiculoDAO();
        Vehiculo v = new Vehiculo();
        v.setPlaca("ABC123");
        v.setTipo("Carro");
        v.setModelo("Toyota Corolla");
        v.setHoraEntrada(LocalDateTime.now());
        System.out.println("Agregar vehículo: " + vehiculoDAO.agregarVehiculo(v));

        List<Vehiculo> vehiculos = vehiculoDAO.obtenerVehiculos();
        System.out.println("Listado de vehículos:");
        for (Vehiculo x : vehiculos) {
            System.out.println(x.getIdVehiculo() + " - " + x.getPlaca() + " - " + x.getTipo() +
                               " - " + x.getHoraEntrada());
        }
    }
}
