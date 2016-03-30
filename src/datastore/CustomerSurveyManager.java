/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.logging.Logger;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the CustomerSurvey class.
 * 
 */

public class CustomerSurveyManager {

	private static final Logger log = 
        Logger.getLogger(CustomerSurveyManager.class.getName());
	
	/**
     * Get a CustomerSurvey instance from the datastore given the CustomerSurvey key.
     * @param key
     * 			: the CustomerSurvey's key
     * @return CustomerSurvey instance, null if CustomerSurvey is not found
     */
	public static CustomerSurvey getCustomerSurvey(Long key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		CustomerSurvey customerSurvey;
		try  {
			customerSurvey = pm.getObjectById(CustomerSurvey.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return customerSurvey;
	}
	
	/**
     * Get all CustomerSurvey instances from the datastore.
     * @return All CustomerSurvey instances
     * TODO: Inefficient touching of objects
     */
	@SuppressWarnings("unchecked")
	public static List<CustomerSurvey> getAllCustomerSurveys() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(CustomerSurvey.class);

        List<CustomerSurvey> result = null;
        try {
        	result = (List<CustomerSurvey>) query.execute();
            // touch all elements
            for (CustomerSurvey customerSurvey : result)
            	customerSurvey.getKey();
        } 
        finally {
        	pm.close();
            query.closeAll();
        }

        return result;
    }
	
	/**
     * Get all CustomerSurvey instances from the datastore
     * that belong to this survey.
     * @return All CustomerSurvey instances
     * TODO: Inefficient touching of objects
     */
	@SuppressWarnings("unchecked")
	public static List<CustomerSurvey> getAllCustomerSurveys(Key survey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(CustomerSurvey.class);
        query.setFilter("survey == skey");
        query.declareParameters(Key.class.getName() + " skey");

        List<CustomerSurvey> result = null;
        try {
        	result = (List<CustomerSurvey>) query.execute(survey);
            // touch all elements
            for (CustomerSurvey customerSurvey : result)
            	customerSurvey.getKey();
        } 
        finally {
        	pm.close();
            query.closeAll();
        }

        return result;
    }
	
	/**
     * Put CustomerSurvey into datastore.
     * Stores the given CustomerSurvey instance in the datastore calling the 
     * PersistenceManager's makePersistent() method.
     * @param customerSurvey
     * 			: the CustomerSurvey instance to store
     */
	public static void putCustomerSurvey(
			CustomerSurvey customerSurvey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.makePersistent(customerSurvey);
			tx.commit();
			log.info("CustomerSurvey stored successfully in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Delete CustomerSurvey from datastore.
    * Deletes the CustomerSurvey corresponding to the given key
    * from the datastore calling the PersistenceManager's 
    * deletePersistent() method.
    * @param key
    * 			: the key of the CustomerSurvey instance to delete
    */
	public static void deleteCustomerSurvey(Long key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			CustomerSurvey customerSurvey = 
					pm.getObjectById(CustomerSurvey.class, key);
			tx.begin();
			pm.deletePersistent(customerSurvey);
			tx.commit();
			log.info("CustomerSurvey deleted successfully from datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
}
