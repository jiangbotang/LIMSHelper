package com.alliancepharmaco.LIMSHelper.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

public class ToolBar {
	
	protected JToolBar init() {
		JToolBar toolBar = new JToolBar();
		
		ImageIcon lims = new ImageIcon("C:\\Users\\Jiangbo\\git\\LIMSHelper\\LIMSHelper\\resrc\\icons\\lims.png");
		ImageIcon excel = new ImageIcon("C:\\Users\\Jiangbo\\git\\LIMSHelper\\LIMSHelper\\resrc\\icons\\excel.png");
		JButton openSplList = new JButton(lims);
		JButton openManifest = new JButton(excel); 
		
		toolBar.add(openSplList);
		toolBar.add(openManifest);
		
		openSplList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		
		return toolBar;
	}
}
