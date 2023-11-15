package com.example.api.controller;

import com.example.api.dto.EmployeeDTO;
import com.example.api.dto.EmployeesDTO;
import com.example.api.mapper.EmployeeMapper;
import com.example.infrastructure.database.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(EmployeesController.EMPLOYEES)
@AllArgsConstructor
public class EmployeesController {

    public static final String EMPLOYEES = "/employees";
    public static final String EMPLOYEE_ID = "/{employeeId}";

    private EmployeeRepository employeeRepository;
    private EmployeeMapper employeeMapper;


    @GetMapping
    public EmployeesDTO employeesList() {
        return EmployeesDTO.of(employeeRepository.findAll().stream()
                .map(employeeMapper::map)
                .toList());
    }
    @GetMapping(value = EMPLOYEE_ID)
    public EmployeeDTO employeeDetails(@PathVariable Integer employeeId) {
        return employeeRepository.findById(employeeId)
                .map(employeeMapper::map)
                .orElseThrow(() -> new EntityNotFoundException(
                        "EmployeeEntity not Found, employeeId [%s]".formatted(employeeId)
                ));
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

