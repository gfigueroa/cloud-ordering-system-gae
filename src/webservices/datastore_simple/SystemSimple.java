/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a simple version of the System table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class SystemSimple implements Serializable {

    public Long key;
    public Integer restaurantListVersion;
    public Integer restaurantTypeListVersion;
    public Integer storeListVersionFoodDrink;
    public Integer storeListVersionShopping;
    public Integer storeListVersionPolls;
    public Integer storeListVersionSalon;
    public Integer storeListVersionGodDwellingPlace;
    public Integer storeListVersionVirtualChannel;
    public String oldestAppVersionSupported;
    public ArrayList<RestaurantVersions> storeVersions;

    /**
     * MenuItemSimple constructor.
     * @param key:
     * 			: order key
     * @param restaurantListVersion
     * 			: restaurant list version
     * @param restaurantTypeListVersion
     * 			: restaurant type list version
     * @param storeListversionFoodDrink
     * 			: store list version for Food & Drink
     * @param storeListVersionShopping
     * 			: store list version for Shopping
     * @param storeListversionPolls
     * 			: store list version for Polls
     * @param storeListVersionSalon
     * 			: store list version for Salons
     * @param storeListVersionGodDwellingPlace
     * 			: store list version for God Dwelling Place
     * @param storeListVersionVirtualChannel
     * 			: store list version for Virtual Channel
     * @param oldestAppVersionSupported
     * 			: oldest app version supported by this server version
     * @param restaurantVersions
     * 			: all restaurant versions (menu, types, sets, etc.)
     */
    public SystemSimple(Long key, Integer restaurantListVersion,
    		Integer restaurantTypeListVersion, Integer storeListVersionFoodDrink,
    		Integer storeListVersionShopping, Integer storeListVersionPolls,
    		Integer storeListVersionSalon, Integer storeListVersionGodDwellingPlace,
    		Integer storeListVersionVirtualChannel,
    		String oldestAppVersionSupported, ArrayList<RestaurantVersions> storeVersions) {
    	
    	this.key = key;
    	this.restaurantListVersion = restaurantListVersion;
    	this.restaurantTypeListVersion = restaurantTypeListVersion;
    	this.storeListVersionFoodDrink = storeListVersionFoodDrink;
    	this.storeListVersionShopping = storeListVersionShopping;
    	this.storeListVersionPolls = storeListVersionPolls;
    	this.storeListVersionSalon = storeListVersionSalon;
    	this.storeListVersionGodDwellingPlace = storeListVersionGodDwellingPlace;
    	this.storeListVersionVirtualChannel = storeListVersionVirtualChannel;
    	this.oldestAppVersionSupported = oldestAppVersionSupported;
    	this.storeVersions = storeVersions;
    }

    /**
     * Compare this system with another system
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this System, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof SystemSimple ) ) return false;
        SystemSimple system = (SystemSimple) o;
        return key.equals(system.key);
    }

}