package org.bh.plugin.pdfexport;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.bh.data.DTOProject;
import org.bh.data.DTOScenario;
import org.bh.data.types.Calculable;
import org.bh.data.types.DistributionMap;
import org.bh.gui.swing.BHDataExchangeDialog;
import org.bh.gui.swing.BHDefaultScenarioExportPanel;
import org.bh.gui.swing.IBHComponent;
import org.bh.platform.IImportExport;
import org.bh.platform.i18n.BHTranslator;
import org.bh.platform.i18n.ITranslator;

/**
 * Plug-in to export certain scenarios to PDF as a kind of report
 * 
 * @author Norman
 * @version 1.0, 10.01.2010
 * 
 */
public class PDFExport implements IImportExport {

	private static final String UNIQUE_ID = "pdf";
	private static final String GUI_KEY = "*.pdf";

	private static final String FILE_DESC = "Portable Document Format";
	private static final String FILE_EXT = "pdf";

	static Logger log = Logger.getLogger(PDFExport.class);

	ITranslator trans = BHTranslator.getInstance();
	ITextDocumentBuilder db = new ITextDocumentBuilder();

	@Override
	public void exportProject(DTOProject project,
			BHDataExchangeDialog exportDialog) {
		throw new UnsupportedOperationException(
				"This method has not been implemented");
	}

	@Override
	public void exportProject(DTOProject project, String filePath) {
		throw new UnsupportedOperationException(
				"This method has not been implemented");
	}

	@Override
	public void exportProjectResults(DTOProject project,
			Map<String, Calculable[]> results, BHDataExchangeDialog exportDialog) {
		throw new UnsupportedOperationException(
				"This method has not been implemented");
	}

	@Override
	public void exportProjectResults(DTOProject project,
			Map<String, Calculable[]> results, String filePath) {
		throw new UnsupportedOperationException(
				"This method has not been implemented");
	}

	@Override
	public void exportProjectResults(DTOProject project,
			DistributionMap results, String filePath) {
		throw new UnsupportedOperationException(
				"This method has not been implemented");
	}

	@Override
	public void exportProjectResults(DTOProject project,
			DistributionMap results, BHDataExchangeDialog exportDialog) {
		throw new UnsupportedOperationException(
				"This method has not been implemented");
	}	

	@Override
	public void exportScenario(final DTOScenario scenario,
			final BHDataExchangeDialog exportDialog) {

		final BHDefaultScenarioExportPanel dp = exportDialog
				.setDefaulExportScenarioPanel(FILE_DESC, FILE_EXT);
		exportDialog.pack();

		exportDialog.setPluginActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				IBHComponent comp = (IBHComponent) e.getSource();
				if (comp.getKey().equals("Mexport")) {
					if(!checkFile(dp.getFilePath(), dp)){
						return;
					}
					
					db.newDocument(dp.getFilePath(), scenario);
					db.buildHeadData(scenario);
					db.buildScenarioData(scenario);
					db.closeDocument();
					log.debug("pdf export completed " + dp.getFilePath());
					if (dp.openAfterExport()) {
						try {
							Desktop.getDesktop().open(
									new File(dp.getFilePath()));
						} catch (IOException e1) {
							log.error(e1);
						}
					}
					exportDialog.dispose();
				}
				if (comp.getKey().equals("Bcancel")) {
					exportDialog.dispose();
				}
			}
		});
	}

	protected boolean checkFile(String filePath, Component parent) {
		File f = new File(filePath);
		if((f.exists())) {
			if(!f.canWrite()) {
				JOptionPane.showMessageDialog(parent, trans.translate("NOWRITE"), trans.translate("NOWRITETITLE"), JOptionPane.ERROR_MESSAGE);
				return false;
			}
			int res =  JOptionPane.showConfirmDialog(parent, trans.translate("OVERWRITE"), trans.translate("OVERWRITETITLE"), JOptionPane.YES_NO_OPTION);
			if(res == JOptionPane.YES_OPTION) {
				return true;
			}
			return false;
		}
		// f does not exist
		try {
			f.createNewFile();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(parent, trans.translate("NOWRITE"), trans.translate("NOWRITETITLE"), JOptionPane.ERROR_MESSAGE);
			return false;
		}
		f.delete();
		return true;
	}

	@Override
	public void exportScenario(DTOScenario scenario, String filePath) {
		db.newDocument(filePath, scenario);
		db.buildHeadData(scenario);
		db.buildScenarioData(scenario);
		db.closeDocument();
		log.debug("pdf scenario batch export completed " + filePath);
	}

	@Override
	public void exportScenarioResults(final DTOScenario scenario,
			final Map<String, Calculable[]> results,
			final BHDataExchangeDialog exportDialog) {

		final BHDefaultScenarioExportPanel dp = exportDialog
				.setDefaulExportScenarioPanel(FILE_DESC, FILE_EXT);
		exportDialog.pack();

		exportDialog.setPluginActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				IBHComponent comp = (IBHComponent) e.getSource();
				if (comp.getKey().equals("Mexport")) {
					if(!checkFile(dp.getFilePath(), dp)){
						return;
					}
					
					db.newDocument(dp.getFilePath(), scenario);
					db.buildHeadData(scenario);
					db.buildScenarioData(scenario);
					db.buildResultDataDet(results);
					db.closeDocument();
					log.debug("pdf export completed " + dp.getFilePath());
					if (dp.openAfterExport()) {
						try {
							Desktop.getDesktop().open(
									new File(dp.getFilePath()));
						} catch (IOException e1) {
							log.error(e1);
						}
					}
					exportDialog.dispose();
				}
				if (comp.getKey().equals("Bcancel")) {
					exportDialog.dispose();
				}
			}
		});
	}

	@Override
	public void exportScenarioResults(DTOScenario scenario,
			Map<String, Calculable[]> results, String filePath) {

		db.newDocument(filePath, scenario);
		db.buildHeadData(scenario);
		db.buildScenarioData(scenario);
		db.buildResultDataDet(results);
		db.closeDocument();
		log.debug("pdf scenario batch export completed " + filePath);
	}

	@Override
	public void exportScenarioResults(final DTOScenario scenario,
			final DistributionMap results,
			final BHDataExchangeDialog exportDialog) {

		final BHDefaultScenarioExportPanel dp = exportDialog
				.setDefaulExportScenarioPanel(FILE_DESC, FILE_EXT);
		exportDialog.pack();

		exportDialog.setPluginActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				IBHComponent comp = (IBHComponent) e.getSource();
				if (comp.getKey().equals("Mexport")) {
					if(checkFile(dp.getFilePath(), dp)){
						return;
					}
					
					db.newDocument(dp.getFilePath(), scenario);
					db.buildHeadData(scenario);
					db.buildScenarioData(scenario);
					db.buildResultDataStoch(results);
					db.closeDocument();
					log.debug("pdf export completed " + dp.getFilePath());
					if (dp.openAfterExport()) {
						try {
							Desktop.getDesktop().open(
									new File(dp.getFilePath()));
						} catch (IOException e1) {
							log.error(e1);
						}
					}
					exportDialog.dispose();
				}
				if (comp.getKey().equals("Bcancel")) {
					exportDialog.dispose();
				}
			}
		});
	}

	@Override
	public void exportScenarioResults(DTOScenario scenario,
			DistributionMap results, String filePath) {

		db.newDocument(filePath, scenario);
		db.buildHeadData(scenario);
		db.buildScenarioData(scenario);
		db.buildResultDataStoch(results);
		db.closeDocument();
		log.debug("pdf scenario batch export completed " + filePath);
	}

	
	@Override
	public int getSupportedMethods() {
		return IImportExport.EXP_SCENARIO_RES
				+ IImportExport.EXP_SCENARIO + IImportExport.BATCH_EXPORT;
	}

	@Override
	public String getUniqueId() {
		return UNIQUE_ID;
	}

	@Override
	public String getGuiKey() {
		return GUI_KEY;
	}

	@Override
	public String toString() {
		return FILE_EXT + " - " + FILE_DESC;
	}

	@Override
	public String getFileDescription() {
		return FILE_DESC;
	}

	@Override
	public String getFileExtension() {
		return FILE_EXT;
	}

	@Override
	public DTOProject importProject(BHDataExchangeDialog importDialog) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("This method has not been implemented");
	}

	@Override
	public DTOScenario importScenario(BHDataExchangeDialog importDialog) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("This method has not been implemented");
	}
}
