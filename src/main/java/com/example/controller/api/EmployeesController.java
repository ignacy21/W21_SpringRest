package com.example.controller.api;

import com.example.controller.dao.PetDAO;
import com.example.controller.dto.EmployeeDTO;
import com.example.controller.dto.EmployeesDTO;
import com.example.controller.dto.EmployeeMapper;
import com.example.infrastructure.database.entity.EmployeeEntity;
import com.example.infrastructure.database.entity.PetEntity;
import com.example.infrastructure.database.repository.EmployeeRepository;
import com.example.infrastructure.database.repository.PetRepository;
import com.example.infrastructure.petstore.Pet;
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
    public static final String EMPLOYEE_UPDATE_PET = "/{employeeId}/pet/{petId}";

    private EmployeeRepository employeeRepository;
    private EmployeeMapper employeeMapper;
    private PetDAO petDao;
    private PetRepository petRepository;


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
            @Valid @RequestBody EmployeeDTO employeeDTO
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
        EmployeeEntity existingEmployee = findEmployeeOrElseThrowException(employeeId);

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
        findEmployeeOrElseThrowException(employeeId);
        employeeRepository.deleteById(employeeId);

        return ResponseEntity.noContent().build();
    }
    @PatchMapping(value = EMPLOYEE_UPDATE_SALARY)
    public ResponseEntity<?> updateEmployeeSalary(
            @PathVariable Integer employeeId,
            @RequestParam BigDecimal newSalary
    ) {
        EmployeeEntity employeeEntity = findEmployeeOrElseThrowException(employeeId);
        employeeEntity.setSalary(newSalary);
        employeeRepository.save(employeeEntity);

        return ResponseEntity.ok().build();
    }

    @PatchMapping(EMPLOYEE_UPDATE_PET)
    public ResponseEntity<?> updateEmployeeWithPet(
            @PathVariable Integer employeeId,
            @PathVariable Integer petId
    ) {
        EmployeeEntity existingEmployee = findEmployeeOrElseThrowException(employeeId);

        Pet petFromStore = petDao.getPet(Long.valueOf(petId))
                .orElseThrow(() -> new RuntimeException(
                        "Pet with id [%s] could not be retrieved".formatted(petId)
                ));

        PetEntity newPet = PetEntity.builder()
                .petStorePetId(petFromStore.getId())
                .name(petFromStore.getName())
                .status(petFromStore.getStatus())
                .employee(existingEmployee)
                .build();

        petRepository.save(newPet);

        return ResponseEntity.ok().build();
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




    private EmployeeEntity findEmployeeOrElseThrowException(Integer employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Employee not found, employeeId [%s]".formatted(employeeId)
                ));
    }
}

