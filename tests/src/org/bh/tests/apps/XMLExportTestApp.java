package org.bh.tests.apps;

import java.awt.FlowLayout;
import java.util.List;
import java.util.Random;
import java.util.ServiceLoader;

import javax.swing.JFrame;

import org.bh.controller.IController;
import org.bh.data.DTOPeriod;
import org.bh.data.DTOProject;
import org.bh.data.DTOScenario;
import org.bh.data.IDTO;
import org.bh.data.types.DoubleValue;
import org.bh.data.types.IValue;
import org.bh.data.types.IntegerValue;
import org.bh.data.types.IntervalValue;
import org.bh.data.types.StringValue;
import org.bh.platform.PluginManager;
import org.bh.plugin.directinput.DTODirectInput;
import org.bh.plugin.gcc.data.DTOGCCBalanceSheet;
import org.bh.plugin.gcc.data.DTOGCCProfitLossStatementCostOfSales;
import org.bh.plugin.xmldataexchange.XMLDataExchangeController;

public class XMLExportTestApp extends JFrame {


	public static void main(String[] args)
	{
		if (args.length != 1)
			System.out.println("Programmaufruf: java XMLExportTestApp [import|export]");
		else
			new XMLExportTestApp(args[0]);
	}
	
	
	
	public XMLExportTestApp(String para)
	{
		setSize(300, 200);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		setVisible(true);
		
		
		PluginManager.getInstance().loadAllServices(IController.class);
		ServiceLoader<IController> controller = PluginManager.getInstance().getServices(IController.class);
		XMLDataExchangeController exportController = null;
		for (IController contrl : controller)
		{			
			contrl.getClass().getPackage().getName().equals("org.bh.plugin.xmldataexchange");
			{
				exportController = (XMLDataExchangeController) contrl;				
				break;
			}
		}
		
		exportController.setModel(createTestDTOStructure());		
		
		if (exportController == null)
			return;
		
		if (para.equalsIgnoreCase("export"))	
			exportController.setExportView();
		else
			exportController.setImportView();
		
		add(exportController.getViewPanel());
			
		
	}
	
	
	private DTOProject createTestDTOStructure()
	{
		// One project
		DTOProject result = new DTOProject();
		result.put(DTOProject.Key.COMMENT, new StringValue("This is a comment."));
		result.put(DTOProject.Key.NAME, new StringValue("My first project"));
		
			// 1. Scenario
			DTOScenario sec1 = new DTOScenario(true);	
			fillDTO(sec1);
			
				// 1. Period
				DTOPeriod per1 = new DTOPeriod();
				fillDTO(per1);
					
					// Balance Sheet
					DTOGCCBalanceSheet bs = new DTOGCCBalanceSheet();
					fillDTO(bs);
					
					// GuV
					DTOGCCProfitLossStatementCostOfSales guv = new DTOGCCProfitLossStatementCostOfSales();
					fillDTO(guv);
					
				per1.addChild(bs);
				per1.addChild(guv);
					
								
				// 2. Period
				DTOPeriod per2 = new DTOPeriod();
				fillDTO(per2);
					
					// Direct Input
					DTODirectInput dinput = new DTODirectInput();
					fillDTO(dinput);
				
				per2.addChild(dinput);
				
				
			sec1.addChild(per1);
			sec1.addChild(per2);
		
		result.addChild(sec1);
		
		return result;
	}
	
	
	private void fillDTO(IDTO dto)
	{
		List<String> keys = dto.getKeys();
		
		for (String key : keys)
		{
			dto.put(key, getRandomValue());
		}
	}
	
	private IValue getRandomValue()
	{
		double random = Math.random() * 5;
		
		if (random >= 0 && random < 1)
			return new DoubleValue(Math.random() * 54546);
		else if (random >= 1 && random < 2)
			return new IntegerValue((int) Math.random() * 54648);
		else if (random >= 2 && random < 3)
			return new IntervalValue(Math.random() * 54147, Math.random() * 78412);
		else
		{
			Random r = new Random();
			String token = Long.toString(Math.abs(r.nextLong()), 36);
			return new StringValue(token);
		}
	}
	
	
}
