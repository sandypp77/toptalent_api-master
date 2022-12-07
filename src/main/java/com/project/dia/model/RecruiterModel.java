package com.project.dia.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;


@Setter
@Getter
@Entity
@Table(name = "recruiter")
public class RecruiterModel {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruiter_id")
    private int recruiterId;

    @Column(name = "recruiter_email", unique = true, nullable = false)
    private String recruiterEmail;

    @Column(name = "recruiter_password")
    private String recruiterPassword;

    @Column(name = "recruiter_phone")
    private String recruiterPhone;

    @Column(name = "recruiter_staff")
    private String recruiterStaff;

    @Column(name = "recruiter_image")
    private String recruiterImage;
    @Column(name = "recruiter_desc")
    @Type(type = "text")
    private String recruiterDesc;
    @Column(name = "recruiter_address")
    private String recruiterAddress;
    @Column(name = "recruiter_company")
    private String recruiterCompany;
    @Column(name = "recruiter_industry")
    private String recruiterIndustry;

    @Column(name = "recruiter_status", columnDefinition = "enum('active','notactive') default 'notactive' ")
    private String recruiterStatus;

    @Column(name = "recruiter_fb")
    private String recruiterFb;

    @Column(name = "recruiter_ig")
    private String recruiterIg;

    @Column(name = "recruiter_linkedin")
    private String recruiterLinkedin;

    @Type(type = "text")
    @Column(name = "recruiter_culture")
    private String recruiterCulture;

    @Type(type = "text")
    @Column(name = "recruiter_benefit")
    private String recruiterBenefit;

    @Column(name = "recruiter_website")
    private String recruiterWebsite;

    @Column(name = "created_at")
    private java.sql.Timestamp createdAt;


}
