package org.coco.jpa.model.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.coco.jpa.model.Department;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InternDto {
  private Long id;
  private String firstName;
  private String email;
  private Department department;
  private BigDecimal salary;
  private Boolean isRemunerate;
  private Long managerId;
}
