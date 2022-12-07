package com.project.dia.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HandlerAuthJobSeekerDTO {
    private String errorCode;
    private RegisterJobseekerDTO registerJobseekerDTO;

    public HandlerAuthJobSeekerDTO(String errorCode, RegisterJobseekerDTO registerJobseekerDTO) {
        this.errorCode = errorCode;
        this.registerJobseekerDTO = registerJobseekerDTO;
    }
}
