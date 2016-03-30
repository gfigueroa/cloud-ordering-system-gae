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
 * made on the RestaurantOpinionPoll class.
 * 
 */

public class RestaurantOpinionPollManager {
	
	private static final Logger log = 
        Logger.getLogger(RestaurantOpinionPollManager.class.getName());
	
	/**
     * Get a RestaurantOpinionPoll instance from the datastore given the RestaurantOpinionPoll key.
     * @param key
     * 			: the RestaurantOpinionPoll's key
     * @return RestaurantOpinionPoll instance, null if RestaurantOpinionPoll is not found
     */
	public static RestaurantOpinionPoll getRestaurantOpinionPoll(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		RestaurantOpinionPoll restaurantOpinionPoll;
		try  {
			restaurantOpinionPoll = pm.getObjectById(RestaurantOpinionPoll.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return restaurantOpinionPoll;
	}
	
	/**
     * Get all active restaurant opinion polls in the datastore
     * and returns them in a List structure
     * @return all restaurant opinion polls that are "ACTIVE"
     */
	@SuppressWarnings("unchecked")
	public static List<RestaurantOpinionPoll> getAllActiveRestaurantOpinionPolls() {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Query query = pm.newQuery(RestaurantOpinionPoll.class);
		query.setFilter("restaurantOpinionPollEndingDate >= now");
		query.declareParameters(Date.class.getName() + " now");

        ArrayList<RestaurantOpinionPoll> finalResult = new ArrayList<RestaurantOpinionPoll>();
        try {
        	Date now = new Date();
        	List<RestaurantOpinionPoll> result = (List<RestaurantOpinionPoll>) query.execute(now);
        	
            for (RestaurantOpinionPoll restaurantOpinionPoll : result) {
            	// Check that they are ACTIVE
            	if (restaurantOpinionPoll.getCurrentStatus() == RestaurantOpinionPoll.Status.ACTIVE) {
            		finalResult.add(restaurantOpinionPoll);
            	}
            }
        }
        finally {
        	pm.close();
        }

        return finalResult;
    }
	
	/**
     * Get ALL the restaurant opinion polls in the datastore from a specific restaurant
     * and returns them in a List structure
     * @param restaurantKey: 
     * 				the key of the restaurant whose restaurant opinion polls will be retrieved
     * @return all restaurant opinion polls in the datastore belonging to the given restaurant
     * TODO: Fix "touching" of restaurant opinion polls
     */
	public static List<RestaurantOpinionPoll> getAllRestaurantOpinionPollsFromRestaurant(Key restaurantKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant restaurant = pm.getObjectById(Restaurant.class, restaurantKey);
		
        List<RestaurantOpinionPoll> result = null;
        try {
            result = restaurant.getRestaurantOpinionPolls();
            // Touch each branch
            for (RestaurantOpinionPoll restaurantOpinionPoll : result) {
            	restaurantOpinionPoll.getKey();
            }
        } 
        finally {
        	pm.close();
        }

        return result;
    }
	
	/**
     * Get inactive restaurant opinion polls in the datastore from a specific restaurant
     * and returns them in a List structure
     * @param restaurantKey: 
     * 				the key of the restaurant whose restaurant opinion poll will be retrieved
     * @return all restaurant opinion polls that are "INACTIVE" belonging to the given restaurant
     */
	public static List<RestaurantOpinionPoll> getInactiveRestaurantOpinionPollsFromRestaurant(Key restaurantKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant restaurant = pm.getObjectById(Restaurant.class, restaurantKey);
		
        List<RestaurantOpinionPoll> result = null;
        ArrayList<RestaurantOpinionPoll> finalResult = new ArrayList<RestaurantOpinionPoll>();
        try {
            result = restaurant.getRestaurantOpinionPolls();
            for (RestaurantOpinionPoll restaurantOpinionPoll : result) {
            	if (restaurantOpinionPoll.getCurrentStatus() == RestaurantOpinionPoll.Status.INACTIVE) {
            		finalResult.add(restaurantOpinionPoll);
            	}
            }
        }
        finally {
        	pm.close();
        }

        return finalResult;
    }
	
	/**
     * Get active restaurant opinion polls in the datastore from a specific restaurant
     * and returns them in a List structure
     * @param restaurantKey: 
     * 				the key of the restaurant whose restaurant opinion polls will be retrieved
     * @return all restaurant opinion polls that are "ACTIVE" belonging to the given restaurant
     */
	public static List<RestaurantOpinionPoll> getActiveRestaurantOpinionPollsFromRestaurant(Key restaurantKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant restaurant = pm.getObjectById(Restaurant.class, restaurantKey);
		
        List<RestaurantOpinionPoll> result = null;
        ArrayList<RestaurantOpinionPoll> finalResult = new ArrayList<RestaurantOpinionPoll>();
        try {
            result = restaurant.getRestaurantOpinionPolls();
            for (RestaurantOpinionPoll restaurantOpinionPoll : result) {
            	if (restaurantOpinionPoll.getCurrentStatus() == RestaurantOpinionPoll.Status.ACTIVE) {
            		finalResult.add(restaurantOpinionPoll);
            	}
            }
        }
        finally {
        	pm.close();
        }

        return finalResult;
    }
	
	/**
     * Get expired restaurant opinion polls in the datastore from a specific restaurant
     * and returns them in a List structure
     * @param restaurantKey: 
     * 				the key of the restaurant whose restaurant opinion polls will be retrieved
     * @return all restaurant opinion polls that are "EXPIRED" or "EXPIRED_MAXED" belonging to the given restaurant
     */
	public static List<RestaurantOpinionPoll> getExpiredRestaurantOpinionPollsFromRestaurant(Key restaurantKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant restaurant = pm.getObjectById(Restaurant.class, restaurantKey);
		
        List<RestaurantOpinionPoll> result = null;
        ArrayList<RestaurantOpinionPoll> finalResult = new ArrayList<RestaurantOpinionPoll>();
        try {
            result = restaurant.getRestaurantOpinionPolls();
            for (RestaurantOpinionPoll restaurantOpinionPoll : result) {
            	if (restaurantOpinionPoll.getCurrentStatus() == RestaurantOpinionPoll.Status.EXPIRED) {
            		finalResult.add(restaurantOpinionPoll);
            	}
            }
        }
        finally {
        	pm.close();
        }

        return finalResult;
    }
	
	/**
     * Check if a customer has already clicked on a specific OpinionPoll
     * @param customerKey:
     * 				the key of the customer who will be checked
     * @param restaurantOpinionPollKey:
     * 				the key of the Restaurant OpinionPoll that will be checked
     * @return True if customer has already clicked on this opinion poll, False otherwise
     */
    @SuppressWarnings("unchecked")
	public static boolean customerAlreadyClickedOpinionPoll(Key customerKey, Key restaurantOpinionPollKey) {
    	PersistenceManager pm = PMF.get().getPersistenceManager();

        Query query = pm.newQuery(CustomerRestaurantOpinionPoll.class);
    	query.setFilter("customer == ckey && restaurantOpinionPoll == okey");
        query.declareParameters(Key.class.getName() + " ckey, " + Key.class.getName() + " okey");
        
        log.info("Query: " + query.toString());
        
        try {
        	List<CustomerRestaurantOpinionPoll> result = 
        			(List<CustomerRestaurantOpinionPoll>) query.execute(customerKey, restaurantOpinionPollKey);
        	
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
     * Get the current opinion poll statistics in a formatted string.
     * @param restaurantOpinionPoll
     * 			: the restaurant opinion poll
     * @return a string representation of the current statistics for this opinion poll
     */
    public static String getRestaurantOpinionPollStatistics(RestaurantOpinionPoll restaurantOpinionPoll) {
    	
    	RestaurantOpinionPoll.Type type = restaurantOpinionPoll.getRestaurantOpinionPollType();
    	
    	List<CustomerRestaurantOpinionPoll> resultList = 
    			CustomerRestaurantOpinionPollManager.getAllCustomerRestaurantOpinionPolls(
    					restaurantOpinionPoll.getKey());
    	
    	String statistics = "";
    	
    	switch (type) {
    		case BINARY:
    			
    			int binaryChoice1Total = 0, binaryChoice2Total = 0;
    			double binaryChoice1Percentage, binaryChoice2Percentage;
    			for (CustomerRestaurantOpinionPoll crop : resultList) {
    				if (crop.getResponse().equals(restaurantOpinionPoll.getBinaryChoice1())) {
    					binaryChoice1Total++;
    				}
    				else if (crop.getResponse().equals(restaurantOpinionPoll.getBinaryChoice2())) {
    					binaryChoice2Total++;
    				}
    			}
    			
    			binaryChoice1Percentage = ((double) binaryChoice1Total / resultList.size()) * 100;
    			binaryChoice2Percentage = ((double) binaryChoice2Total / resultList.size()) * 100;
    			
    			statistics += 
    					restaurantOpinionPoll.getBinaryChoice1() + 
    					": " + Math.round(binaryChoice1Percentage) + "%\n";
    			statistics += 
    					restaurantOpinionPoll.getBinaryChoice2() + 
    					": " + Math.round(binaryChoice2Percentage) + "%";
    			break;
    		case RATING:
    			
    			long totalRating = 0;
    			double averageRating;
    			for (CustomerRestaurantOpinionPoll crop : resultList) {
    				int rating = Integer.parseInt(crop.getResponse());
    				totalRating += rating;
    			}
    			averageRating = (double) totalRating / resultList.size();
    			
    			statistics += "平均評分: " + Math.round(averageRating);
    			break;
    		case MULTIPLE_CHOICE:
    			List<RestaurantOpinionPollMultipleChoiceValue> values = 
    					restaurantOpinionPoll.getMultipleChoiceValues();
    			ArrayList<Integer> valuesTotal = new ArrayList<Integer>();
    			
    			// Initialize totals array with 0
    			for (int i = 0; i < values.size(); i++) {
    				valuesTotal.add(0);
    			}
    			
    			// For non-multiple selection
    			if (!restaurantOpinionPoll.allowsMultipleSelection()) {
    				for (CustomerRestaurantOpinionPoll crop : resultList) {
    					int index = Integer.parseInt(crop.getResponse());
    					int total = valuesTotal.get(index);
    					valuesTotal.set(index, total + 1);
    				}
    			}
    			// For multiple selection
    			else {
    				for (CustomerRestaurantOpinionPoll crop : resultList) {
    					String[] indexStrings = crop.getResponse().split(",");
    					for (String indexString : indexStrings) {
    						int index = Integer.parseInt(indexString);
    						int total = valuesTotal.get(index);
    						valuesTotal.set(index, total + 1);
    					}
    				}
    			}
    			
    			for (int i = 0; i < values.size(); i++) {
    				statistics += values.get(i).getMultipleChoiceValue() + ": " + 
    						valuesTotal.get(i) + "\n";
    			}
    			break;
    		default:
    			break;
    	}
    	
    	return statistics;
    }
	
	/**
     * Put RestaurantOpinionPoll into datastore.
     * Stores the given RestaurantOpinionPoll instance in the datastore for this
     * restaurant.
     * @param email
     * 			: the email of the Restaurant where the restaurantOpinionPoll will be added
     * @param restaurantOpinionPoll
     * 			: the RestaurantOpinionPoll instance to store
     */
	public static void putRestaurantOpinionPoll(Email email, RestaurantOpinionPoll restaurantOpinionPoll) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Restaurant.class.getSimpleName(), email.getEmail());
			Restaurant restaurant = pm.getObjectById(Restaurant.class, key);
			tx.begin();
			restaurant.addRestaurantOpinionPoll(restaurantOpinionPoll);
			tx.commit();
			log.info("RestaurantOpinionPoll \"" + restaurantOpinionPoll.getRestaurantOpinionPollContent() + 
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
    * Delete RestaurantOpinionPoll from datastore.
    * Deletes the RestaurantOpinionPoll corresponding to the given key
    * from the datastore calling the PersistenceManager's 
    * deletePersistent() method.
    * @param key
    * 			: the key of the RestaurantOpinionPoll instance to delete
    */
	public static void deleteRestaurantOpinionPoll(Key key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			RestaurantOpinionPoll opinionPoll = pm.getObjectById(RestaurantOpinionPoll.class, key);
			String restaurantOpinionPollTitle = opinionPoll.getRestaurantOpinionPollContent();
			
			Restaurant restaurant = pm.getObjectById(Restaurant.class, key.getParent());
			tx.begin();
			restaurant.removeRestaurantOpinionPoll(opinionPoll);
			tx.commit();
			
			log.info("RestaurantOpinionPoll \"" + restaurantOpinionPollTitle + 
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
    * Update RestaurantOpinionPoll attributes.
    * Update's the given RestaurantOpinionPoll's attributes in the datastore.
    * @param key
    * 			: the key of the RestaurantOpinionPoll whose attributes will be updated
    * @param restaurantOpinionPollTitle
    * 			: the title of the opinion poll
    * @param restaurantOpinionPollContent
    * 			: the content of the opinion poll
    * @param restaurantNewStartingDate
    * 			: the starting date of this opinion poll
    * @param restaurantOpinionPollEndingDate
    * 			: the ending date of this opinion poll
    * @param restaurantOpinionPollPriority
    * 			: the priority for this opinion poll represented as a number
    * @param publicResults
    * 			: whether this opinion poll's results are public or not
    * @param restaurantOpinionPollImage
    * 			: the blobkey of this opinion poll' image
    * @param binaryChoice1
    * 			: the first binary choice for BINARY type
    * @param binaryChoice2
    * 			: the second binary choice for BINARY type
    * @param ratingLowValue
    * 			: the lowest possible value for RATING type
    * @param ratingHighValue
    * 			: the highest possible value for RATING type
    * @param allowMultipleSelection
    * 			: whether this opinion poll allows multiple selection or not
    * 				for MULTIPLE_CHOICE type
	* @throws MissingRequiredFieldsException 
    */
	public static void updateRestaurantOpinionPollAttributes(
			Key key, String restaurantOpinionPollTitle, String restaurantOpinionPollContent,
			Date restaurantOpinionPollStartingDate, Date restaurantOpinionPollEndingDate, 
			Integer restaurantOpinionPollPriority, Boolean publicResults,
			BlobKey restaurantOpinionPollImage,
			String binaryChoice1, String binaryChoice2, Integer ratingLowValue, 
			Integer ratingHighValue, Boolean allowMultipleSelection) 
                       throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			RestaurantOpinionPoll restaurantOpinionPoll = pm.getObjectById(RestaurantOpinionPoll.class, key);
			tx.begin();
			restaurantOpinionPoll.setRestaurantOpinionPollTitle(restaurantOpinionPollTitle);
			restaurantOpinionPoll.setRestaurantOpinionPollContent(restaurantOpinionPollContent);
			restaurantOpinionPoll.setRestaurantOpinionPollStartingDate(restaurantOpinionPollStartingDate);
			restaurantOpinionPoll.setRestaurantOpinionPollEndingDate(restaurantOpinionPollEndingDate);
			restaurantOpinionPoll.setRestaurantOpinionPollPriority(restaurantOpinionPollPriority);
			restaurantOpinionPoll.setPublicResults(publicResults);
			restaurantOpinionPoll.setRestaurantOpinionPollImage(restaurantOpinionPollImage);
			restaurantOpinionPoll.setBinaryChoice1(binaryChoice1);
			restaurantOpinionPoll.setBinaryChoice2(binaryChoice2);
			restaurantOpinionPoll.setRatingLowValue(ratingLowValue);
			restaurantOpinionPoll.setRatingHighValue(ratingHighValue);
			restaurantOpinionPoll.setAllowMultipleChoiceSelection(allowMultipleSelection);
			tx.commit();
			log.info("RestaurantOpinionPoll \"" + restaurantOpinionPoll.getRestaurantOpinionPollTitle() + 
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
    * Add RestaurantOpinionPoll click.
    * Add a click to this restaurant opinion poll.
    * @param key
    * 			: the key of the RestaurantOpinionPoll whose attributes will be updated
    * @return True if this opinion poll is still ACTIVE, False otherwise
    */
	public static boolean addRestaurantOpinionPollClick(Key key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			RestaurantOpinionPoll restaurantOpinionPoll = pm.getObjectById(RestaurantOpinionPoll.class, key);
			boolean clickSuccessful = false;
			tx.begin();
			clickSuccessful = restaurantOpinionPoll.addClick();
			tx.commit();
			
			if (clickSuccessful) {
				log.info("Added a click to the opinion poll \"" + restaurantOpinionPoll.getRestaurantOpinionPollTitle() + "\".");
			}
			else {
				log.info("Adding a click to the opinion poll \"" + restaurantOpinionPoll.getRestaurantOpinionPollTitle() + "\" failed.");
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
