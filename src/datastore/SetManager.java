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
 * (get, put, delete, update) made on the Set class.
 */
public class SetManager {
	
	private static final Logger log = 
        Logger.getLogger(SetManager.class.getName());

	/**
	 * Get a set using its complex key (includes the Restaurant key as well)
	 * @param key
	 *        : The set's key
	 * @return Set 
	 */
	public static Set getSet(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Set set;
		try  {
			set = pm.getObjectById(Set.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return set;
	}

	/**
     * Get ALL the Sets in the datastore that belong to
     * the given set type and returns them in a List structure
     * @parameter setType:
     * 			the set type filter
     * @return all sets in the datastore corresponding to
     * 			the given set type
     * TODO: make more efficient "touching" of the sets
     */
	@SuppressWarnings("unchecked")
	public static List<Set> getAllSetsByType(Set.SetType setType) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Set.class);
        query.setFilter("setType == type");
        query.setOrdering("setTime asc");
        query.declareParameters("Enum type");

        List<Set> result = null;
        try {
            result = (List<Set>) query.execute(setType);
            // touch
            for (Set set : result)
                set.getSetName();
        } 
        finally {
        	pm.close();
            query.closeAll();
        }

        return result;
    }
	
	/**
     * Get ALL the Sets in the datastore from a specific store
     * and returns them in a List structure
     * @param storeKey: 
     * 				the key of the store whose sets will be retrieved
     * @return all sets in the datastore belonging to the given store
     * TODO: Fix "touching" of sets
     */
	public static List<Set> getAllSetsByStore(Key storeKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant restaurant = pm.getObjectById(Restaurant.class, storeKey);
		
        List<Set> result = null;
        try {
            result = restaurant.getSets();
            // Touch each branch
            for (Set set : result) {
            	set.getSetName();
            }
        } 
        finally {
        	pm.close();
        }

        return result;
    }
	
	/**
    * Add set to a Store.
    * Add a new set in the datastore for this Store.
    * @param email
    * 			: the email of the Store where the set will be added
    * @param set
    * 			: the set to be added
    */
	public static void putSet(Email email, Set set) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Restaurant.class.getSimpleName(), 
					email.getEmail());
			Restaurant restaurant = pm.getObjectById(Restaurant.class, key);
			tx.begin();
			restaurant.addSet(set);
			restaurant.updateSetVersion();
			tx.commit();
			log.info("Set \"" + set.getSetName() + "\" added to Restaurant \"" + 
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
    * Delete set.
    * Delete a set in the datastore.
    * @param key
    * 			: the key of the set to delete (includes Store key)
    */
	public static void deleteSet(Key key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Restaurant restaurant = pm.getObjectById(Restaurant.class, key.getParent());
			Set set = pm.getObjectById(Set.class, key);
			String setName = set.getSetName();
			tx.begin();
			restaurant.removeSet(set);
			restaurant.updateSetVersion();
			tx.commit();
			log.info("Set \"" + setName + "\" deleted from Restaurant \"" + 
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
    * Update Set attributes.
    * Updates the given Set's attributes in the datastore.
    * @param key
    * 			: the key of the Set whose attributes will be updated
    * @param setNumber
    * 			: the number to give to the Set
    * @param setName
    * 			: the new name to give to the Set
    * @param setDescription
    * 			: the new description to give to the Set
    * @param setPrice
    * 			: the new price to give to the Set
    * @param hasFixedPrice
    * 			: whether this Set has a fixed price or not
    * @param setDiscount
    * 			: the new discount to give to the Set
    * @param setImage
    * 			: the new image blob key to give to the Set
    * @param setServingTime
    * 			: the serving time of this set (in minutes)
    * @param isAvailable
    * 			: whether the Set is available or not
    * @param setServiceTime
    * 			: the set service time
    * @param setComments
    * 			: the new comments to give to the Set
	* @throws MissingRequiredFieldsException 
    */
	public static void updateSetAttributes(Key key, Integer setNumber, String setName,
			String setDescription, Double setPrice, Boolean hasFixedPrice,
			Double setDiscount, BlobKey setImage, Integer setServingTime, 
			Boolean isAvailable, Integer setServiceTime, String setComments,
			ArrayList<Key> menuItems, ArrayList<TypeSetMenuItem> typeSetMenuItems) 
			throws MissingRequiredFieldsException, InvalidFieldFormatException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			
			Set set = pm.getObjectById(Set.class, key);
			Restaurant restaurant = pm.getObjectById(Restaurant.class, 
					key.getParent());
			
			tx.begin();
			
			set.setSetNumber(setNumber);
			set.setSetName(setName);
			set.setSetDescription(setDescription);
			set.setSetPrice(setPrice);
			set.setHasFixedPrice(hasFixedPrice);
			set.setSetDiscount(setDiscount);
			set.setSetImage(setImage);
			set.setSetServingTime(setServingTime);
			set.setIsAvailable(isAvailable);
			set.setSetServiceTime(setServiceTime);
			set.setSetComments(setComments);
			set.setMenuItems(menuItems);
			set.setTypeSetMenuItems(typeSetMenuItems);
			
			restaurant.updateSetVersion();
			
			tx.commit();
			log.info("Set \"" + setName + "\"'s attributes updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

}
