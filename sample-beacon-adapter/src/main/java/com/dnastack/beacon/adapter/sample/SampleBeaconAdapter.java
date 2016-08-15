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
package com.dnastack.beacon.adapter.sample;

import com.dnastack.beacon.adapter.api.BeaconAdapter;
import com.dnastack.beacon.utils.AdapterConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.ga4gh.beacon.*;

import javax.enterprise.context.Dependent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status;

/**
 * Beacon adapter that returns sample data.
 *
 * @author Artem (tema.voskoboynick@gmail.com)
 * @author Patrick Magee (patrickmageee@gmail.com)
 */
@Dependent
public class SampleBeaconAdapter implements BeaconAdapter {

    @Override
    public Beacon getBeacon() {
        return SampleData.BEACON;
    }

    @Override
    public void initAdapter(AdapterConfig adapterConfig) {
    }

    @Override
    public BeaconAlleleResponse getBeaconAlleleResponse(BeaconAlleleRequest request) {
        BeaconAlleleResponse response = new BeaconAlleleResponse();
        response.setBeaconId(SampleData.BEACON.getId());
        response.setAlleleRequest(request);

        DatasetsQueryResult datasetsResult = queryDatasets(request.getDatasetIds(), request.getAssemblyId(),
                request.getReferenceName(), request.getStart(), request.getReferenceBases(), request.getAlternateBases());
        response.setExists(datasetsResult.exists);
        if (BooleanUtils.isTrue(request.getIncludeDatasetResponses())) {
            response.setDatasetAlleleResponses(datasetsResult.responses);
        }

        return response;
    }

    @Override
    public BeaconAlleleResponse getBeaconAlleleResponse(String referenceName, Long start, String referenceBases, String alternateBases, String assemblyId, List<String> datasetIds, Boolean includeDatasetResponses) {
        BeaconAlleleRequest request = createRequest(referenceName, start, referenceBases, alternateBases, assemblyId, datasetIds, includeDatasetResponses);
        return getBeaconAlleleResponse(request);
    }

    private BeaconAlleleRequest createRequest(String referenceName, Long start, String referenceBases, String alternateBases, String assemblyId, List<String> datasetIds, Boolean includeDatasetResponses) {
        return BeaconAlleleRequest.newBuilder()
                .setReferenceName(referenceName)
                .setStart(start)
                .setReferenceBases(referenceBases)
                .setAlternateBases(alternateBases)
                .setAssemblyId(assemblyId)
                .setDatasetIds(datasetIds)
                .setIncludeDatasetResponses(includeDatasetResponses)
                .build();
    }

    private DatasetsQueryResult queryDatasets(List<String> datasetIds, String assemblyId, String referenceName, Long start,
                                              String referenceBases, String alternateBases) {
        if (CollectionUtils.isEmpty(datasetIds)) {
            // All datasets requested.
            datasetIds = SampleData.DATASETS.keySet().stream().collect(Collectors.toList());
        }

        return doQueryDatasets(datasetIds, assemblyId, referenceName, start, referenceBases, alternateBases);
    }

    private DatasetsQueryResult doQueryDatasets(List<String> datasetIds, String assemblyId, String referenceName, Long start,
                                                String referenceBases, String alternateBases) {
        DatasetsQueryResult datasetsResult = new DatasetsQueryResult();

        for (String datasetId : datasetIds) {
            BeaconDatasetAlleleResponse datasetResponse = queryDataset(datasetId, assemblyId, referenceName, start,
                    referenceBases, alternateBases);

            // Find in descending priority: true, false, null.
            Boolean existsInDataset = datasetResponse.getExists();
            if (datasetsResult.exists == null || BooleanUtils.isTrue(existsInDataset)) {
                datasetsResult.exists = existsInDataset;
            }

            // Collect any error, if exists.
            if (datasetResponse.getError() != null) {
                datasetsResult.error = datasetResponse.getError();
            }

            datasetsResult.responses.add(datasetResponse);
        }

        return datasetsResult;
    }

    @SuppressWarnings("unchecked")
    private BeaconDatasetAlleleResponse queryDataset(String datasetId, String assemblyId, String referenceName, long start, String referenceBases, String alternateBases) {
        Map<String, Map> dataset = SampleData.DATASETS.get(datasetId);
        if (dataset == null) {
            return createErrorDatasetResponse(datasetId, Status.NOT_FOUND.getStatusCode(), "Dataset not found");
        }

        Map<String, Map> assembly = dataset.get(assemblyId);
        if (assembly == null) {
            return createErrorDatasetResponse(datasetId, Status.NOT_FOUND.getStatusCode(), "Assembly not found");
        }

        Map<Long, Map> reference = assembly.get(referenceName);
        if (reference == null) {
            return createErrorDatasetResponse(datasetId, Status.NOT_FOUND.getStatusCode(), "Reference not found");
        }

        Map<String, String> bases = reference.get(start);
        if (bases == null) {
            return createSuccessfulDatasetResponse(datasetId, false);
        }

        if (bases.get("referenceBases").equals(referenceBases) && bases.get("alternateBases").equals(alternateBases)) {
            return createSuccessfulDatasetResponse(datasetId, true);
        } else {
            return createSuccessfulDatasetResponse(datasetId, false);
        }
    }

    private BeaconDatasetAlleleResponse createErrorDatasetResponse(String datasetId, int errorCode, String message) {
        BeaconDatasetAlleleResponse response = new BeaconDatasetAlleleResponse();
        response.setDatasetId(datasetId);

        BeaconError error = BeaconError.newBuilder()
                .setErrorCode(errorCode)
                .setMessage(message).build();
        response.setError(error);

        return response;
    }

    private BeaconDatasetAlleleResponse createSuccessfulDatasetResponse(String datasetId, boolean exists) {
        BeaconDatasetAlleleResponse response = BeaconDatasetAlleleResponse.newBuilder(SampleData.BASE_DATASET_RESPONSE).build();
        response.setDatasetId(datasetId);
        response.setExists(exists);
        return response;
    }

    private static class DatasetsQueryResult {
        Boolean exists;
        BeaconError error;
        List<BeaconDatasetAlleleResponse> responses = new ArrayList<>();
    }
}
