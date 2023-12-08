package com.example.controller.api;

import com.example.controller.dao.PetDAO;
import com.example.controller.dto.EmployeeDTO;
import com.example.controller.dto.EmployeeMapper;
import com.example.infrastructure.database.entity.EmployeeEntity;
import com.example.infrastructure.database.repository.EmployeeRepository;
import com.example.infrastructure.database.repository.PetRepository;
import com.example.util.DtoFixtures;
import com.example.util.EntityFixtures;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = EmployeesController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeesControllerMvcTest {

    private final MockMvc mockMvc;


    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private PetRepository petRepository;

    @MockBean
    private PetDAO petDAO;

    @MockBean
    private EmployeeMapper employeeMapper;

    @Test
    public void thatEmployeeCanBeRetrieved() throws Exception {
        // given
        int employeeId = 123;
        EmployeeEntity employeeEntity = EntityFixtures.someEmployee1().withEmployeeId(employeeId);
        EmployeeDTO employeeDTO = DtoFixtures.someEmployee1().withEmployeeId(employeeId);

        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employeeEntity));
        Mockito.when(employeeMapper.map(ArgumentMatchers.any(EmployeeEntity.class))).thenReturn(employeeDTO);
        //when, then
        String endpoint = EmployeesController.EMPLOYEES + EmployeesController.EMPLOYEE_ID;
        mockMvc.perform(MockMvcRequestBuilders.get(endpoint, employeeId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId", is(employeeDTO.getEmployeeId())))
                .andExpect(jsonPath("$.name", is(employeeDTO.getName())))
                .andExpect(jsonPath("$.surname", is(employeeDTO.getSurname())))
                .andExpect(jsonPath("$.phone", is(employeeDTO.getPhone())))
                .andExpect(jsonPath("$.email", is(employeeDTO.getEmail())))
                .andExpect(jsonPath("$.salary", is(employeeDTO.getSalary()), BigDecimal.class));
    }

    @Test
    void thatEmailValidationWorksCorrectly() throws Exception {
        //given
        final String request = """
                {
                    "email": "badEmail"
                }
                """;
        //when, then
        mockMvc.perform(
                        post(EmployeesController.EMPLOYEES)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorId", Matchers.notNullValue()));
    }

    @ParameterizedTest
    @MethodSource
    void thatPhoneValidationWorksCorrectly(Boolean correctPhone, String phone) throws Exception {
        final String request = """
                {
                    "phone": "%s"
                }
                """.formatted(phone);
        Mockito.when(employeeRepository.save(ArgumentMatchers.any(EmployeeEntity.class)))
                .thenReturn(EntityFixtures.someEmployee1().withEmployeeId(123));

        //when, then
        if (correctPhone) {
            String expectedRedirect = EmployeesController.EMPLOYEES
                    + EmployeesController.EMPLOYEE_ID_RESULT.formatted(123);
            mockMvc.perform(post(EmployeesController.EMPLOYEES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isCreated())
                    .andExpect(redirectedUrl(expectedRedirect));
        } else {
            mockMvc.perform(post(EmployeesController.EMPLOYEES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorId", Matchers.notNullValue()));
        }
    }

    public static Stream<Arguments> thatPhoneValidationWorksCorrectly() {
        return Stream.of(
                Arguments.of(false, ""),
                Arguments.of(false, "+48 504 203 260@@"),
                Arguments.of(false, "+48.504.203.260"),
                Arguments.of(false, "+55(123) 456-78-90-"),
                Arguments.of(false, "+55(123) - 456-78-90"),
                Arguments.of(false, "504.203.260"),
                Arguments.of(false, " "),
                Arguments.of(false, "-"),
                Arguments.of(false, "()"),
                Arguments.of(false, "() + ()"),
                Arguments.of(false, "(21 7777"),
                Arguments.of(false, "+48 (21)"),
                Arguments.of(false, "+"),
                Arguments.of(false, " 1"),
                Arguments.of(false, "1"),
                Arguments.of(false, "+48 (12) 504 203 260"),
                Arguments.of(false, "+48 (12) 504-203-260"),
                Arguments.of(false, "+48(12)504203260"),
                Arguments.of(false, "555-5555-555"),
                Arguments.of(true, "+48 504 203 260")
        );
    }

}
