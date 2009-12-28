package org.bh.plugin.fte;

import java.util.HashMap;
import java.util.Map;

import org.bh.calculation.IShareholderValueCalculator;
import org.bh.data.DTOPeriod;
import org.bh.data.DTOScenario;
import org.bh.data.types.Calculable;
import org.bh.data.types.DoubleValue;
/**
 * This class provides the functionality to calculate an enterprise value with the
 * Flow to Equity method. 
 * The formula for the enterprise value is </br>
 * UW[t] = (UW[t+1] + FTE[t+1]) / (EKrFTE[t+1] + 1) </br>
 * with:</br>
 * <li> FTE = FCF + TaxShield - Z + DA </li>
 * <li> Z = Interest payment on debt and DA = Debt amortization </li>
 * <li> UW[T] = FTE[T] / EKrFTE[T] </li>
 * @author Sebastian
 * @version 1.0, 25.12.2009
 */
public class FTECalculator implements IShareholderValueCalculator {
	private static final String UNIQUE_ID = "fte";
	private static final String GUI_KEY = "fte";

	@Override
	public Map<String, Calculable[]> calculate(DTOScenario scenario) {
		Calculable[] fcf = new Calculable[scenario.getChildrenSize()];
		Calculable[] fk = new Calculable[scenario.getChildrenSize()];
		int i = 0;
		for (DTOPeriod period : scenario.getChildren()) {
			if (i > 0)
				fcf[i] = period.getFCF();
			fk[i] = period.getLiabilities();
			i++;
		}	
		// Get all needed input parameters for the calculation
		int T = fcf.length - 1;
		
		// Instantiate the result arrays
		Calculable[] uw = new Calculable[fcf.length];
		Calculable ekr = (Calculable)scenario.get(DTOScenario.Key.REK);
		Calculable fkr = (Calculable)scenario.get(DTOScenario.Key.RFK);
		Calculable s = (DoubleValue) scenario.getTax();
		
		
		// Instantiate result Arrays
		Calculable[] FTEInterest = new Calculable[fcf.length]; // Interest payment on debt (Zinszahlung auf FK)
		Calculable[] FTETaxShield = new Calculable[fcf.length]; // Tax benefit from debt (Steuervorteil aus Verschuldung)
		Calculable[] FTE = new Calculable[fcf.length]; // Flow to equity (EK Zugang)
		Calculable[] FTEDebtAmort = new Calculable[fcf.length]; // Debt amortization (Differenz FK[t] zu FK[t-1])
		Calculable[] PresentValueTaxShield = new Calculable[fcf.length];
		Calculable[] EKrFTE = new Calculable[fcf.length];
		
		// Instantiate helper array
		Calculable[] uwLastCalc = new Calculable[fcf.length];
		
		//### Step 1
		// Calculation of FTE
		// FTE = FCF + TaxShield - Z + DA
		// with Z = Interest payment on debt and DA = Debt amortization
		FTEInterest[T] = fkr.mul(fk[T]);
		FTETaxShield[T] = s.mul(fkr).mul(fk[T]);
		FTE[T] = fcf[T].add(FTETaxShield[T]).sub(FTEInterest[T]);
		
		for (int t = T - 1; t >= 1; t--) {
			FTEDebtAmort[t] = fk[t].sub(fk[t - 1]);
			FTEInterest[t] = fkr.mul(fk[t-1]);
			FTETaxShield[t] = s.mul(fkr).mul(fk[t-1]);
			FTE[t] = fcf[t].add(FTETaxShield[t]).sub(FTEInterest[t]).add(FTEDebtAmort[t]);
		}
		
		//### Step 2
		// Calculation of the present value tax shield
		PresentValueTaxShield = calcPresentValueTaxShield(s, fkr, fk, PresentValueTaxShield);
		
		//### Step 3
		// Calculation of enterprise values
		
		// Load dynamic equity rate of return with initial equity rate of return. 
		// One value is needed to start
		for (int j = 0; j < EKrFTE.length; j++) {
			EKrFTE[j] = ekr;
		}
		// Calculation of UW and rEKv with FTE
		boolean isIdentical = false;
		int iterations = 0;
		do {
			for (int k = 0; k < uw.length; k++) {
				uwLastCalc[k] = uw[k];
			}
			// Calculation of the enterprise value
			// UW[T] = FTE[T] / EKrFTE[T]
			uw[T] = FTE[T].div(EKrFTE[T]);
			for (int t = T -1; t >= 0; t--) {
				// UW[t] = (UW[t+1] + FTE[t+1]) / (EKrFTE[t+1] + 1)
				uw[t] = (uw[t + 1].add(FTE[t + 1])).div(EKrFTE[t + 1].add(new DoubleValue(1)));
			}

			EKrFTE = calcVariableEquityReturnRate(s, fkr, fk, ekr, uw, EKrFTE, PresentValueTaxShield);
			
			// Check if former and current values are the same
			isIdentical = false;
			if (uwLastCalc[0] == null || checkAbs(uw[0], uwLastCalc[0])) {
				isIdentical = false;
			} else {
				isIdentical = true;
			}
			iterations++;
			if (iterations > 25000) {
				isIdentical = true;
			}
		} while (!isIdentical);
			
		Map<String, Calculable[]> result = new HashMap<String, Calculable[]>();
		result.put(SHAREHOLDER_VALUE, uw);
		result.put("PRESENT_VALUE_TAX_SHIELD", PresentValueTaxShield);
		result.put("FLOW_TO_EQUITY_INTEREST", FTEInterest);
		result.put("FLOW_TO_EQUITY_TAX_SHIELD", FTETaxShield);
		result.put("FLOW_TO_EQUITY", FTE);
		result.put("DEBT_AMORTISATION", FTEDebtAmort);
		result.put("EQUITY_RETURN_RATE_FTE", EKrFTE);
		return result;
	}

	@Override
	public String getUniqueId() {
		return UNIQUE_ID;
	}

	@Override
	public String getGuiKey() {
		return GUI_KEY;
	}
	
	// Calculate PresentValueTaxShield for endless period
	// PresentValueTaxShield[T] = (s * FKr * FK[T]) / FKr
	private Calculable calcPresentValueTaxShieldEndless(Calculable s, Calculable FKr, Calculable FK){
		return s.mul(FKr).mul(FK).div(FKr);
	}
	
	// Calculate PresentValueTaxShield for finite period
	// PresentValueTaxShield[t] = (PresentValueTaxShield[t + 1] + (s * FKr * FK[t])) / (FKr + 1)
	private Calculable[] calcPresentValueTaxShieldFinite(Calculable s, Calculable FKr, Calculable[] FK, Calculable[] PVTS){
		for (int i = PVTS.length - 2; i >= 0; i--) {
			PVTS[i] = (PVTS[i + 1].add(s.mul(FKr).mul(FK[i + 1 - 1]))).div(FKr.add(new DoubleValue(1)));
		}
		return PVTS;
	}
	/**
	 * Calculates the value of the indebted enterprise. Used in FCF and FTE method.
	 * @param s Taxes
	 * @param FKr Debt rate of return
	 * @param FK Debt
	 * @param PVTS self reference
	 * @return The value of the indebted enterprise for each year
	 */
	private Calculable[] calcPresentValueTaxShield(Calculable s, Calculable FKr, Calculable[] FK, Calculable[] PVTS){
		PVTS[PVTS.length - 1] = calcPresentValueTaxShieldEndless(s, FKr, FK[FK.length - 1]);
		return calcPresentValueTaxShieldFinite(s, FKr, FK, PVTS);
	}
	
	/**
	 * Calculates the variable equity return rate for the endless period. Used in FCF and FTE method.</br>
	 * EKrFCF[T] = EKr + ((EKr - FKr) * (1-s) * (FK[T] / UW[T]))
	 * @param s Taxes
	 * @param FKr Debt rate of return  
	 * @param FK Debt
	 * @param EKr Equity rate of return
	 * @param UW Enterprise value
	 * @return Variable equity return rate for the endless period
	 */
	private Calculable calcVariableEquityReturnRateEndless(Calculable s, Calculable FKr, Calculable FK, Calculable EKr, Calculable UW){
		return EKr.add(((EKr.sub(FKr)).mul(s.mul(new DoubleValue(-1)).add(new DoubleValue(1))).mul(FK.div(UW))));
	}
	/**
	 * Calculates the variable equity return rate for the finite periods. Used in FCF and FTE method.</br>
	 * EKrFCF[t] = EKr + ((EKr - FKr) * ((FK[t -1] - PVTS[t-1]) / UW[t - 1]))
	 * @param s Taxes
	 * @param FKr Debt rate of return
	 * @param FK Debt
	 * @param EKr Equity rate of return
	 * @param UW Enterprise value
	 * @param EKrV self reference
	 * @param PresentValueTaxShield Value of the indebted enterprise
	 * @return Variable equity return rate for the finite periods
	 */
	private Calculable[] calcVariableEquityReturnRateFinite(Calculable s, Calculable FKr, Calculable[] FK, 
																		Calculable EKr, Calculable UW[], Calculable[] EKrV,
																		Calculable[] PresentValueTaxShield){
		for (int t = 1; t < UW.length - 1; t++) {
			EKrV[t] = EKr.add(((EKr.sub(FKr)).mul((FK[t - 1].sub(PresentValueTaxShield[t - 1])).div(UW[t - 1]))));
		}
		return EKrV;
	}
	/**
	 * Calculates the variable equity return rate. Used in FCF and FTE method.
	 * @param s Taxes
	 * @param FKr Debt rate of return
	 * @param FK Debt
	 * @param EKr Equity rate of return
	 * @param UW Enterprise value
	 * @param EKrV self reference
	 * @param PresentValueTaxShield Value of the indebted enterprise
	 * @return Variable equity return rate
	 */
	private Calculable[] calcVariableEquityReturnRate(Calculable s, Calculable FKr, Calculable[] FK, 
																Calculable EKr, Calculable UW[], Calculable[] EKrV,
																Calculable[] PresentValueTaxShield){
		EKrV[EKrV.length - 1] = calcVariableEquityReturnRateEndless(s, FKr, FK[EKrV.length - 1], EKr, UW[EKrV.length - 1]);
		return calcVariableEquityReturnRateFinite(s, FKr, FK, EKr, UW, EKrV, PresentValueTaxShield);
	}
	/**
	 * This method checks whether the input parameters differ from each other more than
	 * 0.0000000001
	 * @param firstValue
	 * @param secondValue
	 * @return <b>true</b> if input parameters differ more<br>
	 * <b>false</b> if input parameters differ less
	 */
	private boolean checkAbs(Calculable firstValue, Calculable secondValue){
		if(firstValue. sub(secondValue).abs().greaterThan(new DoubleValue(0.0000000001))){
			return true;
		}else{
			return false;
		}
	}
}