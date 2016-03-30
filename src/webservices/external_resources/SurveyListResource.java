/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.external_resources;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import util.DateManager;
import webservices.datastore_simple.OpinionPollRatingEntrySimple;
import webservices.datastore_simple.SurveyOpinionPollSimple;
import webservices.datastore_simple.SurveySimple;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.KeyFactory;

import datastore.Customer;
import datastore.CustomerManager;
import datastore.Survey;
import datastore.SurveyManager;
import datastore.SurveyOpinionPoll;
import datastore.SurveyOpinionPollMultipleChoiceValue;
import datastore.SurveyOpinionPollRatingEntry;

/**
 * This class represents the list of surveys from a store
 * as a Resource with only one representation
 */

public class SurveyListResource extends ServerResource {
	
	private static final Logger log = 
	        Logger.getLogger(SurveyListResource.class.getName());
	
	/**
	 * Returns the survey list as a JSON object.
	 * @return An ArrayList of surveys in JSON format
	 */
    @Get("json")
    public ArrayList<SurveySimple> toJson() {
    	
    	String queryInfo = (String) getRequest().getAttributes().get("queryinfo");
    	
    	List<Survey> surveys;
    	
    	int indexOfSecondParameter = queryInfo.indexOf('&');
    	String customerEmail = "";
    	
    	// If no store key is given then search global objects
    	if (indexOfSecondParameter == -1) {
    		// return null if no customer email is given
    		if (Character.toLowerCase(queryInfo.charAt(0)) != 'c') {
    			return null;
    		}
    		
    		customerEmail = queryInfo.substring(2);
    		log.info("c=" + customerEmail);
    		surveys = SurveyManager.getGlobalActiveSurveys();
    	}
    	else {
    		char searchByAttribute1 = queryInfo.charAt(0);
        	String searchByValue1 = queryInfo.substring(2, queryInfo.indexOf("&"));
        	char searchByAttribute2 = queryInfo.charAt(queryInfo.indexOf("&") + 1);
        	String searchByValue2 = queryInfo.substring(queryInfo.indexOf("&") + 3);
        	
        	log.info(searchByAttribute1 + "=" + searchByValue1 +
        			" --- " + searchByAttribute2 + "=" + searchByValue2);
        	
        	String storeKeyString = "";
        	if (Character.toLowerCase(searchByAttribute1) == 's') {
        		storeKeyString = searchByValue1;
        	}
        	else if (Character.toLowerCase(searchByAttribute1) == 'c') {
        		customerEmail = searchByValue1;
        	}
        	else {
        		return null;
        	}
        	
        	if (Character.toLowerCase(searchByAttribute2) == 's') {
        		storeKeyString = searchByValue2;
        	}
        	else if (Character.toLowerCase(searchByAttribute2) == 'c') {
        			customerEmail = searchByValue2;
        	}
        	else {
        		return null;
        	}
        	
        	surveys = SurveyManager.getNonGlobalActiveSurveysFromStore(KeyFactory.stringToKey(storeKeyString));
    	}
    	
    	ArrayList<SurveySimple> simpleSurveys = new ArrayList<SurveySimple>();
		Customer customer = CustomerManager.getCustomer(new Email(customerEmail));
		for (Survey survey : surveys) {
        	Boolean userAlreadyResponded = SurveyManager.customerAlreadyClickedSurvey(customer.getKey(), survey.getKey());
  	
        	// Get opinion polls
        	ArrayList<SurveyOpinionPollSimple> opinionPolls = new ArrayList<SurveyOpinionPollSimple>();
        	for (SurveyOpinionPoll opinionPoll : survey.getOpinionPolls()) {
        		
        		// Get opinion poll rating entries
        		ArrayList<OpinionPollRatingEntrySimple> ratingEntries = new ArrayList<OpinionPollRatingEntrySimple>();
        		for (SurveyOpinionPollRatingEntry entry : opinionPoll.getRatingEntries()) {
        			ratingEntries.add(new OpinionPollRatingEntrySimple(
        					KeyFactory.keyToString(entry.getKey()),
        					entry.getRatingEntry(),
        					entry.getRatingEntryIndex()
        					));
        		}
        		
        		// Get opinion poll multiple choice values
            	ArrayList<String> multipleChoiceValues = new ArrayList<String>();
            	for (SurveyOpinionPollMultipleChoiceValue value : opinionPoll.getMultipleChoiceValues()) {
            		multipleChoiceValues.add(value.getMultipleChoiceValue());
            	}
        		
        		opinionPolls.add(new SurveyOpinionPollSimple(KeyFactory.keyToString(opinionPoll.getKey()),
        				opinionPoll.getSurveyOpinionPollType(),
        				opinionPoll.getSurveyOpinionPollTitle(),
        				opinionPoll.getSurveyOpinionPollContent(),
        				opinionPoll.getBinaryChoice1() != null ? opinionPoll.getBinaryChoice1() : "",
        				opinionPoll.getBinaryChoice2() != null ? opinionPoll.getBinaryChoice2() : "",
        				opinionPoll.getRatingLowValue() != null ? opinionPoll.getRatingLowValue() : 0,
        				opinionPoll.getRatingHighValue() != null ? opinionPoll.getRatingHighValue() : 0,
        				ratingEntries,
        				opinionPoll.allowsMultipleSelection() != null ? opinionPoll.allowsMultipleSelection() : false,
        				multipleChoiceValues
        				));	
        	}
        	
    		simpleSurveys.add(new SurveySimple(KeyFactory.keyToString(survey.getKey()),
    				KeyFactory.keyToString(survey.getKey().getParent()),
    				survey.getSurveyType(),
    				survey.getSurveyTitle(),
    				survey.getSurveyDescription(),
    				survey.getValidationCode(),
    				survey.getCurrentClicks(),
    				userAlreadyResponded,
    				DateManager.printDateAsString(survey.getSurveyEndingDate()),
    				survey.getSurveyPriority(),
    				survey.resultsArePublic(),
    				opinionPolls
    				));
    	}
    	
    	return simpleSurveys;
    }

}