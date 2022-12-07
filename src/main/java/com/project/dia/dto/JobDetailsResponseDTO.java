package com.project.dia.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class JobDetailsResponseDTO {
    private int code;
    private JobDetailsDTO jobDetails;
    private JobDetailsWithStatusDTO jobDetailsWithStatus;

}
