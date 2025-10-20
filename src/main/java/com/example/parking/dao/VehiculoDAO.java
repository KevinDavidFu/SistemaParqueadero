package com.example.parking.dao;

import com.example.parking.model.Vehiculo;
import com.example.parking.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {

    // Agregar vehículo
    public boolean agregarVehiculo(Vehiculo vehiculo) {
        String sql = "INSERT INTO Vehiculo (placa, modelo, tipo, ingreso) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehiculo.getPlaca());
            stmt.setString(2, vehiculo.getModelo());
            stmt.setString(3, vehiculo.getTipo());
            stmt.setTimestamp(4, Timestamp.valueOf(vehiculo.getIngreso()));
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtener todos los vehículos
    public List<Vehiculo> obtenerVehiculos() {
        List<Vehiculo> vehiculos = new ArrayList<>();
        String sql = "SELECT * FROM Vehiculo";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vehiculo v = new Vehiculo();
                v.setId(rs.getInt("id"));
                v.setPlaca(rs.getString("placa"));
                v.setModelo(rs.getString("modelo"));
                v.setTipo(rs.getString("tipo"));
                v.setIngreso(rs.getTimestamp("ingreso").toLocalDateTime());
                Timestamp salidaTs = rs.getTimestamp("salida");
                v.setSalida(salidaTs != null ? salidaTs.toLocalDateTime() : null);
                v.setTotalPagado(rs.getDouble("total_pagado"));
                v.setActivo(rs.getBoolean("activo"));
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
        String sql = "SELECT * FROM Vehiculo WHERE placa = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, placa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    v = new Vehiculo();
                    v.setId(rs.getInt("id"));
                    v.setPlaca(rs.getString("placa"));
                    v.setModelo(rs.getString("modelo"));
                    v.setTipo(rs.getString("tipo"));
                    v.setIngreso(rs.getTimestamp("ingreso").toLocalDateTime());
                    Timestamp salidaTs = rs.getTimestamp("salida");
                    v.setSalida(salidaTs != null ? salidaTs.toLocalDateTime() : null);
                    v.setTotalPagado(rs.getDouble("total_pagado"));
                    v.setActivo(rs.getBoolean("activo"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return v;
    }

    // Actualizar vehículo
    public boolean actualizarVehiculo(Vehiculo vehiculo) {
        String sql = "UPDATE Vehiculo SET modelo = ?, tipo = ?, ingreso = ?, salida = ?, total_pagado = ?, activo = ? WHERE placa = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehiculo.getModelo());
            stmt.setString(2, vehiculo.getTipo());
            stmt.setTimestamp(3, Timestamp.valueOf(vehiculo.getIngreso()));
            stmt.setTimestamp(4, vehiculo.getSalida() != null ? Timestamp.valueOf(vehiculo.getSalida()) : null);
            stmt.setDouble(5, vehiculo.getTotalPagado());
            stmt.setBoolean(6, vehiculo.isActivo());
            stmt.setString(7, vehiculo.getPlaca());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar vehículo por placa
    public boolean eliminarPorPlaca(String placa) {
        String sql = "DELETE FROM Vehiculo WHERE placa = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, placa);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
