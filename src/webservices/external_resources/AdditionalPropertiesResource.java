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

import webservices.datastore_simple.AdditionalPropertyTypeSimple;
import webservices.datastore_simple.AdditionalPropertyValueSimple;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import datastore.AdditionalPropertyType;
import datastore.AdditionalPropertyTypeManager;
import datastore.AdditionalPropertyValue;
import datastore.AdditionalPropertyValueManager;

/**
 * This class represents the list of menu items 
 * as a Resource with only one representation
 */

public class AdditionalPropertiesResource extends ServerResource {
	
	private static final Logger log = 
	        Logger.getLogger(AdditionalPropertiesResource.class.getName());

	/**
	 * Returns the queried menu items as a JSON object.
	 * @return An ArrayList of AdditionalPropertySimple in JSON format
	 */
    @Get("json")
    public ArrayList<AdditionalPropertyTypeSimple> toJson() {
        // we will be returning a list of elements
        List<AdditionalPropertyType> additionalProperties = null;
        
        String queryInfo = (String) getRequest().getAttributes()
            .get("queryinfo");
        log.info("queryinfo: " + queryInfo);

        String searchKey = queryInfo.substring(2);
        Key key;
        key = KeyFactory.stringToKey(searchKey);

        additionalProperties = AdditionalPropertyTypeManager.getRestaurantAdditionalPropertyTypes(key);
        
        ArrayList<AdditionalPropertyTypeSimple> simpleAdditionalPropertyTypes = 
        		new ArrayList<AdditionalPropertyTypeSimple>();
        for (AdditionalPropertyType additionalProperty : additionalProperties) {
        	
        	// Get additional property values
        	List<AdditionalPropertyValue> additionalPropertyValues =
        			AdditionalPropertyValueManager.getAdditionalPropertyValues(
        					additionalProperty.getKey());
        	
        	ArrayList<AdditionalPropertyValueSimple> additionalPropertyValuesSimple =
        			new ArrayList<AdditionalPropertyValueSimple>();
        	for (AdditionalPropertyValue value : additionalPropertyValues) {
        		AdditionalPropertyValueSimple valueSimple = 
        				new AdditionalPropertyValueSimple(
        						KeyFactory.keyToString(value.getKey()),
        						value.getAdditionalPropertyValueName()
        						);
        		additionalPropertyValuesSimple.add(valueSimple);
        	}
        	
        	AdditionalPropertyTypeSimple additionalPropertyTypeSimple = 
        			new AdditionalPropertyTypeSimple(
        					KeyFactory.keyToString(additionalProperty.getKey()),
        					additionalProperty.getAdditionalPropertyTypeName(),
        					additionalPropertyValuesSimple
        					);
        	simpleAdditionalPropertyTypes.add(additionalPropertyTypeSimple);
        }
        
        return simpleAdditionalPropertyTypes;
    }
}