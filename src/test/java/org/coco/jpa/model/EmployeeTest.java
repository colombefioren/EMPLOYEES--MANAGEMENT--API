package org.coco.jpa.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class EmployeeTest {

  @Test
  void should_create_with_defaults() {
    var emp = new Employee();

    assertNull(emp.getId());
    assertNull(emp.getFirstName());
    assertNull(emp.getEmail());
    assertNull(emp.getDepartment());
    assertNull(emp.getSalary());
    assertTrue(emp.getIsActive());
    assertNotNull(emp.getInterns());
  }

  @Test
  void should_set_and_get_fields() {
    var emp = new Employee();
    emp.setId(1L);
    emp.setFirstName("John");
    emp.setEmail("john@test.com");
    emp.setDepartment(Department.IT);
    emp.setSalary(new BigDecimal("2500"));
    emp.setIsActive(false);

    assertEquals(1L, emp.getId());
    assertEquals("John", emp.getFirstName());
    assertEquals("john@test.com", emp.getEmail());
    assertEquals(Department.IT, emp.getDepartment());
    assertEquals(new BigDecimal("2500"), emp.getSalary());
    assertFalse(emp.getIsActive());
  }
}
