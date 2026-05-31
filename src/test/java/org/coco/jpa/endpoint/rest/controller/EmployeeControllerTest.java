package org.coco.jpa.endpoint.rest.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.coco.jpa.endpoint.rest.mapper.EmployeeMapper;
import org.coco.jpa.model.Department;
import org.coco.jpa.model.Employee;
import org.coco.jpa.model.dto.EmployeeDto;
import org.coco.jpa.service.EmployeeService;
import org.coco.jpa.service.InternService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private EmployeeService employeeService;

  @MockBean private InternService internService;

  @MockBean private EmployeeMapper employeeMapper;

  @Test
  void get_all_employees_should_return_list() throws Exception {
    var emp = new Employee();
    emp.setId(1L);
    var dto = EmployeeDto.builder().id(1L).firstName("John").build();

    when(employeeService.findAllByFilters(any(), any(), any(), any(Pageable.class)))
        .thenReturn(List.of(emp));
    when(employeeService.countByFilters(any(), any(), any())).thenReturn(1L);
    when(employeeMapper.toRest(emp)).thenReturn(dto);

    mockMvc
        .perform(get("/employees"))
        .andExpect(status().isOk())
        .andExpect(header().string("X-Total-Count", "1"))
        .andExpect(jsonPath("$[0].id").value(1));
  }

  @Test
  void get_all_employees_should_handle_sort_param() throws Exception {
    var emp = new Employee();
    emp.setId(2L);
    var dto = EmployeeDto.builder().id(2L).build();

    when(employeeService.findAllByFilters(any(), any(), any(), any(Pageable.class)))
        .thenReturn(List.of(emp));
    when(employeeService.countByFilters(any(), any(), any())).thenReturn(1L);
    when(employeeMapper.toRest(emp)).thenReturn(dto);

    mockMvc
        .perform(get("/employees?_sort=firstName&_order=DESC"))
        .andExpect(status().isOk())
        .andExpect(header().string("X-Total-Count", "1"))
        .andExpect(jsonPath("$[0].id").value(2));
  }

  @Test
  void get_employees_by_ids_should_return_list() throws Exception {
    var emp = new Employee();
    emp.setId(1L);
    var dto = EmployeeDto.builder().id(1L).build();

    when(employeeService.findAllByIds(List.of(1L, 2L))).thenReturn(List.of(emp));
    when(employeeMapper.toRest(emp)).thenReturn(dto);

    mockMvc
        .perform(get("/employees?id=1&id=2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1));
  }

  @Test
  void get_employee_by_id_should_return_when_exists() throws Exception {
    var emp = new Employee();
    emp.setId(1L);
    var dto = EmployeeDto.builder().id(1L).firstName("Jane").build();

    when(employeeService.exists(1L)).thenReturn(true);
    when(employeeService.findById(1L)).thenReturn(emp);
    when(employeeMapper.toRest(emp)).thenReturn(dto);

    mockMvc
        .perform(get("/employees/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value("Jane"));
  }

  @Test
  void get_employee_by_id_should_return_not_found() throws Exception {
    when(employeeService.exists(99L)).thenReturn(false);

    mockMvc.perform(get("/employees/99")).andExpect(status().isNotFound());
  }

  @Test
  void create_employee_should_return_created() throws Exception {
    var emp = new Employee();
    emp.setFirstName("Jack");
    emp.setDepartment(Department.IT);
    emp.setSalary(new BigDecimal("2000"));
    var dto = EmployeeDto.builder().id(1L).firstName("Jack").build();

    when(employeeService.create(any())).thenReturn(emp);
    when(employeeMapper.toRest(emp)).thenReturn(dto);

    mockMvc
        .perform(
            post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    objectMapper.writeValueAsString(
                        Map.of("firstName", "Jack", "department", "IT", "salary", 2000))))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.firstName").value("Jack"));
  }

  @Test
  void update_employee_should_return_ok_when_exists() throws Exception {
    var emp = new Employee();
    emp.setFirstName("Updated");
    var dto = EmployeeDto.builder().id(1L).firstName("Updated").build();

    when(employeeService.exists(1L)).thenReturn(true);
    when(employeeService.update(eq(1L), any())).thenReturn(emp);
    when(employeeMapper.toRest(emp)).thenReturn(dto);

    mockMvc
        .perform(
            put("/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("firstName", "Updated"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value("Updated"));
  }

  @Test
  void update_employee_should_return_not_found_when_missing() throws Exception {
    when(employeeService.exists(99L)).thenReturn(false);

    mockMvc
        .perform(
            put("/employees/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("firstName", "Nope"))))
        .andExpect(status().isNotFound());
  }

  @Test
  void patch_employee_should_return_ok_when_exists() throws Exception {
    var emp = new Employee();
    emp.setFirstName("Patched");
    var dto = EmployeeDto.builder().id(1L).firstName("Patched").build();

    when(employeeService.exists(1L)).thenReturn(true);
    when(employeeService.partialUpdate(eq(1L), any())).thenReturn(emp);
    when(employeeMapper.toRest(emp)).thenReturn(dto);

    mockMvc
        .perform(
            patch("/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("firstName", "Patched"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value("Patched"));
  }

  @Test
  void patch_employee_should_return_not_found_when_missing() throws Exception {
    when(employeeService.exists(99L)).thenReturn(false);

    mockMvc
        .perform(
            patch("/employees/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("firstName", "Nope"))))
        .andExpect(status().isNotFound());
  }

  @Test
  void delete_employee_should_return_no_content_when_exists() throws Exception {
    when(employeeService.exists(1L)).thenReturn(true);
    doNothing().when(internService).unassignManager(1L);
    doNothing().when(employeeService).delete(1L);

    mockMvc.perform(delete("/employees/1")).andExpect(status().isNoContent());

    verify(internService).unassignManager(1L);
    verify(employeeService).delete(1L);
  }

  @Test
  void delete_employee_should_return_not_found_when_missing() throws Exception {
    when(employeeService.exists(99L)).thenReturn(false);

    mockMvc.perform(delete("/employees/99")).andExpect(status().isNotFound());
  }
}
