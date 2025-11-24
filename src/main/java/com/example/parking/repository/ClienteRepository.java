package com.example.parking.repository;

import com.example.parking.entity.ClienteEntity;
import com.example.parking.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Optional;
@SuppressWarnings("ConvertToTryWithResources")
public class ClienteRepository {
    
    public ClienteEntity save(ClienteEntity cliente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (cliente.getId() == null) {
                em.persist(cliente);
            } else {
                cliente = em.merge(cliente);
            }
            em.getTransaction().commit();
            return cliente;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar cliente", e);
        } finally {
            em.close();
        }
    }
    
    public Optional<ClienteEntity> findById(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            ClienteEntity cliente = em.find(ClienteEntity.class, id);
            return Optional.ofNullable(cliente);
        } finally {
            em.close();
        }
    }
    
    @SuppressWarnings("ConvertToTryWithResources")
    public Optional<ClienteEntity> findByDocumento(String documento) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            ClienteEntity cliente = em.createQuery(
                "SELECT c FROM ClienteEntity c WHERE c.documento = :documento", 
                ClienteEntity.class)
                .setParameter("documento", documento)
                .getSingleResult();
            return Optional.of(cliente);
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }
    
    public List<ClienteEntity> findAll() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery("SELECT c FROM ClienteEntity c ORDER BY c.id", ClienteEntity.class)
                    .getResultList();
        }
    }
    
    public void delete(Integer id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            ClienteEntity cliente = em.find(ClienteEntity.class, id);
            if (cliente != null) {
                em.remove(cliente);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar cliente", e);
        } finally {
            em.close();
        }
    }
}
