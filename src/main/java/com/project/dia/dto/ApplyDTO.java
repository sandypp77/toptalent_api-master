package com.project.dia.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Setter
@Getter
public class ApplyDTO {
    private int jobId;
    private String jobName;
    private String recruiterCompany;
    private String recruiterAddress;
    private String applicationStatus;
    private String recruiterImage;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;

    public ApplyDTO(int jobId, String jobName, String recruiterCompany, String recruiterAddress, String applicationStatus, String recruiterImage, LocalDateTime createdAt) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.recruiterCompany = recruiterCompany;
        this.recruiterAddress = recruiterAddress;
        this.applicationStatus = applicationStatus;
        this.recruiterImage = recruiterImage;
        this.createdAt = createdAt;
    }

    public ApplyDTO() {
    }
}
