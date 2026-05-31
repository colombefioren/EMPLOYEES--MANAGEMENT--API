package org.coco.jpa.repository.dao;

import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.coco.jpa.model.Department;
import org.coco.jpa.model.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class EmployeeDao {
  private final EntityManager entityManager;

  public List<Employee> findByCriteria(
      String searchTerm, String department, Boolean isActive, Pageable pageable) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Employee> query = builder.createQuery(Employee.class);
    Root<Employee> root = query.from(Employee.class);
    List<Predicate> predicates = buildPredicates(builder, root, searchTerm, department, isActive);
    query.where(predicates.toArray(new Predicate[0]));

    if (pageable.getSort().isSorted()) {
      pageable
          .getSort()
          .forEach(
              order -> {
                String property = order.getProperty();
                if (order.isAscending()) {
                  query.orderBy(builder.asc(root.get(property)));
                } else {
                  query.orderBy(builder.desc(root.get(property)));
                }
              });
    }

    return entityManager
        .createQuery(query)
        .setFirstResult((int) pageable.getOffset())
        .setMaxResults(pageable.getPageSize())
        .getResultList();
  }

  public long countByCriteria(String searchTerm, String department, Boolean isActive) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> query = builder.createQuery(Long.class);
    Root<Employee> root = query.from(Employee.class);

    List<Predicate> predicates = buildPredicates(builder, root, searchTerm, department, isActive);

    query.select(builder.count(root));
    query.where(predicates.toArray(new Predicate[0]));

    return entityManager.createQuery(query).getSingleResult();
  }

  private List<Predicate> buildPredicates(
      CriteriaBuilder builder,
      Root<Employee> root,
      String searchTerm,
      String department,
      Boolean isActive) {
    List<Predicate> predicates = new ArrayList<>();

    if (department != null && !department.isEmpty()) {
      Department dept = Department.valueOf(department);
      predicates.add(builder.equal(root.get("department"), dept));
    }

    if (isActive != null) {
      predicates.add(builder.equal(root.get("isActive"), isActive));
    }

    if (searchTerm != null && !searchTerm.isEmpty()) {
      String searchPattern = "%" + searchTerm.toLowerCase() + "%";
      Predicate firstNameMatch = builder.like(builder.lower(root.get("firstName")), searchPattern);
      Predicate emailMatch = builder.like(builder.lower(root.get("email")), searchPattern);
      predicates.add(builder.or(firstNameMatch, emailMatch));
    }

    return predicates;
  }
}
