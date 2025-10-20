package com.example.parking.dao;

import com.example.parking.model.Tarifa;
import com.example.parking.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar las operaciones CRUD de las tarifas.
 */
public class TarifaDAO {

    public void agregarTarifa(Tarifa tarifa) {
        String sql = "INSERT INTO tarifa (tipo_vehiculo, valor_hora) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tarifa.getTipoVehiculo());
            stmt.setDouble(2, tarifa.getValorHora());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al agregar tarifa: " + e.getMessage());
        }
    }

    public List<Tarifa> obtenerTarifas() {
        List<Tarifa> lista = new ArrayList<>();
        String sql = "SELECT * FROM tarifa";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Tarifa t = new Tarifa(
                    rs.getInt("id"),
                    rs.getString("tipo_vehiculo"),
                    rs.getDouble("valor_hora")
                );
                lista.add(t);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener tarifas: " + e.getMessage());
        }
        return lista;
    }

    public Tarifa obtenerTarifaPorId(int id) {
        String sql = "SELECT * FROM tarifa WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Tarifa(
                    rs.getInt("id"),
                    rs.getString("tipo_vehiculo"),
                    rs.getDouble("valor_hora")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener tarifa: " + e.getMessage());
        }
        return null;
    }

    public void actualizarTarifa(Tarifa tarifa) {
        String sql = "UPDATE tarifa SET tipo_vehiculo = ?, valor_hora = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarifa.getTipoVehiculo());
            stmt.setDouble(2, tarifa.getValorHora());
            stmt.setInt(3, tarifa.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar tarifa: " + e.getMessage());
        }
    }

    public void eliminarTarifa(int id) {
        String sql = "DELETE FROM tarifa WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar tarifa: " + e.getMessage());
        }
    }
}
