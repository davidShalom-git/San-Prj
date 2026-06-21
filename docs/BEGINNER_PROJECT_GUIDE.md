# Resume Builder Project Guide

This document explains the Resume Builder project from the beginning. It is written for a student who is new to full stack development.

## 1. What This Project Does

This is a full stack resume builder application.

The user can:

- Enter resume details manually.
- Use an optional AI helper to generate resume content based on a role or experience.
- Choose a resume template.
- Preview the resume.
- Download the resume as a PDF.
- Edit saved resume data while the backend is running.

The project has two main parts:

- Frontend: React app, shown in the browser.
- Backend: Spring Boot app, handles APIs, database, AI, and PDF generation.

There is also a database:

- H2 in-memory database for development.
- It stores data only while backend is running.

## 2. Project Folder Structure

Main folder:

```text
java-spr/
```

Important files and folders:

```text
java-spr/
  backend/
  frontend/
  docs/
  run.bat
  README.md
  sample-api-requests.md
  gen-lang-client-0506905559-a9238ec231c0.json
```

Explanation:

- `backend/` contains the Spring Boot Java backend.
- `frontend/` contains the React frontend.
- `docs/` contains project explanation documents.
- `run.bat` starts backend and frontend together.
- `README.md` is the basic project readme.
- `sample-api-requests.md` shows sample API requests.
- `gen-lang-client-0506905559-a9238ec231c0.json` is the Google Vertex AI service account credential file. Do not commit this file publicly.

## 3. How To Start The Backend

Open PowerShell.

Go to the project folder:

```powershell
cd C:\Users\david\OneDrive\Desktop\java-spr
```

Go to the backend folder:

```powershell
cd backend
```

Set Java path:

```powershell
$env:JAVA_HOME="$PWD\jdk-21.0.2"
```

This tells PowerShell:

- Use the Java installed inside the backend folder.
- `$PWD` means current folder.
- So this points to `backend/jdk-21.0.2`.

Set Maven and Java in the command path:

```powershell
$env:PATH="$env:JAVA_HOME\bin;$PWD\apache-maven-3.9.6\bin;$env:PATH"
```

This tells PowerShell:

- Find Java commands from `jdk-21.0.2/bin`.
- Find Maven commands from `apache-maven-3.9.6/bin`.
- Keep the old system path also.

Check Maven:

```powershell
mvn -v
```

If Maven version is shown, Maven is working.

Start backend:

```powershell
mvn spring-boot:run
```

Wait until you see:

```text
Tomcat started on port 8080
Started ResumeBuilderApplication
```

Backend is now running at:

```text
http://localhost:8080
```

Important: keep this terminal open. If you close it, backend stops.

## 4. Common Backend Startup Error

If you see:

```text
mvn : The term 'mvn' is not recognized
```

It means PowerShell cannot find Maven.

Run this again from the `backend` folder:

```powershell
$env:JAVA_HOME="$PWD\jdk-21.0.2"
$env:PATH="$env:JAVA_HOME\bin;$PWD\apache-maven-3.9.6\bin;$env:PATH"
```

Then:

```powershell
mvn spring-boot:run
```

If you see:

```text
Port 8080 was already in use
```

It means another backend is already running on port `8080`.

Find the process:

```powershell
netstat -ano | findstr :8080
```

Stop it:

```powershell
Stop-Process -Id PROCESS_ID -Force
```

Replace `PROCESS_ID` with the number from the previous command.

## 5. How To Start The Frontend

Open a new PowerShell terminal.

Go to project folder:

```powershell
cd C:\Users\david\OneDrive\Desktop\java-spr
```

Go to frontend:

```powershell
cd frontend
```

Install dependencies if needed:

```powershell
npm install
```

Start frontend:

```powershell
npm run dev
```

Wait until you see a local URL like:

```text
http://localhost:5173
```

Open that in the browser.

Frontend is the user interface. Backend must also be running for save, preview, AI, and PDF features.

## 6. Simple Application Flow

The normal flow is:

```text
User opens frontend
  ↓
User fills resume form
  ↓
Frontend sends data to backend API
  ↓
Backend saves data in database
  ↓
User opens preview
  ↓
Backend fetches saved data
  ↓
Frontend displays resume preview
  ↓
User clicks download PDF
  ↓
Backend generates PDF
```

## 7. What Is Frontend?

Frontend means the part of the application the user sees.

In this project, frontend is built with:

- React
- Vite
- Tailwind CSS
- Axios
- React Router

Frontend folder:

```text
frontend/
```

Important frontend files:

```text
frontend/src/main.jsx
frontend/src/App.jsx
frontend/src/layouts/AppLayout.jsx
frontend/src/pages/DashboardPage.jsx
frontend/src/pages/ResumeFormPage.jsx
frontend/src/pages/ResumePreviewPage.jsx
frontend/src/pages/PdfDownloadPage.jsx
frontend/src/services/api.js
frontend/src/services/resumeService.js
frontend/src/services/userService.js
frontend/src/components/
```

## 8. Frontend Entry File

File:

```text
frontend/src/main.jsx
```

This is where the React app starts.

It connects the React application to the HTML page.

Think of it like:

```text
Browser opens index.html
  ↓
main.jsx starts React
  ↓
React shows App.jsx
```

## 9. Frontend Routes

File:

```text
frontend/src/App.jsx
```

This file defines which page should open for each URL.

Routes:

```text
/                         Dashboard page
/resume/new               Create new resume form
/resume/edit/:userId      Edit existing resume
/resume/preview/:userId   Preview resume
/resume/pdf/:userId       PDF download page
```

Example:

If browser URL is:

```text
http://localhost:5173/resume/new
```

React shows:

```text
ResumeFormPage.jsx
```

## 10. Frontend Layout

File:

```text
frontend/src/layouts/AppLayout.jsx
```

This file creates common layout for all pages.

It contains:

- Header
- Resume Builder title
- Dashboard link
- Create Resume link
- Main content area

`<Outlet />` means:

```text
Show the current page here.
```

So the layout stays the same, but page content changes.

## 11. Dashboard Page

File:

```text
frontend/src/pages/DashboardPage.jsx
```

This page shows saved resumes.

It can:

- Load all users/resumes.
- Show resume names.
- Open preview.
- Open edit page.
- Download PDF.
- Delete resume.

It calls backend APIs through service files.

## 12. Resume Form Page

File:

```text
frontend/src/pages/ResumeFormPage.jsx
```

This is the most important frontend page.

It handles:

- Create resume.
- Edit resume.
- Personal details.
- Optional AI helper.
- Resume template selection.
- Education.
- Skills.
- Projects.
- Experience.
- Certifications.
- Save button.

Important state:

```text
formData
```

This stores all values entered by the user.

Example:

```text
formData.fullName
formData.email
formData.educations
formData.skills
```

When user types in a field, React updates `formData`.

When user clicks Save Resume:

```text
handleSubmit()
```

is called.

That function:

1. Checks required fields.
2. Calls `saveResume()`.
3. Sends data to backend.
4. Redirects user to preview page.

## 13. Optional AI Helper

The AI helper is at the top of the form.

It is optional.

The user can:

- Skip AI and manually fill everything.
- Or type a prompt like:

```text
I have 7 years in hospitality and want a hotel operations manager resume.
```

Then click:

```text
Generate with Gemini 2.5 Pro
```

The frontend sends the prompt and current form data to backend.

Backend sends it to Google Vertex AI.

Vertex AI returns resume content.

Frontend fills the form with that generated content.

The user can still edit everything before saving.

## 14. Resume Template Selection

The form supports three templates:

```text
modern
classic
compact
```

The selected template is saved with the user.

It affects:

- Resume preview layout.
- PDF layout.

## 15. Resume Preview Page

File:

```text
frontend/src/pages/ResumePreviewPage.jsx
```

This page shows the final resume preview.

It fetches complete resume data from backend:

```text
GET /api/resume/{userId}
```

It displays:

- Name
- Contact details
- Summary
- Skills
- Experience
- Projects
- Education
- Certifications

It also has:

- Edit Resume button.
- Download PDF button.

## 16. PDF Download Page

File:

```text
frontend/src/pages/PdfDownloadPage.jsx
```

This page helps download a PDF.

The direct PDF URL is:

```text
http://localhost:8080/api/resume/{userId}/pdf
```

The backend creates the PDF and sends it to the browser.

## 17. Frontend Service Files

Service files are used to call backend APIs.

They keep API code separate from UI code.

### api.js

File:

```text
frontend/src/services/api.js
```

It creates one Axios object:

```text
baseURL: http://localhost:8080/api
```

This means frontend API calls go to backend.

Example:

```text
/users
```

becomes:

```text
http://localhost:8080/api/users
```

### userService.js

File:

```text
frontend/src/services/userService.js
```

Handles user APIs:

- Get all users.
- Get one user.
- Create user.
- Update user.
- Delete user.

### resumeService.js

File:

```text
frontend/src/services/resumeService.js
```

Handles resume APIs:

- Save complete resume.
- Fetch resume.
- Download PDF.
- Call AI helper.

Important function:

```text
saveResume()
```

It first saves personal user details.

Then it saves:

- Education
- Skills
- Projects
- Experience
- Certifications

## 18. What Is Backend?

Backend means the server-side part of the app.

Frontend only shows UI.

Backend does the actual work:

- Receives API requests.
- Validates data.
- Saves data to database.
- Fetches data from database.
- Calls Google Vertex AI.
- Generates PDF.
- Sends responses back to frontend.

Backend folder:

```text
backend/
```

Backend technology:

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- H2 Database
- OpenPDF
- Google Auth Library

## 19. Backend Entry File

File:

```text
backend/src/main/java/com/resumebuilder/ResumeBuilderApplication.java
```

This is the backend starting point.

Important line:

```java
SpringApplication.run(ResumeBuilderApplication.class, args);
```

Meaning:

```text
Start the Spring Boot application.
Start embedded Tomcat server.
Load controllers, services, repositories, and config.
```

## 20. Backend Configuration

File:

```text
backend/src/main/resources/application.properties
```

Important settings:

```properties
server.port=8080
```

Backend runs on port `8080`.

```properties
spring.datasource.url=jdbc:h2:mem:testdb
```

Use H2 in-memory database.

```properties
spring.jpa.hibernate.ddl-auto=update
```

Hibernate creates/updates tables automatically based on entity classes.

```properties
spring.h2.console.enabled=true
```

Enable H2 browser console.

```properties
vertex.ai.credentials-path=${GOOGLE_APPLICATION_CREDENTIALS:../gen-lang-client-0506905559-a9238ec231c0.json}
```

Use Google service account JSON for Vertex AI.

If `GOOGLE_APPLICATION_CREDENTIALS` is not set, it uses:

```text
../gen-lang-client-0506905559-a9238ec231c0.json
```

from the backend folder.

## 21. What Is Database?

Database means a place where application data is stored.

In this app, we store:

- User personal details.
- Education.
- Skills.
- Projects.
- Experience.
- Certifications.
- Professional summary.
- Selected resume template.

Without a database:

- User fills form.
- Page refreshes.
- Data disappears.

With a database:

- User fills form.
- Backend saves it.
- User can preview/edit/download later.

## 22. Current Database: H2

This project currently uses:

```text
H2 in-memory database
```

Meaning:

- It runs inside backend.
- No separate database installation is needed.
- It is good for learning and testing.
- Data disappears when backend restarts.

So yes, data is stored, but only temporarily.

For production, use a permanent database like:

- MySQL
- PostgreSQL
- MongoDB

This project now includes a Neon PostgreSQL profile for deployment.

## 22.1 Neon PostgreSQL Database

Neon is a cloud PostgreSQL database.

It is similar to MongoDB Atlas in one important way:

```text
You create a database online.
You copy the connection string.
You put that connection string in backend environment variables.
The deployed backend connects to the cloud database.
```

Neon gives a connection string like:

```text
postgresql://username:password@host.neon.tech/dbname?sslmode=require
```

Spring Boot uses JDBC format:

```text
jdbc:postgresql://host.neon.tech/dbname?sslmode=require
```

The project has this file:

```text
backend/src/main/resources/application-neon.properties
```

This file is used only when this profile is active:

```text
neon
```

To run backend with Neon, set:

```powershell
$env:SPRING_PROFILES_ACTIVE="neon"
$env:NEON_DATABASE_URL="jdbc:postgresql://HOST/DB_NAME?sslmode=require"
$env:NEON_DATABASE_USERNAME="YOUR_NEON_USERNAME"
$env:NEON_DATABASE_PASSWORD="YOUR_NEON_PASSWORD"
```

Then run backend:

```powershell
cd backend
mvn spring-boot:run
```

This project also includes:

```text
run-neon.example.ps1
```

You can copy it to:

```text
run-neon.local.ps1
```

and put real Neon credentials there. The `.local.ps1` file is ignored by Git because it contains secrets.

When the `neon` profile is active:

- H2 is not used.
- H2 console is disabled.
- PostgreSQL driver is used.
- Tables are created/updated in Neon by Hibernate.
- Data stays even after backend restarts.

For deployment, set the same environment variables in Render, Railway, or whichever backend hosting provider you use.

## 23. How To Open H2 Database Console

Backend must be running.

Open:

```text
http://localhost:8080/h2-console
```

Use:

```text
JDBC URL: jdbc:h2:mem:testdb
User Name: sa
Password: password
```

Then click Connect.

You can see tables like:

```text
USERS
EDUCATION
SKILLS
PROJECTS
EXPERIENCE
CERTIFICATIONS
```

## 24. Database Tables

The database contains tables that match resume sections.

### users

Stores personal details:

```text
id
full_name
email
phone_number
address
linked_in_url
github_url
professional_summary
resume_template
```

### education

Stores education details:

```text
id
degree
college_name
graduation_year
cgpa_or_percentage
user_id
```

`user_id` connects education to a user.

### skills

Stores skills:

```text
id
skill_name
user_id
```

### projects

Stores projects:

```text
id
project_title
description
technologies_used
user_id
```

### experience

Stores experience:

```text
id
company_name
role
duration
description
user_id
```

### certifications

Stores certifications:

```text
id
certification_name
organization
certification_year
user_id
```

## 25. Entity Classes

Entity classes represent database tables in Java.

Folder:

```text
backend/src/main/java/com/resumebuilder/entity/
```

Important entity files:

```text
User.java
Education.java
Skill.java
Project.java
Experience.java
Certification.java
```

Example:

`User.java` represents the `users` table.

`Education.java` represents the `education` table.

Annotations:

```java
@Entity
```

Means this class is a database table.

```java
@Table(name = "users")
```

Means use table name `users`.

```java
@Id
```

Means this field is the primary key.

```java
@GeneratedValue
```

Means the database automatically creates the ID.

## 26. Repository Classes

Repository classes talk to the database.

Folder:

```text
backend/src/main/java/com/resumebuilder/repository/
```

Files:

```text
UserRepository.java
EducationRepository.java
SkillRepository.java
ProjectRepository.java
ExperienceRepository.java
CertificationRepository.java
```

They usually extend:

```java
JpaRepository<EntityName, Long>
```

This gives ready-made database functions:

- save
- findById
- findAll
- delete

So we do not need to write SQL manually for basic operations.

## 27. DTO Classes

DTO means Data Transfer Object.

Folder:

```text
backend/src/main/java/com/resumebuilder/dto/
```

DTOs are used for API request and response data.

Why not directly send Entity classes?

Because DTOs are safer and cleaner.

Example:

```text
UserDto.java
EducationDto.java
ResumeDto.java
ResumeFormDataDto.java
```

`ResumeDto` combines all resume sections:

- User
- Education
- Skills
- Projects
- Experience
- Certifications

## 28. Service Classes

Service classes contain business logic.

Folder:

```text
backend/src/main/java/com/resumebuilder/service/
backend/src/main/java/com/resumebuilder/service/impl/
```

Example:

```text
UserService.java
UserServiceImpl.java
```

The interface says what the service can do.

The implementation says how it does it.

Example:

```text
UserService
```

declares:

- createUser
- getUserById
- updateUser
- deleteUser

```text
UserServiceImpl
```

uses `UserRepository` to actually save and fetch data.

## 29. Controller Classes

Controller classes define API endpoints.

Folder:

```text
backend/src/main/java/com/resumebuilder/controller/
```

Controllers:

```text
UserController.java
EducationController.java
SkillController.java
ProjectController.java
ExperienceController.java
CertificationController.java
ResumeController.java
AiResumeController.java
```

Example:

`UserController.java` handles:

```text
POST /api/users
GET /api/users
GET /api/users/{id}
PUT /api/users/{id}
DELETE /api/users/{id}
```

Controller flow:

```text
Frontend request
  ↓
Controller receives request
  ↓
Controller calls Service
  ↓
Service calls Repository
  ↓
Repository talks to DB
  ↓
Response goes back to frontend
```

## 30. API Endpoints

Base backend URL:

```text
http://localhost:8080/api
```

Important APIs:

```text
GET    /api/users
POST   /api/users
GET    /api/users/{id}
PUT    /api/users/{id}
DELETE /api/users/{id}
```

Education:

```text
POST   /api/education
GET    /api/education/user/{userId}
DELETE /api/education/{id}
```

Complete resume:

```text
GET /api/resume/{userId}
```

Download PDF:

```text
GET /api/resume/{userId}/pdf
```

AI:

```text
POST /api/ai/resume-tailor
```

## 31. PDF Generation

File:

```text
backend/src/main/java/com/resumebuilder/service/impl/ResumeServiceImpl.java
```

This file generates PDF using OpenPDF.

When user clicks Download PDF:

```text
Frontend opens /api/resume/{userId}/pdf
```

Backend:

1. Fetches resume data.
2. Creates PDF document.
3. Adds user details and sections.
4. Returns PDF bytes.

Browser downloads:

```text
resume-{userId}.pdf
```

## 32. AI With Google Vertex AI

File:

```text
backend/src/main/java/com/resumebuilder/service/impl/GeminiAiResumeServiceImpl.java
```

This service:

1. Reads the service account JSON.
2. Gets an OAuth access token.
3. Sends prompt to Vertex AI Gemini model.
4. Receives generated JSON.
5. Converts JSON into resume form data.
6. Sends it back to frontend.

Current model:

```text
gemini-2.5-pro
```

Current location:

```text
us-central1
```

Current project:

```text
gen-lang-client-0506905559
```

Credential file:

```text
gen-lang-client-0506905559-a9238ec231c0.json
```

Important: never upload this credential file to GitHub.

## 33. CORS Configuration

File:

```text
backend/src/main/java/com/resumebuilder/config/CorsConfig.java
```

CORS allows frontend and backend to communicate.

Frontend runs on:

```text
http://localhost:5173
```

Backend runs on:

```text
http://localhost:8080
```

Because they use different ports, browser may block requests unless CORS is allowed.

This config fixes that.

## 34. Exception Handling

Folder:

```text
backend/src/main/java/com/resumebuilder/exception/
```

Important file:

```text
GlobalExceptionHandler.java
```

This catches errors and sends clean messages to frontend.

Example:

If user is not found:

```text
User not found with id: 5
```

If AI fails:

```text
Vertex AI access is denied.
```

## 35. Manual Resume Save Flow

When user clicks Save Resume:

Frontend:

```text
ResumeFormPage.jsx
  ↓
saveResume()
  ↓
resumeService.js
```

Backend:

```text
UserController
  ↓
UserServiceImpl
  ↓
UserRepository
  ↓
users table
```

Then each section is saved:

```text
EducationController → education table
SkillController → skills table
ProjectController → projects table
ExperienceController → experience table
CertificationController → certifications table
```

Finally frontend opens:

```text
/resume/preview/{userId}
```

## 36. AI Resume Flow

When user clicks Generate with Gemini:

```text
ResumeFormPage.jsx
  ↓
tailorResumeWithAi()
  ↓
POST /api/ai/resume-tailor
  ↓
AiResumeController
  ↓
GeminiAiResumeServiceImpl
  ↓
Google Vertex AI
  ↓
Generated resume JSON
  ↓
Frontend fills form fields
```

The user still needs to click Save Resume after reviewing.

AI does not automatically save to database.

## 37. Why AI Is Optional

AI is optional because:

- Some users want to write their resume manually.
- Some users only want help with wording.
- AI-generated content should always be reviewed.
- The user should control final resume details.

So the app supports both:

```text
Manual mode
AI-assisted mode
```

## 38. What Happens When Backend Restarts

Because H2 is in-memory:

```text
Backend restart = database cleared
```

So saved resumes disappear after restart.

This is normal for development.

For real use, switch to MySQL or PostgreSQL.

## 39. How To Explain This In Presentation

You can say:

This project is a full stack Resume Builder application. The frontend is built with React and handles the user interface. The backend is built with Spring Boot and provides REST APIs for saving resume data, fetching previews, generating PDFs, and connecting to Google Vertex AI. The database stores resume details like personal information, education, skills, projects, experience, certifications, summary, and selected template. Currently, we use H2 in-memory database for development, so data is stored only while the backend is running. For production, we can replace H2 with MySQL or PostgreSQL for permanent storage.

## 40. One-Line Explanation

```text
Frontend collects resume data, backend processes and stores it, database keeps the data, AI helps generate content, and backend creates the final PDF.
```

## 41. Best Order To Learn This Project

If you are a beginner, learn in this order:

1. Run frontend.
2. Run backend.
3. Open resume form.
4. Save one resume.
5. Check H2 database.
6. Open preview page.
7. Download PDF.
8. Test AI helper.
9. Read frontend services.
10. Read backend controllers.
11. Read backend services.
12. Read backend entities and repositories.

## 42. Quick Checklist

Before testing:

- Backend running on `8080`.
- Frontend running on `5173`.
- H2 console available.
- Vertex credential JSON exists in project root.
- Browser opens `http://localhost:5173`.

If something fails:

- Check backend terminal.
- Check browser console.
- Check if port `8080` is already used.
- Check if credential JSON exists.
- Check if form required fields are filled.
