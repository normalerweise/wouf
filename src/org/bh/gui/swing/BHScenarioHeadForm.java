package org.bh.gui.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;

import org.bh.controller.Controller;
import org.bh.data.DTOScenario;
import org.bh.gui.ValidationMethods;
import org.bh.platform.Services;
import org.bh.platform.i18n.BHTranslator;
import org.bh.platform.i18n.ITranslator;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * This class contains the form for head-data
 * 
 * @author Anton Kharitonov
 * @author Patrick Heinz
 * @version 0.3, 01.01.2010
 * 
 */
public class BHScenarioHeadForm extends JPanel {

	private BHLabel lscenname;
	private BHLabel lscendescript;
	private BHLabel lequityyield;
	private BHLabel ldeptyield;
	private BHLabel ltradetax;
	private BHLabel lcorporatetax;
	private BHLabel lbaseyear;
	private BHLabel lprocess;

	private JComboBox cbprocess;

	private BHTextField tfscenname;
	private BHTextField tfscendescript;
	private BHTextField tfequityyield;
	private BHTextField tfdeptyield;
	private BHTextField tftradetax;
	private BHTextField tfcorporatetax;
	private BHTextField tfbaseyear;
	private BHLabel lpercentequity;
	private BHLabel lpercentdept;
	private BHLabel lpercenttrade;
	private BHLabel lpercentcorporate;

	private JPanel pprocess;

	private JTable tperioddata;

	private BHButton bcalculate;
	private BHLabel lcalculate;

	ITranslator translator = Services.getTranslator();

	/**
	 * Constructor.
	 */
	public BHScenarioHeadForm() {
		this.initialize();
	}

	/**
	 * Initialize method.
	 */
	private void initialize() {

		String rowDef = "4px,p,4px,p,4px,p,4px,p,20px,p,4px,p,20px,p,20px,max(80px;p),4px,p,4px";
		String colDef = "4px,4px,right:pref,4px,pref,max(80px;pref),4px,left:pref,24px:grow,pref,4px,pref,4px,right:pref,4px,pref,pref,4px,pref,4px,pref,4px";

		FormLayout layout = new FormLayout(colDef, rowDef);
		this.setLayout(layout);

		layout.setColumnGroups(new int[][] { { 6, 17 } });

		CellConstraints cons = new CellConstraints();

		this.add(this.getlscenName(), cons.xywh(3, 4, 1, 1));
		// this.add(this.getLprocess(), cons.xywh(14, 4, 1, 1));
		// this.add(this.getCbprocess(), cons.xywh(17, 4, 3, 1));
		this.add(this.getlbaseYear(), cons.xywh(14, 4, 1, 1));
		this.add(this.gettfbaseYear(), cons.xywh(17, 4, 1, 1));

		this.add(this.getlscenDescript(), cons.xywh(3, 6, 1, 1));
		this.add(this.getlequityYield(), cons.xywh(3, 10, 1, 1));
		this.add(this.getldeptYield(), cons.xywh(3, 12, 1, 1));
		this.add(this.getltradeTax(), cons.xywh(14, 10, 1, 1));
		this.add(this.getlcorporateTax(), cons.xywh(14, 12, 1, 1));
		// this.add(this.getlbaseYear(), cons.xywh(3, 8, 1, 1));
		this.add(this.gettfscenName(), cons.xywh(6, 4, 4, 1));
		this.add(this.gettfscenDescript(), cons.xywh(6, 6, 12, 1));
		this.add(this.gettfequityYeild(), cons.xywh(6, 10, 1, 1));
		this.add(this.gettfdeptYield(), cons.xywh(6, 12, 1, 1));
		this.add(this.gettftradeTax(), cons.xywh(17, 10, 1, 1));
		this.add(this.gettfcorporateTax(), cons.xywh(17, 12, 1, 1));
		// this.add(this.gettfbaseYear(), cons.xywh(6, 8, 1, 1));
		this.add(this.getlpercentEquity(), cons.xywh(8, 10, 1, 1));
		this.add(this.getlpercentDept(), cons.xywh(8, 12, 1, 1));
		this.add(this.getlpercentTrade(), cons.xywh(19, 10, 1, 1));
		this.add(this.getlpercentCorporate(), cons.xywh(19, 12, 1, 1));
		this.add(this.getPprocess(), cons.xywh(2, 14, 18, 1));
		this.add(new JScrollPane(this.getTperioddata()), cons
				.xywh(3, 16, 17, 1));
		this.add(this.getBcalculate(), cons.xywh(17, 18, 1, 1));
	}

	// TODO add missing label keys etc. and translations, change hard coded values to keys
	
	public JPanel getPprocess() {
		if (pprocess == null) {
			pprocess = new BHProcessForm();
			pprocess.setBorder(BorderFactory
					.createEtchedBorder(EtchedBorder.LOWERED));
		}
		return pprocess;
	}

	public BHButton getBcalculate() {
		if (this.bcalculate == null) {
			this.bcalculate = new BHButton("");
			this.bcalculate.setText("UW berechnen");
		}
		return bcalculate;
	}

	public JTable getTperioddata() {
		if (this.tperioddata == null) {

			String[] columnnames = { "Periode", "Fremdkapital", "FCF" };
			Integer[][] data = { { 2009, 0, 0 } };
			this.tperioddata = new JTable(data, columnnames);
			this.tperioddata.setGridColor(new Color(0, 0, 0));
			this.tperioddata.setPreferredScrollableViewportSize(new Dimension(
					30, 30));

		}
		return tperioddata;
	}

	public BHLabel getLprocess() {
		if (this.lprocess == null) {
			this.lprocess = new BHLabel("", "Berechnungsart");
		}
		return lprocess;
	}

	public JComboBox getCbprocess() {
		if (this.cbprocess == null) {
			this.cbprocess = new JComboBox();
		}
		return cbprocess;
	}

	/**
	 * Getter method for component lscenName.
	 * 
	 * @return the initialized component
	 */
	public BHLabel getlscenName() {

		if (this.lscenname == null) {
			this.lscenname = new BHLabel(DTOScenario.Key.NAME.toString(),
					"Szenarioname");
		}

		return this.lscenname;
	}

	/**
	 * Getter method for component lscenDescript.
	 * 
	 * @return the initialized component
	 */
	public BHLabel getlscenDescript() {

		if (this.lscendescript == null) {
			this.lscendescript = new BHLabel(
					DTOScenario.Key.COMMENT.toString(), "Beschreibung");
		}

		return this.lscendescript;
	}

	/**
	 * Getter method for component lequityYield.
	 * 
	 * @return the initialized component
	 */
	public BHLabel getlequityYield() {

		if (this.lequityyield == null) {
			this.lequityyield = new BHLabel(DTOScenario.Key.REK.toString(),
					"Renditeforderung Eigenkapital");
		}

		return this.lequityyield;
	}

	/**
	 * Getter method for component ldeptYield.
	 * 
	 * @return the initialized component
	 */
	public BHLabel getldeptYield() {

		if (this.ldeptyield == null) {
			this.ldeptyield = new BHLabel(DTOScenario.Key.RFK.toString(),
					"Renditeforderung Fremdkapital");
		}

		return this.ldeptyield;
	}

	/**
	 * Getter method for component ltradeTax.
	 * 
	 * @return the initialized component
	 */
	public BHLabel getltradeTax() {

		if (this.ltradetax == null) {
			this.ltradetax = new BHLabel(DTOScenario.Key.BTAX.toString(),
					"Gewerbesteuersatz");
		}

		return this.ltradetax;
	}

	/**
	 * Getter method for component lcorporateTax.
	 * 
	 * @return the initialized component
	 */
	public BHLabel getlcorporateTax() {

		if (this.lcorporatetax == null) {
			this.lcorporatetax = new BHLabel(DTOScenario.Key.CTAX.toString(),
					"Körperschaftssteuer und Solidaritätszuschlag");
		}

		return this.lcorporatetax;
	}

	/**
	 * Getter method for component lbaseYear.
	 * 
	 * @return the initialized component
	 */
	public BHLabel getlbaseYear() {

		if (this.lbaseyear == null) {
			this.lbaseyear = new BHLabel("baseyear", "Basisjahr");
		}

		return this.lbaseyear;
	}

	// Here do the getters for the textfields begin

	/**
	 * Getter method for component tfscenName.
	 * 
	 * @return the initialized component
	 */
	public BHTextField gettfscenName() {

		if (this.tfscenname == null) {
			this.tfscenname = new BHTextField(DTOScenario.Key.NAME.toString(),
					translator.translate("IscenarioName"));
			int[] rules = { ValidationMethods.isMandatory };
			tfscenname.setValidateRules(rules);
		}
		return this.tfscenname;
	}

	/**
	 * Getter method for component tfscenDescript.
	 * 
	 * @return the initialized component
	 */
	public BHTextField gettfscenDescript() {

		if (this.tfscendescript == null) {
			this.tfscendescript = new BHTextField(DTOScenario.Key.COMMENT
					.toString(), translator.translate("IscenDescript"));
		}
		return this.tfscendescript;
	}

	/**
	 * Getter method for component tfequityYeild.
	 * 
	 * @return the initialized component
	 */
	public BHTextField gettfequityYeild() {

		if (this.tfequityyield == null) {
			this.tfequityyield = new BHTextField(
					DTOScenario.Key.REK.toString(), translator.translate("IequityYield"));
			int[] rules = { ValidationMethods.isMandatory,
					ValidationMethods.isDouble, ValidationMethods.isPositive };
			tfequityyield.setValidateRules(rules);
		}
		return this.tfequityyield;
	}

	/**
	 * Getter method for component tfdeptYield.
	 * 
	 * @return the initialized component
	 */
	public BHTextField gettfdeptYield() {

		if (this.tfdeptyield == null) {
			this.tfdeptyield = new BHTextField(DTOScenario.Key.RFK.toString(),
					translator.translate("IdeptYield"));
			int[] rules = { ValidationMethods.isMandatory,
					ValidationMethods.isDouble, ValidationMethods.isPositive };
			tfdeptyield.setValidateRules(rules);
		}
		return this.tfdeptyield;
	}

	/**
	 * Getter method for component tftradeTax.
	 * 
	 * @return the initialized component
	 */
	public BHTextField gettftradeTax() {

		if (this.tftradetax == null) {
			this.tftradetax = new BHTextField(DTOScenario.Key.BTAX.toString(),
					translator.translate("ItradeTax"));
			int[] rules = { ValidationMethods.isMandatory,
					ValidationMethods.isDouble, ValidationMethods.isPositive,
					ValidationMethods.isBetween0and100 };
			tftradetax.setValidateRules(rules);
		}
		return this.tftradetax;
	}

	/**
	 * Getter method for component tfcorporateTax.
	 * 
	 * @return the initialized component
	 */
	public BHTextField gettfcorporateTax() {

		if (this.tfcorporatetax == null) {
			this.tfcorporatetax = new BHTextField(DTOScenario.Key.CTAX
					.toString(), translator.translate("IcorporateTax"));
			int[] rules = { ValidationMethods.isMandatory,
					ValidationMethods.isDouble, ValidationMethods.isPositive,
					ValidationMethods.isBetween0and100 };
			tfcorporatetax.setValidateRules(rules);
		}
		return this.tfcorporatetax;
	}

	/**
	 * Getter method for component tfbaseYear.
	 * 
	 * @return the initialized component
	 */
	public BHTextField gettfbaseYear() {

		if (this.tfbaseyear == null) {
			this.tfbaseyear = new BHTextField("", "");
			// TODO insert key and inputhint
			int[] rules = { ValidationMethods.isMandatory,
					ValidationMethods.isInteger, ValidationMethods.isPositive };
			tfbaseyear.setValidateRules(rules);
		}
		return this.tfbaseyear;
	}

	/**
	 * Getter method for component lpercentEquity.
	 * 
	 * @return the initialized component
	 */
	public BHLabel getlpercentEquity() {

		if (this.lpercentequity == null) {
			this.lpercentequity = new BHLabel("", "%");
		}
		return this.lpercentequity;
	}

	/**
	 * Getter method for component lpercentDept.
	 * 
	 * @return the initialized component
	 */
	public BHLabel getlpercentDept() {

		if (this.lpercentdept == null) {
			this.lpercentdept = new BHLabel("", "%");
		}
		return this.lpercentdept;
	}

	/**
	 * Getter method for component lpercentTrade.
	 * 
	 * @return the initialized component
	 */
	public BHLabel getlpercentTrade() {

		if (this.lpercenttrade == null) {
			this.lpercenttrade = new BHLabel("", "%");
		}
		return this.lpercenttrade;
	}

	/**
	 * Getter method for component lpercentCorporate.
	 * 
	 * @return the initialized component
	 */
	public BHLabel getlpercentCorporate() {

		if (this.lpercentcorporate == null) {
			this.lpercentcorporate = new BHLabel("", "%");
		}
		return this.lpercentcorporate;
	}

	// TODO remove main later
	/**
	 * Test main method.
	 */
	@SuppressWarnings("deprecation")
	public static void main(String args[]) {

		JFrame test = new JFrame("Test for ViewHeadData1");
		test.setContentPane(new BHScenarioHeadForm());
		test.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		test.pack();
		test.show();
	}
}
