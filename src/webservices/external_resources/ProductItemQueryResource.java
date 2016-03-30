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

import webservices.datastore_simple.AdditionalPropertyConstraintSimple;
import webservices.datastore_simple.MenuItemAdditionalPropertyValueSimple;
import webservices.datastore_simple.ProductItemSimple;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import datastore.AdditionalPropertyConstraint;
import datastore.AdditionalPropertyConstraintManager;
import datastore.MenuItem;
import datastore.MenuItemAdditionalPropertyValue;
import datastore.MenuItemAdditionalPropertyValueManager;
import datastore.MenuItemManager;

/**
 * This class represents the list of menu items 
 * as a Resource with only one representation
 */

public class ProductItemQueryResource extends ServerResource {
	
	private static final Logger log = 
	        Logger.getLogger(ProductItemQueryResource.class.getName());

	/**
	 * Returns the queried menu items as a JSON object.
	 * @return An ArrayList of MenuItemSimple in JSON format
	 */
    @Get("json")
    public ArrayList<ProductItemSimple> toJson() {
        // we will be returning a list of elements
        List<MenuItem> menuItems = null;

        // we have to check the queryInfo to see if the query was
        // based on the Restaurant or MenuItem Type
        // the format of the queryinfo is:
        // [r|t]=key
        String queryInfo = (String) getRequest().getAttributes()
            .get("queryinfo");
        log.info("queryinfo: " + queryInfo);
        
        char searchBy = queryInfo.charAt(0);
        String searchKey = queryInfo.substring(2);
        Key key;
        key = KeyFactory.stringToKey(searchKey);

        switch (searchBy) {
            case 'r':   
                menuItems = MenuItemManager.getAllMenuItemsByRestaurant(key);
                break;
            case 't':
                menuItems = MenuItemManager.getAllMenuItemsByType(key);
                break;
            default:
                assert(false); // no such type
        }
        
        ArrayList<ProductItemSimple> simpleMenuItems = new ArrayList<ProductItemSimple>();
        for (MenuItem menuItem : menuItems) {
        	
        	// Get additional property values
        	List<MenuItemAdditionalPropertyValue> additionalPropertyValues =
        			MenuItemAdditionalPropertyValueManager.getMenuItemAdditionalPropertyValues(
        					menuItem.getKey());
        	ArrayList<MenuItemAdditionalPropertyValueSimple> additionalPropertyValuesSimple =
        			new ArrayList<MenuItemAdditionalPropertyValueSimple>();
        	for (MenuItemAdditionalPropertyValue value : additionalPropertyValues) {
        		MenuItemAdditionalPropertyValueSimple valueSimple = 
        				new MenuItemAdditionalPropertyValueSimple(
        						KeyFactory.keyToString(value.getKey()),
        						KeyFactory.keyToString(value.getAdditionalPropertyValue()),
        						value.getAdditionalCharge() != null ? value.getAdditionalCharge() : 0
        						);
        		additionalPropertyValuesSimple.add(valueSimple);
        	}
        	
        	// Get additional property constrains
        	List<AdditionalPropertyConstraint> additionalPropertyConstraints =
        			AdditionalPropertyConstraintManager.getMenuItemAdditionalPropertyConstraints(
        					menuItem.getKey());
        	ArrayList<AdditionalPropertyConstraintSimple> additionalPropertyConstraintsSimple =
        			new ArrayList<AdditionalPropertyConstraintSimple>();
        	for (AdditionalPropertyConstraint apc: additionalPropertyConstraints) {
        		AdditionalPropertyConstraintSimple apcSimple = 
        				new AdditionalPropertyConstraintSimple(KeyFactory.keyToString(apc.getKey()),
        						KeyFactory.keyToString(apc.getMenuItemAdditionalPropertyValue1()),
        						KeyFactory.keyToString(apc.getMenuItemAdditionalPropertyValue2()));
        		additionalPropertyConstraintsSimple.add(apcSimple);
        	}
        	
        	ProductItemSimple menuItemSimple = new ProductItemSimple(
        			KeyFactory.keyToString(menuItem.getKey()),
        			KeyFactory.keyToString(menuItem.getMenuItemType()),
        			menuItem.getMenuItemName(),
        			menuItem.getMenuItemPrice(),
        			menuItem.getMenuItemDiscount() != null ? 
        					menuItem.getMenuItemDiscount() : 0,
        			menuItem.getMenuItemDescription(),
        			menuItem.getMenuItemImage() != null ? 
        					menuItem.getMenuItemImage() : new BlobKey(""),
        			menuItem.getMenuItemServingTime() != null ? 
        					menuItem.getMenuItemServingTime() : 0,
        			menuItem.isAvailable(),
        			menuItem.getMenuItemServiceTime(),
        			additionalPropertyValuesSimple,
        			additionalPropertyConstraintsSimple
        			);
        	simpleMenuItems.add(menuItemSimple);
        }
        
        return simpleMenuItems;
    }

}
