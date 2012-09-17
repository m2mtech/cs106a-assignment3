/*
 * File: LetterSubstitutionCipher.java
 * -----------------------------------
 * This program translates a line of text using a letter-substitution
 * cipher.
 */
import acm.program.*;

public class LetterSubstitutionCipher extends ConsoleProgram {

	public void run() {
		println("This program implements a letter-substitution cipher."); 
		String key = readKey();
		String plaintext = readLine("Plaintext: ");
		String ciphertext = encrypt(plaintext, key);
		String decryption = decrypt(ciphertext, key); 
		println("Ciphertext: " + ciphertext);
		println("Decryption: " + decryption);
	}

	/**
	 * Reads an encryption key from the user and checks it for
	 * legality (see isLegalKey below). If the user enters an
	 * illegal key, the user is asked to reenter a valid one. */
	private String readKey() {
		String key = readLine("Enter 26-letter key: "); 
		while (!isLegalKey(key)) {
			println("That key is not legal.");
			key = readLine("Reenter key: ");
		}
		return key; 
	}

	/**
	 * Checks to see whether a key is legal, which means it meets 
	 * the following conditions:
	 * (1) It is 26 characters long,
	 * (2) It contains only uppercase letters, and
	 * (3) It has no duplicated letters.
	 */
	private boolean isLegalKey(String key) { 
		if (key.length() != 26) return false; 
		for (int i = 0; i < 26; i++) {
			char ch = key.charAt(i);
			if (!Character.isUpperCase(ch)) return false; 
			for (int j = i + 1; j < 26; j++) {
				if (ch == key.charAt(j)) return false; 
			}
		}
		return true;
	}

	/**
	 * Encrypts a string according to the key. 
	 */
	private String encrypt(String str, String key) { 
		String result = "";
		for (int i = 0; i < str.length(); i++) {
			char ch = encryptCharacter(str.charAt(i), key);
			result += ch;
		}
		return result;
	}

	/**
	 * Encrypts a single character according to the key.
	 */
	private char encryptCharacter(char ch, String key) { 
		if (Character.isLetter(ch)) {
			ch = key.charAt(Character.toUpperCase(ch) - 'A'); }
		return ch; 
	}

	/**
	 * Decrypts a string according to the key. 
	 */
	private String decrypt(String str, String key) { 
		String result = "";
		for (int i = 0; i < str.length(); i++) {
			char ch = decryptCharacter(str.charAt(i), key);
			result += ch;
		}
		return result;
	}

	/**
	 * Decrypts a single character according to the key. 
	 */
	private char decryptCharacter(char ch, String key) {
		int index = key.indexOf(ch);
		if (index != -1) {
			ch = (char) ('A' + index);
		}
		return ch; 
	}

}