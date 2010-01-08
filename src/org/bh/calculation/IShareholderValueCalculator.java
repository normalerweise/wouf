package org.bh.calculation;

import java.util.Map;

import org.bh.data.DTOScenario;
import org.bh.data.types.Calculable;
import org.bh.platform.IDisplayablePlugin;

/**
 * This interface is implemented by classes which provide calculation methods
 * for a company's shareholder value.
 * 
 * @author Robert Vollmer
 * @version 1.2, 18.12.2009
 * 
 */
public interface IShareholderValueCalculator extends IDisplayablePlugin {
	/**
	 * Key for {@link #calculate(DTOScenario)} which identifies the shareholder
	 * value.
	 */
	final String SHAREHOLDER_VALUE = "sv";
	final String DEBT = "fk";
	final String FREE_CASH_FLOW = "fcf";

	/**
	 * This method calculates the shareholder value and possibly other values.
	 * 
	 * <p>
	 * The returned map contains at least an entry with the key
	 * {@link #SHAREHOLDER_VALUE}, so the shareholder value can always be
	 * retrieved using
	 * <code>result.get(IShareholderValueCalculator.SHAREHOLDER_VALUE)[0]</code>.
	 * 
	 * @param scenario
	 *            The DTO of the scenario.
	 * @return A map with the results. Values which do not belong to a specific
	 *         value are stored in an array with one element.
	 */
	Map<String, Calculable[]> calculate(DTOScenario scenario);

	/**
	 * Defines a unique string which identifies this calculation method.
	 * 
	 * @return The unique ID.
	 */
	String getUniqueId();
}
