package com.wang.manager;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import com.wang.manager.file.Computer;
import com.wang.manager.file.DataFile;

//import javax.swing.SwingUtilities;

import com.wang.manager.file.Log;
import com.wang.manager.file.Password;
import com.wang.manager.file.Setting;
import com.wang.manager.gui.Panel;
import com.wang.manager.gui.PasswordManager;

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
	 * This is the {@code Log}.
	 */
	public static final Log LOG = new Log("log");
	
	/**
	 * This is the {@code Password}
	 */
	public static final Password PASS = new Password();
	
	/**
	 * This is the {@code Password Manager}
	 */
	public static final PasswordManager PM = new PasswordManager();
	
	/**
	 * This is the <i>array</i> that contains the {@code Computers}
	 */
	private static ArrayList<Computer> computers = new ArrayList<Computer>();
	
	/**
	 * This is the {@code Main Method}
	 * 
	 * @param args
	 * This will determine what to open
	 */
	public static void main(String[] args) {
		// Makes the directories if needed
		DataFile.DATADIR.mkdirs();
		Computer.COMDIR.mkdir();
		// Opens the Control Panel
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Safely <i>invokes</i> the {@code GUI}
			 */
			@Override
			public void run() {
				if(!PM.isUnlocked())
					PM.enterPassword();
				else {
					Panel p = new Panel();
					p.init();
				}
			}
		});
	}
	
	/**
	 * Reloads the {@code Computer} <i>Array</i>
	 */
	public static void reloadCom() {
		computers = new ArrayList<Computer>();
		// Gets all the computers
		for(String name : Computer.COMDIR.list()) {
			name = name.replaceAll("\\.coma", "");
			if(!name.equals("key.key"))
				computers.add(new Computer(name));
		}
	}
	
	/**
	 * Return the {@code Computer} <i>Array</i>
	 * 
	 * @return
	 * The {@code Computer} <i>Array</i>
	 */
	public static ArrayList<Computer> getComs() {
		return computers;
	}
}
