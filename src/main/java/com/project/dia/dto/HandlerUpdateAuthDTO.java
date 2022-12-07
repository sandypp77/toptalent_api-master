package com.project.dia.dto;

import lombok.Data;

@Data
public class HandlerUpdateAuthDTO {
    private String errorCode;
    private UpdateProfileDTO updateProfileDTO;

    public HandlerUpdateAuthDTO(String errorCode, UpdateProfileDTO updateProfileDTO) {
        this.errorCode = errorCode;
        this.updateProfileDTO = updateProfileDTO;
    }
}
