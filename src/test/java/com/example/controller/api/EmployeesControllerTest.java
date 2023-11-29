package com.example.controller.api;

import com.example.controller.dto.EmployeeDTO;
import com.example.controller.dto.EmployeeMapper;
import com.example.util.DtoFixtures;
import com.example.util.EntityFixtures;
import com.example.infrastructure.database.entity.EmployeeEntity;
import com.example.infrastructure.database.repository.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class EmployeesControllerTest {


    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeesController employeesController;

    @Test
    void thatRetrievingEmployeeWorksCorrectly() {
        // given
        Integer employeeId = 10;
        EmployeeEntity employeeEntity = EntityFixtures.someEmployee1();
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employeeEntity));
        Mockito.when(employeeMapper.map(employeeEntity)).thenReturn(DtoFixtures.someEmployee1());

        // when
        EmployeeDTO result = employeesController.employeeDetails(employeeId);

        // then
        Assertions.assertThat(result).isEqualTo(DtoFixtures.someEmployee1());
    }

    @Test
    void thatSavingEmployeeWorksCorrectly() {
        // given
        Mockito.when(employeeRepository.save(ArgumentMatchers.any(EmployeeEntity.class)))
                .thenReturn(EntityFixtures.someEmployee1().withEmployeeId(123));

        // when
        ResponseEntity<?> result = employeesController.addEmployee(DtoFixtures.someEmployee1());

        // then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }


}
