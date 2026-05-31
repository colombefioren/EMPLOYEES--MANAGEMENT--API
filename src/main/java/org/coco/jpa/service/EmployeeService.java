package org.coco.jpa.service;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.coco.jpa.model.Employee;
import org.coco.jpa.model.exception.NotFoundException;
import org.coco.jpa.repository.EmployeeRepository;
import org.coco.jpa.repository.dao.EmployeeDao;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class EmployeeService {
  private final EmployeeRepository employeeRepository;
  private final EmployeeDao employeeDao;

  public Employee findById(Long id) {
    return employeeRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("Employee not found with id: " + id));
  }

  public List<Employee> findAllByIds(List<Long> ids) {
    return employeeRepository.findAllById(ids);
  }

  @Transactional
  public Employee create(Employee employee) {
    if (employee.getIsActive() == null) {
      employee.setIsActive(true);
    }
    return employeeRepository.save(employee);
  }

  @Transactional
  public Employee update(Long id, Employee employee) {
    employee.setId(id);
    return employeeRepository.save(employee);
  }

  @Transactional
  public Employee partialUpdate(Long id, Map<String, Object> updates) {
    Employee existing = findById(id);

    updates.forEach(
        (key, value) -> {
          switch (key) {
            case "firstName":
              existing.setFirstName((String) value);
              break;
            case "email":
              existing.setEmail((String) value);
              break;
            case "department":
              if (value != null) {
                existing.setDepartment(org.coco.jpa.model.Department.valueOf((String) value));
              }
              break;
            case "salary":
              if (value != null) {
                existing.setSalary(new java.math.BigDecimal(value.toString()));
              }
              break;
            case "isActive":
              existing.setIsActive((Boolean) value);
              break;
          }
        });

    return employeeRepository.save(existing);
  }

  @Transactional
  public void delete(Long id) {
    employeeRepository.deleteById(id);
  }

  public boolean exists(Long id) {
    return employeeRepository.existsById(id);
  }

  public List<Employee> findAllByFilters(
      String searchTerm, String department, Boolean isActive, Pageable pageable) {
    return employeeDao.findByCriteria(searchTerm, department, isActive, pageable);
  }

  public long countByFilters(String searchTerm, String department, Boolean isActive) {
    return employeeDao.countByCriteria(searchTerm, department, isActive);
  }

  public List<Employee> findByIsActive(Boolean isActive) {
    return employeeRepository.findByIsActive(isActive);
  }
}
