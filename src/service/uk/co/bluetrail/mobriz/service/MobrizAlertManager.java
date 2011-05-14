
package uk.co.bluetrail.mobriz.service;

import java.util.List;

import uk.co.bluetrail.mobriz.service.Manager;
import uk.co.bluetrail.mobriz.model.MobrizAlert;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.dao.MobrizAlertDao;

public interface MobrizAlertManager extends Manager {
    /**
     * Retrieves all of the mobrizAlerts
     * @param currentUser TODO
     */
    public List getMobrizAlerts(User currentUser);

    /**
     * Gets mobrizAlert's information based on id.
     * @param id the mobrizAlert's id
     * @return mobrizAlert populated mobrizAlert object
     */
    public MobrizAlert getMobrizAlert(final String id);

    /**
     * Saves a mobrizAlert's information
     * @param mobrizAlert the object to be saved
     * @param currentUser TODO
     */
    public void saveMobrizAlert(MobrizAlert mobrizAlert, User currentUser);

    /**
     * Removes a mobrizAlert from the database by id
     * @param id the mobrizAlert's id
     * @param currentUser TODO
     */
    public void removeMobrizAlert(final String id, User currentUser);
}

