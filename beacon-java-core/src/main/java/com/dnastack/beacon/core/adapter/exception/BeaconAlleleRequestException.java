package com.dnastack.beacon.core.adapter.exception;

import com.dnastack.beacon.core.utils.Reason;
import org.ga4gh.beacon.BeaconAlleleRequest;

/**
 * Exception raised when there is an issue with the beacon allele request
 *
 * @patrickmagee
 */
public class BeaconAlleleRequestException extends BeaconException {

    private BeaconAlleleRequest request;

    public BeaconAlleleRequestException(String msg, Reason reason, BeaconAlleleRequest request) {
        super(reason,msg);
        this.request = request;
    }

    public BeaconAlleleRequestException(Reason reason, String msg) {
        super(reason,msg);
    }

    public BeaconAlleleRequest getRequest() {
        return request;
    }

}
