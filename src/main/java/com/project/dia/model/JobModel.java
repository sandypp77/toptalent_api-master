package com.project.dia.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;


import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "job")
public class JobModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private int jobId;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "job_salary")
    private int jobSalary;

    @Column(name = "job_position")
    private String jobPosition;

    @Column(name = "job_address")
    private String jobAddress;

    @Column(name = "job_desc")
    @Type(type = "text")
    private String jobDesc;

    @Column(name = "job_requirement")
    @Type(type = "text")
    private String jobRequirement;

    @Column(name = "job_status", columnDefinition = "enum('Active','Hidden','Visible','NonActive') default 'Active' ")
    private String jobStatus;

    @Column(name = "recruiter_id")
    private int recruiterId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", shape = JsonFormat.Shape.STRING, locale = "in_ID")
    @Column(name = "created_at")
    private LocalDateTime createdAt;


    public JobModel() {

    }
}
