package com.example.parking.dao;

import com.example.parking.model.Vehiculo;
import com.example.parking.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {

    // Agregar vehículo
    public boolean agregarVehiculo(Vehiculo vehiculo) {
        String sql = "INSERT INTO vehiculo (placa, tipo, hora_entrada) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehiculo.getPlaca());
            stmt.setString(2, vehiculo.getTipo());
            stmt.setTimestamp(3, Timestamp.valueOf(vehiculo.getHoraEntrada()));
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtener todos los vehículos
    public List<Vehiculo> obtenerVehiculos() {
        List<Vehiculo> vehiculos = new ArrayList<>();
        String sql = "SELECT * FROM vehiculo";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vehiculo v = new Vehiculo();
                v.setIdVehiculo(rs.getInt("id_vehiculo"));
                v.setPlaca(rs.getString("placa"));
                v.setTipo(rs.getString("tipo"));
                v.setHoraEntrada(rs.getTimestamp("hora_entrada").toLocalDateTime());
                v.setHoraSalida(rs.getTimestamp("hora_salida") != null
                        ? rs.getTimestamp("hora_salida").toLocalDateTime()
                        : null);
                v.setTotalPagar(rs.getDouble("total_pagar"));
                vehiculos.add(v);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehiculos;
    }

    // Buscar vehículo por placa
    public Vehiculo buscarPorPlaca(String placa) {
        Vehiculo v = null;
        String sql = "SELECT * FROM vehiculo WHERE placa = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, placa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    v = new Vehiculo();
                    v.setIdVehiculo(rs.getInt("id_vehiculo"));
                    v.setPlaca(rs.getString("placa"));
                    v.setTipo(rs.getString("tipo"));
                    v.setHoraEntrada(rs.getTimestamp("hora_entrada").toLocalDateTime());
                    v.setHoraSalida(rs.getTimestamp("hora_salida") != null
                            ? rs.getTimestamp("hora_salida").toL_
