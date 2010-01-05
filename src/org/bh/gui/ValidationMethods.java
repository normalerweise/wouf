package org.bh.gui;

import java.util.Iterator;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.bh.gui.swing.BHTextField;
import org.bh.gui.swing.IBHComponent;
import org.bh.gui.swing.IBHModelComponent;
import org.bh.platform.Services;
import org.bh.platform.i18n.ITranslator;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.ValidationUtils;
import com.jgoodies.validation.view.ValidationComponentUtils;

/**
 * This class contains the validation rules for all platform panels
 * 
 * @author Patrick Heinz
 * @version 0.3, 30.12.2009
 * 
 */

public class ValidationMethods extends BHValidityEngine {

	ITranslator translator = Services.getTranslator();

	public static final int isMandatory = 1;
	public static final int isDouble = 2;
	public static final int isInteger = 3;
	public static final int isPositive = 4;
	public static final int isNegative = 5;
	public static final int isNotZero = 6;
	public static final int isBetween0and100 = 7;

	@Override
	public ValidationResult validate(IBHModelComponent comp) throws ViewException {
		
		ValidationResult validationResult = null;

		if (comp instanceof JTextField || comp instanceof BHTextField) {
			BHTextField tf_toValidate = (BHTextField) comp;

			validationResult = new ValidationResult();

			int[] allValidationRules = tf_toValidate.getValidateRules();

			int validateRule;

			// check and handle all validation rules a textfield has
			for (int j = 0; j < allValidationRules.length; j++) {

				validateRule = allValidationRules[j];

				String valueString;
				double value;

				switch (validateRule) {

				// checks if a textfield is mandatory and not filled
				case isMandatory:
					if (ValidationComponentUtils.isMandatoryAndBlank(tf_toValidate)) {
						validationResult.addError(translator.translate("Efield")
								+ translator.translate(tf_toValidate.getKey())
								+ translator.translate("EisMandatory"));
						ValidationComponentUtils.setErrorBackground(tf_toValidate);
					}
					break;

				// checks if a textfield requires a double value
				case isDouble:
					valueString = tf_toValidate.getText();
					valueString.replace(',', '.');
					try {
						value = Double.valueOf(Double.parseDouble(valueString));
					} catch (NumberFormatException nfe) {
						validationResult.addError(translator.translate("Efield")
								+ translator.translate(tf_toValidate.getKey())
								+ translator.translate("EisDouble"));
						ValidationComponentUtils.setErrorBackground(tf_toValidate);
					}
					break;

				// checks if a textfield requires an integer value
				case isInteger:
					if (ValidationUtils.isNumeric(tf_toValidate.getText()) == false) {
						validationResult.addError(translator.translate("Efield")
								+ translator.translate(tf_toValidate.getKey())
								+ translator.translate("EisInteger"));
						ValidationComponentUtils.setErrorBackground(tf_toValidate);
					}
					break;

				// checks if a textfield requires only positive values
				case isPositive:
					valueString = tf_toValidate.getText();
					value = Double.parseDouble(valueString);
					if (value <= 0) {
						validationResult.addError(translator.translate("Efield")
								+ translator.translate(tf_toValidate.getKey())
								+ translator.translate("EisPositive"));
						ValidationComponentUtils.setErrorBackground(tf_toValidate);
					}
					break;

				// checks if a textfield requires only negative values
				case isNegative:
					valueString = tf_toValidate.getText();
					value = Double.parseDouble(valueString);
					if (value >= 0) {
						validationResult.addError(translator.translate("Efield")
								+ translator.translate(tf_toValidate.getKey())
								+ translator.translate("EisNegative"));
						ValidationComponentUtils.setErrorBackground(tf_toValidate);
					}
					break;

				// checks if a textfield mustn't be zero
				case isNotZero:
					valueString = tf_toValidate.getText();
					value = Double.parseDouble(valueString);
					if (value == 0) {
						validationResult.addError(translator.translate("Efield")
								+ translator.translate(tf_toValidate.getKey())
								+ translator.translate("EisNotZero"));
						ValidationComponentUtils.setErrorBackground(tf_toValidate);
					}
					break;

				// checks if a textfield's value is between 0 and 100
				case isBetween0and100:
					valueString = tf_toValidate.getText();
					value = Double.parseDouble(valueString);
					if (value < 0 || value > 100) {
						validationResult.addError(translator.translate("Efield")
								+ translator.translate(tf_toValidate.getKey())
								+ translator.translate("EisBetween0and100"));
						ValidationComponentUtils.setErrorBackground(tf_toValidate);
					}
					break;
				
				// The textfield does not have one of the rules above
				default:
					System.out.println(translator.translate("EnoValidationRulesFound"));
				}
			}
		}
		else { // (!(comp instanceof JTextField) || (comp instanceof BHTextField))
			try {
				validationResult = new ValidationResult();
			} catch (Exception e) {
				throw new ViewException(translator.translate("ExtypecastBHTextfieldFailed"));
			}
		}
		return validationResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ValidationResult validateAll(Map<String, IBHModelComponent> toValidate)
			throws ViewException {

		ValidationResult validationResultAll = new ValidationResult();

		int mapsize = toValidate.size();
		Iterator<?> iterator = toValidate.entrySet().iterator();

		for (int i = 0; i < mapsize; i++) {

			Map.Entry entry = (Map.Entry) iterator.next();
			IBHModelComponent tf_toValidate = (IBHModelComponent) entry.getValue();

			ValidationResult validationResultSingle = validate(tf_toValidate);

			validationResultAll.addAllFrom(validationResultSingle);
		}
		return validationResultAll;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void registerComponents(Map<String, IBHModelComponent> toValidate)
			throws ViewException {

		int mapsize = toValidate.size();
		Iterator<?> iterator = toValidate.entrySet().iterator();

		for (int i = 0; i < mapsize; i++) {

			Map.Entry entry = (Map.Entry) iterator.next();
			
			// TODO check why blue border disappears using if below
//			if (entry instanceof JTextField || entry instanceof BHTextField) {
				BHTextField tf_toValidate = (BHTextField) entry.getValue();

				// add some kind of tooltipp to textfield
				ValidationComponentUtils.setInputHint(tf_toValidate,
						tf_toValidate.getInputHint());

				int[] allValidationRules = tf_toValidate.getValidateRules();

				int validateRule;

				// check if a textfield has the rule isMandatory
				for (int j = 0; j < allValidationRules.length; j++) {

					validateRule = allValidationRules[j];

					if (validateRule == isMandatory) {
						// set textfield mandatory and highlight it with a blue border
						ValidationComponentUtils.setMandatory(tf_toValidate, true);
						ValidationComponentUtils.setMandatoryBorder(tf_toValidate);
						// TODO check if after "break" only the inner for-loop is left
						break;
					}
				}
//			}
		}
	}

	
	// TODO Remove methods below, when everything is working
	
	public static void setInputHintLabel(IBHComponent comp) {
		JLabel infoLabel = ((JLabel) ValidationComponentUtils
				.getInputHint((JComponent) comp));
		System.out.println(infoLabel.toString());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void publishValidationAll(Map<String, IBHModelComponent> toValidate)
			throws ViewException {
		ValidationResult validationResultAll = validateAll(toValidate);
		setValidityStatus(validationResultAll);
		JScrollPane pane = createValidationResultList(validationResultAll);
		int counter = pane.countComponents();
		for (int i = 0; i < counter; i++) {
			System.out.println(pane.getNextFocusableComponent().toString());
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void publishValidationComp(IBHModelComponent comp)
			throws ViewException {
		ValidationResult valRes = validate(comp);
		JScrollPane pane = createValidationResultList(valRes);
		int counter = pane.countComponents();
		for (int i = 0; i < counter; i++) {
			System.out.println(pane.getNextFocusableComponent().toString());
		}
	}
}