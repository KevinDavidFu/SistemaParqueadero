package com.example.parking.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    private static final String PERSISTENCE_UNIT_NAME = "ParkingPU";
    private static EntityManagerFactory entityManagerFactory;
    private static Exception initializationError;

    static {
        try {
            System.out.println("===========================================");
            System.out.println("[JPAUtil] Iniciando EntityManagerFactory...");
            System.out.println("[JPAUtil] Persistence Unit: " + PERSISTENCE_UNIT_NAME);
            
            // IMPORTANTE: NO usar propiedades adicionales aquí
            // Todas las propiedades deben estar en persistence.xml
            entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            
            System.out.println("[JPAUtil] ✓ EntityManagerFactory iniciado EXITOSAMENTE");
            System.out.println("===========================================");
            
            // Test de conexión
            testConnection();
            
        } catch (Exception e) {
            initializationError = e;
            System.err.println("===========================================");
            System.err.println("[JPAUtil] ✗ ERROR CRÍTICO al iniciar EntityManagerFactory");
            System.err.println("[JPAUtil] Mensaje: " + e.getMessage());
            
            // Imprimir stack trace completo para debug
            System.err.println("[JPAUtil] Stack Trace:");
            e.printStackTrace();
            
            // Si hay causa raíz, mostrarla
            Throwable cause = e.getCause();
            while (cause != null) {
                System.err.println("[JPAUtil] Causa: " + cause.getMessage());
                cause = cause.getCause();
            }
            
            System.err.println("===========================================");
            System.err.println("[JPAUtil] VERIFICAR:");
            System.err.println("  1. MySQL está corriendo: net start MySQL95");
            System.err.println("  2. La base de datos 'parkingDB' existe");
            System.err.println("  3. Usuario: root / Password: Kevin12345*");
            System.err.println("  4. El puerto 3306 está disponible");
            System.err.println("  5. Ejecutar: mysql -u root -pKevin12345* -e 'USE parkingDB; SHOW TABLES;'");
            System.err.println("===========================================");
            
            // NO lanzar excepción aquí para permitir que el servidor inicie
            // La excepción se lanzará cuando se intente usar getEntityManager()
        }
    }

    private static void testConnection() {
        if (entityManagerFactory != null) {
            EntityManager em = null;
            try {
                em = entityManagerFactory.createEntityManager();
                em.getTransaction().begin();
                em.getTransaction().commit();
                System.out.println("[JPAUtil] ✓ Test de conexión a BD exitoso");
            } catch (Exception e) {
                System.err.println("[JPAUtil] ✗ Test de conexión FALLÓ: " + e.getMessage());
            } finally {
                if (em != null && em.isOpen()) {
                    em.close();
                }
            }
        }
    }

    public static EntityManager getEntityManager() {
        if (initializationError != null) {
            throw new IllegalStateException(
                "EntityManagerFactory no pudo ser inicializado. Error: " + 
                initializationError.getMessage(), 
                initializationError
            );
        }
        
        if (entityManagerFactory == null) {
            throw new IllegalStateException("EntityManagerFactory no ha sido inicializado");
        }
        
        if (!entityManagerFactory.isOpen()) {
            throw new IllegalStateException("EntityManagerFactory está cerrado");
        }
        
        return entityManagerFactory.createEntityManager();
    }

    public static void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            System.out.println("[JPAUtil] Cerrando EntityManagerFactory...");
            entityManagerFactory.close();
            System.out.println("[JPAUtil] EntityManagerFactory cerrado");
        }
    }

    public static boolean isOpen() {
        return entityManagerFactory != null && entityManagerFactory.isOpen();
    }
    
    public static boolean isInitialized() {
        return entityManagerFactory != null && initializationError == null;
    }
    
    public static Exception getInitializationError() {
        return initializationError;
    }
}