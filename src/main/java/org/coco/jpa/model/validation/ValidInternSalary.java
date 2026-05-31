package org.coco.jpa.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = InternSalaryValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidInternSalary {
  String message() default "Salary must be at least 100 if remunerated";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
