package com.resumebuilder.service;

import com.resumebuilder.dto.SkillDto;
import java.util.List;

public interface SkillService {
    SkillDto createSkill(SkillDto skillDto);
    SkillDto getSkillById(Long id);
    List<SkillDto> getSkillsByUserId(Long userId);
    SkillDto updateSkill(Long id, SkillDto skillDto);
    void deleteSkill(Long id);
}
