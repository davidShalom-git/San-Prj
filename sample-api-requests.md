# Sample API Requests

## Run Backend With Neon PostgreSQL

Neon gives a PostgreSQL connection string. For Spring Boot, use the JDBC version:

```text
jdbc:postgresql://HOST/DB_NAME?sslmode=require
```

Set these environment variables before starting the backend:

```powershell
$env:SPRING_PROFILES_ACTIVE="neon"
$env:NEON_DATABASE_URL="jdbc:postgresql://HOST/DB_NAME?sslmode=require"
$env:NEON_DATABASE_USERNAME="YOUR_NEON_USERNAME"
$env:NEON_DATABASE_PASSWORD="YOUR_NEON_PASSWORD"
```

Then start:

```powershell
cd backend
mvn spring-boot:run
```

For local convenience, this project also supports an ignored file:

```powershell
.\run-neon.local.ps1
```

Use that only on your machine because it contains real database credentials.

## Create User

```http
POST http://localhost:8080/api/users
Content-Type: application/json

{
  "fullName": "Aarav Sharma",
  "email": "aarav.sharma@example.com",
  "phoneNumber": "9876543210",
  "address": "Bangalore, India",
  "linkedInUrl": "https://linkedin.com/in/aaravsharma",
  "githubUrl": "https://github.com/aaravsharma"
}
```

## Create Project

```http
POST http://localhost:8080/api/projects
Content-Type: application/json

{
  "projectTitle": "Resume Builder",
  "description": "A full stack application to create and download resumes.",
  "technologiesUsed": "React, Spring Boot, MySQL",
  "userId": 1
}
```

## Get Complete Resume JSON

```http
GET http://localhost:8080/api/resume/1
```

## Download Resume PDF

```http
GET http://localhost:8080/api/resume/1/pdf
```

## Generate AI Resume Suggestions with Gemini 2.5 Pro

The backend uses Vertex AI with a Google service account JSON. Set `GOOGLE_APPLICATION_CREDENTIALS`,
or place `gen-lang-client-0506905559-a9238ec231c0.json` in the project root.

```http
POST http://localhost:8080/api/ai/resume-tailor
Content-Type: application/json

{
  "prompt": "I have 7 years in hospitality and want a hotel operations manager resume.",
  "resume": {
    "fullName": "Aarav Sharma",
    "email": "aarav.sharma@example.com",
    "phoneNumber": "9876543210",
    "address": "Bangalore, India",
    "resumeTemplate": "modern"
  }
}
```
