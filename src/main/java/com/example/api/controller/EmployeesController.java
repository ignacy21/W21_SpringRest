package com.example.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(EmployeesController.EMPLOYEES)
public class EmployeesController {

    public static final String EMPLOYEES = "/employees";

    @PostMapping
    public ResponseEntity<?> addEmployee(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "surname") String surname,
            @RequestParam(value = "salary") String salary
    ) {
        // TODO
        return null;
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<?> showEmployeeDetails(@PathVariable Integer employeeId) {
        return null;
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<?> showEmployeeDetails(
            @PathVariable Integer employeeId,
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String salary
    ) {
        // TODO
        return null;
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Integer employeeId) {
        // TODO
        return null;
    }

    @PatchMapping("/{employeeId}")
    public ResponseEntity<?> partiallyUpdateEmployee(@PathVariable Integer employeeId) {
        // TODO
        return null;
    }

}
