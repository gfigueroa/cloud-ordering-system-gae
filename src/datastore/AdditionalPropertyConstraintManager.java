/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the AdditionalPropertyConstraint class.
 * 
 */

public class AdditionalPropertyConstraintManager {
	
	private static final Logger log = 
        Logger.getLogger(AdditionalPropertyConstraintManager.class.getName());
	
	/**
     * Get an AdditionalPropertyConstraint instance from the datastore given the 
     * AdditionalPropertyConstraint key.
     * @param key
     * 			: the AdditionalPropertyConstraint's key
     * @return AdditionalPropertyConstraint instance, null if AdditionalPropertyConstraint is not found
     */
	public static AdditionalPropertyConstraint getAdditionalPropertyConstraint(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		AdditionalPropertyConstraint additionalPropertyConstraint;
		try  {
			additionalPropertyConstraint = pm.getObjectById(
					AdditionalPropertyConstraint.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return additionalPropertyConstraint;
	}
	
	/**
    * Get AdditionalPropertyConstraints from datastore for this
    * menu item
    * @param menuItemKey
    * 			: the key of the menuItem
    * TODO: Fix Touching
    */
	public static List<AdditionalPropertyConstraint> getMenuItemAdditionalPropertyConstraints(
			Key menuItemKey) {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		MenuItem menuItem = pm.getObjectById(MenuItem.class, menuItemKey);
		
		List<AdditionalPropertyConstraint> result = null;
        try {
        	result = menuItem.getAdditionalPropertyConstraints();
        	// Touch each constraint
        	for (AdditionalPropertyConstraint apc : result) {
        		apc.getKey();
        	}
        	
        	return result;
		}
		finally {
			pm.close();
		}
	}
	
	/**
    * Get AdditionalPropertyConstraints from datastore that include the given
    * menu item additional property value.
    * @param menuItemAdditionalPropertyValueKey
    * 			: the key of the menuItemAdditionalPropertyValue
    */
	@SuppressWarnings("unchecked")
	public static List<AdditionalPropertyConstraint> getAdditionalPropertyConstraints(
			Key menuItemAdditionalPropertyValueKey) {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query1 = pm.newQuery(AdditionalPropertyConstraint.class);
    	query1.setFilter("menuItemAdditionalPropertyValue1 == miapv");
        query1.declareParameters(Key.class.getName() + " miapv");
        
        Query query2 = pm.newQuery(AdditionalPropertyConstraint.class);
    	query2.setFilter("menuItemAdditionalPropertyValue2 == miapv");
        query2.declareParameters(Key.class.getName() + " miapv");
        
        List<AdditionalPropertyConstraint> result1;
        List<AdditionalPropertyConstraint> result2;
        ArrayList<AdditionalPropertyConstraint> result = new ArrayList<AdditionalPropertyConstraint>();
        try {
        	result1 = (List<AdditionalPropertyConstraint>) query1.execute(
        			menuItemAdditionalPropertyValueKey);
        	result2 = (List<AdditionalPropertyConstraint>) query2.execute(
        			menuItemAdditionalPropertyValueKey);
        	
        	for (AdditionalPropertyConstraint apc : result1) {
        		apc.getKey();
        		result.add(apc);
        	}
        	for (AdditionalPropertyConstraint apc : result2) {
        		apc.getKey();
        		result.add(apc);
        	}
        	
        	return result;
		}
		finally {
			pm.close();
		}
	}
	
	/**
     * Put AdditionalPropertyConstraints into datastore for this menu item.
     * Stores the given AdditionalPropertyConstraints in the datastore.
     * @param menuItemKey
     * 			: the key of the menu item where the property constraints
     * 			  will be stored
     * @param additionalPropertyConstraints
     * 			: the list of AdditionalPropertyConstraints to store
     */
	@SuppressWarnings("unchecked")
	public static void putAdditionalPropertyConstraints(Key menuItemKey,
			ArrayList<AdditionalPropertyConstraint> additionalPropertyConstraints) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query1 = pm.newQuery(AdditionalPropertyConstraint.class);
    	query1.setFilter("menuItemAdditionalPropertyValue1 == miapv1 && " +
    			"menuItemAdditionalPropertyValue2 == miapv2");
        query1.declareParameters(Key.class.getName() + " miapv1, " + 
        		Key.class.getName() + " miapv2");
        
        Query query2 = pm.newQuery(AdditionalPropertyConstraint.class);
    	query2.setFilter("menuItemAdditionalPropertyValue1 == miapv2 && " +
    			"menuItemAdditionalPropertyValue2 == miapv1");
        query2.declareParameters(Key.class.getName() + " miapv1, " + 
        		Key.class.getName() + " miapv2");
		
		MenuItem menuItem = pm.getObjectById(MenuItem.class, menuItemKey);
		Restaurant restaurant = pm.getObjectById(Restaurant.class, menuItemKey.getParent());
		
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			for (AdditionalPropertyConstraint apc : additionalPropertyConstraints) {
				
				// Check that constraint doesn't already exist
				List<AdditionalPropertyConstraint> result1 = 
						(List<AdditionalPropertyConstraint>) query1.execute(
								apc.getMenuItemAdditionalPropertyValue1(),
								apc.getMenuItemAdditionalPropertyValue2());
				List<AdditionalPropertyConstraint> result2 = 
						(List<AdditionalPropertyConstraint>) query2.execute(
								apc.getMenuItemAdditionalPropertyValue1(),
								apc.getMenuItemAdditionalPropertyValue2());
				
				if (!result1.isEmpty() || !result2.isEmpty()) {
					continue;
				}

				menuItem.addAdditionalPropertyConstraint(apc);
			}
			
			restaurant.updateMenuVersion();
			
			tx.commit();
			log.info("AdditionalPropertyConstraints stored successfully in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Delete AdditionalPropertyConstraint from datastore.
    * Deletes the AdditionalPropertyConstraint corresponding to the given key
    * from the datastore calling the PersistenceManager's 
    * deletePersistent() method.
    * @param key
    * 			: the key of the AdditionalPropertyConstraint instance to delete
    */
	public static void deleteAdditionalPropertyConstraint(Key key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			AdditionalPropertyConstraint additionalPropertyConstraint = 
					pm.getObjectById(AdditionalPropertyConstraint.class, key);
			
			Restaurant restaurant = pm.getObjectById(
					Restaurant.class, key.getParent().getParent());
			
			tx.begin();
			pm.deletePersistent(additionalPropertyConstraint);
			restaurant.updateMenuVersion();
			tx.commit();
			log.info("AdditionalPropertyConstraint \"" + 
					additionalPropertyConstraint.getKey() + 
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
    * Delete AdditionalPropertyConstraints from datastore.
    * Deletes the AdditionalPropertyConstraints corresponding to the given
    * menu item additional property value keys from the datastore calling the 
    * PersistenceManager's deletePersistentAll() method.
    * @param menuItemAdditionalPropertyValueKey1
    * 			: the key of the menuItemAdditionalPropertyValue1
    * @param menuItemAdditionalPropertyValueKey2
    * 			: the key of the menuItemAdditionalPropertyValue2
    */
	public static void deleteAdditionalPropertyConstraints(
			Key menuItemAdditionalPropertyValueKey1, 
			Key menuItemAdditionalPropertyValueKey2) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query1 = pm.newQuery(AdditionalPropertyConstraint.class);
    	query1.setFilter("menuItemAdditionalPropertyValue1 == miapv1 && " +
    			"menuItemAdditionalPropertyValue2 == miapv2");
        query1.declareParameters(Key.class.getName() + " miapv1, " + 
        		Key.class.getName() + " miapv2");
        
        Query query2 = pm.newQuery(AdditionalPropertyConstraint.class);
    	query2.setFilter("menuItemAdditionalPropertyValue1 == miapv2 && " +
    			"menuItemAdditionalPropertyValue2 == miapv1");
        query2.declareParameters(Key.class.getName() + " miapv1, " + 
        		Key.class.getName() + " miapv2");
        
        Transaction tx = pm.currentTransaction();
        try {
        	Restaurant restaurant = pm.getObjectById(
					Restaurant.class, 
					menuItemAdditionalPropertyValueKey1.getParent().getParent());
        	
			tx.begin();
			query1.deletePersistentAll(menuItemAdditionalPropertyValueKey1,
					menuItemAdditionalPropertyValueKey2);
			query2.deletePersistentAll(menuItemAdditionalPropertyValueKey1,
					menuItemAdditionalPropertyValueKey2);
			restaurant.updateMenuVersion();
			tx.commit();
			log.info("AdditionalPropertyConstraints deleted successfully " +
					"from datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
}
