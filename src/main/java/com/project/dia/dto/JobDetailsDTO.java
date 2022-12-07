package com.project.dia.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JobDetailsDTO {
    private int jobId;
    private String jobName;
    private int jobSalary;
    private String jobPosition;
    private String jobAddress;
    private String jobDesc;
    private String jobRequirement;
    private String jobStatus;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;
    private String recruiterImage;
    private String recruiterAddress;
    private String recruiterCompany;
    private String recruiterDesc;
    private String recruiterIndustry;
    private String recruiterStaff;
    private String recruiterBenefit;
    private String recruiterFb;
    private String recruiterLinkedin;
    private String recruiterIg;
    private String recruiterCulture;
    private String recruiterWebsite;


}
