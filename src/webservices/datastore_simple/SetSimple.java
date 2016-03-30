/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.appengine.api.blobstore.BlobKey;

import datastore.Set;

/**
 * This class represents a simple version of the Set table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class SetSimple implements Serializable {

    public String key;
    public Integer setNumber;
    public Set.SetType setType;
    public String setName;
    public String setDescription;
    public Double setPrice;
    public Boolean hasFixedPrice;
    public Double setDiscount;
    public BlobKey setImage;
    public Integer setServingTime;
    public Boolean isAvailable;
    public Integer setServiceTime;
    public ArrayList<String> menuItems;
    public ArrayList<TypeSetMenuItemSimple> typeSetMenuItems;

    /**
     * SetSimple constructor.
     * @param key:
     * 			: set key
     * @param setNumber
     * 			: set number
     * @param setType
     * 			: set type
     * @param setName
     * 			: set name
     * @param setDescription
     * 			: set description
     * @param setPrice
     * 			: set price
     * @param hasFixedPrice
     * 			: whether this set has fixed price or not
     * @param setDiscount
     * 			: set discount
     * @param setImage
     * 			: set image blob key
     * @param setServingTime
     * 			: set serving time
     * @param isAvailable
     * 			: set is available
     * @param setServiceTime
     * 			: set service time
     * @param menuItems
     * 			: menu items keys (for fixed sets)
     * @param typeSetMenuItems
     * 			: the type set menu items (for type sets)
     */
    public SetSimple(String key, Integer setNumber, 
    		Set.SetType setType, String setName, 
    		String setDescription, Double setPrice, 
    		Boolean hasFixedPrice, Double setDiscount,  
    		BlobKey setImage, Integer setServingTime, 
    		Boolean isAvailable, Integer setServiceTime,
    		ArrayList<String> menuItems,
    		ArrayList<TypeSetMenuItemSimple> typeSetMenuItems) {
    	
    	this.key = key;
    	this.setNumber = setNumber;
    	this.setType = setType;
    	this.setName = setName;
    	this.setDescription = setDescription;
    	this.setPrice = setPrice;
    	this.hasFixedPrice = hasFixedPrice;
    	this.setDiscount = setDiscount;
    	this.setImage = setImage;
    	this.setServingTime = setServingTime;
    	this.isAvailable = isAvailable;
    	this.setServiceTime = setServiceTime;
    	this.menuItems = menuItems;
    	this.typeSetMenuItems = typeSetMenuItems;
    }

    /**
     * Compare this set with another set
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this Set, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof SetSimple ) ) return false;
        SetSimple set = (SetSimple) o;
        return key.equals(set.key);
    }

}