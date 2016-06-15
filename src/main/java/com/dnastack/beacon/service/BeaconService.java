/*
 * The MIT License
 *
 * Copyright 2014 DNAstack.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.dnastack.beacon.service;

import com.dnastack.beacon.adapters.api.BeaconAdapter;
import com.dnastack.beacon.adapters.exceptions.BeaconAdapterException;
import org.apache.avro.AvroRemoteException;
import org.ga4gh.beacon.Beacon;
import org.ga4gh.beacon.BeaconAlleleRequest;
import org.ga4gh.beacon.BeaconAlleleResponse;
import org.ga4gh.beacon.BeaconMethods;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

/**
 * Beacon service.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
@Stateless
public class BeaconService implements BeaconMethods {


    /**
     * Beacon adapter to be resolved at run time. The adapter defines how the beacon interacts with its datasource,
     * making that configuration independant from the actual beacon itself
     */
    @Inject
    private BeaconAdapter adapter;

    /**
     * Get a beacon allele response from the beacon
     *
     * @param referenceName
     * @param start
     * @param referenceBases
     * @param alternateBases
     * @param assemblyId
     * @param datasetIds
     * @param includeDatasetResponses
     * @return Beacon allele response
     * @throws AvroRemoteException
     */
    public BeaconAlleleResponse getBeaconAlleleResponse(String referenceName, Long start, String referenceBases, String alternateBases, String assemblyId, List<String> datasetIds, Boolean includeDatasetResponses) throws AvroRemoteException {
        BeaconAlleleRequest request = new BeaconAlleleRequest();
        request.setReferenceName(referenceName);
        request.setReferenceBases(referenceBases);
        request.setAlternateBases(alternateBases);
        request.setStart(start);
        request.setIncludeDatasetResponses(includeDatasetResponses);
        request.setAssemblyId(assemblyId);
        request.setDatasetIds(datasetIds);

        return getBeaconAlleleResponse(request);
    }

    /**
     * Retrieve general information defining this beacon
     *
     * @return
     * @throws AvroRemoteException
     */
    @Override
    public Beacon getBeacon() throws AvroRemoteException {
        try {
            return adapter.getBeaconResponse();
        } catch (BeaconAdapterException e) {
            return null;
        }
    }

    /**
     * @param beaconAlleleRequest
     * @return
     * @throws AvroRemoteException
     */
    @Override
    public BeaconAlleleResponse getBeaconAlleleResponse(BeaconAlleleRequest beaconAlleleRequest) throws AvroRemoteException {
        try {
            return adapter.getAlleleResponse(beaconAlleleRequest);
        } catch (BeaconAdapterException e) {
            return null;
        }
    }

}
