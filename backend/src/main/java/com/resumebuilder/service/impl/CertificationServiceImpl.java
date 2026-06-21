package com.resumebuilder.service.impl;

import com.resumebuilder.dto.CertificationDto;
import com.resumebuilder.entity.Certification;
import com.resumebuilder.entity.User;
import com.resumebuilder.exception.ResourceNotFoundException;
import com.resumebuilder.repository.CertificationRepository;
import com.resumebuilder.service.CertificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificationServiceImpl implements CertificationService {

    private final CertificationRepository certificationRepository;
    private final UserServiceImpl userService;

    @Override
    public CertificationDto createCertification(CertificationDto certificationDto) {
        User user = userService.findUser(certificationDto.getUserId());
        Certification certification = toEntity(certificationDto, user);
        return toDto(certificationRepository.save(certification));
    }

    @Override
    public CertificationDto getCertificationById(Long id) {
        return toDto(findCertification(id));
    }

    @Override
    public List<CertificationDto> getCertificationsByUserId(Long userId) {
        userService.findUser(userId);
        return certificationRepository.findByUserId(userId).stream().map(this::toDto).toList();
    }

    @Override
    public CertificationDto updateCertification(Long id, CertificationDto certificationDto) {
        Certification certification = findCertification(id);
        User user = userService.findUser(certificationDto.getUserId());
        certification.setCertificationName(certificationDto.getCertificationName());
        certification.setOrganization(certificationDto.getOrganization());
        certification.setYear(certificationDto.getYear());
        certification.setUser(user);
        return toDto(certificationRepository.save(certification));
    }

    @Override
    public void deleteCertification(Long id) {
        certificationRepository.delete(findCertification(id));
    }

    private Certification findCertification(Long id) {
        return certificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certification not found with id: " + id));
    }

    private CertificationDto toDto(Certification certification) {
        CertificationDto dto = new CertificationDto();
        dto.setId(certification.getId());
        dto.setCertificationName(certification.getCertificationName());
        dto.setOrganization(certification.getOrganization());
        dto.setYear(certification.getYear());
        dto.setUserId(certification.getUser().getId());
        return dto;
    }

    private Certification toEntity(CertificationDto dto, User user) {
        Certification certification = new Certification();
        certification.setCertificationName(dto.getCertificationName());
        certification.setOrganization(dto.getOrganization());
        certification.setYear(dto.getYear());
        certification.setUser(user);
        return certification;
    }
}
