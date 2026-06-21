package com.resumebuilder.controller;

import com.resumebuilder.dto.CertificationDto;
import com.resumebuilder.service.CertificationService;
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
@RequestMapping("/api/certifications")
@RequiredArgsConstructor
public class CertificationController {

    private final CertificationService certificationService;

    @PostMapping
    public ResponseEntity<CertificationDto> createCertification(@Valid @RequestBody CertificationDto certificationDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(certificationService.createCertification(certificationDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificationDto> getCertificationById(@PathVariable Long id) {
        return ResponseEntity.ok(certificationService.getCertificationById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CertificationDto>> getCertificationsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(certificationService.getCertificationsByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CertificationDto> updateCertification(@PathVariable Long id, @Valid @RequestBody CertificationDto certificationDto) {
        return ResponseEntity.ok(certificationService.updateCertification(id, certificationDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCertification(@PathVariable Long id) {
        certificationService.deleteCertification(id);
        return ResponseEntity.noContent().build();
    }
}
