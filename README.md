# Resume Builder

Resume Builder is a beginner-friendly full stack mini-project with a separate React frontend and Spring Boot backend. It lets users create, edit, delete, preview, and download resumes as PDF files.

## Tech Stack

### Frontend

- React with Vite
- React Router
- Axios
- Tailwind CSS

### Backend

- Java 21
- Spring Boot 3
- Spring Data JPA
- MySQL
- Maven
- Lombok
- OpenPDF

## Project Architecture

```text
frontend (React)
    |
    | Axios REST API calls
    v
backend (Spring Boot)
    |
    | Spring Data JPA
    v
MySQL
```

## Folder Structure

```text
java-spr/
├── backend/
│   ├── pom.xml
│   ├── schema.sql
│   ├── resume-builder-postman-collection.json
│   └── src/
│       └── main/
│           ├── java/com/resumebuilder/
│           │   ├── config/
│           │   ├── controller/
│           │   ├── dto/
│           │   ├── entity/
│           │   ├── exception/
│           │   ├── repository/
│           │   ├── service/
│           │   └── ResumeBuilderApplication.java
│           └── resources/
│               └── application.properties
├── frontend/
│   ├── package.json
│   ├── tailwind.config.js
│   ├── postcss.config.js
│   ├── vite.config.js
│   ├── index.html
│   └── src/
│       ├── components/
│       ├── layouts/
│       ├── pages/
│       ├── services/
│       ├── App.jsx
│       ├── index.css
│       └── main.jsx
└── sample-api-requests.md
```

## Database Design

### Main Tables

- `users`
- `education`
- `skills`
- `projects`
- `experience`
- `certifications`

### Relationships

- One user can have many education records.
- One user can have many skills.
- One user can have many projects.
- One user can have many experience records.
- One user can have many certifications.

## Backend API Endpoints

### Users

- `POST /api/users`
- `GET /api/users`
- `GET /api/users/{id}`
- `PUT /api/users/{id}`
- `DELETE /api/users/{id}`

### Education

- `POST /api/education`
- `GET /api/education/{id}`
- `GET /api/education/user/{userId}`
- `PUT /api/education/{id}`
- `DELETE /api/education/{id}`

### Skills

- `POST /api/skills`
- `GET /api/skills/{id}`
- `GET /api/skills/user/{userId}`
- `PUT /api/skills/{id}`
- `DELETE /api/skills/{id}`

### Projects

- `POST /api/projects`
- `GET /api/projects/{id}`
- `GET /api/projects/user/{userId}`
- `PUT /api/projects/{id}`
- `DELETE /api/projects/{id}`

### Experience

- `POST /api/experience`
- `GET /api/experience/{id}`
- `GET /api/experience/user/{userId}`
- `PUT /api/experience/{id}`
- `DELETE /api/experience/{id}`

### Certifications

- `POST /api/certifications`
- `GET /api/certifications/{id}`
- `GET /api/certifications/user/{userId}`
- `PUT /api/certifications/{id}`
- `DELETE /api/certifications/{id}`

### Resume

- `GET /api/resume/{userId}`
- `GET /api/resume/{userId}/pdf`

## Step-by-Step Backend Setup

1. Install Java 21, Maven, and MySQL.
2. Create a database named `resume_builder_db`, or let Spring create it automatically.
3. Open [application.properties](/C:/Users/david/OneDrive/Desktop/java-spr/backend/src/main/resources/application.properties) and update:

```properties
spring.datasource.username=root
spring.datasource.password=your_mysql_password
```

4. Open a terminal in `backend`.
5. Run:

```bash
mvn clean install
mvn spring-boot:run
```

6. Backend will start at `http://localhost:8080`.

## Step-by-Step Frontend Setup

1. Open a terminal in `frontend`.
2. Install dependencies:

```bash
npm install
```

3. Start the Vite development server:

```bash
npm run dev
```

4. Frontend will start at `http://localhost:5173`.

## Frontend Pages

- Dashboard
- Resume Form
- Resume Preview
- PDF Download

## Frontend Flow

1. Create a resume from the form page.
2. Save the resume details to the backend through Axios service modules.
3. Preview the full resume.
4. Download the PDF from the preview page or dashboard.

## Axios Service Layer

Frontend API files:

- [api.js](/C:/Users/david/OneDrive/Desktop/java-spr/frontend/src/services/api.js)
- [userService.js](/C:/Users/david/OneDrive/Desktop/java-spr/frontend/src/services/userService.js)
- [resumeService.js](/C:/Users/david/OneDrive/Desktop/java-spr/frontend/src/services/resumeService.js)

## PDF Generation

- Implemented with OpenPDF in [ResumeServiceImpl.java](/C:/Users/david/OneDrive/Desktop/java-spr/backend/src/main/java/com/resumebuilder/service/impl/ResumeServiceImpl.java)
- API endpoint: `GET /api/resume/{userId}/pdf`

## Postman Collection

- Import [resume-builder-postman-collection.json](/C:/Users/david/OneDrive/Desktop/java-spr/backend/resume-builder-postman-collection.json) into Postman.

## Verification Notes

- Frontend production build was verified with `npm run build` on June 13, 2026.
- Maven is not installed in this environment, so backend compilation could not be executed here.
