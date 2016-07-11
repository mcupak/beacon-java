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

package com.dnastack.beacon.core.service.impl;

import avro.shaded.com.google.common.collect.ImmutableMap;
import org.ga4gh.beacon.*;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class simply is acting as a datastorage for the BeaconAdapter to perform lookups on
 *
 * @author patmagee
 * @author Artem (tema.voskoboynick@gmail.com)
 */
public class SampleData {

    public static final Map<String, Map> DATASETS;
    public static final Beacon BEACON;
    public static final BeaconDatasetAlleleResponse BASE_DATASET_RESPONSE;

    static {
        BeaconDataset dataset = BeaconDataset.newBuilder()
                .setId("sample_dataset_id_1")
                .setName("Sample Dataset")
                .setAssemblyId("GRCh37")
                .setUpdateDateTime(new Date().toString())
                .setCreateDateTime(new Date().toString())
                .setVersion("0.3.0")
                .setCallCount(10L)
                .setSampleCount(10L)
                .setDescription("Sample implementation")
                .setInfo(ImmutableMap.of("note", "This is a Sample Dataset"))
                .build();

        BASE_DATASET_RESPONSE = BeaconDatasetAlleleResponse.newBuilder()
                .setCallCount(1L)
                .setFrequency(0.221)
                .setVariantCount(1L)
                .setSampleCount(1L)
                .setExternalUrl("www.google.com")
                .setInfo(ImmutableMap.of("Sample Note", "Sample Note Value"))
                .setDatasetId(dataset.getId())
                .setExists(null) // Setting nulls explicitly required by avro.
                .build();

        BeaconAlleleRequest request = BeaconAlleleRequest.newBuilder()
                .setDatasetIds(Collections.singletonList(dataset.getId()))
                .setReferenceName("1")
                .setAssemblyId("GRCh37")
                .setIncludeDatasetResponses(true)
                .setStart(1000L)
                .setReferenceBases("A")
                .setAlternateBases("C")
                .build();

        DATASETS = new HashMap<String, Map>() {
            {
                Map<String, String> bases = ImmutableMap.of(
                        "referenceBases", request.getReferenceBases(),
                        "alternateBases", request.getAlternateBases()
                );
                Map<Long, Map> positions = ImmutableMap.of(request.getStart(), bases);
                Map<String, Map> references = ImmutableMap.of(request.getReferenceName(), positions);
                Map<String, Map> assemblies = ImmutableMap.of(request.getAssemblyId(), references);

                put(dataset.getId(), assemblies);
            }
        };

        BeaconOrganization organization = BeaconOrganization.newBuilder()
                .setAddress("123 Nullpointer Ave, StackTrace, Ohio")
                .setContactUrl("www.google.com")
                .setDescription("Sample implementation of a beacon organization")
                .setWelcomeUrl("www.google.com")
                .setLogoUrl("www.logoUrl.com")
                .setName("Sample Organization")
                .setId("sample_organization_id")
                .setInfo(ImmutableMap.of("note", "This is a sample Organization"))
                .build();

        BEACON = Beacon.newBuilder()
                .setApiVersion("0.3.0")
                .setVersion("0.3.0")
                .setDescription("Sample description")
                .setName("Sample Beacon")
                .setAlternativeUrl("www.google.ca")
                .setWelcomeUrl("www.url")
                .setDatasets(Collections.singletonList(dataset))
                .setCreateDateTime(new Date().toString())
                .setUpdateDateTime(new Date().toString())
                .setSampleAlleleRequests(Collections.singletonList(request))
                .setOrganization(organization)
                .setId("sample_beacon_id")
                .setInfo(ImmutableMap.of("note", "This is a sample Beacon"))
                .build();
    }
}
