package com.dnastack.beacon.rest.sys;

import com.dnastack.beacon.exceptions.BeaconAlleleRequestException;
import com.dnastack.beacon.exceptions.BeaconException;
import org.ga4gh.beacon.BeaconAlleleResponse;
import org.ga4gh.beacon.BeaconError;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

/**
 * Captures all Beacon exceptions and creates corresponding responses.
 *
 * @author patmagee
 * @author Artem (tema.voskoboynick@gmail.com)
 * @version 1.0
 */
@Provider
public class BeaconExceptionHandler implements ExceptionMapper<BeaconException> {

    @Override
    public Response toResponse(BeaconException exception) {
        if (exception instanceof BeaconAlleleRequestException) {
            return handleBeaconAlleleRequestException((BeaconAlleleRequestException) exception);
        } else {
            return handleDefaultBeaconException(exception);
        }
    }

    /**
     * Due to backward compatibility issues, the response on Beacon Query request should always be of the {@link BeaconAlleleResponse} type,
     * and any errors, if any, are stored inside it.
     */
    private Response handleBeaconAlleleRequestException(BeaconAlleleRequestException exception) {
        BeaconAlleleResponse response = new BeaconAlleleResponse();
        response.setAlleleRequest(exception.getRequest());
        response.setExists(null);

        BeaconError error = BeaconError.newBuilder()
                .setErrorCode(BAD_REQUEST.getStatusCode())
                .setMessage(exception.getMessage())
                .build();
        response.setError(error);

        return Response.status(error.getErrorCode()).entity(response).build();
    }

    private Response handleDefaultBeaconException(BeaconException exception) {
        BeaconError response = BeaconError.newBuilder()
                .setMessage(exception.getMessage())
                .setErrorCode(INTERNAL_SERVER_ERROR.getStatusCode())
                .build();

        return Response.status(response.getErrorCode()).entity(response).build();
    }
}
