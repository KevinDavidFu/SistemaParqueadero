package com.example.parking.dao;

import com.example.parking.model.Vehiculo;
import com.example.parking.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {

    public boolean agregarVehiculo(Vehiculo vehiculo) throws SQLException {
        String sql = "INSERT INTO Vehiculo(placa, modelo, tipo, ingreso, activo) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, vehiculo.getPlaca());
            ps.setString(2, vehiculo.getModelo());
            ps.setString(3, vehiculo.getTipo());
            ps.setTimestamp(4, Timestamp.valueOf(vehiculo.getIngreso()));
            ps.setBoolean(5, vehiculo.isActivo());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public List<Vehiculo> obtenerVehiculos() throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Vehiculo";
        try (Connection con = DBUtil.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Vehiculo v = new Vehiculo();
                v.setId(rs.getInt("id"));
                v.setPlaca(rs.getString("placa"));
                v.setModelo(rs.getString("modelo"));
                v.setTipo(rs.getString("tipo"));
                v.setIngreso(rs.getTimestamp("ingreso").toLocalDateTime());
                Timestamp salida = rs.getTimestamp("salida");
                if (salida != null) {
                    v.setSalida(salida.toLocalDateTime());
                }
                v.setTotalPagado(rs.getDouble("total_pagado"));
                v.setActivo(rs.getBoolean("activo"));
                lista.add(v);
            }
        }
        return lista;
    }

    public Vehiculo buscarPorPlaca(String placa) throws SQLException {
        String sql = "SELECT * FROM Vehiculo WHERE placa = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, placa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Vehiculo v = new Vehiculo();
                    v.setId(rs.getInt("id"));
                    v.setPlaca(rs.getString("placa"));
                    v.setModelo(rs.getString("modelo"));
                    v.setTipo(rs.getString("tipo"));
                    v.setIngreso(rs.getTimestamp("ingreso").toLocalDateTime());
                    Timestamp salida = rs.getTimestamp("salida");
                    if (salida != null) {
                        v.setSalida(salida.toLocalDateTime());
                    }
                    v.setTotalPagado(rs.getDouble("total_pagado"));
                    v.setActivo(rs.getBoolean("activo"));
                    return v;
                }
            }
        }
        return null;
    }

    public boolean actualizarVehiculo(Vehiculo vehiculo) throws SQLException {
        String sql = "UPDATE Vehiculo SET salida=?, total_pagado=?, activo=? WHERE placa=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, vehiculo.getSalida() != null ? Timestamp.valueOf(vehiculo.getSalida()) : null);
            ps.setDouble(2, vehiculo.getTotalPagado());
            ps.setBoolean(3, vehiculo.isActivo());
            ps.setString(4, vehiculo.getPlaca());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean eliminarVehiculo(String placa) throws SQLException {
        String sql = "DELETE FROM Vehiculo WHERE placa=?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, placa);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }
}