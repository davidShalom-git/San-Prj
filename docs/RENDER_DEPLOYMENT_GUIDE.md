# Deploy Spring Boot Backend On Render

This guide deploys the backend only.

Recommended production setup:

```text
Frontend: Vercel
Backend: Render
Database: Neon PostgreSQL
AI: Google Vertex AI service account
```

## 1. Prepare GitHub

Push this project to GitHub.

Render will read the code from GitHub and deploy it.

Do not push secret files:

```text
run-neon.local.ps1
gen-lang-client-*.json
.env
```

These are ignored by `.gitignore`.

## 2. Create Render Web Service

1. Open Render dashboard.
2. Click `New`.
3. Select `Web Service`.
4. Connect your GitHub repository.
5. Select this project repository.

## 3. Render Service Settings

Use these values:

```text
Name: resume-builder-backend
Runtime: Docker
Root Directory: leave empty
Build Command: leave empty
Start Command: leave empty
```

The backend reads Render's `PORT` automatically through:

```properties
server.port=${PORT:8080}
```

Render will use the root [Dockerfile](/C:/Users/david/OneDrive/Desktop/java-spr/Dockerfile) to build and run the Spring Boot backend.

## 4. Add Neon Environment Variables

In Render service:

1. Go to `Environment`.
2. Add these variables:

```env
SPRING_PROFILES_ACTIVE=neon
NEON_DATABASE_URL=jdbc:postgresql://YOUR_NEON_HOST/YOUR_DATABASE?sslmode=require
NEON_DATABASE_USERNAME=YOUR_NEON_USERNAME
NEON_DATABASE_PASSWORD=YOUR_NEON_PASSWORD
```

Do not write the password in GitHub code.

## 5. Add Vertex AI Credentials

The backend needs the Google service account JSON file for Vertex AI.

In Render:

1. Go to `Environment`.
2. Add a `Secret File`.
3. Filename:

```text
gen-lang-client-0506905559-a9238ec231c0.json
```

4. Paste the full JSON content.
5. Add this environment variable:

```env
GOOGLE_APPLICATION_CREDENTIALS=/etc/secrets/gen-lang-client-0506905559-a9238ec231c0.json
```

If Render shows a different path for secret files, use that path.

## 6. Deploy

Click:

```text
Deploy Web Service
```

Wait until logs show:

```text
The following 1 profile is active: "neon"
Tomcat started on port ...
Started ResumeBuilderApplication
```

## 7. Test Backend

After deployment, Render gives a URL like:

```text
https://resume-builder-backend.onrender.com
```

Test:

```text
https://resume-builder-backend.onrender.com/api/users
```

If it returns `[]`, backend is working.

## 8. Connect Frontend

After backend is deployed, update frontend API base URL.

Local frontend currently uses:

```text
http://localhost:8080/api
```

For deployed frontend, use:

```text
https://YOUR_RENDER_BACKEND_URL/api
```
