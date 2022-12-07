package com.project.dia.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JobModelDTO {
    private int jobId;
    private String jobName;
    private int jobSalary;
    private String jobPosition;
    private String jobAddress;
    private String jobDesc;
    private String jobRequirement;
    private String jobStatus;

}
