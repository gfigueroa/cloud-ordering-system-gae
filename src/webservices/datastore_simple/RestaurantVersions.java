/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;

/**
 * This class represents a simple version of the Restaurant Versions.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class RestaurantVersions implements Serializable {
    
	public String restaurantKey;
	public Integer menuVersion;
	public Integer setVersion;
	public Integer menuItemTypeVersion;
	public Integer additionalPropertyVersion;
	public Integer messageVersion;
    
    /**
     * RestaurantVersionsSimple constructor.
     * @param restaurantKey
     * 			: restaurant key
     * @param menuVersion
     * 			: menu version
     * @param setVersion
     * 			: set version
     * @param menuItemTypeVersion
     * 			: menu item type version
     * @param additionalPropertyVersion
     * 			: additional property version
     * @param messageVersion
     * 			: message version
     */
    public RestaurantVersions(String restaurantKey, Integer menuVersion, 
    		Integer setVersion, Integer menuItemTypeVersion, 
    		Integer additionalPropertyVersion, Integer messageVersion) {

    	this.restaurantKey = restaurantKey;
    	this.menuVersion = menuVersion;
    	this.setVersion = setVersion;
    	this.menuItemTypeVersion = menuItemTypeVersion;
    	this.additionalPropertyVersion = additionalPropertyVersion;
    	this.messageVersion = messageVersion;
    }
    
    /**
     * Compare this restaurantVersions with another RestaurantVersions
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this RestaurantVersions, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof RestaurantVersions ) ) return false;
        RestaurantVersions r = (RestaurantVersions) o;
        return this.restaurantKey.equals(r.restaurantKey);
    }
    
}
