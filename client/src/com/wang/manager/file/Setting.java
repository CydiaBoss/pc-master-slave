package com.wang.manager.file;

import java.io.File;

/**
 * The is the setting file
 * 
 * @author Andrew Wang
 * @since 1.0
 */
public class Setting extends DataFile {

	/**
	 * The Private Key
	 */
	private Key key = new Key(new File(DATADIR + File.separator + "sKey.key"));
	
	/**
	 * The File
	 */
	private File setting;
	
	/*- The Settings -*/
	
	// The Shutdown Timeout
	private int shutdown = 60;
	// The Update Speed
	private int updSpd = 1;
	
	/**
	 * Creates the Setting File
	 */
	public Setting() {
		setting = new File(DATADIR + File.separator + "setting.coma");
		if(!check(setting)) {
			update();
		}else
			reload();
	}
	
	/**
	 * Updates the file
	 */
	private void update() {
		writeTo(setting, encrypt(key.getKey(), ("updSpeed:" + updSpd + "\n" +
						"shutdown:" + shutdown).getBytes()));
		reload();
	}
	
	/**
	 * Reload the settings
	 */
	public void reload() {
		String data = new String(decrypt(key.getKey(), read(setting)));
		for(String line : data.split("\n")) {
			String[] pkgs = line.split(":");
			if(pkgs[0].equals("updSpeed")){
				try {
					updSpd = Integer.parseInt(pkgs[1]);
				}catch(ArrayIndexOutOfBoundsException e) {
					updSpd = 1;
					update();
				}
			}else if(pkgs[0].equals("shutdown")){
				try {
					shutdown = Integer.parseInt(pkgs[1].trim());
				}catch(ArrayIndexOutOfBoundsException e) {
					shutdown = 60;
					update();
				}
			}
		}
	}
	
	/**
	 * Gets Update Speed
	 * 
	 * @return
	 * The update speed
	 */
	public int getUpdSpd() {
		return updSpd;
	}
	
	/**
	 * Gets the shutdown Time
	 * 
	 * @return
	 * The shutdown time
	 */
	public int getTimeout() {
		return shutdown;
	}
	
	/**
	 * Sets the Update Speed
	 * 
	 * @param upd
	 * The new update speed
	 */
	public void setUpdSpd(int upd) {
		updSpd = upd;
		update();
	}
	
	/**
	 * Set the timeout
	 * 
	 * @param time
	 * The new timeout
	 */
	public void setTimeout(int time) {
		shutdown = time;
		update();
	}
}
