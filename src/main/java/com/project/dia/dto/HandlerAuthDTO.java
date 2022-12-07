package com.project.dia.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HandlerAuthDTO {
    private String errorCode;
    private RegisterDTO registerDTO;

    public HandlerAuthDTO(String errorCode, RegisterDTO registerDTO) {
        this.errorCode = errorCode;
        this.registerDTO = registerDTO;
    }
}
