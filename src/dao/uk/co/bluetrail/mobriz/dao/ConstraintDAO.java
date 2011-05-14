
package uk.co.bluetrail.mobriz.dao;

import java.util.List;

import uk.co.bluetrail.mobriz.model.Constraint;
import uk.co.bluetrail.mobriz.model.User;

public interface ConstraintDAO extends Dao {

		
	
	public List getUpdated(Long ckp, boolean deleted, User newParam);
	
	/**
     * Retrieves all of the for a sid
     */
	public List getConstraints(Long sid);

	
	
	 /**
     * Retrieves all of the constraints
     */
    public List getConstraints(Constraint constraint);

    /**
     * Gets constraint's information based on primary key. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if 
     * nothing is found.
     * 
     * @param id the constraint's id
     * @return constraint populated constraint object
     */
    public Constraint getConstraint(final Long id);

    /**
     * Saves a constraint's information
     * @param constraint the object to be saved
     */	
    public void saveConstraint(Constraint constraint);

	/**
     * Removes a constraint from the database by id
     * @param id the constraint's id
     */
    public void removeConstraint(final Long id);
}

