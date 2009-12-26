package org.bh.platform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bh.data.*;

/**
 *This manager provides necessary classes for global project repository management.
 *
 *It's designed to be mainly used by the PlatformController.
 *
 *
 * @author Marco Hammel
 * @author Alexander Schmalzhaf
 * 
 * @version 0.1 2009/12/26 Alexander Schmalzhaf
 */

public class ProjectRepositoryManager {
	
    private List<DTOProject> repositoryList = new ArrayList<DTOProject>() ;
    
    /**
     * Adds a new project to the actual repository.
     * 
     * @param newProject
     */
    public void addProject(DTOProject newProject){
    	
    	repositoryList.add(newProject);
    }
    
    /**
     * Removes a project out of the actual repository if it exists. 
     * Identifier for selection of the project which should be deleted is its name. 
     * 
     * @param name name of project to delete
     */
    public void removeProject(String name){
    	
    	Iterator<DTOProject> rLIterator = repositoryList.iterator();
    	DTOProject tempProject;
    	
    	//walk through repository and check projects
    	while(rLIterator.hasNext()){
    		tempProject = rLIterator.next();
    		if(tempProject.get("NAME").toString().equalsIgnoreCase(name)){
    			repositoryList.remove(tempProject);
    			break;
    		}
    	}
    }
    
    /**
     * Allows the replacement of the complete repository.
     * Necessary e.g. while loading another repo-file.
     * 
     * 
     * @param newRepoList
     */
    protected void replaceProjectList(ArrayList<DTOProject> newRepoList){
    	
    	this.repositoryList = newRepoList;
    }
    
    /**
     * Creates a new, clean and empty repository.
     */
    protected void clearProjectList(){
    	
    	this.repositoryList = new ArrayList<DTOProject>();
    }
    
    /**
     * Allocates the complete repository in the form of a reference onto the list.
     * Necessary e.g. for saving the current status of the repository.
     * 
     * @return repository List
     */
    protected List<DTOProject> getRepositoryList(){
    	
    	return this.repositoryList;
    }
    
}