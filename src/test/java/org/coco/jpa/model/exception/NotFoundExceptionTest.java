package org.coco.jpa.model.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NotFoundExceptionTest {

  @Test
  void should_create_with_message() {
    var exception = new NotFoundException("Not found");

    assertEquals("Not found", exception.getMessage());
  }
}
