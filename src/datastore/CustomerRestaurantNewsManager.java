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

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the CustomerRestaurantNews class.
 * 
 */

public class CustomerRestaurantNewsManager {
	
	private static final Logger log = 
        Logger.getLogger(CustomerRestaurantNewsManager.class.getName());
	
	/**
     * Get a CustomerRestaurantNews instance from the datastore given the CustomerRestaurantNews key.
     * @param key
     * 			: the CustomerRestaurantNews's key
     * @return CustomerRestaurantNews instance, null if CustomerRestaurantNews is not found
     */
	public static CustomerRestaurantNews getCustomerRestaurantNews(Long key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		CustomerRestaurantNews CustomerRestaurantNews;
		try  {
			CustomerRestaurantNews = pm.getObjectById(CustomerRestaurantNews.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return CustomerRestaurantNews;
	}
	
	/**
     * Get all CustomerRestaurantNews instances from the datastore.
     * @return All CustomerRestaurantNews instances
     * TODO: Inefficient touching of objects
     */
	@SuppressWarnings("unchecked")
	public static List<CustomerRestaurantNews> getAllCustomerRestaurantNews() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(CustomerRestaurantNews.class);

        List<CustomerRestaurantNews> result = null;
        try {
        	result = (List<CustomerRestaurantNews>) query.execute();
            // touch all elements
            for (CustomerRestaurantNews crn : result)
                crn.getKey();
        } finally {
        	pm.close();
            query.closeAll();
        }

        return result;
    }
	
	/**
     * Put CustomerRestaurantNews into datastore.
     * Stores the given CustomerRestaurantNews instance in the datastore calling the 
     * PersistenceManager's makePersistent() method.
     * @param customerRestaurantNews
     * 			: the CustomerRestaurantNews instance to store
     */
	public static void putCustomerRestaurantNews(
			CustomerRestaurantNews customerRestaurantNews) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.makePersistent(customerRestaurantNews);
			tx.commit();
			log.info("CustomerRestaurantNews \"" + 
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
    * Delete CustomerRestaurantNews from datastore.
    * Deletes the CustomerRestaurantNews corresponding to the given key
    * from the datastore calling the PersistenceManager's 
    * deletePersistent() method.
    * @param key
    * 			: the key of the CustomerRestaurantNews instance to delete
    */
	public static void deleteCustomerRestaurantNews(Long key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			CustomerRestaurantNews customerRestaurantNews = 
					pm.getObjectById(CustomerRestaurantNews.class, key);
			tx.begin();
			pm.deletePersistent(customerRestaurantNews);
			tx.commit();
			log.info("CustomerRestaurantNews \"" +
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
