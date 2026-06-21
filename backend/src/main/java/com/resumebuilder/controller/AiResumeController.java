package com.resumebuilder.controller;

import com.resumebuilder.dto.AiResumeRequestDto;
import com.resumebuilder.dto.ResumeFormDataDto;
import com.resumebuilder.service.AiResumeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiResumeController {

    private final AiResumeService aiResumeService;

    @PostMapping("/resume-tailor")
    public ResponseEntity<ResumeFormDataDto> tailorResume(@Valid @RequestBody AiResumeRequestDto request) {
        return ResponseEntity.ok(aiResumeService.tailorResume(request));
    }
}
