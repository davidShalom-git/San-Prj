# Copy this file to run-neon.local.ps1 and fill in your real Neon values.
# Do not commit run-neon.local.ps1.

Set-Location "$PSScriptRoot\backend"

$env:JAVA_HOME = "$PWD\jdk-21.0.2"
$env:PATH = "$env:JAVA_HOME\bin;$PWD\apache-maven-3.9.6\bin;$env:PATH"

$env:SPRING_PROFILES_ACTIVE = "neon"
$env:NEON_DATABASE_URL = "jdbc:postgresql://YOUR_NEON_HOST/YOUR_DATABASE?sslmode=require"
$env:NEON_DATABASE_USERNAME = "YOUR_NEON_USERNAME"
$env:NEON_DATABASE_PASSWORD = "YOUR_NEON_PASSWORD"

mvn spring-boot:run
