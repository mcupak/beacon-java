package com.dnastack.beacon.core.adapter.exception;

import com.dnastack.beacon.core.utils.Reason;

/**
 * Base Beacon exception
 * @author patrickmagee on 2016-06-17.
 */
public class BeaconException extends Exception {

    private Reason reason;

    public BeaconException(Reason reason) {
        super();
        this.reason = reason;
    }

    public BeaconException(Reason reason, String msg) {
        super(msg);
        this.reason = reason;
    }

    public Reason getReason(){
        return reason;
    }

}
