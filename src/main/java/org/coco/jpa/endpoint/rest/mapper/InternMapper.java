package org.coco.jpa.endpoint.rest.mapper;

import org.coco.jpa.model.Intern;
import org.coco.jpa.model.dto.InternDto;
import org.springframework.stereotype.Component;

@Component
public class InternMapper {

  public InternDto toRest(Intern intern) {
    return InternDto.builder()
        .id(intern.getId())
        .firstName(intern.getFirstName())
        .email(intern.getEmail())
        .department(intern.getDepartment())
        .salary(intern.getSalary())
        .isRemunerate(intern.getIsRemunerate())
        .managerId(intern.getManager() != null ? intern.getManager().getId() : null)
        .build();
  }
}
