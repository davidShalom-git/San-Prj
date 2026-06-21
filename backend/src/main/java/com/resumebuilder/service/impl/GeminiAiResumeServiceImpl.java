package com.resumebuilder.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.resumebuilder.dto.AiResumeRequestDto;
import com.resumebuilder.dto.ResumeFormDataDto;
import com.resumebuilder.exception.AiServiceException;
import com.resumebuilder.service.AiResumeService;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class GeminiAiResumeServiceImpl implements AiResumeService {

    private static final List<String> VERTEX_SCOPES = List.of("https://www.googleapis.com/auth/cloud-platform");

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;
    private final Path credentialsPath;
    private final String configuredProjectId;
    private final String location;
    private final String model;
    private GoogleCredentials credentials;

    public GeminiAiResumeServiceImpl(
            ObjectMapper objectMapper,
            @Value("${vertex.ai.credentials-path}") String credentialsPath,
            @Value("${vertex.ai.project-id:}") String configuredProjectId,
            @Value("${vertex.ai.location}") String location,
            @Value("${vertex.ai.model}") String model) {
        this.objectMapper = objectMapper;
        this.credentialsPath = Path.of(credentialsPath).normalize();
        this.configuredProjectId = configuredProjectId;
        this.location = location;
        this.model = model;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    @Override
    public ResumeFormDataDto tailorResume(AiResumeRequestDto request) {
        try {
            String prompt = buildPrompt(request);
            Map<String, Object> payload = Map.of(
                    "contents", List.of(Map.of(
                            "role", "user",
                            "parts", List.of(Map.of("text", prompt)))),
                    "generationConfig", Map.of(
                            "temperature", 0.6,
                            "responseMimeType", "application/json"));

            GoogleCredentials loadedCredentials = getCredentials();
            String projectId = getProjectId(loadedCredentials);
            AccessToken token = loadedCredentials.refreshAccessToken();

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(buildVertexEndpoint(projectId)))
                    .timeout(Duration.ofSeconds(60))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getTokenValue())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(payload)))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new AiServiceException(buildGeminiErrorMessage(response.statusCode()));
            }

            String generatedText = extractGeneratedText(response.body());
            return objectMapper.readValue(cleanJson(generatedText), ResumeFormDataDto.class);
        } catch (AiServiceException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new AiServiceException("Failed to generate resume suggestions with Gemini.", exception);
        }
    }

    private String buildPrompt(AiResumeRequestDto request) throws Exception {
        ResumeFormDataDto resume = request.getResume() == null ? new ResumeFormDataDto() : request.getResume();
        return """
                You are an expert resume writer.

                The user can describe any profession, target role, experience level, industry, or career goal.
                Tailor the resume content to the user's exact prompt without assuming a software engineering role.
                Preserve accurate personal/contact information from the current resume.
                Improve empty or weak sections with realistic, editable draft content.
                Keep all fields concise and professional.
                Do not invent company names when the user did not provide them; use neutral placeholders like "Current Organization".
                Choose resumeTemplate as one of: modern, classic, compact.

                Return only valid JSON with exactly this shape:
                {
                  "fullName": "",
                  "email": "",
                  "phoneNumber": "",
                  "address": "",
                  "linkedInUrl": "",
                  "githubUrl": "",
                  "professionalSummary": "",
                  "resumeTemplate": "modern",
                  "educations": [{"degree": "", "collegeName": "", "year": "", "cgpaOrPercentage": ""}],
                  "skills": [{"skillName": ""}],
                  "projects": [{"projectTitle": "", "description": "", "technologiesUsed": ""}],
                  "experiences": [{"companyName": "", "role": "", "duration": "", "description": ""}],
                  "certifications": [{"certificationName": "", "organization": "", "year": ""}]
                }

                User prompt:
                %s

                Current resume JSON:
                %s
                """.formatted(request.getPrompt(), objectMapper.writeValueAsString(resume));
    }

    private GoogleCredentials getCredentials() throws IOException {
        if (credentials == null) {
            if (!credentialsPath.toFile().exists()) {
                throw new AiServiceException(
                        "Vertex AI service account JSON was not found at " + credentialsPath
                                + ". Set GOOGLE_APPLICATION_CREDENTIALS to the JSON file path.");
            }
            try (FileInputStream inputStream = new FileInputStream(credentialsPath.toFile())) {
                credentials = GoogleCredentials.fromStream(inputStream).createScoped(VERTEX_SCOPES);
            }
        }
        return credentials;
    }

    private String getProjectId(GoogleCredentials loadedCredentials) {
        if (configuredProjectId != null && !configuredProjectId.isBlank()) {
            return configuredProjectId;
        }
        if (loadedCredentials instanceof ServiceAccountCredentials serviceAccountCredentials
                && serviceAccountCredentials.getProjectId() != null
                && !serviceAccountCredentials.getProjectId().isBlank()) {
            return serviceAccountCredentials.getProjectId();
        }
        throw new AiServiceException("Vertex AI project id is missing. Set VERTEX_AI_PROJECT_ID.");
    }

    private String buildVertexEndpoint(String projectId) {
        return "https://" + location + "-aiplatform.googleapis.com/v1/projects/" + projectId
                + "/locations/" + location + "/publishers/google/models/" + model + ":generateContent";
    }

    private String extractGeneratedText(String responseBody) throws Exception {
        JsonNode root = objectMapper.readTree(responseBody);
        JsonNode parts = root.path("candidates").path(0).path("content").path("parts");
        if (!parts.isArray() || parts.isEmpty() || parts.path(0).path("text").isMissingNode()) {
            throw new AiServiceException("Gemini did not return resume content.");
        }
        return parts.path(0).path("text").asText();
    }

    private String buildGeminiErrorMessage(int statusCode) {
        if (statusCode == 429) {
            return "Vertex AI quota or rate limit reached. Check Gemini quota/billing or try again later.";
        }
        if (statusCode == 403) {
            return "Vertex AI access is denied. Check that the service account has Vertex AI permissions.";
        }
        if (statusCode == 404) {
            return "Vertex AI model " + model + " was not available in location " + location + ".";
        }
        return "Vertex AI request failed with status " + statusCode + ".";
    }

    private String cleanJson(String value) {
        String trimmed = value.trim();
        if (trimmed.startsWith("```")) {
            trimmed = trimmed.replaceFirst("^```(?:json)?\\s*", "");
            trimmed = trimmed.replaceFirst("\\s*```$", "");
        }
        return trimmed.trim();
    }
}
