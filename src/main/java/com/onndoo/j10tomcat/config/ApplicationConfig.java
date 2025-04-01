package com.onndoo.j10tomcat.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.annotation.FacesConfig;
import jakarta.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;

@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "${'java:comp/env/jdbc/MyDataSource'}",
        callerQuery = "select password from Profile where username = ?",
        groupsQuery = "select groupName from Profile where username = ?"
)
@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
        loginPage = "/index",
        errorPage = "",
        useForwardToLogin = false)
)
@FacesConfig
@ApplicationScoped
public class ApplicationConfig {
    
}