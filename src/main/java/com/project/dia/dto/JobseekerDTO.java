package com.project.dia.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
public class JobseekerDTO extends RegisterJobseekerDTO {
    private int jobseekerId;
    private String jobseekerName;
    private String jobseekerEmail;
    private String jobseekerImage;
    private String jobseekerEducation;
    private String jobseekerResume;
    private String jobseekerAddress;
    private String jobseekerPhone;
    private String jobseekerAbout;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate jobseekerDateOfBirth;
    private String jobseekerProfession;
    private String jobseekerPortfolio;
    private String jobseekerMedsos;
    private String jobseekerSkill;
    private String jobseekerCompany;
    private Integer workStartYear;
    private Integer workEndYear;
    private byte[] jobseekerFile;
}
