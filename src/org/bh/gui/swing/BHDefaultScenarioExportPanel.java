package org.bh.gui.swing;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.bh.platform.PlatformController;
import org.bh.validation.VRCheckFileTag;
import org.bh.validation.ValidationRule;

public class BHDefaultScenarioExportPanel extends JPanel implements ActionListener {
	
	private BHTextField txtPath;
	private JCheckBox open;
	

	private String fileDesc;
	private String fileExt;
	
	public BHDefaultScenarioExportPanel(String fileDesc, String fileExt)
	{
		this.fileDesc = fileDesc;
		this.fileExt = fileExt;
		
		setLayout(new BorderLayout());
		
		JPanel descrPanel = createDescriptionPanel();		
		JPanel selPanel = createSelectionArea();		
		JPanel actionPanel = createActionArea();
						
		add(descrPanel, BorderLayout.NORTH);
		add(selPanel, BorderLayout.CENTER);
		add(actionPanel, BorderLayout.SOUTH);
	}
	
	
	private JPanel createActionArea() {
		// Panel on which the Buttons will be placed
		JPanel actionPanel = new JPanel(new BorderLayout());
		// Add seperator for optical seperation
		actionPanel.add(new JSeparator(), BorderLayout.NORTH);
		
		// Button panel for adding an empty border as margin
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.add(Box.createHorizontalGlue());
		
		// Cancel 
		BHButton btnCancel = new BHButton("Bcancel");
		
		// Start export
		BHButton btnExport = new BHButton("Mexport");
		btnExport.setAlignmentX(Component.RIGHT_ALIGNMENT);		
		
		buttonPanel.add(btnCancel);
		buttonPanel.add(btnExport);
		
		
		actionPanel.add(buttonPanel, BorderLayout.CENTER);
		return actionPanel;
	}


	private JPanel createSelectionArea() {
		
		// Create panel on wich the selection list and the file chooser area
		// will be placed
		JPanel selectPanel = new JPanel(new BorderLayout());
		selectPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 15, 15));
		
		// An extra panel for file selection
		JPanel fileSelectionPanel = new JPanel();	
		fileSelectionPanel.setBorder(BorderFactory.createEmptyBorder(7,0,0,0));
		// 2 columns, 2 rows
		double size[][] = 
		{{0.7, 0.3}, {0.5, 0.5}};
		fileSelectionPanel.setLayout(new TableLayout(size));
			
		// Small label for instruction
		BHDescriptionLabel lblselExportPath = new BHDescriptionLabel("DExportPathSelection");		
		fileSelectionPanel.add(lblselExportPath, "0,0");
		
		// Text field which will show the chosen path
		txtPath = new BHTextField("DTxtExportImportPath", "");
		ValidationRule[] r = { VRCheckFileTag.PDF };
		txtPath.setValidationRules(r);
		
		fileSelectionPanel.add(txtPath, "0,1");
		
		// Button to start the file choosing dialog
		BHButton btnChooseFile = new BHButton("Bbrowse");
		btnChooseFile.addActionListener(this);
		fileSelectionPanel.add(btnChooseFile, "1,1");
		
		selectPanel.add(fileSelectionPanel, BorderLayout.NORTH);
		
		// Open option 
		JPanel openPanel = new JPanel();
		openPanel.setBorder(BorderFactory.createEmptyBorder(7,0,0,0));
		// 2 columns, 1 row
		double size2[][] = 
		{{0.5, 0.5}, {0.5}};
		openPanel.setLayout(new TableLayout(size2));
			
		// Small label for instruction
		BHDescriptionLabel lblOpen = new BHDescriptionLabel("DOpenSelection");		
		openPanel.add(lblOpen, "0,0");
		
		// Text field which will show the chosen path
		open = new JCheckBox();
		openPanel.add(open, "1,0");
		
		selectPanel.add(openPanel, BorderLayout.SOUTH);
		
		return selectPanel;
	}


	private JPanel createDescriptionPanel() {
		// Create description section
		JPanel panDescr = new JPanel();
		panDescr.setForeground(Color.white);
		panDescr.setLayout(new BorderLayout());
		
		// Text area with description
		BHDescriptionTextArea lblDescr = new BHDescriptionTextArea("DScenarioExportDescription");		
		lblDescr.setFocusable(false);
		
		// Create border for that textarea
		Border marginBorder = BorderFactory.createEmptyBorder(15, 5, 15, 5);
		Border whiteBorder = BorderFactory.createLineBorder(Color.white);
		Border grayBorder = BorderFactory.createLineBorder(UIManager.getColor("controlDkShadow"));
		Border outerBorder = BorderFactory.createCompoundBorder(whiteBorder, grayBorder);		
		
		// Set border
		lblDescr.setBorder(BorderFactory.createCompoundBorder(outerBorder, marginBorder));
		
		panDescr.add(lblDescr, BorderLayout.CENTER);		
		return panDescr;
	}


	public String getFilePath() {
		return txtPath.getText();
	}
	
	public boolean openAfterExport() {
		return open.isSelected();
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		IBHComponent comp =  (IBHComponent) e.getSource();
		if (comp.getKey().equals("Bbrowse"))
		{
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			
			String strDefDir = PlatformController.preferences.get("lastExportDirectory", null);
			if (strDefDir != null)
			{
				File defDir = new File(strDefDir);
				fileChooser.setCurrentDirectory(defDir);
			}		
			
			fileChooser.setFileFilter(new FileNameExtensionFilter(fileDesc, fileExt));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY );
			int returnVal = fileChooser.showSaveDialog(this);		
			
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				PlatformController.preferences.put("lastExportDirectory", fileChooser.getSelectedFile().getParent()); 
				txtPath.setText(fileChooser.getSelectedFile().getPath());			
			}
			
		}
	}

}