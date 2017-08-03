package com.dnastack.beacon.adapter.variants.tests.successful

import com.dnastack.beacon.adapter.variants.BaseTest
import org.ga4gh.beacon.BeaconAlleleRequest
import org.ga4gh.beacon.BeaconAlleleResponse

import static com.dnastack.beacon.adapter.variants.TestData.*
import static org.assertj.core.api.Assertions.assertThat
/**
 * @author Artem (tema.voskoboynick@gmail.com)
 * @version 1.0
 */
class BeaconResponseTest extends BaseTest {

    @Override
    void doTest() {
        def referenceName = SEARCH_VARIANTS_TEST_REQUEST.referenceName
        def start = SEARCH_VARIANTS_TEST_REQUEST.start
        def referenceBases = TEST_VARIANT.referenceBases
        def alternateBases = TEST_VARIANT.getAlternateBases(0)
        def assemblyId = TEST_REFERENCE_SET.assemblyId
        def datasetIds = null
        def includeDatasetResponses = true

        BeaconAlleleRequest request = BeaconAlleleRequest.newBuilder()
                .setReferenceName(referenceName)
                .setStart(start)
                .setReferenceBases(referenceBases)
                .setAlternateBases(alternateBases)
                .setAssemblyId(assemblyId)
                .setDatasetIds(datasetIds)
                .setIncludeDatasetResponses(includeDatasetResponses)
                .build();

        testPostMethod(request)
        testGetMethod(request)
    }

    private void testGetMethod(BeaconAlleleRequest request) {
        BeaconAlleleResponse getMethodResponse = ADAPTER.getBeaconAlleleResponse(
                request.getReferenceName(),
                request.getStart(),
                request.getReferenceBases(),
                request.getAlternateBases(),
                request.getAssemblyId(),
                request.getDatasetIds(),
                request.getIncludeDatasetResponses());
        checkAssertions(getMethodResponse, request)
    }

    private void testPostMethod(BeaconAlleleRequest request) {
        BeaconAlleleResponse postMethodResponse = ADAPTER.getBeaconAlleleResponse(request);
        checkAssertions(postMethodResponse, request)
    }

    private void checkAssertions(BeaconAlleleResponse response, BeaconAlleleRequest request) {
        assertThat(response.alleleRequest).isEqualTo(request)
        assertThat(response.beaconId).isEqualTo(ADAPTER.getBeacon().getId())
        assertThat(response.datasetAlleleResponses).hasSize(1)
        assertThat(response.error).isNull()
        assertThat(response.exists).isTrue()

        def datasetResponse = response.datasetAlleleResponses.get(0)
        assertThat(datasetResponse.error).isNull()
        assertThat(datasetResponse.exists).isTrue()
        assertThat(datasetResponse.callCount).isEqualTo(TEST_VARIANT.callsCount)
        assertThat(datasetResponse.datasetId).isEqualTo(TEST_DATASET.id)
        assertThat(datasetResponse.frequency).isEqualTo(0.25d) // 4 total genotypes, only 1 matches (see test calls).
        assertThat(datasetResponse.sampleCount).isEqualTo(2) // 2 call sets with 2 distinct bio samples
        assertThat(datasetResponse.variantCount).isEqualTo(1) // 1 test variant
    }
}
