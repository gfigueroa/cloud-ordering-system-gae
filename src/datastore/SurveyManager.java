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

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import exceptions.InexistentObjectException;
import exceptions.MissingRequiredFieldsException;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the Survey class.
 * 
 */

public class SurveyManager {
	
	private static final Logger log = 
        Logger.getLogger(SurveyManager.class.getName());
	
	/**
     * Get a Survey instance from the datastore given the Survey key.
     * @param key
     * 			: the Survey's key
     * @return Survey instance, null if Survey is not found
     */
	public static Survey getSurvey(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Survey survey;
		try  {
			survey = pm.getObjectById(Survey.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return survey;
	}
	
	/**
     * Get ALL the surveys in the datastore from a specific store
     * and returns them in a List structure
     * @param storeKey: 
     * 				the key of the store whose surveys will be retrieved
     * @return all surveys in the datastore belonging to the given store
     * TODO: Fix "touching" of surveys
     */
	public static List<Survey> getAllSurveysFromStore(Key storeKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant store = pm.getObjectById(Restaurant.class, storeKey);
		
        List<Survey> result = null;
        try {
            result = store.getSurveys();
            // Touch each survey
            for (Survey survey : result) {
            	survey.getKey();
            }
        }
        finally {
        	pm.close();
        }
        
        return result;
    }
	
	/**
     * Get all global active surveys in the datastore and returns 
     * them in a List structure
     * @return all surveys that are "ACTIVE" and "GLOBAL"
     */
	@SuppressWarnings("unchecked")
	public static List<Survey> getGlobalActiveSurveys() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Survey.class);
		query.setFilter("surveyEndingDate >= now");
		query.declareParameters(Date.class.getName() + " now");

        ArrayList<Survey> finalResult = new ArrayList<Survey>();
        try {
        	Date now = new Date();
        	List<Survey> result = (List<Survey>) query.execute(now);
        	
            for (Survey survey : result) {
            	if (survey.getCurrentStatus() == Survey.Status.ACTIVE &&
            			survey.getSurveyType() == Survey.Type.GLOBAL) {
            		finalResult.add(survey);
            	}
            }
        }
        finally {
        	pm.close();
        }

        return finalResult;
    }
	
	/**
     * Get non-global active surveys in the datastore from a specific store
     * and returns them in a List structure
     * @param storeKey: 
     * 				the key of the store whose surveys will be retrieved
     * @return all surveys that are "ACTIVE" and either "PRIVATE" or 
     * 			"INVITATION" belonging to the given store
     */
	public static List<Survey> getNonGlobalActiveSurveysFromStore(Key storeKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant store = pm.getObjectById(Restaurant.class, storeKey);
		
        List<Survey> result = null;
        ArrayList<Survey> finalResult = new ArrayList<Survey>();
        try {
            result = store.getSurveys();
            for (Survey survey : result) {
            	if (survey.getCurrentStatus() == Survey.Status.ACTIVE &&
            			survey.getSurveyType() != Survey.Type.GLOBAL) {
            		finalResult.add(survey);
            	}
            }
        }
        finally {
        	pm.close();
        }

        return finalResult;
    }
	
	/**
     * Get active surveys in the datastore from a specific store
     * and returns them in a List structure
     * @param storeKey: 
     * 				the key of the store whose surveys will be retrieved
     * @return all surveys that are "ACTIVE" belonging to the given store
     */
	public static List<Survey> getActiveSurveysFromStore(Key storeKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant store = pm.getObjectById(Restaurant.class, storeKey);
		
        List<Survey> result = null;
        ArrayList<Survey> finalResult = new ArrayList<Survey>();
        try {
            result = store.getSurveys();
            for (Survey survey : result) {
            	if (survey.getCurrentStatus() == Survey.Status.ACTIVE) {
            		finalResult.add(survey);
            	}
            }
        }
        finally {
        	pm.close();
        }

        return finalResult;
    }
	
	/**
     * Get inactive surveys in the datastore from a specific store
     * and returns them in a List structure
     * @param storeKey: 
     * 				the key of the store whose surveys will be retrieved
     * @return all surveys that are "INACTIVE" belonging to the given store
     */
	public static List<Survey> getInactiveSurveysFromStore(Key storeKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant store = pm.getObjectById(Restaurant.class, storeKey);
		
        List<Survey> result = null;
        ArrayList<Survey> finalResult = new ArrayList<Survey>();
        try {
            result = store.getSurveys();
            for (Survey survey : result) {
            	if (survey.getCurrentStatus() == Survey.Status.INACTIVE) {
            		finalResult.add(survey);
            	}
            }
        }
        finally {
        	pm.close();
        }

        return finalResult;
    }
	
	/**
     * Get expired surveys in the datastore from a specific store
     * and returns them in a List structure
     * @param storeKey: 
     * 				the key of the store whose surveys will be retrieved
     * @return all surveys that are "EXPIRED" or "EXPIRED_MAXED" belonging to the given store
     */
	public static List<Survey> getExpiredSurveysFromStore(Key storeKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant store = pm.getObjectById(Restaurant.class, storeKey);
		
        List<Survey> result = null;
        ArrayList<Survey> finalResult = new ArrayList<Survey>();
        try {
            result = store.getSurveys();
            for (Survey survey : result) {
            	if (survey.getCurrentStatus() == Survey.Status.EXPIRED) {
            		finalResult.add(survey);
            	}
            }
        }
        finally {
        	pm.close();
        }

        return finalResult;
    }
	
	/**
     * Check if a customer has already clicked on a specific Survey
     * @param customerKey:
     * 				the key of the customer who will be checked
     * @param surveyKey:
     * 				the key of the survey that will be checked
     * @return True if customer has already clicked on this survey, False otherwise
     */
    @SuppressWarnings("unchecked")
	public static boolean customerAlreadyClickedSurvey(Key customerKey, Key surveyKey) {
    	PersistenceManager pm = PMF.get().getPersistenceManager();
    	
        Query query = pm.newQuery(CustomerSurvey.class);
    	query.setFilter("customer == ckey && survey == skey");
        query.declareParameters(Key.class.getName() + " ckey, " + Key.class.getName() + " skey");
        
        log.info("Query: " + query.toString());
        
        try {
        	List<CustomerSurvey> result = 
        			(List<CustomerSurvey>) query.execute(customerKey, surveyKey);
        	
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
     * Put Survey into datastore.
     * Stores the given Survey instance in the datastore for this
     * store.
     * @param email
     * 			: the email of the Store where the survey will be added
     * @param survey
     * 			: the Survey instance to store
     */
	public static void putSurvey(Email email, Survey survey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Restaurant.class.getSimpleName(), email.getEmail());
			Restaurant store = pm.getObjectById(Restaurant.class, key);
			tx.begin();
			store.addSurvey(survey);
			tx.commit();
			log.info("Survey \"" + survey.getSurveyTitle() + 
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
    * Delete Survey from datastore.
    * Deletes the Survey corresponding to the given key
    * from the datastore calling the PersistenceManager's 
    * deletePersistent() method.
    * @param key
    * 			: the key of the Survey instance to delete
    */
	public static void deleteSurvey(Key key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Restaurant store = pm.getObjectById(Restaurant.class, key.getParent());
			Survey survey = pm.getObjectById(Survey.class, key);
			String surveyTitle = survey.getSurveyTitle();
			tx.begin();
			store.removeSurvey(survey);
			tx.commit();
			log.info("Survey \"" + surveyTitle + 
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
    * Update Survey attributes.
    * Update's the given Survey's attributes in the datastore.
    * @param key
    * 			: the key of the Survey whose attributes will be updated
    * @param surveyTitle
    * 			: the title of the survey
    * @param surveyDescription
    * 			: the description of the survey
    * @param validationCode
    * 			: the validation code of the survey
    * @param storeNewStartingDate
    * 			: the starting date of this survey
    * @param surveyEndingDate
    * 			: the ending date of this survey
    * @param surveyPriority
    * 			: the priority for this survey represented as a number
    * @param publicResults
    * 			: whether this survey's results are public or not
	* @throws MissingRequiredFieldsException 
    */
	public static void updateSurveyAttributes(
			Key key, String surveyTitle, String surveyDescription,
			String validationCode, Date surveyStartingDate, Date surveyEndingDate, 
			Integer surveyPriority, Boolean publicResults) 
                       throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Survey survey = pm.getObjectById(Survey.class, key);
			tx.begin();
			survey.setSurveyTitle(surveyTitle);
			survey.setSurveyDescription(surveyDescription);
			survey.setValidationCode(validationCode);
			survey.setSurveyStartingDate(surveyStartingDate);
			survey.setSurveyEndingDate(surveyEndingDate);
			survey.setSurveyPriority(surveyPriority);
			survey.setPublicResults(publicResults);
			tx.commit();
			log.info("Survey \"" + survey.getSurveyTitle() + 
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
    * Add Survey click.
    * Add a click to this survey.
    * @param key
    * 			: the key of the Survey whose attributes will be updated
    * @return True if this survey is still ACTIVE, False otherwise
    */
	public static boolean addSurveyClick(Key key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Survey survey = pm.getObjectById(Survey.class, key);
			boolean clickSuccessful = false;
			tx.begin();
			clickSuccessful = survey.addClick();
			tx.commit();
			
			if (clickSuccessful) {
				log.info("Added a click to the survey \"" + survey.getSurveyTitle() + "\".");
			}
			else {
				log.info("Adding a click to the survey \"" + survey.getSurveyTitle() + "\" failed.");
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
	
    /**
     * Get the current Survey statistics in a formatted string.
     * @param survey
     * 			: the survey 
     * @return a string representation of the current statistics for this survey
     */
	public static String getSurveyStatistics(Survey survey){

		String statistics = "";
		List<SurveyOpinionPoll> surveyOpinionPollList = survey.getOpinionPolls();
		
		for (SurveyOpinionPoll surveyOpinionPoll : surveyOpinionPollList) {
			statistics+=" - ";
			statistics+=surveyOpinionPoll.getSurveyOpinionPollTitle();
			statistics+=": \n";
			statistics+=SurveyOpinionPollManager.getSurveyOpinionPollStatistics(surveyOpinionPoll);
			statistics+="\n\n";
			
		}
	
		
		return statistics;
	}
	
}
