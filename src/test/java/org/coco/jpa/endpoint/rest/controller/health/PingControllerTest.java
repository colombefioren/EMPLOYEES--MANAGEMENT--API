package org.coco.jpa.endpoint.rest.controller.health;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.coco.jpa.repository.DummyRepository;
import org.coco.jpa.repository.DummyUuidRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PingController.class)
class PingControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private DummyRepository dummyRepository;

  @MockBean private DummyUuidRepository dummyUuidRepository;

  @Test
  void ping_should_return_pong() throws Exception {
    mockMvc.perform(get("/ping")).andExpect(status().isOk()).andExpect(content().string("pong"));
  }
}
