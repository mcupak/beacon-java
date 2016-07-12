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

import com.dnastack.beacon.rest.exceptions.InvalidAlleleRequestException;
import org.ga4gh.beacon.BeaconAlleleRequest;
import org.ga4gh.beacon.BeaconAlleleResponse;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Beacon rest resource for querying allele information from a beacon.
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
     * Returns the completed BeaconAlleleResponse or a BeaconAlleleResponse with a BeaconError object if an error
     * was encountered.
     *
     * @param referenceName           reference name (chromosome). Accepted values: 1-22, X, Y
     * @param start                   0-base start position
     * @param referenceBases          reference bases for this variant (starting from `start`). Accepted values: see the
     *                                REF field in VCF 4.2 specification (https://samtools.github.io/hts-specs/VCFv4.2.pdf)
     * @param alternateBases          the bases that appear instead of the reference bases. Accepted values: see the ALT
     *                                field in VCF 4.2 specification (https://samtools.github.io/hts-specs/VCFv4.2.pdf)
     * @param assemblyId              assembly identifier (GRC notation, e.g. `GRCh37`)
     * @param datasetIds              identifiers of datasets. If this field is null, all datasets will be queried
     * @param includeDatasetResponses indicator of whether responses for individual datasets should be included (not null)
     *                                in the response. If null, the default value of false is assumed
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
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    BeaconAlleleResponse query(BeaconAlleleRequest request, @Context HttpServletRequest servletRequest) throws InvalidAlleleRequestException;

}
