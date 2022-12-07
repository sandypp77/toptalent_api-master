package com.project.dia.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "jobseeker_salary")
public class JobseekerSalary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salary_id")
    private int salaryId;

    @Column(name = "jobseeker_id")
    private int jobseekerId;

    @Column(name = "current_currency")
    private String currentCurrency;

    @Column(name = "expected_currency")
    private String expectedCurrency;

    @Column(name = "current_salary")
    private float currentSalary;

    @Column(name = "expected_minimum")
    private float expectedMinimum;

    @Column(name = "expected_maximum")
    private float expectedMaximum;
}
