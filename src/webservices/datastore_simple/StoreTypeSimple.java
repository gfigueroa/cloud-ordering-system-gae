/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.)
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;

import datastore.RestaurantType;

/**
 * This class represents a simple version of the RestaurantType table.
 * It is kept simple to return only some information to mobile Apps.
 * 
 */

@SuppressWarnings("serial")
public class StoreTypeSimple implements Serializable {
    
	public String key;
	public RestaurantType.StoreSuperType storeSuperType;
	public String restaurantTypeName;
	public String restaurantTypeDescription;
	public Integer storeTypeVersion;
    
    /**
     * StoreTypeSimple constructor.
     * @param key
     * 			: RestaurantType key string
     * @param storeSuperType
     * 			: The store super type
     * @param restaurantTypeName
     * 			: The name of the restaurant type
     * @param restaurantTypeDescription
     * 			: The description of the restaurant type
     * @param storeTypeVersion
     * 			: The version number for this store type's stores
     */
    public StoreTypeSimple(String key, 
    		RestaurantType.StoreSuperType storeSuperType,
    		String restaurantTypeName,
    		String restaurantTypeDescription,
    		Integer storeTypeVersion) {

    	this.key = key;
    	this.storeSuperType = storeSuperType;
    	this.restaurantTypeName = restaurantTypeName;
    	this.restaurantTypeDescription = restaurantTypeDescription;
    	this.storeTypeVersion = storeTypeVersion;
    }
    
    /**
     * Compare this store type with another StoreType
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this StoreType, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof StoreTypeSimple ) ) return false;
        StoreTypeSimple r = (StoreTypeSimple) o;
        return this.key.equals(r.key);
    }
    
}
