package com.example.util;

import com.example.infrastructure.database.entity.EmployeeEntity;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class EntityFixtures {

    public static EmployeeEntity someEmployee1() {
        return EmployeeEntity.builder()
                .name("employee1")
                .surname("employees' 1 surname")
                .phone("+48 111 111 111")
                .email("employee1@gamil.com")
                .salary(new BigDecimal("1111.11"))
                .build();
    }
    public static EmployeeEntity someEmployee2() {
        return EmployeeEntity.builder()
                .name("employee2")
                .surname("employees' 2 surname")
                .phone("+48 222 222 222")
                .email("employee2@gamil.com")
                .salary(new BigDecimal("2222.22"))
                .build();
    }
    public static EmployeeEntity someEmployee3() {
        return EmployeeEntity.builder()
                .name("employee3")
                .surname("employees' 3 surname")
                .phone("+48 333 333 333")
                .email("employee3@gamil.com")
                .salary(new BigDecimal("3333.33"))
                .build();
    }
}
