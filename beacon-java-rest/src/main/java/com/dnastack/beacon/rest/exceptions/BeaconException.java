package com.dnastack.beacon.rest.exceptions;

/**
 * Base class for all checked Beacon exceptions.
 *
 * @author Artem (tema.voskoboynick@gmail.com)
 * @version 1.0
 */
public class BeaconException extends Exception {

    public BeaconException() {
        super();
    }

    public BeaconException(String message) {
        super(message);
    }
}
