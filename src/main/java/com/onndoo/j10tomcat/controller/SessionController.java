package com.onndoo.j10tomcat.controller;

import java.io.IOException;
import java.io.Serializable;

import com.onndoo.j10tomcat.model.Profile;
import com.onndoo.j10tomcat.service.Service;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;

@Named(value = "SessionController")
@SessionScoped
public class SessionController implements Serializable {

	private static final long serialVersionUID = 1L;

	public SessionController() {

	}

	@NotEmpty
	private String username;
	@NotEmpty
	private String password;
	private Profile currentUser;

	@Inject
	private Service UserService;

	@Inject
	FacesContext facesContext;

	@Inject
	SecurityContext securityContext;

	public void executeLogin() {
		try {
			// Cambiamos el 'switch' para usar la sintaxis tradicional de 'case' en Java 11
			switch (processAuthentication()) {
			case SEND_CONTINUE:
				facesContext.responseComplete();
				break;
			case SEND_FAILURE:
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid username or password."));
				break;
			case SUCCESS:
				currentUser = UserService.getSession(securityContext.getCallerPrincipal().getName());
				getExternalContext().redirect(getExternalContext().getRequestContextPath() + "/secured/index.xhtml");
				break;
			case NOT_DONE:
				// No se realiza ninguna acción para este caso
				break;
			default:
				throw new AssertionError();
			}

		} catch (IOException e) {
			System.out.println("Error logging in " + e.getLocalizedMessage());
		}
	}

	public String logOut() {
		try {
			this.currentUser = null;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Logout successful!"));
			ExternalContext ec = facesContext.getExternalContext();
			((HttpServletRequest) ec.getRequest()).logout();
			return "/index?faces-redirect=true";
		} catch (ServletException e) {

		}
		return null;
	}

	public boolean isValid() {
		return currentUser != null;
	}

	private AuthenticationStatus processAuthentication() {
		ExternalContext ec = getExternalContext();
		return securityContext.authenticate((HttpServletRequest) ec.getRequest(),
				(HttpServletResponse) ec.getResponse(),
				AuthenticationParameters.withParams().credential(new UsernamePasswordCredential(username, password)));
	}

	private ExternalContext getExternalContext() {
		return facesContext.getExternalContext();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}