package com.onndoo.j10tomcat;

import com.onndoo.j10tomcat.model.Profile;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {

	public static void main(String[] args) {
		
		Profile profile = new Profile();
		profile.setUsername("admin");
		profile.setPassword(null);
		profile.setGroupName("admin");
				
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myPersistenceUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(profile);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();
               
	}
}
