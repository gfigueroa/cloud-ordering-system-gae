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

import com.google.appengine.api.datastore.Key;

import exceptions.InexistentObjectException;
import exceptions.MissingRequiredFieldsException;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the AdditionalPropertyValue class.
 * 
 */

public class AdditionalPropertyValueManager {
	
	private static final Logger log = 
        Logger.getLogger(AdditionalPropertyValueManager.class.getName());
	
	/**
     * Get an AdditionalPropertyValue instance from the datastore given the AdditionalPropertyValue key.
     * @param key
     * 			: the AdditionalPropertyValue's key
     * @return AdditionalPropertyValue instance, null if AdditionalPropertyValue is not found
     */
	public static AdditionalPropertyValue getAdditionalPropertyValue(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		AdditionalPropertyValue additionalPropertyValue;
		try  {
			additionalPropertyValue = pm.getObjectById(AdditionalPropertyValue.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return additionalPropertyValue;
	}
	
	/**
     * Get ALL the additional property Values in the datastore from a specific 
     * additional property type and returns them in a List structure
     * @param additionalPropertyTypeKey: 
     * 				the key of the additional property type whose additional property values will be retrieved
     * @return all additional property values in the datastore belonging to the given additional property type
     * TODO: Fix "touching" of additional property values
     */
	public static List<AdditionalPropertyValue> getAdditionalPropertyValues(Key additionalPropertyTypeKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		AdditionalPropertyType additionalPropertyType = 
				pm.getObjectById(AdditionalPropertyType.class, additionalPropertyTypeKey);
		
        List<AdditionalPropertyValue> result = null;
        try {
            result = additionalPropertyType.getAdditionalPropertyValues();
            // Touch
            for (AdditionalPropertyValue additionalPropertyValue : result) {
            	additionalPropertyValue.getAdditionalPropertyValueName();
            }
        } 
        finally {
        	pm.close();
        }

        return result;
    }
	
	/**
     * Put AdditionalPropertyValue into datastore.
     * Stores the given AdditionalPropertyValue instance in the datastore for this
     * additional property type.
     * @param additionalPropertyTypeKey
     * 			: the key of the additional property type where the additionalPropertyValue will be added
     * @param additionalPropertyValue
     * 			: the AdditionalPropertyValue instance to store
     */
	public static void putAdditionalPropertyValue(
			Key additionalPropertyTypeKey, AdditionalPropertyValue additionalPropertyValue) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			AdditionalPropertyType additionalPropertyType = 
					pm.getObjectById(AdditionalPropertyType.class, additionalPropertyTypeKey);
			Restaurant restaurant = pm.getObjectById(Restaurant.class, additionalPropertyTypeKey.getParent());
			tx.begin();
			additionalPropertyType.addAdditionalPropertyValue(additionalPropertyValue);
			restaurant.updateAdditionalPropertyVersion();
			tx.commit();
			log.info("AdditionalPropertyValue \"" + additionalPropertyValue.getAdditionalPropertyValueName() + 
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
    * Delete AdditionalPropertyValue from datastore.
    * Deletes the AdditionalPropertyValue corresponding to the given key
    * from the datastore calling the PersistenceManager's 
    * deletePersistent() method.
    * @param key
    * 			: the key of the AdditionalPropertyValue instance to delete
    */
	public static void deleteAdditionalPropertyValue(Key key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			AdditionalPropertyValue additionalPropertyValue = 
					pm.getObjectById(AdditionalPropertyValue.class, key);
			AdditionalPropertyType additionalPropertyType = 
					pm.getObjectById(AdditionalPropertyType.class, key.getParent());
			Restaurant restaurant = pm.getObjectById(Restaurant.class, key.getParent().getParent());
			String additionalPropertyValueName = additionalPropertyValue.getAdditionalPropertyValueName();
			tx.begin();
			additionalPropertyType.removeAdditionalPropertyValue(additionalPropertyValue);
			restaurant.updateAdditionalPropertyVersion();
			tx.commit();
			log.info("AdditionalPropertyValue \"" + additionalPropertyValueName + 
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
    * Update AdditionalPropertyValue attributes.
    * Update's the given AdditionalPropertyValue's attributes in the datastore.
    * @param key
    * 			: the key of the AdditionalPropertyValue whose attributes will be updated
    * @param additionalPropertyValueName
    * 			: the new name to give to the AdditionalPropertyValue
	* @throws MissingRequiredFieldsException 
    */
	public static void updateAdditionalPropertyValueAttributes(Key key, String additionalPropertyValueName) 
                       throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant restaurant = 
				pm.getObjectById(Restaurant.class, key.getParent().getParent());
		
		Transaction tx = pm.currentTransaction();
		try {
			AdditionalPropertyValue additionalPropertyValue = 
					pm.getObjectById(AdditionalPropertyValue.class, key);
			tx.begin();
			additionalPropertyValue.setAdditionalPropertyValueName(additionalPropertyValueName);
			restaurant.updateAdditionalPropertyVersion();
			tx.commit();
			log.info("AdditionalPropertyValue \"" + additionalPropertyValueName + 
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
