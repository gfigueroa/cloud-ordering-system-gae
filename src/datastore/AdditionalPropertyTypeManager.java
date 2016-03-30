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

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import exceptions.InexistentObjectException;
import exceptions.MissingRequiredFieldsException;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the AdditionalPropertyType class.
 * 
 */

public class AdditionalPropertyTypeManager {
	
	private static final Logger log = 
        Logger.getLogger(AdditionalPropertyTypeManager.class.getName());
	
	/**
     * Get a AdditionalPropertyType instance from the datastore given the AdditionalPropertyType key.
     * @param key
     * 			: the AdditionalPropertyType's key
     * @return AdditionalPropertyType instance, null if AdditionalPropertyType is not found
     */
	public static AdditionalPropertyType getAdditionalPropertyType(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		AdditionalPropertyType additionalPropertyType;
		try  {
			additionalPropertyType = pm.getObjectById(AdditionalPropertyType.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return additionalPropertyType;
	}
	
	/**
     * Get ALL the additional property Types in the datastore from a specific restaurant
     * and returns them in a List structure
     * @param restaurantKey: 
     * 				the key of the restaurant whose additional property types will be retrieved
     * @return all additional property types in the datastore belonging to the given restaurant
     * TODO: Fix "touching" of additional property types
     */
	public static List<AdditionalPropertyType> getRestaurantAdditionalPropertyTypes(Key restaurantKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant restaurant = pm.getObjectById(Restaurant.class, restaurantKey);
		
        List<AdditionalPropertyType> result = null;
        try {
            result = restaurant.getAdditionalPropertyTypes();
            // Touch
            for (AdditionalPropertyType additionalPropertyType : result) {
            	additionalPropertyType.getAdditionalPropertyTypeName();
            }
        } 
        finally {
        	pm.close();
        }

        return result;
    }
	
	/**
     * Put AdditionalPropertyType into datastore.
     * Stores the given AdditionalPropertyType instance in the datastore for this
     * restaurant.
     * @param email
     * 			: the email of the Restaurant where the additionalPropertyType will be added
     * @param additionalPropertyType
     * 			: the AdditionalPropertyType instance to store
     */
	public static void putAdditionalPropertyType(Email email, AdditionalPropertyType additionalPropertyType) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Restaurant.class.getSimpleName(), email.getEmail());
			Restaurant restaurant = pm.getObjectById(Restaurant.class, key);
			tx.begin();
			restaurant.addAdditionalPropertyType(additionalPropertyType);
			restaurant.updateAdditionalPropertyVersion();
			tx.commit();
			log.info("AdditionalPropertyType \"" + additionalPropertyType.getAdditionalPropertyTypeName() + 
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
    * Delete AdditionalPropertyType from datastore.
    * Deletes the AdditionalPropertyType corresponding to the given key
    * from the datastore calling the PersistenceManager's 
    * deletePersistent() method.
    * @param key
    * 			: the key of the AdditionalPropertyType instance to delete
    */
	public static void deleteAdditionalPropertyType(Key key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Restaurant restaurant = pm.getObjectById(Restaurant.class, key.getParent());
			AdditionalPropertyType additionalPropertyType = pm.getObjectById(AdditionalPropertyType.class, key);
			String additionalPropertyTypeName = additionalPropertyType.getAdditionalPropertyTypeName();
			tx.begin();
			restaurant.removeAdditionalPropertyType(additionalPropertyType);
			restaurant.updateAdditionalPropertyVersion();
			tx.commit();
			log.info("AdditionalPropertyType \"" + additionalPropertyTypeName + 
                     "\" deleted successfully from datastore.");
		}
		catch (InexistentObjectException e) {
			e.printStackTrace();
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
    * Update AdditionalPropertyType attributes.
    * Update's the given AdditionalPropertyType's attributes in the datastore.
    * @param key
    * 			: the key of the AdditionalPropertyType whose attributes will be updated
    * @param additionalPropertyTypeName
    * 			: the new name to give to the AdditionalPropertyType
    * @param additionalPropertyTypeDescription
    * 			: the new description to give to the AdditionalPropertyType
	* @throws MissingRequiredFieldsException 
    */
	public static void updateAdditionalPropertyTypeAttributes(Key key, 
			String additionalPropertyTypeName, 
			String additionalPropertyTypeDescription) 
                       throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant restaurant = 
				pm.getObjectById(Restaurant.class, key.getParent());
		
		Transaction tx = pm.currentTransaction();
		try {
			AdditionalPropertyType additionalPropertyType = 
					pm.getObjectById(AdditionalPropertyType.class, key);
			tx.begin();
			additionalPropertyType.setAdditionalPropertyTypeName(
					additionalPropertyTypeName);
			additionalPropertyType.setAdditionalPropertyTypeDescription(
					additionalPropertyTypeDescription);
			
			restaurant.updateAdditionalPropertyVersion();
			tx.commit();
			log.info("AdditionalPropertyType \"" + additionalPropertyTypeName + 
                     "\"'s attributes updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
}
