#!/usr/bin/env python3
"""
Secure Credential Setup Script
This script helps developers configure their own API credentials securely
"""

import os
import re

def setup_credentials():
    """Interactive setup for API credentials"""
    
    print("🔒 Travel App - Secure Credential Setup")
    print("=" * 50)
    print()
    print("⚠️  IMPORTANT: Never commit real credentials to GitHub!")
    print("This script will help you configure your API credentials securely.")
    print()
    
    # Get credentials from user
    api_key = input("Enter your Google Maps API Key: ").strip()
    client_id = input("Enter your Google OAuth Client ID: ").strip()
    maps_id = input("Enter your Google Maps ID (optional): ").strip()
    
    if not api_key or not client_id:
        print("❌ Error: API Key and Client ID are required!")
        return
    
    print("\n🔧 Configuring credentials...")
    
    # Update google_maps_key.xml
    update_google_maps_key(api_key)
    
    # Update google-services.json
    update_google_services(client_id, api_key)
    
    # Update documentation
    update_documentation(api_key, client_id, maps_id)
    
    print("\n✅ Credentials configured successfully!")
    print("📱 You can now build and test the app:")
    print("   ./gradlew assembleDebug")
    print()
    print("⚠️  Remember to encrypt before committing:")
    print("   python encrypt_credentials.py")

def update_google_maps_key(api_key):
    """Update google_maps_key.xml with real API key"""
    file_path = "app/src/main/res/values/google_maps_key.xml"
    
    content = f'''<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- Google Maps API key -->
    <string name="google_maps_key">{api_key}</string>
</resources>'''
    
    with open(file_path, 'w') as f:
        f.write(content)
    
    print(f"✅ Updated {file_path}")

def update_google_services(client_id, api_key):
    """Update google-services.json with real credentials"""
    file_path = "app/google-services.json"
    
    # Read current file
    with open(file_path, 'r') as f:
        content = f.read()
    
    # Replace placeholders
    content = content.replace("ENCRYPTED_CLIENT_ID", client_id)
    content = content.replace("ENCRYPTED_API_KEY", api_key)
    
    # Write updated file
    with open(file_path, 'w') as f:
        f.write(content)
    
    print(f"✅ Updated {file_path}")

def update_documentation(api_key, client_id, maps_id):
    """Update documentation with configured status"""
    # Update GOOGLE_API_SETUP.md
    setup_content = f'''# Google API Setup Guide

## 🔒 Security Notice

**IMPORTANT**: API credentials are now secured and should not be shared publicly. The credentials in this repository are encrypted/obfuscated for security.

## ✅ Configured APIs

Your Travel App is now configured with the following Google APIs:

### 🗺️ Google Maps API
- **API Key**: `{api_key[:10]}...` (Configured)
- **Maps ID**: `{maps_id or 'Not configured'}`
- **Status**: ✅ Securely configured

### 🔐 Google OAuth
- **Client ID**: `{client_id[:20]}...` (Configured)
- **Status**: ✅ Securely configured

## 🔧 Required API Enabling

To make the app fully functional, you need to enable these APIs in your Google Cloud Console:

### 1. Google Maps SDK for Android
- **Status**: ✅ Already enabled (you have the API key)
- **Purpose**: Interactive maps and location services

### 2. Google Calendar API
- **Status**: ⚠️ Needs to be enabled
- **Purpose**: Sync travel events from Google Calendar
- **Steps**:
  1. Go to [Google Cloud Console](https://console.cloud.google.com/)
  2. Select your project
  3. Navigate to "APIs & Services" > "Library"
  4. Search for "Google Calendar API"
  5. Click "Enable"

### 3. Gmail API
- **Status**: ⚠️ Needs to be enabled
- **Purpose**: Parse travel confirmation emails
- **Steps**:
  1. Go to [Google Cloud Console](https://console.cloud.google.com/)
  2. Select your project
  3. Navigate to "APIs & Services" > "Library"
  4. Search for "Gmail API"
  5. Click "Enable"

### 4. Google Drive API (Optional)
- **Status**: ⚠️ Optional
- **Purpose**: Access to Google Drive files (if needed for attachments)
- **Steps**: Same as above, search for "Google Drive API"

## 🔑 OAuth Consent Screen Setup

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Navigate to "APIs & Services" > "OAuth consent screen"
3. Choose "External" user type
4. Fill in the required information:
   - **App name**: Travel App
   - **User support email**: Your email
   - **Developer contact information**: Your email
5. Add scopes:
   - `../auth/calendar.readonly`
   - `../auth/gmail.readonly`
6. Add test users (your email address)

## 📱 App Permissions

The app will request these permissions at runtime:
- **Location**: For current location on map
- **Calendar**: For reading travel events
- **Internet**: For API calls

## 🚀 Testing the App

1. **Build and Install**:
   ```bash
   ./gradlew assembleDebug
   ```

2. **Test Google Sign-In**:
   - Open the app
   - Go to Agenda tab
   - Tap "Sign in with Google"
   - Complete OAuth flow

3. **Test Maps**:
   - Go to Map tab
   - Verify map loads with your location
   - Test POI selection and saving

4. **Test Calendar Sync**:
   - After signing in, go to Settings
   - Tap "Sync Now"
   - Check Agenda tab for synced events

## 🔍 Troubleshooting

### Maps Not Loading
- Verify API key is correct
- Check that Maps SDK for Android is enabled
- Ensure package name matches in Google Cloud Console

### Sign-In Issues
- Verify OAuth Client ID is correct
- Check OAuth consent screen is configured
- Ensure package name and SHA-1 fingerprint match

### Calendar/Gmail Sync Issues
- Verify APIs are enabled
- Check OAuth scopes are granted
- Ensure test user is added to OAuth consent screen

## 📋 Next Steps

1. Enable the required APIs in Google Cloud Console
2. Set up OAuth consent screen
3. Build and test the app
4. Add your email as a test user
5. Test all features end-to-end

Your Travel App is now ready to use with Google services! 🎉
'''
    
    with open("GOOGLE_API_SETUP.md", 'w') as f:
        f.write(setup_content)
    
    print("✅ Updated documentation")

if __name__ == "__main__":
    setup_credentials()
