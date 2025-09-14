#!/usr/bin/env python3
"""
Credential Decryption Script
This script decrypts API credentials for development use
"""

import base64
import os
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

def decrypt_credentials():
    """Decrypt all credential files"""
    
    # Password for decryption (must match encryption password)
    password = b"TravelApp2024SecureKey!"
    salt = b"travel_app_salt_2024"
    
    # Generate decryption key
    key = generate_key(password, salt)
    fernet = Fernet(key)
    
    # Files to decrypt
    files_to_decrypt = [
        "app/src/main/res/values/google_maps_key.xml.encrypted",
        "app/google-services.json.encrypted"
    ]
    
    for encrypted_file in files_to_decrypt:
        if os.path.exists(encrypted_file):
            print(f"Decrypting {encrypted_file}...")
            
            # Read encrypted content
            with open(encrypted_file, 'rb') as f:
                encrypted_content = f.read()
            
            # Decrypt content
            decrypted_content = fernet.decrypt(encrypted_content).decode()
            
            # Write decrypted content to original file
            original_file = encrypted_file.replace('.encrypted', '')
            with open(original_file, 'w', encoding='utf-8') as f:
                f.write(decrypted_content)
            
            print(f"✅ Decrypted {encrypted_file} -> {original_file}")
        else:
            print(f"⚠️  Encrypted file not found: {encrypted_file}")
    
    print("\n🔓 All credentials have been decrypted!")
    print("📁 Original files restored with real credentials")
    print("⚠️  Remember to encrypt before committing to GitHub!")

if __name__ == "__main__":
    decrypt_credentials()
