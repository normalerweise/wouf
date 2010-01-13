package org.bh.gui.swing;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.bh.platform.PlatformKey;
import org.bh.platform.Services;
import org.bh.platform.i18n.ITranslator;

/**
 * BHMenuBar to display a menu bar in screen.
 * 
 * <p>
 * This class extends the Swing <code>JMenuBar</code> to display a menu bar on
 * the screen.
 * 
 * To use the shortcut keys, you should use the constant field values shown on
 * http://java.sun.com/j2se/1.4.2/docs/api/constant-values.html. If no shortcut
 * is necessary use '0' as the key.
 * 
 * @author Tietze.Patrick
 * @version 0.1, 2009/12/16
 * 
 */

public class BHMenuBar extends JMenuBar{

	ITranslator translator = Services.getTranslator();
	
	
	public BHMenuBar() {

		/**
		 * create the menu bar with all the items
		 **/

		// create menu --> File
		JMenu menuFile = new JMenu(translator.translate("Mfile"));
		// menuFile.setMnemonic(KeyEvent.VK_D);
		add(menuFile);

		// create menu --> Project
		JMenu menuProject = new JMenu(translator.translate("Mproject"));
		// menuProject.setMnemonic(KeyEvent.VK_P);
		add(menuProject);

		// create menu --> Scenario
		JMenu menuScenario = new JMenu(translator.translate("Mscenario"));
		// menuScenario.setMnemonic(KeyEvent.VK_S);
		add(menuScenario);

//		// create menu --> Bilanz & GuV
//		JMenu menuBilanzGuV = new JMenu(translator
//				.translate("MbalanceProfitLoss"));
//		// menuBilanzGuV.setMnemonic(KeyEvent.VK_B);
//		add(menuBilanzGuV);

		// create menu --> Options
		JMenu menuOptions = new JMenu(translator.translate("Moptions"));
		// menuOptions.setMnemonic(KeyEvent.VK_O);
		add(menuOptions);

		// create menu --> Help
		JMenu menuHelp = new JMenu(translator.translate("Mhelp"));
		// menuHelp.setMnemonic(KeyEvent.VK_H);
		add(menuHelp);

		/**
		 * create menu items --> file
		 **/
		menuFile.add(new BHMenuItem(PlatformKey.FILENEW, 78)); // N
		menuFile.add(new BHMenuItem(PlatformKey.FILEOPEN, 79)); // O
		menuFile.add(new BHMenuItem(PlatformKey.FILESAVE, 83)); // S
		menuFile.add(new BHMenuItem(PlatformKey.FILESAVEAS, 83)); // S
		menuFile.addSeparator();
		menuFile.add(new BHMenuItem(PlatformKey.FILECLOSE, 87)); // W
		menuFile.add(new BHMenuItem(PlatformKey.FILEQUIT, 81));  // Q


		/**
		 * create menu items --> project
		 **/
		menuProject.add(new BHMenuItem(PlatformKey.PROJECTCREATE));
//		menuProject.add(new BHMenuItem(PlatformKey.PROJECTRENAME));
		menuProject.add(new BHMenuItem(PlatformKey.PROJECTDUPLICATE));
		menuProject.addSeparator();
		menuProject.add(new BHMenuItem(PlatformKey.PROJECTIMPORT));
		menuProject.add(new BHMenuItem(PlatformKey.PROJECTEXPORT));
		menuProject.addSeparator();
		menuProject.add(new BHMenuItem(PlatformKey.PROJECTREMOVE));


		/**
		 * create menu items --> scenario
		 **/
		menuScenario.add(new BHMenuItem(PlatformKey.SCENARIOCREATE));
//		menuScenario.add(new BHMenuItem(PlatformKey.SCENARIORENAME));
		menuScenario.add(new BHMenuItem(PlatformKey.SCENARIODUPLICATE));
		menuScenario.addSeparator();
//		menuScenario.add(new BHMenuItem(PlatformKey.SCENARIOMOVE));
		menuScenario.add(new BHMenuItem(PlatformKey.SCENARIOREMOVE));

		
//		/**
//		 * create menu items --> Bilanz & GuV
//		 **/
//		menuBilanzGuV.add(new BHMenuItem(PlatformKey.BILANZGUVSHOW, 66));
//		menuBilanzGuV.add(new BHMenuItem(PlatformKey.BILANZGUVCREATE));
//		menuBilanzGuV.add(new BHMenuItem(PlatformKey.BILANZGUVIMPORT));
//		menuBilanzGuV.add(new BHMenuItem(PlatformKey.BILANZGUVREMOVE));

		
		/**
		 * create menu items --> options
		 **/
		menuOptions.add(new BHMenuItem(PlatformKey.OPTIONSCHANGE, 80));
		
		
		/**
		 * create menu items --> options
		 **/
		menuHelp.add(new BHMenuItem(PlatformKey.HELPUSERHELP, 72));
		menuHelp.add(new BHMenuItem(PlatformKey.HELPMATHHELP));
		menuHelp.addSeparator();
		menuHelp.add(new BHMenuItem(PlatformKey.HELPINFO, 112));
	}
	
	public class BHMenu extends JMenu {
		public BHMenu(String title) {
			super(title);
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			System.err.println("BHMenuBar.paintComponent()");
			Graphics2D g2d = (Graphics2D) g;
//			super.paintComponent(g2d);
//			g2d.rotate(60.0);
			g2d.setComposite(AlphaComposite.SrcOver.derive(0.8f));
			super.paintComponent(g2d);
		}
	}
}