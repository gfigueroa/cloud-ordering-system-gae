/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Link;


/**
 * This class represents a simple version of the Restaurant table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class RestaurantSimple implements Serializable {
    
	public String key;
	public String restaurantType;
	public String restaurantName;
	public String restaurantDescription;
	public Link restaurantWebsite;
	public BlobKey restaurantLogo;
	public String restaurantOpeningTime; 
	public String restaurantClosingTime;
	public ArrayList<BranchSimple> branches;
    
    /**
     * RestaurantSimple constructor.
     * @param key
     * 			: restaurant key
     * @param restaurantType
     * 			: restaurant type key
     * @param restaurantName
     * 			: restaurant name
     * @param restaurantDescription
     * 			: restaurant description
     * @param restaurantWebsite
     * 			: restaurant website
     * @param restaurantLogo
     * 			: restaurant logo blob key
     * @param restaurantOpeningTime
     * 			: restaurant opening time
     * @param restaurantClosingTime
     * 			: restaurant closing time
     * @param branches
     * 			: restaurant branches
     */
    public RestaurantSimple(String key, String restaurantType,
    		String restaurantName, String restaurantDescription,
    		Link restaurantWebsite, BlobKey restaurantLogo, 
    		String restaurantOpeningTime, String restaurantClosingTime,
    		ArrayList<BranchSimple> branches) {

    	this.key = key;
    	this.restaurantType = restaurantType;
    	this.restaurantName = restaurantName;
    	this.restaurantDescription = restaurantDescription;
        this.restaurantWebsite = restaurantWebsite;
        this.restaurantLogo = restaurantLogo;
        this.restaurantOpeningTime = restaurantOpeningTime;
        this.restaurantClosingTime = restaurantClosingTime;
        this.branches = branches;
    }
    
    /**
     * Compare this restaurant with another Restaurant
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this Restaurant, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof RestaurantSimple ) ) return false;
        RestaurantSimple r = (RestaurantSimple) o;
        return this.key.equals(r.key);
    }
    
}
