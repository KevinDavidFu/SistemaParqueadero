package com.example.parking.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

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
                    PASSWORD = "KevDa#120038#_";
                }
            }
            
            // Cargar driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            throw new RuntimeException("Error al inicializar configuración de BD", e);
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