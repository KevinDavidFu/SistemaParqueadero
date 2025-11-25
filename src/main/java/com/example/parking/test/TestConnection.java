package com.example.parking.test;

import java.sql.Connection;
import java.util.List;

import com.example.parking.entity.TarifaEntity;
import com.example.parking.util.DBUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Clase para probar la conexión a la base de datos
 * Ejecutar desde la línea de comandos o desde el IDE
 */
public class TestConnection {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("PRUEBA DE CONEXIÓN - SISTEMA PARQUEADERO");
        System.out.println("========================================");
        System.out.println();
        
        // Test 1: Conexión JDBC básica
        System.out.println("1. Probando conexión JDBC...");
        testJDBCConnection();
        System.out.println();
        
        // Test 2: EntityManagerFactory
        System.out.println("2. Probando EntityManagerFactory...");
        testJPAConnection();
        System.out.println();
        
        // Test 3: Query simple
        System.out.println("3. Probando query a base de datos...");
        testSimpleQuery();
        System.out.println();
        
        System.out.println("========================================");
        System.out.println("PRUEBAS COMPLETADAS");
        System.out.println("========================================");
    }
    
    private static void testJDBCConnection() {
        try {
            Connection conn = DBUtil.getConnection();
            System.out.println("   ✓ Conexión JDBC exitosa");
            System.out.println("   Catálogo: " + conn.getCatalog());
            conn.close();
        } catch (Exception e) {
            System.err.println("   ✗ Error en conexión JDBC");
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testJPAConnection() {
        EntityManagerFactory emf = null;
        try {
            System.out.println("   Creando EntityManagerFactory...");
            emf = Persistence.createEntityManagerFactory("ParkingPU");
            System.out.println("   ✓ EntityManagerFactory creado exitosamente");
            
            EntityManager em = emf.createEntityManager();
            System.out.println("   ✓ EntityManager creado exitosamente");
            em.close();
            
        } catch (Exception e) {
            System.err.println("   ✗ Error al crear EntityManagerFactory");
            System.err.println("   Mensaje: " + e.getMessage());
            System.err.println("   Causa: " + (e.getCause() != null ? e.getCause().getMessage() : "N/A"));
            e.printStackTrace();
        } finally {
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        }
    }
    
    private static void testSimpleQuery() {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory("ParkingPU");
            em = emf.createEntityManager();
            
            System.out.println("   Ejecutando query: SELECT t FROM TarifaEntity t");
            List<TarifaEntity> tarifas = em.createQuery(
                "SELECT t FROM TarifaEntity t", 
                TarifaEntity.class
            ).getResultList();
            
            System.out.println("   ✓ Query exitosa");
            System.out.println("   Tarifas encontradas: " + tarifas.size());
            
            for (TarifaEntity tarifa : tarifas) {
                System.out.println("     - " + tarifa.getTipo() + ": $" + tarifa.getPrecioPorHora());
            }
            
        } catch (Exception e) {
            System.err.println("   ✗ Error al ejecutar query");
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        }
    }
}