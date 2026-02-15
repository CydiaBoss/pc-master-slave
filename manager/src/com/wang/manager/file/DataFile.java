package com.wang.manager.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import com.wang.manager.Core;

/**
 * This is the <i>parent</i> file for all files
 * 
 * @author Andrew Wang
 * @since 1.0
 */
public class DataFile {
	
	/**
	 * Main Directory
	 */
	public static final File DATADIR = new File(/*Core.SET.getSerDir()*/File.separator + File.separator + "WANG-AIRPORT-TI" + File.separator + "Time Capsule" + File.separator + "AppData");
	
	/**
	 * The Selected Algorithm
	 */
	protected static final String ALGORITHM = "AES";

	/**
	 * Reads a file and returns the data inside the file
	 * 
	 * @param input
	 * 		      The input file
	 * @return
	 *            The data
	 */
	protected static byte[] read(File input) {
		// The data
		FileInputStream inputStream;
		byte[] data = new byte[0];
		try {
			inputStream = new FileInputStream(input);
			int curByte;
			int i = 0;
			byte[] modData;
			// Reads the file
			while((curByte = inputStream.read()) != -1) {
				modData = new byte[data.length + 1];
				for(byte b : data) {
					modData[i] = b;
					i++;
				}
				modData[modData.length - 1] = (byte) curByte;
				data = modData;
				i = 0;
			}
			inputStream.close();
		} catch (IOException e) {
			error("Failed to write data to file" + input.getName(), false);
		}
		return data;
	}
	
	/**
	 * Writes to the file
	 * 
	 * @param output
	 * 		      The output file
	 * @param data
	 * 		      The data to write to the file
	 */
	protected static void writeTo(File output, byte[] data) {
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(output);
			outputStream.write(data);
			outputStream.close();
		} catch (IOException e) {
			error("Failed to write data to file " + output.getName(), false);
		}
	}
	
	/**
	 * Encrypts the <i>input</i>
	 * 
	 * @param key
	 *            The private key to use to encrypt
	 * @param input
	 *            The data to encrypt
	 * @return
	 * 			  The data that is encrypted
	 */
	protected static byte[] encrypt(SecretKey key, byte[] input) {
		try {
			return doCrypto(Cipher.ENCRYPT_MODE, key, input);
		} catch (InvalidAlgorithmParameterException e) {}
		return null;
	}

	/**
	 * Decrypts a file
	 * 
	 * @param key
	 *            The private key to use to decrypt
	 * @param input
	 *            The data to decrypt
	 * @return
	 * 			  The data that is decrypted
	 */
	protected static byte[] decrypt(SecretKey key, byte[] input) {
		try {
			return doCrypto(Cipher.DECRYPT_MODE, key, input);
		} catch (InvalidAlgorithmParameterException e) {}
		return null;
	}

	/**
	 * Encrypts or Decrypts a {@code byte} array
	 * 
	 * @param cipherMode
	 *            The mode of cryptography
	 * @param key
	 *            The private key to use 
	 * @param input
	 *            The data
	 * @return
	 * 			  The data encrypted or decrypted
	 * @throws InvalidAlgorithmParameterException 
	 */
	private static byte[] doCrypto(int cipherMode, SecretKey secretKey, byte[] input) throws InvalidAlgorithmParameterException {
		try {
			// Create the Cipher
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(cipherMode, secretKey);
			// Manual Padding
			while(input.length % 16 != 0) {
				byte[] modInput = new byte[input.length + 1];
				int i = 0;
				for(byte b : input) {
					modInput[i] = b;
					i++;
				}
				input = modInput;
			}
			// Encrypt or decrypt
			return cipher.doFinal(input);
		// Error Catching
		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException
				| IllegalBlockSizeException ex) {
			error("The Encryption or Decryption process has failed. " + ex.getMessage() + ". Unable to continue.", true);
		} catch(BadPaddingException ex) {
			error("Something is wrong with the key. Unable to continue.", true);
		}
		return null;
	}
	
	/**
	 * Outputs an error to the {@code Log}
	 * 
	 * @param msg
	 *            The error message
	 * @param stop
	 *            Whether to crash the program
	 */
	protected static void error(String msg, boolean stop) {
		Core.LOG.write(msg, true);
		if(stop) 
			System.exit(0);
	}
	
	/**
	 * Check if file exists and if not, creates it.
	 * 
	 * @param f
	 *            The targeted file
	 * @return
	 * 			  Whether the file exists or not
	 */
	protected static boolean check(File f) {
		try {
			if(!f.exists()) {
				f.createNewFile();
				return false;
			}
		} catch (IOException e) {
			error("Unable to check file " + f.getName() + ". Unable to continue", true);
		}
		return true;
	}
}
