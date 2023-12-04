package com.example.integration.support;

import ch.qos.logback.core.model.conditional.ElseModel;
import com.example.controller.api.EmployeesController;
import com.example.controller.dto.EmployeeDTO;
import com.example.controller.dto.EmployeesDTO;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;

public interface EmployeesControllerTestSupport {

    RequestSpecification requestSpecification();

    default EmployeesDTO listEmployees() {
        return requestSpecification()
                .get(EmployeesController.EMPLOYEES)
                .then()
                .statusCode(HttpStatus.OK.value())
                .and()
                .extract()
                .as(EmployeesDTO.class);
    }

    default EmployeeDTO getEmployeeById(final Integer employeeId) {
        return requestSpecification()
                .get(EmployeesController.EMPLOYEES + EmployeesController.EMPLOYEE_ID, employeeId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .and()
                .extract()
                .as(EmployeeDTO.class);
    }

    default ExtractableResponse<Response> saveEmployee(final EmployeeDTO employeeDTO) {
        return requestSpecification()
                .body(employeeDTO)
                .post(EmployeesController.EMPLOYEES)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .and()
                .extract();
    }


    default void updateEmployee(final Integer employeeId, final Long petId) {
        String endPoint = EmployeesController.EMPLOYEES + EmployeesController.EMPLOYEE_UPDATE_PET;
        requestSpecification()
                .patch(endPoint)
                .then()
                .statusCode(HttpStatus.OK.value());
    }
}
