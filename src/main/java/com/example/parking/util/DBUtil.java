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
            Properties prop = new Properties();
            
            try (InputStream input = DBUtil.class.getClassLoader()
                    .getResourceAsStream("application.properties")) {
                
                if (input != null) {
                    prop.load(input);
                    URL = prop.getProperty("db.url");
                    USER = prop.getProperty("db.user");
                    PASSWORD = prop.getProperty("db.password");
                    
                    System.out.println("[DBUtil] Configuración cargada desde application.properties");
                    System.out.println("[DBUtil] URL: " + URL);
                    System.out.println("[DBUtil] USER: " + USER);
                } else {
                    System.err.println("[DBUtil] application.properties NO ENCONTRADO");
                    URL = "jdbc:mysql://localhost:3306/parkingDB";
                    USER = "root";
                    PASSWORD = "";
                }
            } catch (IOException e) {
                System.err.println("[DBUtil] Error al leer application.properties: " + e.getMessage());
                URL = "jdbc:mysql://localhost:3306/parkingDB";
                USER = "root";
                PASSWORD = "";
            }
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("[DBUtil] Driver MySQL cargado correctamente");
            
        } catch (ClassNotFoundException e) {
            System.err.println("[DBUtil] ERROR: Driver de MySQL NO encontrado");
            e.printStackTrace();
            throw new RuntimeException("Error: Driver de MySQL no encontrado", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("[DBUtil] Conexión a base de datos EXITOSA");
            return conn;
        } catch (SQLException e) {
            System.err.println("[DBUtil] ERROR al conectar a la base de datos");
            System.err.println("[DBUtil] URL: " + URL);
            System.err.println("[DBUtil] USER: " + USER);
            System.err.println("[DBUtil] Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            boolean isValid = conn != null && !conn.isClosed();
            System.out.println("[DBUtil] Test de conexión: " + (isValid ? "OK" : "FALLIDO"));
            return isValid;
        } catch (SQLException e) {
            System.err.println("[DBUtil] Test de conexión FALLIDO: " + e.getMessage());
            return false;
        }
    }
}