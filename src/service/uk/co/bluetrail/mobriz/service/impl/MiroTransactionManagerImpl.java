
package uk.co.bluetrail.mobriz.service.impl;

import java.util.List;

import uk.co.bluetrail.mobriz.service.impl.BaseManager;
import uk.co.bluetrail.mobriz.model.MiroTransaction;
import uk.co.bluetrail.mobriz.model.User;
import uk.co.bluetrail.mobriz.dao.MiroTransactionDao;
import uk.co.bluetrail.mobriz.dao.UserDao;
import uk.co.bluetrail.mobriz.service.MiroTransactionManager;

public class MiroTransactionManagerImpl extends BaseManager implements MiroTransactionManager {
    private MiroTransactionDao dao;
    private UserDao userDao ;
    
    

   

	/**
	 * @param userDao the userDao to set
	 */
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	/**
     * Set the Dao for communication with the data layer.
     * @param dao
     */
    public void setMiroTransactionDao(MiroTransactionDao dao) {
        this.dao = dao;
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.MiroTransactionManager#getMiroTransactions(uk.co.bluetrail.mobriz.model.MiroTransaction)
     */
    public List getMiroTransactions(final MiroTransaction miroTransaction) {
        return dao.getMiroTransactions(miroTransaction);
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.MiroTransactionManager#getMiroTransaction(String id)
     */
    public MiroTransaction getMiroTransaction(final String id) {
        return dao.getMiroTransaction(new Long(id));
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.MiroTransactionManager#saveMiroTransaction(MiroTransaction miroTransaction)
     */
    public void saveMiroTransaction(MiroTransaction miroTransaction) {
    	miroTransaction.setCheckPoint(new Long(0));
    	
    	if(miroTransaction.getStatus()==MiroTransaction.OK) {
    		//it is ok so up date the user doc creditbalance
    		miroTransaction.setStatus(MiroTransaction.OK_PROCESSED);
    		User user = userDao.getUser(miroTransaction.getUser_id());
    		int creditBalance = user.getCreditBalance();
    		user.setCreditBalance(creditBalance+miroTransaction.getCredits());
    		userDao.saveUser(user);
    		 dao.saveMiroTransaction(miroTransaction);
    		 return;
    	}
    	
    	if(miroTransaction.getStatus()==MiroTransaction.FAILURE) {
    		miroTransaction.setStatus(MiroTransaction.FAILURE_PROCESSED);
    		dao.saveMiroTransaction(miroTransaction);
    		return;
    	}
    	
    	//normal save 
    	dao.saveMiroTransaction(miroTransaction);
       
    }

    /**
     * @see uk.co.bluetrail.mobriz.service.MiroTransactionManager#removeMiroTransaction(String id)
     */
    public void removeMiroTransaction(final String id) {
        dao.removeMiroTransaction(new Long(id));
    }
}
