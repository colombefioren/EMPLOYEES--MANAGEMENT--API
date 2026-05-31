package org.coco.jpa.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.coco.jpa.model.Department;
import org.coco.jpa.model.Employee;
import org.coco.jpa.model.exception.NotFoundException;
import org.coco.jpa.repository.EmployeeRepository;
import org.coco.jpa.repository.dao.EmployeeDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

  @Mock private EmployeeRepository employeeRepository;

  @Mock private EmployeeDao employeeDao;

  @InjectMocks private EmployeeService employeeService;

  @Test
  void find_by_id_should_return_employee_when_exists() {
    var emp = new Employee();
    emp.setId(1L);
    emp.setFirstName("John");
    when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));

    var result = employeeService.findById(1L);

    assertEquals("John", result.getFirstName());
  }

  @Test
  void find_by_id_should_throw_when_not_found() {
    when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> employeeService.findById(99L));
  }

  @Test
  void find_all_by_ids_should_return_list() {
    when(employeeRepository.findAllById(List.of(1L, 2L)))
        .thenReturn(List.of(new Employee(), new Employee()));

    var result = employeeService.findAllByIds(List.of(1L, 2L));

    assertEquals(2, result.size());
  }

  @Test
  void create_should_set_is_active_default_when_null() {
    var emp = new Employee();
    emp.setFirstName("Jane");
    emp.setIsActive(null);
    when(employeeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var result = employeeService.create(emp);

    assertTrue(result.getIsActive());
  }

  @Test
  void create_should_keep_is_active_when_provided() {
    var emp = new Employee();
    emp.setFirstName("Jack");
    emp.setIsActive(false);
    when(employeeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var result = employeeService.create(emp);

    assertFalse(result.getIsActive());
  }

  @Test
  void update_should_preserve_interns() {
    var existing = new Employee();
    existing.setId(1L);
    existing.setInterns(List.of());
    var incoming = new Employee();
    incoming.setFirstName("Updated");

    when(employeeRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(employeeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var result = employeeService.update(1L, incoming);

    assertNotNull(result.getInterns());
  }

  @Test
  void partial_update_should_update_first_name() {
    var existing = new Employee();
    existing.setId(1L);
    when(employeeRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(employeeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var result = employeeService.partialUpdate(1L, Map.of("firstName", "Updated"));

    assertEquals("Updated", result.getFirstName());
  }

  @Test
  void partial_update_should_update_email() {
    var existing = new Employee();
    existing.setId(1L);
    when(employeeRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(employeeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var result = employeeService.partialUpdate(1L, Map.of("email", "test@test.com"));

    assertEquals("test@test.com", result.getEmail());
  }

  @Test
  void partial_update_should_update_department() {
    var existing = new Employee();
    existing.setId(1L);
    when(employeeRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(employeeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var result = employeeService.partialUpdate(1L, Map.of("department", "IT"));

    assertEquals(Department.IT, result.getDepartment());
  }

  @Test
  void partial_update_should_handle_null_department() {
    var existing = new Employee();
    existing.setId(1L);
    when(employeeRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(employeeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var updates = new HashMap<String, Object>();
    updates.put("department", null);
    var result = employeeService.partialUpdate(1L, updates);

    assertNull(result.getDepartment());
  }

  @Test
  void partial_update_should_update_salary() {
    var existing = new Employee();
    existing.setId(1L);
    when(employeeRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(employeeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var result = employeeService.partialUpdate(1L, Map.of("salary", "2000.0"));

    assertEquals(new BigDecimal("2000.0"), result.getSalary());
  }

  @Test
  void partial_update_should_handle_null_salary() {
    var existing = new Employee();
    existing.setId(1L);
    existing.setSalary(new BigDecimal("3000"));
    when(employeeRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(employeeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var updates = new HashMap<String, Object>();
    updates.put("salary", null);
    var result = employeeService.partialUpdate(1L, updates);

    assertEquals(new BigDecimal("3000"), result.getSalary());
  }

  @Test
  void partial_update_should_update_is_active() {
    var existing = new Employee();
    existing.setId(1L);
    existing.setIsActive(true);
    when(employeeRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(employeeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var result = employeeService.partialUpdate(1L, Map.of("isActive", false));

    assertFalse(result.getIsActive());
  }

  @Test
  void delete_should_call_repository() {
    doNothing().when(employeeRepository).deleteById(1L);

    employeeService.delete(1L);

    verify(employeeRepository).deleteById(1L);
  }

  @Test
  void exists_should_return_true_when_found() {
    when(employeeRepository.existsById(1L)).thenReturn(true);

    assertTrue(employeeService.exists(1L));
  }

  @Test
  void exists_should_return_false_when_not_found() {
    when(employeeRepository.existsById(99L)).thenReturn(false);

    assertFalse(employeeService.exists(99L));
  }

  @Test
  void find_all_by_filters_should_call_dao() {
    var pageable = PageRequest.of(0, 10);
    when(employeeDao.findByCriteria("search", "IT", true, pageable)).thenReturn(List.of());

    employeeService.findAllByFilters("search", "IT", true, pageable);

    verify(employeeDao).findByCriteria("search", "IT", true, pageable);
  }

  @Test
  void count_by_filters_should_call_dao() {
    when(employeeDao.countByCriteria("search", "IT", true)).thenReturn(5L);

    var count = employeeService.countByFilters("search", "IT", true);

    assertEquals(5L, count);
  }

  @Test
  void find_by_is_active_should_call_repository() {
    when(employeeRepository.findByIsActive(true)).thenReturn(List.of());

    employeeService.findByIsActive(true);

    verify(employeeRepository).findByIsActive(true);
  }
}
