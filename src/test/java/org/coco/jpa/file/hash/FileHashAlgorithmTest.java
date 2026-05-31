package org.coco.jpa.file.hash;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FileHashAlgorithmTest {

  @Test
  void should_have_expected_values() {
    assertEquals(2, FileHashAlgorithm.values().length);
    assertEquals("SHA256", FileHashAlgorithm.SHA256.name());
    assertEquals("NONE", FileHashAlgorithm.NONE.name());
  }
}
