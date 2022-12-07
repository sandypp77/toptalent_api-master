package com.project.dia.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class PostJobDTO {
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
    private String recruiterCompany;
    private String recruiterDesc;



    public PostJobDTO(int jobId, String jobName, int jobSalary, String jobPosition, String jobAddress, String jobDesc, String jobRequirement, String jobStatus, LocalDateTime createdAt, String recruiterImage, String recruiterCompany, String recruiterDesc) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.jobSalary = jobSalary;
        this.jobPosition = jobPosition;
        this.jobAddress = jobAddress;
        this.jobDesc = jobDesc;
        this.jobRequirement = jobRequirement;
        this.jobStatus = jobStatus;
        this.createdAt = createdAt;
        this.recruiterImage = recruiterImage;
        this.recruiterCompany = recruiterCompany;
        this.recruiterDesc = recruiterDesc;
    }
}
