package com.wang.manager.file;

import java.io.File;
import java.io.IOException;

/**
 * The is the log file
 * 
 * @author Andrew Wang
 * @since 1.0
 */
public class Log extends DataFile {
	
	/**
	 * Log
	 */
	private File log;
	
	/**
	 * The log output
	 */
	private String output = "LOG\n"
						  + "-----\n";
	
	/**
	 * Creates the a log file
	 */
	public Log(String name) {
		int i = 1;
		do{
			log = new File(DATADIR.getAbsolutePath() + File.separator + name + i + ".txt");
			i++;
		}while(log.exists());
		try{
			log.createNewFile();
		}catch(IOException e) {}
	}
	
	/**
	 * Writes to the log as info
	 * 
	 * @param msg
	 *            The message          
	 */
	public void write(String msg) {
		write(msg, false);
	}
	
	/**
	 * Writes to log as info or an error
	 * 
	 * @param msg
	 *            The message
	 * @param error
	 * 			  Whether it's an error or not           
	 */
	public void write(String msg, boolean error) {
		output += ((error)? "[ERROR] " : "[INFO] ") + msg + '\n';
		writeTo(log, output.getBytes());
	}
}
