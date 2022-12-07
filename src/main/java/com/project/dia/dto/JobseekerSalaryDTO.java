package com.project.dia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JobseekerSalaryDTO {
    private int salaryId;
    private int jobseekerId;
    private String currentCurrency;
    private String expectedCurrency;
    private float currentSalary;
    private float expectedMinimum;
    private float expectedMaximum;
}
