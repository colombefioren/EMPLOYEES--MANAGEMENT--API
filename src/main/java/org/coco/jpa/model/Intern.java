package org.coco.jpa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.coco.jpa.model.validation.ValidInternSalary;

@Entity
@Getter
@Setter
@Table(name = "intern")
@ValidInternSalary
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

  private BigDecimal salary;

  @Column(name = "is_remunerate")
  private Boolean isRemunerate = false;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "manager_id")
  private Employee manager;
}
