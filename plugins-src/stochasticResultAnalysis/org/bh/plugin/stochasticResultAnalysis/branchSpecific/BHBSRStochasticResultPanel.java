/*******************************************************************************
 * Copyright 2011: Matthias Beste, Hannes Bischoff, Lisa Doerner, Victor Guettler, Markus Hattenbach, Tim Herzenstiel, Günter Hesse, Jochen Hülß, Daniel Krauth, Lukas Lochner, Mark Maltring, Sven Mayer, Benedikt Nees, Alexandre Pereira, Patrick Pfaff, Yannick Rödl, Denis Roster, Sebastian Schumacher, Norman Vogel, Simon Weber * : Anna Aichinger, Damian Berle, Patrick Dahl, Lisa Engelmann, Patrick Groß, Irene Ihl, Timo Klein, Alena Lang, Miriam Leuthold, Lukas Maciolek, Patrick Maisel, Vito Masiello, Moritz Olf, Ruben Reichle, Alexander Rupp, Daniel Schäfer, Simon Waldraff, Matthias Wurdig, Andreas Wußler
 *
 * Copyright 2009: Manuel Bross, Simon Drees, Marco Hammel, Patrick Heinz, Marcel Hockenberger, Marcus Katzor, Edgar Kauz, Anton Kharitonov, Sarah Kuhn, Michael Löckelt, Heiko Metzger, Jacqueline Missikewitz, Marcel Mrose, Steffen Nees, Alexander Roth, Sebastian Scharfenberger, Carsten Scheunemann, Dave Schikora, Alexander Schmalzhaf, Florian Schultze, Klaus Thiele, Patrick Tietze, Robert Vollmer, Norman Weisenburger, Lars Zuckschwerdt
 *
 * Copyright 2008: Camil Bartetzko, Tobias Bierer, Lukas Bretschneider, Johannes Gilbert, Daniel Huser, Christopher Kurschat, Dominik Pfauntsch, Sandra Rath, Daniel Weber
 *
 * This program is free software: you can redistribute it and/or modify it un-der the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FIT-NESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.bh.plugin.stochasticResultAnalysis.branchSpecific;




import java.awt.BorderLayout;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.bh.data.DTOScenario;
import org.bh.data.types.DistributionMap;
import org.bh.gui.swing.BHMenuBar;
import org.bh.gui.swing.comp.BHButton;

@SuppressWarnings("serial")
public class BHBSRStochasticResultPanel extends JPanel{
	
	static final Logger log = Logger.getLogger(BHBSRStochasticResultPanel.class);
	private DTOScenario scenario;
	private DistributionMap result;
	private DistributionMap resultBSR;
	private BHButton exportButton;
	private BHButton printButton;
	JPanel mainPanel = null;
	GridBagConstraints d;
	JLabel space;
	
	public BHBSRStochasticResultPanel(DTOScenario scenario, DistributionMap result) {
		this.scenario = scenario;
		this.result = result;
		initialize();
	}
	
	public BHBSRStochasticResultPanel(DTOScenario scenario, DistributionMap result, DistributionMap resultBSR) {
		this.scenario = scenario;
		this.result = result;
		this.resultBSR = resultBSR;
		initialize();
	}
	
	private void initialize(){
		this.setLayout(new BorderLayout());
		exportButton = new BHButton(BHBSRStochasticResultController.PanelKeys.EXPORTSCENARIO);
		printButton = new BHButton(BHBSRStochasticResultController.PanelKeys.PRINTSCENARIO);
		
		// ActionListener for print-function (menu)
		BHMenuBar.filePrint.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){ 
				printButton.doClick();
	    	}
	    });
		
		// ActionListener for export-function (menu)
		BHMenuBar.scenarioExport.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){ 
				exportButton.doClick();
	    	}
	    });
		
		// deactivate Print- and Export-Button
		exportButton.setVisible(false);
		printButton.setVisible(false);
		
		BSRStochasticPanel mainStochastic = new BSRStochasticPanel();
		if(result.isTimeSeries()){
			mainStochastic.addTimeSeries();
//			if(this.resultBSR!=null)
//				mainStochastic.addTimeSeries();
		}		
		
		mainPanel = mainStochastic;
		this.add(mainPanel, BorderLayout.CENTER);
		JPanel exportArea = new JPanel(new GridBagLayout());
		d = new GridBagConstraints();

		d.fill = GridBagConstraints.HORIZONTAL;
		d.gridx = 0;
		d.gridy = 0;
		d.insets = new Insets(10,10,10,0);
		exportArea.add(exportButton, d );
		
		d.fill = GridBagConstraints.HORIZONTAL;
		d.gridx = 1;
		d.gridy = 0;
		d.insets = new Insets(10,10,10,10);
		exportArea.add(printButton, d);
		
		space = new JLabel();
		d.gridx = 2;
		d.gridy = 0;
		d.weightx = 1.0;
		exportArea.add(space, d);
		exportArea.setMaximumSize(new Dimension(200, 40));
		
		add(exportArea, BorderLayout.NORTH);
	}
}
