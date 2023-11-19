package com.example.api.controller;

import com.example.api.dto.EmployeeDTO;
import com.example.api.dto.EmployeesDTO;
import com.example.api.mapper.EmployeeMapper;
import com.example.infrastructure.database.entity.EmployeeEntity;
import com.example.infrastructure.database.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping(EmployeesController.EMPLOYEES)
@AllArgsConstructor
public class EmployeesController {

    public static final String EMPLOYEES = "/employees";
    public static final String EMPLOYEE_ID = "/{employeeId}";
    public static final String EMPLOYEE_UPDATE_SALARY = "/{employeeId}/salary";
    public static final String EMPLOYEE_ID_RESULT = "/%s";

    private EmployeeRepository employeeRepository;
    private EmployeeMapper employeeMapper;


    @GetMapping(produces = {
            MediaType.APPLICATION_JSON_VALUE
//            MediaType.APPLICATION_XML_VALUE
    })
    public EmployeesDTO employeesList() {
        return EmployeesDTO.of(employeeRepository.findAll().stream()
                .map(employeeMapper::map)
                .toList());
    }

    @GetMapping(value = EMPLOYEE_ID, produces = {
            MediaType.APPLICATION_JSON_VALUE
//            MediaType.APPLICATION_XML_VALUE
    })
    public EmployeeDTO employeeDetails(@PathVariable Integer employeeId) {
        return employeeRepository.findById(employeeId)
                .map(employeeMapper::map)
                .orElseThrow(() -> new EntityNotFoundException(
                        "EmployeeEntity not Found, employeeId [%s]".formatted(employeeId)
                ));
    }

    @PostMapping
    @Transactional // normally in service that we unfortunately don't have
    public ResponseEntity<EmployeeDTO> addEmployee(
            @RequestBody EmployeeDTO employeeDTO
    ) {
        EmployeeEntity employeeEntity = EmployeeEntity.builder()
                .employeeId(employeeDTO.getEmployeeId())
                .name(employeeDTO.getName())
                .surname(employeeDTO.getSurname())
                .salary(employeeDTO.getSalary())
                .phone(employeeDTO.getPhone())
                .email(employeeDTO.getEmail())
                .build();
        EmployeeEntity created = employeeRepository.save(employeeEntity);
        return ResponseEntity
                .created(URI.create(EMPLOYEES + EMPLOYEE_ID_RESULT.formatted(created.getEmployeeId())))
                .build();
    }

    @PutMapping(value = EMPLOYEE_ID)
    public ResponseEntity<?> updateEmployee(
            @PathVariable Integer employeeId,
            @Valid @RequestBody EmployeeDTO employeeDTO
    ) {
        EmployeeEntity existingEmployee = findEmployeeByElseThrowException(employeeId);

        existingEmployee.setName(employeeDTO.getName());
        existingEmployee.setSurname(employeeDTO.getSurname());
        existingEmployee.setSalary(employeeDTO.getSalary());
        existingEmployee.setPhone(employeeDTO.getPhone());
        existingEmployee.setEmail(employeeDTO.getEmail());
        employeeRepository.save(existingEmployee);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = EMPLOYEE_ID)
    public ResponseEntity<?> deleteEmployee(
            @PathVariable Integer employeeId
    ) {
        findEmployeeByElseThrowException(employeeId);
        employeeRepository.deleteById(employeeId);

        return ResponseEntity.noContent().build();
    }
    @PatchMapping(value = EMPLOYEE_UPDATE_SALARY)
    public ResponseEntity<?> updateEmployeeSalary(
            @PathVariable Integer employeeId,
            @RequestParam BigDecimal newSalary
    ) {
        EmployeeEntity employeeEntity = findEmployeeByElseThrowException(employeeId);
        employeeEntity.setSalary(newSalary);
        employeeRepository.save(employeeEntity);

        return ResponseEntity.ok().build();
    }

    private EmployeeEntity findEmployeeByElseThrowException(Integer employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Employee not found, employeeId [%s]".formatted(employeeId)
                ));
    }

    @GetMapping(value = "test-header")
    public ResponseEntity<?> testHeader(
            @RequestHeader(value = HttpHeaders.ACCEPT) MediaType accept,
            @RequestHeader(value = "httpStatus") int httpStatus
    ) {
        return ResponseEntity
                .status(httpStatus)
                .body("Accepted: " + accept);


    }

}

