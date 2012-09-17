/*
 * File: CaesarCipher.java
 * -----------------------
 * This program translates a line of text into its Caesar cipher * form.
 */
import acm.program.*;

public class CaesarCipher extends ConsoleProgram {

	public void run() {
		println("This program uses a Caesar cipher for encryption."); 
		int key = readInt("Enter encryption key: ");
		String plaintext = readLine("Plaintext: ");
		String ciphertext = encryptCaesar(plaintext, key);
		println("Ciphertext: " + ciphertext);
	}

	/*
	 * Encrypts a string by adding the value of key
	 * The first line makes sure that key is always
	 * converting negative keys to the equivalent positive shift. */
	private String encryptCaesar(String str, int key) { 
		if (key < 0) {
			key = 26 - (-key % 26);
		}
		String result = "";
		for (int i = 0; i < str.length(); i++) {
			char ch = encryptCharacter(str.charAt(i), key);
			result += ch;
		}
		return result;
	}

	/*
	 * Encrypts a single character using the key given. This
	 * method assumes the key is non-negative. Non-letter
	 * characters are returned unchanged. */
	private char encryptCharacter(char ch, int key) { 
		if (Character.isLetter(ch)) {
			ch = (char) ('A' + (Character.toUpperCase(ch) - 'A' + key) % 26);
		}
		return ch; 
	}

}