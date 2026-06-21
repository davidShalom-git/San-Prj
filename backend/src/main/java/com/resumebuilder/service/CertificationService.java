package com.resumebuilder.service;

import com.resumebuilder.dto.CertificationDto;
import java.util.List;

public interface CertificationService {
    CertificationDto createCertification(CertificationDto certificationDto);
    CertificationDto getCertificationById(Long id);
    List<CertificationDto> getCertificationsByUserId(Long userId);
    CertificationDto updateCertification(Long id, CertificationDto certificationDto);
    void deleteCertification(Long id);
}
