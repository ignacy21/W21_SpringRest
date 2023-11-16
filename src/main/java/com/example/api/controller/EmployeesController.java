package com.example.api.controller;

import com.example.api.dto.EmployeeDTO;
import com.example.api.dto.EmployeesDTO;
import com.example.api.mapper.EmployeeMapper;
import com.example.infrastructure.database.entity.EmployeeEntity;
import com.example.infrastructure.database.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
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

//    @GetMapping("/{employeeId}")
//    public ResponseEntity<?> showEmployeeDetails(@PathVariable Integer employeeId) {
//        return null;
//    }
//
//    @PostMapping
//    public ResponseEntity<?> addEmployee(
//            @RequestParam(value = "name") String name,
//            @RequestParam(value = "surname") String surname,
//            @RequestParam(value = "salary") String salary
//    ) {
//        // TODO
//        return null;
//    }
//
//    @PutMapping("/{employeeId}")
//    public ResponseEntity<?> showEmployeeDetails(
//            @PathVariable Integer employeeId,
//            @RequestParam String name,
//            @RequestParam String surname,
//            @RequestParam String salary
//    ) {
//        // TODO
//        return null;
//    }
//
//    @DeleteMapping("/{employeeId}")
//    public ResponseEntity<?> deleteEmployee(@PathVariable Integer employeeId) {
//        // TODO
//        return null;
//    }
//
//    @PatchMapping("/{employeeId}")
//    public ResponseEntity<?> partiallyUpdateEmployee(@PathVariable Integer employeeId) {
//        // TODO
//        return null;
//    }
}

