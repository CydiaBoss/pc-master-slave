package com.wang.manager.file;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * The is the {@code Encryption Key}
 * 
 * @author Andrew Wang
 * @since 1.0
 */
public class Key extends DataFile {
	
	/**
	 * The key
	 */
	private byte[] key;		
	
	/**
	 * The key file
	 */
	private File privKey;
	
	/**
	 * Finds or creates the a key file
	 * 
	 * @param key
	 * 			  Where the key is stored
	 */
	public Key(File key) {
		privKey = key;
		if(!check(privKey)) {
			this.key = genKey().getBytes();	
			writeTo(privKey, this.key);
		}else 
			this.key = read(privKey);
	}
	
	/**
	 * Generates a key
	 * 
	 * @return
	 * 			 A key
	 */
	private String genKey() {
		try {
			KeyGenerator gen = KeyGenerator.getInstance("AES");
			gen.init(128);
			return Base64.getEncoder().encodeToString((gen.generateKey().getEncoded()));
		} catch (NoSuchAlgorithmException e) {
			error("The Algorithm isn't found. Unable to continue.", true);
		}
		return null;
	}
	
	/**
	 * Get the key
	 * 
	 * @return
	 * 			 The key
	 */
	public SecretKey getKey() {
		byte[] byteKey = Base64.getDecoder().decode(key);
		return new SecretKeySpec(byteKey, 0, byteKey.length, ALGORITHM);
	}
}
