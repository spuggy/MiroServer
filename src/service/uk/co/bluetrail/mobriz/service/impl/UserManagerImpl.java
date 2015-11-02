package uk.co.bluetrail.mobriz.service.impl;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectRetrievalFailureException;
import uk.co.bluetrail.mobriz.dao.MiroTransactionDao;
import uk.co.bluetrail.mobriz.dao.UserDao;
import uk.co.bluetrail.mobriz.model.MiroTransaction;
import uk.co.bluetrail.mobriz.model.Role;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.service.UserExistsException;
import uk.co.bluetrail.mobriz.service.UserManager;
import uk.co.bluetrail.mobriz.util.RandomString;
import uk.co.bluetrail.mobriz.util.StringUtil;
import uk.co.bluetrail.mobriz.util.SurveyElementUtil;

import java.util.List;


/**
 * Implementation of UserManager interface.</p>
 * <p/>
 * <p>
 * <a href="UserManagerImpl.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class UserManagerImpl extends BaseManager implements UserManager {
    private UserDao dao;
    private MiroTransactionDao mtDao;


    /**
     * @param mtDao the mtDao to set
     */
    public void setMtDao(MiroTransactionDao mtDao) {
        this.mtDao = mtDao;
    }

    /**
     * Set the Dao for communication with the data layer.
     *
     * @param dao
     */
    public void setUserDao(UserDao dao) {
        this.dao = dao;
    }

    public List getUserQBE(User user) {
        return dao.getUserQBE(user);
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.UserManager#getUser(java.lang.String)
     */
    public User getUser(String userId) {
        return dao.getUser(new Long(userId));
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.UserManager#getUsers(uk.co.bluetrail.mobriz.model.User)
     */
    public List getUsers(User user) {
        return dao.getUsers(user);
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.UserManager#saveUser(uk.co.bluetrail.mobriz.model.User)
     */
    public void saveUser(User user) throws UserExistsException {
        // if new user, lowercase userId
        if (user.getVersion() == null) {
            user.setUsername(user.getUsername().toLowerCase());
        }
        try {

            dao.saveUser(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
        }
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.UserManager#removeUser(java.lang.String)
     */
    public void removeUser(String userId) {
        if (log.isDebugEnabled()) {
            log.debug("removing user: " + userId);
        }

        User user = dao.getUser(new Long(userId));
        user.setDeleted(true);
        user.setUsername(user.getUsername() + System.currentTimeMillis());
        user.setEnabled(false);
        dao.saveUser(user);
    }

    public User getUserByUsername(String username) throws UsernameNotFoundException {
        return (User) dao.loadUserByUsername(username);
    }

    public void setUser(User u) {
        dao.setCurrentUser(u);

    }

    public List getEnabledUsers() {
        return dao.getEnabledUsers();
    }

    public List getProjectUsers(String projectId) {
        return dao.getProjectUsers(new Long(projectId));
    }

    public User saveUser(User userDTO, String algorithm, Role userRole) throws UserExistsException {

        Long checkPoint = dao.getNextCheckPoint();
        User user = null;

        if (userDTO.getId() == null || userDTO.getId().equals("")) {
            //its new
            user = new User();
        } else {
            user = getUser(userDTO.getId().toString());
        }

        if (user == null) {
            log.error("could not find user to save with id =   " + userDTO.getId());
            throw new ObjectRetrievalFailureException(User.class, userDTO.getId());
        }

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());

        user.setAccount_id(new Long(0));
        user.setEmployeeRef("N/A");
        user.setEnabled(true);
        user.setPinNumber("N/A");
        user.setProject_id(userDTO.getProject_id());

        if (user.getPassword() == null || user.getPassword().equals("")) {
            String password = RandomString.randomstring();
            user.setPasswordHint(password);
            user.setConfirmPassword(password);
            user.setPassword(StringUtil.encodePassword(password, algorithm));
        }

        if (user.getRoles() == null || user.getRoles().size() == 0) {
            user.addRole(userRole);
        }

        try {
            String userName = user.getProject_id() + user.getFirstName() + user.getLastName();
            user.setUsername(Integer.toHexString(new String(userName).hashCode()));
        } catch (Exception e) {
            // ignore odd exception
        }


        this.saveUser(user);


        userDTO.setId(user.getId());
        return user;
    }

    public List getAdminUsers() {
        return dao.getAdminUsers();
    }

    public User buyReport(Long user_id, Long pUser_id) {

        User user = null;
        User pUser = null;

        user = dao.getUser(user_id);
        pUser = dao.getUser(pUser_id);

        user.setStatus(User.DOWNLOAD_REPORT);

        User pracUser = dao.getUser(pUser.getId());

        int balance = pracUser.getCreditBalance();

        //if(balance == 0 ) {
        //	log.error("trying to buy report when balance is zero " + pUser.getId());
        //	throw new SurveyException("trying to buy report when balance is zero " + pUser.getId());
        //}

        pracUser.setCreditBalance(balance - 1);


        //create trans
        MiroTransaction mt = new MiroTransaction();
        mt.setCredits(1);
        mt.setStatus(MiroTransaction.OK_BUY_REPORT);
        mt.setUser_id(pracUser.getId());
        mt.setPaymentTransId(user.getId().toString());
        mt.setPaymentStatusDetail(user.getFullName());
        mt.setCheckPoint(new Long(0));
        SurveyElementUtil.timeStamp(mt, user);

        SurveyElementUtil.timeStamp(user, user);

        user.setCreatedBy_id(pUser_id);  //hack so this gets set to the pracuser

        mtDao.saveMiroTransaction(mt);
        dao.saveUser(pracUser);
        dao.saveUser(user);

        return pUser;

    }

    public List getUsers(User user, int status) {
        return dao.getUsers(user, status);
    }

    public List getUsersByProjects(String[] selectedProjects) {

        Long[] selectedProjectIds = new Long[selectedProjects.length];

        for (int i = 0; i < selectedProjects.length; i++) {
            selectedProjectIds[i] = new Long(Long.parseLong(selectedProjects[i]));
        }

        //add status column too to only return paid ones ;
        return dao.getUsersByProjects(selectedProjectIds, User.DOWNLOAD_REPORT);

    }

    public List getUsers(String[] userIdsStr) {
        Long[] userIds = new Long[userIdsStr.length];

        for (int i = 0; i < userIdsStr.length; i++) {
            userIds[i] = new Long(Long.parseLong(userIdsStr[i]));
        }

        return dao.getUsers(userIds);
    }

    public void saveAsPurchased(Long id, Long sr_id) throws UserExistsException {

        User u = dao.getUser(id);

        u.setStatus(User.PURCHASE_REPORT);
        u.setResponse_id(sr_id);

        this.saveUser(u);

    }

    public List getCandidates(String project_id_str) {
        return getCandidates(project_id_str, UserManager.GET_CANDIDATES_LIMIT);
    }


    public List getCandidates(String project_id_str, int limit) {


        Long project_id = new Long(project_id_str);

        String[] fieldNames = new String[]{"project_id", "limit"};
        Object[] objects = new Object[]{project_id, limit};

        return dao.getSurveyNamedQuery("findUsersAndSurveyIdsForProject", fieldNames, objects);
    }
}
