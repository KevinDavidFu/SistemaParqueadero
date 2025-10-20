package Main;

import config.ConexionBD;
import java.sql.Connection;

public class TestConexion {

    public static void main(String[] args) {
        System.out.println("🔍 Probando conexión a la base de datos...");

        Connection conexion = ConexionBD.getConexion();

        if (conexion != null) {
            System.out.println("✅ Conexión establecida correctamente desde TestConexion.");
        } else {
            System.out.println("❌ No se pudo establecer conexión.");
        }

        ConexionBD.cerrarConexion();
    }
}
