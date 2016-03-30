/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import exceptions.InexistentObjectException;
import exceptions.InvalidFieldFormatException;
import exceptions.MissingRequiredFieldsException;

/**
 * This class is used to manage the GAE Datastore operations 
 * (get, put, delete, update) made on the MenuItem class.
 */
public class MenuItemManager {
	
	private static final Logger log = 
        Logger.getLogger(MenuItemManager.class.getName());

	/**
	 * Get a menuItem using its complex key (includes the Restaurant key as well)
	 * @param key
	 *        : The menuItem's key
	 * @return MenuItem 
	 */
	public static MenuItem getMenuItem(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		MenuItem menuItem;
		try  {
			menuItem = pm.getObjectById(MenuItem.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return menuItem;
	}

	/**
     * Get ALL the MenuItems in the datastore that belong to
     * the given menuItem type and returns them in a List structure
     * @parameter menuItemType:
     * 			the menuItem type filter
     * @return all menuItems in the datastore corresponding to
     * 			the given menuItem type
     * TODO: make more efficient "touching" of the menuItems
     */
	@SuppressWarnings("unchecked")
	public static List<MenuItem> getAllMenuItemsByType(Key menuItemType) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(MenuItem.class);
        query.setFilter("menuItemType == type");
        query.setOrdering("menuItemTime asc");
        query.declareParameters(Key.class.getName() + " type");

        List<MenuItem> result = null;
        try {
            result = (List<MenuItem>) query.execute(menuItemType);
            // touch
            for (MenuItem menuItem : result)
                menuItem.getMenuItemName();
        } 
        finally {
        	pm.close();
            query.closeAll();
        }

        return result;
    }
	
	/**
     * Get ALL the Menu Items in the datastore from a specific restaurant
     * and returns them in a List structure
     * @param restaurantKey: 
     * 				the key of the restaurant whose menu items will be retrieved
     * @return all menu items in the datastore belonging to the given restaurant
     * TODO: Fix "touching" of menu items
     */
	public static List<MenuItem> getAllMenuItemsByRestaurant(Key restaurantKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant restaurant = pm.getObjectById(Restaurant.class, restaurantKey);
		
        List<MenuItem> result = null;
        try {
            result = restaurant.getMenuItems();
            // Touch each branch
            for (MenuItem menuItem : result) {
            	menuItem.getMenuItemName();
            }
        } 
        finally {
        	pm.close();
        }

        return result;
    }
	
	/**
     * Get ALL the MenuItems in the datastore that belong to
     * the given restaurant and group them by menu item type
     * @parameter restaurant:
     * 			the restaurant whose items will be retrieved
     * @return all menuItems in the datastore corresponding to
     * 			the given restaurant grouped by type
     * TODO: make more efficient "touching" of the menuItems
     */
	public static HashMap<Key, ArrayList<MenuItem>> 
			getAllMenuItemsGroupedByType(Restaurant restaurant) {
		
		List<MenuItem> menuItemList = getAllMenuItemsByRestaurant(restaurant.getKey());
		
		HashMap<Key, ArrayList<MenuItem>> menuItemMap = new HashMap<Key, ArrayList<MenuItem>>();
	    for (MenuItem menuItem : menuItemList) {
	    	Key menuItemTypeKey = menuItem.getMenuItemType();
	    	ArrayList<MenuItem> menuItemArrayList = menuItemMap.get(menuItemTypeKey);
	    	if (menuItemArrayList == null) {
	    		menuItemArrayList = new ArrayList<MenuItem>();
	    	}
	    	menuItemArrayList.add(menuItem);
	    	menuItemMap.put(menuItem.getMenuItemType(), menuItemArrayList);
	    }
	    
	    return menuItemMap;
    }
	
	/**
    * Add menuItem to a Restaurant.
    * Add a new menuItem in the datastore for this Restaurant.
    * @param email
    * 			: the email of the Restaurant where the menuItem will be added
    * @param menuItem
    * 			: the menuItem to be added
    */
	public static void putMenuItem(Email email, MenuItem menuItem) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Restaurant.class.getSimpleName(), email.getEmail());
			Restaurant restaurant = pm.getObjectById(Restaurant.class, key);
			tx.begin();
			restaurant.addMenuItem(menuItem);
			restaurant.updateMenuVersion();
			tx.commit();
			log.info("Menu item \"" + menuItem.getMenuItemName() + "\" added to Restaurant \"" + 
					email.getEmail() + "\" in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Add new menuItems to a Restaurant.
    * Add a list of menuItems in the datastore for this Restaurant.
    * @param email
    * 			: the email of the Restaurant where the menuItems will be added
    * @param menuItems
    * 			: the list of menuItems to be added
    */
	public static void putMenuItemAll(Email email, ArrayList<MenuItem> menuItems) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Restaurant.class.getSimpleName(), email.getEmail());
			Restaurant restaurant = pm.getObjectById(Restaurant.class, key);
			tx.begin();
			restaurant.addMenuItemAll(menuItems);
			restaurant.updateMenuVersion();
			tx.commit();
			log.info("Menu items added to Restaurant \"" + email.getEmail() + "\" in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Delete menuItem.
    * Delete a menuItem in the datastore.
    * @param key
    * 			: the key of the menuItem to delete (includes Restaurant key)
    */
	public static void deleteMenuItem(Key key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Restaurant restaurant = pm.getObjectById(Restaurant.class, key.getParent());
			MenuItem menuItem = pm.getObjectById(MenuItem.class, key);
			String menuItemName = menuItem.getMenuItemName();
			tx.begin();
			restaurant.removeMenuItem(menuItem);
			restaurant.updateMenuVersion();
			tx.commit();
			log.info("Menu item \"" + menuItemName + "\" deleted from Restaurant \"" + 
					restaurant.getUser().getUserEmail().getEmail() + "\" in datastore.");
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
    * Update MenuItem attributes.
    * Updates the given MenuItem's attributes in the datastore.
    * @param key
    * 			: the key of the MenuItem whose attributes will be updated
    * @param menuItemType
    * 			: the key of the new menuItem type to give to the MenuItem
    * @param menuItemName
    * 			: the new name to give to the MenuItem
    * @param menuItemPrice
    * 			: the new price to give to the MenuItem
    * @param menuItemDiscount
    * 			: the new discount to give to the MenuItem
    * @param menuItemDescription
    * 			: the new description to give to the MenuItem
    * @param menuItemImage
    * 			: the new image blob key to give to the MenuItem
    * @param menuItemServingTime
    * 			: the serving time of this menu item (in minutes)
    * @param isAvailable
    * 			: whether the MenuItem is available or not
    * @param menuItemServiceTime
    * 			: the menu item service time
    * @param menuItemComments
    * 			: the new comments to give to the MenuItem
	* @throws MissingRequiredFieldsException 
    */
	public static void updateMenuItemAttributes(Key key, Key menuItemType, String menuItemName,
			Double menuItemPrice, Double menuItemDiscount, String menuItemDescription, 
			BlobKey menuItemImage, Integer menuItemServingTime, Boolean isAvailable, 
			Integer menuItemServiceTime, String menuItemComments) 
			throws MissingRequiredFieldsException, InvalidFieldFormatException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant restaurant = pm.getObjectById(Restaurant.class, key.getParent());
		
		Transaction tx = pm.currentTransaction();
		try {
			MenuItem menuItem = pm.getObjectById(MenuItem.class, key);
			tx.begin();
			menuItem.setMenuItemType(menuItemType);
			menuItem.setMenuItemName(menuItemName);
			menuItem.setMenuItemPrice(menuItemPrice);
			menuItem.setMenuItemDiscount(menuItemDiscount);
			menuItem.setMenuItemDescription(menuItemDescription);
			menuItem.setMenuItemImage(menuItemImage);
			menuItem.setMenuItemServingTime(menuItemServingTime);
			menuItem.setIsAvailable(isAvailable);
			menuItem.setMenuItemServiceTime(menuItemServiceTime);
			menuItem.setMenuItemComments(menuItemComments);
			
			restaurant.updateMenuVersion();
			tx.commit();
			log.info("MenuItem \"" + menuItemName + "\"'s attributes updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
}
