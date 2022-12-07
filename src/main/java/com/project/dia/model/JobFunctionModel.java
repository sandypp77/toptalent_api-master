package com.project.dia.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "jobfunction")
public class JobFunctionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_function_id")
    private int jobFunctionId;

    @Column(name = "job_function")
    private String jobFunction;
}
