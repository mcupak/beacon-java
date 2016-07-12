package com.dnastack.beacon.rest.sys.security;

import org.keycloak.adapters.servlet.KeycloakOIDCFilter;

import javax.inject.Inject;
import javax.servlet.*;
import java.io.IOException;

/**
 * Extends Keycloak security filter to allow enabling/disabling it depending on a context parameter.
 *
 * @author Artem (tema.voskoboynick@gmail.com)
 * @version 1.0
 */
public class ConfigurableKeycloakOIDCFilter extends KeycloakOIDCFilter {

    @Inject
    private SecurityContext securityContext;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (securityContext.isSecurityEnabled()) {
            super.init(filterConfig);
        }
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (securityContext.isSecurityEnabled()) {
            super.doFilter(req, res, chain);
        } else {
            chain.doFilter(req, res);
        }
    }
}
