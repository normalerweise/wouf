package org.bh.plugin.pdfexport;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.bh.data.DTOPeriod;
import org.bh.data.DTOProject;
import org.bh.data.DTOScenario;
import org.bh.data.IPeriodicalValuesDTO;
import org.bh.data.types.Calculable;
import org.bh.data.types.DistributionMap;
import org.bh.data.types.IValue;
import org.bh.gui.swing.BHDataExchangeDialog;
import org.bh.gui.swing.BHDefaultScenarioExportPanel;
import org.bh.gui.swing.IBHComponent;
import org.bh.platform.IImportExport;
import org.bh.platform.i18n.BHTranslator;
import org.bh.platform.i18n.ITranslator;

import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

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

	private static ITranslator trans = BHTranslator.getInstance();

	private static final SimpleDateFormat S = new SimpleDateFormat(
			"EEEEE, 'den' dd.MM.yyyy");

	private static final Font TITLE_FONT = FontFactory.getFont(
			FontFactory.HELVETICA, 20, Font.BOLD);

	private static final Font SECTION1_FONT = FontFactory.getFont(
			FontFactory.HELVETICA, 16, Font.BOLD);

	private static final Font SECTION2_FONT = FontFactory.getFont(
			FontFactory.HELVETICA, 14, Font.BOLD);

	private Document doc;
	private Chapter report;

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
					newDocument(dp.getFilePath(), scenario);
					buildHeadData(scenario);
					buildScenarioData(scenario);
					closeDocument();
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
	public void exportScenario(DTOScenario scenario, String filePath) {
		newDocument(filePath, scenario);
		buildHeadData(scenario);
		buildScenarioData(scenario);
		closeDocument();
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
					newDocument(dp.getFilePath(), scenario);
					buildHeadData(scenario);
					buildScenarioData(scenario);
					buildResultDataDet(results);
					closeDocument();
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

		newDocument(filePath, scenario);
		buildHeadData(scenario);
		buildScenarioData(scenario);
		buildResultDataDet(results);
		closeDocument();
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
					newDocument(dp.getFilePath(), scenario);
					buildHeadData(scenario);
					buildScenarioData(scenario);
					buildResultDataStoch(results);
					closeDocument();
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

		newDocument(filePath, scenario);
		buildHeadData(scenario);
		buildScenarioData(scenario);
		buildResultDataStoch(results);
		closeDocument();
		log.debug("pdf scenario batch export completed " + filePath);
	}

	
	@Override
	public int getSupportedMethods() {
		return IImportExport.EXP_SCENARIO_RES_DET
				+ IImportExport.EX_SCENARIO_RES_STOCH
				+ IImportExport.EXP_SCENARIO + IImportExport.BATCH_EXPORT;
	}

	void newDocument(String path, DTOScenario scenario) {
		try {
			doc = new Document(PageSize.A4, 50, 50, 50, 50);

			PdfWriter.getInstance(doc, new FileOutputStream(path));

			doc.addAuthor("Business Horizon");
			doc.addSubject("Scenario Report");
			doc.addCreationDate();
			doc.addHeader("Header1", "Scenario Report header");
			doc.addTitle("Scenario Report Title");
			doc.open();

		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (DocumentException e) {
			log.error(e);
		}

	}

	void closeDocument() {
		try {
			doc.add(report);
			doc.close();
		} catch (DocumentException e) {
			log.error(e);
		}

	}

	void buildResultDataDet(Map<String, Calculable[]> resultMap) {
		Paragraph title;
		Section results;
		Section resultMapSection;
		PdfPTable t;

		results = buildResultHead();

		if (resultMap != null && resultMap.size() > 0) {
			title = new Paragraph("Result Map", SECTION2_FONT);
			resultMapSection = results.addSection(title, 2);
			resultMapSection.add(new Paragraph("\n"));
			t = new PdfPTable(2);
			for (Entry<String, Calculable[]> e : resultMap.entrySet()) {
				Calculable[] val = e.getValue();
				if (val.length >= 1) {
					t.addCell(trans.translate(e.getKey()));
					if (val[0] != null) {
						t.addCell(val[0].toString());
					} else {
						t.addCell(" ");
					}
				}
				if (val.length > 1) {
					for (int i = 1; i < val.length; i++) {
						t.addCell(" ");
						if (val[i] != null) {
							t.addCell(val[i].toString());
						} else {
							t.addCell(" ");
						}
					}
				}
			}

			resultMapSection.add(t);
		}
		// TODO Graphs

	}

	void buildResultDataStoch(DistributionMap distMap) {
		Paragraph title;
		Section results;
		Section distMapSection;
		PdfPTable t;

		results = buildResultHead();

		title = new Paragraph("Distribution Map", SECTION2_FONT);
		distMapSection = results.addSection(title, 2);

		t = new PdfPTable(2);
		for (Iterator<Entry<Double, Integer>> i = distMap.iterator(); i
				.hasNext();) {
			Entry<Double, Integer> val = i.next();
			t.addCell(val.getKey().toString());
			t.addCell(val.getValue().toString());
		}
		distMapSection.add(t);

		// TODO addCharts
	}

	Section buildResultHead() {
		Paragraph title;
		title = new Paragraph("Ergebnisse", SECTION1_FONT);

		return report.addSection(title, 1);
	}

	void buildHeadData(DTOScenario scenario) {
		Section data;
		PdfPTable t;

		Paragraph title = new Paragraph("Scenario Report - "
				+ scenario.get(DTOScenario.Key.IDENTIFIER), TITLE_FONT);
		report = new Chapter(title, 1);
		report
				.add(new Paragraph("Erstellt am " + S.format(new Date())
						+ "\n\n"));
		report.setNumberDepth(0);

		title = new Paragraph("Szenario Daten", SECTION1_FONT);
		data = report.addSection(title, 1);

		data.add(new Paragraph("\n"));
		t = new PdfPTable(2);
		for (Iterator<Entry<String, IValue>> i = scenario.iterator(); i
				.hasNext();) {
			Map.Entry<String, IValue> val = i.next();
			t.addCell(trans.translate(val.getKey()));
			t.addCell(val.getValue().toString());
		}
		data.add(t);
		data.add(new Paragraph("\n\n"));
	}

	@SuppressWarnings("unchecked")
	void buildScenarioData(DTOScenario scenario) {
		Paragraph title;
		Section input;
		Section period;
		PdfPTable t;

		title = new Paragraph("Periodendaten", SECTION1_FONT);
		input = report.addSection(title, 1);

		for (DTOPeriod d : scenario.getChildren()) {
			title = new Paragraph(d.get(DTOPeriod.Key.NAME).toString(),
					SECTION2_FONT);
			period = input.addSection(title, 2);
			period.add(new Paragraph("\n"));
			for (IPeriodicalValuesDTO pv : d.getChildren()) {
				t = new PdfPTable(2);
				for (Iterator<Entry<String, IValue>> i = pv.iterator(); i
						.hasNext();) {
					Map.Entry<String, IValue> val = i.next();
					t.addCell(trans.translate(val.getKey()));
					t.addCell(val.getValue().toString());
				}
				period.add(t);
				period.add(new Paragraph("\n\n"));
			}
		}
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
	public DTOProject importProject(DTOProject project,
			BHDataExchangeDialog exportDialog) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("This method has not been implemented");
	}

	@Override
	public DTOScenario importScenario(BHDataExchangeDialog importDialog) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("This method has not been implemented");
	}
}
