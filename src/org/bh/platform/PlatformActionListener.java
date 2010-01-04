package org.bh.platform;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.bh.data.DTOPeriod;
import org.bh.data.DTOProject;
import org.bh.data.DTOScenario;
import org.bh.data.types.StringValue;
import org.bh.gui.swing.BHMainFrame;
import org.bh.gui.swing.BHStatusBar;
import org.bh.gui.swing.BHTreeNode;
import org.bh.gui.swing.IBHAction;
import org.bh.platform.PlatformController.BHTreeModel;
import org.bh.platform.i18n.BHTranslator;

/**
 * The PlatformActionListener handles all actions that are fired by a button
 * etc. of the platform.
 */
class PlatformActionListener implements ActionListener {
	
	private static final Logger log = Logger.getLogger(PlatformActionListener.class);

	BHMainFrame bhmf;
	ProjectRepositoryManager projectRepoManager;
	PlatformController pC;
	
	public PlatformActionListener(BHMainFrame bhmf, ProjectRepositoryManager projectRepoManager, PlatformController platformController){
		this.bhmf = bhmf;
		this.projectRepoManager = projectRepoManager;
		this.pC = platformController;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent aEvent) {

		// get actionKey of fired action
		PlatformKey actionKey = ((IBHAction) aEvent.getSource()).getPlatformKey();
		
		//do right action...
		switch (actionKey) {
		
		/*
		 * Clear current workspace
		 * 
		 * @author Michael Löckelt
		 */
		case FILENEW:
			this.fileNew();
			break;
		
		/*
		 * Open a workspace 
		 * 
		 * @author Michael Löckelt
		 */
		case FILEOPEN:
			// this.fileOpen();
			break;
			
		/*
		 * Save the whole workspace using the already defined filepath
		 * 
		 * @author Michael Löckelt
		 */
			
		case FILESAVE:
			// this.fileSave();
			break;
			
		/*
		 * Save the whole workspace - incl. filepath save dialog
		 * 
		 * @author Michael Löckelt
		 */
			
		case FILESAVEAS:
			// this.fileSaveAs();
			break;
			
		case FILEQUIT:
			//TODO Prüfen und ggf. implementieren!
			int i = JOptionPane.showConfirmDialog(bhmf, Services.getTranslator().translate("Pquit"));
			if (i == JOptionPane.YES_OPTION) {
				// TODO finalize application.
				bhmf.dispose();
			}
			break;
			
		case PROJECTCREATE:
			this.createProject();
			break;
			
		case PROJECTDUPLICATE:
			//TODO Zuckschwerdt.Lars
//			TreePath currentDuplicateProjectSelection = bhmf.getBHTree().getSelectionPath();
//			if(currentDuplicateProjectSelection != null){
//				//Zugriff auf markiertes Projekt
//				BHTreeNode duplicateProjectNode = (BHTreeNode)bhmf.getBHTree().getSelectionPath().getLastPathComponent();
//				
//				//zu kopierendes Project in eigene Variable
//				DTOProject duplicateProject = (DTOProject)duplicateProjectNode.getUserObject();
//				
//				//neues DTOProject mit Referenz auf den Klon
//				DTOProject newProject = (DTOProject)duplicateProject.clone();
//				
//				BHTreeNode newProjectNode = new BHTreeNode(newProject);
//				((DefaultTreeModel)bhmf.getBHTree().getModel()).insertNodeInto(
//						newProjectNode, 
//						(DefaultMutableTreeNode)bhmf.getBHTree().getModel().getRoot(), 
//						((DefaultMutableTreeNode)bhmf.getBHTree().getModel().getRoot()).getChildCount()
//				);
//				for(int x = 0; x <= newProject.getChildren().size(); x++){
//					System.out.println(x);
//				}
//				
//				
//				//last steps: unfold tree to new element, set focus and start editing
//				bhmf.getBHTree().scrollPathToVisible(new TreePath(newProjectNode.getPath()));
//				bhmf.getBHTree().startEditingAtPath(new TreePath(newProjectNode.getPath()));
//				
//				
//			}
			break;
			
		// TODO Katzor.Marcus
		case PROJECTIMPORT:
			
			break;
		
		// TODO Katzor.Marcus
			
		case PROJECTEXPORT:
			DTOProject t = new DTOProject();
			break;
			
		case PROJECTREMOVE:
			TreePath currentRemoveProjectSelection = bhmf.getBHTree().getSelectionPath();
			if(currentRemoveProjectSelection != null){
				BHTreeNode removeProjectNode = (BHTreeNode)bhmf.getBHTree().getSelectionPath().getLastPathComponent();
				if(removeProjectNode.getUserObject() instanceof DTOProject){
					((BHTreeModel) bhmf.getBHTree().getModel()).removeNodeFromParent(removeProjectNode);
					projectRepoManager.removeProject((DTOProject) removeProjectNode.getUserObject());
				} else {
					BHStatusBar.getInstance().setHint(BHTranslator.getInstance().translate("EisSelectProject"), true);										
				}
			}
			break;
			
		case SCENARIOCREATE:
			this.createScenario();
			break;
			
		case SCENARIODUPLICATE:
			//TODO Zuckschwerdt.Lars
			break;
			
		case SCENARIOMOVE:
			//TODO Prüfen und ggf. implementieren!
			break;
			
		case SCENARIOREMOVE:
			TreePath currentRemoveScenarioSelection = bhmf.getBHTree().getSelectionPath();
			if(currentRemoveScenarioSelection != null){
				BHTreeNode removeScenarioNode = (BHTreeNode)bhmf.getBHTree().getSelectionPath().getLastPathComponent();
				if(removeScenarioNode.getUserObject() instanceof DTOScenario){
					((BHTreeModel) bhmf.getBHTree().getModel()).removeNodeFromParent(removeScenarioNode);
					((DTOScenario)((BHTreeNode)removeScenarioNode.getParent()).getUserObject()).removeChild((DTOPeriod) removeScenarioNode.getUserObject());
				} else {
					BHStatusBar.getInstance().setHint(BHTranslator.getInstance().translate("EisSelectScenario"), true);					
				}
			}
			break;
			
		case BILANZGUVSHOW:
			//TODO Prüfen und ggf. implementieren!
			break;
			
		case BILANZGUVCREATE:
			//TODO Prüfen und ggf. implementieren!
			break;
			
		case BILANZGUVIMPORT:
			//TODO Prüfen und ggf. implementieren!
			break;
			
		case BILANZGUVREMOVE:
			//TODO Prüfen und ggf. implementieren!
			break;
			
		case OPTIONSCHANGE:
			//TODO Tietze.Patrick
			break;
			
		case HELPUSERHELP:
			
			System.out.println("HELPUSERHELP gefeuert");
			JFrame frame = new JFrame();
			//TODO Hartgecodeder String raus!! LZ
			frame.setTitle("Business Horizon Help");
			frame.setSize(610,600);
			frame.getContentPane().add(new BHHelpSystem("userhelp"));
			frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			frame.setResizable(false);
			frame.setVisible(true);
			break;
			
		case HELPMATHHELP:
			System.out.println("HELPMATHHELP gefeuert");
			JFrame mathframe = new JFrame();
			//TODO Hartgecodeder String raus!! LZ
			mathframe.setTitle("Business Horizon Mathematische Erläuterungen");
			mathframe.setSize(610,600);
			mathframe.getContentPane().add(new BHHelpSystem("mathhelp"));
			mathframe.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			mathframe.setResizable(false);
			mathframe.setVisible(true);
			break;
			
		case HELPINFO:
			//TODO Prüfen und ggf. implementieren!
			break;
			
		case TOOLBAROPEN: 
			//TODO Prüfen und ggf. implementieren!
			break;
			
		case TOOLBARSAVE: 
			//TODO Prüfen und ggf. implementieren!
			break; 
			
		case TOOLBARADDPRO:
			this.createProject();
			break;
			
		case TOOLBARADDS: 
			this.createScenario();							
			break;
		
		case TOOLBARADDPER:
			this.createPeriod();
			break;
			
		case TOOLBARREMOVE:
			
			TreePath currentSelection = bhmf.getBHTree().getSelectionPath();
			//is a node selected?
			if (currentSelection != null) {
				//remove node from GUI...
				 BHTreeNode currentNode = (BHTreeNode)bhmf.getBHTree().getSelectionPath().getLastPathComponent();
				 ((BHTreeModel) bhmf.getBHTree().getModel()).removeNodeFromParent(currentNode);
				 
				 //..and from data model
				 if(currentNode.getUserObject() instanceof DTOProject){
					 projectRepoManager.removeProject((DTOProject) currentNode.getUserObject());
				
				 }else if(currentNode.getUserObject() instanceof DTOScenario){
					 ((DTOProject)((BHTreeNode)currentNode.getParent()).getUserObject()).removeChild((DTOScenario) currentNode.getUserObject());
				
				 }else if(currentNode.getUserObject() instanceof DTOPeriod){
					 ((DTOScenario)((BHTreeNode)currentNode.getParent()).getUserObject()).removeChild((DTOPeriod) currentNode.getUserObject());
					 
				 }
			}
			
			
			
			break;
			
		default:
			System.out.println("Was anderes, und zwar: "+actionKey.getActionKey());
			break;
		}
	}
	
	private void fileNew() {
		log.debug("handling FILENEW event");
		
		projectRepoManager.clearProjectList();
		pC.setupTree(bhmf, projectRepoManager);
	}
	
	private void fileOpen() {
		log.debug("handling FILEOPEN event");
		
		// create a open-dialog
		int returnVal = bhmf.getChooser().showOpenDialog(bhmf);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			System.out.println("You chose to open this file: "
					+ bhmf.getChooser().getSelectedFile().getName());

		}
		
		// create a PlatformPersistence instance incl. filepath
		PlatformPersistence myOpener = new PlatformPersistence(bhmf.getChooser().getSelectedFile());
		
		// open already provided file
		ArrayList<DTOProject> projectList = myOpener.openFile();
		
		// replace ProjectRepository
		projectRepoManager.replaceProjectList(projectList);
		
		// TODO rebuild Tree
		pC.setupTree(bhmf, projectRepoManager);
		
		log.debug("file" + bhmf.getChooser().getSelectedFile() + "successfully opened");
	}
	
	private void fileSave() {
		log.debug("handling FILESAVE event");
	}
	
	private void fileSaveAs() {
		log.debug("handling FILESAVEAS event");
		
		// create save dialog
		int returnSave = bhmf.getChooser().showSaveDialog(bhmf);
		if (returnSave == JFileChooser.APPROVE_OPTION) {
			System.out.println("You chose to save this file: "
					+ bhmf.getChooser().getSelectedFile().getName());

		}
		// create a PlatformPersistence instance incl. filepath
		PlatformPersistence mySaver = new PlatformPersistence(bhmf.getChooser().getSelectedFile());
		
		// perform save
		mySaver.saveFile(projectRepoManager.getRepositoryList());
		
		log.debug("ProjectRepository saved to " + bhmf.getChooser().getSelectedFile());
	}
	
	private void createProject(){
		//Create new project
		DTOProject newProject = new DTOProject();
		//TODO hardgecodeder String raus! AS
		newProject.put(DTOProject.Key.NAME, new StringValue("neues Projekt"));
		//add it to DTO-Repository
		projectRepoManager.addProject(newProject);
		
		//and create a Node for tree on gui
		BHTreeNode newProjectNode = bhmf.getBHTree().addProjectNode(newProject, bhmf);
		
		//last steps: unfold tree to new element, set focus and start editing
		bhmf.getBHTree().scrollPathToVisible(new TreePath(newProjectNode.getPath()));
		bhmf.getBHTree().startEditingAtPath(new TreePath(newProjectNode.getPath()));
		
	}
	private void createScenario(){
		//If a path is selected...
		if(bhmf.getBHTree().getSelectionPath() != null){
			//...create new scenario
			DTOScenario newScenario = new DTOScenario();
			//TODO hardgecodeder String raus! AS
			newScenario.put(DTOScenario.Key.NAME, new StringValue("neues Scenario"));
			
			//...add it to DTO-Repository
			((DTOProject)((BHTreeNode)bhmf.getBHTree().getSelectionPath().getPathComponent(1)).getUserObject()).addChild(newScenario);
			
			//...and insert it into GUI-Tree
			BHTreeNode newScenarioNode = bhmf.getBHTree().addScenarioNode(newScenario, bhmf);
			
			//last steps: unfold tree to new element, set focus and start editing
			bhmf.getBHTree().scrollPathToVisible(new TreePath(newScenarioNode.getPath()));
			bhmf.getBHTree().startEditingAtPath(new TreePath(newScenarioNode.getPath()));
		}
		
	}
	private void createPeriod(){
		//If a scenario or a period is selected...
		if(bhmf.getBHTree().getSelectionPath()!=null && bhmf.getBHTree().getSelectionPath().getPathCount()>2){
			//...create new period
			DTOPeriod newPeriod = new DTOPeriod();
			//TODO hardgecodeder String raus! AS
			newPeriod.put(DTOPeriod.Key.NAME, new StringValue("neue Periode"));
			
			//...add it to DTO-Repository
			((DTOScenario)((BHTreeNode)bhmf.getBHTree().getSelectionPath().getPathComponent(2)).getUserObject()).addChild(newPeriod);
			
			//...and insert it into GUI-Tree
			BHTreeNode newPeriodNode = new BHTreeNode(newPeriod);
			((BHTreeModel)bhmf.getBHTree().getModel()).insertNodeInto(
					newPeriodNode,
					(BHTreeNode)(bhmf.getBHTree().getSelectionPath().getPathComponent(2)), 
					((BHTreeNode) bhmf.getBHTree().getSelectionPath().getPathComponent(2)).getChildCount()
			);
			
			//last steps: unfold tree to new element, set focus and start editing
			bhmf.getBHTree().scrollPathToVisible(new TreePath(newPeriodNode.getPath()));
			bhmf.getBHTree().startEditingAtPath(new TreePath(newPeriodNode.getPath()));
		}
	}

}
