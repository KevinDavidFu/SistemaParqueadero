package com.example.parking.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/parkingDB?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Kevin12345*";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("[DBUtil] Driver MySQL cargado correctamente");
        } catch (ClassNotFoundException e) {
            System.err.println("[DBUtil] ERROR: Driver de MySQL NO encontrado");
            e.printStackTrace();
            throw new RuntimeException(e);
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
            e.printStackTrace();
            throw e;
        }
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
