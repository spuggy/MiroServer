
package uk.co.bluetrail.mobriz.service;

import java.util.List;

import uk.co.bluetrail.mobriz.service.Manager;
import uk.co.bluetrail.mobriz.model.MiroProject;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.dao.MiroProjectDao;

public interface MiroProjectManager extends Manager {
	  public MiroProject getMiroProject(String id);   
	
	
	/**
     * Retrieves all of the miroProjects
     * @param user
     */
    public List getMiroProjects(MiroProject miroProject, User user);

    /**
     * Gets miroProject's information based on id.
     * @param id the miroProject's id
     * @param user
     * @return miroProject populated miroProject object
     */
    public MiroProject getMiroProject(final String id, User user);

    /**
     * Saves a miroProject's information
     * @param miroProject the object to be saved
     * @param currentUser
     */
    public void saveMiroProject(MiroProject miroProject, User currentUser);

    /**
     * Removes a miroProject from the database by id
     * @param id the miroProject's id
     * @param user
     */
    public void removeMiroProject(final String id, User user);
}

