package com.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    // Configuración de la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/parkingdb?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "KevDa#120038#_";
    // Cargar el driver de MySQL
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error cargando el driver de MySQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para obtener la conexión
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Método para cerrar la conexión
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error cerrando la conexión: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Métodos para cerrar otros recursos
    public static void closeStatement(java.sql.Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Error cerrando el Statement: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void closeResultSet(java.sql.ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error cerrando el ResultSet: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
