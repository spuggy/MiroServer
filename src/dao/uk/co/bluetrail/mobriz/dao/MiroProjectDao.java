
package uk.co.bluetrail.mobriz.dao;

import java.util.List;

import uk.co.bluetrail.mobriz.dao.Dao;
import uk.co.bluetrail.mobriz.model.MiroProject;
import uk.co.bluetrail.mobriz.model.User;

public interface MiroProjectDao extends Dao {

    /**
     * Retrieves all of the miroProjects
     * @param user TODO
     */
    public List getMiroProjects(MiroProject miroProject, User user);  

    /**
     * Gets miroProject's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if 
     * nothing is found.
     * 
     * @param id the miroProject's id
     * @return miroProject populated miroProject object
     */
    public MiroProject getMiroProject(final Long id);

    /**
     * Saves a miroProject's information
     * @param miroProject the object to be saved
     */    
    public void saveMiroProject(MiroProject miroProject);

    /**
     * Removes a miroProject from the database by id
     * @param id the miroProject's id
     */
    public void removeMiroProject(final Long id);
}

