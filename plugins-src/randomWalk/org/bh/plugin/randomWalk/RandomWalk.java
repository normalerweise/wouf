package org.bh.plugin.randomWalk;

import java.awt.Component;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.apache.log4j.Logger;
import org.bh.calculation.IShareholderValueCalculator;
import org.bh.calculation.IStochasticProcess;
import org.bh.controller.Controller;
import org.bh.data.DTOKeyPair;
import org.bh.data.DTOPeriod;
import org.bh.data.DTOScenario;
import org.bh.data.IPeriodicalValuesDTO;
import org.bh.data.types.Calculable;
import org.bh.data.types.DistributionMap;
import org.bh.data.types.DoubleValue;
import org.bh.data.types.IValue;
import org.bh.data.types.IntegerValue;
import org.bh.data.types.StringValue;
import org.bh.gui.swing.BHLabel;
import org.bh.gui.swing.BHTextField;
import org.bh.platform.Services;
import org.bh.platform.i18n.ITranslator;
import org.bh.plugin.directinput.DTODirectInput;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * This class provides the functionality to process the Random Walk on every value which
 * should be determined stochastically.
 * 
 * @author Sebastian
 * @version 0.1, 04.01.2010
 *
 */
public class RandomWalk implements IStochasticProcess {
	private static final String INCREMENT = "increment";
	private static final String CHANCE = "chance";
	private static final String REPETITIONS = "repetitions";
	private static final String STEPS_PER_PERIOD = "stepsPerPeriod";
	private static final String AMOUNT_OF_PERIODS = "amountOfPeriods";

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(RandomWalk.class);

	private static final String UNIQUE_ID = "randomWalk";
	private static final String GUI_KEY = "randomWalk";

	private DTOScenario scenario;
	private JPanel panel;
	private HashMap<String, IValue> internalMap;
	private HashMap<String, Integer> map;

	@Override
	public DistributionMap calculate() {

		DistributionMap result = new DistributionMap(1);
		DTOPeriod last = scenario.getLastChildren();
		List<DTOKeyPair> stochasticKeys = scenario.getPeriodStochasticKeys();
		// List<String> stochasticKeys = last.getStochasticKeys();

		for (int j = 0; j < map.get(REPETITIONS); j++) {
			DTOScenario temp = new DTOScenario(true);
			temp.put(DTOScenario.Key.REK, scenario.get(DTOScenario.Key.REK));
			temp.put(DTOScenario.Key.RFK, scenario.get(DTOScenario.Key.RFK));
			temp.put(DTOScenario.Key.BTAX, scenario.get(DTOScenario.Key.BTAX));
			temp.put(DTOScenario.Key.CTAX, scenario.get(DTOScenario.Key.CTAX));

			DTOPeriod clone = last.clone();
			DTOPeriod previous = last.getPrevious();

			for (DTOKeyPair key : stochasticKeys) {
				IPeriodicalValuesDTO pvdto = clone.getPeriodicalValuesDTO(key
						.getDtoId());
				pvdto.put(key.getKey(), this.doOneRandomWalk(previous
						.getCalculable(key.getKey()),
						map.get(STEPS_PER_PERIOD),
						(Calculable) this.internalMap
								.get(key.getKey() + CHANCE),
						(Calculable) this.internalMap.get(key.getKey()
								+ INCREMENT)));
			}
			temp.addChild(clone);

			for (int i = 0; i < map.get(AMOUNT_OF_PERIODS) - 1; i++) {
				previous = clone;
				clone = last.clone();
				for (DTOKeyPair key : stochasticKeys) {
					IPeriodicalValuesDTO pvdto = clone
							.getPeriodicalValuesDTO(key.getDtoId());
					pvdto.put(key.getKey(), this.doOneRandomWalk(previous
							.getCalculable(key.getKey()), map
							.get(STEPS_PER_PERIOD),
							(Calculable) this.internalMap.get(key.getKey()
									+ CHANCE), (Calculable) this.internalMap
									.get(key.getKey() + INCREMENT)));
				}
				temp.addChild(clone);
			}

			result
					.put(((DoubleValue) Services.getDCFMethod("apv").calculate(
							temp).get(
							IShareholderValueCalculator.SHAREHOLDER_VALUE)[0])
							.getValue());
		}
		return result;
	}

	@Override
	public JPanel calculateParameters() {
		ITranslator translator = Controller.getTranslator();
		internalMap = new HashMap<String, IValue>();
		map = new HashMap<String, Integer>();

		TreeMap<DTOKeyPair, List<Calculable>> toBeDetermined = scenario
				.getPeriodStochasticKeysAndValues();

		if (toBeDetermined.isEmpty())
			return null;
		else {
			JPanel result = new JPanel();

			String rowDef = "4px,p,4px,p,4px,p,4px,p,4px";
			String colDef = "4px,right:pref,4px,60px:grow,8px:grow,right:pref,4px,max(35px;pref):grow,4px:grow";
			FormLayout layout = new FormLayout(colDef, rowDef);
			result.setLayout(layout);
			layout.setColumnGroups(new int[][] {{4,8}});
			CellConstraints cons = new CellConstraints();
			
			result.add(new BHLabel(translator.translate(AMOUNT_OF_PERIODS)), cons.xywh(2, 2, 1, 1));
			BHTextField tf = new BHTextField(AMOUNT_OF_PERIODS);
			int[] rule = { ValidationRandomWalk.isMandatory,
					ValidationRandomWalk.isInteger,
					ValidationRandomWalk.isPositive };
			tf.setValidateRules(rule);
			result.add(tf, cons.xywh(4, 2, 1, 1));
			map.put(AMOUNT_OF_PERIODS, new Integer(5));

			result.add(new BHLabel(translator.translate(STEPS_PER_PERIOD)), cons.xywh(2, 4, 1, 1));
			BHTextField tf1 = new BHTextField(STEPS_PER_PERIOD);
			int[] rule1 = { ValidationRandomWalk.isMandatory,
					ValidationRandomWalk.isInteger,
					ValidationRandomWalk.isPositive };
			tf1.setValidateRules(rule1);
			result.add(tf1, cons.xywh(4, 4, 1, 1));
			map.put(STEPS_PER_PERIOD, new Integer(250 / 5));

			result.add(new BHLabel(translator.translate(REPETITIONS)), cons.xywh(6, 2, 1, 1));
			BHTextField tf2 = new BHTextField(REPETITIONS);
			int[] rule2 = { ValidationRandomWalk.isMandatory,
					ValidationRandomWalk.isInteger,
					ValidationRandomWalk.isPositive };
			tf2.setValidateRules(rule2);
			result.add(tf2, cons.xywh(8, 2, 1, 1));
			map.put(REPETITIONS, new Integer(100000));
			
			result.add(new JSeparator(), cons.xywh(2, 8, 7, 1));

			for (Entry<DTOKeyPair, List<Calculable>> e : toBeDetermined
					.entrySet()) {
				String key = e.getKey().getKey();
				Calculable chance = calcChance(e.getValue());
				Calculable increment = calcIncrement(e.getValue());
				
				layout.appendRow(RowSpec.decode("p"));
				layout.appendRow(RowSpec.decode("4px"));

				result.add(new BHLabel(translator.translate(key)), cons.xywh(2, layout.getRowCount()-1, 1, 1));

				layout.appendRow(RowSpec.decode("p"));
				layout.appendRow(RowSpec.decode("14px"));
				
				result.add(new BHLabel(translator.translate(CHANCE)), cons.xywh(2, layout.getRowCount()-1, 1, 1));
				result.add(new BHTextField(key + CHANCE, "" + chance), cons.xywh(4, layout.getRowCount()-1, 1, 1));
				result.add(new BHLabel(translator.translate(INCREMENT)), cons.xywh(6, layout.getRowCount()-1, 1, 1));
				result
						.add(new BHTextField(key + INCREMENT, "" + increment), cons.xywh(8, layout.getRowCount()-1, 1, 1));

				internalMap.put(key + CHANCE, chance);
				internalMap.put(key + INCREMENT, increment);
			}
			this.panel = result;
			return result;
		}
	}

	@Override
	public Map<String, IValue> getParametersForAnalysis() {
		return internalMap;
	}

	@Override
	public void setScenario(DTOScenario scenario) {
		this.scenario = scenario;
	}

	@Override
	public void updateParameters() {
		Component[] components = panel.getComponents();
		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof BHTextField) {
				BHTextField c = (BHTextField) components[i];
				String key = c.getKey();
				String text = c.getText();
				if (internalMap.containsKey(key))
					internalMap.put(key, Calculable.parseCalculable(text));
				else
					map.put(key, Integer.parseInt(text));
			}
		}
	}
	
	@Override
	public IStochasticProcess createNewInstance() {
		return new RandomWalk();
	}

	@Override
	public String getUniqueId() {
		return UNIQUE_ID;
	}

	@Override
	public String getGuiKey() {
		return GUI_KEY;
	}

	private Calculable calcIncrement(List<Calculable> inputValues) {
		Calculable sum = new DoubleValue(0);
		int i = 0;
		Calculable old = null;
		for (Calculable c : inputValues) {
			if (i > 0) {
				sum = sum.add(c.sub(old).abs());
			}
			old = c;
			i++;
		}
		Calculable result = sum.div(new DoubleValue(inputValues.size() - 1));
		// System.out.println("Increment: " + result);
		return result;
	}

	private Calculable calcChance(List<Calculable> inputValues) {
		double sum = 0;
		int i = 0;
		Calculable old = null;
		for (Calculable c : inputValues) {
			if (i > 0) {
				if (old.lessThan(c))
					sum++;
			}
			old = c;
			i++;
		}
		Calculable result = new DoubleValue(sum / (inputValues.size() - 1));
		// System.out.println("Chance: " + result);
		return result;
	}

	private Calculable doOneRandomWalk(Calculable lastValue, int amountOfSteps,
			Calculable chance, Calculable increment) {
		Calculable inc = increment.div(new IntegerValue(amountOfSteps));
		Calculable value = lastValue;
		for (int i = 1; i <= amountOfSteps; i++) {
			if (new DoubleValue(Math.random()).lessThan(chance))
				value = value.add(inc);
			else
				value = value.sub(inc);
		}

		return value;
	}
/*
	public static void main(String[] args) {
		DTOScenario scenario = new DTOScenario(false);
		scenario.put(DTOScenario.Key.REK, new DoubleValue(0.11));
		scenario.put(DTOScenario.Key.RFK, new DoubleValue(0.1));
		scenario.put(DTOScenario.Key.BTAX, new DoubleValue(0.1694));
		scenario.put(DTOScenario.Key.CTAX, new DoubleValue(0.26375));
		scenario.put(DTOScenario.Key.DCF_METHOD, new StringValue("apv"));

		DTODirectInput di0 = new DTODirectInput();
		DTOPeriod period0 = new DTOPeriod();
		period0.addChild(di0);
		scenario.addChild(period0);

		DTODirectInput di = new DTODirectInput();
		di.put(DTODirectInput.Key.FCF, new DoubleValue(130));
		di.put(DTODirectInput.Key.LIABILITIES, new DoubleValue(1200));
		DTOPeriod period1 = new DTOPeriod();
		period1.addChild(di);
		scenario.addChild(period1);

		DTODirectInput di2 = new DTODirectInput();
		di2.put(DTODirectInput.Key.FCF, new DoubleValue(105));
		di2.put(DTODirectInput.Key.LIABILITIES, new DoubleValue(1050));
		DTOPeriod period2 = new DTOPeriod();
		period2.addChild(di2);
		scenario.addChild(period2);

		DTODirectInput di3 = new DTODirectInput();
		di3.put(DTODirectInput.Key.FCF, new DoubleValue(90));
		di3.put(DTODirectInput.Key.LIABILITIES, new DoubleValue(1100));
		DTOPeriod period3 = new DTOPeriod();
		period3.addChild(di3);
		scenario.addChild(period3);

		DTODirectInput di4 = new DTODirectInput();
		di4.put(DTODirectInput.Key.FCF, new DoubleValue(100));
		di4.put(DTODirectInput.Key.LIABILITIES, new DoubleValue(1000));
		DTOPeriod period4 = new DTOPeriod();
		period4.addChild(di4);
		scenario.addChild(period4);

		RandomWalk rw = new RandomWalk();
		rw.setScenario(scenario);
		
		JFrame test = new JFrame();
		test.setContentPane(rw.calculateParameters());
		test.pack();
		test.show();
		
		DistributionMap dm = rw.calculate();
		
		

		Iterator<Entry<java.lang.Double, Integer>> iter = dm.iterator();
		System.out.println("Key 0");
		while (iter.hasNext()) {
			Map.Entry<java.lang.Double, java.lang.Integer> entry = (Map.Entry<java.lang.Double, java.lang.Integer>) iter
					.next();
			System.out.println(entry.getKey());
		}
		iter = dm.iterator();
		System.out.println("Value 0");
		while (iter.hasNext()) {
			Map.Entry<java.lang.Double, java.lang.Integer> entry = (Map.Entry<java.lang.Double, java.lang.Integer>) iter
					.next();
			System.out.println(entry.getValue());
		}
	}
*/
}
