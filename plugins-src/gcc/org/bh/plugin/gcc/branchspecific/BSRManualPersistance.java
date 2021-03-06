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
package org.bh.plugin.gcc.branchspecific;

import java.io.IOException;
import java.util.Map;

import org.bh.controller.Controller;
import org.bh.data.DTO;
import org.bh.data.DTOBusinessData;
import org.bh.platform.IImportExport;
import org.bh.platform.PlatformController;

import org.bh.platform.PlatformPersistenceManager;
import org.bh.platform.Services;
import org.apache.log4j.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.bh.platform.i18n.ITranslator;

/**
 * <short_description> This class implements basic functions to import and
 * export the BSR Data
 * <p>
 * <detailed_description> This class implements a manual Import and Export
 * Dialog, incl. the Import and Export activities in background.
 * 
 * @author Nees.Benedikt
 * @version 1.0, 07.01.2012
 * 
 */
public class BSRManualPersistance {

	public enum GUI_KEYS {
		// saveBranches() Error:
		EXPORT_BRANCH_ERROR_TITLE, EXPORT_BRANCH_ERROR_MESSAGE,
		// saveBranches() Success:
		EXPORT_BRANCH_SUCCESS_TITLE, EXPORT_BRANCH_SUCCESS_MESSAGE,
		// loadBranches() ERROR:
		IMPORT_BRANCH_ERROR_TITLE, IMPORT_BRANCH_ERROR_MESSAGE,
		// loadBranches() Success:
		IMPORT_BRANCH_SUCCESS_TITLE, IMPORT_BRANCH_SUCCESS_MESSAGE;

		public String toString() {
			return getClass().getName() + "." + super.toString();
		}
	}

	final ITranslator translator = Controller.getTranslator();
	private static final Logger log = Logger
			.getLogger(PlatformPersistenceManager.class);

	/**
	 * This method imports an external XML file with Branch Data into the
	 * internal XML-Structure so users can port their maintained Branch Data.
	 */
	public static boolean saveBranches() {

		JFileChooser fileChooser = new JFileChooser();
		fileChooser
				.setFileFilter(new FileNameExtensionFilter("XML File", "xml"));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		fileChooser.showSaveDialog(fileChooser);

		String savePath = fileChooser.getSelectedFile().toString();
		log.debug("Save path for company data: " + savePath);

		// Get right plugin
		Map<String, IImportExport> plugins = Services
				.getImportExportPlugins(IImportExport.EXP_PROJECT);

		IImportExport plugin = plugins.get("XML");

		try {
			// Get businessDataDTO
			DTOBusinessData dtoBusinessData = PlatformController.getBusinessDataDTO();

			// Trigger export
			plugin.setFileAndModel(savePath, dtoBusinessData);
			plugin.startExport();
			JOptionPane
					.showMessageDialog(
							null,
							Services.getTranslator()
									.translate(
											BSRManualPersistance.GUI_KEYS.EXPORT_BRANCH_SUCCESS_MESSAGE),
							Services.getTranslator()
									.translate(
											BSRManualPersistance.GUI_KEYS.EXPORT_BRANCH_SUCCESS_TITLE),
							JOptionPane.INFORMATION_MESSAGE);
			log.info("Branches exported successfully to " + savePath);

		} catch (IOException ioe) {
			// Wrong filepath?
			log.error("Failed to export Branches into XML", ioe);
			JOptionPane
					.showMessageDialog(
							null,
							Services.getTranslator()
									.translate(
											BSRManualPersistance.GUI_KEYS.EXPORT_BRANCH_ERROR_MESSAGE),
							Services.getTranslator()
									.translate(
											BSRManualPersistance.GUI_KEYS.EXPORT_BRANCH_ERROR_TITLE),
							JOptionPane.ERROR_MESSAGE);

			return false;
		}

		// PlatformController.preferences.put("branches", savePath);
		return true;

	}

	/**
	 * This method exports the internal XML-Structure with Branch Data to an
	 * external XML-file so users can port their maintained Branch Data.
	 * The XML-file will be validated.
	 */
	public static void loadBranches() {
		Map<String, IImportExport> plugins = Services
				.getImportExportPlugins(IImportExport.IMP_PROJECT);

		IImportExport plugin = plugins.get("XML");

		// Aufruf des JFileChooser zur Dateiauswahl
		JFileChooser fileChooser = new JFileChooser();
		fileChooser
				.setFileFilter(new FileNameExtensionFilter("XML File", "xml"));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		fileChooser.showOpenDialog(fileChooser);

		String xmlBranchesName = null;
		xmlBranchesName = fileChooser.getSelectedFile().toString();
		plugin.setFile(xmlBranchesName);

		DTOBusinessData bd = null;
		DTO.setThrowEvents(false); // Don't throw events.

		try {
			// Load data from default path
			bd = (DTOBusinessData) plugin.startImport();
			JOptionPane
					.showMessageDialog(
							null,
							Services.getTranslator()
									.translate(
											BSRManualPersistance.GUI_KEYS.IMPORT_BRANCH_SUCCESS_MESSAGE),
							Services.getTranslator()
									.translate(
											BSRManualPersistance.GUI_KEYS.IMPORT_BRANCH_SUCCESS_TITLE),
							JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception exc) {
			JOptionPane
					.showMessageDialog(
							null,
							Services.getTranslator()
									.translate(
											BSRManualPersistance.GUI_KEYS.IMPORT_BRANCH_ERROR_MESSAGE),
							Services.getTranslator()
									.translate(
											BSRManualPersistance.GUI_KEYS.IMPORT_BRANCH_ERROR_TITLE),
							JOptionPane.ERROR_MESSAGE);
			log.error("Could not load branches from external file.", exc);

		} finally {

			// Default BusinessDataDTO
			PlatformController.setBusinessDataDTO(bd);

			DTO.setThrowEvents(true);
		}
	}
}
