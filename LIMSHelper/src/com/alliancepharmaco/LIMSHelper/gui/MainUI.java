package com.alliancepharmaco.LIMSHelper.gui;

import java.awt.Container;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;

public class MainUI extends JFrame {
	
	public MainUI() {
		initUI();
	}
	
	private void initUI() {
		Container pane = getContentPane();
		GroupLayout gl = new GroupLayout(pane);
		pane.setLayout(gl);
		
		JMenuBar menubar = new JMenuBar();
		
		JMenu file = (new FileMenu()).init();
		JMenu help = (new HelpMenu()).init();
		menubar.add(file);
		menubar.add(help);
		
		setJMenuBar(menubar);
		setTitle("IDAdder");
		setSize(600, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainUI main = new MainUI();
				main.setVisible(true);
			}			
		});
	}
}
