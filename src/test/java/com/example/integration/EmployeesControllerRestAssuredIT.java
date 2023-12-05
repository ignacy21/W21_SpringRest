package com.example.integration;

import com.example.controller.dto.EmployeeDTO;
import com.example.controller.dto.EmployeesDTO;
import com.example.integration.configuration.RestAssuredIntegrationTestBase;
import com.example.integration.support.api.EmployeesControllerTestSupport;
import com.example.util.DtoFixtures;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;
import java.util.regex.Pattern;

public class EmployeesControllerRestAssuredIT
        extends RestAssuredIntegrationTestBase
        implements EmployeesControllerTestSupport {

    @Test
    void thatEmployeesListCanBeRetrievedCorrectly() {
        // given
        EmployeeDTO employee1 = DtoFixtures.someEmployee1();
        EmployeeDTO employee2 = DtoFixtures.someEmployee2();

        // when
        saveEmployee(employee1);
        saveEmployee(employee2);

        EmployeesDTO employeesDTO = listEmployees();

        // then
        Assertions.assertThat(employeesDTO.getEmployees())
                // we don't know what id server (database) will generate
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("employeeId")
                .contains(employee1.withPets(Set.of()), employee2.withPets(Set.of()));

    }

    @Test
    void thatEmployeeCanBeCreatedCorrectly() {
        // given
        EmployeeDTO employee1 = DtoFixtures.someEmployee1();

        // when
        ExtractableResponse<Response> response = saveEmployee(employee1);

        // then
        String responseAsString = response.body().asString();
        Assertions.assertThat(responseAsString).isEmpty();
        Assertions.assertThat(response.headers().get("Location").getValue())
                .matches(Pattern.compile("/employees/\\d"));
    }

    @Test
    void thatCreatedEmployeeCanBeRetrievedCorrectly() {
        // given
        EmployeeDTO employee1 = DtoFixtures.someEmployee1();

        // when
        ExtractableResponse<Response> response = saveEmployee(employee1);
        String employeeDetailsPath = response.headers().get("Location").getValue();

        EmployeeDTO employee = getEmployee(employeeDetailsPath);

        // then
        Assertions.assertThat(employee)
                .usingRecursiveComparison()
                .ignoringFields("employeeId")
                .isEqualTo(employee1.withPets(Set.of()));
    }

    @Test
    void thatEmployeeIsUpdatedCorrectly() {
        // given
        EmployeeDTO employee1 = DtoFixtures.someEmployee1();
        String newSalary = "123.69";

        // when
        ExtractableResponse<Response> response = saveEmployee(employee1);
        String employeeDetailsPath = response.headers().get("Location").getValue();
        EmployeeDTO retrievedEmployee = getEmployee(employeeDetailsPath);
        Integer retrievedEmployeeId = retrievedEmployee.getEmployeeId();

        updateEmployeesSalary(retrievedEmployeeId, new BigDecimal(newSalary));

        EmployeeDTO employeeById = getEmployeeById(retrievedEmployeeId);

        // then
        Assertions.assertThat(employeeById.getSalary())
                .isEqualTo(newSalary);

    }
}
