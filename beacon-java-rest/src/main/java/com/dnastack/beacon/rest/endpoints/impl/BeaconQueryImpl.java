/*
 * The MIT License
 *
 * Copyright 2014 Miroslav Cupak (mirocupak@gmail.com).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.dnastack.beacon.rest.endpoints.impl;

import com.dnastack.beacon.core.service.BeaconService;
import com.dnastack.beacon.rest.endpoints.BeaconQuery;
import com.dnastack.beacon.rest.exceptions.InvalidAlleleRequestException;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.ga4gh.beacon.BeaconAlleleRequest;
import org.ga4gh.beacon.BeaconAlleleResponse;
import org.keycloak.AuthorizationContext;
import org.keycloak.KeycloakSecurityContext;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import java.util.List;

/**
 * @author Artem (tema.voskoboynick@gmail.com)
 * @author Patrick Magee (patrickmageee@gmail.com)
 */
@Path("/query")
public class BeaconQueryImpl implements BeaconQuery {

    @Inject
    private BeaconService service;

    @Override
    public BeaconAlleleResponse query(String referenceName, Long start, String referenceBases, String alternateBases,
                                      String assemblyId, List<String> datasetIds, Boolean includeDatasetResponses, @Context HttpServletRequest servletRequest)
            throws InvalidAlleleRequestException {
        validateRequest(referenceName, start, referenceBases, alternateBases, assemblyId);
        BeaconAlleleRequest request = BeaconAlleleRequest.newBuilder()
                .setReferenceName(referenceName)
                .setStart(start)
                .setReferenceBases(referenceBases)
                .setAlternateBases(alternateBases)
                .setAssemblyId(assemblyId)
                .setDatasetIds(datasetIds)
                .setIncludeDatasetResponses(BooleanUtils.isTrue(includeDatasetResponses))
                .build();

        return service.getBeaconAlleleResponse(request);
    }

    @Override
    public BeaconAlleleResponse query(BeaconAlleleRequest request, @Context HttpServletRequest servletRequest) throws InvalidAlleleRequestException {
        validateRequest(request);
        return service.getBeaconAlleleResponse(request);
    }

    private void validateRequest(BeaconAlleleRequest request) throws InvalidAlleleRequestException {
        throwIf(request == null, "Request can't be null", request);
        try {
            validateRequest(request.getReferenceName(), request.getStart(), request.getReferenceBases(), request.getAlternateBases(), request.getAssemblyId());
        } catch (InvalidAlleleRequestException e) {
            e.setRequest(request);
            throw e;
        }
    }

    /**
     * Validates the request parameters according to the 0.3.0 beacon specifications.
     */
    private void validateRequest(String referenceName, Long start, String referenceBases, String alternateBases, String assemblyId) throws InvalidAlleleRequestException {
        boolean isValidReferenceName = StringUtils.isNotBlank(referenceName);
        boolean isValidStart = start != null && start >= 0;
        boolean isValidReferenceBases = StringUtils.isNotBlank(referenceBases);
        boolean isValidAlternateBases = StringUtils.isNotBlank(alternateBases);
        boolean isValidAssemblyId = StringUtils.startsWith(assemblyId, "GRCh");

        throwIf(!isValidReferenceName, "Reference name can't be null");
        throwIf(!isValidStart, "Start position can't be null, should be 0-based positive integer");
        throwIf(!isValidReferenceBases, "Reference bases can't be null");
        throwIf(!isValidAlternateBases, "Alternate bases can't be null");
        throwIf(!isValidAssemblyId, "Assembly ID can't be null, should start with GRCh");
    }

    private static void throwIf(boolean expression, String message) throws InvalidAlleleRequestException {
        if (expression) {
            throw new InvalidAlleleRequestException(message);
        }
    }

    private static void throwIf(boolean expression, String message, BeaconAlleleRequest request) throws InvalidAlleleRequestException {
        if (expression) {
            throw new InvalidAlleleRequestException(request, message);
        }
    }
}
