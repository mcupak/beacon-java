package com.dnastack.beacon.rest.providers;

import com.dnastack.beacon.core.adapter.exception.BeaconAlleleRequestException;
import com.dnastack.beacon.core.adapter.exception.BeaconException;
import com.dnastack.beacon.service.BeaconService;
import org.ga4gh.beacon.Beacon;
import org.ga4gh.beacon.BeaconAlleleResponse;
import org.ga4gh.beacon.BeaconError;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Beacon exception handler for catching beacon errors and returning the appropriate object
 *
 * @patrickmagee on 2016-06-17.
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
        if (exception.getClass().getCanonicalName().equals(BeaconAlleleRequestException.class.getCanonicalName())){
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
