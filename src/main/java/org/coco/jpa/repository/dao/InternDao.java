package org.coco.jpa.repository.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.coco.jpa.model.Department;
import org.coco.jpa.model.Intern;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InternDao {
    private final EntityManager entityManager;

    public List<Intern> findByCriteria(
            String department, Boolean isRemunerate, Long managerId, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Intern> query = builder.createQuery(Intern.class);
        Root<Intern> root = query.from(Intern.class);

        List<Predicate> predicates = buildPredicates(builder, root, department, isRemunerate, managerId);

        query.where(predicates.toArray(new Predicate[0]));

        if (pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> {
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

    public long countByCriteria(String department, Boolean isRemunerate, Long managerId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Intern> root = query.from(Intern.class);

        List<Predicate> predicates = buildPredicates(builder, root, department, isRemunerate, managerId);

        query.select(builder.count(root));
        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getSingleResult();
    }

    private List<Predicate> buildPredicates(
            CriteriaBuilder builder,
            Root<Intern> root,
            String department,
            Boolean isRemunerate,
            Long managerId) {
        List<Predicate> predicates = new ArrayList<>();

        if (department != null && !department.isEmpty()) {
            Department dept = Department.valueOf(department);
            predicates.add(builder.equal(root.get("department"), dept));
        }

        if (isRemunerate != null) {
            predicates.add(builder.equal(root.get("isRemunerate"), isRemunerate));
        }

        if (managerId != null) {
            predicates.add(builder.equal(root.get("manager").get("id"), managerId));
        }

        return predicates;
    }
}
