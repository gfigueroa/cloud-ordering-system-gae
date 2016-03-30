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
import webservices.datastore_simple.RestaurantOpinionPollSimple;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import datastore.Customer;
import datastore.OpinionPollRatingEntry;
import datastore.RestaurantOpinionPoll;
import datastore.RestaurantOpinionPollManager;
import datastore.RestaurantOpinionPollMultipleChoiceValue;

/**
 * This class represents the list of restaurant opinion poll
 * as a Resource with only one representation
 */

public class RestaurantOpinionPollListResource extends ServerResource {
	
	private static final Logger log = 
	        Logger.getLogger(RestaurantOpinionPollListResource.class.getName());
	
	/**
	 * Returns the restaurant opinion poll list as a JSON object.
	 * @return An ArrayList of restaurant opinion poll in JSON format
	 */
    @Get("json")
    public ArrayList<RestaurantOpinionPollSimple> toJson() {
    	
    	String queryInfo = (String) getRequest().getAttributes()
                .get("queryinfo");
            
    	Key customerKey = null;
    	if (!queryInfo.equalsIgnoreCase("none")) {     
	        char searchBy = queryInfo.charAt(0);
	        String searchEmailString = queryInfo.substring(2);
	        
	        customerKey = KeyFactory.createKey(Customer.class.getSimpleName(), searchEmailString);
	        
	        log.info("Query: " + searchBy + "=" + searchEmailString);
    	}
    	
        List<RestaurantOpinionPoll> restaurantOpinionPollList = 
        		RestaurantOpinionPollManager.getAllActiveRestaurantOpinionPolls();
        ArrayList<RestaurantOpinionPollSimple> restaurantOpinionPollListSimple = 
        		new ArrayList<RestaurantOpinionPollSimple>();
        for (RestaurantOpinionPoll restaurantOpinionPoll : restaurantOpinionPollList) {
        	
        	boolean customerAlreadyClicked = false;
        	if (!queryInfo.equalsIgnoreCase("none")) {
        		customerAlreadyClicked = 
        				RestaurantOpinionPollManager.customerAlreadyClickedOpinionPoll(
        						customerKey, restaurantOpinionPoll.getKey());
        	}
        	log.info("Customer already clicked news: " + customerAlreadyClicked);
        	
        	// TODO: Change this later
        	// if (!customerAlreadyClicked) {
        	
        		// Get rating entries
        		ArrayList<OpinionPollRatingEntrySimple> ratingEntries = 
        				new ArrayList<OpinionPollRatingEntrySimple>();
        		for (OpinionPollRatingEntry entry : 
        				restaurantOpinionPoll.getRatingEntries()) {
        			ratingEntries.add(new OpinionPollRatingEntrySimple(
        					KeyFactory.keyToString(entry.getKey()),
        					entry.getRatingEntry(),
        					entry.getRatingEntryIndex()
        					));
        		}
        	
        		// Get multiple choice values
        		ArrayList<String> multipleChoiceValues = new ArrayList<String>();
        		for (RestaurantOpinionPollMultipleChoiceValue value 
        				: restaurantOpinionPoll.getMultipleChoiceValues()) {
        			multipleChoiceValues.add(value.getMultipleChoiceValue());
        		}
        		
	        	RestaurantOpinionPollSimple restaurantOpinionPollSimple = 
	        			new RestaurantOpinionPollSimple(
	        					KeyFactory.keyToString(restaurantOpinionPoll.getKey()),
			        			KeyFactory.keyToString(restaurantOpinionPoll.getKey().getParent()),
			        			restaurantOpinionPoll.getRestaurantOpinionPollType(),
			        			restaurantOpinionPoll.getRestaurantOpinionPollTitle(),
			        			restaurantOpinionPoll.getRestaurantOpinionPollContent(),
			        			restaurantOpinionPoll.getCurrentClicks(),
			        			DateManager.printDateAsString(
			        					restaurantOpinionPoll.getRestaurantOpinionPollEndingDate()),
			        			restaurantOpinionPoll.getRestaurantOpinionPollPriority(),
			        			restaurantOpinionPoll.resultsArePublic() != null ? 
			        					restaurantOpinionPoll.resultsArePublic() : true,
			        			restaurantOpinionPoll.getRestaurantOpinionPollImage() != null ?
			        					restaurantOpinionPoll.getRestaurantOpinionPollImage() :
			        						new BlobKey(""),
			        			restaurantOpinionPoll.getBinaryChoice1() != null ? 
			        					restaurantOpinionPoll.getBinaryChoice1() : "",
			        			restaurantOpinionPoll.getBinaryChoice2() != null ? 
			        					restaurantOpinionPoll.getBinaryChoice2() : "",
			        			restaurantOpinionPoll.getRatingLowValue() != null ? 
			        					restaurantOpinionPoll.getRatingLowValue() : 0,
			        			restaurantOpinionPoll.getRatingHighValue() != null ? 
			        					restaurantOpinionPoll.getRatingHighValue() : 0,
			        			ratingEntries,
			        			restaurantOpinionPoll.allowsMultipleSelection() != null ? 
			        					restaurantOpinionPoll.allowsMultipleSelection() : false,
			        			multipleChoiceValues
	        			);
	        	
	        	restaurantOpinionPollListSimple.add(restaurantOpinionPollSimple);
        	}
        //}
        
        return restaurantOpinionPollListSimple;
    }

}