package com.wang.manager.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.wang.manager.Core;
import com.wang.manager.file.Computer;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Color;
import javax.swing.Icon;

public class Panel {
	
	private JFrame main;
	private JTabbedPane settings;
	private JTextField updSpd;
	private JTextField timeout;
	private JPanel[] computers;
	
	/**
	 * Initialize the contents of the frame.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public void init() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			Core.LOG.write("Unable to load UIs.", true);
		}
		main = new JFrame();
		main.setResizable(false);
		main.setTitle("Control Panel");
		try {
			main.setIconImage(ImageIO.read(Core.class.getResourceAsStream("/cm.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		main.setBounds(100, 100, 450, 300);
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.getContentPane().setLayout(new BorderLayout(0, 0));
		
		settings = new JTabbedPane(JTabbedPane.TOP);
		main.getContentPane().add(settings);
		
		JPanel general = new JPanel();
		settings.addTab("General", (Icon) null, general, null);
		general.setLayout(null);
		
		JLabel updSpdLlb = new JLabel("Update Time");
		updSpdLlb.setFont(new Font("Tahoma", Font.BOLD, 12));
		updSpdLlb.setBounds(25, 25, 89, 23);
		general.add(updSpdLlb);
		
		updSpd = new JTextField("" + Core.SET.getUpdSpd());
		updSpd.setHorizontalAlignment(SwingConstants.TRAILING);
		updSpdLlb.setLabelFor(updSpd);
		updSpd.setBounds(25, 50, 68, 20);
		general.add(updSpd);
		updSpd.setColumns(10);
		
		JLabel updLlb = new JLabel("Seconds");
		updLlb.setLabelFor(updSpd);
		updLlb.setFont(new Font("Tahoma", Font.PLAIN, 9));
		updLlb.setBounds(95, 53, 68, 14);
		general.add(updLlb);
		
		JLabel timeLlb = new JLabel("Shutdown Timeout");
		timeLlb.setFont(new Font("Tahoma", Font.BOLD, 12));
		timeLlb.setBounds(25, 75, 128, 23);
		general.add(timeLlb);
		
		timeout = new JTextField("" + Core.SET.getTimeout());
		timeLlb.setLabelFor(timeout);
		timeout.setHorizontalAlignment(SwingConstants.TRAILING);
		timeout.setBounds(25, 98, 68, 20);
		general.add(timeout);
		timeout.setColumns(10);
		
		JButton chagPass = new JButton();
		chagPass.setText("Change Password");
		try {
			chagPass.setIcon(new ImageIcon(ImageIO.read(Core.class.getResourceAsStream("/key.png"))));
		} catch (IOException e2) {}
		chagPass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Core.PM.changePassword();
			}
		});
		
		JLabel secLlb = new JLabel("Seconds");
		secLlb.setLabelFor(timeout);
		secLlb.setFont(new Font("Tahoma", Font.PLAIN, 9));
		secLlb.setBounds(95, 101, 68, 14);
		general.add(secLlb);
		chagPass.setBounds(212, 80, 201, 43);
		general.add(chagPass);
		
		JButton submit = new JButton("Apply");
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(updSpd.getText().equals("")) {
					new Popup("Error", "The inputed update speed is empty.");
				}else if(timeout.getText().equals("")) {
					new Popup("Error", "The inputed timeout is empty.");
				}else if(Integer.parseInt(updSpd.getText()) < 0) {
					new Popup("Error", "The inputed update speed can not be less than 0.");
				}else if(Integer.parseInt(timeout.getText()) < 0) {
					new Popup("Error", "The inputed timeout can not be less than 0.");
				}else{
					Core.SET.setUpdSpd(Integer.parseInt(updSpd.getText()));
					Core.SET.setTimeout(Integer.parseInt(timeout.getText()));
					new Popup("Success", "The settings are up to date.");
				}
			}
		});
		submit.setBounds(325, 200, 89, 23);
		general.add(submit);
		
		JButton button = new JButton("Add Computer");
		try {
			button.setIcon(new ImageIcon(ImageIO.read(Core.class.getResourceAsStream("/com.png"))));
		} catch (IOException e2) {}
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addComputer();
			}
		});
		button.setBounds(212, 25, 201, 43);
		general.add(button);
		
		JButton reload = new JButton("Reload");
		reload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reload();
				updSpd.setText("" + Core.SET.getUpdSpd());
				timeout.setText("" + Core.SET.getTimeout());
				new Popup("Success", "Reload Complete!");
			}
		});
		reload.setBounds(25, 200, 89, 23);
		general.add(reload);
		
		reload();
		
		main.setVisible(true);
	}
	
	/**
	 * Reload the Program Tabs
	 */
	private void reload() {
		Core.reloadCom();
		for(int i = settings.getTabCount() - 1; i > 0; i--)
			settings.removeTabAt(i);
		computers = new JPanel[Core.getComs().size()];
		for(int i = 0; i < computers.length; i++) {
			Computer curCom = Core.getComs().get(i);
			settings.addTab(curCom.getName(), null, genComFrame(i, curCom), null);
		}
	}
	
	/**
	 * Creates the Computer Tabs
	 * 
	 * @param curCom
	 * The current computer
	 * @param i
	 * The tab index
	 * @return
	 * The Panel
	 */
	private JPanel genComFrame(int i, Computer curCom) {
		JPanel computer = new JPanel();
		computer.setLayout(null);
		
		JLabel status = new JLabel("Status: " + ((curCom.isLocked())? "Locked" : "Unlocked"));
		status.setForeground((curCom.isLocked())? Color.RED : Color.GREEN);
		status.setHorizontalAlignment(SwingConstants.CENTER);
		status.setFont(new Font("Tahoma", Font.BOLD, 28));
		status.setBounds(25, 22, 390, 66);
		computer.add(status);
		
		JLabel newReason = new JLabel("Reason");
		newReason.setBounds(27, 102, 95, 14);
		computer.add(newReason);
		
		JLabel newRansom = new JLabel("Ransom");
		newRansom.setBounds(27, 127, 95, 14);
		computer.add(newRansom);
		
		JTextField inReason = new JTextField();
		inReason.setText(curCom.getReason());
		inReason.setBounds(87, 99, 330, 20);
		computer.add(inReason);
		inReason.setColumns(10);
		
		JTextField inRansom = new JTextField();
		inRansom.setText(curCom.getRansom());
		inRansom.setBounds(87, 124, 330, 20);
		computer.add(inRansom);
		inRansom.setColumns(10);
		
		if(curCom.isLocked()) {
			inReason.setEnabled(false);
			inRansom.setEnabled(false);
		}
		
		JButton lock = new JButton("Lock");
		JButton unlock = new JButton("Unlock");
		
		lock.addActionListener(new ActionListener() {
			/**
			 * Lock button click event
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				unlock.setEnabled(true);
				lock.setEnabled(false);
				curCom.lock(inReason.getText(), inRansom.getText());
				status.setForeground(Color.RED);
				status.setText("Status: Locked");
				inReason.setText(curCom.getReason());
				inRansom.setText(curCom.getRansom());
				inReason.setEnabled(false);
				inRansom.setEnabled(false);
			}
		});
		try {
			lock.setIcon(new ImageIcon(ImageIO.read(Core.class.getResourceAsStream("/cm.png"))));
		} catch (IOException e2) {}
		lock.setBounds(20, 161, 129, 41);
		if(curCom.isLocked())
			lock.setEnabled(false);
		computer.add(lock);
		
		
		unlock.addActionListener(new ActionListener() {
			/**
			 * Unlock button click event
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				unlock.setEnabled(false);
				lock.setEnabled(true);
				curCom.unlock();
				status.setForeground(Color.GREEN);
				status.setText("Status: Unlocked");
				inReason.setText("");
				inRansom.setText("");
				inReason.setEnabled(true);
				inRansom.setEnabled(true);
			}
		});
		try {
			unlock.setIcon(new ImageIcon(ImageIO.read(Core.class.getResourceAsStream("/cm_unlock.png"))));
		} catch (IOException e2) {}
		unlock.setBounds(288, 161, 129, 41);
		if(!curCom.isLocked())
			unlock.setEnabled(false);
		computer.add(unlock);
		
		JButton remove = new JButton("Remove");
		remove.setForeground(Color.RED);
		remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				curCom.remove();
				new Popup("Success", "The removal of computer " + curCom.getName() + " was a success!");
				settings.removeTabAt(i + 1);
			}
		});
		remove.setBounds(176, 209, 89, 23);
		computer.add(remove);
		return computer;
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void addComputer() {
		JFrame prompt = new JFrame();
		prompt.setTitle("New Computer");
		prompt.setResizable(false);
		try {
			prompt.setIconImage(ImageIO.read(Core.class.getResourceAsStream("/cm.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		prompt.setType(Type.POPUP);
		prompt.setAlwaysOnTop(true);
		prompt.setBounds(100, 100, 325, 135);
		prompt.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		prompt.getContentPane().setLayout(null);
		
		JLabel info = new JLabel("Enter the Computer's Name");
		info.setHorizontalAlignment(SwingConstants.CENTER);
		info.setBounds(72, 11, 175, 31);
		prompt.getContentPane().add(info);
		
		JTextField name = new JTextField();
		name.setBounds(36, 38, 247, 20);
		prompt.getContentPane().add(name);
		name.setColumns(10);
		
		JButton apply = new JButton("OK");
		
		name.addKeyListener(new KeyListener() {
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
		
		apply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Computer curCom = new Computer(name.getText());
				Core.getComs().add(curCom);
				settings.addTab(curCom.getName(), null, genComFrame(settings.getTabCount() - 1, curCom), null);
				prompt.setVisible(false);
				prompt.dispose();
			}
		});
		apply.setBounds(115, 69, 89, 23);
		prompt.getContentPane().add(apply);
		prompt.setVisible(true);
	}
}
