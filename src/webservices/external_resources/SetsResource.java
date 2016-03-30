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

import webservices.datastore_simple.SetSimple;
import webservices.datastore_simple.TypeSetMenuItemSimple;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import datastore.Set;
import datastore.SetManager;
import datastore.TypeSetMenuItem;

/**
 * This class represents the list of sets 
 * as a Resource with only one representation
 */

public class SetsResource extends ServerResource {
	
	private static final Logger log = 
	        Logger.getLogger(SetsResource.class.getName());

	/**
	 * Returns the queried sets as a JSON object.
	 * @return An ArrayList of SetSimple in JSON format
	 */
    @Get("json")
    public ArrayList<SetSimple> toJson() {
        // we will be returning a list of elements
        List<Set> sets = null;

        // we have to check the queryInfo to see if the query was
        // based on the Restaurant or Set Type
        // the format of the queryinfo is:
        // [s|t]=key
        String queryInfo = (String) getRequest().getAttributes()
            .get("queryinfo");
        log.info("queryinfo: " + queryInfo);
        
        char searchBy = queryInfo.charAt(0);
        String searchKey = queryInfo.substring(2);

        switch (searchBy) {
            case 's': 
            	Key storeKey = KeyFactory.stringToKey(searchKey);
                sets = SetManager.getAllSetsByStore(storeKey);
                break;
            case 't':
            	Set.SetType setType = Set.getSetTypeFromString(searchKey);
                sets = SetManager.getAllSetsByType(setType);
                break;
            default:
                return null;
        }
        
        ArrayList<SetSimple> simpleSets = new ArrayList<SetSimple>();
        for (Set set : sets) {
        	
        	// Menu Item Keys
        	ArrayList<String> menuItemKeys = new ArrayList<String>();
        	for (Key menuItemKey : set.getMenuItems()) {
        		menuItemKeys.add(KeyFactory.keyToString(menuItemKey));
        	}
        	
        	// Type Set Menu Items
        	ArrayList<TypeSetMenuItemSimple> typeSetMenuItemsSimple = 
        			new ArrayList<TypeSetMenuItemSimple>();
        	for (TypeSetMenuItem item : set.getTypeSetMenuItems()) {
        		TypeSetMenuItemSimple itemSimple = new TypeSetMenuItemSimple(
        				KeyFactory.keyToString(item.getKey()),
        				KeyFactory.keyToString(item.getMenuItem()),
        				item.getTypeSetMenuItemPrice()
        				);
        		typeSetMenuItemsSimple.add(itemSimple);
        	}
        	
        	SetSimple setSimple = new SetSimple(
        			KeyFactory.keyToString(set.getKey()),
        			set.getSetNumber() != null ? set.getSetNumber() : 0,
        			set.getSetType(),
        			set.getSetName(),
        			set.getSetDescription() != null ? set.getSetDescription() : "",
        			set.getSetPrice() != null ? set.getSetPrice() : 0,
        			set.hasFixedPrice(),
        			set.getSetDiscount() != null ? set.getSetDiscount() : 0,
        			set.getSetImage() != null ? set.getSetImage() : new BlobKey(""),
        			set.getSetServingTime() != null ? set.getSetServingTime() : 0,
        			set.isAvailable(),
        			set.getSetServiceTime(),
        			menuItemKeys,
        			typeSetMenuItemsSimple
        			);
        	simpleSets.add(setSimple);
        }
        
        return simpleSets;
    }

}
