package com.wang.manager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import com.wang.manager.client.Background;
import com.wang.manager.file.Computer;
import com.wang.manager.file.DataFile;
import com.wang.manager.file.Log;
import com.wang.manager.file.Setting;

/**
 * The is the {@code Main} file.
 * 
 * @author Andrew Wang
 * @since 1.0
 */
public class Core {
	
	/**
	 * These are the program settings
	 */
	public static final Setting SET = new Setting();
	
	/**
	 * The Local Computer's Hostname
	 */
	private static String HOST; 
	
	/**
	 * This is the {@code Log}.
	 */
	public static Log LOG;
	
	/**
	 * This is the <i>array</i> that contains the {@code Computers}
	 */
	public static ArrayList<Computer> computers = new ArrayList<Computer>();
	
	/**
	 * This is the {@code Main Method}
	 * 
	 * @param args
	 * This will determine what to open
	 */
	public static void main(String[] args) {
		try {
			HOST = InetAddress.getLocalHost().getHostName();
			LOG = new Log("client_" + HOST);
		} catch (UnknownHostException e) {}
		// Makes the directories if needed
		DataFile.DATADIR.mkdirs();
		Computer.COMDIR.mkdir();
		// Gets all the computers
		for(String name : Computer.COMDIR.list()) {
			String newName = name.replaceAll("\\.coma", "");
			if(!newName.equals("key.key"))
				computers.add(new Computer(newName));
		}
		// Starts the Background Task
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Safely <i>invokes</i> the {@code Background Task}
			 */
			@Override
			public void run() {
				Background bg = new Background(HOST);
				bg.start();
			}
		});
	}
	
	/**
	 * Returns the Log
	 * 
	 * @return
	 * The Log
	 */
	public static Log getLog() {
		return LOG;
	}
}
