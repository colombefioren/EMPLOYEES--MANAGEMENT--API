package org.coco.jpa.endpoint.rest.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.coco.jpa.endpoint.rest.mapper.InternMapper;
import org.coco.jpa.model.Intern;
import org.coco.jpa.model.dto.InternDto;
import org.coco.jpa.service.InternService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(InternController.class)
class InternControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private InternService internService;

  @MockBean private InternMapper internMapper;

  @Test
  void get_all_interns_should_return_list() throws Exception {
    var intern = new Intern();
    intern.setId(1L);
    var dto = InternDto.builder().id(1L).firstName("Alice").build();

    when(internService.findAllByFilters(any(), any(), any(), any(Pageable.class)))
        .thenReturn(List.of(intern));
    when(internService.countByFilters(any(), any(), any())).thenReturn(1L);
    when(internMapper.toRest(intern)).thenReturn(dto);

    mockMvc
        .perform(get("/interns").param("managerId", "2"))
        .andExpect(status().isOk())
        .andExpect(header().string("X-Total-Count", "1"))
        .andExpect(jsonPath("$[0].id").value(1));
  }

  @Test
  void get_interns_by_ids_should_return_list() throws Exception {
    var intern = new Intern();
    intern.setId(1L);
    var dto = InternDto.builder().id(1L).build();

    when(internService.findAllByIds(List.of(1L, 2L))).thenReturn(List.of(intern));
    when(internMapper.toRest(intern)).thenReturn(dto);

    mockMvc
        .perform(get("/interns?id=1&id=2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1));
  }

  @Test
  void get_interns_by_manager_should_return_list() throws Exception {
    var intern = new Intern();
    intern.setId(1L);
    var dto = InternDto.builder().id(1L).build();

    when(internService.findByManagerId(2L)).thenReturn(List.of(intern));
    when(internMapper.toRest(intern)).thenReturn(dto);

    mockMvc
        .perform(get("/interns/manager/2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1));
  }

  @Test
  void get_intern_by_id_should_return_when_exists() throws Exception {
    var intern = new Intern();
    intern.setId(1L);
    var dto = InternDto.builder().id(1L).firstName("Bob").build();

    when(internService.exists(1L)).thenReturn(true);
    when(internService.findById(1L)).thenReturn(intern);
    when(internMapper.toRest(intern)).thenReturn(dto);

    mockMvc
        .perform(get("/interns/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value("Bob"));
  }

  @Test
  void get_intern_by_id_should_return_not_found() throws Exception {
    when(internService.exists(99L)).thenReturn(false);

    mockMvc.perform(get("/interns/99")).andExpect(status().isNotFound());
  }

  @Test
  void create_intern_should_return_created() throws Exception {
    var intern = new Intern();
    intern.setFirstName("Carol");
    var dto = InternDto.builder().id(1L).firstName("Carol").build();

    when(internService.create(any())).thenReturn(intern);
    when(internMapper.toRest(intern)).thenReturn(dto);

    mockMvc
        .perform(
            post("/interns")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("firstName", "Carol"))))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.firstName").value("Carol"));
  }

  @Test
  void update_intern_should_return_ok_when_exists() throws Exception {
    var intern = new Intern();
    intern.setFirstName("Updated");
    var dto = InternDto.builder().id(1L).firstName("Updated").build();

    when(internService.exists(1L)).thenReturn(true);
    when(internService.update(eq(1L), any())).thenReturn(intern);
    when(internMapper.toRest(intern)).thenReturn(dto);

    mockMvc
        .perform(
            put("/interns/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("firstName", "Updated"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value("Updated"));
  }

  @Test
  void update_intern_should_return_not_found_when_missing() throws Exception {
    when(internService.exists(99L)).thenReturn(false);

    mockMvc
        .perform(
            put("/interns/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("firstName", "Nope"))))
        .andExpect(status().isNotFound());
  }

  @Test
  void patch_intern_should_return_ok_when_exists() throws Exception {
    var intern = new Intern();
    intern.setFirstName("Patched");
    var dto = InternDto.builder().id(1L).firstName("Patched").build();

    when(internService.exists(1L)).thenReturn(true);
    when(internService.partialUpdate(eq(1L), any())).thenReturn(intern);
    when(internMapper.toRest(intern)).thenReturn(dto);

    mockMvc
        .perform(
            patch("/interns/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("firstName", "Patched"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value("Patched"));
  }

  @Test
  void patch_intern_should_return_not_found_when_missing() throws Exception {
    when(internService.exists(99L)).thenReturn(false);

    mockMvc
        .perform(
            patch("/interns/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("firstName", "Nope"))))
        .andExpect(status().isNotFound());
  }

  @Test
  void delete_intern_should_return_no_content_when_exists() throws Exception {
    when(internService.exists(1L)).thenReturn(true);
    doNothing().when(internService).delete(1L);

    mockMvc.perform(delete("/interns/1")).andExpect(status().isNoContent());

    verify(internService).delete(1L);
  }

  @Test
  void delete_intern_should_return_not_found_when_missing() throws Exception {
    when(internService.exists(99L)).thenReturn(false);

    mockMvc.perform(delete("/interns/99")).andExpect(status().isNotFound());
  }
}
