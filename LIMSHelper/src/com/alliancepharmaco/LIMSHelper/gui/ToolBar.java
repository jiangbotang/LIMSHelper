package com.alliancepharmaco.LIMSHelper.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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

import com.alliancepharmaco.LIMSHelper.exceptions.SplNotInLIMSException;
import com.alliancepharmaco.LIMSHelper.fileProcessors.IdAdder;
import com.alliancepharmaco.LIMSHelper.files.LimsSampleListXls;
import com.alliancepharmaco.LIMSHelper.files.ManifestXls;
import com.alliancepharmaco.LIMSHelper.files.Xls;

public class ToolBar {
	//File object represent LIMS .xls and sample list .xls file
	private File limsFile;
	private File manifestFile;
	private File limsImportFile;
	
	private Xls baseManifestXls;
	private ManifestXls manifestXls;
	private LimsSampleListXls limsXls;
	
	//indices from user selection. Generated "enterPos" tool bar button
	private int apIdIndex;
	private int exIdIndex;
	private int subjectIndex;
	private int visitIndex;
	private int timeIndex;	
	
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
		 * 1. Open a FileChooser dialog for user to select a .xls exported from LIMS
		 * 2. Initialize limsXls
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
		 * 1. Open a FileChooser dialog for user to select a .xls manifest
		 * 2. Initialize baseManifestXls
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
		 * Perform the following in sequence when user clicks the button
		 * 1. Check if baseManifestXls has been initialized.
		 * 		no:		give error message
		 *      yes:	next step
		 * 2. Open a dialog for user input of the columns for apID, exID, subject, visit,
		 *      time of the baseManifestXls
		 * 3. Initialize manifiestXls with user's input 
		 */
		enterPos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if(baseManifestXls != null) {
					JPanel inputPanel = new JPanel();
					inputPanel.setLayout(new GridLayout(6, 2, 5,5));
					ArrayList<String> choices = new ArrayList<String>();
	
					Map<Integer, Object> header = baseManifestXls.getHeader();				
					Set<Integer> keyset = header.keySet();
					for (Integer key : keyset) {
						choices.add(key.toString() + "@" + header.get(key).toString());
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
					
					//setup panel for user selection of items
					inputPanel.add(new JLabel("Information"));
					inputPanel.add(new JLabel("column index@header"));
					inputPanel.add(new JLabel("AP_ID:"));
					inputPanel.add(selectApID);
					inputPanel.add(new JLabel("External_ID:"));
					inputPanel.add(selectExID);
					inputPanel.add(new JLabel("subject:"));
					inputPanel.add(selectSubject);
					inputPanel.add(new JLabel("visit:"));
					inputPanel.add(selectVisit);
					inputPanel.add(new JLabel("time:"));
					inputPanel.add(selectTime);
					
					//record user selections of combo box options
					int option = JOptionPane.showConfirmDialog(panel, inputPanel, "specify columns",
							JOptionPane.OK_CANCEL_OPTION);
					if (option == JOptionPane.OK_OPTION) {
						apIdIndex = getIndex((String) selectApID.getSelectedItem());
						exIdIndex = getIndex((String) selectExID.getSelectedItem());
						subjectIndex = getIndex((String) selectSubject.getSelectedItem());
						visitIndex = getIndex((String) selectVisit.getSelectedItem());
						timeIndex = getIndex((String) selectTime.getSelectedItem());					
					}
					
					//Create a ManifestXls
					manifestXls = new ManifestXls(manifestFile, subjectIndex, visitIndex,
							timeIndex, apIdIndex, exIdIndex);
				}
				else {
					JOptionPane.showMessageDialog(panel, "Please select a manifest file first!",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		/*
		 * Add actionListener for the start button
		 * Perform the following in sequence when user clicks the button
		 * 1. Check if limsXls and manifestXls have been initialize.
		 * 		no: 	give error message
		 * 		yes: 	next step
		 * 2. Open a FileChooser dialog for user to select a .xls for file save
		 * 3. Add AP_ID and external_ID to limsXls and output to .xls from step 2
		 */
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				// Step 1:
				if (limsXls != null && manifestXls != null) {
					// Step 2:
					JFileChooser fc = new JFileChooser();
					FileFilter filter = new FileNameExtensionFilter("xls file", "xls");
					fc.addChoosableFileFilter(filter);
									
					int ret = fc.showSaveDialog(panel);
					if (ret == JFileChooser.APPROVE_OPTION) {
						limsImportFile = fc.getSelectedFile();
					}
					
					// Step 3:
					try {
						Xls importXls = IdAdder.addId(limsXls, manifestXls);
						importXls.toXls(limsImportFile);
						
						// Show user a task completion dialog
						JOptionPane.showMessageDialog(panel, "AP ID and External ID added.",
								"Completed", JOptionPane.INFORMATION_MESSAGE);
					} catch (SplNotInLIMSException ex) {
						String errorMessage = ex.getMessage();
						JOptionPane.showMessageDialog(panel, errorMessage, "Error",
								JOptionPane.ERROR_MESSAGE);
					}
					
				}
				//limsXls or manifestXls not intialized
				else {
					JOptionPane.showMessageDialog(panel, "LIMS export file and/or manifest file"
							+ " (.xls) has not been specified yet.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		return toolBar;
	}
	
	/*
	 * Split String in the form of "int@String" and return the int part 
	 */
	private int getIndex(String str) {
		String[] parts = str.split("@");
		return Integer.parseInt(parts[0]);
	}
	
	public File getLimsFile() {
		return limsFile;
	}
	
	public File getSplFile() {
		return manifestFile;
	}
}
