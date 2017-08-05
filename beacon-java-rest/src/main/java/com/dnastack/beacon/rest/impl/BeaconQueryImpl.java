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
package com.dnastack.beacon.rest.impl;

import com.dnastack.beacon.exceptions.BeaconException;
import com.dnastack.beacon.rest.api.BeaconQuery;
import com.dnastack.beacon.service.api.BeaconService;
import org.ga4gh.beacon.BeaconAlleleRequest;
import org.ga4gh.beacon.BeaconAlleleResponse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Beacon query implementation.
 *
 * @author Miroslav Cupak (mirocupak@gmail.com)
 */
@Path("/query")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class BeaconQueryImpl implements BeaconQuery {

    @Inject
    private BeaconService service;

    @GET
    @Override
    public BeaconAlleleResponse query(@QueryParam("referenceName") String referenceName, @QueryParam("start") Long start, @QueryParam("referenceBases") String referenceBases, @QueryParam("alternateBases") String alternateBases, @QueryParam("assemblyId") String assemblyId, @QueryParam("datasetIds") List<String> datasetIds, @QueryParam("includeDatasetResponses") Boolean includeDatasetResponses) throws BeaconException {
        return service.queryAllele(referenceName,
                                   start,
                                   referenceBases,
                                   alternateBases,
                                   assemblyId,
                                   datasetIds,
                                   includeDatasetResponses);
    }

    @POST
    @Override
    public BeaconAlleleResponse query(BeaconAlleleRequest request) throws BeaconException {
        return service.queryAllele(request);
    }
}
