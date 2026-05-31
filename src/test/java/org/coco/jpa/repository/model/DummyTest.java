package org.coco.jpa.repository.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DummyTest {

  @Test
  void should_set_and_get_id() {
    var dummy = new Dummy();
    dummy.setId("dummy-id");

    assertEquals("dummy-id", dummy.getId());
  }
}
