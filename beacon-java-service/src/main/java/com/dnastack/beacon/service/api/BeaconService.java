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
package com.dnastack.beacon.service.api;

import com.dnastack.beacon.exceptions.BeaconAlleleRequestException;
import com.dnastack.beacon.exceptions.BeaconException;
import org.apache.avro.AvroRemoteException;
import org.ga4gh.beacon.Beacon;
import org.ga4gh.beacon.BeaconAlleleRequest;
import org.ga4gh.beacon.BeaconAlleleResponse;

import java.util.List;

/**
 * Beacon service abstract class
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public interface BeaconService {

    /**
     * Get a beacon allele response from a beacon allele request.
     *
     * @param referenceName           Not null chromosome name
     * @param start                   Not null start position
     * @param referenceBases          Not null reference bases
     * @param alternateBases          Not null
     * @param assemblyId              Not null assembly version prefixed with GRCh
     * @param datasetIds              Dataset Ids to search, if null search all
     * @param includeDatasetResponses Include datasets in return.
     * @return Beacon allele response
     * @throws AvroRemoteException
     */
    BeaconAlleleResponse queryAllele(String referenceName, Long start, String referenceBases, String alternateBases, String assemblyId, List<String> datasetIds, Boolean includeDatasetResponses) throws BeaconException;

    /**
     * Get a beacon allele response from the beacon, given a BeaconAlleleRequest
     *
     * @param request Not null request object
     * @return
     * @throws BeaconAlleleRequestException
     */
    BeaconAlleleResponse queryAllele(BeaconAlleleRequest request) throws BeaconException;

    /**
     * Get information for a single beacon.
     *
     * @return
     * @throws BeaconException
     */
    Beacon queryBeacon() throws BeaconException;

}
