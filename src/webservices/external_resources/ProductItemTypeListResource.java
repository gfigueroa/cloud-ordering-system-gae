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

import webservices.datastore_simple.ProductItemTypeSimple;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import datastore.MenuItemType;
import datastore.MenuItemTypeManager;

/**
 * This class represents the list of menu item types
 * as a Resource with only one representation
 */

public class ProductItemTypeListResource extends ServerResource {
	
	private static final Logger log = 
	        Logger.getLogger(ProductItemTypeListResource.class.getName());

	/**
	 * Returns the menu item type list as a JSON object.
	 * @return An ArrayList of MenuItemType in JSON format
	 */
    @Get("json")
    public ArrayList<ProductItemTypeSimple> toJson() {
    	
    	String queryInfo = (String) getRequest().getAttributes()
                .get("queryinfo");
            
        char searchBy = queryInfo.charAt(0);
        String searchKeyString = queryInfo.substring(2);
        Key searchKey = KeyFactory.stringToKey(searchKeyString);
        
        log.info("Query: " + searchBy + "=" + searchKeyString);
    	
        List<MenuItemType> menuItemTypes = MenuItemTypeManager.getRestaurantMenuItemTypes(searchKey);
        ArrayList<ProductItemTypeSimple> menuItemTypesSimple = new ArrayList<ProductItemTypeSimple>();
        for (MenuItemType menuItemType : menuItemTypes) {
        	ProductItemTypeSimple menuItemTypeSimple = new ProductItemTypeSimple(
        			KeyFactory.keyToString(menuItemType.getKey()),
        			menuItemType.getMenuItemTypeName(),
        			menuItemType.getMenuItemTypeDescription() != null ? 
        					menuItemType.getMenuItemTypeDescription() : "",
        			menuItemType.getProductItemTypeVersion()
        			);
        	menuItemTypesSimple.add(menuItemTypeSimple);
        }
        
        return menuItemTypesSimple;
    }

}