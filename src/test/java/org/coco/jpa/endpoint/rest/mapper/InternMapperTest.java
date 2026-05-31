package org.coco.jpa.endpoint.rest.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.coco.jpa.model.Department;
import org.coco.jpa.model.Employee;
import org.coco.jpa.model.Intern;
import org.junit.jupiter.api.Test;

class InternMapperTest {

  private final InternMapper mapper = new InternMapper();

  @Test
  void to_rest_should_map_all_fields() {
    var manager = new Employee();
    manager.setId(1L);
    var intern = new Intern();
    intern.setId(2L);
    intern.setFirstName("Alice");
    intern.setEmail("alice@test.com");
    intern.setDepartment(Department.RH);
    intern.setSalary(new BigDecimal("500"));
    intern.setIsRemunerate(true);
    intern.setManager(manager);

    var dto = mapper.toRest(intern);

    assertEquals(2L, dto.getId());
    assertEquals("Alice", dto.getFirstName());
    assertEquals("alice@test.com", dto.getEmail());
    assertEquals(Department.RH, dto.getDepartment());
    assertEquals(new BigDecimal("500"), dto.getSalary());
    assertTrue(dto.getIsRemunerate());
    assertEquals(1L, dto.getManagerId());
  }

  @Test
  void to_rest_should_handle_null_manager() {
    var intern = new Intern();
    intern.setId(3L);
    intern.setFirstName("Bob");

    var dto = mapper.toRest(intern);

    assertEquals("Bob", dto.getFirstName());
    assertNull(dto.getManagerId());
  }

  @Test
  void to_rest_should_handle_null_fields() {
    var intern = new Intern();
    intern.setIsRemunerate(null);
    intern.setManager(null);

    var dto = mapper.toRest(intern);

    assertNull(dto.getId());
    assertNull(dto.getFirstName());
    assertNull(dto.getEmail());
    assertNull(dto.getDepartment());
    assertNull(dto.getSalary());
    assertNull(dto.getIsRemunerate());
    assertNull(dto.getManagerId());
  }
}
