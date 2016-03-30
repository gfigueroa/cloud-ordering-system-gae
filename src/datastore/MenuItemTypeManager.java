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
 * made on the MenuItemType class.
 * 
 */

public class MenuItemTypeManager {
	
	private static final Logger log = 
        Logger.getLogger(MenuItemTypeManager.class.getName());
	
	/**
     * Get a MenuItemType instance from the datastore given the MenuItemType key.
     * @param key
     * 			: the MenuItemType's key
     * @return MenuItemType instance, null if MenuItemType is not found
     */
	public static MenuItemType getMenuItemType(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		MenuItemType menuItemType;
		try  {
			menuItemType = pm.getObjectById(MenuItemType.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return menuItemType;
	}
	
	/**
     * Get ALL the Menu Item Types in the datastore from a specific restaurant
     * and returns them in a List structure
     * @param restaurantKey: 
     * 				the key of the restaurant whose menu item types will be retrieved
     * @return all menu item types in the datastore belonging to the given restaurant
     * TODO: Fix "touching" of menu item types
     */
	public static List<MenuItemType> getRestaurantMenuItemTypes(Key restaurantKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant restaurant = pm.getObjectById(Restaurant.class, restaurantKey);
		
        List<MenuItemType> result = null;
        try {
            result = restaurant.getMenuItemTypes();
            // Touch each branch
            for (MenuItemType menuItemType : result) {
            	menuItemType.getMenuItemTypeName();
            }
        } 
        finally {
        	pm.close();
        }

        return result;
    }
	
	/**
     * Put MenuItemType into datastore.
     * Stores the given MenuItemType instance in the datastore for this
     * restaurant.
     * @param email
     * 			: the email of the Restaurant where the menuItemType will be added
     * @param menuItemType
     * 			: the MenuItemType instance to store
     */
	public static void putMenuItemType(Email email, MenuItemType menuItemType) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Restaurant.class.getSimpleName(), 
					email.getEmail());
			Restaurant restaurant = pm.getObjectById(Restaurant.class, key);
			tx.begin();
			restaurant.addMenuItemType(menuItemType);
			restaurant.updateMenuItemTypeVersion();
			tx.commit();
			log.info("MenuItemType \"" + menuItemType.getMenuItemTypeName() + 
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
    * Delete MenuItemType from datastore.
    * Deletes the MenuItemType corresponding to the given key
    * from the datastore calling the PersistenceManager's 
    * deletePersistent() method.
    * @param key
    * 			: the key of the MenuItemType instance to delete
    */
	public static void deleteMenuItemType(Key key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Restaurant restaurant = pm.getObjectById(Restaurant.class, 
					key.getParent());
			MenuItemType menuItemType = pm.getObjectById(MenuItemType.class, key);
			String menuItemTypeName = menuItemType.getMenuItemTypeName();
			tx.begin();
			restaurant.removeMenuItemType(menuItemType);
			restaurant.updateMenuItemTypeVersion();
			tx.commit();
			log.info("MenuItemType \"" + menuItemTypeName + 
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
    * Update MenuItemType attributes.
    * Update's the given MenuItemType's attributes in the datastore.
    * @param key
    * 			: the key of the MenuItemType whose attributes will be updated
    * @param menuItemTypeName
    * 			: the new name to give to the MenuItemType
    * @param menuItemTypeDescription
    * 			: the new description to give to the MenuItemType
	* @throws MissingRequiredFieldsException 
    */
	public static void updateMenuItemTypeAttributes(Key key, String menuItemTypeName, 
			String menuItemTypeDescription) 
                       throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant restaurant = pm.getObjectById(Restaurant.class, key.getParent());
		
		Transaction tx = pm.currentTransaction();
		try {
			MenuItemType menuItemType = pm.getObjectById(MenuItemType.class, key);
			tx.begin();
			menuItemType.setMenuItemTypeName(menuItemTypeName);
			menuItemType.setMenuItemTypeDescription(menuItemTypeDescription);
			
			restaurant.updateMenuItemTypeVersion();
			tx.commit();
			log.info("MenuItemType \"" + menuItemTypeName + 
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
	 * Update the product item type version by 1.
	 * @param key
	 * 			: the key of the product item type whose version will be updated
	 */
	public static void updateProductItemTypeVersion(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			MenuItemType productItemType = pm.getObjectById(MenuItemType.class, key);
			tx.begin();
			productItemType.updateProductItemTypeVersion();
			tx.commit();
			log.info("ProductItemType \"" + productItemType.getMenuItemTypeName() + 
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
