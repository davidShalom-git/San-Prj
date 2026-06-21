package com.resumebuilder.service.impl;

import com.resumebuilder.dto.ProjectDto;
import com.resumebuilder.entity.Project;
import com.resumebuilder.entity.User;
import com.resumebuilder.exception.ResourceNotFoundException;
import com.resumebuilder.repository.ProjectRepository;
import com.resumebuilder.service.ProjectService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserServiceImpl userService;

    @Override
    public ProjectDto createProject(ProjectDto projectDto) {
        User user = userService.findUser(projectDto.getUserId());
        Project project = toEntity(projectDto, user);
        return toDto(projectRepository.save(project));
    }

    @Override
    public ProjectDto getProjectById(Long id) {
        return toDto(findProject(id));
    }

    @Override
    public List<ProjectDto> getProjectsByUserId(Long userId) {
        userService.findUser(userId);
        return projectRepository.findByUserId(userId).stream().map(this::toDto).toList();
    }

    @Override
    public ProjectDto updateProject(Long id, ProjectDto projectDto) {
        Project project = findProject(id);
        User user = userService.findUser(projectDto.getUserId());
        project.setProjectTitle(projectDto.getProjectTitle());
        project.setDescription(projectDto.getDescription());
        project.setTechnologiesUsed(projectDto.getTechnologiesUsed());
        project.setUser(user);
        return toDto(projectRepository.save(project));
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.delete(findProject(id));
    }

    private Project findProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    private ProjectDto toDto(Project project) {
        ProjectDto dto = new ProjectDto();
        dto.setId(project.getId());
        dto.setProjectTitle(project.getProjectTitle());
        dto.setDescription(project.getDescription());
        dto.setTechnologiesUsed(project.getTechnologiesUsed());
        dto.setUserId(project.getUser().getId());
        return dto;
    }

    private Project toEntity(ProjectDto dto, User user) {
        Project project = new Project();
        project.setProjectTitle(dto.getProjectTitle());
        project.setDescription(dto.getDescription());
        project.setTechnologiesUsed(dto.getTechnologiesUsed());
        project.setUser(user);
        return project;
    }
}
