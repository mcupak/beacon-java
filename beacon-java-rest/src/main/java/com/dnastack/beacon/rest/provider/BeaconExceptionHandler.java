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
package com.dnastack.beacon.rest.provider;

import com.dnastack.beacon.exceptions.BeaconAlleleRequestException;
import com.dnastack.beacon.exceptions.BeaconException;
import com.dnastack.beacon.service.api.BeaconService;
import org.ga4gh.beacon.Beacon;
import org.ga4gh.beacon.BeaconAlleleResponse;
import org.ga4gh.beacon.BeaconError;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Beacon exceptions handler for catching beacon errors and returning the appropriate object
 *
 * @author patmagee
 */
@Provider
public class BeaconExceptionHandler implements ExceptionMapper<BeaconException> {

    @Inject
    BeaconService service;

    @Override
    public Response toResponse(BeaconException exception) {

        BeaconError error = new BeaconError();
        error.setMessage(exception.getMessage());
        switch (exception.getReason()) {
            case INVALID_REQUEST:
                error.setErrorCode(Response.Status.BAD_REQUEST.getStatusCode());
                break;
            default:
                error.setErrorCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }

        //If this is an alleleRequest then return a BeaconAlleleResponse with the error fields set appropriately
        if (exception.getClass().getCanonicalName().equals(BeaconAlleleRequestException.class.getCanonicalName())) {
            BeaconAlleleRequestException e = (BeaconAlleleRequestException) exception;
            BeaconAlleleResponse response = new BeaconAlleleResponse();
            response.setExists(null);
            response.setError(error);

            if (e.getRequest() != null) {
                response.setAlleleRequest(e.getRequest());
            }

            try {
                Beacon beacon = service.queryBeacon();
                response.setBeaconId(beacon.getId());
            } catch (BeaconException ex) {
                response.setBeaconId(null);
            }

            return Response.status(error.getErrorCode()).entity(response).build();

        } else {
            BeaconError response = error;
            return Response.status(error.getErrorCode()).entity(error).build();
        }
    }
}
