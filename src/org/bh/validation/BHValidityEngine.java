/*******************************************************************************
 * Copyright 2011: Matthias Beste, Hannes Bischoff, Lisa Doerner, Victor Guettler, Markus Hattenbach, Tim Herzenstiel, Günter Hesse, Jochen Hülß, Daniel Krauth, Lukas Lochner, Mark Maltring, Sven Mayer, Benedikt Nees, Alexandre Pereira, Patrick Pfaff, Yannick Rödl, Denis Roster, Sebastian Schumacher, Norman Vogel, Simon Weber 
 *
 * Copyright 2010: Anna Aichinger, Damian Berle, Patrick Dahl, Lisa Engelmann, Patrick Groß, Irene Ihl, Timo Klein, Alena Lang, Miriam Leuthold, Lukas Maciolek, Patrick Maisel, Vito Masiello, Moritz Olf, Ruben Reichle, Alexander Rupp, Daniel Schäfer, Simon Waldraff, Matthias Wurdig, Andreas Wußler
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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bh.validation;

import java.awt.Component;
import java.util.Collection;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.text.JTextComponent;

import org.apache.log4j.Logger;
import org.bh.gui.IBHComponent;
import org.bh.gui.IBHModelComponent;
import org.bh.gui.swing.BHStatusBar;
import org.bh.gui.swing.misc.Icons;
import org.bh.gui.view.ViewException;
import org.bh.platform.Services;

import com.jgoodies.validation.ValidationMessage;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.view.ValidationComponentUtils;
import com.jgoodies.validation.view.ValidationResultViewFactory;

/**
 *
 * @author Marco Hammel
 */
public abstract class BHValidityEngine {

    private static Logger log = Logger.getLogger(BHValidityEngine.class);

    /**
     * flag for the validation of a complete UI instance
     */
    private boolean isValid = false;
    /**
     * keep the result from validateAll
     */
    private ValidationResult validationResultAll;

    /**
     * set boolean <code>isValid</code> wheater the last validationAll has an error or warning
     * 
     * @param validation    as Result of a Validation
     * @see ValidationResult
     */
    public void setValidityStatus(ValidationResult validation) {
        if (validation.hasMessages()) {
            log.debug("validation has errors or warnings");
            this.isValid = false;
        } else {
            this.isValid = true;
        }
    }

    /**
     * deliver the flag whether a UI is valid or not
     *
     * @return  boolean true is valid false --> isn´t proofed yet, is not valid
     */
    public boolean isValid() {
        if(validationResultAll != null){
            setValidityStatus(validationResultAll);
            return this.isValid;
        }else{
            return false;
        }
        
    }
    /**
     * creates the JScroll Pane with validation messages
     *
     * @param validationResult
     * @return  JScrollPane
     * @see JScrollPane
     */
    public JScrollPane createValidationResultList(ValidationResult validationResult) {
        log.debug("JScrollPane is build");
        ValidationResultModel validationResultModel = new DefaultValidationResultModel();
        validationResultModel.setResult(validationResult);

        JScrollPane resultList = (JScrollPane) ValidationResultViewFactory.createReportList(validationResultModel);
        JList list = (JList) resultList.getViewport().getView();
        list.setCellRenderer(new BHValidationMessageCellRenderer());
        return resultList;
    }
    
    @SuppressWarnings("serial")
    protected static class BHValidationMessageCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
            super.getListCellRendererComponent(
                list,
                value,
                index,
                false,  // Ignore the selection state
                false); // Ignore the cell focus state
            ValidationMessage message = (ValidationMessage) value;
            this.setIcon(Icons.getIcon(message.severity()));
            this.setText(message.formattedText());
            return this;
        }
    }
    
    /**
     * run a validation and deliver the Result to the BHStatusBar
     *
     * @param toValidate IBHComponents which have to be validated as a Map
     * @throws ViewException 
     * @see IBHComponent
     * @see BHStatusBar
     */
    public ValidationResult publishValidationAll(Map<String, IBHModelComponent> toValidate) {
    	log.debug("Trigger validation process for All Components");
        validationResultAll = validateAll(toValidate);
        setValidityStatus(validationResultAll);
        if (validationResultAll.hasMessages())
        	Services.getBHstatusBar().setErrorHint(createValidationResultList(validationResultAll));
        else
        	Services.getBHstatusBar().removeErrorHint();
        return validationResultAll;
    }
    /**
     * set the messages of the validation of a single component to the BHStatusBar
     *
     * @param comp
     * @throws ViewException
     * @see BHStatusBar
     */
    public ValidationResult publishValidationComp(IBHModelComponent comp) {
        log.debug("Trigger validation for a single component with key " + comp.getKey());
        ValidationResult valRes = validate(comp);
        if (valRes.hasMessages())
        	Services.getBHstatusBar().setErrorHint(createValidationResultList(valRes));
        else
        	Services.getBHstatusBar().removeErrorHint();
        return valRes;
    }
    /**
     * have to register the model related components and set the ValidationComponentUtils entries
     *
     * @param toValidate a Collection of IBHComponents
     * @see ValidationComponentUtils
     */
    public abstract void registerComponents(Collection<IBHModelComponent> toValidate) throws ViewException;
   
    /**
     * Shell proof the constant based rules of a single component
     *
     * @param comp single IBHComponent
     * @return the result of the Validation as ValidationResult
     * @see ValidationResult
     */
	public ValidationResult validate(IBHModelComponent comp) {
		if (comp instanceof Component && !((Component) comp).isVisible())
			return ValidationResult.EMPTY;
		
		ValidationResult validationResult = new ValidationResult();
		for (ValidationRule validationRule : comp.getValidationRules()) {
			validationResult.addAllFrom(validationRule.validate(comp));
		}
		if (comp instanceof JTextComponent) {
			if (validationResult.hasMessages())
				ValidationComponentUtils.setErrorBackground((JTextComponent)comp);
			else
				((JTextComponent)comp).setBackground(null);
		}
		return validationResult;
	}

    /**
     * Shell proof the components and can also proof related conditions between
     * components
     *
     * @param toValidate
     * @return the result of the Validation as ValidationResult
     * @see ValidationResult
     */
	public ValidationResult validateAll(Map<String, IBHModelComponent> toValidate) {
		ValidationResult validationResultAll = new ValidationResult();
		for (Map.Entry<String, IBHModelComponent> entry : toValidate.entrySet()) {
			ValidationResult validationResultSingle = validate(entry.getValue());
			validationResultAll.addAllFrom(validationResultSingle);
		}
		return validationResultAll;
	}
}
