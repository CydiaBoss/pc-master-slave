package com.wang.manager.gui;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.wang.manager.Core;

import java.awt.Window.Type;
import java.io.IOException;
import java.awt.GridLayout;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Font;

public class Popup {

	private JFrame popup;
	private String title;
	private String info;

	/**
	 * Create a Popup Window.
	 */
	public Popup(String title, String info) {
		this.title = title;
		this.info = info;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			Core.LOG.write("Unable to load UIs.", true);
		}
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Safely Invoke The Popup
			 */
			@Override
			public void run() {
				initialize();
				popup.setVisible(true);
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		JLabel info = new JLabel(this.info);
		
		popup = new JFrame();
		popup.setTitle(title);
		popup.setType(Type.POPUP);
		try {
			popup.setIconImage(ImageIO.read(Core.class.getResourceAsStream("/cm.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		popup.setResizable(false);
		popup.setAlwaysOnTop(true);
		// ((this.info.length() * 5) > 150) ? 200 + (this.info.length() * 5) : 200 //
		popup.setBounds(100, 100, ((this.info.length() * 10) > 250) ? (this.info.length() * 10) : 250, 100);
		popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		popup.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		Component bufferTop = Box.createVerticalStrut(20);
		popup.getContentPane().add(bufferTop);
		
		info.setFont(new Font("Tahoma", Font.BOLD, 12));
		info.setHorizontalAlignment(SwingConstants.CENTER);
		popup.getContentPane().add(info);
		
		Component bufferBottom = Box.createVerticalStrut(20);
		popup.getContentPane().add(bufferBottom);
		
		if(title.equals("Error"))
			Core.LOG.write(this.info, true);
		else
			Core.LOG.write(this.info);
	}

}
