package org.coco.jpa.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class InternTest {

  @Test
  void should_create_with_defaults() {
    var intern = new Intern();

    assertNull(intern.getId());
    assertNull(intern.getFirstName());
    assertNull(intern.getEmail());
    assertNull(intern.getDepartment());
    assertNull(intern.getSalary());
    assertFalse(intern.getIsRemunerate());
    assertNull(intern.getManager());
  }

  @Test
  void should_set_and_get_fields() {
    var intern = new Intern();
    intern.setId(1L);
    intern.setFirstName("Alice");
    intern.setEmail("alice@test.com");
    intern.setDepartment(Department.RH);
    intern.setSalary(new BigDecimal("500"));
    intern.setIsRemunerate(true);
    var mgr = new Employee();
    mgr.setId(2L);
    intern.setManager(mgr);

    assertEquals(1L, intern.getId());
    assertEquals("Alice", intern.getFirstName());
    assertEquals("alice@test.com", intern.getEmail());
    assertEquals(Department.RH, intern.getDepartment());
    assertEquals(new BigDecimal("500"), intern.getSalary());
    assertTrue(intern.getIsRemunerate());
    assertEquals(2L, intern.getManager().getId());
  }
}
