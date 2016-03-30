/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import exceptions.InexistentObjectException;
import exceptions.MissingRequiredFieldsException;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the RestaurantNews class.
 * 
 */

public class RestaurantNewsManager {
	
	private static final Logger log = 
        Logger.getLogger(RestaurantNewsManager.class.getName());
	
	/**
     * Get a RestaurantNews instance from the datastore given the RestaurantNews key.
     * @param key
     * 			: the RestaurantNews's key
     * @return RestaurantNews instance, null if RestaurantNews is not found
     */
	public static RestaurantNews getRestaurantNews(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		RestaurantNews restaurantNews;
		try  {
			restaurantNews = pm.getObjectById(RestaurantNews.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return restaurantNews;
	}
	
	/**
     * Get all public active restaurant news in the datastore
     * and returns them in a List structure
     * @return all restaurant news that are "ACTIVE" or "ACTIVE_MAXED" and public
     * TODO: Make more efficient query
     */
	@SuppressWarnings("unchecked")
	public static List<RestaurantNews> getAllPublicActiveRestaurantNews() {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Query query = pm.newQuery(RestaurantNews.class);
		//query.setFilter("currentClicks < maxClicks && restaurantNewsEndingDate >= now");
		query.setFilter("restaurantNewsEndingDate >= now");
		query.declareParameters(Date.class.getName() + " now");

        ArrayList<RestaurantNews> finalResult = new ArrayList<RestaurantNews>();
        try {
        	Date now = new Date();
        	List<RestaurantNews> result = (List<RestaurantNews>) query.execute(now);
        	
            for (RestaurantNews restaurantNews : result) {
            	// Check if news are still active
            	if (restaurantNews.getCurrentStatus() == RestaurantNews.Status.ACTIVE ||
            			restaurantNews.getCurrentStatus() == RestaurantNews.Status.ACTIVE_MAXED) {
            		// Check if news is public
            		if (!restaurantNews.isPrivate()) {
            			finalResult.add(restaurantNews);
            		}
            	}
            }
        }
        finally {
        	pm.close();
        }

        return finalResult;
    }
	
	/**
     * Get ALL the restaurant news in the datastore from a specific restaurant
     * and returns them in a List structure
     * @param restaurantKey: 
     * 				the key of the restaurant whose restaurant news will be retrieved
     * @return all restaurant news in the datastore belonging to the given restaurant
     * TODO: Fix "touching" of restaurant news
     */
	public static List<RestaurantNews> getAllRestaurantNewsFromRestaurant(Key restaurantKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant restaurant = pm.getObjectById(Restaurant.class, restaurantKey);
		
        List<RestaurantNews> result = null;
        try {
            result = restaurant.getRestaurantNews();
            // Touch each branch
            for (RestaurantNews restaurantNews : result) {
            	restaurantNews.getKey();
            }
        } 
        finally {
        	pm.close();
        }

        return result;
    }
	
	/**
     * Get inactive restaurant news in the datastore from a specific restaurant
     * and returns them in a List structure
     * @param restaurantKey: 
     * 				the key of the restaurant whose restaurant news will be retrieved
     * @return all restaurant news that are "INACTIVE" belonging to the given restaurant
     */
	public static List<RestaurantNews> getInactiveRestaurantNewsFromRestaurant(Key restaurantKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant restaurant = pm.getObjectById(Restaurant.class, restaurantKey);
		
        List<RestaurantNews> result = null;
        ArrayList<RestaurantNews> finalResult = new ArrayList<RestaurantNews>();
        try {
            result = restaurant.getRestaurantNews();
            for (RestaurantNews restaurantNews : result) {
            	if (restaurantNews.getCurrentStatus() == RestaurantNews.Status.INACTIVE) {
            		finalResult.add(restaurantNews);
            	}
            }
        }
        finally {
        	pm.close();
        }

        return finalResult;
    }
	
	/**
     * Get active restaurant news in the datastore from a specific restaurant
     * and returns them in a List structure
     * @param restaurantKey: 
     * 				the key of the restaurant whose restaurant news will be retrieved
     * @return all restaurant news that are "ACTIVE" or "ACTIVE_MAXED" belonging to the given restaurant
     */
	public static List<RestaurantNews> getActiveRestaurantNewsFromRestaurant(Key restaurantKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant restaurant = pm.getObjectById(Restaurant.class, restaurantKey);
		
        List<RestaurantNews> result = null;
        ArrayList<RestaurantNews> finalResult = new ArrayList<RestaurantNews>();
        try {
            result = restaurant.getRestaurantNews();
            for (RestaurantNews restaurantNews : result) {
            	if (restaurantNews.getCurrentStatus() == RestaurantNews.Status.ACTIVE ||
            			restaurantNews.getCurrentStatus() == RestaurantNews.Status.ACTIVE_MAXED) {
            		finalResult.add(restaurantNews);
            	}
            }
        }
        finally {
        	pm.close();
        }

        return finalResult;
    }
	
	/**
     * Get private active restaurant news in the datastore from a specific restaurant
     * and returns them in a List structure
     * @param restaurantKey: 
     * 				the key of the restaurant whose restaurant news will be retrieved
     * @return all private restaurant news that are "ACTIVE" or "ACTIVE_MAXED" 
     * 				belonging to the given restaurant
     */
	public static List<RestaurantNews> getPrivateActiveRestaurantNewsFromRestaurant(Key restaurantKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant restaurant = pm.getObjectById(Restaurant.class, restaurantKey);
		
        List<RestaurantNews> result = null;
        ArrayList<RestaurantNews> finalResult = new ArrayList<RestaurantNews>();
        try {
            result = restaurant.getRestaurantNews();
            for (RestaurantNews restaurantNews : result) {
            	// Check that news are still active
            	if (restaurantNews.getCurrentStatus() == RestaurantNews.Status.ACTIVE ||
            			restaurantNews.getCurrentStatus() == RestaurantNews.Status.ACTIVE_MAXED) {
            		// Check that news are private
            		if (restaurantNews.isPrivate()) {
            			finalResult.add(restaurantNews);
            		}
            	}
            }
        }
        finally {
        	pm.close();
        }

        return finalResult;
    }
	
	/**
     * Get expired restaurant news in the datastore from a specific restaurant
     * and returns them in a List structure
     * @param restaurantKey: 
     * 				the key of the restaurant whose restaurant news will be retrieved
     * @return all restaurant news that are "EXPIRED" or "EXPIRED_MAXED" belonging to the given restaurant
     */
	public static List<RestaurantNews> getExpiredRestaurantNewsFromRestaurant(Key restaurantKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant restaurant = pm.getObjectById(Restaurant.class, restaurantKey);
		
        List<RestaurantNews> result = null;
        ArrayList<RestaurantNews> finalResult = new ArrayList<RestaurantNews>();
        try {
            result = restaurant.getRestaurantNews();
            for (RestaurantNews restaurantNews : result) {
            	if (restaurantNews.getCurrentStatus() == RestaurantNews.Status.EXPIRED ||
            			restaurantNews.getCurrentStatus() == RestaurantNews.Status.EXPIRED_MAXED) {
            		finalResult.add(restaurantNews);
            	}
            }
        }
        finally {
        	pm.close();
        }

        return finalResult;
    }
	
	/**
     * Check if a customer has already clicked on a specific News
     * @param customerKey:
     * 				the key of the customer who will be checked
     * @param restaurantNewsKey:
     * 				the key of the Restaurant News that will be checked
     * @return True if customer has already clicked on this news, False otherwise
     */
    @SuppressWarnings("unchecked")
	public static boolean customerAlreadyClickedNews(Key customerKey, Key restaurantNewsKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();

        Query query = pm.newQuery(CustomerRestaurantNews.class);
    	query.setFilter("customer == ckey && restaurantNews == nkey");
        query.declareParameters(Key.class.getName() + " ckey, " + Key.class.getName() + " nkey");
        
        log.info("Query: " + query.toString());
        
        try {
        	List<CustomerRestaurantNews> result = 
        			(List<CustomerRestaurantNews>) query.execute(customerKey, restaurantNewsKey);
        	
        	if (result == null || result.isEmpty()) {
        		return false;
        	}
        	else {
        		return true;
        	}
        }
        finally {
        	pm.close();
            query.closeAll();
        }
    }
	
	/**
     * Put RestaurantNews into datastore.
     * Stores the given RestaurantNews instance in the datastore for this
     * restaurant.
     * @param email
     * 			: the email of the Restaurant where the restaurantNews will be added
     * @param restaurantNews
     * 			: the RestaurantNews instance to store
     */
	public static void putRestaurantNews(Email email, RestaurantNews restaurantNews) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Restaurant.class.getSimpleName(), email.getEmail());
			Restaurant restaurant = pm.getObjectById(Restaurant.class, key);
			tx.begin();
			restaurant.addRestaurantNews(restaurantNews);
			tx.commit();
			log.info("RestaurantNews \"" + restaurantNews.getRestaurantNewsContent() + 
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
    * Delete RestaurantNews from datastore.
    * Deletes the RestaurantNews corresponding to the given key
    * from the datastore calling the PersistenceManager's 
    * deletePersistent() method.
    * @param key
    * 			: the key of the RestaurantNews instance to delete
    */
	public static void deleteRestaurantNews(Key key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Restaurant restaurant = pm.getObjectById(Restaurant.class, key.getParent());
			RestaurantNews restaurantNews = pm.getObjectById(RestaurantNews.class, key);
			String restaurantNewsContent = restaurantNews.getRestaurantNewsContent();
			tx.begin();
			restaurant.removeRestaurantNews(restaurantNews);
			tx.commit();
			log.info("RestaurantNews \"" + restaurantNewsContent + 
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
    * Update RestaurantNews attributes.
    * Update's the given RestaurantNews's attributes in the datastore.
    * @param key
    * 			: the key of the RestaurantNews whose attributes will be updated
    * @param restaurantNewsTitle
    * 			: the title of the news
    * @param restaurantNewsContent
    * 			: the content of the news
    * @param maxClicks
    * 			: the max number of clicks allowed for this news
    * @param restaurantNewStartingDate
    * 			: the starting date of this news
    * @param restaurantNewsEndingDate
    * 			: the ending date of this news
    * @param restaurantNewsPriority
    * 			: the priority for this news represented as a number
    * @param isPrivate
    * 			: whether this news is private or not
    * @param restaurantNewsImage
    * 			: the blobkey of this news' image
	* @throws MissingRequiredFieldsException 
    */
	public static void updateRestaurantNewsAttributes(
			Key key, String restaurantNewsTitle, String restaurantNewsContent,
			Integer maxClicks, Date restaurantNewsStartingDate,
			Date restaurantNewsEndingDate, Integer restaurantNewsPriority,
			Boolean isPrivate,
			BlobKey restaurantNewsImage) 
                       throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			RestaurantNews restaurantNews = pm.getObjectById(RestaurantNews.class, key);
			tx.begin();
			restaurantNews.setRestaurantNewsTitle(restaurantNewsTitle);
			restaurantNews.setRestaurantNewsContent(restaurantNewsContent);
			restaurantNews.setMaxClicks(maxClicks);
			restaurantNews.setRestaurantNewsStartingDate(restaurantNewsStartingDate);
			restaurantNews.setRestaurantNewsEndingDate(restaurantNewsEndingDate);
			restaurantNews.setRestaurantNewsPriority(restaurantNewsPriority);
			restaurantNews.setIsPrivate(isPrivate);
			restaurantNews.setRestaurantNewsImage(restaurantNewsImage);
			tx.commit();
			log.info("RestaurantNews \"" + restaurantNews.getRestaurantNewsContent() + 
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
    * Add RestaurantNews click.
    * Add a click to this restaurant news.
    * @param key
    * 			: the key of the RestaurantNews whose attributes will be updated
    * @return True if this news is still ACTIVE, False otherwise
    */
	public static boolean addRestaurantNewsClick(Key key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			RestaurantNews restaurantNews = pm.getObjectById(RestaurantNews.class, key);
			boolean clickSuccessful = false;
			tx.begin();
			clickSuccessful = restaurantNews.addClick();
			tx.commit();
			
			if (clickSuccessful) {
				log.info("Added a click to the news \"" + restaurantNews.getRestaurantNewsTitle() + "\".");
			}
			else {
				log.info("Adding a click to the news \"" + restaurantNews.getRestaurantNewsTitle() + "\" failed.");
			}
			
			return clickSuccessful;
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
}
