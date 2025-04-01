package com.onndoo.j10tomcat.servlet;

import java.io.IOException;

import com.onndoo.j10tomcat.model.Profile;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/data")
public class DataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static final EntityManagerFactory emf =
	        Persistence.createEntityManagerFactory("myPersistenceUnit");
	
@Inject
	
	Pbkdf2PasswordHash passwordHasher;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println(passwordHasher.generate("password123".toCharArray()));
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		Profile profile = new Profile();
		profile.setUsername("admin");
		profile.setPassword(passwordHasher.generate("password123".toCharArray()));
		profile.setGroupName("admin");
		
		persistUser(profile);

        
        response.getWriter().println("User added successfully!" );
        
        
	}

	private void persistUser(Profile user) {
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin(); // Inicia la transacción
        em.persist(user);           // Guarda el usuario
        em.getTransaction().commit(); // Confirma la transacción
        em.close();                 // Cierra el EntityManager
    }

    @Override
    public void destroy() {
        emf.close(); // Cierra el EntityManagerFactory al detener la aplicación
    }
}
