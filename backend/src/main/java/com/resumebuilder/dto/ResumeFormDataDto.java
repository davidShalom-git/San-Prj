package com.resumebuilder.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResumeFormDataDto {

    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String linkedInUrl;
    private String githubUrl;
    private String professionalSummary;
    private String resumeTemplate;
    private List<EducationDto> educations = new ArrayList<>();
    private List<SkillDto> skills = new ArrayList<>();
    private List<ProjectDto> projects = new ArrayList<>();
    private List<ExperienceDto> experiences = new ArrayList<>();
    private List<CertificationDto> certifications = new ArrayList<>();
}
