package com.project.dia.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HandlerRegisterDTO {
    private String errorCode;
    private RegisterJobseekerDTO registerJobseekerDTO;

    public HandlerRegisterDTO(String errorCode, RegisterJobseekerDTO registerJobseekerDTO) {
        this.errorCode = errorCode;
        this.registerJobseekerDTO = registerJobseekerDTO;
    }
}


