package org.bh.plugin.gcc.data;

import org.apache.log4j.Logger;
import org.bh.data.DTO;
import org.bh.data.IPeriodicalValuesDTO;
import org.bh.data.types.Calculable;

/**
 * GCCProfitLossStatementCostOfSales DTO.
 * 
 * <p>
 * Data Transfer Object to handle GCCProfitLossStatementCostOfSales values and
 * methods
 * 
 * @author Michael Löckelt
 * @version 0.2, 16.12.2009
 * 
 */

@SuppressWarnings("unchecked")
public class DTOGCCProfitLossStatementCostOfSales extends DTO implements
		IPeriodicalValuesDTO {
	private static final long serialVersionUID = 5411394776750186305L;
	private static final String UNIQUE_ID = "gcc_pls_costofsales";
	private static final Logger log = Logger.getLogger(DTOGCCProfitLossStatementCostOfSales.class);

	public enum Key {

		/**
		 * (+) Umsatzerlöse
		 */
		@Stochastic
		UE,

		/**
		 * (-) Herstellkosten der zur Erzielung der Umsatzerlöse erbrachten
		 * Leistungen
		 */
		@Stochastic
		HK,

		/**
		 * (=) Bruttoergebnis vom Umsatz
		 */
		@Method
		BUM,

		/**
		 * (-) Vertriebskosten
		 */
		VERTK,

		/**
		 * (-) Verwaltungskosten
		 */
		@Stochastic
		VERWK,

		/**
		 * (+) sonstige betriebliche Erträge
		 */
		@Stochastic
		SBE,

		/**
		 * (-) sonstige betriebliche Aufwendungen
		 */
		SBA,

		/**
		 * (=) Betriebsergebnis
		 */
		@Method
		BERG,

		/**
		 * (+) Erträge aus Beteiligungen
		 */
		EBET,

		/**
		 * (+) Erträge aus anderen Wertpapieren und Ausleihungen an
		 * Finanzanlagevermögens
		 */
		EWAF,

		/**
		 * (+) sonstige Zinsen und ähnliche Erträge
		 */
		SZAE,

		/**
		 * (-) Abschreibungen auf Finanzanlagen und übliche Abschreibungen auf
		 * Wertpapiere des Umalufvermögens
		 */
		AFW,

		/**
		 * (-) Zinsen und ähnliche Aufwendungen
		 */
		ZA,

		/**
		 * (=) Finanzergebnis / Ergebnis der gewöhnlichen Geschäftstätigkeit
		 */
		@Method
		EGG,

		/**
		 * (+) außerordentliche Erträge
		 */
		AUE,

		/**
		 * (-) außerordentliche Aufwendungen
		 */
		AUA,

		/**
		 * (=) außerordentliches Ergebnis
		 */
		@Method
		AUERG,

		/**
		 * (-) Steuern vom Einkommen und vom Ertrag
		 */
		SEE,

		/**
		 * (-) sonstige Steuern (insofern erfolgswirksam)
		 */
		SS,

		/**
		 * (=) Jahresüberschuss / Jahresfehlbetrag
		 */
		@Method
		JUJF,
		
		/**
		 * Vertriebskosten + allg. Verwaltungskosten +sonstige betriebliche Aufwendungen
		 * (von der BWL Gruppe als extra Feld gewünscht)
		 */
		VVSBA;
		
		@Override
		public String toString() {
			return getClass().getName() + "." + super.toString();
		}	

	}

	public DTOGCCProfitLossStatementCostOfSales() {
		super(Key.class);
		log.debug("Object created!");
	}

	@Override
	public String getUniqueId() {
		return UNIQUE_ID;
	}

	/**
	 * intermediate result: gross profit on sales
	 * 
	 * @return Calculable
	 */
	public Calculable getBUM() {
		return null;
	}

	/**
	 * intermediate result: operating income
	 * 
	 * @return Calculable
	 */
	public Calculable getBERG() {
		return null;
	}

	/**
	 * intermediate result: result of ordinary activities
	 * 
	 * @return Calculable
	 */
	public Calculable getEGG() {
		return null;
	}

	/**
	 * intermediate result: extraordinary result
	 * 
	 * @return Calculable
	 */
	public Calculable getAUERG() {
		return null;
	}

	/**
	 * intermediate result: net income
	 * 
	 * @return Calculable
	 */
	public Calculable getJUJF() {
		return null;
	}

	public static String getUniqueIdStatic() {
		return UNIQUE_ID;
	}

}
