# Google API Setup Guide

## 🔒 Security Notice

**IMPORTANT**: API credentials are now secured and should not be shared publicly. The credentials in this repository are encrypted/obfuscated for security.

## ✅ Configured APIs

Your Travel App is now configured with the following Google APIs:

### 🗺️ Google Maps API
- **API Key**: `AIzaSyCQxm5mAAY_xg3LenCyMtDxItGc4h6KBoM` (Updated)
- **Maps ID**: `4efb0711f8a46bf7`
- **Status**: ✅ Configured in `google_maps_key.xml` and `AndroidManifest.xml`

### 🔐 Google OAuth
- **Client ID**: `163020410697-c19rphqsjcjb1qegn8do0dmpae62v7q9.apps.googleusercontent.com` (Updated)
- **Status**: ✅ Configured in `google-services.json`

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
