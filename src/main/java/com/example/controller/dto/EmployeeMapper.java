package com.example.controller.dto;

import com.example.controller.dto.EmployeeDTO;
import com.example.infrastructure.database.entity.EmployeeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {

    EmployeeDTO map(EmployeeEntity employeeEntity);
}
