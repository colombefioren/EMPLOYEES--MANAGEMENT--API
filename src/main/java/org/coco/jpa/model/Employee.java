package org.coco.jpa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "employee")
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @NotBlank
  @Column(nullable = false, name = "first_name")
  private String firstName;

  @Column(unique = true)
  @Email(message = "Invalid email format")
  private String email;

  @Enumerated(EnumType.STRING)
  private Department department;

  @DecimalMin(value = "1500.0", message = "Salary must be at least 1500?")
  private BigDecimal salary;

  @Column(name = "is_active")
  private Boolean isActive = true;

  @OneToMany(
      mappedBy = "manager",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY)
  private List<Intern> interns = new ArrayList<>();
}
