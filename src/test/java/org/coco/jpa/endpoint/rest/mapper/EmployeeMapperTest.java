package org.coco.jpa.endpoint.rest.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.coco.jpa.model.Department;
import org.coco.jpa.model.Employee;
import org.junit.jupiter.api.Test;

class EmployeeMapperTest {

  private final EmployeeMapper mapper = new EmployeeMapper();

  @Test
  void to_rest_should_map_all_fields() {
    var emp = new Employee();
    emp.setId(1L);
    emp.setFirstName("John");
    emp.setEmail("john@test.com");
    emp.setDepartment(Department.IT);
    emp.setSalary(new BigDecimal("2500"));
    emp.setIsActive(true);

    var dto = mapper.toRest(emp);

    assertEquals(1L, dto.getId());
    assertEquals("John", dto.getFirstName());
    assertEquals("john@test.com", dto.getEmail());
    assertEquals(Department.IT, dto.getDepartment());
    assertEquals(new BigDecimal("2500"), dto.getSalary());
    assertTrue(dto.getIsActive());
  }

  @Test
  void to_rest_should_handle_null_fields() {
    var emp = new Employee();
    emp.setIsActive(null);

    var dto = mapper.toRest(emp);

    assertNull(dto.getId());
    assertNull(dto.getFirstName());
    assertNull(dto.getEmail());
    assertNull(dto.getDepartment());
    assertNull(dto.getSalary());
    assertNull(dto.getIsActive());
  }
}
