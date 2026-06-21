package com.resumebuilder.service.impl;

import com.resumebuilder.dto.SkillDto;
import com.resumebuilder.entity.Skill;
import com.resumebuilder.entity.User;
import com.resumebuilder.exception.ResourceNotFoundException;
import com.resumebuilder.repository.SkillRepository;
import com.resumebuilder.service.SkillService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final UserServiceImpl userService;

    @Override
    public SkillDto createSkill(SkillDto skillDto) {
        User user = userService.findUser(skillDto.getUserId());
        Skill skill = toEntity(skillDto, user);
        return toDto(skillRepository.save(skill));
    }

    @Override
    public SkillDto getSkillById(Long id) {
        return toDto(findSkill(id));
    }

    @Override
    public List<SkillDto> getSkillsByUserId(Long userId) {
        userService.findUser(userId);
        return skillRepository.findByUserId(userId).stream().map(this::toDto).toList();
    }

    @Override
    public SkillDto updateSkill(Long id, SkillDto skillDto) {
        Skill skill = findSkill(id);
        User user = userService.findUser(skillDto.getUserId());
        skill.setSkillName(skillDto.getSkillName());
        skill.setUser(user);
        return toDto(skillRepository.save(skill));
    }

    @Override
    public void deleteSkill(Long id) {
        skillRepository.delete(findSkill(id));
    }

    private Skill findSkill(Long id) {
        return skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with id: " + id));
    }

    private SkillDto toDto(Skill skill) {
        SkillDto dto = new SkillDto();
        dto.setId(skill.getId());
        dto.setSkillName(skill.getSkillName());
        dto.setUserId(skill.getUser().getId());
        return dto;
    }

    private Skill toEntity(SkillDto dto, User user) {
        Skill skill = new Skill();
        skill.setSkillName(dto.getSkillName());
        skill.setUser(user);
        return skill;
    }
}
