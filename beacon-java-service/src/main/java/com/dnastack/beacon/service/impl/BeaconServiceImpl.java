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
package com.dnastack.beacon.service.impl;

import com.dnastack.beacon.adapter.api.BeaconAdapter;
import com.dnastack.beacon.exceptions.BeaconAlleleRequestException;
import com.dnastack.beacon.exceptions.BeaconException;
import com.dnastack.beacon.service.api.BeaconService;
import com.dnastack.beacon.utils.Reason;
import org.ga4gh.beacon.Beacon;
import org.ga4gh.beacon.BeaconAlleleRequest;
import org.ga4gh.beacon.BeaconAlleleResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

/**
 * Beacon service implementation. Ensures the request are valid, and throws the appropriate exceptions
 *
 * @author patrickmagee
 */
@RequestScoped
public class BeaconServiceImpl implements BeaconService {

    @Inject
    private BeaconAdapter adapter;

    /**
     * Validate the beacon fields according to the 0.3.0 beacon specifications
     *
     * @param referenceName
     * @param start
     * @param referenceBases
     * @param alternateBases
     * @param assemblyId
     * @throws BeaconAlleleRequestException
     */
    private void validateRequest(String referenceName, Long start, String referenceBases, String alternateBases, String assemblyId) throws BeaconAlleleRequestException {
        if (referenceName == null) {
            throw new BeaconAlleleRequestException(Reason.INVALID_REQUEST,
                                                   "Reference cannot be null. Please provide an appropriate reference name");
        } else if (start == null) {
            throw new BeaconAlleleRequestException(Reason.INVALID_REQUEST,
                                                   "Start position cannot be null. Please provide a 0-based start position");
        } else if (referenceBases == null) {
            throw new BeaconAlleleRequestException(Reason.INVALID_REQUEST, "Reference bases cannot be null");
        } else if (alternateBases == null) {
            throw new BeaconAlleleRequestException(Reason.INVALID_REQUEST, "Alternate bases cannot be null");
        } else if (assemblyId == null) {
            throw new BeaconAlleleRequestException(Reason.INVALID_REQUEST,
                                                   "AssemblyId cannot be null. Please defined a valid GRCh assembly Id");
        } else if (!assemblyId.startsWith("GRCh")) {
            throw new BeaconAlleleRequestException(Reason.INVALID_REQUEST,
                                                   "Invalid assemblyId. Assemblies must be from GRCh builds");
        }
    }

    /**
     * @inheritDoc
     **/
    @Override
    public BeaconAlleleResponse queryAllele(String referenceName, Long start, String referenceBases, String alternateBases, String assemblyId, List<String> datasetIds, Boolean includeDatasetResponses) throws BeaconException {
        validateRequest(referenceName, start, referenceBases, alternateBases, assemblyId);

        if (includeDatasetResponses == null) {
            includeDatasetResponses = false;
        }

        BeaconAlleleRequest request = new BeaconAlleleRequest();
        request.setReferenceName(referenceName);
        request.setReferenceBases(referenceBases);
        request.setAlternateBases(alternateBases);
        request.setStart(start);
        request.setIncludeDatasetResponses(includeDatasetResponses);
        request.setAssemblyId(assemblyId);
        request.setDatasetIds(datasetIds);

        return adapter.getBeaconAlleleResponse(request);
    }

    /**
     * @inheritDoc
     **/
    @Override
    public BeaconAlleleResponse queryAllele(BeaconAlleleRequest request) throws BeaconException {
        validateRequest(request.getReferenceName(),
                        request.getStart(),
                        request.getReferenceBases(),
                        request.getAlternateBases(),
                        request.getAssemblyId());

        if (request.getIncludeDatasetResponses() == null) {
            request.setIncludeDatasetResponses(false);
        }

        return adapter.getBeaconAlleleResponse(request);
    }

    /**
     * @inheritDoc
     **/
    @Override
    public Beacon queryBeacon() throws BeaconException {
        return adapter.getBeacon();
    }
}
