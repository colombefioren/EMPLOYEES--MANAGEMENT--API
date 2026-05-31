package org.coco.jpa.model.dto;

import java.math.BigDecimal;
import org.coco.jpa.model.Department;

public class EmployeeDto {
  public Long id;
  public String firstName;
  public String email;
  public Department department;
  public BigDecimal salary;
  public Boolean isActive;
}
