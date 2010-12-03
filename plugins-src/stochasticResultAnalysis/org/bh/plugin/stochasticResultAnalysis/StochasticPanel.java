package org.bh.plugin.stochasticResultAnalysis;

import info.clearthought.layout.TableLayout;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.bh.controller.Controller;
import org.bh.gui.chart.BHChartFactory;
import org.bh.gui.chart.BHChartPanel;
import org.bh.gui.swing.comp.BHDescriptionLabel;
import org.bh.gui.swing.comp.BHSlider;
import org.bh.gui.swing.comp.BHValueLabel;
import org.bh.gui.swing.forms.border.BHBorderFactory;
import org.bh.platform.i18n.ITranslator;

import com.jgoodies.forms.layout.FormLayout;

@SuppressWarnings("serial")
public class StochasticPanel extends JPanel{
	
	final ITranslator translator = Controller.getTranslator();

	 @SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(StochasticPanel.class);
	    //Chart
	    private BHChartPanel distributionChart;
	    
	    public StochasticPanel(){
	        this.initialize();
	    }

	    public void initialize() {
	        double border = 10;
	        double size[][] = {{border, TableLayout.MINIMUM, border, TableLayout.MINIMUM, border, TableLayout.PREFERRED, border}, // Columns
	            {border, TableLayout.PREFERRED, border, TableLayout.PREFERRED, border, TableLayout.PREFERRED, border, TableLayout.PREFERRED, border,TableLayout.PREFERRED, 
	        	border,TableLayout.PREFERRED,border,TableLayout.PREFERRED}}; // Rows


	        this.setLayout(new TableLayout(size));

	        distributionChart = BHChartFactory.getXYBarChart(BHStochasticResultController.ChartKeys.DISTRIBUTION_CHART);
	        
	        
	        BHDescriptionLabel sd = new BHDescriptionLabel("standardDeviation");
	        BHDescriptionLabel ew = new BHDescriptionLabel((BHStochasticResultController.PanelKeys.AVERAGE));
	        BHValueLabel sdValue = new BHValueLabel((BHStochasticResultController.ChartKeys.STANDARD_DEVIATION));
	        BHValueLabel ewValue = new BHValueLabel((BHStochasticResultController.ChartKeys.AVERAGE).toString());
	        
	        BHDescriptionLabel riskAt = new BHDescriptionLabel(BHStochasticResultController.PanelKeys.VALUE);
	        
	        BHSlider slider = new BHSlider(BHStochasticResultController.ChartKeys.RISK_AT_VALUE, 0, 100, 95);
	        //BHTextField riskAtField = new BHTextField(BHStochasticResultController.ChartKeys.RISK_AT_VALUE,"95", true);
	        
	        BHDescriptionLabel min = new BHDescriptionLabel("min");
	        BHDescriptionLabel max = new BHDescriptionLabel("max");
	        BHValueLabel minValue = new BHValueLabel(BHStochasticResultController.ChartKeys.RISK_AT_VALUE_MIN);
	        BHValueLabel maxValue = new BHValueLabel(BHStochasticResultController.ChartKeys.RISK_AT_VALUE_MAX);

	        JPanel rav = new JPanel();
	        rav.setLayout(new FormLayout ("4px:grow,right:pref,10px,pref,4px,pref,4px:grow","4px,p,4px,p,4px,p,4px"));
	        rav.add(riskAt, "2,2");
	        //riskAtField.setPreferredSize(new Dimension(50,riskAtField.getPreferredSize().height));
                slider.setPreferredSize(new Dimension(200,slider.getPreferredSize().height));
//	        ValidationRule[] rules = {VRIsDouble.INSTANCE, VRIsBetween.BETWEEN0AND100};//Validation for value at risk
//                riskAtField.setValidationRules(rules);
	        rav.add(slider, "4,2");
	        rav.add(new JLabel("%"), "6,2");
	        rav.add(min, "2,4");
	        rav.add(minValue, "4,4");
	        rav.add(new BHDescriptionLabel("currency"), "6,4"); //AWussler replaced: 3.12.2010: Now its translatable
	        rav.add(max, "2,6");
	        rav.add(maxValue, "4,6");
	        rav.add(new BHDescriptionLabel("currency"), "6,6"); //AWussler replaced: 3.12.2010: Now its translatable
	        rav.setBorder(BHBorderFactory.getInstacnce().createTitledBorder(BHBorderFactory.getInstacnce()
					.createEtchedBorder(EtchedBorder.LOWERED),
					BHStochasticResultController.ChartKeys.RISK_AT_VALUE, TitledBorder.LEFT,
					TitledBorder.DEFAULT_JUSTIFICATION));

	        
	        this.add(distributionChart, "1,3,5,3"); 
	        
	        /**
	         * AWussler added: 3.12.2010
	         * put sd and ew (standardabweichung, erwartungswert) in a panel box:
	         */
	        JPanel p_sd_ew = new JPanel();
	        p_sd_ew.setLayout(new GridLayout(2, 3)); //Gridlayout: 2 rows, 3 columns
	        //add first row:
	        p_sd_ew.add(sd);
	        p_sd_ew.add(sdValue);
	        p_sd_ew.add(new BHDescriptionLabel("currency"));
	        //add second row:
	        p_sd_ew.add(ew);
	        p_sd_ew.add(ewValue);
	        p_sd_ew.add(new BHDescriptionLabel("currency"));
	        p_sd_ew.setBorder(BHBorderFactory.getInstacnce().createEtchedBorder(EtchedBorder.LOWERED));//set untitled Border
	        //end: AWussler added: 3.12.2010
	        
	        /* AWussler removed and replaced: 3.12.2010: instead of adding each Label add the new panel containing all labels:
	        this.add(sd, "1,5");
	        this.add(sdValue, "3,5");
	        this.add(new JLabel("GE"), "5,5");
	        
	        this.add(ew, "1,7");
	        this.add(ewValue, "3,7");
	        this.add(new JLabel("GE"), "5,7");*/
	        this.add(p_sd_ew, "1,5,3,7");//first and second number: coordinate (x,y) of upper left corner, first and second number: coordinate (x,y) of under right corner
	        //end: AWussler removed and replaced: 3.12.2010
	        
	        this.add(rav,"1,9,3,9");
	        
	    }
	}
