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

import com.google.appengine.api.datastore.Key;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the MenuItemAdditionalPropertyValue class.
 * 
 */

public class MenuItemAdditionalPropertyValueManager {
	
	private static final Logger log = 
        Logger.getLogger(MenuItemAdditionalPropertyValueManager.class.getName());
	
	/**
     * Get an MenuItemAdditionalPropertyValue instance from the datastore given the MenuItemAdditionalPropertyValue key.
     * @param key
     * 			: the MenuItemAdditionalPropertyValue's key
     * @return MenuItemAdditionalPropertyValue instance, null if MenuItemAdditionalPropertyValue is not found
     */
	public static MenuItemAdditionalPropertyValue getMenuItemAdditionalPropertyValue(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		MenuItemAdditionalPropertyValue menuItemAdditionalPropertyValue;
		try  {
			menuItemAdditionalPropertyValue = pm.getObjectById(MenuItemAdditionalPropertyValue.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return menuItemAdditionalPropertyValue;
	}
	
	/**
     * Get ALL the menu item additional property Values in the datastore from a specific 
     * menu item and returns them in a List structure
     * @param menuItemKey: 
     * 				the key of the menu item whose menu item additional property values will be retrieved
     * @return all menu item additional property values in the datastore belonging to the given menu item
     * TODO: Fix "touching" of menu item additional property values
     */
	public static List<MenuItemAdditionalPropertyValue> getMenuItemAdditionalPropertyValues(
			Key menuItemKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		MenuItem menuItem = pm.getObjectById(MenuItem.class, menuItemKey);
		
        List<MenuItemAdditionalPropertyValue> result = null;
        try {
            result = menuItem.getMenuItemAdditionalPropertyValues();
            // Touch
            for (MenuItemAdditionalPropertyValue menuItemAdditionalPropertyValue : result) {
            	menuItemAdditionalPropertyValue.getKey();
            }
        } 
        finally {
        	pm.close();
        }

        return result;
    }
	
	/**
     * Get ALL the additional property Types in the datastore from a specific 
     * menu item and returns them in an ArrayList structure
     * @param menuItemKey: 
     * 				the key of the menu item whose additional property types will be retrieved
     * @return all additional property types in the datastore belonging to the given menu item
     * TODO: Fix "touching" of additional property types
     */
	public static ArrayList<AdditionalPropertyType> getMenuItemAdditionalPropertyTypes(
			Key menuItemKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		MenuItem menuItem = pm.getObjectById(MenuItem.class, menuItemKey);
		
        List<MenuItemAdditionalPropertyValue> result = null;
        ArrayList<AdditionalPropertyType> finalResult = new ArrayList<AdditionalPropertyType>();
        try {
            result = menuItem.getMenuItemAdditionalPropertyValues();
            // Get types
            for (MenuItemAdditionalPropertyValue menuItemAdditionalPropertyValue : result) {
            	AdditionalPropertyType additionalPropertyType = 
            			pm.getObjectById(AdditionalPropertyType.class, 
            					menuItemAdditionalPropertyValue.getAdditionalPropertyValue().getParent());
            	finalResult.add(additionalPropertyType);
            }
        } 
        finally {
        	pm.close();
        }

        return finalResult;
    }
	
	/**
     * Put MenuItemAdditionalPropertyValues into datastore.
     * Stores the given MenuItemAdditionalPropertyValue list in the datastore for this
     * menu item.
     * @param menuKey
     * 			: the key of the menu item where the menuItemAdditionalPropertyValues will be added
     * @param menuItemAdditionalPropertyValues
     * 			: the list MenuItemAdditionalPropertyValues instance to store
     */
	public static void setMenuItemAdditionalPropertyValues(
			Key menuItemKey, 
			ArrayList<MenuItemAdditionalPropertyValue> menuItemAdditionalPropertyValues) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		MenuItem menuItem = pm.getObjectById(MenuItem.class, menuItemKey);
		Restaurant restaurant = pm.getObjectById(Restaurant.class, menuItemKey.getParent());
		
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			menuItem.setMenuItemAdditionalPropertyValues(menuItemAdditionalPropertyValues);
			restaurant.updateMenuVersion();
			tx.commit();
			log.info("MenuItemAdditionalPropertyValues stored successfully in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
    * Update MenuItemAdditionalPropertyValue attributes.
    * Update's the given MenuItemAdditionalPropertyValue's attributes in the datastore.
    * @param key
    * 			: the key of the MenuItemAdditionalPropertyValue whose attributes will be updated
    * @param additionalCharge
    * 			: the new additional charge to give to the MenuItemAdditionalPropertyValue
    */
	public static void updateMenuItemAdditionalPropertyValueAttributes(Key key, Double additionalCharge) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant restaurant = pm.getObjectById(Restaurant.class, key.getParent().getParent());
		
		Transaction tx = pm.currentTransaction();
		try {
			MenuItemAdditionalPropertyValue menuItemAdditionalPropertyValue = 
					pm.getObjectById(MenuItemAdditionalPropertyValue.class, key);
			tx.begin();
			menuItemAdditionalPropertyValue.setAdditionalCharge(additionalCharge);
			restaurant.updateMenuVersion();
			tx.commit();
			log.info("MenuItemAdditionalPropertyValue's attributes updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
}
