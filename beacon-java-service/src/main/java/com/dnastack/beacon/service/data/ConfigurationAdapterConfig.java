package com.dnastack.beacon.service.data;

import avro.shaded.com.google.common.collect.ImmutableList;
import com.dnastack.beacon.utils.AdapterConfig;
import com.dnastack.beacon.utils.ConfigValue;

/**
 * @author Andrey Mochalov (mochalovandrey@gmail.com)
 */
public class ConfigurationAdapterConfig {

    public static AdapterConfig getAdapterConfigForGoogleGenomics() {
        return AdapterConfig.builder()
                .name("Google Genomics Adapter")
                .adapterClass("VariantsBeaconAdapter.class")
                .configValues(ImmutableList.of(
                        ConfigValue.builder()
                                .name("apiKey")
                                .value("AIzaSyCIIzOm0yU2gCLLLUTW6gs0INWp6knvwlE")
                                .build(),
                        ConfigValue.builder()
                                .name("beaconJson")
                                .value("{\n" +
                                        "  \"id\": \"sample-beacon\",\n" +
                                        "  \"name\": \"variant_test_beacon\",\n" +
                                        "  \"apiVersion\": \"0.3\",\n" +
                                        "  \"organization\": {\n" +
                                        "    \"id\": \"variant_org\",\n" +
                                        "    \"name\": \"variant Adapter organization\",\n" +
                                        "    \"description\": \"test organization for the variant Beacon adapter\",\n" +
                                        "    \"address\": \"99 Lambda Drive, Consumer, Canada\",\n" +
                                        "    \"welcomeUrl\": \"www.welcome.com\",\n" +
                                        "    \"contactUrl\": \"www.contact.com\",\n" +
                                        "    \"logoUrl\": \"www.logo.com\"\n" +
                                        "  },\n" +
                                        "  \"description\": \"This beacon demonstrates the usage of the variantBeaconAdapter\",\n" +
                                        "  \"version\": \"1\",\n" +
                                        "  \"welcomeUrl\": \"www.welcome.com\",\n" +
                                        "  \"alternativeUrl\": \"www.alternative.com\",\n" +
                                        "  \"createDateTime\": \"2016/07/23 19:23:11\",\n" +
                                        "  \"updateDateTime\": \"2016/07/23 19:23:11\",\n" +
                                        "  \"datasets\": [\n" +
                                        "    {\n" +
                                        "      \"id\": \"10473108253681171589\",\n" +
                                        "      \"name\": \"variant-test-gt\",\n" +
                                        "      \"description\": \"variant Adapter test dataset which includes sample / gt info\",\n" +
                                        "      \"assemblyId\": \"test-assembly\",\n" +
                                        "      \"createDateTime\": \"2016/07/23 19:23:11\",\n" +
                                        "      \"updateDateTime\": \"2016/07/23 19:23:11\",\n" +
                                        "      \"version\": \"1\",\n" +
                                        "      \"variantCount\": 26,\n" +
                                        "      \"sampleCount\": 1,\n" +
                                        "      \"externalUrl\": \"https://genomics.googleapis.com/v1/\"\n" +
                                        "    }\n" +
                                        "  ],\n" +
                                        "  \"sampleAlleleRequests\": [\n" +
                                        "    {\n" +
                                        "      \"referenceName\": \"1\",\n" +
                                        "      \"start\": 10176,\n" +
                                        "      \"referenceBases\": \"A\",\n" +
                                        "      \"alternateBases\": \"AC\",\n" +
                                        "      \"assemblyId\": \"GRCh37\",\n" +
                                        "      \"datasetIds\": [\n" +
                                        "        \"10473108253681171589\"\n" +
                                        "      ],\n" +
                                        "      \"includeDatasetResponses\": true\n" +
                                        "    }\n" +
                                        "  ]\n" +
                                        "}")
                                .build()))
                .build();
    }

    public static AdapterConfig getAdapterConfigForPhenopackets() {
        return AdapterConfig.builder()
                .name("Phenopackets")
                .adapterClass("Phenopackets class")
                .configValues(ImmutableList.of(
                        ConfigValue.builder()
                                .name("beaconJson")
                                .value("{\n" +
                                        "    \"id\": \"sample-beacon\",\n" +
                                        "    \"name\": \"variant_test_beacon\",\n" +
                                        "    \"apiVersion\": \"0.3\",\n" +
                                        "    \"organization\": {\n" +
                                        "        \"id\": \"variant_org\",\n" +
                                        "        \"name\": \"variant Adapter organization\",\n" +
                                        "        \"description\": \"test organization for the variant Beacon adapter\",\n" +
                                        "        \"address\": \"99 Lambda Drive, Consumer, Canada\",\n" +
                                        "        \"welcomeUrl\": \"www.welcome.com\",\n" +
                                        "        \"contactUrl\": \"www.contact.com\",\n" +
                                        "        \"logoUrl\": \"www.logo.com\"\n" +
                                        "    },\n" +
                                        "    \"description\": \"This beacon demonstrates the usage of the variantBeaconAdapter\",\n" +
                                        "    \"version\": \"1\",\n" +
                                        "    \"welcomeUrl\": \"www.welcome.com\",\n" +
                                        "    \"alternativeUrl\": \"www.alternative.com\",\n" +
                                        "    \"createDateTime\": \"2016/07/23 19:23:11\",\n" +
                                        "    \"updateDateTime\": \"2016/07/23 19:23:11\",\n" +
                                        "    \"datasets\": [\n" +
                                        "        {\n" +
                                        "            \"id\": \"test-dataset\",\n" +
                                        "            \"name\": \"variant-test-gt\",\n" +
                                        "            \"description\": \"variant Adapter test dataset which includes sample / gt info\",\n" +
                                        "            \"assemblyId\": \"test-assembly\",\n" +
                                        "            \"createDateTime\": \"2016/07/23 19:23:11\",\n" +
                                        "            \"updateDateTime\": \"2016/07/23 19:23:11\",\n" +
                                        "            \"version\": \"1\",\n" +
                                        "            \"variantCount\": 26,\n" +
                                        "            \"sampleCount\": 1,\n" +
                                        "            \"externalUrl\": \"http://localhost:8089/\"\n" +
                                        "        }\n" +
                                        "    ],\n" +
                                        "    \"sampleAlleleRequests\": [\n" +
                                        "        {\n" +
                                        "            \"referenceName\": \"test-reference-name\",\n" +
                                        "            \"start\": 100,\n" +
                                        "            \"referenceBases\": \"T\",\n" +
                                        "            \"alternateBases\": \"C\",\n" +
                                        "            \"assemblyId\": \"grch37\",\n" +
                                        "            \"datasetIds\": [\n" +
                                        "                \"variant-test-gt\"\n" +
                                        "            ],\n" +
                                        "            \"includeDatasetResponses\": true\n" +
                                        "        },\n" +
                                        "        {\n" +
                                        "            \"referenceName\": \"1\",\n" +
                                        "            \"start\": 10109,\n" +
                                        "            \"referenceBases\": \"A\",\n" +
                                        "            \"alternateBases\": \"T\",\n" +
                                        "            \"assemblyId\": \"grch37\",\n" +
                                        "            \"datasetIds\": [\n" +
                                        "                \"variant-test-no-gt\"\n" +
                                        "            ],\n" +
                                        "            \"includeDatasetResponses\": true\n" +
                                        "        }\n" +
                                        "    ]\n" +
                                        "}")
                                .build(),
                        ConfigValue.builder()
                                .name("phenoPacket")
                                .value("{\n" +
                                        "  \"id\" : \"id\",\n" +
                                        "  \"title\" : \"Patient with a phenotype\",\n" +
                                        "  \"variants\" : [ {\n" +
                                        "    \"assembly\" : \"GRCh37\",\n" +
                                        "    \"startPosition\" : 10571,\n" +
                                        "    \"endPosition\" : 10572,\n" +
                                        "    \"refBases\" : \"A\",\n" +
                                        "    \"altBases\" : \"AC\"\n" +
                                        "  }, {\n" +
                                        "    \"assembly\" : \"GRCh37\",\n" +
                                        "    \"startPosition\" : 10572,\n" +
                                        "    \"endPosition\" : 10573,\n" +
                                        "    \"refBases\" : \"A\",\n" +
                                        "    \"altBases\" : \"BC\"\n" +
                                        "  } ]\n" +
                                        "}")
                                .build()
                ))
                .build();
    }

}
