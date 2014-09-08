package com.alliancepharmaco.LIMSHelper.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.alliancepharmaco.LIMSHelper.files.LimsSampleListXls;
import com.alliancepharmaco.LIMSHelper.files.ManifestXls;
import com.alliancepharmaco.LIMSHelper.files.Xls;

public class ToolBar {
	//File object represent LIMS .xls and sample list .xls file
	private File limsFile;
	private File manifestFile;
	
	private Xls baseManifestXls;
	private ManifestXls manifestXls;
	private LimsSampleListXls limsXls;
	
	JPanel panel;
	JTextArea area;
	
	/*
	 * Initialize tool bar
	 */
	protected JToolBar init(JPanel jpanel, JTextArea jarea) {
		this.panel = jpanel;
		this.area = jarea;
		
		JToolBar toolBar = new JToolBar();
		
		//set up toolbar items
		ImageIcon lims = new ImageIcon(".\\resrc\\icons\\lims.png");
		ImageIcon manifest = new ImageIcon(".\\resrc\\icons\\excel.png");
		ImageIcon pos = new ImageIcon(".\\resrc\\icons\\input_2.jpg");
		ImageIcon startIcon = new ImageIcon(".\\resrc\\icons\\start_2.jpg");
		JButton openLimsXls = new JButton(lims);
		JButton openManifest = new JButton(manifest);
		JButton enterPos = new JButton(pos);
		JButton start = new JButton(startIcon);
		
		openLimsXls.setToolTipText("Click to locate the .xls exported from LIMS.");
		openManifest.setToolTipText("Click to locate the .xls manifest file.");
		enterPos.setToolTipText("Click to specify the columns of 'subject', 'visit', and 'time'. "
				+ "in manifest file.");
		start.setToolTipText("Click to generate .xls file with AP ID and external ID "
				+ " added for import to LIMS.");
		
		toolBar.add(openLimsXls);
		toolBar.add(openManifest);
		toolBar.add(enterPos);
		toolBar.add(start);
		
		/*
		 * Add ActionListener for openLimsList button
		 */
		openLimsXls.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFileChooser fc = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("xls file", "xls");
				fc.addChoosableFileFilter(filter);
								
				int ret = fc.showDialog(panel, "Open file");
				if (ret == JFileChooser.APPROVE_OPTION) {
					limsFile = fc.getSelectedFile();
					limsXls = new LimsSampleListXls(limsFile);
					String content = limsXls.toString();
					area.setText(content);
				}
			}
		});
		
		/*
		 * Add ActionListener for openManifest button
		 */
		openManifest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFileChooser fc = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("xls file", "xls");
				fc.addChoosableFileFilter(filter);
								
				int ret = fc.showDialog(panel, "Open file");
				if (ret == JFileChooser.APPROVE_OPTION) {
					manifestFile = fc.getSelectedFile();
					baseManifestXls = new Xls(manifestFile);
					String content = baseManifestXls.toString();
					area.setText(content);
				}
			}
		});
		
		/*
		 * Add ActionListener for enterPos button
		 */
		enterPos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JPanel inputPanel = new JPanel();
				ArrayList<String> choices = new ArrayList<String>();

				if(baseManifestXls != null) {
					Map<Integer, Object> header = baseManifestXls.getHeader();				
					Set<Integer> keyset = header.keySet();
					for (Integer key : keyset) {
						choices.add(key.toString() + "@" + header.get(key).toString());
					}
				}
				
				//convert ArrayList<String> to String[]
				String[] choicesArr = new String[choices.size()];
				choicesArr = choices.toArray(choicesArr);
				
				//setup combo boxes
				JComboBox selectApID = new JComboBox(choicesArr);
				JComboBox selectExID = new JComboBox(choicesArr);
				JComboBox selectSubject = new JComboBox(choicesArr);
				JComboBox selectVisit = new JComboBox(choicesArr);
				JComboBox selectTime = new JComboBox(choicesArr);
				
				inputPanel.add(new JLabel("specifiy AP_ID column:"));
				inputPanel.add(selectApID);
				inputPanel.add(new JLabel("specifiy External_ID column:"));
				inputPanel.add(selectExID);
				inputPanel.add(new JLabel("specifiy subject column:"));
				inputPanel.add(selectSubject);
				inputPanel.add(new JLabel("specifiy visit column:"));
				inputPanel.add(selectVisit);
				inputPanel.add(new JLabel("specifiy time column:"));
				inputPanel.add(selectTime);
				
//				JTextField subjectIndex = new JTextField();
//				JTextField visitIndex = new JTextField();
//				JTextField timeIndex = new JTextField();
//				
//				Object[] message = {
//					"Enter column index for subject: ", subjectIndex,
//					"Enter column index for visit: ", visitIndex,
//					"Enter column index for time: ", timeIndex,
//				};
//				
				int option = JOptionPane.showConfirmDialog(panel, inputPanel, "index@"
						+ "column lable", JOptionPane.OK_CANCEL_OPTION);
				
			}
		});
		
		return toolBar;
	}
	
	public File getLimsFile() {
		return limsFile;
	}
	
	public File getSplFile() {
		return manifestFile;
	}
}
