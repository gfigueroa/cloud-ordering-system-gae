/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.ArrayList;
import java.util.Date;
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
import com.google.appengine.api.datastore.Link;

import exceptions.MissingRequiredFieldsException;
import exceptions.ObjectExistsInDatastoreException;
import exceptions.UserValidationException;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the Restaurant class.
 * 
 */

public class RestaurantManager {
	
	private static final Logger log = 
	        Logger.getLogger(RestaurantManager.class.getName());

	/**
     * Get a Restaurant instance from the datastore given its user entity.
     * The method uses the user's email field to obtain the Restaurant key.
     * @param user
     * 			: the user belonging to this Restaurant
     * @return Restaurant instance, null if Restaurant is not found
     */
	public static Restaurant getRestaurant(User user) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Key key = KeyFactory.createKey(Restaurant.class.getSimpleName(), 
                                       user.getUserEmail().getEmail());
		Restaurant restaurant;
		try  {
			restaurant = pm.getObjectById(Restaurant.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return restaurant;
	}
	
	/**
     * Get a Restaurant instance from the datastore given the user's email.
     * The method uses this email to obtain the Restaurant key.
     * @param email
     * 			: the Restaurant's email address
     * @return Restaurant instance, null if Restaurant is not found
     */
	public static Restaurant getRestaurant(Email email) {		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Key key = KeyFactory.createKey(Restaurant.class.getSimpleName(), 
                                       email.getEmail());
		
		Restaurant restaurant;
		try  {
			restaurant = pm.getObjectById(Restaurant.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return restaurant;
	}
	
	/**
     * Get a Restaurant instance from the datastore given the Restaurant key.
     * @param key
     * 			: the Restaurant's key
     * @return Restaurant instance, null if Restaurant is not found
     */
	public static Restaurant getRestaurant(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Restaurant restaurant;
		try  {
			restaurant = pm.getObjectById(Restaurant.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return restaurant;
	}
	
	/**
     * Get ALL the Restaurants in the database and return them
     * in a List structure
     * @return all restaurants in the datastore
     * TODO: Make more efficient "touching" of the users
     */
    @SuppressWarnings("unchecked")
	public static List<Restaurant> getAllRestaurants() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Restaurant.class);

        try {
        	List<Restaurant> restaurants = (List<Restaurant>) query.execute();
        	// Touch the user to keep in memory
        	for (Restaurant restaurant : restaurants) {
        		restaurant.getUser();
        	}
        	return restaurants;
            //return (List<Restaurant>) query.execute();
        } finally {
        	pm.close();
            query.closeAll();
        }
    }
    
    /**
     * Get ALL the Restaurant in the datastore that belong to
     * the given restaurant type and returns them in a List structure
     * @parameter restaurantType:
     * 			the restaurant type filter
     * @return all restaurant in the datastore corresponding to
     * 			the given restaurant type
     * TODO: make more efficient "touching" of the restaurant
     */
	@SuppressWarnings("unchecked")
	public static List<Restaurant> getAllRestaurantsByType(Long restaurantType) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Restaurant.class);
        query.setFilter("restaurantType == type");
        query.declareParameters("Long type");

        List<Restaurant> result = null;
        try {
            result = (List<Restaurant>) query.execute(restaurantType);
            // touch
            for (Restaurant restaurant : result)
                restaurant.getRestaurantName();
        } 
        finally {
        	pm.close();
            query.closeAll();
        }

        return result;
    }
	
	/**
     * Get ALL the Restaurants in the datastore that belong to
     * the given store super type and returns them in a List structure
     * @parameter storeSuperType:
     * 			the store super type filter
     * @return all restaurants in the datastore corresponding to
     * 			the given store super type
     */
	@SuppressWarnings("unchecked")
	public static List<Restaurant> getAllStoresBySuperType(
			RestaurantType.StoreSuperType storeSuperType) {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Restaurant.class);

        List<Restaurant> result = null;
        ArrayList<Restaurant> finalResult = new ArrayList<Restaurant>();
        try {
            result = (List<Restaurant>) query.execute();
            for (Restaurant restaurant : result) {
            	RestaurantType restaurantType = null;
            	try {
	            	// Check store super type
	            	restaurantType = pm.getObjectById(
	            			RestaurantType.class, restaurant.getRestaurantType());
            	}
            	catch (JDOObjectNotFoundException e) {
            		log.info("Missing type");
            	}
            	if (restaurantType != null) {
	            	if (restaurantType.getStoreSuperType() == storeSuperType) {
	            		finalResult.add(restaurant);
	            	}
            	}
            }
        } 
        finally {
        	pm.close();
            query.closeAll();
        }

        return finalResult;
    }
	
	/**
     * Get ALL the stores in the datastore that have the given
     * service and returns them in a List structure
     * @parameter service:
     * 			the service filter
     * @return all stores in the datastore that have the given service parameter
     * TODO: make more efficient "touching" of the restaurant
     */
	@SuppressWarnings("unchecked")
	public static List<Restaurant> getAllStoresWithService(Restaurant.Service service) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Restaurant.class);
        
        switch (service) {
        	case NEWS:
        		query.setFilter("hasNewsService == true");
        		break;
        	case PRODUCTS:
        		query.setFilter("hasProductsService == true");
        		break;
        	case SERVICE_PROVIDERS:
        		query.setFilter("hasServiceProvidersService == true");
        		break;
        	case MESSAGES:
        		query.setFilter("hasMessagesService == true");
        		break;
        	default:
        		return null;
        }

        List<Restaurant> result = null;
        try {
            result = (List<Restaurant>) query.execute();
            // touch
            for (Restaurant restaurant : result)
                restaurant.getRestaurantName();
        } 
        finally {
        	pm.close();
            query.closeAll();
        }

        return result;
    }
	
	/**
     * Put Restaurant into datastore.
     * Restaurants the given Restaurant instance in the datastore calling the 
     * PersistenceManager's makePersistent() method.
     * @param RestaurantSimple
     * 			: the Restaurant instance to restaurant
     * @throws ObjectExistsInDatastoreException
     */
	public static void putRestaurant(Restaurant restaurant) 
           throws ObjectExistsInDatastoreException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		// Check if the user already exists in the datastore
		Email email = restaurant.getUser().getUserEmail();
		Key administratorKey = KeyFactory.createKey(
				Administrator.class.getSimpleName(), email.getEmail());
		Key customerKey = KeyFactory.createKey(Customer.class.getSimpleName(), 
				email.getEmail());
		if (DatastoreManager.entityExists(Restaurant.class, 
				restaurant.getKey()) || 
				DatastoreManager.entityExists(Administrator.class, 
						administratorKey) ||
				DatastoreManager.entityExists(Customer.class, customerKey)) {
			throw new ObjectExistsInDatastoreException(restaurant, "User \"" + 
					restaurant.getUser().getUserEmail().getEmail() + 
					"\" already exists in the datastore.");
		}
		
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.makePersistent(restaurant);
			tx.commit();
			log.info("Restaurant \"" + 
					restaurant.getUser().getUserEmail().getEmail() + 
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
    * Delete Restaurant from datastore.
    * Deletes the given Restaurant from the datastore calling the PersistenceManager's
    * deletePersistent() method.
    * @param RestaurantSimple
    * 			: the Restaurant instance to delete
    */
	public static void deleteRestaurant(Restaurant restaurant) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			restaurant = pm.getObjectById(Restaurant.class, 
					restaurant.getKey());
			String email = restaurant.getUser().getUserEmail().getEmail();
			tx.begin();
			pm.deletePersistent(restaurant);
			tx.commit();
			log.info("Restaurant \"" + email +
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
    * Delete Restaurant from datastore.
    * Deletes the Restaurant corresponding to the given email 
    * from the datastore calling the PersistenceManager's deletePersistent() method.
    * @param email
    * 			: the email of the Restaurant instance to delete
    */
	public static void deleteRestaurant(Email email) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Restaurant.class.getSimpleName(), 
					email.getEmail());
			Restaurant restaurant = pm.getObjectById(Restaurant.class, key);
			tx.begin();
			pm.deletePersistent(restaurant);
			tx.commit();
			log.info("Restaurant \"" + email.getEmail() + 
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
    * Update Restaurant password in datastore.
    * Update's the Restaurant's password in the datastore.
    * @param email
    * 			: the email of the Restaurant whose password will be changed
    * @param currentPassword
    * 			: the current password of this Restaurant
    * @param newPassword
    * 			: the new password for this Restaurant
    * @throws UserValidationException
	* @throws MissingRequiredFieldsException 
    */
	public static void updateRestaurantPassword(Email email, 
			String currentPassword, String newPassword) 
			throws UserValidationException, MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Restaurant.class.getSimpleName(), 
					email.getEmail());
			Restaurant restaurant = pm.getObjectById(Restaurant.class, key);
			tx.begin();
			if (restaurant.getUser().validateUser(email, currentPassword)) {
				restaurant.getUser().setUserPassword(newPassword);
				tx.commit();
				log.info("Restaurant \"" + email.getEmail() + 
						"\"'s password updated in datastore.");
			}
			else {
				tx.rollback();
				throw new UserValidationException(restaurant.getUser(), 
						"User email and/or password are incorrect.");
			}
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Update Restaurant password in datastore.
    * Update's the Restaurant's password in the datastore.
    * @param email
    * 			: the email of the Restaurant whose password will be changed
    * @param newPassword
    * 			: the new password for this Restaurant
	* @throws MissingRequiredFieldsException 
    */
	public static void updateRestaurantPassword(Email email, String newPassword) 
			throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Restaurant.class.getSimpleName(), 
					email.getEmail());
			Restaurant restaurant = pm.getObjectById(Restaurant.class, key);
			tx.begin();
			restaurant.getUser().setUserPassword(newPassword);
			tx.commit();
			log.info("Restaurant \"" + email.getEmail() + 
					"\"'s password updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Update Restaurant attributes.
    * Update's the given Restaurant's attributes in the datastore.
    * @param email
    * 			: the email of the Restaurant whose attributes will be updated
    * @param restaurantType
    * 			: the new restaurant type's key to give to the Restaurant
    * @param restaurantName
    * 			: the new name to give to the Restaurant
    * @param restaurantDescription
    * 			: the new description to give to the Restaurant
    * @param restaurantWebsite
    * 			: Restaurant website
    * @param restaurantLogo
    * 			: Restaurant logo blob key
    * @param restaurantOpeningTime
    * 			: the restaurant's opening time
    * @param restaurantClosingTime
    * 			: the restaurant's closing time
    * @param restaurantComments
    * 			: the new comments to give to the Restaurant
	* @throws MissingRequiredFieldsException 
    */
	public static void updateRestaurantAttributes(
			Email email,
			RestaurantType.StoreSuperType storeSuperType,
			Long restaurantType,
			Boolean hasNewsService,
			Boolean hasProductsService,
			Boolean hasServiceProvidersService,
			Boolean hasMessagesService,
            String restaurantName,
            Integer channelNumber,
            String restaurantDescription,
            Link restaurantWebsite, 
            BlobKey restaurantLogo, 
            Date restaurantOpeningTime,
            Date restaurantClosingTime,
            String restaurantComments) 
            		throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Restaurant.class.getSimpleName(), 
					email.getEmail());
			Restaurant restaurant = pm.getObjectById(Restaurant.class, key);
			tx.begin();
			restaurant.setRestaurantType(restaurantType);
			restaurant.setHasNewsService(hasNewsService);
			restaurant.setHasProductsService(hasProductsService);
			restaurant.setHasServiceProvidersService(hasServiceProvidersService);
			restaurant.setHasMessagesService(hasMessagesService);
			restaurant.setRestaurantName(restaurantName);
			restaurant.setChannelNumber(storeSuperType, channelNumber);
			restaurant.setRestaurantDescription(restaurantDescription);
			restaurant.setRestaurantWebsite(restaurantWebsite);
			restaurant.setRestaurantLogo(restaurantLogo);
			restaurant.setRestaurantOpeningTime(restaurantOpeningTime);
			restaurant.setRestaurantClosingTime(restaurantClosingTime);
			restaurant.setRestaurantComments(restaurantComments);
			tx.commit();
			log.info("Restaurant \"" + email.getEmail() + 
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
    * Update Restaurant Menu Version.
    * Updates the given Restaurant's Menu Version by 1 in the datastore.
    * @param restaurantKey
    * 			: the key of the Restaurant whose attributes will be updated
    */
	public static void updateRestaurantMenuVersion(Key restaurantKey) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Restaurant restaurant = pm.getObjectById(Restaurant.class, restaurantKey);
			tx.begin();
			restaurant.updateMenuVersion();
			tx.commit();
			log.info("Restaurant \"" + restaurant.getRestaurantName() + 
					"\"'s menu version updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Update Restaurant Menu Item Type Version.
    * Updates the given Restaurant's Menu Item Type Version by 1 in the datastore.
    * @param email
    * 			: the email of the Restaurant whose attributes will be updated
    */
	public static void updateRestaurantMenuItemTypeVersion(Email email) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Restaurant.class.getSimpleName(), 
					email.getEmail());
			Restaurant restaurant = pm.getObjectById(Restaurant.class, key);
			tx.begin();
			restaurant.updateMenuItemTypeVersion();
			tx.commit();
			log.info("Restaurant \"" + email.getEmail() + 
					"\"'s menu item type version updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Update Restaurant Additional Property Version.
    * Updates the given Restaurant's Additional Property Version by 1 in the datastore.
    * @param restaurantKey
    * 			: the key of the Restaurant whose attributes will be updated
    */
	public static void updateRestaurantAdditionalPropertyVersion(Key restaurantKey) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Restaurant restaurant = pm.getObjectById(Restaurant.class, restaurantKey);
			tx.begin();
			restaurant.updateAdditionalPropertyVersion();
			tx.commit();
			log.info("Restaurant \"" + restaurant.getRestaurantName() + 
					"\"'s additional property version updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
}
