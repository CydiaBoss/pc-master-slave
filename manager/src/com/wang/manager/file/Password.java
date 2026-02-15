 package com.wang.manager.file;

import java.io.File;

import com.wang.manager.Core;

/**
 * <b>Password</b><br>
 * 
 * The is the password file
 * 
 * @author Andrew Wang
 * @since 1.0
 */
public class Password extends DataFile {
	
	/**
	 * Password
	 */
	private File pass;
	
	/**
	 * The password
	 */
	private String code;
	
	/**
	 * The key
	 */
	private Key key;
	
	/**
	 * <b>Password</b><br>
	 * 
	 * Creates the a password file
	 */
	public Password() {
		pass = new File(DATADIR + File.separator + "pass.coma");
		key = new Key(new File(DATADIR + File.separator + "privatekey.key"));
		if(check(pass)) 
			setPassword(new String(decrypt(key.getKey(), read(pass))));
		else
			Core.PM.newPassword();
	}

	/**
	 * Checks if the password was correct
	 * 
	 * @param key
	 * The password
	 * @return
	 * If successful
	 */
	public boolean checkCode(String key) {
		return code.equals(key);
	}
	
	/**
	 * Change if the password was correct
	 * 
	 * @param key
	 * The password
	 */
	public void changeCode(String key) {
		writeTo(pass, encrypt(this.key.getKey(), key.getBytes()));
		setPassword(key);
	}
	
	/**
	 * Trims and sets the password
	 */
	private void setPassword(String key) {
		code = key;
		code = code.trim();
	}
}
