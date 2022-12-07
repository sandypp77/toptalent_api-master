package com.project.dia.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
public class ApplicantDTO {
    private int applicationId;
    private int jobId;
    private String jobName;
    private String jobPosition;
    private int jobseekerId;
    private String jobseekerName;
    private String jobseekerEmail;
    private String jobseekerResume;
    private String jobseekerImage;
    private String jobseekerAddress;
    private String jobseekerPhone;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate jobseekerDateOfBirth;
    private String jobseekerProfession;
    private String jobseekerPortfolio;
    private String applicationStatus;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;

    public ApplicantDTO(int applicationId, int jobId, String jobName, String jobPosition, int jobseekerId, String jobseekerName, String jobseekerEmail, String jobseekerResume, String jobseekerImage, String jobseekerAddress, String jobseekerPhone, LocalDate jobseekerDateOfBirth, String jobseekerProfession, String jobseekerPortfolio, String applicationStatus, LocalDateTime createdAt) {
        this.applicationId = applicationId;
        this.jobId = jobId;
        this.jobName = jobName;
        this.jobPosition = jobPosition;
        this.jobseekerId = jobseekerId;
        this.jobseekerName = jobseekerName;
        this.jobseekerEmail = jobseekerEmail;
        this.jobseekerResume = jobseekerResume;
        this.jobseekerImage = jobseekerImage;
        this.jobseekerAddress = jobseekerAddress;
        this.jobseekerPhone = jobseekerPhone;
        this.jobseekerDateOfBirth = jobseekerDateOfBirth;
        this.jobseekerProfession = jobseekerProfession;
        this.jobseekerPortfolio = jobseekerPortfolio;
        this.applicationStatus = applicationStatus;
        this.createdAt = createdAt;
    }

    //    public ApplicantDTO(int applicationId, int jobId, String jobName, String jobPosition, int jobseekerId, String jobseekerName, String jobseekerEmail, String jobseekerResume, String jobseekerImage, String jobseekerAddress, String jobseekerPhone, LocalDate jobseekerDateOfBirth, String jobseekerProfession, String jobseekerPortfolio, String applicationStatus) {
//        this.applicationId = applicationId;
//        this.jobId = jobId;
//        this.jobName = jobName;
//        this.jobPosition = jobPosition;
//        this.jobseekerId = jobseekerId;
//        this.jobseekerName = jobseekerName;
//        this.jobseekerEmail = jobseekerEmail;
//        this.jobseekerResume = jobseekerResume;
//        this.jobseekerImage = jobseekerImage;
//        this.jobseekerAddress = jobseekerAddress;
//        this.jobseekerPhone = jobseekerPhone;
//        this.jobseekerDateOfBirth = jobseekerDateOfBirth;
//        this.jobseekerProfession = jobseekerProfession;
//        this.jobseekerPortfolio = jobseekerPortfolio;
//        this.applicationStatus = applicationStatus;
//    }


    public ApplicantDTO() {
    }
}
