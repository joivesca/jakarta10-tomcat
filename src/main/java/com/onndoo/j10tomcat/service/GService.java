package com.onndoo.j10tomcat.service;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;


@Transactional
public abstract class GService<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private static  EntityManagerFactory emf;
	protected EntityManager em;
	
	protected abstract Class<T> getEntityClass();

	public void init() {
		if(emf == null) {
			emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
		}
		em = emf.createEntityManager();
	}
	
    public List<T> listAll() {
        try {
            TypedQuery<T> query = em.createQuery("SELECT e FROM " + getEntityClass().getSimpleName() + " e", getEntityClass());
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public void update(T entity) {
        try {
            em.merge(entity);
        } catch (Exception e) {
        }
    }

    public void create(T entity) {
        try {
            em.persist(entity);
        } catch (Exception e) {
        }
    }

    public void delete(T entity) {
        try {
            if (!em.contains(entity)) {
                entity = em.find(getEntityClass(), entity);
            }

            if (entity != null) {
                em.remove(entity);
            } else {
                System.out.println("Entity not found");
            }
        } catch (Exception e) {
            System.out.println("Error deleting "+ getEntityClass().getSimpleName() +" : " + e.toString());
        }
    }

    public Long count() {
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(e) FROM " + getEntityClass().getSimpleName() + " e", Long.class);
            return query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Error counting "+ getEntityClass().getSimpleName() +" : " + e.getLocalizedMessage());
            return null;
        }
    }
    
    public void close() {
    	if(em != null) {
    		em.close();
    	}
    	
    	if(emf != null) {
    		emf.close();
    	}
    }
    
}
