package org.coco.jpa.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DepartmentTest {

  @Test
  void should_have_all_values() {
    assertNotNull(Department.valueOf("IT"));
    assertNotNull(Department.valueOf("RH"));
    assertNotNull(Department.valueOf("Marketing"));
    assertNotNull(Department.valueOf("Finance"));
  }
}
