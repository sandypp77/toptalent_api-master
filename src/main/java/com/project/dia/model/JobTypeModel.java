package com.project.dia.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "jobtype")
public class JobTypeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_type_id")
    private int jobTypeId;

    @Column(name = "job_type")
    private String jobType;

    public JobTypeModel() {

    }
}
