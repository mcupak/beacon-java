package com.dnastack.beacon.adater.variants.client.ga4gh;

import com.dnastack.beacon.adater.variants.client.ga4gh.exceptions.Ga4ghClientException;
import com.dnastack.beacon.adater.variants.client.ga4gh.retro.Ga4ghRetroService;
import com.dnastack.beacon.adater.variants.client.ga4gh.retro.Ga4ghRetroServiceFactory;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Message.Builder;
import ga4gh.Metadata.Dataset;
import ga4gh.MetadataServiceOuterClass.SearchDatasetsRequest;
import ga4gh.MetadataServiceOuterClass.SearchDatasetsResponse;
import ga4gh.References.ReferenceSet;
import ga4gh.VariantServiceOuterClass.SearchVariantSetsRequest;
import ga4gh.VariantServiceOuterClass.SearchVariantSetsResponse;
import ga4gh.VariantServiceOuterClass.SearchVariantsRequest;
import ga4gh.VariantServiceOuterClass.SearchVariantsResponse;
import ga4gh.Variants.CallSet;
import ga4gh.Variants.Variant;
import ga4gh.Variants.VariantSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.ga4gh.beacon.Beacon;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Executes requests to the given Ga4gh server.
 *
 * @author Artem (tema.voskoboynick@gmail.com)
 * @author Miro Cupak (mirocupak@gmail.com)
 * @version 1.0
 */
public class Ga4ghClient {

    private Beacon beacon;
    private HashMap<String, Ga4ghRetroService> ga4ghRetroServices;

    /**
     * A function that returns a single response page for the given request and throws {@link Ga4ghClientException} on
     * any IO error. In fact, this is just a copy of the the Java 8 function, but that throws {@link Ga4ghClientException}.
     */
    @FunctionalInterface
    private interface RequestExecutor<REQUEST, RESPONSE> {

        RESPONSE execute(REQUEST request) throws Ga4ghClientException;
    }

    public Ga4ghClient(Beacon beacon) {
        this.beacon = beacon;
        this.ga4ghRetroServices = new HashMap<>();

        beacon.getDatasets().forEach(beaconDataset -> ga4ghRetroServices.put(
                beaconDataset.getId(),
                Ga4ghRetroServiceFactory.create(beaconDataset.getExternalUrl()))
        );
    }

    public Beacon getBeacon() {
        return beacon;
    }

    private <T> T executeCall(Call<T> call) throws Ga4ghClientException {
        Response<T> response;
        try {
            response = call.execute();
        } catch (IOException | RuntimeException e) {
            throw new Ga4ghClientException("Error during communication to server.", e);
        }

        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw new Ga4ghClientException(String.format("Received error response from server. HTTP code: %s",
                    response.code()));
        }
    }

    /**
     * Loads all response pages for given request. Ga4gh server returns responses by pages.
     *
     * @return list of all response pages
     * @throws Ga4ghClientException on IO error
     */
    private <REQUEST extends GeneratedMessage, RESPONSE> List<RESPONSE> requestAllResponsePages(REQUEST request, RequestExecutor<REQUEST, RESPONSE> requestExecutor) throws Ga4ghClientException {
        List<RESPONSE> responsePages = new ArrayList<>();

        Builder requestBuilder = request.toBuilder();
        String nextPageToken = "";
        do {
            RESPONSE responsePage = loadResponsePage(requestBuilder, requestExecutor, nextPageToken);
            responsePages.add(responsePage);

            nextPageToken = invokeMethod(responsePage, "getNextPageToken");
        } while (StringUtils.isNotBlank(nextPageToken));

        return responsePages;
    }

    private <REQUEST extends GeneratedMessage, RESPONSE> RESPONSE loadResponsePage(Builder requestBuilder, RequestExecutor<REQUEST, RESPONSE> requestExecutor, String nextPageToken) throws Ga4ghClientException {
        invokeMethod(requestBuilder, "setPageToken", nextPageToken);
        //noinspection unchecked
        REQUEST requestWithPageToken = (REQUEST) requestBuilder.build();
        return requestExecutor.execute(requestWithPageToken);
    }

    @SuppressWarnings("unchecked")
    private <T> T invokeMethod(Object response, String methodName, Object... args) throws Ga4ghClientException {
        try {
            return (T) MethodUtils.invokeMethod(response, methodName, args);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new Ga4ghClientException(String.format("Couldn't invoke method %s.", methodName), e);
        }
    }

    public List<Dataset> searchDatasets() throws Ga4ghClientException {
        SearchDatasetsRequest request = SearchDatasetsRequest.newBuilder().build();

        List<SearchDatasetsResponse> allResponsePages = new ArrayList<>();

        for (Ga4ghRetroService ga4ghRetroService : ga4ghRetroServices.values()) {
            allResponsePages.addAll(requestAllResponsePages(request,
                    pagedRequest -> executeCall(
                            ga4ghRetroService.searchDatasets(
                                    pagedRequest))));
        }

        return allResponsePages.stream()
                .flatMap(responsePage -> responsePage.getDatasetsList().stream())
                .collect(Collectors.toList());
    }

    public List<Variant> searchVariants(String datasetId, String variantSetId, String referenceName, long start) throws Ga4ghClientException {
        SearchVariantsRequest request = SearchVariantsRequest.newBuilder()
                .setVariantSetId(variantSetId)
                .setReferenceName(referenceName)
                .setStart(start)
                .setEnd(start + 1)
                .build();

        List<SearchVariantsResponse> allResponsePages = requestAllResponsePages(request,
                pagedRequest -> executeCall(
                        ga4ghRetroServices.get(datasetId).searchVariants(
                                pagedRequest)));

        return allResponsePages.stream()
                .flatMap(responsePage -> responsePage.getVariantsList().stream())
                .collect(Collectors.toList());
    }

    public List<VariantSet> searchVariantSets(String datasetId) throws Ga4ghClientException {
        SearchVariantSetsRequest request = SearchVariantSetsRequest.newBuilder().setDatasetId(datasetId).build();

        List<SearchVariantSetsResponse> allResponsePages = requestAllResponsePages(request,
                pagedRequest -> executeCall(
                        ga4ghRetroServices.get(datasetId).searchVariantSets(
                                pagedRequest)));

        return allResponsePages.stream()
                .flatMap(responsePage -> responsePage.getVariantSetsList()
                        .stream())
                .collect(Collectors.toList());
    }

    public ReferenceSet loadReferenceSet(String datasetId, String referenceSetId) throws Ga4ghClientException {
        return executeCall(ga4ghRetroServices.get(datasetId).loadReferenceSet(referenceSetId));
    }

    public CallSet loadCallSet(String datasetId, String callSetId) throws Ga4ghClientException {
        return executeCall(ga4ghRetroServices.get(datasetId).loadCallSet(callSetId));
    }

    public boolean isExistDataset(String datasetId) {
        return ga4ghRetroServices.get(datasetId) != null;
    }

}
