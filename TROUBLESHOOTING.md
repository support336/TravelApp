# Troubleshooting Guide

## ❌ Java Version Compatibility Error

**Error**: `Unsupported class file major version 65`

**Cause**: You're using Java 21 with Gradle 8.0, which doesn't support Java 21.

## ✅ Solutions

### Solution 1: Use Android Studio's Built-in Gradle (Recommended)

1. **In Android Studio**:
   - Go to `File` → `Settings` → `Build, Execution, Deployment` → `Build Tools` → `Gradle`
   - Select "Use Gradle from: 'gradle-wrapper.properties' file"
   - Click "Apply" and "OK"

2. **Clean and Rebuild**:
   - Go to `Build` → `Clean Project`
   - Wait for it to complete
   - Go to `Build` → `Rebuild Project`

### Solution 2: Use Java 17 (Alternative)

If you have Java 17 installed:

1. **Set JAVA_HOME to Java 17**:
   - Windows: Set environment variable `JAVA_HOME` to your Java 17 installation path
   - Example: `C:\Program Files\Java\jdk-17`

2. **Restart Android Studio** and try building again

### Solution 3: Use Android Studio's JDK

1. **In Android Studio**:
   - Go to `File` → `Project Structure` → `SDK Location`
   - Set "Gradle JDK" to "Embedded JDK" or a compatible version
   - Click "Apply" and "OK"

## 🔧 Manual Fixes Applied

I've already updated your project with:

1. **Gradle 8.4** (from 8.0) - supports Java 21
2. **Android Gradle Plugin 8.2.0** (from 8.1.2)
3. **Java Toolchain** configuration for Java 17
4. **gradle.properties** with optimal settings

## 🚀 Next Steps

1. **Open Android Studio**
2. **Import the project** (if not already open)
3. **Wait for Gradle sync** to complete
4. **Clean and rebuild** the project
5. **Run the app** on device/emulator

## 📱 Testing the App

Once the build succeeds:

1. **Agenda Tab**: Test Google Sign-In
2. **Map Tab**: Verify maps load and location works
3. **Points Tab**: Test POI management
4. **Settings Tab**: Check sync functionality

## 🆘 If Still Having Issues

1. **Check Java Version**:
   ```bash
   java -version
   ```

2. **Check Gradle Version**:
   ```bash
   .\gradlew.bat --version
   ```

3. **Invalidate Caches**:
   - In Android Studio: `File` → `Invalidate Caches and Restart`

4. **Delete .gradle folder**:
   - Close Android Studio
   - Delete `C:\Users\Root\.gradle` folder
   - Reopen Android Studio

The project is now configured for maximum compatibility! 🎉
