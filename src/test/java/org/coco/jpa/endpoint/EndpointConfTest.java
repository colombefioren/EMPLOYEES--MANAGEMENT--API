package org.coco.jpa.endpoint;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = EndpointConf.class)
class EndpointConfTest {

  @Autowired private EndpointConf endpointConf;

  @Test
  void object_mapper_should_be_created() {
    var mapper = endpointConf.objectMapper();

    assertNotNull(mapper);
    assertFalse(
        mapper.isEnabled(
            com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));
    assertFalse(
        mapper.isEnabled(
            com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
  }
}
