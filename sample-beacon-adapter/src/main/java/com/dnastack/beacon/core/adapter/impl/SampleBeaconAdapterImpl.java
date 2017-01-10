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
import com.dnastack.beacon.utils.AdapterConfig;
import org.ga4gh.beacon.*;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import java.util.*;

/**
 * @author patrickmagee
 */
@Singleton
public class SampleBeaconAdapterImpl implements BeaconAdapter {

    public static final String API_VERSION = "0.3.0";
    public static final String BEACON_ID = "beacon_id";
    public static final String DATASET_ID = "dataset_id";
    public static final String ORG_ID = "org_id";

    private SampleDataStore dataStore;

    private BeaconDatasetAlleleResponse lookupDataset(String datasetId, String assemblyId, String referencName, long start, String refBases, String altBases) {
        BeaconDatasetAlleleResponse response = new BeaconDatasetAlleleResponse();
        response.setDatasetId(datasetId);

        Map<String, Map> dataset = dataStore.getDATA().get(datasetId);
        if (dataset == null || dataset.isEmpty()) {
            BeaconError be = new BeaconError();
            be.setErrorCode(404);
            be.setMessage("Could not find dataset");

            response.setExists(null);
            response.setError(be);

            return response;
        }
        Map<String, Map> assembly = ((Map) dataset.get(assemblyId));
        if (assembly == null || assembly.isEmpty()) {
            BeaconError be = new BeaconError();
            be.setErrorCode(404);
            be.setMessage("Could not find assembly in current dataset");

            response.setExists(null);
            response.setError(be);

            return response;
        }
        Map<Long, Map> reference = ((Map) assembly.get(referencName));
        if (reference == null || reference.isEmpty()) {
            response.setExists(null);
            return addInfo(response);
        }
        Map<String, String> bases = ((Map) reference.get(start));
        if (bases == null || bases.isEmpty()) {
            response.setExists(false);
            return addInfo(response);
        }
        if (bases.get("referenceBases").equals(refBases) && bases.get("alternateBases").equals(altBases)) {
            response.setExists(true);
            return addInfo(response);
        } else {
            response.setExists(false);
            return addInfo(response);
        }
    }

    private BeaconDatasetAlleleResponse addInfo(BeaconDatasetAlleleResponse datasetResponse) {
        datasetResponse.setCallCount(1l);
        datasetResponse.setFrequency(0.221);
        datasetResponse.setVariantCount(1l);
        datasetResponse.setSampleCount(1l);
        datasetResponse.setExternalUrl("www.google.com");
        datasetResponse.setNote("This is a sample beacon only");
        Map<String, String> info = new HashMap<>();
        info.put("note", "Sample Beacon only");
        datasetResponse.setInfo(info);
        return datasetResponse;
    }

    private Beacon createSampleBeacon() {
        Beacon beacon = new Beacon();
        beacon.setApiVersion(API_VERSION);
        beacon.setVersion(API_VERSION);
        beacon.setDescription("This is the description for a sample beacon");
        beacon.setName("Sample Beacon");
        beacon.setAlternativeUrl("www.google.ca");
        beacon.setWelcomeUrl("www.url");
        beacon.setDatasets(Arrays.asList(createSampleBeaconDataset()));
        beacon.setCreateDateTime(new Date().toString());
        beacon.setUpdateDateTime(new Date().toString());
        beacon.setSampleAlleleRequests(Arrays.asList(createSampleRequest()));
        beacon.setOrganization(createSampleOrganization());
        beacon.setId(BEACON_ID);
        Map<String, String> info = new HashMap<>();
        info.put("note", "Sample Beacon only");
        beacon.setInfo(info);

        return beacon;
    }

    private BeaconAlleleRequest createSampleRequest() {
        BeaconAlleleRequest request = new BeaconAlleleRequest();
        request.setDatasetIds(Arrays.asList(DATASET_ID));
        request.setReferenceName("1");
        request.setAssemblyId("GRCh37");
        request.setIncludeDatasetResponses(true);
        request.setStart(1000l);
        request.setReferenceBases("A");
        request.setAlternateBases("C");
        return request;

    }

    private BeaconOrganization createSampleOrganization() {
        BeaconOrganization organization = new BeaconOrganization();
        organization.setAddress("123 Nullpointer Ave, StackTrace, Ohio");
        organization.setContactUrl("www.google.com");
        organization.setDescription("Sample implementation of a beacon organization");
        organization.setWelcomeUrl("www.google.com");
        organization.setLogoUrl("www.logoUrl.com");
        organization.setName("Sample Org");
        organization.setId(ORG_ID);
        Map<String, String> info = new HashMap<>();
        info.put("note", "Sample Beacon only");
        organization.setInfo(info);

        return organization;
    }

    private BeaconDataset createSampleBeaconDataset() {
        BeaconDataset dataset = new BeaconDataset();
        dataset.setName("Sample Dataset");
        dataset.setAssemblyId("GRCh37");
        dataset.setUpdateDateTime(new Date().toString());
        dataset.setCreateDateTime(new Date().toString());
        dataset.setId(DATASET_ID);
        dataset.setVersion(API_VERSION);
        dataset.setCallCount(10l);
        dataset.setSampleCount(10l);
        dataset.setDescription("Sample implementation");
        Map<String, String> info = new HashMap<>();
        info.put("note", "Sample Beacon only");
        dataset.setInfo(info);

        return dataset;

    }

    @PostConstruct
    public void init() {
        initAdapter(null);
    }

    @Override
    public void initAdapter(AdapterConfig adapterConfig) {
        dataStore = new SampleDataStore();
    }

    @Override
    public BeaconAlleleResponse getBeaconAlleleResponse(BeaconAlleleRequest request) throws BeaconException {
        BeaconAlleleResponse response = new BeaconAlleleResponse();
        response.setBeaconId(BEACON_ID);
        response.setAlleleRequest(request);

        List<BeaconDatasetAlleleResponse> responses = new ArrayList<>();
        for (String datasetId : request.getDatasetIds()) {
            responses.add(lookupDataset(datasetId,
                                        request.getAssemblyId(),
                                        request.getReferenceName(),
                                        request.getStart(),
                                        request.getReferenceBases(),
                                        request.getAlternateBases()));
        }

        if (!request.getIncludeDatasetResponses() && responses.size() == 1 && responses.get(0).getError() != null) {
            response.setExists(null);
            response.setError(responses.get(0).getError());
        } else if (request.getIncludeDatasetResponses()) {
            response.setDatasetAlleleResponses(responses);
        }
        boolean exists = false;
        for (BeaconDatasetAlleleResponse datasetResponses : responses) {

            if (datasetResponses.getExists()) {
                exists = true;
            }
        }
        response.setExists(exists);
        return response;
    }

    @Override
    public BeaconAlleleResponse getBeaconAlleleResponse(String referenceName, Long start, String referenceBases, String alternateBases, String assemblyId, List<String> datasetIds, Boolean includeDatasetResponses) throws BeaconException {
        BeaconAlleleRequest request = new BeaconAlleleRequest();
        request.setReferenceName(referenceName);
        request.setStart(start);
        request.setReferenceBases(referenceBases);
        request.setAlternateBases(alternateBases);
        request.setAssemblyId(assemblyId);
        request.setDatasetIds(datasetIds);
        if (includeDatasetResponses == null) {
            includeDatasetResponses = false;
        }
        request.setIncludeDatasetResponses(includeDatasetResponses);

        return getBeaconAlleleResponse(request);
    }

    @Override
    public Beacon getBeacon() throws BeaconException {
        return createSampleBeacon();
    }

}
