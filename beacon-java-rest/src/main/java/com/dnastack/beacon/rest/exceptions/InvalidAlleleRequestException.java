package com.dnastack.beacon.rest.exceptions;

import org.ga4gh.beacon.BeaconAlleleRequest;

/**
 * @author Artem (tema.voskoboynick@gmail.com)
 * @version 1.0
 */
public class InvalidAlleleRequestException extends BeaconException {

    private BeaconAlleleRequest request;

    public InvalidAlleleRequestException(String message) {
        super(message);
    }

    public InvalidAlleleRequestException(BeaconAlleleRequest request, String message) {
        super(message);
        this.request = request;
    }

    public void setRequest(BeaconAlleleRequest request) {
        this.request = request;
    }

    public BeaconAlleleRequest getRequest() {
        return request;
    }
}
