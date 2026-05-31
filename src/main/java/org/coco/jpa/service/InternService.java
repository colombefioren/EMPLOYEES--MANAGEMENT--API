package org.coco.jpa.service;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.coco.jpa.model.Employee;
import org.coco.jpa.model.Intern;
import org.coco.jpa.model.exception.NotFoundException;
import org.coco.jpa.repository.EmployeeRepository;
import org.coco.jpa.repository.InternRepository;
import org.coco.jpa.repository.dao.InternDao;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class InternService {
  private final InternRepository internRepository;
  private final EmployeeRepository employeeRepository;
  private final InternDao internDao;

  public Intern findById(Long id) {
    return internRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("Intern not found with id: " + id));
  }

  public List<Intern> findAllByIds(List<Long> ids) {
    return internRepository.findAllById(ids);
  }

  public List<Intern> findByManagerId(Long managerId) {
    return internRepository.findByManagerId(managerId);
  }

  @Transactional
  public Intern create(Intern intern) {
    if (intern.getIsRemunerate() == null) {
      intern.setIsRemunerate(false);
    }

    if (intern.getManager() != null && intern.getManager().getId() != null) {
      Employee manager = employeeRepository.findById(intern.getManager().getId()).orElse(null);
      intern.setManager(manager);
    } else {
      intern.setManager(null);
    }

    return internRepository.save(intern);
  }

  @Transactional
  public Intern update(Long id, Intern intern) {
    intern.setId(id);

    if (intern.getManager() != null && intern.getManager().getId() != null) {
      Employee manager = employeeRepository.findById(intern.getManager().getId()).orElse(null);
      intern.setManager(manager);
    } else {
      intern.setManager(null);
    }

    return internRepository.save(intern);
  }

  @Transactional
  public Intern partialUpdate(Long id, Map<String, Object> updates) {
    Intern existing = findById(id);

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
              } else {
                existing.setSalary(null);
              }
              break;
            case "isRemunerate":
              existing.setIsRemunerate((Boolean) value);
              break;
            case "managerId":
              if (value != null) {
                Employee manager =
                    employeeRepository.findById(Long.valueOf(value.toString())).orElse(null);
                existing.setManager(manager);
              } else {
                existing.setManager(null);
              }
              break;
          }
        });

    return internRepository.save(existing);
  }

  @Transactional
  public void delete(Long id) {
    internRepository.deleteById(id);
  }

  public boolean exists(Long id) {
    return internRepository.existsById(id);
  }

  @Transactional
  public void unassignManager(Long managerId) {
    List<Intern> interns = internRepository.findByManagerId(managerId);
    interns.forEach(intern -> intern.setManager(null));
    internRepository.saveAll(interns);
  }

  public List<Intern> findAllByFilters(
      String department, Boolean isRemunerate, Long managerId, Pageable pageable) {
    return internDao.findByCriteria(department, isRemunerate, managerId, pageable);
  }

  public long countByFilters(String department, Boolean isRemunerate, Long managerId) {
    return internDao.countByCriteria(department, isRemunerate, managerId);
  }
}
