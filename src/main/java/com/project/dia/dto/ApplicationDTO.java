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
public class ApplicationDTO {
    private int applicationId;
    private int jobId;
    private int jobseekerId;
    private int recruiterId;
    private String applicationStatus;
    private String jobseekerResume;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;

    public ApplicationDTO(int applicationId, int jobId, int jobseekerId, int recruiterId, String applicationStatus, String jobseekerResume, LocalDateTime createdAt) {
        this.applicationId = applicationId;
        this.jobId = jobId;
        this.jobseekerId = jobseekerId;
        this.recruiterId = recruiterId;
        this.applicationStatus = applicationStatus;
        this.jobseekerResume = jobseekerResume;
        this.createdAt = createdAt;
    }

    public ApplicationDTO() {
    }
}
