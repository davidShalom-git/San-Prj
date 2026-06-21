package com.resumebuilder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CertificationDto {

    private Long id;

    @NotBlank(message = "Certification name is required")
    private String certificationName;

    @NotBlank(message = "Organization is required")
    private String organization;

    @NotBlank(message = "Year is required")
    private String year;

    @NotNull(message = "User id is required")
    private Long userId;
}
