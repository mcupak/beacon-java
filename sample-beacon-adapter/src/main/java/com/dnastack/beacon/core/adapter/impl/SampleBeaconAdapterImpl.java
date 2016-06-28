/*
 * The MIT License
 *
 *  Copyright 2014 Miroslav Cupak (mirocupak@gmail.com).
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package com.dnastack.beacon.core.adapter.impl;

import com.dnastack.beacon.adapter.api.BeaconAdapter;
import com.dnastack.beacon.exceptions.BeaconException;
import org.ga4gh.beacon.*;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by patrickmagee on 2016-06-17.
 */
@Stateless
public class SampleBeaconAdapterImpl extends BeaconAdapter {

    private static final String API_VERSION = "0.3.0";

    private Beacon beacon;
    private BeaconDataset dataset;
    private BeaconOrganization organization;
    private BeaconDatasetAlleleResponse datasetResponse;
    private BeaconAlleleRequest request;
    private BeaconAlleleResponse response;


    @Override
    @PostConstruct
    public void init() {

        request = createSampleRequest();
        organization = createSampleOrganization();
        dataset = createSampleBeaconDataset();
        datasetResponse = createSampleDatasetResponse();
        beacon = createSampleBeacon();
        response = createSampleResponse();

    }

    @Override
    public BeaconAlleleResponse getAlleleResponse(BeaconAlleleRequest request) throws BeaconException {
        response.setAlleleRequest(request);

        if (request.getIncludeDatasetResponses()) {
            response.setDatasetAlleleResponses(Arrays.asList(datasetResponse));
        } else {
            response.setDatasetAlleleResponses(null);
        }

        return response;
    }

    @Override
    public Beacon getBeaconResponse() throws BeaconException {
        createSampleResponse();
        createSampleBeacon();

        return beacon;
    }


    private Beacon createSampleBeacon() {
        Beacon beacon = new Beacon();
        beacon.setApiVersion(API_VERSION);
        beacon.setVersion(API_VERSION);
        beacon.setDescription("This is the description for a sample beacon");
        beacon.setName("Sample Beacon");
        beacon.setAlternativeUrl("www.google.ca");
        beacon.setWelcomeUrl("www.url");
        beacon.setDatasets(Arrays.asList(this.dataset));
        beacon.setCreateDateTime(new Date().toString());
        beacon.setUpdateDateTime(new Date().toString());
        beacon.setSampleAlleleRequests(Arrays.asList(request));
        beacon.setOrganization(organization);
        beacon.setId("1231231ssdaa");

        return beacon;
    }

    private BeaconAlleleRequest createSampleRequest() {
        BeaconAlleleRequest request = new BeaconAlleleRequest();
        request.setDatasetIds(Arrays.asList("123123"));
        request.setReferenceName("chr12");
        request.setAssemblyId("GRCh37");
        request.setIncludeDatasetResponses(true);
        request.setStart(123123123l);
        request.setReferenceBases("A");
        request.setAlternateBases("C");

        return request;

    }

    private BeaconAlleleResponse createSampleResponse() {
        BeaconAlleleResponse response = new BeaconAlleleResponse();
        response.setAlleleRequest(request);
        response.setBeaconId(beacon.getId());
        response.setExists(true);
        response.setError(null);
        response.setDatasetAlleleResponses(Arrays.asList(datasetResponse));
        return response;
    }

    private BeaconDatasetAlleleResponse createSampleDatasetResponse() {
        BeaconDatasetAlleleResponse datasetResponse = new BeaconDatasetAlleleResponse();
        datasetResponse.setDatasetId(dataset.getId());
        datasetResponse.setExists(true);
        datasetResponse.setCallCount(100l);
        datasetResponse.setFrequency(0.221);

        return datasetResponse;
    }

    private BeaconOrganization createSampleOrganization() {
        BeaconOrganization organization = new BeaconOrganization();
        organization.setName("Sample Org");
        organization.setId("123ad12d");

        return organization;
    }

    private BeaconDataset createSampleBeaconDataset() {
        BeaconDataset dataset = new BeaconDataset();
        dataset.setName("Sample Dataset");
        dataset.setAssemblyId("GRC37");
        dataset.setUpdateDateTime(new Date().toString());
        dataset.setCreateDateTime(new Date().toString());
        dataset.setId("125sda211");

        return dataset;

    }


}
