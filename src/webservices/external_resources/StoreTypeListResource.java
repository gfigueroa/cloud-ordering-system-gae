/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.external_resources;

import java.util.ArrayList;
import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import webservices.datastore_simple.StoreTypeSimple;

import datastore.RestaurantType;
import datastore.RestaurantTypeManager;

/**
 * This class represents the list of store types 
 * as a Resource with only one representation
 */

public class StoreTypeListResource extends ServerResource {

	/**
	 * Returns the store type list as a JSON object.
	 * @return An ArrayList of StoreType in JSON format
	 */
    @Get("json")
    public ArrayList<StoreTypeSimple> toJson() {
        
    	String queryInfo = (String) getRequest().getAttributes()
                .get("queryinfo");
        
    	List<RestaurantType> storeTypes; 
	    if (queryInfo != null && !queryInfo.isEmpty()) {    
	        char searchBy = queryInfo.charAt(0);
	    	
	    	if (searchBy == 't') {
		        String storeSuperTypeString = queryInfo.substring(2);
		        RestaurantType.StoreSuperType storeSuperType = 
		        		RestaurantType.getStoreSuperTypeFromString(storeSuperTypeString);
		        storeTypes = RestaurantTypeManager.getAllRestaurantTypes(storeSuperType);
	        }
	    	else {
	    		return null;
	    	}
    	}
        else {
        	storeTypes = RestaurantTypeManager.getAllRestaurantTypes();
        }
    	
    	// We will be returning a list of elements
    	ArrayList<StoreTypeSimple> storeTypesSimple =
        		new ArrayList<StoreTypeSimple>();
    	for (RestaurantType restaurantType : storeTypes) {
    		StoreTypeSimple storeTypeSimple 
    				= new StoreTypeSimple(restaurantType.getKey().toString(),
    						restaurantType.getStoreSuperType(),
    						restaurantType.getRestaurantTypeName(),
    						restaurantType.getRestaurantTypeDescription() != null ? 
    								restaurantType.getRestaurantTypeDescription() : "",
    						restaurantType.getStoreTypeVersion()
    						);
    		storeTypesSimple.add(storeTypeSimple);
    	}

        return storeTypesSimple;
    }

}