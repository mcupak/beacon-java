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
package com.dnastack.beacon.rest.endpoints;

import com.dnastack.beacon.rest.exceptions.BeaconException;
import com.dnastack.beacon.rest.exceptions.InvalidAlleleRequestException;
import org.ga4gh.beacon.BeaconAlleleRequest;
import org.ga4gh.beacon.BeaconAlleleResponse;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Beacon rest resource for querying alle information from a beacon
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 * @author Patrick Magee    (patrickmageee@gmail.com)
 * @author Artem (tema.voskoboynick@gmail.com)
 * @version 1.0
 */
@Path("/query")
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
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    BeaconAlleleResponse query(@QueryParam("referenceName") String referenceName,
                               @QueryParam("start") Long start,
                               @QueryParam("referenceBases") String referenceBases,
                               @QueryParam("alternateBases") String alternateBases,
                               @QueryParam("assemblyId") String assemblyId,
                               @QueryParam("datasetIds") List<String> datasetIds,
                               @QueryParam("includeDatasetResponses") Boolean includeDatasetResponses,
                               @Context HttpServletRequest servletRequest) throws InvalidAlleleRequestException;

    /**
     * Query a beacon resource for information on whether an allele exists or not. Optionally includes the datasets.
     * Returns the completed BeaconAlleleResponse, Or a BeaconAlleleResponse with a BeaconError object if an error
     * was encountered.
     *
     * @param request Completed Beacon response object
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    BeaconAlleleResponse query(BeaconAlleleRequest request) throws InvalidAlleleRequestException;

}
