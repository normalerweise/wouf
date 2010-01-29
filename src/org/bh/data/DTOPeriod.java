package org.bh.data;

import java.util.ServiceLoader;

import org.apache.log4j.Logger;
import org.bh.calculation.ICalculationPreparer;
import org.bh.data.types.Calculable;
import org.bh.platform.PluginManager;

/**
 * Period DTO
 * 
 * <p>
 * This DTO contains period data.
 * 
 * @author Michael Löckelt
 * @version 0.2, 16.12.2009
 * 
 */

public class DTOPeriod extends DTO<IPeriodicalValuesDTO> {
	private static final long serialVersionUID = 1576283051584502782L;
	private static final Logger log = Logger.getLogger(DTOPeriod.class);
	
	public enum Key {
		/**
		 * identify the position of this period
		 * for example a year or a quarter
		 */
		NAME,
		
		/**
		 * total liabilities
		 */
		@Method LIABILITIES,
		
		/**
		 * FreeCashFlow
		 */
		@Method FCF;
		
		@Override
		public String toString() {
			return getClass().getName() + "." + super.toString();
		}	
	}
	
	DTOPeriod previous = null;
	DTOPeriod next = null;
	DTOScenario scenario = null;
	
    /**
     * initialize key and method list
     */
	public DTOPeriod() {
		super(Key.class);
		log.debug("Object created!");
	}
	
	/**
	 * Get or calculate the liabilities for this period.
	 * @return
	 */
	public Calculable getLiabilities() {
		Calculable result = null;
		ServiceLoader<ICalculationPreparer> preparers = PluginManager.getInstance().getServices(ICalculationPreparer.class);
		for (ICalculationPreparer preparer : preparers) {
			result = preparer.getLiabilities(this);
			if (result != null)
				break;
		}
		if (result == null) {
			throw new DTOAccessException("Cannot calculate liabilities");
		}
		return result;
	}
	
	/**
	 * Get or calculate the FCF for this period.
	 * @return
	 */
	public Calculable getFCF() {
		Calculable result = null;
		ServiceLoader<ICalculationPreparer> preparers = PluginManager.getInstance().getServices(ICalculationPreparer.class);
		for (ICalculationPreparer preparer : preparers) {
			result = preparer.getFCF(this);
			if (result != null)
				break;
		}
		if (result == null) {
			throw new DTOAccessException("Cannot calculate FCF");
		}
		return result;
	}

	/**
	 * Get the DTO for the previous period.
	 * @return DTO for the previous period.
	 */
	public DTOPeriod getPrevious() {
		return previous;
	}

	/**
	 * Get the DTO for the following period.
	 * @return DTO for the following period.
	 */
	public DTOPeriod getNext() {
		return next;
	}

	/**
	 * Returns the first matching DTO with periodical values.
	 * @param uniqueId Type of the DTO.
	 * @return The DTO or null if none could be found.
	 */
	public IPeriodicalValuesDTO getPeriodicalValuesDTO(String uniqueId) {
		for (IPeriodicalValuesDTO child : children) {
			if (child.getUniqueId().equals(uniqueId))
				return child;
		}
		return null;
	}
	
	/**
	 * Get taxes for scenario.
	 * @return Taxes for scenario.
	 */
	public Calculable getTax() {
		return scenario.getTax();
	}
	
	public DTOScenario getScenario() {
		return scenario;
	}	
}
