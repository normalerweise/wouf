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
package org.bh.platform.i18n;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.bh.gui.IBHComponent;
import org.bh.platform.PlatformController;
import org.bh.platform.PlatformEvent;
import org.bh.platform.Services;

/**
 * Implements the <code>ITranslator</code> Interface for Translation of all i18n
 * relevant keys in Business Horizon.
 * 
 * @author Thiele.Klaus
 * @version 1.0 2010/01/22
 * 
 */
public final class BHTranslator implements ITranslator {

	/**
	 * private Logging instance for log.
	 */
	private static final Logger LOG = Logger.getLogger(BHTranslator.class);

	/**
	 * Constant for used <code>ResourceBundle</code>.
	 */
	private static final String BHGUIKEYS = "BHGUIKeys";

	/**
	 * Available <code>Locale</code>s. Only provided if corresponding properties
	 * file is provided!
	 */
	private static final Locale[] AVAILABLE = { Locale.ENGLISH, Locale.GERMAN };

	/**
	 * Default Locale for Business Horizon.
	 */
	private static final Locale DEFAULT = Locale.GERMAN;

	/**
	 * Singleton instance.
	 */
	private static ITranslator instance;

	/**
	 * <code>Locale</code> used for translation.
	 */
	private  static Locale locale;

	/**
	 * <code>ResourceBundle</code> for access to the properties file.
	 */
	private ResourceBundle bundle;

	/**
	 * Registered <code>PropertyChangeListener</code>.
	 */
	private ArrayList<PropertyChangeListener> listener;

	/**
	 * Default Constructor which instantiates the <code>BHTranslator</code> with
	 * the default <code>Locale</code>. Private for singelton.
	 */
	private BHTranslator() {
		// Try to get preferred language
		String prefLanguage = PlatformController.preferences.get("language",
				"initial");

		// If no language has been preferred, set Locale.
		if ("initial".equals(prefLanguage)) {

			// if OS language supported, set OS language.
			for (Locale l : BHTranslator.AVAILABLE) {
				if (l.getLanguage().equals(Locale.getDefault().getLanguage())) {
					this.locale = new Locale(l.getLanguage());
					break;
				}
			}

			// Still no language found, use BH default.
			if (this.locale == null) {
				this.locale = BHTranslator.DEFAULT;
			}

			// Finally save invoked language to preferences.
			PlatformController.preferences.put("language", BHTranslator.DEFAULT
					.getLanguage());

		} else {
			// set preferred Locale.
			for (Locale l : BHTranslator.AVAILABLE) {
				if (l.getLanguage().equals(prefLanguage)) {
					this.locale = l;
				}
			}
		}

		// Set locale defaults for i18n & Look&Feel.
		Locale.setDefault(this.locale);
		JComponent.setDefaultLocale(this.locale);
		Services.initNumberFormats();

		// init resource bundle instance
		this.bundle = ResourceBundle.getBundle(BHTranslator.BHGUIKEYS,
				this.locale);

		this.listener = new ArrayList<PropertyChangeListener>();
		LOG.debug("Translator initialized with Locale "
				+ this.bundle.getLocale());
	}

	/**
	 * Returns the Singleton instance of <code>BHTranslator</code>.
	 * 
	 * @return the instance.
	 */
	public static ITranslator getInstance() {
		if (BHTranslator.instance == null) {
			BHTranslator.instance = new BHTranslator();
		}
		return instance;
	}

	/**
	 * Core method to translate keys into domain-specific String to be used in
	 * user facing screen texts.
	 * 
	 * @param key
	 *            key to be translated
	 * @return translated <code>String</code>
	 */
	@Override
	public String translate(Object key) {
		try {
			return this.bundle.getString(key.toString());
		} catch (MissingResourceException e) {
			try {
				if (key.toString().startsWith(IBHComponent.MINVALUE)
						|| key.toString().startsWith(IBHComponent.MAXVALUE)) {
					return this.bundle.getString(key.toString().replaceFirst(
							"^.*_", ""));
				}
			} catch (MissingResourceException ex) {
			}
			LOG.error("Could not translate key \"" + key + "\"", e);
			return key.toString();
		}
	}

	/**
	 * Special method for dedicated version of keys.
	 * 
	 * @param key
	 *            key to be translated
	 * @param texttype
	 * @return translated <code>String</code>
	 */
	public String translate(Object key, int type) {

		switch (type) {

		case SHORT:
			try {
				return this.bundle.getString(key.toString() + "_short");
			} catch (MissingResourceException e) {
				return this.translate(key);
			}

		case REGULAR:
			return this.translate(key);

		case LONG:
			try {
				return this.bundle.getString(key.toString() + "_long");
			} catch (MissingResourceException e) {
				return this.translate(key);
			}

		case MNEMONIC:
			try {
				return this.bundle.getString(key.toString() + "_m");
			} catch (MissingResourceException e) {
				return "Y"; // fall back mnemonic... random one
			}

		default:
			return this.translate(key);
		}

	}

	/**
	 * Sets a new <code>Locale</code> for further translation.
	 * 
	 * @param newLocale
	 *            locale to be set
	 */
	@Override
	public void setLocale(Locale newLocale) {
		Locale oldLocale = this.locale;
		this.locale = newLocale;

		Locale.setDefault(newLocale);
		JComponent.setDefaultLocale(newLocale);
		Services.initNumberFormats();

		this.bundle = ResourceBundle.getBundle(BHTranslator.BHGUIKEYS,
				this.locale);

		this.firePropertyChange("Locale", oldLocale, this.bundle.getLocale());
	}

	/**
	 * Returns the currently used <code>Locale</code> for translation.
	 * 
	 * @return the currently used <code>Locale</code>
	 */
	@Override
	public Locale getLocale() {
		return this.bundle.getLocale();
	}

	/**
	 * Checks if key is available for translation.
	 * 
	 * @param key
	 *            key to be checked
	 * @return true if contains else false.
	 */
	@Override
	public boolean containsKey(Object key) {
		return this.bundle.containsKey(key.toString());
	}

	/**
	 * Provides all available languages.
	 */
	@Override
	public Locale[] getAvailableLocales() {
		return BHTranslator.AVAILABLE;
	}

	/**
	 * Adds a PropertyChangeListener.
	 */
	@Override
	public void addPropertyChangeListener(PropertyChangeListener l) {
		this.listener.add(l);
	}

	/**
	 * Removes a PropertyChangeListener.
	 * 
	 * @param l
	 *            Listener to be added.
	 */
	@Override
	public void removePropertyChangeListener(PropertyChangeListener l) {
		this.listener.remove(l);
	}

	/**
	 * Fire new PropertyChangeEvent to all registered PropertyChangeListeners.
	 * 
	 * @param key
	 *            property that has changed
	 * @param oldValue
	 *            old value
	 * @param newValue
	 *            new value
	 */
	private void firePropertyChange(String key, Object oldValue, Object newValue) {
		LOG.debug("BHTranslator: Property changed: " + key + ": " + newValue);

		// for each listener call propertyChange with proper attributes
		for (PropertyChangeListener l : this.listener) {
			l.propertyChange(new PropertyChangeEvent(this, key, oldValue,
					newValue));
		}
		Services.firePlatformEvent(new PlatformEvent(BHTranslator.class,
				PlatformEvent.Type.LOCALE_CHANGED));
	}
	public static Locale getLoc(){
		return BHTranslator.locale;
	}
}