package com.example.controller.util;

import com.example.controller.dto.EmployeeDTO;
import com.example.controller.dto.PetDTO;

import java.math.BigDecimal;

public class DtoFixtures {


    public static EmployeeDTO someEmployee1() {

        return EmployeeDTO.builder()
                .name("employee1")
                .surname("employees' 1 surname")
                .phone("+48 111 111 111")
                .email("employee1@gamil.com")
                .salary(new BigDecimal("1111.11"))
                .build();
    }
    public static EmployeeDTO someEmployee2() {

        return EmployeeDTO.builder()
                .name("employee2")
                .surname("employees' 2 surname")
                .phone("+48 222 222 222")
                .email("employee2@gamil.com")
                .salary(new BigDecimal("2222.22"))
                .build();
    }
    public static EmployeeDTO someEmployee3() {

        return EmployeeDTO.builder()
                .name("employee3")
                .surname("employees' 3 surname")
                .phone("+48 333 333 333")
                .email("employee3@gamil.com")
                .salary(new BigDecimal("3333.33"))
                .build();
    }

    public static PetDTO somePet() {
        return PetDTO.builder()
                .petId(1)
                .petStoreId(4L)
                .name("Lion")
                .category("Cats")
                .build();
    }
}
