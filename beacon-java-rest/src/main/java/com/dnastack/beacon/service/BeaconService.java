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

import com.dnastack.beacon.core.adapter.exception.BeaconAlleleRequestException;
import com.dnastack.beacon.core.adapter.exception.BeaconException;
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
     * Get a beacon allele response from the beacon.
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
    BeaconAlleleResponse queryAllele(String referenceName, Long start, String referenceBases, String alternateBases, String assemblyId, List<String> datasetIds, Boolean includeDatasetResponses) throws BeaconException;

    /**
     * Get a beacon allele response from the beacon, given a BeaconAlleleRequest
     *
     * @param request
     * @return
     * @throws BeaconAlleleRequestException
     */
    BeaconAlleleResponse queryAllele(BeaconAlleleRequest request) throws BeaconException;


    /**
     * Get beacon information.
     *
     * @return
     * @throws BeaconException
     */
    Beacon queryBeacon() throws BeaconException;

}
