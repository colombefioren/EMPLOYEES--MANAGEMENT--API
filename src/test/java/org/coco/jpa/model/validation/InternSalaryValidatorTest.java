package org.coco.jpa.model.validation;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.coco.jpa.model.Intern;
import org.junit.jupiter.api.Test;

class InternSalaryValidatorTest {

  private final InternSalaryValidator validator = new InternSalaryValidator();

  @Test
  void should_be_valid_when_intern_is_null() {
    assertTrue(validator.isValid(null, null));
  }

  @Test
  void should_be_valid_when_not_remunerated() {
    var intern = new Intern();
    intern.setIsRemunerate(false);

    assertTrue(validator.isValid(intern, null));
  }

  @Test
  void should_be_valid_when_remunerated_and_salary_meets_minimum() {
    var intern = new Intern();
    intern.setIsRemunerate(true);
    intern.setSalary(new BigDecimal("100.0"));

    assertTrue(validator.isValid(intern, null));
  }

  @Test
  void should_be_valid_when_remunerated_and_salary_above_minimum() {
    var intern = new Intern();
    intern.setIsRemunerate(true);
    intern.setSalary(new BigDecimal("500.0"));

    assertTrue(validator.isValid(intern, null));
  }

  @Test
  void should_be_invalid_when_remunerated_and_salary_below_minimum() {
    var intern = new Intern();
    intern.setIsRemunerate(true);
    intern.setSalary(new BigDecimal("50.0"));

    assertFalse(validator.isValid(intern, null));
  }

  @Test
  void should_be_invalid_when_remunerated_and_salary_is_null() {
    var intern = new Intern();
    intern.setIsRemunerate(true);
    intern.setSalary(null);

    assertFalse(validator.isValid(intern, null));
  }
}
