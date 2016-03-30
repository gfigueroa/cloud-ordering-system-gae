/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.external_resources;

import java.util.ArrayList;
import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.appengine.api.datastore.KeyFactory;

import webservices.datastore_simple.RestaurantVersions;
import webservices.datastore_simple.SystemSimple;

import datastore.Restaurant;
import datastore.RestaurantManager;
import datastore.RestaurantType;
import datastore.System;
import datastore.SystemManager;

/**
 * This class represents the System
 * JDO Object which contains some version numbers
 * required by the mobile app.
 */

public class SystemResource extends ServerResource {

	/**
	 * Returns the System table instance as a JSON object.
	 * @return The instance of the System object in JSON format
	 */
    @Get("json")
    public SystemSimple toJson() {
        
    	// Get Store Versions
    	List<Restaurant> restaurants = RestaurantManager.getAllRestaurants();
    	ArrayList<RestaurantVersions> storeVersions = new ArrayList<RestaurantVersions>();
    	for (Restaurant restaurant : restaurants) {
            RestaurantVersions restaurantVersions = new RestaurantVersions(
            		KeyFactory.keyToString(restaurant.getKey()),
            		restaurant.getMenuVersion(),
            		restaurant.getSetVersion() != null ? 
            				restaurant.getSetVersion() : 0,
            		restaurant.getMenuItemTypeVersion(),
            		restaurant.getAdditionalPropertyVersion() != null ? 
            				restaurant.getAdditionalPropertyVersion() : 0,
            		restaurant.getMessageVersion() != null ?
            				restaurant.getMessageVersion() : 0
            		);
            
            storeVersions.add(restaurantVersions);
    	}
    	
        System system = SystemManager.getSystem();
        SystemSimple systemSimple = new SystemSimple(
        		system.getKey(),
        		system.getRestaurantListVersion(),
        		system.getRestaurantTypeListVersion(),
        		system.getStoreListVersion(RestaurantType.StoreSuperType.FOOD_DRINK),
        		system.getStoreListVersion(RestaurantType.StoreSuperType.SHOPPING),
        		system.getStoreListVersion(RestaurantType.StoreSuperType.POLLS),
        		system.getStoreListVersion(RestaurantType.StoreSuperType.SALON),
        		system.getStoreListVersion(RestaurantType.StoreSuperType.GOD_DWELLING_PLACE),
        		system.getStoreListVersion(RestaurantType.StoreSuperType.VIRTUAL_CHANNEL),
        		system.getOldestAppVersionSupportedString() != null ? 
        				system.getOldestAppVersionSupportedString() : "",
        		storeVersions
        		);
        return systemSimple;
    }

}
