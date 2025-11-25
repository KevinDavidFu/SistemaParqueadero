package com.example.parking.repository;

import com.example.parking.entity.VehiculoEntity;
import com.example.parking.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("ConvertToTryWithResources")
public class VehiculoRepository {
    
    public VehiculoEntity save(VehiculoEntity vehiculo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (vehiculo.getId() == null) {
                em.persist(vehiculo);
            } else {
                vehiculo = em.merge(vehiculo);
            }
            em.getTransaction().commit();
            return vehiculo;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar vehículo", e);
        } finally {
            em.close();
        }
    }
    
    // NUEVO: Método update explícito
    public VehiculoEntity update(VehiculoEntity vehiculo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            VehiculoEntity updated = em.merge(vehiculo);
            em.getTransaction().commit();
            return updated;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar vehículo", e);
        } finally {
            em.close();
        }
    }
    
    public Optional<VehiculoEntity> findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            VehiculoEntity vehiculo = em.find(VehiculoEntity.class, id);
            return Optional.ofNullable(vehiculo);
        } finally {
            em.close();
        }
    }
    
    public Optional<VehiculoEntity> findByPlaca(String placa) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            VehiculoEntity vehiculo = em.createQuery(
                "SELECT v FROM VehiculoEntity v WHERE v.placa = :placa", 
                VehiculoEntity.class)
                .setParameter("placa", placa)
                .getSingleResult();
            return Optional.of(vehiculo);
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }
    
    public List<VehiculoEntity> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT v FROM VehiculoEntity v ORDER BY v.id DESC", VehiculoEntity.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<VehiculoEntity> findByActivo(Boolean activo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                "SELECT v FROM VehiculoEntity v WHERE v.activo = :activo ORDER BY v.ingreso DESC", 
                VehiculoEntity.class)
                .setParameter("activo", activo)
                .getResultList();
        } finally {
            em.close();
        }
    }
    
    public void delete(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            VehiculoEntity vehiculo = em.find(VehiculoEntity.class, id);
            if (vehiculo != null) {
                em.remove(vehiculo);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar vehículo", e);
        } finally {
            em.close();
        }
    }
}