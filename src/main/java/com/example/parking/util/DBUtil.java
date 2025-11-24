package com.example.parking.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@SuppressWarnings("CallToPrintStackTrace")
public class DBUtil {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        String urlTemp = null;
        String userTemp = null;
        String passwordTemp = null;

        try {
            Properties prop = new Properties();

            try (InputStream input = DBUtil.class.getClassLoader()
                    .getResourceAsStream("application.properties")) {

                if (input != null) {
                    prop.load(input);
                    urlTemp = prop.getProperty("db.url");
                    userTemp = prop.getProperty("db.user");
                    passwordTemp = prop.getProperty("db.password");

                    System.out.println("[DBUtil] Configuración cargada desde application.properties");
                } else {
                    System.err.println("[DBUtil] application.properties NO ENCONTRADO");
                    urlTemp = "jdbc:mysql://localhost:3306/parkingDB";
                    userTemp = "root";
                    passwordTemp = "";
                }

            } catch (IOException e) {
                System.err.println("[DBUtil] Error al leer application.properties: " + e.getMessage());
                urlTemp = "jdbc:mysql://localhost:3306/parkingDB";
                userTemp = "root";
                passwordTemp = "";
            }

            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("[DBUtil] Driver MySQL cargado correctamente");

        } catch (ClassNotFoundException e) {
            System.err.println("[DBUtil] ERROR: Driver de MySQL NO encontrado");
            e.printStackTrace();
            throw new RuntimeException("Error: Driver de MySQL no encontrado", e);
        }

        URL = urlTemp;
        USER = userTemp;
        PASSWORD = passwordTemp;
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
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
