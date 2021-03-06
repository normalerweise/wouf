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
package org.bh.data.types;

import javax.help.UnsupportedOperationException;

import org.bh.platform.Services;

/**
 * Calculable implementation for interval values.
 * 
 * @author Sebastian
 * @author Norman
 * 
 * @version 0.1, 21.11.2009, Sebastian
 * @version 0.3, 21.12.2009, Norman
 * @version 0.4, 03.01.2009, Norman corrected abs(), sqrt()
 */
public class IntervalValue extends Calculable {
	private static final long serialVersionUID = 6854419128579732095L;

	/** The min. */
	protected final double min;

	/** The max. */
	protected final double max;

	/**
	 * Instantiates a new interval value.
	 * 
	 * @param min
	 *            the min
	 * @param max
	 *            the max
	 */
	public IntervalValue(double min, double max) {
		this.min = min;
		this.max = max;
	}

	/* Specified by interface/super class. */
	@Override
	public final Calculable add(final Calculable summand) {
		if (summand instanceof IntegerValue) {
			int value = ((IntegerValue) summand).value;
			return add(new IntervalValue(value, value));
		} else if (summand instanceof DoubleValue) {
			double value = ((DoubleValue) summand).value;
			return add(new IntervalValue(value, value));
		} else {
			return new IntervalValue(Math.nextAfter(min
					+ ((IntervalValue) summand).min, Double.NEGATIVE_INFINITY),
					Math.nextAfter(max + ((IntervalValue) summand).max,
							Double.POSITIVE_INFINITY));
		}
	}

	/* Specified by interface/super class. */
	@Override
	public final Calculable sub(final Calculable subtrahend) {
		if (subtrahend instanceof IntegerValue) {
			int value = ((IntegerValue) subtrahend).value;
			return sub(new IntervalValue(value, value));
		} else if (subtrahend instanceof DoubleValue) {
			double value = ((DoubleValue) subtrahend).value;
			return sub(new IntervalValue(value, value));
		} else {
			// TODO ask Mr. Ratz whether works for [x] - [y] as well
			IntervalValue sub = (IntervalValue) subtrahend;
			if (sub.min == Double.NEGATIVE_INFINITY
					&& sub.max == Double.POSITIVE_INFINITY) {
				return new IntervalValue(Double.NEGATIVE_INFINITY,
						Double.POSITIVE_INFINITY);
			}
			if (sub.max == Double.POSITIVE_INFINITY) {
				return new IntervalValue(Double.NEGATIVE_INFINITY, Math
						.nextAfter(max - sub.min, Double.POSITIVE_INFINITY));
			}
			if (sub.min == Double.NEGATIVE_INFINITY) {
				return new IntervalValue(Math.nextAfter(min - sub.max,
						Double.NEGATIVE_INFINITY), Double.POSITIVE_INFINITY);
			}
			if (Double.isNaN(sub.min) && Double.isNaN(sub.max)) {
				return new IntervalValue(Double.NaN, Double.NaN);
			}
			return new IntervalValue(Math.nextAfter(min
					- ((IntervalValue) subtrahend).max,
					Double.NEGATIVE_INFINITY), Math.nextAfter(max
					- ((IntervalValue) subtrahend).min,
					Double.POSITIVE_INFINITY));
		}
	}

	/* Specified by interface/super class. */
	@Override
	public final Calculable mul(final Calculable multiplicand) {
		if (multiplicand instanceof IntegerValue) {
			int value = ((IntegerValue) multiplicand).value;
			return mul(new IntervalValue(value, value));
		} else if (multiplicand instanceof DoubleValue) {
			double value = ((DoubleValue) multiplicand).value;
			return mul(new IntervalValue(value, value));
		} else {

			double x = min(min * ((IntervalValue) multiplicand).min, min
					* ((IntervalValue) multiplicand).max, max
					* ((IntervalValue) multiplicand).min, max
					* ((IntervalValue) multiplicand).max);

			double y = max(min * ((IntervalValue) multiplicand).min, min
					* ((IntervalValue) multiplicand).max, max
					* ((IntervalValue) multiplicand).min, max
					* ((IntervalValue) multiplicand).max);

			return new IntervalValue(Math
					.nextAfter(x, Double.NEGATIVE_INFINITY), Math.nextAfter(y,
					Double.POSITIVE_INFINITY));
		}
	}

	/* Specified by interface/super class. */
	@Override
	public final Calculable div(final Calculable divisor) {
		if (divisor instanceof IntegerValue) {
			int value = ((IntegerValue) divisor).value;
			return div(new IntervalValue(value, value));
		} else if (divisor instanceof DoubleValue) {
			double value = ((DoubleValue) divisor).value;
			return div(new IntervalValue(value, value));
		} else {
			IntervalValue div = (IntervalValue) divisor;
			IntegerValue zero = new IntegerValue(0);

			if (!(div.contains(0))) {
				return mul(new IntervalValue(1 / div.max, 1 / div.min));
				// double rounding ? --> mul operation rounds as well
				// Math.nextAfter((1 / div.max), Double.NEGATIVE_INFINITY),
				// Math.nextAfter((1 / div.min), Double.POSITIVE_INFINITY)));
			}
			if (div.min == 0 && div.max == 0) {
				return new IntervalValue(Double.NaN, Double.NaN);
			} else if (div.min == 0) {
				if (greaterThan(zero)) {
					return new IntervalValue(Math.nextAfter(min / div.max,
							Double.NEGATIVE_INFINITY), Double.POSITIVE_INFINITY);
				}
				if (lessThan(zero)) {
					return new IntervalValue(Double.NEGATIVE_INFINITY, Math
							.nextAfter(max / div.max, Double.POSITIVE_INFINITY));
				}
				throw new UnsupportedOperationException("Unknown case");

			} else if (div.max == 0) {
				if (greaterThan(zero)) {
					return new IntervalValue(Double.NEGATIVE_INFINITY, Math
							.nextAfter(min / div.min, Double.POSITIVE_INFINITY));
				}
				if (lessThan(zero)) {
					return new IntervalValue(Math.nextAfter(max / div.min,
							Double.POSITIVE_INFINITY), Double.POSITIVE_INFINITY);
				}
				throw new UnsupportedOperationException("Unknown case");
			} else { // div contains 0 == true && lower & upper
				return new IntervalValue(Double.NEGATIVE_INFINITY,
						Double.POSITIVE_INFINITY);
			}
		}
	}

	/* Specified by interface/super class. */
	@Override
	public final Calculable pow(final Calculable exponent) {
		// TODO ask Mr Ratz
		double exp = 0;
		if (exponent instanceof IntegerValue) {
			exp = ((IntegerValue) exponent).value;
		} else if (exponent instanceof DoubleValue) {
			exp = ((DoubleValue) exponent).value;
		} else if (exponent instanceof IntervalValue) {
			// TODO possible?
			throw new UnsupportedOperationException(
					"Not implemented/ allowed so far");
		}
		double resMin = Math.pow(min, exp);
		double resMax = Math.pow(max, exp);

		if (resMin < resMax) {
			return new IntervalValue(Math.nextAfter(resMin,
					Double.NEGATIVE_INFINITY), Math.nextAfter(resMax,
					Double.POSITIVE_INFINITY));
		}
		return new IntervalValue(Math.nextAfter(resMax,
				Double.NEGATIVE_INFINITY), Math.nextAfter(resMin,
				Double.POSITIVE_INFINITY));
	}

	public final Calculable sqr() {
		double resMin = Math.pow(min, 2);
		double resMax = Math.pow(max, 2);

		if (resMin < resMax) {
			return new IntervalValue(Math.nextAfter(resMin,
					Double.NEGATIVE_INFINITY), Math.nextAfter(resMax,
					Double.POSITIVE_INFINITY));
		}
		return new IntervalValue(Math.nextAfter(resMax,
				Double.NEGATIVE_INFINITY), Math.nextAfter(resMin,
				Double.POSITIVE_INFINITY));
	}

	/* Specified by interface/super class. */
	@Override
	public final Calculable sqrt() {
		return new IntervalValue(Math.nextAfter(Math.sqrt(min),
				Double.NEGATIVE_INFINITY), Math.nextAfter(Math.sqrt(max),
				Double.POSITIVE_INFINITY));
	}

	/**
	 * Gets the max.
	 * 
	 * @return the max
	 */
	@Override
	public double getMax() {
		return max;
	}

	/**
	 * Gets the min.
	 * 
	 * @return the min
	 */
	@Override
	public double getMin() {
		return min;
	}
	
	@Override
	public double getMinMaxDiff() {
		return max - min;
	}

	/* Specified by interface/super class. */
	@Override
	public final String toString() {
		return "[" + Services.numberToString(min) + "; " + Services.numberToString(max) + "]";
	}

	/* Specified by interface/super class. */
	@Override
	public final Calculable clone() {
		return new IntervalValue(min, max);
	}

	/**
	 * Determines the maximum value of the given parameters.
	 * 
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @param c
	 *            the c
	 * @param d
	 *            the d
	 * 
	 * @return the maximum double value
	 */
	private double max(double a, double b, double c, double d) {
		double maxVal;
		maxVal = Math.max(a, b);
		maxVal = Math.max(maxVal, c);
		maxVal = Math.max(maxVal, d);
		return maxVal;
	}

	/**
	 * Determines the minimum value of the given parameters.
	 * 
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 * @param c
	 *            the c
	 * @param d
	 *            the d
	 * 
	 * @return the minimum double value
	 */
	private double min(double a, double b, double c, double d) {
		double minVal;
		minVal = Math.min(a, b);
		minVal = Math.min(minVal, c);
		minVal = Math.min(minVal, d);
		return minVal;
	}

	/**
	 * Checks whether, a given DoubleValue is part of the current interval
	 * 
	 * @param d
	 *            DoubleValue to check
	 * @return true if the Interval contains the DoubleValue
	 */
	public boolean contains(DoubleValue d) {
		return contains(d.value);
	}

	/**
	 * Checks whether, a given double is part of the current interval
	 * 
	 * @param d
	 *            double to check
	 * @return true if the Interval contains the double value
	 */
	public boolean contains(double d) {
		return (d >= min && d <= max);
	}

	/* Specified by interface/super class. */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(max);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(min);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/* Specified by interface/super class. */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IntervalValue other = (IntervalValue) obj;
		if (Double.doubleToLongBits(max) != Double.doubleToLongBits(other.max))
			return false;
		if (Double.doubleToLongBits(min) != Double.doubleToLongBits(other.min))
			return false;
		return true;
	}

	@Override
	public boolean greaterThan(Calculable compare) {
		if (compare instanceof IntegerValue) {
			IntegerValue integerValue = (IntegerValue) compare;
			return greaterThan(new IntervalValue(integerValue.value,
					integerValue.value));
		} else if (compare instanceof DoubleValue) {
			DoubleValue doubleValue = (DoubleValue) compare;
			return greaterThan(new IntervalValue(doubleValue.value,
					doubleValue.value));
		} else {
			IntervalValue intervalValue = (IntervalValue) compare;
			if (getMin() > intervalValue.getMax()) {
				return true;
			}
			return false;
		}
	}

	@Override
	public boolean lessThan(Calculable compare) {
		if (compare instanceof IntegerValue) {
			IntegerValue integerValue = (IntegerValue) compare;
			return lessThan(new IntervalValue(integerValue.value,
					integerValue.value));
		} else if (compare instanceof DoubleValue) {
			DoubleValue doubleValue = (DoubleValue) compare;
			return lessThan(new IntervalValue(doubleValue.value,
					doubleValue.value));
		} else {
			IntervalValue intervalValue = (IntervalValue) compare;
			if (getMax() < intervalValue.getMin()) {
				return true;
			}
			return false;
		}
	}

	@Override
	public Calculable abs() {
		double minAbs = Math.abs(min);
		double maxAbs = Math.abs(max);

		if (minAbs <= maxAbs) {
			return new IntervalValue(minAbs, maxAbs);
		}
		return new IntervalValue(maxAbs, minAbs);
	}

	@Override
	public boolean diffToLess(Calculable c, double limit) {
		if (c instanceof IntegerValue) {
			int value = ((IntegerValue) c).getValue();
			return diffToLess(new IntervalValue(value, value), limit);
		} else if (c instanceof DoubleValue) {
			double value = ((DoubleValue) c).getValue();
			return diffToLess(new IntervalValue(value, value), limit);
		} else if (c instanceof IntervalValue) {
			IntervalValue c2 = (IntervalValue) c;
			double diffMin;
			double diffMax;

			diffMin = Math.abs(min - c2.min);
			diffMax = Math.abs(max - c2.max);

			if (diffMin < limit && diffMax < limit) {
				return true;
			}
			return false;
		} else {
			throw new UnsupportedOperationException("Unsupported");
		}
	}

	@Override
	public Number toNumber() {
		// FIXME this should not be called for intervals, but we don't want the
		// app to crash
		return min;
	}
	
	
}
