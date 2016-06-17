package com.dnastack.beacon.core.adapter.api;

import com.dnastack.beacon.core.adapter.exception.BeaconException;
import org.ga4gh.beacon.Beacon;
import org.ga4gh.beacon.BeaconAlleleRequest;
import org.ga4gh.beacon.BeaconAlleleResponse;

import javax.annotation.PostConstruct;

/**
 * Abstract Beacon adapter class to be used when creating a new beacon adapter
 * Created by patrickmagee on 2016-06-16.
 */
public abstract class BeaconAdapter {


    /**
     * Initializer for a beacon adapter, called after bean creation
     */
    @PostConstruct
    public abstract void init();

    /**
     * Retrieve a Beacon allele response from the supplied datasource
     * @param request
     * @return
     */
    public abstract BeaconAlleleResponse getAlleleResponse(BeaconAlleleRequest request) throws BeaconException;


    /**
     * Retrieve information about the specified beacon
     * @return
     */
    public abstract Beacon getBeaconResponse() throws BeaconException;

}
