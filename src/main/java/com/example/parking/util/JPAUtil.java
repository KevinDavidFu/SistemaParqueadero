package com.example.parking.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    private static final String PERSISTENCE_UNIT_NAME = "ParkingPU";
    private static EntityManagerFactory entityManagerFactory;

    static {
        try {
            System.out.println("[JPAUtil] Iniciando EntityManagerFactory...");
            entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            System.out.println("[JPAUtil] EntityManagerFactory iniciado correctamente");
        } catch (Exception e) {
            System.err.println("[JPAUtil] ERROR al iniciar EntityManagerFactory: " + e.getMessage());
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    public static EntityManager getEntityManager() {
        if (entityManagerFactory == null) {
            throw new IllegalStateException("EntityManagerFactory no ha sido inicializado");
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
}
