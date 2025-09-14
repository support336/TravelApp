# 🔒 Secure Credential Workflow

## ⚠️ CRITICAL: Never commit real credentials to GitHub!

This repository uses encrypted credentials to maintain security. Follow this workflow:

## 🚀 Development Workflow

### 1. **Start Development** (Get Real Credentials)
```bash
# Decrypt credentials for development
python decrypt_credentials.py

# Now you can build and test the app
./gradlew assembleDebug
```

### 2. **Before Committing** (Encrypt Credentials)
```bash
# Encrypt credentials before committing
python encrypt_credentials.py

# Commit the encrypted/placeholder files
git add .
git commit -m "Your changes"
git push
```

### 3. **Emergency: If Credentials Are Exposed**
```bash
# Immediately rotate credentials in Google Cloud Console
# Then update the encrypted files and force push
git push --force-with-lease origin master
```

## 📁 File Structure

- `google_maps_key.xml` - Contains placeholder `ENCRYPTED_API_KEY`
- `google-services.json` - Contains placeholder `ENCRYPTED_CLIENT_ID`
- `*.encrypted` - Real encrypted credentials (NEVER commit these)
- `encrypt_credentials.py` - Script to encrypt before committing
- `decrypt_credentials.py` - Script to decrypt for development

## 🔐 Security Features

1. **Encryption**: Uses Fernet (AES 128) with PBKDF2 key derivation
2. **Placeholders**: Real credentials replaced with placeholders in repo
3. **Gitignore**: Encrypted files excluded from version control
4. **Workflow**: Clear process for development vs. committing

## 🚨 Security Checklist

Before every commit, verify:
- [ ] No real API keys in `google_maps_key.xml`
- [ ] No real Client IDs in `google-services.json`
- [ ] All credentials show as `ENCRYPTED_*` placeholders
- [ ] No `.encrypted` files in git status
- [ ] Ran `python encrypt_credentials.py` before committing

## 📱 Testing

1. **Decrypt credentials**: `python decrypt_credentials.py`
2. **Build app**: `./gradlew assembleDebug`
3. **Test functionality**: Install and test
4. **Encrypt before commit**: `python encrypt_credentials.py`

## 🆘 Troubleshooting

### "Maps not loading"
- Run `python decrypt_credentials.py` to restore real credentials
- Verify API key is correct in Google Cloud Console

### "Sign-in not working"
- Run `python decrypt_credentials.py` to restore real credentials
- Verify Client ID is correct in Google Cloud Console

### "Build errors"
- Make sure you've decrypted credentials for development
- Check that all placeholder values are replaced

---

**Remember**: The repository should NEVER contain real credentials in plain text!
