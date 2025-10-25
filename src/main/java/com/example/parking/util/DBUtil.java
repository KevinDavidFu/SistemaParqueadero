package com.example.parking.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
        try {
            // Intentar cargar desde archivo de propiedades
            try (InputStream input = DBUtil.class.getClassLoader()
                    .getResourceAsStream("application.properties")) {
                if (input != null) {
                    Properties prop = new Properties();
                    prop.load(input);
                    URL = prop.getProperty("db.url", "jdbc:mysql://localhost:3306/parkingDB");
                    USER = prop.getProperty("db.user", "root");
                    PASSWORD = prop.getProperty("db.password", "");
                } else {
                    // Valores por defecto
                    URL = "jdbc:mysql://localhost:3306/parkingDB";
                    USER = "root";
                    PASSWORD = ""; // CAMBIA ESTO por tu contraseña
                }
            } catch (IOException e) {
                // Si falla la lectura del archivo, usar valores por defecto
                System.err.println("No se pudo cargar application.properties, usando valores por defecto");
                URL = "jdbc:mysql://localhost:3306/parkingDB";
                USER = "root";
                PASSWORD = "";
            }
            
            // Cargar driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error: Driver de MySQL no encontrado", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}