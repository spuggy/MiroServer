
package uk.co.bluetrail.mobriz.service.impl;

import java.util.List;

import uk.co.bluetrail.mobriz.model.Constraint;
import uk.co.bluetrail.mobriz.model.SurveyElement;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.dao.ConstraintDAO;
import uk.co.bluetrail.mobriz.service.ConstraintManager;
import uk.co.bluetrail.mobriz.util.SurveyElementUtil;

public class ConstraintManagerImpl extends BaseManager implements ConstraintManager {
    private ConstraintDAO dao;

    public List getUpdated(Long ckp, boolean deleted, User newParam){
		return dao.getUpdated(ckp, deleted, null);
	}
    
    public List getConstraints(Long sid){
    	return dao.getConstraints(sid);
    }
    
    
    /**
     * Set the DAO for communication with the data layer.
     * @param dao
     */
    public void setConstraintDAO(ConstraintDAO dao) {
        this.dao = dao;
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.ConstraintManager#getConstraints(uk.co.bluetrail.mobriz.model.Constraint)
     */
    public List getConstraints(final Constraint constraint) {
    	return dao.getConstraints(constraint);
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.ConstraintManager#getConstraint(String id)
     */
    public Constraint getConstraint(final String id) {
        return dao.getConstraint(new Long(id));
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.ConstraintManager#saveConstraint(Constraint constraint)
     */
    public void saveConstraint(Constraint constraint,User user) {
    	SurveyElementUtil.timeStamp((SurveyElement) constraint, user);
    	dao.saveConstraint(constraint);
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.ConstraintManager#removeConstraint(String id)
     */
    public void removeConstraint(final String id) {
        dao.removeConstraint(new Long(id));
    }
}
