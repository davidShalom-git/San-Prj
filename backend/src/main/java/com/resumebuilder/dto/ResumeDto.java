package com.resumebuilder.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResumeDto {

    private UserDto user;
    private List<EducationDto> educations;
    private List<SkillDto> skills;
    private List<ProjectDto> projects;
    private List<ExperienceDto> experiences;
    private List<CertificationDto> certifications;
}
