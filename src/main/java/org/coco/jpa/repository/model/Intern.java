package org.coco.jpa.repository.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "intern")
public class Intern {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @NotBlank(message = "First name is required")
  @Column(nullable = false, name = "first_name")
  private String firstName;

  @Email(message = "Invalid email format")
  @Column(unique = true)
  private String email;

  @Enumerated(EnumType.STRING)
  private Department department;

  @DecimalMin(value = "100.0", message = "Salary must be at least 100? if remunerated")
  private BigDecimal salary;

  @Column(name = "is_remunerate")
  private Boolean isRemunerate = false;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "manager_id")
  @JsonBackReference
  private Employee manager;
}
