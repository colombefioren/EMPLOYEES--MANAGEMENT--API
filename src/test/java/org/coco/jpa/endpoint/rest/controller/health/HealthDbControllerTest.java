package org.coco.jpa.endpoint.rest.controller.health;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import org.coco.jpa.repository.DummyRepository;
import org.coco.jpa.repository.model.Dummy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HealthDbController.class)
class HealthDbControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private DummyRepository dummyRepository;

  @Test
  void should_return_ok_when_dummy_table_not_empty() throws Exception {
    when(dummyRepository.findAll()).thenReturn(List.of(new Dummy()));

    mockMvc.perform(get("/health/db")).andExpect(status().isOk()).andExpect(content().string("OK"));
  }

  @Test
  void should_return_ko_when_dummy_table_empty() throws Exception {
    when(dummyRepository.findAll()).thenReturn(List.of());

    mockMvc
        .perform(get("/health/db"))
        .andExpect(status().isInternalServerError())
        .andExpect(content().string("KO"));
  }
}
