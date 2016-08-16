package com.dnastack.beacon.rest.sys.security;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.servlet.ServletContext;

/**
 * @author Artem (tema.voskoboynick@gmail.com)
 * @version 1.0
 */
@Dependent
public class SecurityContext {
    @Inject
    private ServletContext servletContext;

    public boolean isSecurityEnabled() {
        return Boolean.parseBoolean(servletContext.getInitParameter("keycloak.security.enabled"));
    }
}
