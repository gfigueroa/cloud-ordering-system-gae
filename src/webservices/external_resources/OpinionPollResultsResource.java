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
import webservices.datastore_simple.OpinionPollResultsSimple;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import datastore.Customer;
import datastore.CustomerManager;
import datastore.CustomerRestaurantOpinionPoll;
import datastore.CustomerRestaurantOpinionPollManager;
import datastore.CustomerSurveyOpinionPoll;
import datastore.CustomerSurveyOpinionPollManager;
import datastore.RestaurantOpinionPoll;
import datastore.RestaurantOpinionPollManager;
import datastore.SurveyOpinionPoll;
import datastore.SurveyOpinionPollManager;

/**
 * This class represents the list of opinion poll results
 * as a Resource with only one representation
 */

public class OpinionPollResultsResource extends ServerResource {
	
	private static final Logger log = 
	        Logger.getLogger(OpinionPollResultsResource.class.getName());
	
	/**
	 * Returns the opinion poll results as a JSON object.
	 * @return The opinion poll results in JSON format
	 */
    @Get("json")
    public OpinionPollResultsSimple toJson() {
    	
    	String queryInfo = (String) getRequest().getAttributes()
                .get("queryinfo");
    	
    	int indexOfSecondParameter = queryInfo.indexOf('&');
    	String opinionPollKeyString = "";
    	String customerEmail = "";
    	
    	// If no customer email is given
    	if (indexOfSecondParameter == -1) {
    		// return null if no opinion poll key is given
    		if (Character.toLowerCase(queryInfo.charAt(0)) != 'o') {
    			return null;
    		}
    		
    		opinionPollKeyString = queryInfo.substring(2);
    		log.info("o=" + opinionPollKeyString);
    	}
    	else {
    		char searchByAttribute1 = queryInfo.charAt(0);
        	String searchByValue1 = queryInfo.substring(2, queryInfo.indexOf("&"));
        	char searchByAttribute2 = queryInfo.charAt(queryInfo.indexOf("&") + 1);
        	String searchByValue2 = queryInfo.substring(queryInfo.indexOf("&") + 3);
        	
        	log.info(searchByAttribute1 + "=" + searchByValue1 +
        			" --- " + searchByAttribute2 + "=" + searchByValue2);

        	if (Character.toLowerCase(searchByAttribute1) == 'o') {
        		opinionPollKeyString = searchByValue1;
        	}
        	else if (Character.toLowerCase(searchByAttribute1) == 'c') {
        		customerEmail = searchByValue1;
        	}
        	else {
        		return null;
        	}
        	
        	if (Character.toLowerCase(searchByAttribute2) == 'o') {
        		opinionPollKeyString = searchByValue2;
        	}
        	else if (Character.toLowerCase(searchByAttribute2) == 'c') {
        			customerEmail = searchByValue2;
        	}
        	else {
        		return null;
        	}
    	}
        
    	Key opinionPollKey = KeyFactory.stringToKey(opinionPollKeyString);
    	String response = "";
    	ArrayList<OpinionPollResultsSimple.FreeResponse> freeResponseResults =
    			new ArrayList<OpinionPollResultsSimple.FreeResponse>();
    	String lastResponse = "";
    	
    	// Check if it's a Restaurant Opinion Poll or a Survey Opinion Poll
    	if (opinionPollKey.getParent().getKind().equalsIgnoreCase("Restaurant")) {
	    	RestaurantOpinionPoll opinionPoll = RestaurantOpinionPollManager.getRestaurantOpinionPoll(
	    			opinionPollKey);
	    	
	    	if (!customerEmail.isEmpty()) {
	    		CustomerRestaurantOpinionPoll customerOpinionPoll = 
	    				CustomerRestaurantOpinionPollManager.getCustomerRestaurantOpinionPoll(
	    						opinionPoll.getKey(), customerEmail);
	    		if (customerOpinionPoll != null)
	    			response = "您是第 " + customerOpinionPoll.getResponseOrder() + " �?回應此新�?�。\n";
	    	}
	    	
	    	response += RestaurantOpinionPollManager.getRestaurantOpinionPollStatistics(opinionPoll);
	    	
    		List<CustomerRestaurantOpinionPoll> opinionPollResponses =
	    			CustomerRestaurantOpinionPollManager.getAllCustomerRestaurantOpinionPolls(
	    					opinionPollKey);
	    	
	    	// Check if it's a FREE_RESPONSE opinion poll
	    	if (opinionPoll.getRestaurantOpinionPollType() == RestaurantOpinionPoll.Type.FREE_RESPONSE) {
		    	for (CustomerRestaurantOpinionPoll opinionPollResponse : opinionPollResponses) {
		    		Customer respondingUser = CustomerManager.getCustomer(
		    				opinionPollResponse.getCustomer());
		    		
		    		OpinionPollResultsSimple.FreeResponse freeResponse =
		    				new OpinionPollResultsSimple.FreeResponse(
		    						respondingUser.getUser().getUserEmail().getEmail(),
		    						DateManager.printDateAsString(opinionPollResponse.getDate()),
		    						opinionPollResponse.getResponse()
		    						);
		    		
		    		freeResponseResults.add(freeResponse);
		    	}
	    	}
	    	
	    	// Get the last response given to this opinion poll
	    	if (!opinionPollResponses.isEmpty()) {
		    	lastResponse = 
		    			opinionPollResponses.get(0).getResponse();
	    	}
    	}
    	else {
    		SurveyOpinionPoll opinionPoll = SurveyOpinionPollManager.getSurveyOpinionPoll(
    				opinionPollKey);
	    	
	    	if (!customerEmail.isEmpty()) {
	    		CustomerSurveyOpinionPoll customerOpinionPoll = 
	    				CustomerSurveyOpinionPollManager.getCustomerSurveyOpinionPoll(
	    						opinionPoll.getKey(), customerEmail);
	    		if (customerOpinionPoll != null)
	    			response = "您是第 " + customerOpinionPoll.getResponseOrder() + " �?回應此新�?�。\n";
	    	}
	    	
	    	response += SurveyOpinionPollManager.getSurveyOpinionPollStatistics(opinionPoll);
	    	
    		List<CustomerSurveyOpinionPoll> opinionPollResponses =
	    			CustomerSurveyOpinionPollManager.getAllCustomerSurveyOpinionPolls(
	    					opinionPollKey);
    		
	    	// Get the last response given to this opinion poll
	    	if (!opinionPollResponses.isEmpty()) {
		    	lastResponse = 
		    			opinionPollResponses.get(0).getResponse();
	    	}
	    	
	    	// Check if it's a FREE_RESPONSE opinion poll
	    	if (opinionPoll.getSurveyOpinionPollType() == SurveyOpinionPoll.Type.FREE_RESPONSE) {
		    	for (CustomerSurveyOpinionPoll opinionPollResponse : opinionPollResponses) {
		    		Customer respondingUser = CustomerManager.getCustomer(
		    				opinionPollResponse.getCustomer());
		    		
		    		OpinionPollResultsSimple.FreeResponse freeResponse =
		    				new OpinionPollResultsSimple.FreeResponse(
		    						respondingUser.getUser().getUserEmail().getEmail(),
		    						DateManager.printDateAsString(opinionPollResponse.getDate()),
		    						opinionPollResponse.getResponse()
		    						);
		    		
		    		freeResponseResults.add(freeResponse);
		    	}
	    	}
    	}
	    
    	OpinionPollResultsSimple results = new OpinionPollResultsSimple(
    			opinionPollKeyString,
    			response,
    			freeResponseResults,
    			lastResponse
    			);
    	
    	return results;
    }

}