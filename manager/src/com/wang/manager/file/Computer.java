package com.wang.manager.file;

import java.io.File;

import com.wang.manager.Core;

/**
 * This is the {@code Computer} file
 * 
 * @author Andrew Wang
 * @since 1.0
 */
public class Computer extends DataFile {
	
	/**
	 * The computers' directory
	 */
	public static final File COMDIR = new File(DATADIR + File.separator + "computers");

	/**
	 * <i>Encryption</i> {@code Key} for the {@code Computer} file
	 */
	Key k = new Key(new File(COMDIR + File.separator + "key.key"));
	
	/**
	 * The {@code File}
	 */
	private File file;
	
	/**
	 * The {@code Computer}'s name
	 */
	private String name;
	
	/**
	 * If the {@code Computer} is locked
	 */
	private boolean lock = false;
	
	/**
	 * Why the {@code Computer} is locked
	 */
	private String reason = " ";
	
	/**
	 * How to unlock the {@code Computer}
	 */
	private String ransom = " ";
	
	/**
	 * Creates a {@code Computer} file
	 * 
	 * @param name
	 * The {@code Computer}'s name
	 */
	public Computer(String name) {
		// Set name
		this.name = name;
		// Create computer file
		file = new File(COMDIR + File.separator + name + ".coma");
		if(!check(file)) {
			update();
		}else
			reload();
	}

	/**
	 * Updates the {@code Computer}'s file
	 */
	private void update() {
		writeTo(file, encrypt(k.getKey(), ("locked:" + lock + "\n" +
			  	   "reason:" + reason + "\n" +
			  	   "ransom:" + ransom).getBytes()));
		reload();
	}
	
	/**
	 * ReReads the {@code Computer}'s File
	 */
	public void reload() {
		// Parse data
		String data = new String(decrypt(k.getKey(), read(file)));
		for(String line : data.split("\n")) {
			String[] pkgs = line.split(":");
			if(pkgs[0].equals("locked"))
				lock = Boolean.parseBoolean(pkgs[1]);
			else if(pkgs[0].equals("reason")) {
				try {
					reason = pkgs[1];
				}catch(ArrayIndexOutOfBoundsException e) {
					reason = " ";
					update();
				}
			}else if(pkgs[0].equals("ransom")) {
				try {
					ransom = pkgs[1];
				}catch(ArrayIndexOutOfBoundsException e) {
					ransom = " ";
					update();
				}
			}
		}
	}
	
	/**
	 * Locks down the {@code Computer}
	 * 
	 * @param reason
	 * The reason for the shutdown
	 * @param ransom
	 * What to do to unlock
	 */
	public void lock(String reason, String ransom) {
		lock = true;
		this.ransom = ransom;
		this.reason = reason;
		update();
	}
	
	/**
	 * Unlocks the {@code Computer}
	 */
	public void unlock() {
		lock = false;
		this.ransom = "";
		this.reason = "";
		update();
	}
	
	/**
	 * Checks if the {@code Computer} is locked
	 * 
	 * @return
	 * Is it locked?
	 */
	public boolean isLocked() {
		return lock;
	}
	
	/**
	 * Returns the {@code Computer}'s name
	 * 
	 * @return
	 * The name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Return the reason for the lockdown
	 * 
	 * @return
	 * The reason
	 */
	public String getReason() {
		reason = reason.trim();
		return reason;
	}
	
	/**
	 * Return the ransom to unlock the {@code Computer}
	 * 
	 * @return
	 * The ransom
	 */
	public String getRansom() {
		ransom = ransom.trim();
		return ransom;
	}
	
	/**
	 * Checks if the {@code Computer}'s exists
	 * 
	 * @return
	 * If the file exists
	 */
	public boolean exists() {
		return file.exists();
	}
	
	/**
	 * Removes the {@code Computer} from the program
	 */
	public void remove() {
		file.delete();
		Core.getComs().remove(this);
	}
}
