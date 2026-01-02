package indi.daniel.esbcommonservice.company.repository.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Employee {

    private String employeeNumber;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private LocalDate hireDate;

    private BigDecimal salary;

    private String department;

    private String positionTitle;

    private Boolean isActive;

    private LocalDateTime createdAt;
}
