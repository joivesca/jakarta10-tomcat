package com.onndoo.j10tomcat.service;

import java.util.List;

import com.onndoo.j10tomcat.model.Profile;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.TypedQuery;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import jakarta.transaction.Transactional;

@Named
@ApplicationScoped
@Transactional
public class Service extends GService<Profile> {

	private static final long serialVersionUID = 1L;

	@Inject
	Pbkdf2PasswordHash passwordHasher;

	@Override
	protected Class<Profile> getEntityClass() {
		return Profile.class;
	}

	@PostConstruct
	public void init() {
		super.init();
		inserAdmin();
	}

	public void inserAdmin() {		
		try {
			this.em.getTransaction().begin();
			String username = "admin2";

			TypedQuery<Profile> query = em.createQuery("SELECT u FROM Profile u WHERE u.username = :username", Profile.class);
			query.setParameter("username", username);
			List<Profile> existingUsers = query.getResultList();

			if (existingUsers.isEmpty()) {
				Profile profile = new Profile();
				profile.setUsername(username);
				profile.setPassword(passwordHasher.generate("password123".toCharArray()));
				profile.setGroupName("admin");

				em.persist(profile);
				this.em.getTransaction().commit();;
				System.out.println("Default Admin Saved!");
			} else {
				System.out.println("User already exists");
				this.em.getTransaction().rollback();;
			}
		} catch (IllegalStateException | SecurityException e) {
			System.out.println("Error in InsertAdmin! Error: " + e.toString());
		}
	}
	
	@Override
    public void create(Profile entity) {
        try {
        	this.em.getTransaction().begin();
            var unHashedPassword = entity.getPassword();
            var HashedPassword = passwordHasher.generate(unHashedPassword.toCharArray());
            entity.setPassword(HashedPassword);
            em.persist(entity);
            this.em.getTransaction().commit();
        } catch (Exception e) {
        	this.em.getTransaction().rollback();
        }
    }
    
    @Override
    public void delete(Profile entity) {
        try {
            if (!em.contains(entity)) {
                entity = em.find(getEntityClass(), entity.getId());
            }

            if (entity != null) {
            	this.em.getTransaction().begin();
                em.remove(entity);
                this.em.getTransaction().commit();
            } else {
                System.out.println("Entity not found");
            }
        } catch (Exception e) {
        	this.em.getTransaction().rollback();
            System.out.println("Error deleting "+ getEntityClass().getSimpleName() +" : " + e.toString());
        }
    }

    public boolean verifyPassword(char[] password, String hashedPassword){    	
        return passwordHasher.verify(password, hashedPassword);
    }
        
    public Profile getSession(String username) {
        try {
            TypedQuery<Profile> query = em.createQuery("SELECT u FROM Profile u WHERE u.username = :username", Profile.class);
            query.setParameter("username", username);

            List<Profile> resultList = query.getResultList();

            if (!resultList.isEmpty()) {
                return resultList.get(0);
            } else {
                return null;
            }
        } catch (IllegalStateException | SecurityException e) {
            System.out.println("Error: ");
            System.out.println(e);
            return null;
        }
    }
}
