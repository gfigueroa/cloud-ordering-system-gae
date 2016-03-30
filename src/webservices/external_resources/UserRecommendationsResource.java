/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.external_resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import util.DateManager;
import webservices.datastore_simple.MessageSimple;
import webservices.datastore_simple.UserRecommendationsSimple;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

import datastore.Customer;
import datastore.CustomerManager;
import datastore.Message;
import datastore.MessageManager;
import datastore.sheep.UserRecommendation;
import datastore.sheep.UserRecommendationManager;

/**
 * This class represents the list of UserRecommendations
 * as a Resource with only one representation
 */

public class UserRecommendationsResource extends ServerResource {

	private static final Logger log = 
	        Logger.getLogger(UserRecommendationsResource.class.getName());
	
	/**
	 * Returns the simple user recommendation list as a JSON object.
	 * @return An instance of UserRecommendationSimple in JSON format
	 */
    @Get("json")
    public UserRecommendationsSimple toJson() {
    	String queryInfo = (String) getRequest().getAttributes()
                .get("queryinfo");
        
    	int indexOfSecondParameter = queryInfo.indexOf('&');
    	int indexOfThirdParameter = queryInfo.lastIndexOf('&');
    	String customerEmail = "";
    	String kindString = "";
    	String dateString = "";
    	
    	// If one or less parameters were given
    	if (indexOfSecondParameter == -1) {
    		return null;
    	}
    	// If only two parameters were given
    	else if (indexOfSecondParameter == indexOfThirdParameter) {
    		
    		char searchByAttribute1 = queryInfo.charAt(0);
        	String searchByValue1 = queryInfo.substring(2, indexOfSecondParameter);
        	char searchByAttribute2 = queryInfo.charAt(indexOfSecondParameter + 1);
        	String searchByValue2 = queryInfo.substring(indexOfSecondParameter + 3);
        	
        	log.info(searchByAttribute1 + "=" + searchByValue1 +
        			" --- " + searchByAttribute2 + "=" + searchByValue2);

        	if (Character.toLowerCase(searchByAttribute1) == 'c') {
        		customerEmail = searchByValue1;
        	}
        	else if (Character.toLowerCase(searchByAttribute1) == 'k') {
        		kindString = searchByValue1;
        	}
        	else {
        		return null;
        	}
        	
        	if (Character.toLowerCase(searchByAttribute2) == 'c') {
        		customerEmail = searchByValue2;
        	}
        	else if (Character.toLowerCase(searchByAttribute2) == 'k') {
        			kindString = searchByValue2;
        	}
        	else {
        		return null;
        	}
    	}
    	else {
    		
    		char searchByAttribute1 = queryInfo.charAt(0);
        	String searchByValue1 = queryInfo.substring(2, indexOfSecondParameter);
        	char searchByAttribute2 = queryInfo.charAt(indexOfSecondParameter + 1);
        	String searchByValue2 = queryInfo.substring(indexOfSecondParameter + 3,
        			indexOfThirdParameter);
        	char searchByAttribute3 = queryInfo.charAt(indexOfThirdParameter + 1);
        	String searchByValue3 = queryInfo.substring(indexOfThirdParameter + 3);
        	
        	log.info(searchByAttribute1 + "=" + searchByValue1 +
        			" --- " + searchByAttribute2 + "=" + searchByValue2 +
        			" --- " + searchByAttribute3 + "=" + searchByValue3);

        	if (Character.toLowerCase(searchByAttribute1) == 'c') {
        		customerEmail = searchByValue1;
        	}
        	else if (Character.toLowerCase(searchByAttribute1) == 'k') {
        		kindString = searchByValue1;
        	}
        	else if (Character.toLowerCase(searchByAttribute1) == 'd') {
        		dateString = searchByValue1;
        	}
        	else {
        		return null;
        	}
        	
        	if (Character.toLowerCase(searchByAttribute2) == 'c') {
        		customerEmail = searchByValue2;
        	}
        	else if (Character.toLowerCase(searchByAttribute2) == 'k') {
        			kindString = searchByValue2;
        	}
        	else if (Character.toLowerCase(searchByAttribute2) == 'd') {
    			dateString = searchByValue2;
        	}
        	else {
        		return null;
        	}
        	
        	if (Character.toLowerCase(searchByAttribute3) == 'c') {
        		customerEmail = searchByValue3;
        	}
        	else if (Character.toLowerCase(searchByAttribute3) == 'k') {
        			kindString = searchByValue3;
        	}
        	else if (Character.toLowerCase(searchByAttribute3) == 'd') {
    			dateString = searchByValue3;
        	}
        	else {
        		return null;
        	}
    		
    	}
    	
    	Key customerKey = KeyFactory.createKey(
    			Customer.class.getSimpleName(), customerEmail);
    	// TODO: Date parameter not working yet
    	Date date = !dateString.isEmpty() ? 
    			DateManager.getSimpleDateValue(dateString) : null;

    	ArrayList<UserRecommendationsSimple.UserRecommendationSimple> 
    			sharedRecommendations = 
    			new ArrayList<UserRecommendationsSimple.UserRecommendationSimple>();
    	ArrayList<UserRecommendationsSimple.UserRecommendationSimple> 
				receivedRecommendations = 
				new ArrayList<UserRecommendationsSimple.UserRecommendationSimple>();
    			
    	if (kindString.equalsIgnoreCase("SHARED") ||
    			kindString.equalsIgnoreCase("BOTH")) {
    		
    		List<UserRecommendation> ownedRecommendations = 
    				UserRecommendationManager.getOwnedRecommendations(customerKey, date);
    		for (UserRecommendation userRecommendation : ownedRecommendations) {
    			
    			// For Message Recommendations
            	MessageSimple messageSimple = null;
            	if (userRecommendation.getUserRecommendationType() ==
            			UserRecommendation.UserRecommendationType.MESSAGE) {
            		
            		Message message = 
            				MessageManager.getMessage(
            						userRecommendation.getUserRecommendationItemKey());
            		
            		messageSimple = new MessageSimple(
                			KeyFactory.keyToString(message.getKey()),
                			message.getMessageType(),
                			message.getMessageTitle(),
                			message.getMessageAuthor(),
                			message.getMessageTextContent() != null ?
                					message.getMessageTextContent() : new Text(""),
                			message.getMessageMultimediaContent() != null ?
                					message.getMessageMultimediaContent() :
                						new BlobKey(""),
                			message.getMessageURL() != null ?
                					message.getMessageURL().getValue() : "",
                			DateManager.printDateAsString(message.getMessageEndingDate())
                			);
            	}
    			
    			UserRecommendationsSimple.UserRecommendationSimple userRecommendationSimple =
    					new UserRecommendationsSimple.UserRecommendationSimple(
    							KeyFactory.keyToString(userRecommendation.getKey()), 
    							customerEmail, 
    							userRecommendation.getUserRecommendationTypeString(), 
    							DateManager.printDateAsString(
    									userRecommendation.getUserRecommendationCreationDate()),
    							userRecommendation.getUserRecommendationComment(), 
    							KeyFactory.keyToString(
    									userRecommendation.getUserRecommendationItemKey()),
    							messageSimple
    							);
    			sharedRecommendations.add(userRecommendationSimple);
    		}
    	}
    	if (kindString.equalsIgnoreCase("RECEIVED") ||
    			kindString.equalsIgnoreCase("BOTH")) {
    		
    		List<UserRecommendation> followedRecommendations = 
    				UserRecommendationManager.getFollowedRecommendations(customerKey, date);
    		for (UserRecommendation userRecommendation : followedRecommendations) {
    			Customer owner = 
    					CustomerManager.getCustomer(userRecommendation.getKey().getParent());
    			
    			// For Message Recommendations
            	MessageSimple messageSimple = null;
            	if (userRecommendation.getUserRecommendationType() ==
            			UserRecommendation.UserRecommendationType.MESSAGE) {
            		
            		Message message = 
            				MessageManager.getMessage(
            						userRecommendation.getUserRecommendationItemKey());
            		
            		messageSimple = new MessageSimple(
                			KeyFactory.keyToString(message.getKey()),
                			message.getMessageType(),
                			message.getMessageTitle(),
                			message.getMessageAuthor(),
                			message.getMessageTextContent() != null ?
                					message.getMessageTextContent() : new Text(""),
                			message.getMessageMultimediaContent() != null ?
                					message.getMessageMultimediaContent() :
                						new BlobKey(""),
                			message.getMessageURL() != null ?
                					message.getMessageURL().getValue() : "",
                			DateManager.printDateAsString(message.getMessageEndingDate())
                			);
            	}
    			
    			UserRecommendationsSimple.UserRecommendationSimple userRecommendationSimple =
    					new UserRecommendationsSimple.UserRecommendationSimple(
    							KeyFactory.keyToString(userRecommendation.getKey()), 
    							owner.getUser().getUserEmail().getEmail(), 
    							userRecommendation.getUserRecommendationTypeString(), 
    							DateManager.printDateAsString(
    									userRecommendation.getUserRecommendationCreationDate()),
    							userRecommendation.getUserRecommendationComment(), 
    							KeyFactory.keyToString(
    									userRecommendation.getUserRecommendationItemKey()),
    							messageSimple
    							);
    			receivedRecommendations.add(userRecommendationSimple);
    		}
    	}
    	
    	UserRecommendationsSimple userRecommendationsSimple = 
    			new UserRecommendationsSimple(
    					customerEmail,
    					sharedRecommendations,
    					receivedRecommendations);
    	
    	return userRecommendationsSimple;
    }

}