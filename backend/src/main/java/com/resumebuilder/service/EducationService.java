package com.resumebuilder.service;

import com.resumebuilder.dto.EducationDto;
import java.util.List;

public interface EducationService {
    EducationDto createEducation(EducationDto educationDto);
    EducationDto getEducationById(Long id);
    List<EducationDto> getEducationByUserId(Long userId);
    EducationDto updateEducation(Long id, EducationDto educationDto);
    void deleteEducation(Long id);
}
