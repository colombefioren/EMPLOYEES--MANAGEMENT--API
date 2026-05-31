package org.coco.jpa;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.Target;
import org.junit.jupiter.api.Test;

class PojaGeneratedTest {

  @Test
  void annotation_should_be_present() {
    var annotation = PojaGenerated.class.getAnnotation(java.lang.annotation.Documented.class);
    assertNotNull(annotation);

    var target = PojaGenerated.class.getAnnotation(Target.class);
    assertNotNull(target);
  }
}
