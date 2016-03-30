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
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.appengine.api.datastore.Key;

import exceptions.InexistentObjectException;
import exceptions.MissingRequiredFieldsException;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the SurveyOpinionPoll class.
 * 
 */

public class SurveyOpinionPollManager {
	
	private static final Logger log = 
        Logger.getLogger(SurveyOpinionPollManager.class.getName());
	
	/**
     * Get a SurveyOpinionPoll instance from the datastore given the SurveyOpinionPoll key.
     * @param key
     * 			: the SurveyOpinionPoll's key
     * @return SurveyOpinionPoll instance, null if SurveyOpinionPoll is not found
     */
	public static SurveyOpinionPoll getSurveyOpinionPoll(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		SurveyOpinionPoll surveyOpinionPoll;
		try  {
			surveyOpinionPoll = pm.getObjectById(SurveyOpinionPoll.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return surveyOpinionPoll;
	}
	
	/**
     * Check if a customer has already clicked on a specific OpinionPoll
     * @param customerKey:
     * 				the key of the customer who will be checked
     * @param surveyOpinionPollKey:
     * 				the key of the Survey OpinionPoll that will be checked
     * @return True if customer has already clicked on this opinion poll, False otherwise
     */
    @SuppressWarnings("unchecked")
	public static boolean customerAlreadyClickedOpinionPoll(Key customerKey, Key surveyOpinionPollKey) {
    	PersistenceManager pm = PMF.get().getPersistenceManager();

        Query query = pm.newQuery(CustomerSurveyOpinionPoll.class);
    	query.setFilter("customer == ckey && surveyOpinionPoll == okey");
        query.declareParameters(Key.class.getName() + " ckey, " + Key.class.getName() + " okey");
        
        log.info("Query: " + query.toString());
        
        try {
        	List<CustomerSurveyOpinionPoll> result = 
        			(List<CustomerSurveyOpinionPoll>) query.execute(customerKey, surveyOpinionPollKey);
        	
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
     * @param surveyOpinionPollKey
     * 			: the survey opinion poll key
     * @return a string representation of the current statistics for this opinion poll
     */
    public static String getSurveyOpinionPollStatistics(SurveyOpinionPoll surveyOpinionPoll) {
    	
    	SurveyOpinionPoll.Type type = surveyOpinionPoll.getSurveyOpinionPollType();
    	
    	List<CustomerSurveyOpinionPoll> resultList = 
    			CustomerSurveyOpinionPollManager.getAllCustomerSurveyOpinionPolls(
    					surveyOpinionPoll.getKey());
    	
    	String statistics = "";
    	
    	switch (type) {
    		case BINARY:
    			
    			int binaryChoice1Total = 0, binaryChoice2Total = 0;
    			double binaryChoice1Percentage, binaryChoice2Percentage;
    			for (CustomerSurveyOpinionPoll crop : resultList) {
    				if (crop.getResponse().equals(surveyOpinionPoll.getBinaryChoice1())) {
    					binaryChoice1Total++;
    				}
    				else if (crop.getResponse().equals(surveyOpinionPoll.getBinaryChoice2())) {
    					binaryChoice2Total++;
    				}
    			}
    			
    			binaryChoice1Percentage = ((double) binaryChoice1Total / resultList.size()) * 100;
    			binaryChoice2Percentage = ((double) binaryChoice2Total / resultList.size()) * 100;
    			
    			statistics += 
    					surveyOpinionPoll.getBinaryChoice1() + 
    					": " + Math.round(binaryChoice1Percentage) + "%\n";
    			statistics += 
    					surveyOpinionPoll.getBinaryChoice2() + 
    					": " + Math.round(binaryChoice2Percentage) + "%";
    			break;
    		case RATING:
    			
    			long totalRating = 0;
    			double averageRating;
    			for (CustomerSurveyOpinionPoll crop : resultList) {
    				int rating = Integer.parseInt(crop.getResponse());
    				totalRating += rating;
    			}
    			averageRating = (double) totalRating / resultList.size();
    			
    			statistics += "平均評分: " + Math.round(averageRating);
    			break;
    		case MULTIPLE_CHOICE:
    			List<SurveyOpinionPollMultipleChoiceValue> values = 
    					surveyOpinionPoll.getMultipleChoiceValues();
    			ArrayList<Integer> valuesTotal = new ArrayList<Integer>();
    			
    			// Initialize totals array with 0
    			for (int i = 0; i < values.size(); i++) {
    				valuesTotal.add(0);
    			}
    			
    			// For non-multiple selection
    			if (!surveyOpinionPoll.allowsMultipleSelection()) {
    				for (CustomerSurveyOpinionPoll crop : resultList) {
    					int index = Integer.parseInt(crop.getResponse());
    					int total = valuesTotal.get(index);
    					valuesTotal.set(index, total + 1);
    				}
    			}
    			// For multiple selection
    			else {
    				for (CustomerSurveyOpinionPoll crop : resultList) {
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
     * Put SurveyOpinionPoll into datastore.
     * Stores the given SurveyOpinionPoll instance in the datastore for this
     * survey.
     * @param surveyKey
     * 			: the key of the Survey where the surveyOpinionPoll will be added
     * @param surveyOpinionPoll
     * 			: the SurveyOpinionPoll instance to store
     */
	public static void putSurveyOpinionPoll(Key surveyKey, SurveyOpinionPoll surveyOpinionPoll) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Survey survey = pm.getObjectById(Survey.class, surveyKey);
			tx.begin();
			survey.addOpinionPoll(surveyOpinionPoll);
			tx.commit();
			log.info("SurveyOpinionPoll \"" + surveyOpinionPoll.getSurveyOpinionPollContent() + 
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
    * Delete SurveyOpinionPoll from datastore.
    * Deletes the SurveyOpinionPoll corresponding to the given key
    * from the datastore calling the PersistenceManager's 
    * deletePersistent() method.
    * @param key
    * 			: the key of the SurveyOpinionPoll instance to delete
    */
	public static void deleteSurveyOpinionPoll(Key key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			SurveyOpinionPoll opinionPoll = pm.getObjectById(SurveyOpinionPoll.class, key);
			String surveyOpinionPollTitle = opinionPoll.getSurveyOpinionPollContent();

			Survey survey = pm.getObjectById(Survey.class, key.getParent());
			tx.begin();
			survey.removeOpinionPoll(opinionPoll);
			tx.commit();
			
			log.info("SurveyOpinionPoll \"" + surveyOpinionPollTitle + 
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
    * Update SurveyOpinionPoll attributes.
    * Update's the given SurveyOpinionPoll's attributes in the datastore.
    * @param key
    * 			: the key of the SurveyOpinionPoll whose attributes will be updated
    * @param surveyOpinionPollTitle
    * 			: the title of the opinion poll
    * @param surveyOpinionPollContent
    * 			: the content of the opinion poll
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
	public static void updateSurveyOpinionPollAttributes(
			Key key, String surveyOpinionPollTitle, String surveyOpinionPollContent,
			String binaryChoice1, String binaryChoice2, Integer ratingLowValue, 
			Integer ratingHighValue, Boolean allowMultipleSelection) 
                       throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			SurveyOpinionPoll surveyOpinionPoll = pm.getObjectById(SurveyOpinionPoll.class, key);
			tx.begin();
			surveyOpinionPoll.setSurveyOpinionPollTitle(surveyOpinionPollTitle);
			surveyOpinionPoll.setSurveyOpinionPollContent(surveyOpinionPollContent);
			surveyOpinionPoll.setBinaryChoice1(binaryChoice1);
			surveyOpinionPoll.setBinaryChoice2(binaryChoice2);
			surveyOpinionPoll.setRatingLowValue(ratingLowValue);
			surveyOpinionPoll.setRatingHighValue(ratingHighValue);
			surveyOpinionPoll.setAllowMultipleChoiceSelection(allowMultipleSelection);
			tx.commit();
			log.info("SurveyOpinionPoll \"" + surveyOpinionPoll.getSurveyOpinionPollTitle() + 
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
    * Add SurveyOpinionPoll click.
    * Add a click to this survey opinion poll.
    * @param key
    * 			: the key of the SurveyOpinionPoll whose attributes will be updated
    */
	public static void addSurveyOpinionPollClick(Key key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			SurveyOpinionPoll surveyOpinionPoll = pm.getObjectById(SurveyOpinionPoll.class, key);
			tx.begin();
			surveyOpinionPoll.addClick();
			tx.commit();

			log.info("Added a click to the opinion poll \"" + surveyOpinionPoll.getSurveyOpinionPollTitle() + "\".");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
}
