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
package org.bh.gui.swing;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.bh.gui.IBHAction;
import org.bh.gui.IBHComponent;
import org.bh.platform.IPlatformListener;
import org.bh.platform.PlatformEvent;
import org.bh.platform.PlatformKey;
import org.bh.platform.Services;
import org.bh.platform.PlatformEvent.Type;
import org.bh.platform.i18n.ITranslator;

/**
 * BHMenuItem to create and display new menu items in the menu bar.
 * 
 * <p>
 * This class extends the Swing <code>JMenuItem</code> to display menu items in
 * the menu bar
 * 
 * @author Tietze.Patrick
 * @author Marco.Hammel
 * @version 0.1, 2009/12/16
 * 
 */
public final class BHMenuItem extends JMenuItem implements IBHComponent, IBHAction, IPlatformListener{
	private static final long serialVersionUID = 457483046895655665L;
	private PlatformKey key;
	private String hint;
	private static List<IBHAction> platformItems = new ArrayList<IBHAction>();

	/**
	 * Creates a new MenuItem (to be used in regular menus)
	 * 
	 * @param key
	 *            key for action handling and texts
	 * @param eventKey
	 *            shortcut button for keyboard-addicted users
	 * @param isPlatformItem
	 *            Menu Item will be placed in platform list if true
	 */
	public BHMenuItem(PlatformKey key, int eventKey, boolean isPlatformItem) {
		super();

		this.key = key;
		reloadText();
		
		Services.addPlatformListener(this);
		if (eventKey != 0) {
			this.setMnemonic(Services.getTranslator().translate(key, ITranslator.MNEMONIC).charAt(0));

			int metakey = 0;
			
			if ("Mac OS X".equals(System.getProperty("os.name"))) {
				metakey = ActionEvent.META_MASK;
			} else {
				metakey = ActionEvent.CTRL_MASK;
			}

			if (key == PlatformKey.FILESAVEAS) {
				metakey |= ActionEvent.SHIFT_MASK;
			}

			if(key == PlatformKey.PROJECTCREATE){
				metakey = 0;
			}
			
			if(key == PlatformKey.SCENARIOCREATE){
				metakey = 0;
			}
			
			if(key == PlatformKey.PERIODCREATE){
				metakey = 0;
			}
			
			if(key == PlatformKey.HELPUSERHELP){
				metakey = 0;
			}
			
			this.setAccelerator(KeyStroke.getKeyStroke(eventKey, metakey));
		}

		if (isPlatformItem) {
			platformItems.add(this);
		}

	}

	/**
	 * Creates a new Menu Item (item is automatically added to platform list)
	 * 
	 * @param key
	 *            key for action handling and texts
	 * @param eventKey
	 *            shortcut button for keyboard-adicted users
	 */
	public BHMenuItem(PlatformKey key, int eventKey) {
		this(key, eventKey, true);
	}

	/**
	 * Creates a new Menu Item (item is automatically added to platform list and
	 * no eventKey for keyboard-shortcut is set)
	 * 
	 * @param key
	 *            key for action handling and texts
	 */
	public BHMenuItem(PlatformKey key) {
		this(key, 0, true);
	}

	public BHMenuItem() {
		super();
	}

	@Override
	public String getKey() {
		return key.toString();
	}

	@Override
	public PlatformKey getPlatformKey() {
		return this.key;
	}

	@Override
	public boolean isPlatformItem() {
		return (this.key != null);
	}

	/**
	 * Method to get all MenuItems that are generated for platforms (i.e. that
	 * ones which have forPlatform = false not set)
	 */
	public static List<IBHAction> getPlatformItems() {
		return platformItems;
	}

	@Override
	public String getHint() {
		return hint;
	}

	@Override
	public void platformEvent(PlatformEvent e) {
		if (e.getEventType() == Type.LOCALE_CHANGED) {
			this.reloadText();
		}
	}

	/**
	 * Reset Text if necessary.
	 */
	protected void reloadText() {
		this.setText(Services.getTranslator().translate(key.toString()));
		hint = Services.getTranslator().translate(key, ITranslator.LONG);
		setToolTipText(hint);
	}
}
