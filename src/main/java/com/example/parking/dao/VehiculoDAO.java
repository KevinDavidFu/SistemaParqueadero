package com.example.parking.dao;

import com.example.parking.model.Vehiculo;
import com.example.parking.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {

    public void insertar(Vehiculo vehiculo) {
        String sql = "INSERT INTO vehiculo (placa, tipo, fechaEntrada, fechaSalida) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehiculo.getPlaca());
            stmt.setString(2, vehiculo.getTipo());
            stmt.setTimestamp(3, Timestamp.valueOf(vehiculo.getFechaEntrada()));
            stmt.setTimestamp(4, vehiculo.getFechaSalida() != null ? Timestamp.valueOf(vehiculo.getFechaSalida()) : null);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Vehiculo obtenerPorId(int id) {
        String sql = "SELECT * FROM vehiculo WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Vehiculo(
                        rs.getInt("id"),
                        rs.getString("placa"),
                        rs.getString("tipo"),
                        rs.getTimestamp("fechaEntrada").toLocalDateTime(),
                        rs.getTimestamp("fechaSalida") != null ? rs.getTimestamp("fechaSalida").toLocalDateTime() : null
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Vehiculo> listarTodos() {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM vehiculo";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Vehiculo v = new Vehiculo(
                        rs.getInt("id"),
                        rs.getString("placa"),
                        rs.getString("tipo"),
                        rs.getTimestamp("fechaEntrada").toLocalDateTime(),
                        rs.getTimestamp("fechaSalida") != null ? rs.getTimestamp("fechaSalida").toLocalDateTime() : null
                );
                lista.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void actualizar(Vehiculo vehiculo) {
        String sql = "UPDATE vehiculo SET placa=?, tipo=?, fechaEntrada=?, fechaSalida=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehiculo.getPlaca());
            stmt.setString(2, vehiculo.getTipo());
            stmt.setTimestamp(3, Timestamp.valueOf(vehiculo.getFechaEntrada()));
            stmt.setTimestamp(4, vehiculo.getFechaSalida() != null ? Timestamp.valueOf(vehiculo.getFechaSalida()) : null);
            stmt.setInt(5, vehiculo.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM vehiculo WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
