package com.resumebuilder.service.impl;

import com.resumebuilder.dto.UserDto;
import com.resumebuilder.entity.User;
import com.resumebuilder.exception.ResourceNotFoundException;
import com.resumebuilder.repository.UserRepository;
import com.resumebuilder.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = toEntity(userDto);
        return toDto(userRepository.save(user));
    }

    @Override
    public UserDto getUserById(Long id) {
        return toDto(findUser(id));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = findUser(id);
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setAddress(userDto.getAddress());
        user.setLinkedInUrl(userDto.getLinkedInUrl());
        user.setGithubUrl(userDto.getGithubUrl());
        user.setProfessionalSummary(userDto.getProfessionalSummary());
        user.setResumeTemplate(normalizeTemplate(userDto.getResumeTemplate()));
        return toDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.delete(findUser(id));
    }

    public User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAddress(user.getAddress());
        dto.setLinkedInUrl(user.getLinkedInUrl());
        dto.setGithubUrl(user.getGithubUrl());
        dto.setProfessionalSummary(user.getProfessionalSummary());
        dto.setResumeTemplate(normalizeTemplate(user.getResumeTemplate()));
        return dto;
    }

    private User toEntity(UserDto dto) {
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setLinkedInUrl(dto.getLinkedInUrl());
        user.setGithubUrl(dto.getGithubUrl());
        user.setProfessionalSummary(dto.getProfessionalSummary());
        user.setResumeTemplate(normalizeTemplate(dto.getResumeTemplate()));
        return user;
    }

    private String normalizeTemplate(String template) {
        if ("classic".equalsIgnoreCase(template) || "compact".equalsIgnoreCase(template)) {
            return template.toLowerCase();
        }
        return "modern";
    }
}
