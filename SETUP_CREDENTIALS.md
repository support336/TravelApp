# 🔒 Secure Credential Setup

## ⚠️ IMPORTANT SECURITY NOTICE

This repository does NOT contain real API credentials for security reasons. You must configure your own credentials before running the app.

## 🚀 Quick Setup

### 1. Get Your API Credentials

1. **Google Maps API Key**:
   - Go to [Google Cloud Console](https://console.cloud.google.com/)
   - Create a new project or select existing
   - Enable "Maps SDK for Android"
   - Create credentials > API Key
   - Restrict the key to your package name

2. **Google OAuth Client ID**:
   - In the same Google Cloud Console project
   - Go to "APIs & Services" > "Credentials"
   - Create OAuth 2.0 Client ID
   - Choose "Android" application type
   - Add your package name and SHA-1 fingerprint

### 2. Configure the App

Replace the placeholder values in these files:

**File: `app/src/main/res/values/google_maps_key.xml`**
```xml
<string name="google_maps_key">YOUR_ACTUAL_API_KEY_HERE</string>
```

**File: `app/google-services.json`**
Replace all instances of `ENCRYPTED_CLIENT_ID_PLACEHOLDER` with your actual Client ID.

### 3. Initialize Credentials (Optional)

The app includes a `CredentialManager` for additional security. To use it:

1. Update the credentials in `CredentialManager.kt`
2. Call `CredentialManager.initializeCredentials(context)` in your Application class

## 🔐 Security Best Practices

### For Development:
- Use test/development API keys
- Restrict keys to specific package names
- Use IP restrictions if possible
- Never commit real credentials to version control

### For Production:
- Use Android Keystore for key storage
- Implement proper key rotation
- Use environment variables
- Monitor API usage and set up alerts
- Regularly audit and rotate credentials

## 🚨 If Credentials Are Exposed

1. **Immediately rotate the exposed credentials** in Google Cloud Console
2. **Update the app** with new credentials
3. **Review access logs** for any unauthorized usage
4. **Consider implementing additional security measures**

## 📱 Testing

After configuring credentials:

1. Build the app: `./gradlew assembleDebug`
2. Install on device/emulator
3. Test Google Maps functionality
4. Test Google Sign-In (if implemented)

## 🆘 Troubleshooting

### Maps Not Loading
- Verify API key is correct and enabled
- Check package name matches in Google Cloud Console
- Ensure Maps SDK for Android is enabled

### Sign-In Issues
- Verify OAuth Client ID is correct
- Check package name and SHA-1 fingerprint match
- Ensure OAuth consent screen is configured

## 📞 Support

If you need help with credential setup, check the [Google Cloud Console documentation](https://cloud.google.com/docs) or contact the development team.

---

**Remember**: Never share your API credentials publicly or commit them to version control!
