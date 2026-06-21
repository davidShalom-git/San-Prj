package com.resumebuilder.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiResumeRequestDto {

    @NotBlank(message = "Prompt is required")
    private String prompt;

    private ResumeFormDataDto resume;
}
