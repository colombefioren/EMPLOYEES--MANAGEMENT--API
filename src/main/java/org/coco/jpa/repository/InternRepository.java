package org.coco.jpa.repository;

import java.util.List;
import org.coco.jpa.model.Intern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternRepository extends JpaRepository<Intern, Long> {
  List<Intern> findByManagerId(Long managerId);
}
