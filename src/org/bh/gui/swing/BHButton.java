package org.bh.gui.swing;

import javax.swing.JButton;

/**
 * BHButton to display buttons at screen.
 * 
 * <p>
 * This class extends the Swing <code>JButton</code> to display simple buttons
 * in Business Horizon.
 * 
 * @author Thiele.Klaus
 * @version 0.1, 2009/12/13
 * 
 */
public class BHButton extends JButton implements IBHComponent{

    private String key;
    private int[] validateRules;

    public BHButton(String key){
        super();
        this.key = key;
    }

    /**
     * set the rules for the JGoodies validation
     * @param validateRules
     */
    public void setValidateRules(int[] validateRules){
        this.validateRules = validateRules;
    }
    /**
     * return the key for value mapping
     * @return
     */
    public String getKey() {
        return key;
    }
    /**
     * return the rules for the validation engine
     * @return
     */
    public int[] getValidateRules() {
        return validateRules;
    }

    public boolean isTypeValid() {
        throw new UnsupportedOperationException("Not supported yet.");
    }



}
