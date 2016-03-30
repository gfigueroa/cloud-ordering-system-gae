/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.List;
//import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the SystemCounters class.
 * 
 */

public class SystemCountersManager {
	
	//private static final Logger log = 
	//		Logger.getLogger(SystemCountersManager.class.getName());
	
	/**
     * Get SystemCounters instance from the datastore.
     * @return The only SystemCounters instance there should be
     */
	public static SystemCounters getSystemCounters() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        SystemCounters systemCounters = null;
			
        try {
        	List<SystemCounters> systemCounterss = getAllSystemCounterss();
        	// Create systemCounters if it hasn't been created yet
        	if (systemCounterss == null || systemCounterss.isEmpty()) {
        		systemCounters = new SystemCounters();
        		pm.makePersistent(systemCounters);
        	}
        	else {
    			systemCounters = systemCounterss.get(0);
    		}
        } 
        finally {
        	pm.close();
        }

        return systemCounters;
    }
	
	/**
     * Get all SystemCounters instances from the datastore.
     * @return All SystemCounters instances
     * TODO: Make "touching" of systemCounterss more efficient
     */
	@SuppressWarnings("unchecked")
	public static List<SystemCounters> getAllSystemCounterss() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(SystemCounters.class);

        List<SystemCounters> systemCounterss = null;
        try {
        	systemCounterss = (List<SystemCounters>) query.execute();
            // touch all elements
            for (SystemCounters s : systemCounterss)
                s.getSystemCountersTime();
        } 
        finally {
        	pm.close();
            query.closeAll();
        }

        return systemCounterss;
    }
	
	/**
    * Increase the given counter by the given increment.
    */
	public static void increaseCounter(
			SystemCounters.Counter counter, Long increment) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			List<SystemCounters> systemCounterss = getAllSystemCounterss();
			SystemCounters systemCounters = null;
			tx.begin();
			// Create systemCounters if it hasn't been created yet
			if (systemCounterss == null || systemCounterss.isEmpty()) {
				systemCounters = new SystemCounters();
				pm.makePersistent(systemCounters);
			}
			else {
				systemCounters = pm.getObjectById(SystemCounters.class, 
						systemCounterss.get(0).getKey());
			}
			systemCounters.increaseCounter(counter, increment);
			tx.commit();
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
}
