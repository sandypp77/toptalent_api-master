package com.project.dia.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdatePhotoDTO {
    private int recruiterId;
    private String recruiterImage;

    public UpdatePhotoDTO(int recruiterId, String recruiterImage) {
        this.recruiterId = recruiterId;
        this.recruiterImage = recruiterImage;
    }
}
