package uk.co.bluetrail.mobriz.service;

import java.util.List;

import uk.co.bluetrail.mobriz.model.MiroTeam;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.Manager;



public interface MiroTeamManager extends Manager{
	
	/**
     * Retrieves all of the miroTeams
     */
    public List getMiroTeams(MiroTeam miroTeam, User user );

    /**
     * Gets miroTeam's information based on id.
     * @param id the miroTeam's id
     * @return miroTeam populated miroTeam object
     */
    public MiroTeam getMiroTeam(final String id);  

    /**
     * Saves a miroTeam's information
     * @param miroTeam the object to be saved
     * @param user TODO
     */
    public void saveMiroTeam(MiroTeam miroTeam, User user);

    /**
     * Removes a miroTeam from the database by id
     * @param id the miroTeam's id
     */
    public void removeMiroTeam(final String id);

	public List getUnprocessedTeams(int miroDocLimit);

	public boolean createPDF(MiroTeam mt, String filePath);
	

}
