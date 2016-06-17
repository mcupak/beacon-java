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

import com.dnastack.beacon.core.adapter.api.BeaconAdapter;
import com.dnastack.beacon.core.adapter.exception.BeaconAlleleRequestException;
import com.dnastack.beacon.core.adapter.exception.BeaconException;
import com.dnastack.beacon.core.utils.QueryUtils;
import com.dnastack.beacon.core.utils.Reason;
import org.ga4gh.beacon.Beacon;
import org.ga4gh.beacon.BeaconAlleleRequest;
import org.ga4gh.beacon.BeaconAlleleResponse;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

/**
 * Beacon service implementation. Ensures the request are valid, and throws the appropriate exceptions
 *
 * @author patrickmagee
 */
@Stateless
public class BeaconServiceImpl implements BeaconService {

    @Inject
    private BeaconAdapter adapter;

    /**
     * @inheritDoc
     **/
    @Override
    public BeaconAlleleResponse queryAllele(String referenceName, Long start, String referenceBases, String alternateBases, String assemblyId, List<String> datasetIds, Boolean includeDatasetResponses) throws BeaconException {
        if (referenceName == null || start == null || referenceBases == null || alternateBases == null || assemblyId == null) {
            throw new BeaconAlleleRequestException(Reason.INVALID_REQUEST, "Values cannot be null");
        }

        if (includeDatasetResponses == null){
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

        return queryAllele(request);
    }

    /**
     * @inheritDoc
     **/
    @Override
    public BeaconAlleleResponse queryAllele(BeaconAlleleRequest request) throws BeaconException {
        QueryUtils.validateRequest(request);
        return adapter.getAlleleResponse(request);
    }

    /**
     * @inheritDoc
     **/
    @Override
    public Beacon queryBeacon() throws BeaconException {
        return adapter.getBeaconResponse();
    }
}
