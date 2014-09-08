package com.alliancepharmaco.LIMSHelper.gui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

public class MainUI extends JFrame {
	
	private JPanel panel;
	private JTextArea area;
	
	public MainUI() {
		initUI();
	}
	
	private void initUI() {
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		area = new JTextArea();
		area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JScrollPane pane = new JScrollPane();
		pane.getViewport().add(area);
		
		panel.add(pane);
		
		//Setup menu
		JMenuBar menubar = new JMenuBar();		
		JMenu file = (new FileMenu()).init();
		JMenu help = (new HelpMenu()).init();
		menubar.add(file);
		menubar.add(help);		
		setJMenuBar(menubar);
		
		//Setup toolbar
		JToolBar toolbar = (new ToolBar()).init(panel, area);
		add(toolbar, BorderLayout.NORTH);
		
		//Setup text area

		add(panel);
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
