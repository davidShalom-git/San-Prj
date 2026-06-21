package com.resumebuilder.service;

import com.resumebuilder.dto.AiResumeRequestDto;
import com.resumebuilder.dto.ResumeFormDataDto;

public interface AiResumeService {

    ResumeFormDataDto tailorResume(AiResumeRequestDto request);
}
