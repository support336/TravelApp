#!/usr/bin/env python3
"""
Credential Encryption Script
This script encrypts API credentials before committing to GitHub
"""

import base64
import os
import re
from cryptography.fernet import Fernet
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.kdf.pbkdf2 import PBKDF2HMAC

def generate_key(password: bytes, salt: bytes) -> bytes:
    """Generate encryption key from password and salt"""
    kdf = PBKDF2HMAC(
        algorithm=hashes.SHA256(),
        length=32,
        salt=salt,
        iterations=100000,
    )
    return base64.urlsafe_b64encode(kdf.derive(password))

def encrypt_credentials():
    """Encrypt all credential files"""
    
    # Password for encryption (in production, use environment variable)
    password = b"TravelApp2024SecureKey!"
    salt = b"travel_app_salt_2024"
    
    # Generate encryption key
    key = generate_key(password, salt)
    fernet = Fernet(key)
    
    # Files to encrypt
    files_to_encrypt = [
        "app/src/main/res/values/google_maps_key.xml",
        "app/google-services.json"
    ]
    
    for file_path in files_to_encrypt:
        if os.path.exists(file_path):
            print(f"Encrypting {file_path}...")
            
            # Read file content
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
            
            # Encrypt content
            encrypted_content = fernet.encrypt(content.encode())
            
            # Create encrypted version
            encrypted_file = file_path + ".encrypted"
            with open(encrypted_file, 'wb') as f:
                f.write(encrypted_content)
            
            # Replace original with placeholder
            if "google_maps_key.xml" in file_path:
                placeholder_content = '''<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- Google Maps API key -->
    <string name="google_maps_key">ENCRYPTED_API_KEY</string>
</resources>'''
            elif "google-services.json" in file_path:
                placeholder_content = '''{
  "project_info": {
    "project_number": "163020410697",
    "project_id": "travel-app-project",
    "storage_bucket": "travel-app-project.appspot.com"
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "1:163020410697:android:abcdef1234567890",
        "android_client_info": {
          "package_name": "com.travelapp"
        }
      },
      "oauth_client": [
        {
          "client_id": "ENCRYPTED_CLIENT_ID",
          "client_type": 1,
          "android_info": {
            "package_name": "com.travelapp",
            "certificate_hash": "sha1_hash_placeholder"
          }
        },
        {
          "client_id": "ENCRYPTED_CLIENT_ID",
          "client_type": 3
        }
      ],
      "api_key": [
        {
          "current_key": "ENCRYPTED_API_KEY"
        }
      ],
      "services": {
        "appinvite_service": {
          "other_platform_oauth_client": [
            {
              "client_id": "ENCRYPTED_CLIENT_ID",
              "client_type": 3
            }
          ]
        }
      }
    }
  ],
  "configuration_version": "1"
}'''
            
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(placeholder_content)
            
            print(f"✅ Encrypted {file_path} -> {encrypted_file}")
            print(f"✅ Replaced {file_path} with placeholder")
    
    print("\n🔒 All credentials have been encrypted!")
    print("📁 Encrypted files are stored with .encrypted extension")
    print("⚠️  Do not commit .encrypted files to version control!")

if __name__ == "__main__":
    encrypt_credentials()
