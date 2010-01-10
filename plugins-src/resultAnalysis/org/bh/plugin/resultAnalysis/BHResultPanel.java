
package org.bh.plugin.resultAnalysis;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.bh.gui.ViewException;
import org.bh.gui.chart.BHChartFactory;
import org.bh.gui.swing.BHDescriptionLabel;
import org.bh.gui.swing.BHDescriptionTextArea;
import org.bh.gui.swing.BHValueLabel;
import org.bh.platform.Services;
import org.bh.platform.i18n.ITranslator;
import org.jfree.chart.ChartPanel;

import com.jgoodies.forms.layout.CellConstraints;
/**
 * 
 * Class to create one ResultPanel with two different Charts and Descriptions 
 *
 * <p>
 * This class creates a <code>JPanel</code> with the Results. There are two different
 * charts and the descriptions. 
 *
 * @author Lars.Zuckschwerdt
 * @version 1.0, 30.12.2009
 *
 */

public final class BHResultPanel extends JPanel{
	
	private JPanel panel;
	private ChartPanel lineChartLabel;
	private ChartPanel pieChartLabel;
	private ChartPanel barChartLabel;
	private BHDescriptionTextArea pieChartTextArea;
	
	//FTE Verfahren
	private BHValueLabel FTEshareholderValue;
	private BHDescriptionLabel FTEshareholderValueDESC;
	private BHValueLabel FTEpresentValueTaxShield;
	private BHDescriptionLabel FTEpresentValueTaxShieldDESC;
	private BHValueLabel FTEflowEquity;
	private BHDescriptionLabel FTEflowEquityDESC;
	private BHValueLabel FTEflowEquityTaxShield;
	private BHDescriptionLabel FTEflowEquityTaxShieldDESC;
	private BHValueLabel FTEflowToEquity;
	private BHDescriptionLabel FTEflowToEquityDESC;
	private BHValueLabel FTEdebtAmortisation;
	private BHDescriptionLabel FTEdebtAmortisationDESC;
	private BHValueLabel FTEequityReturnRate;
	private BHDescriptionLabel FTEequityReturnRateDESC;
	
	//FCF Verfahren
	private BHValueLabel FCFshareholderValue;
	private BHDescriptionLabel FCFshareholderValueDESC;
	private BHValueLabel FCFpresentValue; //Label
	private BHDescriptionLabel FCFpresentValueDESC;
	private BHValueLabel FCFequityReturnRate;
	private BHDescriptionLabel FCFequityReturnRateDESC;
	private BHValueLabel FCFdebtToEquityRatio;
	private BHDescriptionLabel FCFdebtToEquityRatioDESC;
	private BHValueLabel FCFwaccEquity;
	private BHDescriptionLabel FCFwaccEquityDESC;
	private BHValueLabel FCFwaccDebts;
	private BHDescriptionLabel FCFwaccDebtsDESC;
	private BHValueLabel FCFwacc;
	private BHDescriptionLabel FCFwaccDESC;
	
	//APV Verfahren
	private BHValueLabel APVshareholderValue;
	private BHDescriptionLabel APVshareholderValueDESC;
	private BHValueLabel APVpresentValue; //Label
	private BHDescriptionLabel APVpresentValueDESC;
	private BHValueLabel APVpresentValueTaxShield;
	private BHDescriptionLabel APVpresentValueTaxShieldDESC;

	private CellConstraints cons;
	
	final ITranslator translator = Services.getTranslator();
	/**
	 * Constructor
	 * @throws ViewException 
	 */
	public BHResultPanel() throws ViewException{
		this.initialize();
	}
	
	/**
	 * Initialize method
	 * @throws ViewException 
	 */
	public void initialize() throws ViewException{
		BorderLayout layout = new BorderLayout();
		
		 double border = 10;
	     double size[][] =
	         {{border, TableLayout.PREFERRED, border, TableLayout.PREFERRED, border, TableLayout.PREFERRED, border},  // Columns
	          {border, TableLayout.PREFERRED, border, TableLayout.PREFERRED, border, TableLayout.PREFERRED, border}}; // Rows


		
		this.setLayout(new TableLayout(size));
		
		//this.setMaximumSize(BHMainFrame.chartsPanel.getMaximumSize());
       		/**
       		 * Creates the default LineChart and add it on a Label 
       		 */
       		lineChartLabel = new ChartPanel(BHChartFactory.getLineChart("TestChart", "XAxis", "YAxis",  "LineChart"));
       		lineChartLabel.setFont(UIManager.getFont("defaultFont"));
       		
       		/**
       		 * Creates the description and add it on a Label
       		 */
       		//lineChartTextArea = new BHDescriptionTextArea("RlineChartText", 5, 5);
       		
       		
       		/**
       		 * creates the default PieChart
       		 */
       		pieChartLabel = new ChartPanel(BHChartFactory.getPieChart("TestPieChart", "XAxis", "YAxis", "PieChart"));
       		pieChartLabel.setFont(UIManager.getFont("defaultFont"));
       		
       		
       		pieChartTextArea = new BHDescriptionTextArea("RpieChartText", 5, 5);
       		
       		/**
       		 * creates BarChart
       		 */
       		
       		//barChartLabel = new ChartPanel(BHChartFactory.getBarChart(IShareholderValueCalculator.SHAREHOLDER_VALUE, "XAxis", "YAxis", "FTEShareholderValue"));
       		//barChartLabel.setFont(UIManager.getFont("defaultFont"));
       		
       		
       		/**
       		 * creates the Value- and DescriptionLabels
       		 */
       		
       		//All Labels to FTE
      		FTEshareholderValue = new BHValueLabel("SHAREHOLDER_VALUE");
      		FTEshareholderValueDESC = new BHDescriptionLabel("SHAREHOLDER_VALUE");
       		FTEdebtAmortisation = new BHValueLabel("DEBT_AMORTISATION");
       		FTEdebtAmortisationDESC = new BHDescriptionLabel("DEBT_AMORTISATION");
       		FTEequityReturnRate = new BHValueLabel("EQUITY_RETURN_RATE_FTE");
       		FTEequityReturnRateDESC = new BHDescriptionLabel("EQUITY_RETURN_RATE_FTE");
      		FTEflowEquity = new BHValueLabel("FLOW_TO_EQUITY");
       		FTEflowEquityDESC = new BHDescriptionLabel("FLOW_TO_EQUITY");
       		FTEflowEquityTaxShield = new BHValueLabel("FLOW_TO_EQUITY_TAX_SHIELD");
       		FTEflowEquityTaxShieldDESC = new BHDescriptionLabel("FLOW_TO_EQUITY_TAX_SHIELD");
       		FTEflowToEquity = new BHValueLabel("LOW_TO_EQUITY_INTEREST");
      		FTEflowToEquityDESC = new BHDescriptionLabel("FLOW_TO_EQUITY_INTEREST");
      		FTEpresentValueTaxShield = new BHValueLabel("PRESENT_VALUE_TAX_SHIELD");
       		FTEpresentValueTaxShieldDESC = new BHDescriptionLabel("PRESENT_VALUE_TAX_SHIELD");
       		
       		//All Labels to FCF
       		FCFshareholderValue = new BHValueLabel("SHAREHOLDER_VALUE");
       		FCFshareholderValueDESC = new BHDescriptionLabel("SHAREHOLDER_VALUE");
       		FCFpresentValue = new BHValueLabel("PRESENT_VALUE_FCF");
       		FCFpresentValueDESC = new BHDescriptionLabel("Result.PRESENT_VALUE_FCF");
       		FCFdebtToEquityRatio = new BHValueLabel("DEBT_TO_EQUITY_RATIO");
      		FCFdebtToEquityRatioDESC = new BHDescriptionLabel("Result.DEBT_TO_EQUITY_RATIO");
       		FCFequityReturnRate = new BHValueLabel("EQUITY_RETURN_RATE_FCF");
       		FCFequityReturnRateDESC = new BHDescriptionLabel("EQUITY_RETURN_RATE_FCF");
       		FCFwacc = new BHValueLabel("WACC");
       		FCFwaccDESC = new BHDescriptionLabel("WACC");
       		FCFwaccDebts = new BHValueLabel("WACC_DEBTS");
       		FCFwaccDebtsDESC = new BHDescriptionLabel("WACC_DEBTS");
       		FCFwaccEquity = new BHValueLabel("WACC_EQUITY");
       		FCFwaccEquityDESC = new BHDescriptionLabel("WACC_EQUITY");
       		
       		//All Labels to APV
      		APVpresentValue = new BHValueLabel("PRESENT_VALUE_FCF");
       		APVpresentValueDESC = new BHDescriptionLabel("PRESENT_VALUE_FCF");
       		APVshareholderValue = new BHValueLabel("SHAREHOLDER_VALUE");
       		APVshareholderValueDESC = new BHDescriptionLabel("SHAREHOLDER_VALUE");
       		APVpresentValueTaxShield = new BHValueLabel("PRESENT_VALUE_TAX_SHIELD");
       		APVpresentValueTaxShieldDESC = new BHDescriptionLabel("PRESENT_VALUE_TAX_SHIELD");
       		
       		//Formeldarstellung
       		
       		
       		/**
       		 * add Content to ResultPanel
       		 */
//       		this.add(FTEshareholderValueDESC, "1,1");
//       		this.add(FTEshareholderValue, "2,1");
//       		this.add(lineChartLabel, "3,1");
//       		
//       		this.add(FTEdebtAmortisationDESC, "1,2");
//       		this.add(FTEdebtAmortisation, "2,2");
//       		this.add(pieChartLabel, "3,2");
//       		
//       		this.add(FTEequityReturnRateDESC, BorderLayout.WEST);
//       		this.add(FTEequityReturnRate, BorderLayout.CENTER);
//       		//this.add(barChartLabel, BorderLayout.EAST);
//       		
//       		//this.add(ResultFormulaParser.getAPVformula());
//       		
//       		this.add(FTEflowEquityDESC, BorderLayout.WEST);
//       		this.add(FTEflowEquity, BorderLayout.CENTER);
//       		//this.add(barChartLabel, BorderLayout.EAST);
//       		
//       		this.add(FTEflowEquityTaxShieldDESC, BorderLayout.WEST);
//       		this.add(FTEflowEquityTaxShield, BorderLayout.CENTER);
//       		//this.add(lineChartLabel, BorderLayout.EAST);
//       		
//       		this.add(FTEflowToEquityDESC, BorderLayout.WEST);
//       		this.add(FTEflowToEquity, BorderLayout.CENTER);
//       		//this.add(lineChartLabel, BorderLayout.EAST);
//       		
//       		this.add(FTEpresentValueTaxShieldDESC, BorderLayout.WEST);
//       		this.add(FTEpresentValueTaxShield, BorderLayout.CENTER);
//       		//this.add(lineChartLabel, BorderLayout.EAST);
//       	
//       		this.add(FCFpresentValueDESC, BorderLayout.WEST);
//       		this.add(FCFpresentValue, BorderLayout.CENTER);
//       		//this.add(lineChartLabel, BorderLayout.EAST);
//       		
//       		this.add(FCFdebtToEquityRatioDESC, BorderLayout.WEST);
//       		this.add(FCFdebtToEquityRatio, BorderLayout.CENTER);
//       		//this.add(lineChartLabel, BorderLayout.EAST);
//       		
//       		this.add(FCFequityReturnRateDESC, BorderLayout.WEST);
//       		this.add(FCFequityReturnRate, BorderLayout.CENTER);
//       		//this.add(lineChartLabel, BorderLayout.EAST);
//       		
//       		this.add(FCFshareholderValueDESC, BorderLayout.WEST);
//       		this.add(FCFshareholderValue, BorderLayout.CENTER);
//       		//this.add(lineChartLabel, BorderLayout.EAST);
//       		
//       		this.add(FCFwaccDESC, BorderLayout.WEST);
//       		this.add(FCFwacc, BorderLayout.CENTER);
//       		//this.add(lineChartLabel, BorderLayout.EAST);
//       		
//       		this.add(FCFwaccDebtsDESC, BorderLayout.WEST);
//       		this.add(FCFwaccDebts, BorderLayout.CENTER);
//       		//this.add(lineChartLabel, BorderLayout.EAST);
//       		
//       		this.add(FCFwaccEquityDESC, BorderLayout.WEST);
//       		this.add(FCFwaccEquity, BorderLayout.CENTER);
//       		//this.add(lineChartLabel, BorderLayout.EAST);
//       		
       		this.add(APVpresentValueDESC, "1,1");
       		this.add(APVpresentValue, "3,1");
       		this.add(pieChartLabel, "5,1");
       		
       		this.add(APVpresentValueTaxShieldDESC, "1,3");
       		this.add(APVpresentValueTaxShield, "3,3");
       		this.add(lineChartLabel, "5,3");
//       		
//       		this.add(APVshareholderValueDESC, "1,2");
//       		this.add(APVshareholderValue, "2,2");
//       		this.add(lineChartLabel, "3,2");
       		
	}
	
	/**
     * Test main method.
	 * @throws ViewException 
     */
    public static void main(String args[]) throws ViewException {

	JFrame test = new JFrame("Test for ResultPanel");
	test.setContentPane(new BHResultPanel());
	test.addWindowListener(new WindowAdapter() {
	    
	    @Override
		public void windowClosing(WindowEvent e) {
		System.exit(0);
	    }
	});
	//test.pack();
	test.setVisible(true);
   }
}