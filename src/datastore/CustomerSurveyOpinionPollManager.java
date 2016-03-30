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
import com.google.appengine.api.datastore.KeyFactory;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the CustomerSurveyOpinionPoll class.
 * 
 */

public class CustomerSurveyOpinionPollManager {
	
	private static final Logger log = 
        Logger.getLogger(CustomerSurveyOpinionPollManager.class.getName());
	
	/**
     * Get a CustomerSurveyOpinionPoll instance from the datastore given the CustomerSurveyOpinionPoll key.
     * @param key
     * 			: the CustomerSurveyOpinionPoll's key
     * @return CustomerSurveyOpinionPoll instance, null if CustomerSurveyOpinionPoll is not found
     */
	public static CustomerSurveyOpinionPoll getCustomerSurveyOpinionPoll(Long key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		CustomerSurveyOpinionPoll customerSurveyOpinionPoll;
		try  {
			customerSurveyOpinionPoll = pm.getObjectById(CustomerSurveyOpinionPoll.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return customerSurveyOpinionPoll;
	}
	
	/**
     * Get a CustomerSurveyOpinionPoll instance from the datastore given the opinion poll key
     * and customer email.
     * @param opinionPollKey
     * 			: the OpinionPoll's key
     * @param customerEMail
     * 			: the email of the customer
     * @return CustomerSurveyOpinionPoll instance, null if CustomerSurveyOpinionPoll is not found
     */
	@SuppressWarnings("unchecked")
	public static CustomerSurveyOpinionPoll getCustomerSurveyOpinionPoll(Key opinionPollKey, 
			String customerEmail) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(CustomerSurveyOpinionPoll.class);
        query.setFilter("surveyOpinionPoll == ropkey && customer == ckey");
        query.declareParameters(Key.class.getName() + " ropkey, " + Key.class.getName() + " ckey");
                      
        log.info("Query = " + query.toString());

        Key customerKey = KeyFactory.createKey(Customer.class.getSimpleName(), customerEmail);
        
        List<CustomerSurveyOpinionPoll> result = null;
        try {
        	result = (List<CustomerSurveyOpinionPoll>) query.execute(opinionPollKey, customerKey);
            // touch all elements
            for (CustomerSurveyOpinionPoll crop : result)
                crop.getKey();
        }
        finally {
        	pm.close();
            query.closeAll();
        }
        
        // Return last instance in case user voted more than once
        if (!result.isEmpty()) {
        	return result.get(result.size() - 1);
        }
        else {
        	return null;
        }
	}
	
	/**
     * Get all CustomerSurveyOpinionPoll instances from the datastore.
     * @return All CustomerSurveyOpinionPoll instances
     * TODO: Inefficient touching of objects
     */
	@SuppressWarnings("unchecked")
	public static List<CustomerSurveyOpinionPoll> getAllCustomerSurveyOpinionPolls() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(CustomerSurveyOpinionPoll.class);

        List<CustomerSurveyOpinionPoll> result = null;
        try {
        	result = (List<CustomerSurveyOpinionPoll>) query.execute();
            // touch all elements
            for (CustomerSurveyOpinionPoll crop : result)
                crop.getKey();
        } 
        finally {
        	pm.close();
            query.closeAll();
        }

        return result;
    }
	
	/**
     * Get all CustomerSurveyOpinionPoll instances from the datastore
     * that belong to this opinion poll.
     * @return All CustomerSurveyOpinionPoll instances
     * TODO: Inefficient touching of objects
     */
	@SuppressWarnings("unchecked")
	public static List<CustomerSurveyOpinionPoll> getAllCustomerSurveyOpinionPolls
			(Key surveyOpinionPoll) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(CustomerSurveyOpinionPoll.class);
        query.setFilter("surveyOpinionPoll == ropkey");
        query.setOrdering("date desc");
        query.declareParameters(Key.class.getName() + " ropkey");

        List<CustomerSurveyOpinionPoll> result = null;
        try {
        	result = (List<CustomerSurveyOpinionPoll>) query.execute(surveyOpinionPoll);
            // touch all elements
            for (CustomerSurveyOpinionPoll crop : result)
                crop.getKey();
        } 
        finally {
        	pm.close();
            query.closeAll();
        }

        return result;
    }
	
	/**
     * Put CustomerSurveyOpinionPoll into datastore.
     * Stores the given CustomerSurveyOpinionPoll instance in the datastore calling the 
     * PersistenceManager's makePersistent() method.
     * @param customerSurveyOpinionPoll
     * 			: the CustomerSurveyOpinionPoll instance to store
     */
	public static void putCustomerSurveyOpinionPoll(
			CustomerSurveyOpinionPoll customerSurveyOpinionPoll) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.makePersistent(customerSurveyOpinionPoll);
			tx.commit();
			log.info("CustomerSurveyOpinionPoll \"" + 
				"\" stored successfully in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Delete CustomerSurveyOpinionPoll from datastore.
    * Deletes the CustomerSurveyOpinionPoll corresponding to the given key
    * from the datastore calling the PersistenceManager's 
    * deletePersistent() method.
    * @param key
    * 			: the key of the CustomerSurveyOpinionPoll instance to delete
    */
	public static void deleteCustomerSurveyOpinionPoll(Long key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			CustomerSurveyOpinionPoll customerSurveyOpinionPoll = 
					pm.getObjectById(CustomerSurveyOpinionPoll.class, key);
			tx.begin();
			pm.deletePersistent(customerSurveyOpinionPoll);
			tx.commit();
			log.info("CustomerSurveyOpinionPoll \"" +
                     "\" deleted successfully from datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
}
