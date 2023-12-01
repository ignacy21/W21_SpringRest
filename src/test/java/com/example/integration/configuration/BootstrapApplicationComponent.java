package com.example.integration.configuration;

import com.example.controller.dto.EmployeesDTO;
import com.example.util.DtoFixtures;
import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
public class BootstrapApplicationComponent {

    @LocalServerPort
    private int port;

    private final TestRestTemplate testRestTemplate;

    @Test
    void applicationWorksCorrectly() {
        String url = "http://localhost:%s/w-20/employees".formatted(port);

        this.testRestTemplate.postForEntity(url, DtoFixtures.someEmployee1(), EmployeesDTO.class);

        ResponseEntity<EmployeesDTO> result = this.testRestTemplate.getForEntity(url, EmployeesDTO.class);

        EmployeesDTO body = result.getBody();
        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.getEmployees()).hasSizeGreaterThan(0);
    }
}
