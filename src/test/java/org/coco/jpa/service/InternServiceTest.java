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
import org.coco.jpa.model.Intern;
import org.coco.jpa.model.exception.NotFoundException;
import org.coco.jpa.repository.EmployeeRepository;
import org.coco.jpa.repository.InternRepository;
import org.coco.jpa.repository.dao.InternDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class InternServiceTest {

  @Mock private InternRepository internRepository;

  @Mock private EmployeeRepository employeeRepository;

  @Mock private InternDao internDao;

  @InjectMocks private InternService internService;

  @Test
  void find_by_id_should_return_intern_when_exists() {
    var intern = new Intern();
    intern.setId(1L);
    intern.setFirstName("Alice");
    when(internRepository.findById(1L)).thenReturn(Optional.of(intern));

    var result = internService.findById(1L);

    assertEquals("Alice", result.getFirstName());
  }

  @Test
  void find_by_id_should_throw_when_not_found() {
    when(internRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> internService.findById(99L));
  }

  @Test
  void find_all_by_ids_should_return_list() {
    when(internRepository.findAllById(List.of(1L, 2L)))
        .thenReturn(List.of(new Intern(), new Intern()));

    var result = internService.findAllByIds(List.of(1L, 2L));

    assertEquals(2, result.size());
  }

  @Test
  void find_by_manager_id_should_return_list() {
    when(internRepository.findByManagerId(1L)).thenReturn(List.of(new Intern()));

    var result = internService.findByManagerId(1L);

    assertEquals(1, result.size());
  }

  @Test
  void create_should_set_is_remunerate_default_when_null() {
    var intern = new Intern();
    intern.setFirstName("Bob");
    when(internRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var result = internService.create(intern);

    assertFalse(result.getIsRemunerate());
  }

  @Test
  void create_should_set_manager_when_manager_id_provided() {
    var manager = new Employee();
    manager.setId(1L);
    var intern = new Intern();
    intern.setFirstName("Carol");
    intern.setIsRemunerate(false);
    var mgrRef = new Employee();
    mgrRef.setId(1L);
    intern.setManager(mgrRef);

    when(employeeRepository.findById(1L)).thenReturn(Optional.of(manager));
    when(internRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var result = internService.create(intern);

    assertEquals(1L, result.getManager().getId());
  }

  @Test
  void create_should_set_manager_to_null_when_not_found() {
    var intern = new Intern();
    intern.setFirstName("Dave");
    intern.setIsRemunerate(false);
    var mgrRef = new Employee();
    mgrRef.setId(99L);
    intern.setManager(mgrRef);

    when(employeeRepository.findById(99L)).thenReturn(Optional.empty());
    when(internRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var result = internService.create(intern);

    assertNull(result.getManager());
  }

  @Test
  void create_should_set_manager_to_null_when_no_manager() {
    var intern = new Intern();
    intern.setFirstName("Eve");
    intern.setIsRemunerate(false);
    intern.setManager(null);

    when(internRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var result = internService.create(intern);

    assertNull(result.getManager());
  }

  @Test
  void update_should_set_manager_when_manager_id_provided() {
    var manager = new Employee();
    manager.setId(1L);
    var intern = new Intern();
    intern.setFirstName("Frank");
    var mgrRef = new Employee();
    mgrRef.setId(1L);
    intern.setManager(mgrRef);

    when(employeeRepository.findById(1L)).thenReturn(Optional.of(manager));
    when(internRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var result = internService.update(1L, intern);

    assertEquals(1L, result.getManager().getId());
  }

  @Test
  void update_should_set_manager_to_null_when_not_found() {
    var intern = new Intern();
    intern.setFirstName("Grace");
    var mgrRef = new Employee();
    mgrRef.setId(99L);
    intern.setManager(mgrRef);

    when(employeeRepository.findById(99L)).thenReturn(Optional.empty());
    when(internRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var result = internService.update(1L, intern);

    assertNull(result.getManager());
  }

  @Test
  void update_should_set_manager_to_null_when_no_manager() {
    var intern = new Intern();
    intern.setFirstName("Heidi");
    intern.setManager(null);

    when(internRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var result = internService.update(1L, intern);

    assertNull(result.getManager());
  }

  @Test
  void partial_update_should_update_first_name() {
    var existing = new Intern();
    existing.setId(1L);
    when(internRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(internRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var result = internService.partialUpdate(1L, Map.of("firstName", "Updated"));

    assertEquals("Updated", result.getFirstName());
  }

  @Test
  void partial_update_should_update_email() {
    var existing = new Intern();
    existing.setId(1L);
    when(internRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(internRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var result = internService.partialUpdate(1L, Map.of("email", "test@test.com"));

    assertEquals("test@test.com", result.getEmail());
  }

  @Test
  void partial_update_should_update_department() {
    var existing = new Intern();
    existing.setId(1L);
    when(internRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(internRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var result = internService.partialUpdate(1L, Map.of("department", "Finance"));

    assertEquals(Department.Finance, result.getDepartment());
  }

  @Test
  void partial_update_should_update_salary() {
    var existing = new Intern();
    existing.setId(1L);
    when(internRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(internRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var result = internService.partialUpdate(1L, Map.of("salary", "500.0"));

    assertEquals(new BigDecimal("500.0"), result.getSalary());
  }

  @Test
  void partial_update_should_handle_null_salary() {
    var existing = new Intern();
    existing.setId(1L);
    existing.setSalary(new BigDecimal("300"));
    when(internRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(internRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var updates = new HashMap<String, Object>();
    updates.put("salary", null);
    var result = internService.partialUpdate(1L, updates);

    assertNull(result.getSalary());
  }

  @Test
  void partial_update_should_update_is_remunerate() {
    var existing = new Intern();
    existing.setId(1L);
    when(internRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(internRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var result = internService.partialUpdate(1L, Map.of("isRemunerate", true));

    assertTrue(result.getIsRemunerate());
  }

  @Test
  void partial_update_should_set_manager_when_manager_id_provided() {
    var existing = new Intern();
    existing.setId(1L);
    var manager = new Employee();
    manager.setId(5L);

    when(internRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(employeeRepository.findById(5L)).thenReturn(Optional.of(manager));
    when(internRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var result = internService.partialUpdate(1L, Map.of("managerId", 5L));

    assertEquals(5L, result.getManager().getId());
  }

  @Test
  void partial_update_should_set_manager_to_null_when_manager_id_null() {
    var existing = new Intern();
    existing.setId(1L);
    existing.setManager(new Employee());

    when(internRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(internRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    var updates = new HashMap<String, Object>();
    updates.put("managerId", null);
    var result = internService.partialUpdate(1L, updates);

    assertNull(result.getManager());
  }

  @Test
  void delete_should_call_repository() {
    doNothing().when(internRepository).deleteById(1L);

    internService.delete(1L);

    verify(internRepository).deleteById(1L);
  }

  @Test
  void exists_should_return_true_when_found() {
    when(internRepository.existsById(1L)).thenReturn(true);

    assertTrue(internService.exists(1L));
  }

  @Test
  void exists_should_return_false_when_not_found() {
    when(internRepository.existsById(99L)).thenReturn(false);

    assertFalse(internService.exists(99L));
  }

  @Test
  void unassign_manager_should_clear_manager_on_all_interns() {
    var intern1 = new Intern();
    intern1.setId(1L);
    intern1.setManager(new Employee());
    var intern2 = new Intern();
    intern2.setId(2L);
    intern2.setManager(new Employee());

    when(internRepository.findByManagerId(1L)).thenReturn(List.of(intern1, intern2));
    when(internRepository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));

    internService.unassignManager(1L);

    assertNull(intern1.getManager());
    assertNull(intern2.getManager());
  }

  @Test
  void find_all_by_filters_should_call_dao() {
    var pageable = PageRequest.of(0, 10);
    when(internDao.findByCriteria("IT", true, 1L, pageable)).thenReturn(List.of());

    internService.findAllByFilters("IT", true, 1L, pageable);

    verify(internDao).findByCriteria("IT", true, 1L, pageable);
  }

  @Test
  void count_by_filters_should_call_dao() {
    when(internDao.countByCriteria("IT", true, 1L)).thenReturn(3L);

    var count = internService.countByFilters("IT", true, 1L);

    assertEquals(3L, count);
  }
}
