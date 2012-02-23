package uk.co.bluetrail.mobriz.dao;




import java.util.List;

import uk.co.bluetrail.mobriz.dao.Dao;
import uk.co.bluetrail.mobriz.model.MiroTeam;
import uk.co.bluetrail.mobriz.model.User;

public interface MiroTeamDao extends Dao {

    /**
     * Retrieves all of the miroTeams
     * @param user TODO
     */
    public List getMiroTeams(MiroTeam miroTeam, User user);  

    /**
     * Gets miroTeam's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if 
     * nothing is found.
     * 
     * @param id the miroTeam's id
     * @return miroTeam populated miroTeam object
     */
    public MiroTeam getMiroTeam(final Long id);

    /**
     * Saves a miroTeam's information
     * @param miroTeam the object to be saved
     */    
    public void saveMiroTeam(MiroTeam miroTeam);

    /**
     * Removes a miroTeam from the database by id
     * @param id the miroTeam's id
     */
    public void removeMiroTeam(final Long id);

	
	
}
