package com.dnastack.beacon.adapter.variants

import com.google.protobuf.ListValue
import com.google.protobuf.Value
import ga4gh.Metadata

import static ga4gh.MetadataServiceOuterClass.SearchDatasetsRequest
import static ga4gh.MetadataServiceOuterClass.SearchDatasetsResponse
import static ga4gh.References.ReferenceSet
import static ga4gh.VariantServiceOuterClass.*
import static ga4gh.Variants.*

/**
 * @author Artem (tema.voskoboynick@gmail.com)
 * @version 1.0
 */
public class TestData {
    def public static final TEST_DATASET = Metadata.Dataset.newBuilder()
            .setId("test-dataset")
            .build()

    def public static final TEST_CALL_SET_1 = CallSet.newBuilder()
            .setId("test-callset-1")
            .setBiosampleId("test-bio-sample-1")
            .build()

    def public static final TEST_CALL_SET_2 = CallSet.newBuilder()
            .setId("test-callset-2")
            .setBiosampleId("test-bio-sample-2")
            .build()

    def public static final TEST_CALL_1 = Call.newBuilder()
            .setCallSetId(TEST_CALL_SET_1.id)
            .setGenotype(ListValue.newBuilder()
                .addValues(Value.newBuilder().setNumberValue(1.0))
                .addValues(Value.newBuilder().setNumberValue(2.0)))
            .build()

    def public static final TEST_CALL_2 = Call.newBuilder()
            .setCallSetId(TEST_CALL_SET_2.id)
            .setGenotype(ListValue.newBuilder()
                .addValues(Value.newBuilder().setNumberValue(3.0))
                .addValues(Value.newBuilder().setNumberValue(4.0)))
            .build()

    def public static final TEST_VARIANT = Variant.newBuilder()
            .setId("test-variant")
            .setReferenceBases("test-reference-bases")
            .addAllAlternateBases(["test-alternate-base-1", "test-alternate-base-2", "test-alternate-base-3"])
            .addAllCalls([TEST_CALL_1, TEST_CALL_2])
            .build()

    def public static final TEST_REFERENCE_SET = ReferenceSet.newBuilder()
            .setId("test-reference-set")
            .setAssemblyId("test-assembly")
            .build()

    def public static final TEST_VARIANT_SET = VariantSet.newBuilder()
            .setId("test-variant-set")
            .setReferenceSetId(TEST_REFERENCE_SET.id)
            .build()

    def public static final SEARCH_DATASET_TEST_RESPONSE = SearchDatasetsResponse.newBuilder()
            .addDatasets(TEST_DATASET)
            .build()

    def public static final SEARCH_VARIANT_SETS_TEST_RESPONSE = SearchVariantSetsResponse.newBuilder()
            .addVariantSets(TEST_VARIANT_SET)
            .build()

    def public static final SEARCH_VARIANTS_TEST_RESPONSE = SearchVariantsResponse.newBuilder()
            .addVariants(TEST_VARIANT)
            .build()

    def public static final SEARCH_DATASET_TEST_REQUEST = SearchDatasetsRequest.newBuilder()
            .build()

    def public static final SEARCH_VARIANT_SETS_TEST_REQUEST = SearchVariantSetsRequest.newBuilder()
            .setDatasetId(TEST_DATASET.id)
            .build()

    def public static final SEARCH_VARIANTS_TEST_REQUEST = SearchVariantsRequest.newBuilder()
            .setVariantSetId(TEST_VARIANT_SET.id)
            .setReferenceName("test-reference-name")
            .setStart(100)
            .setEnd(101)
            .build()
}
