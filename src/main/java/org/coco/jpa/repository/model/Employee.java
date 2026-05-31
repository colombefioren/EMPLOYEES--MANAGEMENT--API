package org.coco.jpa.repository.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
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
  private String email;

  @Enumerated(EnumType.STRING)
  private Department department;

  private BigDecimal salary;

  @Column(name = "is_active")
  private Boolean isActive = true;
}
