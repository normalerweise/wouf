package org.bh.gui.swing;

import java.awt.Component;

import org.bh.data.types.IValue;
import org.bh.gui.BHValidityEngine;

/**
 * Every Swing element which shall use a defined DTO key must implement this
 * interface.
 * 
 * @author Marco Hammel
 * @author Thiele.Klaus
 * 
 * @version 0.2, 2009/12/25
 */
public interface IBHComponent {

	/**
	 * Constant can be use to check objects.
	 */
	Boolean ISBHCOMPONENT = true;
        /**
         * Have to be used by instancing a BHComponent representing a minimum
         * of a intervall
         */
        String MINVALUE = "MIN_";
        /**
         * Have to be used by instancing a BHComponent representing a maximum
         * of a intervall
         */
        String MAXVALUE = "MAX_";

	/**
	 * Constant for Nimbus Look&Feel: Size for JComponent is large
	 */
	String LARGE = "large";

	/**
	 * Constant for Nimbus Look&Feel: Size for JComponent is regular
	 */
	String REGULAR = "regular";

	/**
	 * Constant for Nimbus Look&Feel: Size for JComponent is small
	 */
	String SMALL = "small";

	/**
	 * Constant for Nimbus Look&Feel: Size for JComponent is mini
	 */
	String MINI = "mini";

	/**
	 * No check for the DTO constants USe the DTO Enums to be conform
	 * 
	 * @return value of a DTO key
	 */
	public String getKey();

	/**
	 * Number of rules and the rules itself are platform independent But shall
	 * be consistent in every plugin by using one Validity Engine per plugin
	 * 
	 * @return amount of rules defined in a subclass of BHValidity engine
	 * @see BHValidityEngine
	 */
	public int[] getValidateRules();

	/**
	 * can set the Rules for the validation by runtime;
	 * 
	 * @param validateRules
	 */
	public void setValidateRules(int[] validateRules);

	/**
	 * you have to override the add method and implement a put into the map for
	 * 
	 * @param comp
	 * @return
	 */
	public Component add(Component comp);

	/**
	 * Must return the Input Hint text of a component
	 * 
	 * @return
	 */
	public String getInputHint();
}
