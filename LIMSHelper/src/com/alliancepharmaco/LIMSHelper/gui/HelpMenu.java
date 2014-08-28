package com.alliancepharmaco.LIMSHelper.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;


public class HelpMenu {
	protected JMenu init() {
		JMenu help = new JMenu("Help");
		help.setMnemonic(KeyEvent.VK_H);
		
		//How to menu item
		JMenuItem howTo = new JMenuItem("How To...");
		howTo.setMnemonic(KeyEvent.VK_H);
		howTo.setToolTipText("Everything you need to know.");
		howTo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String msg = "This program adds AP sample ID and sample External ID from sample\n"
						+ " receipt list (.xls) to Watson LIMS\n"
						+ "\n"
						+ "Follow the following steps:\n"
						+ "1. Open LIMS exported .xls file and save copy as excel 1997-2003\n"
						+ "2. Locate this excel 1997-2003 file in IDAdder\n"
						+ "3. Make sure sample receipt file is in excel 1997-2003 format and \n"
						+     "    the columns for subject, visit, time, ap id and external id \n"
						+     "    are in text format.\n"
						+ "4. Locate the receipt file in IDAdder.\n"
						+ "5. Specify the column number of subject, visit, and time in IDAdder.\n"
						+     "    Number starts at 0.\n"
						+ "6. Hit Create to specify name and location of a .xls file.\n"
						+ 	  "    This is the file to be imported to LIMS.\n";
				JOptionPane.showMessageDialog(null, msg, "How to...", JOptionPane.PLAIN_MESSAGE);
			}
		});
		
		//About menu item
		JMenuItem about = new JMenuItem("About IDAdder");
		about.setMnemonic(KeyEvent.VK_A);
		about.setToolTipText("Information about this program");
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String msg = "IDAdder version 1.0\n"
						+ "Jiangbo Tang\n"
						+ "All rights reserved. 2014.";
				JOptionPane.showMessageDialog(null, msg, "About IDAdder", JOptionPane.PLAIN_MESSAGE);
			}
		});
		
		
		help.add(howTo);
		help.add(about);
		
		return help;
	}
}

