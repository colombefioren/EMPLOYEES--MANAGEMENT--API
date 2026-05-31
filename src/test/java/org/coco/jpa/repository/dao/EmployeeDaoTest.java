package org.coco.jpa.repository.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.util.List;
import org.coco.jpa.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EmployeeDaoTest {

  @Mock private EntityManager entityManager;

  private EmployeeDao employeeDao;

  @BeforeEach
  void setUp() {
    employeeDao = new EmployeeDao(entityManager);
  }

  private CriteriaQuery<Employee> mockFindByCriteria() {
    var cb = mock(CriteriaBuilder.class);
    var cq = mock(CriteriaQuery.class);
    var root = mock(Root.class);

    when(entityManager.getCriteriaBuilder()).thenReturn(cb);
    when(cb.createQuery(Employee.class)).thenReturn(cq);
    when(cq.from(Employee.class)).thenReturn(root);

    doReturn(cq).when(cq).where(any(Predicate[].class));

    var tq = mock(TypedQuery.class);
    when(entityManager.createQuery(cq)).thenReturn(tq);
    when(tq.setFirstResult(anyInt())).thenReturn(tq);
    when(tq.setMaxResults(anyInt())).thenReturn(tq);
    when(tq.getResultList()).thenReturn(List.of(new Employee()));

    return cq;
  }

  @Test
  void findByCriteria_should_return_results() {
    mockFindByCriteria();
    var result = employeeDao.findByCriteria(null, null, null, PageRequest.of(0, 10));
    assertEquals(1, result.size());
  }

  @Test
  void findByCriteria_should_filter_by_department() {
    var cq = mockFindByCriteria();
    var cb = mock(CriteriaBuilder.class);
    when(entityManager.getCriteriaBuilder()).thenReturn(cb);
    when(cb.equal(any(), any())).thenReturn(mock(Predicate.class));
    when(cb.createQuery(Employee.class)).thenReturn(cq);

    employeeDao.findByCriteria(null, "IT", null, PageRequest.of(0, 10));
    verify(cq).where(any(Predicate[].class));
  }

  @Test
  void findByCriteria_should_filter_by_is_active() {
    var cq = mockFindByCriteria();
    var cb = mock(CriteriaBuilder.class);
    when(entityManager.getCriteriaBuilder()).thenReturn(cb);
    when(cb.equal(any(), any())).thenReturn(mock(Predicate.class));
    when(cb.createQuery(Employee.class)).thenReturn(cq);

    employeeDao.findByCriteria(null, null, true, PageRequest.of(0, 10));
    verify(cq).where(any(Predicate[].class));
  }

  @Test
  void findByCriteria_should_search_by_term() {
    var cq = mockFindByCriteria();
    var cb = mock(CriteriaBuilder.class);
    when(entityManager.getCriteriaBuilder()).thenReturn(cb);
    when(cb.createQuery(Employee.class)).thenReturn(cq);
    when(cb.lower(any())).thenReturn(mock(Expression.class));
    when(cb.like(any(), anyString())).thenReturn(mock(Predicate.class));
    when(cb.or(any(Predicate[].class))).thenReturn(mock(Predicate.class));

    employeeDao.findByCriteria("search", null, null, PageRequest.of(0, 10));
    verify(cq).where(any(Predicate[].class));
  }

  @Test
  void findByCriteria_should_apply_sort_asc() {
    var cb = mock(CriteriaBuilder.class);
    var cq = mock(CriteriaQuery.class);
    var root = mock(Root.class);

    when(entityManager.getCriteriaBuilder()).thenReturn(cb);
    when(cb.createQuery(Employee.class)).thenReturn(cq);
    when(cq.from(Employee.class)).thenReturn(root);
    doReturn(cq).when(cq).where(any(Predicate[].class));

    when(root.get("firstName")).thenReturn(mock(Path.class));
    when(cb.asc(any())).thenReturn(mock(Order.class));
    doReturn(cq).when(cq).orderBy(any(Order.class));

    var tq = mock(TypedQuery.class);
    when(entityManager.createQuery(cq)).thenReturn(tq);
    when(tq.setFirstResult(anyInt())).thenReturn(tq);
    when(tq.setMaxResults(anyInt())).thenReturn(tq);
    when(tq.getResultList()).thenReturn(List.of());

    var pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("firstName")));
    employeeDao.findByCriteria(null, null, null, pageable);

    verify(cb).asc(any());
    verify(cq).orderBy(any(Order.class));
  }

  @Test
  void findByCriteria_should_apply_sort_desc() {
    var cb = mock(CriteriaBuilder.class);
    var cq = mock(CriteriaQuery.class);
    var root = mock(Root.class);

    when(entityManager.getCriteriaBuilder()).thenReturn(cb);
    when(cb.createQuery(Employee.class)).thenReturn(cq);
    when(cq.from(Employee.class)).thenReturn(root);
    doReturn(cq).when(cq).where(any(Predicate[].class));

    when(root.get("firstName")).thenReturn(mock(Path.class));
    when(cb.desc(any())).thenReturn(mock(Order.class));
    doReturn(cq).when(cq).orderBy(any(Order.class));

    var tq = mock(TypedQuery.class);
    when(entityManager.createQuery(cq)).thenReturn(tq);
    when(tq.setFirstResult(anyInt())).thenReturn(tq);
    when(tq.setMaxResults(anyInt())).thenReturn(tq);
    when(tq.getResultList()).thenReturn(List.of());

    var pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("firstName")));
    employeeDao.findByCriteria(null, null, null, pageable);

    verify(cb).desc(any());
    verify(cq).orderBy(any(Order.class));
  }

  @Test
  void countByCriteria_should_return_count() {
    var cb = mock(CriteriaBuilder.class);
    var cq = mock(CriteriaQuery.class);
    var root = mock(Root.class);

    when(entityManager.getCriteriaBuilder()).thenReturn(cb);
    when(cb.createQuery(Long.class)).thenReturn(cq);
    when(cq.from(Employee.class)).thenReturn(root);
    doReturn(cq).when(cq).select(any());
    doReturn(cq).when(cq).where(any(Predicate[].class));

    var tq = mock(TypedQuery.class);
    when(entityManager.createQuery(cq)).thenReturn(tq);
    when(tq.getSingleResult()).thenReturn(5L);

    var result = employeeDao.countByCriteria(null, null, null);
    assertEquals(5L, result);
  }
}
