package com.project.dia.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "jobseeker_experience")
public class JobseekerExperience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "experience_id")
    private int experienceId;

    @Column(name = "jobseeker_id")
    private int jobseekerId;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "job_type_id")
    private int jobTypeId;

    @Column(name = "start_period_month")
    private String startPeriodMonth;

    @Column(name = "end_period_month")
    private String endPeriodMonth;

    @Column(name = "start_period_year")
    private float startPeriodYear;

    @Column(name = "end_period_year")
    private float endPeriodYear;

    @Column(name = "status", columnDefinition = "enum('present','notpresent') default 'notpresent' ")
    private String status;
}
