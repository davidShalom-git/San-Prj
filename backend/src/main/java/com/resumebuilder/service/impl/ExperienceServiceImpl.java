package com.resumebuilder.service.impl;

import com.resumebuilder.dto.ExperienceDto;
import com.resumebuilder.entity.Experience;
import com.resumebuilder.entity.User;
import com.resumebuilder.exception.ResourceNotFoundException;
import com.resumebuilder.repository.ExperienceRepository;
import com.resumebuilder.service.ExperienceService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final UserServiceImpl userService;

    @Override
    public ExperienceDto createExperience(ExperienceDto experienceDto) {
        User user = userService.findUser(experienceDto.getUserId());
        Experience experience = toEntity(experienceDto, user);
        return toDto(experienceRepository.save(experience));
    }

    @Override
    public ExperienceDto getExperienceById(Long id) {
        return toDto(findExperience(id));
    }

    @Override
    public List<ExperienceDto> getExperiencesByUserId(Long userId) {
        userService.findUser(userId);
        return experienceRepository.findByUserId(userId).stream().map(this::toDto).toList();
    }

    @Override
    public ExperienceDto updateExperience(Long id, ExperienceDto experienceDto) {
        Experience experience = findExperience(id);
        User user = userService.findUser(experienceDto.getUserId());
        experience.setCompanyName(experienceDto.getCompanyName());
        experience.setRole(experienceDto.getRole());
        experience.setDuration(experienceDto.getDuration());
        experience.setDescription(experienceDto.getDescription());
        experience.setUser(user);
        return toDto(experienceRepository.save(experience));
    }

    @Override
    public void deleteExperience(Long id) {
        experienceRepository.delete(findExperience(id));
    }

    private Experience findExperience(Long id) {
        return experienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found with id: " + id));
    }

    private ExperienceDto toDto(Experience experience) {
        ExperienceDto dto = new ExperienceDto();
        dto.setId(experience.getId());
        dto.setCompanyName(experience.getCompanyName());
        dto.setRole(experience.getRole());
        dto.setDuration(experience.getDuration());
        dto.setDescription(experience.getDescription());
        dto.setUserId(experience.getUser().getId());
        return dto;
    }

    private Experience toEntity(ExperienceDto dto, User user) {
        Experience experience = new Experience();
        experience.setCompanyName(dto.getCompanyName());
        experience.setRole(dto.getRole());
        experience.setDuration(dto.getDuration());
        experience.setDescription(dto.getDescription());
        experience.setUser(user);
        return experience;
    }
}
