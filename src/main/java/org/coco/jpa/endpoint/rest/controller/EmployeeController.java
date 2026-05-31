package org.coco.jpa.endpoint.rest.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.coco.jpa.endpoint.rest.mapper.EmployeeMapper;
import org.coco.jpa.model.Employee;
import org.coco.jpa.model.dto.EmployeeDto;
import org.coco.jpa.service.EmployeeService;
import org.coco.jpa.service.InternService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
@AllArgsConstructor
public class EmployeeController {

  private final EmployeeService employeeService;
  private final InternService internService;
  private final EmployeeMapper employeeMapper;

  @GetMapping
  public ResponseEntity<List<EmployeeDto>> getAllEmployees(
      @RequestParam(defaultValue = "0") int _start,
      @RequestParam(defaultValue = "10") int _end,
      @RequestParam(required = false) String _sort,
      @RequestParam(defaultValue = "ASC") String _order,
      @RequestParam(required = false) String department,
      @RequestParam(required = false) String q,
      @RequestParam(required = false) Boolean isActive,
      HttpServletResponse response) {

    int page = _start / (_end - _start);
    int size = _end - _start;

    Sort.Direction direction = Sort.Direction.fromString(_order.toUpperCase());
    String sortField = (_sort != null && !_sort.isEmpty()) ? _sort : "id";
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

    List<Employee> employees = employeeService.findAllByFilters(q, department, isActive, pageable);
    long total = employeeService.countByFilters(q, department, isActive);

    response.setHeader("X-Total-Count", String.valueOf(total));
    response.setHeader("Access-Control-Expose-Headers", "X-Total-Count");

    List<EmployeeDto> dtos = employees.stream().map(employeeMapper::toRest).toList();
    return ResponseEntity.ok(dtos);
  }

  @GetMapping(params = "id")
  public ResponseEntity<List<EmployeeDto>> getEmployeesByIds(@RequestParam List<Long> id) {
    List<Employee> employees = employeeService.findAllByIds(id);
    List<EmployeeDto> dtos = employees.stream().map(employeeMapper::toRest).toList();
    return ResponseEntity.ok(dtos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
    if (!employeeService.exists(id)) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(employeeMapper.toRest(employeeService.findById(id)));
  }

  @PostMapping
  public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody Employee employee) {
    Employee saved = employeeService.create(employee);
    return ResponseEntity.status(HttpStatus.CREATED).body(employeeMapper.toRest(saved));
  }

  @PutMapping("/{id}")
  public ResponseEntity<EmployeeDto> updateEmployee(
      @PathVariable Long id, @Valid @RequestBody Employee employee) {
    if (!employeeService.exists(id)) {
      return ResponseEntity.notFound().build();
    }
    Employee saved = employeeService.update(id, employee);
    return ResponseEntity.ok(employeeMapper.toRest(saved));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<EmployeeDto> patchEmployee(
      @PathVariable Long id, @RequestBody Map<String, Object> updates) {
    if (!employeeService.exists(id)) {
      return ResponseEntity.notFound().build();
    }
    Employee saved = employeeService.partialUpdate(id, updates);
    return ResponseEntity.ok(employeeMapper.toRest(saved));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
    if (!employeeService.exists(id)) {
      return ResponseEntity.notFound().build();
    }

    internService.unassignManager(id);
    employeeService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
