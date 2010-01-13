package org.bh.plugin.resultAnalysis;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.bh.data.DTOScenario;
import org.bh.data.types.Calculable;
import org.bh.gui.ViewException;
import org.bh.gui.chart.BHChartFactory;
import org.bh.gui.swing.BHButton;
import org.bh.gui.swing.BHDescriptionLabel;
import org.bh.gui.swing.BHDescriptionTextArea;
import org.bh.gui.swing.BHValueLabel;
import org.bh.platform.IImportExport;
import org.bh.platform.Services;
import org.bh.platform.formula.FormulaException;
import org.bh.platform.formula.IFormula;
import org.bh.platform.formula.IFormulaFactory;
import org.bh.platform.i18n.ITranslator;
import org.jfree.chart.ChartPanel;

import com.jgoodies.forms.layout.CellConstraints;
import java.awt.FlowLayout;

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
 * @author Norman
 * @version 1.1, 10.01.2010
 *
 * @author Marco Hammel
 * @version 1.2 11.01.2010
 */
public final class BHResultPanel extends JPanel {

    private static final Logger log = Logger.getLogger(BHResultPanel.class);
    private final BHResultPanel me = this;
    private JPanel panel;
    private ChartPanel lineChartLabel;
    private ChartPanel pieChartLabel;
    private ChartPanel barChartLabel;
    private BHDescriptionTextArea pieChartTextArea;
    //TODO check Marco, Lars: Really necessary for every procedure?
    private CellConstraints cons;
    // formulas
    private Component finiteFormula;
    private Component infiniteFormula;
    //export button
    private BHButton exportButton;
    //probably not necessary in a later version
    final DTOScenario scenario;
    final Map<String, Calculable[]> result;
    final ITranslator translator = Services.getTranslator();

    /**
     * Constructor
     * @throws ViewException
     */
    public BHResultPanel(DTOScenario scenario, Map<String, Calculable[]> result) {
        this.scenario = scenario;
        this.result = result;
        initialize();
    }

    /**
     * Initialize method
     * @throws ViewException
     */
    public void initialize() {

//        double border = 10;
//        double size[][] = {{border, TableLayout.PREFERRED, border, TableLayout.PREFERRED, border, TableLayout.PREFERRED, border}, // Columns
//            {border, TableLayout.PREFERRED, border, TableLayout.PREFERRED, border, TableLayout.PREFERRED, border, TableLayout.PREFERRED, border}}; // Rows
//
//
//        this.setLayout(new TableLayout(size));
        this.setLayout(new BorderLayout());

//        //this.setMaximumSize(BHMainFrame.chartsPanel.getMaximumSize());
//       		/*
//         * Creates the default LineChart and add it on a Label
//         */
//        lineChartLabel = BHChartFactory.getLineChart("TestChart", "XAxis", "YAxis", "LineChart");
//        lineChartLabel.setFont(UIManager.getFont("defaultFont"));
//
//        /*
//         * Creates the description and add it on a Label
//         */
//        //lineChartTextArea = new BHDescriptionTextArea("RlineChartText", 5, 5);
//
//
//        /*
//         * creates the default PieChart
//         */
//        pieChartLabel = BHChartFactory.getPieChart("TestPieChart", "XAxis", "YAxis", "PieChart");
//        pieChartLabel.setFont(UIManager.getFont("defaultFont"));
//
//
//        pieChartTextArea = new BHDescriptionTextArea("RpieChartText", 5, 5);

        /*
         * creates BarChart
         */

        //barChartLabel = new ChartPanel(BHChartFactory.getBarChart(IShareholderValueCalculator.SHAREHOLDER_VALUE, "XAxis", "YAxis", "FTEShareholderValue"));
        //barChartLabel.setFont(UIManager.getFont("defaultFont"));


        /*
         * creates the Value- and DescriptionLabels
         */

        // exportButton
        exportButton = new BHButton("EXPORTSCENARIO");
        exportButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, IImportExport> impExpPlugins;
                impExpPlugins = Services.getImportExportPlugins(IImportExport.EXP_SCENARIO_RES_DET);
                //BHSelectionList selList = new BHSelectionList(impExpPlugins.keySet().toArray());
                //BHExportDialog expD = new BHExportDialog();
                //expD.add(selList);
                //expD.setVisible(true);
                String[] impExpArray = impExpPlugins.keySet().toArray(new String[0]);
                String s = (String) JOptionPane.showInputDialog(
                        me, "Choose output format",
                        "Scenario Export",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        impExpArray, impExpArray[0]);
                try {
                    IImportExport esp = impExpPlugins.get(s).getClass().newInstance();
                    esp.exportScenarioResults(scenario, result);

                } catch (InstantiationException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                //If a string was returned, say so.
//		if ((s != null) && (s.length() > 0)) {
//		    setLabel("Green eggs and... " + s + "!");
//		    return;
//		}

            }
        });





        /*
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
       
        if (scenario.getDCFMethod().getUniqueId().equals("fcf")) {
        	this.add(new BH_FCF_ResultPanel(), BorderLayout.CENTER);
        } else if (scenario.getDCFMethod().getUniqueId().equals("apv")) {
        	this.add(new BH_APV_ResultPanel(), BorderLayout.CENTER);
        } else if (scenario.getDCFMethod().getUniqueId().equals("fte")) {
        	this.add(new BH_FTE_ResultPanel(), BorderLayout.CENTER);
        }
        this.add(exportButton, BorderLayout.SOUTH);

        
    }
//	/**
//     * Test main method.
//	 * @throws ViewException 
//     */
//    public static void main(String args[]) throws ViewException {
//
//	JFrame test = new JFrame("Test for ResultPanel");
//	test.setContentPane(new BHResultPanel());
//	test.addWindowListener(new WindowAdapter() {
//	    
//	    @Override
//		public void windowClosing(WindowEvent e) {
//		System.exit(0);
//	    }
//	});
//	//test.pack();
//	test.setVisible(true);
//   }
}
