package com.resumebuilder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectDto {

    private Long id;

    @NotBlank(message = "Project title is required")
    private String projectTitle;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Technologies used is required")
    private String technologiesUsed;

    @NotNull(message = "User id is required")
    private Long userId;
}
