package com.resumebuilder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillDto {

    private Long id;

    @NotBlank(message = "Skill name is required")
    private String skillName;

    @NotNull(message = "User id is required")
    private Long userId;
}
