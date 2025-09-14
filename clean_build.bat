@echo off
echo Cleaning build artifacts...

REM Kill Java processes
taskkill /f /im java.exe /t >nul 2>&1
taskkill /f /im gradle.exe /t >nul 2>&1

REM Wait a moment
timeout /t 2 /nobreak >nul

REM Remove build directories
if exist "app\build" rmdir /s /q "app\build"
if exist ".gradle" rmdir /s /q ".gradle"

echo Build cleanup complete.
