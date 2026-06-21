package com.resumebuilder.service.impl;

import com.resumebuilder.dto.EducationDto;
import com.resumebuilder.entity.Education;
import com.resumebuilder.entity.User;
import com.resumebuilder.exception.ResourceNotFoundException;
import com.resumebuilder.repository.EducationRepository;
import com.resumebuilder.service.EducationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService {

    private final EducationRepository educationRepository;
    private final UserServiceImpl userService;

    @Override
    public EducationDto createEducation(EducationDto educationDto) {
        User user = userService.findUser(educationDto.getUserId());
        Education education = toEntity(educationDto, user);
        return toDto(educationRepository.save(education));
    }

    @Override
    public EducationDto getEducationById(Long id) {
        return toDto(findEducation(id));
    }

    @Override
    public List<EducationDto> getEducationByUserId(Long userId) {
        userService.findUser(userId);
        return educationRepository.findByUserId(userId).stream().map(this::toDto).toList();
    }

    @Override
    public EducationDto updateEducation(Long id, EducationDto educationDto) {
        Education education = findEducation(id);
        User user = userService.findUser(educationDto.getUserId());
        education.setDegree(educationDto.getDegree());
        education.setCollegeName(educationDto.getCollegeName());
        education.setYear(educationDto.getYear());
        education.setCgpaOrPercentage(educationDto.getCgpaOrPercentage());
        education.setUser(user);
        return toDto(educationRepository.save(education));
    }

    @Override
    public void deleteEducation(Long id) {
        educationRepository.delete(findEducation(id));
    }

    private Education findEducation(Long id) {
        return educationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Education not found with id: " + id));
    }

    private EducationDto toDto(Education education) {
        EducationDto dto = new EducationDto();
        dto.setId(education.getId());
        dto.setDegree(education.getDegree());
        dto.setCollegeName(education.getCollegeName());
        dto.setYear(education.getYear());
        dto.setCgpaOrPercentage(education.getCgpaOrPercentage());
        dto.setUserId(education.getUser().getId());
        return dto;
    }

    private Education toEntity(EducationDto dto, User user) {
        Education education = new Education();
        education.setDegree(dto.getDegree());
        education.setCollegeName(dto.getCollegeName());
        education.setYear(dto.getYear());
        education.setCgpaOrPercentage(dto.getCgpaOrPercentage());
        education.setUser(user);
        return education;
    }
}
