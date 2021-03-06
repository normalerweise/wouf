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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;

import org.bh.gui.swing.comp.BHButton;
import org.bh.gui.swing.comp.BHCheckBox;
import org.bh.gui.swing.comp.BHDescriptionLabel;
import org.bh.platform.PlatformController;
import org.bh.platform.Services;
import org.bh.platform.i18n.BHTranslator;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

@SuppressWarnings("serial")
public final class BHOptionDialog extends JDialog implements ActionListener {

	private CellConstraints cons;

	private BHDescriptionLabel language;
	private JComboBox combo;
	private BHButton apply;
	private BHDescriptionLabel lanimation;
	private BHCheckBox chbanimation;
	
	private JPanel elements;
	private JPanel buttons;

	public BHOptionDialog(JFrame owner, boolean modal) {
		super(owner);
		this.setModal(modal);
		
		String rowDef = "4px:grow,p,4px,p,4px:grow";
		String colDef = "4px,fill:p:grow,4px";
		
		setLayout(new FormLayout(colDef, rowDef));
		cons = new CellConstraints();

		add(this.getElements(),cons.xy(2, 2));
		add(this.getButtons(),cons.xy(2, 4,"right, center"));
		
		this.setSize(400,200);
		this.setProperties();
		
		this.setIconImages(Services.setIcon());
		
	}
	
	public JPanel getElements() {
		elements = new JPanel();
		// setLayout to the status bar
		String rowDef = "4px,p,4px,p,4px,p,4px";
		String colDef = "20px:grow,pref,4px,max(80px;pref),20px:grow";
		elements.setLayout(new FormLayout(colDef, rowDef));
		cons = new CellConstraints();

		// create select language components
		language = new BHDescriptionLabel("MoptionsLanguage");
		lanimation = new BHDescriptionLabel("MoptionsAnimation");

		combo = new JComboBox(Services.getTranslator().getAvailableLocales());
		combo.setRenderer(new BHLanguageRenderer());
		combo.setSelectedItem(Services.getTranslator().getLocale());
		combo.setPreferredSize(new Dimension(100,25));
		
		chbanimation = new BHCheckBox ("Chbanimation");
		chbanimation.setLabel("");
		chbanimation.setSelected(PlatformController.preferences.getBoolean("animation", true));
		
		// add components
		elements.add(language, cons.xywh(2, 2, 1, 1, "right,center"));
		elements.add(combo, cons.xywh(4, 2, 1, 1,"left, default"));
		elements.add(lanimation, cons.xy(2, 4, "right,center"));
		elements.add(chbanimation, cons.xy(4, 4, "left,center"));
		
		elements.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
//		elements.setSize(this.getPreferredSize());
		
		return elements;
	}

	public JPanel getButtons() {
		buttons = new JPanel();
		// setLayout to the status bar
		String rowDef = "4px,p,4px";
		String colDef = "4px,p,4px";
		buttons.setLayout(new FormLayout(colDef, rowDef));
		cons = new CellConstraints();
		
		apply = new BHButton("Bapply");
		apply.setText(BHTranslator.getInstance().translate("Bokay"));
		apply.addActionListener(this);
		
		buttons.add(apply, cons.xywh(2, 2, 1, 1, "right, center"));
		
		buttons.setSize(buttons.getPreferredSize());
		return buttons;
	}


	private void setProperties() {
		this.setTitle(BHTranslator.getInstance().translate("MoptionsDialog"));
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		this.setVisible(true);
	}

	/**
	 * Handle Buttonclick
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Persit preferences.
		PlatformController.preferences.put("language", ((Locale) combo.getSelectedItem()).getLanguage());
		PlatformController.preferences.put("animation", Boolean.toString(chbanimation.isSelected()));
		
		// Apply preferences.
		Services.getTranslator().setLocale((Locale) combo.getSelectedItem());
		
		this.dispose();
	}

	/**
	 * Renderer for Locales in Options combobox.
	 * 
	 * <p>
	 * This class implementing the <code>ListCellRenderer</code> Interface
	 * provides a stamp like method for the Languages.
	 * 
	 * @author klaus
	 * @version 1.0, Jan 7, 2010
	 * 
	 */
	public class BHLanguageRenderer implements ListCellRenderer {
		
		/**
		 * <code>JLabel</code> to be used for every item.
		 */
		private JLabel l = null;

		/**
		 * Default constructor.
		 */
		public BHLanguageRenderer() {
			l = new JLabel();
			l.setPreferredSize(new Dimension(l.getPreferredSize().width, 20));
		}
		/**
		 * Method to return a Component for each item of the List.
		 */
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {

			l.setText(((Locale) value).getDisplayLanguage(Services
					.getTranslator().getLocale()));
			return l;

		}

	}
}
