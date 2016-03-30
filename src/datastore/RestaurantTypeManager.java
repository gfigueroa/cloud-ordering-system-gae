/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.jdo.Query;

import datastore.RestaurantType.StoreSuperType;

import exceptions.MissingRequiredFieldsException;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the RestaurantType class.
 * 
 */

public class RestaurantTypeManager {
	
	private static final Logger log = 
        Logger.getLogger(RestaurantTypeManager.class.getName());
	
	/**
     * Get a RestaurantType instance from the datastore given the RestaurantType key.
     * @param key
     * 			: the RestaurantType's key
     * @return RestaurantType instance, null if RestaurantType is not found
     */
	public static RestaurantType getRestaurantType(Long key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		RestaurantType restaurantType;
		try  {
			restaurantType = pm.getObjectById(RestaurantType.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return restaurantType;
	}
	
	/**
     * Get all RestaurantType instances from the datastore.
     * @return All RestaurantType instances
     * TODO: Inefficient touching of objects
     */
	@SuppressWarnings("unchecked")
	public static List<RestaurantType> getAllRestaurantTypes() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(RestaurantType.class);

        List<RestaurantType> types = null;
        try {
        	types = (List<RestaurantType>) query.execute();
            // touch all elements
            for (RestaurantType t : types)
                t.getRestaurantTypeName();
        } finally {
        	pm.close();
            query.closeAll();
        }

        return types;
    }
	
	/**
     * Get all RestaurantType instances from the datastore
     * that belong to a super store type.
     * @param storeSuperType
     * 			: the store super type for this restaurant type
     * @return All RestaurantType instances that belong to this
     * 			store super type
     */
	@SuppressWarnings("unchecked")
	public static List<RestaurantType> getAllRestaurantTypes(
			StoreSuperType storeSuperType) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(RestaurantType.class);

        List<RestaurantType> types = null;
        ArrayList<RestaurantType> finalTypes = new ArrayList<RestaurantType>();
        try {
        	types = (List<RestaurantType>) query.execute();
            // touch all elements
            for (RestaurantType t : types) {
            	// Check the store super type
            	if (t.getStoreSuperType() == storeSuperType) {
            		finalTypes.add(t);
            	}
            }
                
        } 
        finally {
        	pm.close();
            query.closeAll();
        }

        return finalTypes;
    }
	
	/**
     * Put RestaurantType into datastore.
     * Stores the given RestaurantType instance in the datastore calling the 
     * PersistenceManager's makePersistent() method.
     * @param restaurantType
     * 			: the RestaurantType instance to store
     */
	public static void putRestaurantType(RestaurantType restaurantType) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.makePersistent(restaurantType);
			tx.commit();
			log.info("RestaurantType \"" + restaurantType.getRestaurantTypeName() + 
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
    * Delete RestaurantType from datastore.
    * Deletes the RestaurantType corresponding to the given key
    * from the datastore calling the PersistenceManager's 
    * deletePersistent() method.
    * @param key
    * 			: the key of the RestaurantType instance to delete
    */
	public static void deleteRestaurantType(Long key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			RestaurantType restaurantType = pm.getObjectById(RestaurantType.class, key);
			String RestaurantTypeName = restaurantType.getRestaurantTypeName();
			tx.begin();
			pm.deletePersistent(restaurantType);
			tx.commit();
			log.info("RestaurantType \"" + RestaurantTypeName + 
                     "\" deleted successfully from datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
    * Update RestaurantType attributes.
    * Update's the given RestaurantType's attributes in the datastore.
    * @param key
    * 			: the key of the RestaurantType whose attributes will be updated
    * @param restaurantTypeName
    * 			: the new name to give to the RestaurantType
    * @param restaurantTypeDescription
    * 			: the new description to give to the RestaurantType
	* @throws MissingRequiredFieldsException 
    */
	public static void updateRestaurantTypeAttributes(Long key, RestaurantType.StoreSuperType storeSuperType,
			String restaurantTypeName, String restaurantTypeDescription) 
                       throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			RestaurantType restaurantType = pm.getObjectById(RestaurantType.class, key);
			tx.begin();
			restaurantType.setStoreSuperType(storeSuperType);
			restaurantType.setRestaurantTypeName(restaurantTypeName);
			restaurantType.setRestaurantTypeDescription(restaurantTypeDescription);
			tx.commit();
			log.info("RestaurantType \"" + restaurantTypeName + 
                     "\"'s attributes updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
	 * Updates the store type version by 1.
	 * @param key
	 * 			: the key of the store type whose version will be updated
	 */
	public static void updateStoreTypeVersion(Long key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			RestaurantType storeType = pm.getObjectById(RestaurantType.class, key);
			tx.begin();
			storeType.updateStoreTypeVersion();
			tx.commit();
			log.info("StoreType \"" + storeType.getRestaurantTypeName() + 
                     "\"'s version updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
}
