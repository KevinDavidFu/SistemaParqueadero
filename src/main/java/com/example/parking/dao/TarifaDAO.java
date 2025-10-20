package com.example.parking.dao;

import com.example.parking.model.Tarifa;
import com.example.parking.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TarifaDAO {

    public void agregarTarifa(Tarifa tarifa) throws SQLException {
        String sql = "INSERT INTO Tarifa(tipo, precio_por_hora) VALUES (?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tarifa.getTipo());
            ps.setDouble(2, tarifa.getPrecioPorHora());
            ps.executeUpdate();
        }
    }

    public List<Tarifa> obtenerTarifas() throws SQLException {
        List<Tarifa> lista = new ArrayList<>();
        String sql = "SELECT * FROM Tarifa";
        try (Connection con = DBUtil.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Tarifa t = new Tarifa();
                t.setId(rs.getInt("id"));
                t.setTipo(rs.getString("tipo"));
                t.setPrecioPorHora(rs.getDouble("precio_por_hora"));
                Timestamp creado = rs.getTimestamp("creado_en");
                if (creado != null) {
                    t.setCreadoEn(creado.toLocalDateTime());
                }
                lista.add(t);
            }
        }
        return lista;
    }

    public void actualizarTarifa(Tarifa tarifa) throws SQLException {
        String sql = "UPDATE Tarifa SET tipo=?, precio_por_hora=? WHERE id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tarifa.getTipo());
            ps.setDouble(2, tarifa.getPrecioPorHora());
            ps.setInt(3, tarifa.getId());
            ps.executeUpdate();
        }
    }

    public void eliminarTarifa(int id) throws SQLException {
        String sql = "DELETE FROM Tarifa WHERE id=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}