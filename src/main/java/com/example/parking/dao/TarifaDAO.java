package com.example.parking.dao;

import com.example.parking.model.Tarifa;
import com.example.parking.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TarifaDAO {

    public void insertar(Tarifa tarifa) {
        String sql = "INSERT INTO tarifa (tipoVehiculo, valorHora) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarifa.getTipoVehiculo());
            stmt.setDouble(2, tarifa.getValorHora());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Tarifa obtenerPorId(int id) {
        String sql = "SELECT * FROM tarifa WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Tarifa(
                        rs.getInt("id"),
                        rs.getString("tipoVehiculo"),
                        rs.getDouble("valorHora")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Tarifa> listarTodos() {
        List<Tarifa> lista = new ArrayList<>();
        String sql = "SELECT * FROM tarifa";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Tarifa t = new Tarifa(
                        rs.getInt("id"),
                        rs.getString("tipoVehiculo"),
                        rs.getDouble("valorHora")
                );
                lista.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void actualizar(Tarifa tarifa) {
        String sql = "UPDATE tarifa SET tipoVehiculo=?, valorHora=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarifa.getTipoVehiculo());
            stmt.setDouble(2, tarifa.getValorHora());
            stmt.setInt(3, tarifa.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM tarifa WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
