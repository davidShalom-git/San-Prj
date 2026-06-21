package com.resumebuilder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EducationDto {

    private Long id;

    @NotBlank(message = "Degree is required")
    private String degree;

    @NotBlank(message = "College name is required")
    private String collegeName;

    @NotBlank(message = "Year is required")
    private String year;

    @NotBlank(message = "CGPA/Percentage is required")
    private String cgpaOrPercentage;

    @NotNull(message = "User id is required")
    private Long userId;
}
