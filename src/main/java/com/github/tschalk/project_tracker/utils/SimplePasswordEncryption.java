package com.github.tschalk.project_tracker.utils;

/**
 * Diese Klasse inst ein Placeholder für eine Verschlüsselungsmethode mit Hash-Algorithmus (wie bcrypt oder Argon2).
 * Sie ist nicht sicher und sollte nicht in einem Produktivsystem verwendet werden.
 */

public class SimplePasswordEncryption {

    public static String encrypt(String password) {
        String encryptedPassword = "";
        for (int i = 0; i < password.length(); i++) {
            char encryptedChar = (char) (password.charAt(i) + 1);
            encryptedPassword += encryptedChar;
        }
        return encryptedPassword;
    }

    public static String decrypt(String encryptedPassword) {
        String decryptedPassword = "";
        for (int i = 0; i < encryptedPassword.length(); i++) {
            char decryptedChar = (char) (encryptedPassword.charAt(i) - 1);
            decryptedPassword += decryptedChar;
        }
        return decryptedPassword;
    }
}