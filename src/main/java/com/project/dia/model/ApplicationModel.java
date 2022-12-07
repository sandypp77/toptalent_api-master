package com.project.dia.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "application")
public class ApplicationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private int applicationId;

    @Column(name = "application_status", columnDefinition = "enum('Screening','Interview','Rejected','Accepted','Sent','Seen') default 'Sent' ")
    private String applicationStatus;

    @Column(name = "jobseeker_id")
    private int jobseekerId;

    @Column(name = "job_id")
    private int jobId;

    @Column(name = "recruiter_id")
    private int recruiterId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", shape = JsonFormat.Shape.STRING, locale = "in_ID")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
