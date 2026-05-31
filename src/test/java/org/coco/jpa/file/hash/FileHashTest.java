package org.coco.jpa.file.hash;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FileHashTest {

  @Test
  void should_create_record() {
    var hash = new FileHash(FileHashAlgorithm.SHA256, "abc123");

    assertEquals(FileHashAlgorithm.SHA256, hash.algorithm());
    assertEquals("abc123", hash.value());
  }
}
