package org.bh.gui.swing;

import javax.swing.JTextField;
import org.bh.platform.Services;


/**
 * BHTextField to display simple input fields at screen.
 * 
 * <p>
 * This class extends the Swing <code>JTextField</code> to display simple input
 * fields in Business Horizon.
 * 
 * @author Thiele.Klaus
 * @version 0.1, 2009/12/13
 * 
 */
public class BHTextField extends JTextField implements IBHComponent {
	/**
	 * unique key to identify Label.
	 */
	private String key;


	private int[] validateRules;
	private String inputHint;

	/**
	 * Constructor to create new <code>BHTextField</code>.
	 * 
	 * @param key
	 *            unique key
	 * @param value
	 *            default value
	 */
	public BHTextField(String key, String value, String inputHint) {
		this(key, value);
		this.setProperties();
		this.inputHint = inputHint;
	}

	/**
	 * Constructor to create new <code>BHTextField</code>. Defined for the use with unkeyed text
	 * 
	 * @param key
	 *            unique key
	 * @param value
	 *            default value
	 */
//	public BHTextField(String key, String value) {
//		super(value);
//		this.setProperties();
//		this.key = key;
//	}

	/**
	 * Constructor to create new <code>BHTextField</code>. with key based text
	 * 
	 * @param key
	 *            unique key
	 */
	public BHTextField(String key) {
//		super(Services.getTranslator().translate(key));
		super();
		this.setProperties();
		this.key = key;
	}

        public BHTextField(String key, String InputHint){
            this(key);
            this.inputHint = InputHint;
        }

	public String getKey() {
		return key;
	}

	public int[] getValidateRules() {
		return validateRules;
	}

	public void setValidateRules(int[] validateRules) {
		this.validateRules = validateRules;
	}

	public boolean isTypeValid() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setInputHint(String inputHint) {
		this.inputHint = inputHint;
	}
	
	public String getInputHint() {
		return this.inputHint;
	}

	/**
	 * set properties of instance.
	 */
	private void setProperties() {
		this.putClientProperty("JComponent.sizeVariant", IBHComponent.MINI); // Minitextfield
	}

}
