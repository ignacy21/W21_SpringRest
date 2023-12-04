package com.example.integration;

import com.example.controller.dto.EmployeeDTO;
import com.example.controller.dto.EmployeesDTO;
import com.example.integration.configuration.RestAssuredIntegrationTestBase;
import com.example.integration.support.EmployeesControllerTestSupport;
import com.example.util.DtoFixtures;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

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

}
