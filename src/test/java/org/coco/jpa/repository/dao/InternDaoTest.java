package org.coco.jpa.repository.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.util.List;
import org.coco.jpa.model.Intern;
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
class InternDaoTest {

  @Mock private EntityManager entityManager;

  private InternDao internDao;

  @BeforeEach
  void setUp() {
    internDao = new InternDao(entityManager);
  }

  private CriteriaQuery<Intern> mockFindByCriteria() {
    var cb = mock(CriteriaBuilder.class);
    var cq = mock(CriteriaQuery.class);
    var root = mock(Root.class);

    when(entityManager.getCriteriaBuilder()).thenReturn(cb);
    when(cb.createQuery(Intern.class)).thenReturn(cq);
    when(cq.from(Intern.class)).thenReturn(root);

    doReturn(cq).when(cq).where(any(Predicate[].class));

    var tq = mock(TypedQuery.class);
    when(entityManager.createQuery(cq)).thenReturn(tq);
    when(tq.setFirstResult(anyInt())).thenReturn(tq);
    when(tq.setMaxResults(anyInt())).thenReturn(tq);
    when(tq.getResultList()).thenReturn(List.of(new Intern()));

    return cq;
  }

  @Test
  void findByCriteria_should_return_results() {
    mockFindByCriteria();
    var result = internDao.findByCriteria(null, null, null, PageRequest.of(0, 10));
    assertEquals(1, result.size());
  }

  @Test
  void findByCriteria_should_filter_by_department() {
    var cq = mockFindByCriteria();
    var cb = mock(CriteriaBuilder.class);
    when(entityManager.getCriteriaBuilder()).thenReturn(cb);
    when(cb.equal(any(), any())).thenReturn(mock(Predicate.class));
    when(cb.createQuery(Intern.class)).thenReturn(cq);

    internDao.findByCriteria("Marketing", null, null, PageRequest.of(0, 10));
    verify(cq).where(any(Predicate[].class));
  }

  @Test
  void findByCriteria_should_filter_by_is_remunerate() {
    var cq = mockFindByCriteria();
    var cb = mock(CriteriaBuilder.class);
    when(entityManager.getCriteriaBuilder()).thenReturn(cb);
    when(cb.equal(any(), any())).thenReturn(mock(Predicate.class));
    when(cb.createQuery(Intern.class)).thenReturn(cq);

    internDao.findByCriteria(null, true, null, PageRequest.of(0, 10));
    verify(cq).where(any(Predicate[].class));
  }

  @Test
  void findByCriteria_should_filter_by_manager_id() {
    var cq = mockFindByCriteria();
    var cb = mock(CriteriaBuilder.class);
    var root = mock(Root.class);
    var managerPath = mock(Path.class);

    when(entityManager.getCriteriaBuilder()).thenReturn(cb);
    when(cb.createQuery(Intern.class)).thenReturn(cq);
    when(cq.from(Intern.class)).thenReturn(root);
    when(cb.equal(any(), any())).thenReturn(mock(Predicate.class));
    when(root.get("manager")).thenReturn(managerPath);
    when(managerPath.get("id")).thenReturn(mock(Path.class));

    internDao.findByCriteria(null, null, 1L, PageRequest.of(0, 10));
    verify(cq).where(any(Predicate[].class));
  }

  @Test
  void findByCriteria_should_apply_sort_asc() {
    var cb = mock(CriteriaBuilder.class);
    var cq = mock(CriteriaQuery.class);
    var root = mock(Root.class);

    when(entityManager.getCriteriaBuilder()).thenReturn(cb);
    when(cb.createQuery(Intern.class)).thenReturn(cq);
    when(cq.from(Intern.class)).thenReturn(root);
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
    internDao.findByCriteria(null, null, null, pageable);

    verify(cb).asc(any());
    verify(cq).orderBy(any(Order.class));
  }

  @Test
  void findByCriteria_should_apply_sort_desc() {
    var cb = mock(CriteriaBuilder.class);
    var cq = mock(CriteriaQuery.class);
    var root = mock(Root.class);

    when(entityManager.getCriteriaBuilder()).thenReturn(cb);
    when(cb.createQuery(Intern.class)).thenReturn(cq);
    when(cq.from(Intern.class)).thenReturn(root);
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
    internDao.findByCriteria(null, null, null, pageable);

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
    when(cq.from(Intern.class)).thenReturn(root);
    doReturn(cq).when(cq).select(any());
    doReturn(cq).when(cq).where(any(Predicate[].class));

    var tq = mock(TypedQuery.class);
    when(entityManager.createQuery(cq)).thenReturn(tq);
    when(tq.getSingleResult()).thenReturn(3L);

    var result = internDao.countByCriteria(null, null, null);
    assertEquals(3L, result);
  }
}
