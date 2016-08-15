package com.dnastack.beacon.rest.endpoints.impl.secured;

import com.dnastack.beacon.exceptions.BeaconException;
import com.dnastack.beacon.rest.endpoints.BeaconQuery;
import com.dnastack.beacon.rest.endpoints.impl.BeaconQueryImpl;
import com.dnastack.beacon.rest.sys.security.SecurityContext;
import org.apache.commons.collections.CollectionUtils;
import org.ga4gh.beacon.BeaconAlleleRequest;
import org.ga4gh.beacon.BeaconAlleleResponse;
import org.ga4gh.beacon.BeaconDatasetAlleleResponse;
import org.keycloak.AuthorizationContext;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.authorization.Permission;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Decorates the original {@link BeaconQueryImpl} to enforce security constraints.
 * Role-based access is automatically checked by the Keycloak filter, this class handles more detailed security constraints.
 *
 * @author Artem (tema.voskoboynick@gmail.com)
 * @version 1.0
 */
@Decorator
public class SecuredBeaconQueryImpl implements BeaconQuery {

    /**
     * Public role is like an admin role - can view anything.
     */
    private static final String PUBLIC_ROLE = "public";

    /**
     * Users with Registered role are allowed to view only answers yes/no.
     * But individual users are allowed to view individual dataset responses.
     */
    private static final String REGISTERED_ROLE = "registered";

    /**
     * In Keycloak, a dataset resource name is in the "Dataset:[dataset_id]" format.
     * Allowed dataset IDs are retrieved by removing the "Dataset:" prefix.
     */
    private static final String DATASET_RESOURCES_PREFIX = "Dataset:";

    @Delegate
    @Any
    @Inject
    private BeaconQuery delegate;

    @Inject
    private SecurityContext securityContext;

    @Override
    public BeaconAlleleResponse query(String referenceName, Long start, String referenceBases, String alternateBases, String assemblyId, List<String> datasetIds, Boolean includeDatasetResponses, @Context HttpServletRequest servletRequest) throws BeaconException {
        BeaconAlleleResponse response = delegate.query(referenceName, start, referenceBases, alternateBases, assemblyId, datasetIds, includeDatasetResponses, servletRequest);
        ensureSecurityConstraints(servletRequest, response);
        return response;
    }

    @Override
    public BeaconAlleleResponse query(BeaconAlleleRequest request, @Context HttpServletRequest servletRequest) throws BeaconException {
        BeaconAlleleResponse response = delegate.query(request, servletRequest);
        ensureSecurityConstraints(servletRequest, response);
        return response;
    }

    private void ensureSecurityConstraints(HttpServletRequest servletRequest, BeaconAlleleResponse response) {
        if (!securityContext.isSecurityEnabled()) {
            return;
        }

        if (servletRequest.isUserInRole(PUBLIC_ROLE)) {
            return;
        }

        if (servletRequest.isUserInRole(REGISTERED_ROLE)) {
            excludeUnauthorizedDatasets(servletRequest, response.getDatasetAlleleResponses());
        }
    }

    private void excludeUnauthorizedDatasets(HttpServletRequest servletRequest, List<BeaconDatasetAlleleResponse> datasetResponses) {
        if (CollectionUtils.isNotEmpty(datasetResponses)) {
            Set<String> permittedDatasetIds = getPermittedDatasetIds(servletRequest);
            datasetResponses.removeIf(datasetResponse -> !permittedDatasetIds.contains(datasetResponse.getDatasetId()));
        }
    }

    private Set<String> getPermittedDatasetIds(HttpServletRequest servletRequest) {
        KeycloakSecurityContext keycloakSecurityContext = (KeycloakSecurityContext) servletRequest.getAttribute(KeycloakSecurityContext.class.getName());
        AuthorizationContext authzContext = keycloakSecurityContext.getAuthorizationContext();

        List<Permission> permissions = authzContext.getPermissions();
        return permissions.stream()
                .filter(permission -> permission.getResourceSetName().startsWith(DATASET_RESOURCES_PREFIX))
                .map(permission -> permission.getResourceSetName().substring(DATASET_RESOURCES_PREFIX.length()))
                .collect(Collectors.toSet());
    }
}
