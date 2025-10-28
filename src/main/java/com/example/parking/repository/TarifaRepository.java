package com.example.parking.repository;

import com.example.parking.entity.TarifaEntity;
import com.example.parking.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class TarifaRepository {
    
    public TarifaEntity save(TarifaEntity tarifa) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (tarifa.getId() == null) {
                em.persist(tarifa);
            } else {
                tarifa = em.merge(tarifa);
            }
            em.getTransaction().commit();
            return tarifa;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    public Optional<TarifaEntity> findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TarifaEntity tarifa = em.find(TarifaEntity.class, id);
            return Optional.ofNullable(tarifa);
        } finally {
            em.close();
        }
    }
    
    public Optional<TarifaEntity> findByTipo(String tipo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TarifaEntity tarifa = em.createQuery(
                "SELECT t FROM TarifaEntity t WHERE t.tipo = :tipo", 
                TarifaEntity.class)
                .setParameter("tipo", tipo)
                .getSingleResult();
            return Optional.of(tarifa);
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }
    
    public List<TarifaEntity> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT t FROM TarifaEntity t", TarifaEntity.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<TarifaEntity> findByActiva(Boolean activa) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                "SELECT t FROM TarifaEntity t WHERE t.activa = :activa", 
                TarifaEntity.class)
                .setParameter("activa", activa)
                .getResultList();
        } finally {
            em.close();
        }
    }
    
    public void delete(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            TarifaEntity tarifa = em.find(TarifaEntity.class, id);
            if (tarifa != null) {
                em.remove(tarifa);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}