package com.wang.manager.gui;

import java.awt.Font;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.wang.manager.Core;

public class PasswordManager {

	private boolean unlocked;
	private JPasswordField oldCode;
	private JPasswordField newCode;
	private JPasswordField newCode_1;
	private JPasswordField code;

	/**
	 * Create the application.
	 */
	public PasswordManager() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			Core.LOG.write("Unable to load UIs.", true);
		}
		unlocked = false;
	}

	/**
	 * Change the Password
	 */
	public void changePassword() {
		JFrame password = new JFrame();
		password.setType(Type.POPUP);
		password.setAlwaysOnTop(true);
		password.setTitle("Change Password");
		password.setResizable(false);
		try {
			password.setIconImage(ImageIO.read(Core.class.getResourceAsStream("/cm.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		password.setBounds(100, 100, 285, 250);
		password.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		password.getContentPane().setLayout(null);
		
		JLabel oldPass = new JLabel("Old Password");
		oldPass.setFont(new Font("Tahoma", Font.BOLD, 11));
		oldPass.setHorizontalAlignment(SwingConstants.CENTER);
		oldPass.setBounds(89, 11, 98, 20);
		password.getContentPane().add(oldPass);
		
		oldCode = new JPasswordField();
		oldPass.setLabelFor(oldCode);
		oldCode.setBounds(72, 31, 132, 20);
		password.getContentPane().add(oldCode);
		
		JLabel newPass = new JLabel("New Password");
		newPass.setHorizontalAlignment(SwingConstants.CENTER);
		newPass.setFont(new Font("Tahoma", Font.BOLD, 11));
		newPass.setBounds(89, 62, 98, 20);
		password.getContentPane().add(newPass);
		
		newCode = new JPasswordField();
		newPass.setLabelFor(newCode);
		newCode.setBounds(72, 81, 132, 20);
		password.getContentPane().add(newCode);
		
		JLabel newPass_1 = new JLabel("Confirm Password");
		newPass_1.setHorizontalAlignment(SwingConstants.CENTER);
		newPass_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		newPass_1.setBounds(82, 112, 115, 20);
		password.getContentPane().add(newPass_1);
		
		newCode_1 = new JPasswordField();
		newPass_1.setLabelFor(newCode_1);
		newCode_1.setBounds(72, 133, 132, 20);
		password.getContentPane().add(newCode_1);
		
		JButton apply = new JButton("Apply");
		apply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Core.PASS.checkCode(new String(oldCode.getPassword())) && 
						new String(newCode.getPassword()).equals(new String(newCode_1.getPassword()))) {
					Core.PASS.changeCode(new String(newCode.getPassword()));
					new Popup("Success", "The Password was Changed!");
					password.setVisible(false);
					password.dispose();
				}else
					new Popup("Error", "The Password couldn't be Changed.");
			}
		});
		apply.setBounds(89, 174, 98, 23);
		password.getContentPane().add(apply);
		password.setVisible(true);
	}
	
	/**
	 * Enter the Password
	 * 
	 * @wbp.parser.entryPoint
	 */
	public void enterPassword() {
		JFrame password = new JFrame();
		password.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		password.setTitle("Enter the Password");
		password.setResizable(false);
		try {
			password.setIconImage(ImageIO.read(Core.class.getResourceAsStream("/cm.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		password.setBounds(100, 100, 295, 140);
		password.setAlwaysOnTop(true);
		password.getContentPane().setLayout(null);
		
		JLabel title = new JLabel("Enter the Password");
		title.setFont(new Font("Tahoma", Font.BOLD, 11));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBounds(85, 11, 118, 29);
		password.getContentPane().add(title);
		
		code = new JPasswordField();
		code.setBounds(52, 36, 185, 20);
		password.getContentPane().add(code);
		
		JButton apply = new JButton("OK");
		apply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Core.PASS.checkCode(new String(code.getPassword()))) {
					unlocked = true;
					password.setVisible(false);
					password.dispose();
					Panel p = new Panel();
					p.init();
				}else
					new Popup("Error", "Wrong Password.");
			}
		});
		apply.setBounds(100, 67, 89, 23);
		password.getContentPane().add(apply);
		
		code.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			/**
			 * Pressing Enter will Trigger the Button
			 */
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10)
					apply.doClick();
			}
			@Override
			public void keyReleased(KeyEvent e) {}
		});
		
		password.setVisible(true);
	}
	
	/**
	 * New Password
	 */
	public void newPassword() {
		JFrame password = new JFrame();
		password.setType(Type.POPUP);
		password.setAlwaysOnTop(true);
		password.setTitle("Change Password");
		password.setResizable(false);
		try {
			password.setIconImage(ImageIO.read(Core.class.getResourceAsStream("/cm.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		password.setBounds(100, 100, 285, 200);
		password.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		password.getContentPane().setLayout(null);
		
		JLabel newPass = new JLabel("New Password");
		newPass.setHorizontalAlignment(SwingConstants.CENTER);
		newPass.setFont(new Font("Tahoma", Font.BOLD, 11));
		newPass.setBounds(89, 21, 98, 20);
		password.getContentPane().add(newPass);
		
		newCode = new JPasswordField();
		newPass.setLabelFor(newCode);
		newCode.setBounds(72, 40, 132, 20);
		password.getContentPane().add(newCode);
		
		JLabel newPass_1 = new JLabel("Confirm Password");
		newPass_1.setHorizontalAlignment(SwingConstants.CENTER);
		newPass_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		newPass_1.setBounds(82, 71, 115, 20);
		password.getContentPane().add(newPass_1);
		
		newCode_1 = new JPasswordField();
		newPass_1.setLabelFor(newCode_1);
		newCode_1.setBounds(72, 92, 132, 20);
		password.getContentPane().add(newCode_1);
		
		JButton apply = new JButton("Apply");
		apply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(new String(newCode.getPassword()).equals(new String(newCode_1.getPassword()))) {
					Core.PASS.changeCode(new String(newCode.getPassword()));
					new Popup("Success", "The password was added successfully!");
					unlocked = true;
					password.setVisible(false);
					password.dispose();
				}else
					new Popup("Error", "The passwords don't match.");
			}
		});
		apply.setBounds(89, 123, 98, 23);
		password.getContentPane().add(apply);
		password.setVisible(true);
	}
	
	/**
	 * Is the session unlocked
	 * 
	 * @return
	 * If the session is unlocked or not
	 */
	public boolean isUnlocked() {
		return unlocked;
	}
}
