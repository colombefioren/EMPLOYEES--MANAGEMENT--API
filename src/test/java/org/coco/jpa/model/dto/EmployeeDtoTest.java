package org.coco.jpa.model.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.coco.jpa.model.Department;
import org.junit.jupiter.api.Test;

class EmployeeDtoTest {

  @Test
  void should_create_with_builder() {
    var dto =
        EmployeeDto.builder()
            .id(1L)
            .firstName("John")
            .email("john@test.com")
            .department(Department.IT)
            .salary(new BigDecimal("2500"))
            .isActive(true)
            .build();

    assertEquals(1L, dto.getId());
    assertEquals("John", dto.getFirstName());
    assertEquals("john@test.com", dto.getEmail());
    assertEquals(Department.IT, dto.getDepartment());
    assertEquals(new BigDecimal("2500"), dto.getSalary());
    assertTrue(dto.getIsActive());
  }

  @Test
  void should_use_no_args_constructor() {
    var dto = new EmployeeDto();
    assertNull(dto.getId());
  }

  @Test
  void should_use_all_args_constructor() {
    var dto =
        new EmployeeDto(1L, "Jane", "jane@test.com", Department.RH, new BigDecimal("3000"), false);

    assertEquals("Jane", dto.getFirstName());
    assertFalse(dto.getIsActive());
  }
}
