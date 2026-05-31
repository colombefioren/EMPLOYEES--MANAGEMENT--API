package org.coco.jpa.repository.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DummyUuidTest {

  @Test
  void should_set_and_get_id() {
    var dummy = new DummyUuid();
    dummy.setId("uuid-id");

    assertEquals("uuid-id", dummy.getId());
  }
}
