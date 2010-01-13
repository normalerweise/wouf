package org.bh.gui.swing;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

public class BHDataExchangeDialog extends JDialog {

	
	public BHDataExchangeDialog(Frame owner, boolean modal) {
		super(owner, modal);
		setLayout(new BorderLayout());
		

		setSize(400, 500);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
	}
	
	
	
}