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
 * made on the CustomerRestaurantOpinionPoll class.
 * 
 */

public class CustomerRestaurantOpinionPollManager {
	
	private static final Logger log = 
        Logger.getLogger(CustomerRestaurantOpinionPollManager.class.getName());
	
	/**
     * Get a CustomerRestaurantOpinionPoll instance from the datastore given the CustomerRestaurantOpinionPoll key.
     * @param key
     * 			: the CustomerRestaurantOpinionPoll's key
     * @return CustomerRestaurantOpinionPoll instance, null if CustomerRestaurantOpinionPoll is not found
     */
	public static CustomerRestaurantOpinionPoll getCustomerRestaurantOpinionPoll(Long key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		CustomerRestaurantOpinionPoll customerRestaurantOpinionPoll;
		try  {
			customerRestaurantOpinionPoll = pm.getObjectById(CustomerRestaurantOpinionPoll.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return customerRestaurantOpinionPoll;
	}
	
	/**
     * Get a CustomerRestaurantOpinionPoll instance from the datastore given the opinion poll key
     * and customer email.
     * @param opinionPollKey
     * 			: the OpinionPoll's key
     * @param customerEMail
     * 			: the email of the customer
     * @return CustomerRestaurantOpinionPoll instance, null if CustomerRestaurantOpinionPoll is not found
     */
	@SuppressWarnings("unchecked")
	public static CustomerRestaurantOpinionPoll getCustomerRestaurantOpinionPoll(Key opinionPollKey, 
			String customerEmail) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(CustomerRestaurantOpinionPoll.class);
        query.setFilter("restaurantOpinionPoll == ropkey && customer == ckey");
        query.declareParameters(Key.class.getName() + " ropkey, " + Key.class.getName() + " ckey");
                      
        log.info("Query = " + query.toString());

        Key customerKey = KeyFactory.createKey(Customer.class.getSimpleName(), customerEmail);
        
        List<CustomerRestaurantOpinionPoll> result = null;
        try {
        	result = (List<CustomerRestaurantOpinionPoll>) query.execute(opinionPollKey, customerKey);
            // touch all elements
            for (CustomerRestaurantOpinionPoll crop : result)
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
     * Get all CustomerRestaurantOpinionPoll instances from the datastore.
     * @return All CustomerRestaurantOpinionPoll instances
     * TODO: Inefficient touching of objects
     */
	@SuppressWarnings("unchecked")
	public static List<CustomerRestaurantOpinionPoll> getAllCustomerRestaurantOpinionPolls() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(CustomerRestaurantOpinionPoll.class);

        List<CustomerRestaurantOpinionPoll> result = null;
        try {
        	result = (List<CustomerRestaurantOpinionPoll>) query.execute();
            // touch all elements
            for (CustomerRestaurantOpinionPoll crop : result)
                crop.getKey();
        } 
        finally {
        	pm.close();
            query.closeAll();
        }

        return result;
    }
	
	/**
     * Get all CustomerRestaurantOpinionPoll instances from the datastore
     * that belong to this opinion poll.
     * @return All CustomerRestaurantOpinionPoll instances
     * TODO: Inefficient touching of objects
     */
	@SuppressWarnings("unchecked")
	public static List<CustomerRestaurantOpinionPoll> getAllCustomerRestaurantOpinionPolls
			(Key restaurantOpinionPoll) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(CustomerRestaurantOpinionPoll.class);
        query.setFilter("restaurantOpinionPoll == ropkey");
        query.setOrdering("date desc");
        query.declareParameters(Key.class.getName() + " ropkey");

        List<CustomerRestaurantOpinionPoll> result = null;
        try {
        	result = (List<CustomerRestaurantOpinionPoll>) query.execute(restaurantOpinionPoll);
            // touch all elements
            for (CustomerRestaurantOpinionPoll crop : result)
                crop.getKey();
        } 
        finally {
        	pm.close();
            query.closeAll();
        }

        return result;
    }
	
	/**
     * Put CustomerRestaurantOpinionPoll into datastore.
     * Stores the given CustomerRestaurantOpinionPoll instance in the datastore calling the 
     * PersistenceManager's makePersistent() method.
     * @param customerRestaurantOpinionPoll
     * 			: the CustomerRestaurantOpinionPoll instance to store
     */
	public static void putCustomerRestaurantOpinionPoll(
			CustomerRestaurantOpinionPoll customerRestaurantOpinionPoll) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.makePersistent(customerRestaurantOpinionPoll);
			tx.commit();
			log.info("CustomerRestaurantOpinionPoll \"" + 
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
    * Delete CustomerRestaurantOpinionPoll from datastore.
    * Deletes the CustomerRestaurantOpinionPoll corresponding to the given key
    * from the datastore calling the PersistenceManager's 
    * deletePersistent() method.
    * @param key
    * 			: the key of the CustomerRestaurantOpinionPoll instance to delete
    */
	public static void deleteCustomerRestaurantOpinionPoll(Long key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			CustomerRestaurantOpinionPoll customerRestaurantOpinionPoll = 
					pm.getObjectById(CustomerRestaurantOpinionPoll.class, key);
			tx.begin();
			pm.deletePersistent(customerRestaurantOpinionPoll);
			tx.commit();
			log.info("CustomerRestaurantOpinionPoll \"" +
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
