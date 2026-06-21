@echo off
echo ==============================================
echo        Starting Resume Builder Project
echo ==============================================

if "%GOOGLE_APPLICATION_CREDENTIALS%"=="" (
    echo Note: GOOGLE_APPLICATION_CREDENTIALS is not set.
    echo The backend will use ..\gen-lang-client-0506905559-a9238ec231c0.json for Vertex AI if it exists.
    echo.
)

echo Database mode:
echo - Default uses H2 in-memory database.
echo - To use Neon, set SPRING_PROFILES_ACTIVE=neon, NEON_DATABASE_URL, NEON_DATABASE_USERNAME, and NEON_DATABASE_PASSWORD before running this file.
echo.

echo [1/2] Starting Spring Boot Backend...
cd backend
set JAVA_HOME=%CD%\jdk-21.0.2
set PATH=%JAVA_HOME%\bin;%PATH%
start cmd /k "title Backend && .\apache-maven-3.9.6\bin\mvn spring-boot:run"

echo [2/2] Starting React Frontend...
cd ..\frontend
start cmd /k "title Frontend && npm install && npm run dev"

echo.
echo Both services are starting up!
echo Backend will be available at:  http://localhost:8080
echo Frontend will be available at: http://localhost:5173
echo.
echo Note: If this is the first time running, Maven and NPM will download dependencies.
echo Note: The backend uses an in-memory H2 database, so data will reset on restart.
pause
