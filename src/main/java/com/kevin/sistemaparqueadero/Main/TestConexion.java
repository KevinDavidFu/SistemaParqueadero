package com.kevin.sistemaparqueadero.Main;

import com.kevin.sistemaparqueadero.config.ConexionBD;

import java.sql.Connection;

public class TestConexion {

    public static void main(String[] args) {
        System.out.println("Probando conexión a la base de datos...");

        try (Connection conn = ConexionBD.obtenerConexion()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Conexión exitosa!");
            } else {
                System.out.println("No se pudo establecer la conexión.");
            }
        } catch (Exception e) {
            System.out.println("Ocurrió un error al conectar:");
            e.printStackTrace();
        }
    }
}
