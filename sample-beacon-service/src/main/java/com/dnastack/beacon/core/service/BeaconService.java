package com.dnastack.beacon.core.service;

import org.ga4gh.beacon.Beacon;
import org.ga4gh.beacon.BeaconAlleleRequest;
import org.ga4gh.beacon.BeaconAlleleResponse;

/**
 * @author Artem (tema.voskoboynick@gmail.com)
 * @version 1.0
 */
public interface BeaconService {
    BeaconAlleleResponse getBeaconAlleleResponse(BeaconAlleleRequest request);

    Beacon getBeacon();
}
