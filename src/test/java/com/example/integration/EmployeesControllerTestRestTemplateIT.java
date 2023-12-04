package com.example.integration;

import com.example.controller.dto.EmployeesDTO;
import com.example.integration.configuration.AbstractIntegrationTest;
import com.example.util.DtoFixtures;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeesControllerTestRestTemplateIT extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate testRestTemplate;

    @Test
    void applicationWorksCorrectly() {
        String url = "http://localhost:%s/w-21/employees".formatted(port);

        this.testRestTemplate.postForEntity(url, DtoFixtures.someEmployee1(), EmployeesDTO.class);

        ResponseEntity<EmployeesDTO> result = this.testRestTemplate.getForEntity(url, EmployeesDTO.class);

        EmployeesDTO body = result.getBody();
        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.getEmployees()).hasSizeGreaterThan(0);
    }
}
