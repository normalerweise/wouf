package org.bh.gui.swing.forms;

import javax.swing.JPanel;

import org.bh.data.DTOPeriod;
import org.bh.gui.swing.comp.BHDescriptionLabel;
import org.bh.gui.swing.comp.BHTextField;
import org.bh.platform.Services;
import org.bh.platform.i18n.ITranslator;
import org.bh.validation.VRMandatory;
import org.bh.validation.ValidationRule;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * This class contains the form for head-data
 * 
 * @author Anton Kharitonov
 * @author Patrick Heinz
 * @version 0.1, 13.01.2010
 * 
 */
@SuppressWarnings("serial")
public final class BHPeriodHeadForm extends JPanel {

	private BHDescriptionLabel lperiodname;
	private BHTextField tfperiodname;

	ITranslator translator = Services.getTranslator();

	public BHPeriodHeadForm() {
		this.initialize();
	}
		
	public void initialize(){

		String rowDef = "4px,p,4px";
		String colDef = "4px,right:pref,4px,max(150px;pref):grow(0.3),4px:grow";

		FormLayout layout = new FormLayout(colDef, rowDef);
		this.setLayout(layout);

		CellConstraints cons = new CellConstraints();

		this.add(this.getLperiodname(), cons.xywh(2, 2, 1, 1));
		this.add(this.getTfperiodname(), cons.xywh(4, 2, 1, 1));

	}
	
	
	public BHDescriptionLabel getLperiodname() {
		if (lperiodname == null) {
			lperiodname = new BHDescriptionLabel(DTOPeriod.Key.NAME);
			lperiodname.setVisible(true);
		}
		return lperiodname;
	}

	/**
	 * Getter method for component tfprojectname.
	 * 
	 * @return BHTextField
	 */
	public BHTextField getTfperiodname() {
		if (tfperiodname == null) {
			tfperiodname = new BHTextField(DTOPeriod.Key.NAME, false);
			ValidationRule[] rules = { VRMandatory.INSTANCE };
			tfperiodname.setValidationRules(rules);
		}
		return tfperiodname;
	}


}
