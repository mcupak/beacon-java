/*
 * The MIT License
 *
 * Copyright 2014 Miroslav Cupak (mirocupak@gmail.com).
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
package com.dnastack.beacon.rest.api;

import com.dnastack.beacon.exceptions.BeaconException;
import org.ga4gh.beacon.BeaconAlleleRequest;
import org.ga4gh.beacon.BeaconAlleleResponse;

import java.util.List;

/**
 * Beacon rest resource for querying alle information from a beacon
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @author Patrick Magee    (patrickmageee@gmail.com)
 * @version 1.0
 */
public interface BeaconQuery {

    /**
     * Query a beacon resource for information on whether an allele exists or not. Optionally includes the datasets.
     * Returns the completed BeaconAlleleResponse, Or a BeaconAlleleResponse with a BeaconError object if an error
     * was encountered.
     *
     * @param referenceName           Name of the chromosome or contig
     * @param start                   0-base start position
     * @param referenceBases          String of reference bases
     * @param alternateBases          String of alternate bases
     * @param assemblyId              String of assembly build version
     * @param datasetIds              List of dataset Ids
     * @param includeDatasetResponses Boolean value to include Dataset responses
     * @return Completed Beacon response object
     * @throws BeaconException
     */
    BeaconAlleleResponse query(String referenceName, Long start, String referenceBases, String alternateBases, String assemblyId, List<String> datasetIds, Boolean includeDatasetResponses) throws BeaconException;

    /**
     * Query a beacon resource for information on whether an allele exists or not. Optionally includes the datasets.
     * Returns the completed BeaconAlleleResponse, Or a BeaconAlleleResponse with a BeaconError object if an error
     * was encountered.
     *
     * @param request Completed Beacon response object
     * @return
     * @throws BeaconException
     */
    BeaconAlleleResponse query(BeaconAlleleRequest request) throws BeaconException;

}
