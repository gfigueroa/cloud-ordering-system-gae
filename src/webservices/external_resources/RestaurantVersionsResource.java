/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.external_resources;

import java.util.logging.Logger;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import webservices.datastore_simple.RestaurantVersions;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import datastore.Restaurant;
import datastore.RestaurantManager;

/**
 * This class represents the list of restaurants 
 * as a Resource with only one representation
 */

public class RestaurantVersionsResource extends ServerResource {

	private static final Logger log = 
	        Logger.getLogger(RestaurantVersionsResource.class.getName());
	
	/**
	 * Returns restaurant versions as a JSON object.
	 * @return An instance of RestaurantVersions in JSON format
	 */
    @Get("json")
    public RestaurantVersions toJson() {
        
        String queryInfo = (String) getRequest().getAttributes()
                .get("queryinfo");
            
        char searchBy = queryInfo.charAt(0);
        String searchKeyString = queryInfo.substring(2);
        
        log.info("Query: " + searchBy + "=" + searchKeyString);
        
        Key searchKey = KeyFactory.stringToKey(searchKeyString);
        
        Restaurant restaurant = RestaurantManager.getRestaurant(searchKey);
        RestaurantVersions restaurantVersions = new RestaurantVersions(searchKeyString,
        		restaurant.getMenuVersion(),
        		restaurant.getSetVersion() != null ? 
        				restaurant.getSetVersion() : 0,
        		restaurant.getMenuItemTypeVersion(),
        		restaurant.getAdditionalPropertyVersion() != null ? 
        				restaurant.getAdditionalPropertyVersion() : 0,
        		restaurant.getMessageVersion() != null ?
        				restaurant.getMessageVersion() : 0
        		);
        
        return restaurantVersions;
    }

}