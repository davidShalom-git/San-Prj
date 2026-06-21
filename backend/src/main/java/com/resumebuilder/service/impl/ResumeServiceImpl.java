package com.resumebuilder.service.impl;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.resumebuilder.dto.CertificationDto;
import com.resumebuilder.dto.EducationDto;
import com.resumebuilder.dto.ExperienceDto;
import com.resumebuilder.dto.ProjectDto;
import com.resumebuilder.dto.ResumeDto;
import com.resumebuilder.dto.SkillDto;
import com.resumebuilder.dto.UserDto;
import com.resumebuilder.service.ResumeService;
import java.io.ByteArrayOutputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final UserServiceImpl userService;
    private final EducationServiceImpl educationService;
    private final SkillServiceImpl skillService;
    private final ProjectServiceImpl projectService;
    private final ExperienceServiceImpl experienceService;
    private final CertificationServiceImpl certificationService;

    @Override
    public ResumeDto getResumeByUserId(Long userId) {
        ResumeDto resumeDto = new ResumeDto();
        resumeDto.setUser(userService.getUserById(userId));
        resumeDto.setEducations(educationService.getEducationByUserId(userId));
        resumeDto.setSkills(skillService.getSkillsByUserId(userId));
        resumeDto.setProjects(projectService.getProjectsByUserId(userId));
        resumeDto.setExperiences(experienceService.getExperiencesByUserId(userId));
        resumeDto.setCertifications(certificationService.getCertificationsByUserId(userId));
        return resumeDto;
    }

    @Override
    public byte[] generateResumePdf(Long userId) {
        ResumeDto resume = getResumeByUserId(userId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UserDto user = resume.getUser();
        String template = user.getResumeTemplate() == null ? "modern" : user.getResumeTemplate();
        Document document = new Document(PageSize.A4, "compact".equals(template) ? 28 : 42, "compact".equals(template) ? 28 : 42, 36, 36);

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, "classic".equals(template) ? 18 : 20);
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, "compact".equals(template) ? 12 : 14);
            Font textFont = FontFactory.getFont(FontFactory.HELVETICA, "compact".equals(template) ? 10 : 11);

            document.add(new Paragraph(user.getFullName(), titleFont));
            document.add(new Paragraph(user.getEmail() + " | " + user.getPhoneNumber(), textFont));
            document.add(new Paragraph(user.getAddress(), textFont));
            document.add(new Paragraph("LinkedIn: " + defaultText(user.getLinkedInUrl()), textFont));
            document.add(new Paragraph("GitHub: " + defaultText(user.getGithubUrl()), textFont));
            document.add(new Paragraph(" "));

            if (user.getProfessionalSummary() != null && !user.getProfessionalSummary().isBlank()) {
                addSection(document, "Summary", sectionFont);
                document.add(new Paragraph(user.getProfessionalSummary(), textFont));
            }

            addSection(document, "Education", sectionFont);
            for (EducationDto education : resume.getEducations()) {
                document.add(new Paragraph(
                        education.getDegree() + " - " + education.getCollegeName() + " (" + education.getYear() + ")",
                        textFont));
                document.add(new Paragraph("Score: " + education.getCgpaOrPercentage(), textFont));
            }

            addSection(document, "Skills", sectionFont);
            document.add(new Paragraph(
                    String.join(", ", resume.getSkills().stream().map(SkillDto::getSkillName).toList()),
                    textFont));

            addSection(document, "Projects", sectionFont);
            for (ProjectDto project : resume.getProjects()) {
                document.add(new Paragraph(project.getProjectTitle(), textFont));
                document.add(new Paragraph(project.getDescription(), textFont));
                document.add(new Paragraph("Technologies: " + project.getTechnologiesUsed(), textFont));
            }

            addSection(document, "Experience", sectionFont);
            for (ExperienceDto experience : resume.getExperiences()) {
                document.add(new Paragraph(
                        experience.getRole() + " at " + experience.getCompanyName() + " (" + experience.getDuration() + ")",
                        textFont));
                document.add(new Paragraph(experience.getDescription(), textFont));
            }

            addSection(document, "Certifications", sectionFont);
            for (CertificationDto certification : resume.getCertifications()) {
                document.add(new Paragraph(
                        certification.getCertificationName() + " - " + certification.getOrganization() + " (" + certification.getYear() + ")",
                        textFont));
            }

            document.close();
            return outputStream.toByteArray();
        } catch (DocumentException exception) {
            throw new RuntimeException("Failed to generate PDF", exception);
        }
    }

    private void addSection(Document document, String title, Font sectionFont) throws DocumentException {
        document.add(new Paragraph(" "));
        document.add(new Paragraph(title, sectionFont));
    }

    private String defaultText(String value) {
        return value == null || value.isBlank() ? "N/A" : value;
    }
}
