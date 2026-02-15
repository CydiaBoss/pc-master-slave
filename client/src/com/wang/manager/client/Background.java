package com.wang.manager.client;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window.Type;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.wang.manager.Core;
import com.wang.manager.file.Computer;

/**
 * This is the background {@code task} that will check the online <i>server</i><br/> 
 * If the Computer is being used without authority. It will force shutdown the computer until<br/>
 * The problem is resolved.
 * 
 * @author Andrew Wang
 * @version 1.0
 */
public class Background extends Thread{
	
	/**
	 * The {@code Computer} file
	 */
	private Computer com;
	
	/**
	 * This <i>constructor</i> initializes the background {@code Thread}.
	 * 
	 * @param name
	 * This is the name of the computer. This will determine which {@code Computer} file 
	 * to read and/or creates one if necessary 
	 */
	public Background(String name) {
		// Thread
		setPriority(MAX_PRIORITY);
		setDaemon(false);
		setName(name);
		// Find Computer file
		com = null;
		for(Computer c : Core.computers) {
			if(c.getName().equals(name)) {
				com = c;
				break;
			}
		}
		// If not found, create!
		if(com == null) {
			com = new Computer(name);
			Core.computers.add(com);
		}
	}
	
	/**
	 * This will start the {@code Thread}
	 */
	@Override
	public void run() {
		// Loops forever
		while(com.exists()) {
			// Reread File
			com.reload();
			// Check if it is Locked
			if(com.isLocked()) {
				// Shutdown
				com.reload();
				if(com.isLocked()) {
					if(init()) {
						try {
							Core.getLog().write("Shutting Down.");
							Runtime.getRuntime().exec("shutdown -s -f -t 0");
						} catch (IOException e) {
							Core.getLog().write("Shutdown Function Failed. Unable to Continue.", true);
							System.exit(-1);
						}
					}
				}
			}
			// Delay until next update
			try {
				sleep(Core.SET.getUpdSpd() * 1000);
			} catch (InterruptedException e) {
				Core.getLog().write("Delay Function Failed. Unable to Continue.", true);
				System.exit(-1);
			}
		}
		Core.getLog().write("Computer " + com.getName() + " is unregistered.");
		Core.getLog().write("Computer " + com.getName() + " is shutting down.");
		try {
			Runtime.getRuntime().exec("shutdown -s -f -t 10 -c \"Error 0x001a has occurred. Contact Administrator.\"");
		} catch (IOException e) {
			Core.getLog().write("Shutdown Function Failed. Unable to Continue.", true);
			System.exit(-1);
		}
	}
	
	/**
	 * Builds the Window
	 * 
	 * @return 
	 * If you should shutdown or not
	 * @wbp.parser.entryPoint
	 */
	private boolean init() {
		// Create the Popup
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			Core.LOG.write("Unable to load UIs.", true);
		}
		JFrame sDPrompt = new JFrame();
		sDPrompt.setResizable(false);
		sDPrompt.setType(Type.POPUP);
		sDPrompt.setTitle("Shutdown");
		try {
			sDPrompt.setIconImage(ImageIO.read(Core.class.getResourceAsStream("/cc.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		sDPrompt.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		sDPrompt.setAlwaysOnTop(true);
		sDPrompt.setBounds(100, 100, 450, 250);
		sDPrompt.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		// Buffer
		Component bufferTop = Box.createVerticalStrut(20);
		sDPrompt.getContentPane().add(bufferTop);
		// Title
		JLabel prompt = new JLabel("This Computer is Shutting Down");
		try{
			prompt.setIcon(new ImageIcon(ImageIO.read(Core.class.getResourceAsStream("/cm.png"))));
		}catch(IOException e){}
		prompt.setFont(new Font("Tahoma", Font.BOLD, 14));
		prompt.setHorizontalAlignment(SwingConstants.CENTER);
		sDPrompt.getContentPane().add(prompt);
		// Reason
		JLabel reason = new JLabel("Reason");
		reason.setFont(new Font("Tahoma", Font.BOLD, 11));
		reason.setHorizontalAlignment(SwingConstants.CENTER);
		sDPrompt.getContentPane().add(reason);
		JLabel reasonPrompt = new JLabel(com.getReason());
		reasonPrompt.setFont(new Font("Tahoma", Font.ITALIC, 11));
		reasonPrompt.setHorizontalAlignment(SwingConstants.CENTER);
		sDPrompt.getContentPane().add(reasonPrompt);
		// Ransom
		JLabel ransom = new JLabel("Ransom");
		ransom.setFont(new Font("Tahoma", Font.BOLD, 11));
		ransom.setHorizontalAlignment(SwingConstants.CENTER);
		sDPrompt.getContentPane().add(ransom);
		JLabel ransomPrompt = new JLabel(com.getRansom());
		ransomPrompt.setFont(new Font("Tahoma", Font.ITALIC, 11));
		ransomPrompt.setHorizontalAlignment(SwingConstants.CENTER);
		sDPrompt.getContentPane().add(ransomPrompt);
		// Buffer
		Component bufferBottom = Box.createVerticalStrut(20);
		sDPrompt.getContentPane().add(bufferBottom);
		// Activate
		sDPrompt.setVisible(true);
		for(int i = Core.SET.getTimeout(); i >= 0; i--) {
			prompt.setText("This Computer is Shutting Down in " + i);
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				Core.getLog().write("Delay Function Failed. Unable to Continue.", true);
				System.exit(-1);
			}
			// If the computer was unlocked
			com.reload();
			if(!com.isLocked()) {
				sDPrompt.setVisible(false);
				sDPrompt.dispose();
				return false;
			}
		}
		// Hide
		sDPrompt.setVisible(false);
		sDPrompt.dispose();
		return true;
	}
}
