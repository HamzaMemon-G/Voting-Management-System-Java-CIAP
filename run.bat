@echo off
title Vote Management System
echo Starting Vote Management System...
echo.

cd /d "%~dp0"

if not exist "bin" (
    echo Creating bin directory...
    mkdir bin
)

echo Compiling Java files...
javac -d bin votesystem/models/*.java votesystem/services/*.java votesystem/ui/*.java votesystem/*.java

if %errorlevel% neq 0 (
    echo Compilation failed! Please check for errors.
    pause
    exit /b 1
)

echo Compilation successful!
echo Starting application...
echo.
echo Default login: admin/admin
echo.

java -cp bin com.votesystem.VoteManagementSystem

pause