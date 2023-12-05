package com.example.integration.support;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import java.util.Map;

public interface WireMockTestSupport {

    default void stubForPet(final WireMockServer wireMockServer, final Long petId) {
        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/pet/%s".formatted(petId)))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("wiremock/petById.json")
                        .withTransformerParameters(Map.of("petId", petId))
                        .withTransformers("response-template")
                ));

    }
}
