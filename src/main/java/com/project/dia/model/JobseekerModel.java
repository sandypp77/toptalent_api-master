package com.project.dia.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "jobseeker")
public class JobseekerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jobseeker_id")
    private int jobseekerId;

    @Column(name = "jobseeker_name")
    private String jobseekerName;

    @Email(message = "Email should be valid")
    @Column(name = "jobseeker_email")
    private String jobseekerEmail;

    @Column(name = "jobseeker_password")
    private String jobseekerPassword;

    @Column(name = "jobseeker_image")
    private String jobseekerImage;

    @Column(name = "jobseeker_education")
    private String jobseekerEducation;

    @Column(name = "jobseeker_resume")
    private String jobseekerResume;

    @Column(name = "jobseeker_address")
    private String jobseekerAddress;

    @Column(name = "jobseeker_phone")
    private String jobseekerPhone;

    @Type(type = "text")
    @Column(name = "jobseeker_about")
    private String jobseekerAbout;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "jobseeker_dateofbirth")
    private LocalDate jobseekerDateOfBirth;

    @Column(name = "created_at")
    private Date jobseekerCreatedAt;

    @Column(name = "status", columnDefinition = "enum('active','notactive') default 'notactive' ")
    private String status;

    @Column(name = "jobseeker_profession")
    private String jobseekerProfession;

    @Column(name = "jobseeker_portfolio")
    private String jobseekerPortfolio;

    @Column(name = "jobseeker_medsos")
    private String jobseekerMedsos;

    @Column(name = "jobseeker_skill")
    private String jobseekerSkill;

    @Column(name = "jobseeker_company")
    private String jobseekerCompany;

    @Column(name = "work_start_year")
    private Integer workStartYear;

    @Column(name = "work_end_year")
    private Integer workEndYear;

}
