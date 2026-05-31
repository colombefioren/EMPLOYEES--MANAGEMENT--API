package org.coco.jpa.model.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.coco.jpa.model.Department;
import org.junit.jupiter.api.Test;

class InternDtoTest {

  @Test
  void should_create_with_builder() {
    var dto =
        InternDto.builder()
            .id(1L)
            .firstName("Alice")
            .email("alice@test.com")
            .department(Department.RH)
            .salary(new BigDecimal("500"))
            .isRemunerate(true)
            .managerId(2L)
            .build();

    assertEquals(1L, dto.getId());
    assertEquals("Alice", dto.getFirstName());
    assertEquals("alice@test.com", dto.getEmail());
    assertEquals(Department.RH, dto.getDepartment());
    assertEquals(new BigDecimal("500"), dto.getSalary());
    assertTrue(dto.getIsRemunerate());
    assertEquals(2L, dto.getManagerId());
  }

  @Test
  void should_use_no_args_constructor() {
    var dto = new InternDto();
    assertNull(dto.getId());
  }

  @Test
  void should_use_all_args_constructor() {
    var dto =
        new InternDto(1L, "Bob", "bob@test.com", Department.IT, new BigDecimal("600"), true, 3L);

    assertEquals("Bob", dto.getFirstName());
    assertTrue(dto.getIsRemunerate());
    assertEquals(3L, dto.getManagerId());
  }
}
