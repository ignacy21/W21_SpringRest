package com.example.integration.configuration;

import com.example.integration.support.ControllerTestSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.server.LocalServerPort;

public abstract class RestAssuredIntegrationTestBase
        extends AbstractIntegrationTest
        implements ControllerTestSupport {

    @LocalServerPort
    private int serverPort;

    @Value("${server.servlet.context-path}")
    private String basePath;

    protected static WireMockServer wireMockServer;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll() {
        wireMockServer = new WireMockServer(
                WireMockConfiguration.wireMockConfig()
                        .port(9999)
                        .templatingEnabled(false)
        );
        wireMockServer.start();
    }

    @AfterEach
    void afterEach() {
        wireMockServer.resetAll();
    }

    @AfterAll
    static void afterAll() {
        wireMockServer.stop();
    }

    @Override
    public ObjectMapper getObjetMapper() {
        return objectMapper;
    }

    public RequestSpecification requestSpecification() {
        return RestAssured
                .given()
                .config(getConfig())
                .basePath(basePath)
                .port(serverPort)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON);
    }

    private RestAssuredConfig getConfig() {
        return RestAssuredConfig
                .config()
                .objectMapperConfig(new ObjectMapperConfig()
                        .jackson2ObjectMapperFactory((p1, p2) -> objectMapper));
    }
}
