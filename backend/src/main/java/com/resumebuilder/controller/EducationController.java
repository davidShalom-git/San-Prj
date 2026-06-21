package com.resumebuilder.controller;

import com.resumebuilder.dto.EducationDto;
import com.resumebuilder.service.EducationService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/education")
@RequiredArgsConstructor
public class EducationController {

    private final EducationService educationService;

    @PostMapping
    public ResponseEntity<EducationDto> createEducation(@Valid @RequestBody EducationDto educationDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(educationService.createEducation(educationDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EducationDto> getEducationById(@PathVariable Long id) {
        return ResponseEntity.ok(educationService.getEducationById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EducationDto>> getEducationByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(educationService.getEducationByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EducationDto> updateEducation(@PathVariable Long id, @Valid @RequestBody EducationDto educationDto) {
        return ResponseEntity.ok(educationService.updateEducation(id, educationDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long id) {
        educationService.deleteEducation(id);
        return ResponseEntity.noContent().build();
    }
}
