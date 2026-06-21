package com.resumebuilder.service;

import com.resumebuilder.dto.ExperienceDto;
import java.util.List;

public interface ExperienceService {
    ExperienceDto createExperience(ExperienceDto experienceDto);
    ExperienceDto getExperienceById(Long id);
    List<ExperienceDto> getExperiencesByUserId(Long userId);
    ExperienceDto updateExperience(Long id, ExperienceDto experienceDto);
    void deleteExperience(Long id);
}
