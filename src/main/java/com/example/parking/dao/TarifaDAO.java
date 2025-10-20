package com.example.parking.dao;

import com.example.parking.model.Tarifa;
import com.example.parking.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TarifaDAO {

    // Agregar tarifa
    public boolean agregarTarifa(Tarifa tarifa) {
        String sql = "INSERT INTO Tarifa (tipo, precio_por_hora) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarifa.getTipo());
            stmt.setDouble(2, tarifa.getPrecioPorHora());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtener todas las tarifas
    public List<Tarifa> obtenerTarifas() {
        List<Tarifa> tarifas = new ArrayList<>();
        String sql = "SELECT * FROM Tarifa";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Tarifa t = new Tarifa();
                t.setId(rs.getInt("id"));
                t.setTipo(rs.getString("tipo"));
                t.setPrecioPorHora(rs.getDouble("precio_por_hora"));
                tarifas.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tarifas;
    }

    // Buscar tarifa por tipo
    public Tarifa buscarPorTipo(String tipo) {
        Tarifa t = null;
        String sql = "SELECT * FROM Tarifa WHERE tipo = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    t = new Tarifa();
                    t.setId(rs.getInt("id"));
                    t.setTipo(rs.getString("tipo"));
                    t.setPrecioPorHora(rs.getDouble("precio_por_hora"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }

    // Actualizar tarifa
    public boolean actualizarTarifa(Tarifa tarifa) {
        String sql = "UPDATE Tarifa SET precio_por_hora = ? WHERE tipo = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, tarifa.getPrecioPorHora());
            stmt.setString(2, tarifa.getTipo());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar tarifa por tipo
    public boolean eliminarPorTipo(String tipo) {
        String sql = "DELETE FROM Tarifa WHERE tipo = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipo);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
