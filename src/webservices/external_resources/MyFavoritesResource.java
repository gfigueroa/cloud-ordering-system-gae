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
import webservices.datastore_simple.MessageSimple;
import webservices.datastore_simple.MyFavoritesSimple;
import webservices.datastore_simple.UserRecommendationsSimple;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

import datastore.Customer;
import datastore.Message;
import datastore.MessageManager;
import datastore.sheep.UserRecommendation;
import datastore.sheep.UserRecommendationManager;

/**
 * This class represents the list of myFavorites
 * as a Resource with only one representation
 */

public class MyFavoritesResource extends ServerResource {
	
	private static final Logger log = 
	        Logger.getLogger(MyFavoritesResource.class.getName());
	
	/**
	 * Returns the myFavorites list as a JSON object
	 * @return An instance of MyFavorites in JSON format
	 */
    @Get("json")
    public MyFavoritesSimple toJson() {
    	
    	String queryInfo = (String) getRequest().getAttributes()
                .get("queryinfo");

	    char searchBy = queryInfo.charAt(0);
	    String searchString = queryInfo.substring(2);
	        
	    String customerEmail = searchString;
	    Key customerKey = KeyFactory.createKey(Customer.class.getSimpleName(), 
	    		customerEmail);
	    log.info("Query: " + searchBy + "=" + searchString);

        List<UserRecommendation> myFavorites = 
        		UserRecommendationManager.getMyFavorites(customerKey);
        
        ArrayList<UserRecommendationsSimple.UserRecommendationSimple>
        		userRecommendationsSimple = 
        		new ArrayList<UserRecommendationsSimple.UserRecommendationSimple>();
        for (UserRecommendation userRecommendation : myFavorites) {
        	
        	// For Message Recommendations
        	MessageSimple messageSimple = null;
        	if (userRecommendation.getUserRecommendationType() ==
        			UserRecommendation.UserRecommendationType.MESSAGE) {
        		
        		Message message = MessageManager.getMessage(
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
							KeyFactory.keyToString(userRecommendation.getUserRecommendationItemKey()),
							messageSimple
							);
			userRecommendationsSimple.add(userRecommendationSimple);
		}
        
        MyFavoritesSimple myFavoritesSimple = new MyFavoritesSimple(
        		customerEmail,
        		userRecommendationsSimple
        		);
        
        return myFavoritesSimple;
    }

}