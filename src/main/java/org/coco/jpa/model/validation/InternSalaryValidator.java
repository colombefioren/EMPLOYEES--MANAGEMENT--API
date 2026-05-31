package org.coco.jpa.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import org.coco.jpa.model.Intern;

public class InternSalaryValidator implements ConstraintValidator<ValidInternSalary, Intern> {

  @Override
  public boolean isValid(Intern intern, ConstraintValidatorContext ctx) {
    if (intern == null) return true;
    if (Boolean.TRUE.equals(intern.getIsRemunerate())) {
      return intern.getSalary() != null
          && intern.getSalary().compareTo(new BigDecimal("100.0")) >= 0;
    }
    return true;
  }
}
