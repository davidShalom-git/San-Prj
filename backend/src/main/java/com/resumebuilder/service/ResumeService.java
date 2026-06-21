package com.resumebuilder.service;

import com.resumebuilder.dto.ResumeDto;

public interface ResumeService {
    ResumeDto getResumeByUserId(Long userId);
    byte[] generateResumePdf(Long userId);
}
