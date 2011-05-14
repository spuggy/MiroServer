
package uk.co.bluetrail.mobriz.service;

import java.util.List;

import uk.co.bluetrail.mobriz.model.Constraint;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.dao.ConstraintDAO;

public interface ConstraintManager extends Manager {

	public List getUpdated(Long ckp, boolean deleted, User newParam);
	
	public List getConstraints(Long sid);
	
	
    /**
     * Setter for DAO, convenient for unit testing
     */
    public void setConstraintDAO(ConstraintDAO constraintDAO);

    /**
     * Retrieves all of the constraints
     */
    public List getConstraints(Constraint constraint);

    /**
     * Gets constraint's information based on id.
     * @param id the constraint's id
     * @return constraint populated constraint object
     */
    public Constraint getConstraint(final String id);

    /**
     * Saves a constraint's information
     * @param constraint the object to be saved
     */
    public void saveConstraint(Constraint constraint, User user);

    /**
     * Removes a constraint from the database by id
     * @param id the constraint's id
     */
    public void removeConstraint(final String id);
}

