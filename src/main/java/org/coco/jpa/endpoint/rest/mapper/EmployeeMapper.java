package org.coco.jpa.endpoint.rest.mapper;

import org.coco.jpa.model.Employee;
import org.coco.jpa.model.dto.EmployeeDto;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

  public EmployeeDto toRest(Employee employee) {
    return EmployeeDto.builder()
        .id(employee.getId())
        .firstName(employee.getFirstName())
        .email(employee.getEmail())
        .department(employee.getDepartment())
        .salary(employee.getSalary())
        .isActive(employee.getIsActive())
        .build();
  }
}
