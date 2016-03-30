/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.appengine.api.blobstore.BlobKey;

/**
 * This class represents a simple version of the MenuItem table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class ProductItemSimple implements Serializable {

    public String key;
    public String menuItemType;
    public String menuItemName;
    public Double menuItemPrice;
    public Double menuItemDiscount;
    public String menuItemDescription;
    public BlobKey menuItemImage;
    public Integer menuItemServingTime;
    public Boolean isAvailable;
    public Integer menuItemServiceTime;
    public ArrayList<MenuItemAdditionalPropertyValueSimple> 
    		menuItemAdditionalPropertyValues;
    public ArrayList<AdditionalPropertyConstraintSimple>
    		additionalPropertyConstraints;

    /**
     * MenuItemSimple constructor.
     * @param key:
     * 			: menuItem key
     * @param menuItemType
     * 			: menuItem type
     * @param menuItemName
     * 			: menuItem name
     * @param menuItemPrice
     * 			: menuItem price
     * @param menuItemDiscount
     * 			: menuItem discount
     * @param menuItemDescription
     * 			: menuItem description
     * @param menuItemImage
     * 			: menuItem image blob key
     * @param menuItemServingTime
     * 			: menuItem serving time
     * @param isAvailable
     * 			: menuItem is available
     * @param menuItemServiceTime
     * 			: menuItem service time
     * @param menuItemAdditionalPropertyValues
     * 			: menuItem additional property values
     * @param additionalPropertyConstraints
     * 			: menuItem additional property constraints
     */
    public ProductItemSimple(String key, String menuItemType, 
    		String menuItemName, Double menuItemPrice, 
    		Double menuItemDiscount, String menuItemDescription, 
    		BlobKey menuItemImage, Integer menuItemServingTime, 
    		Boolean isAvailable, Integer menuItemServiceTime,
    		ArrayList<MenuItemAdditionalPropertyValueSimple> 
    				menuItemAdditionalPropertyValues,
    		ArrayList<AdditionalPropertyConstraintSimple>
    				additionalPropertyConstraints) {
    	
    	this.key = key;
    	this.menuItemType = menuItemType;
    	this.menuItemName = menuItemName;
    	this.menuItemPrice = menuItemPrice;
    	this.menuItemDiscount = menuItemDiscount;
    	this.menuItemDescription = menuItemDescription;
    	this.menuItemImage = menuItemImage;
    	this.menuItemServingTime = menuItemServingTime;
    	this.isAvailable = isAvailable;
    	this.menuItemServiceTime = menuItemServiceTime;
    	this.menuItemAdditionalPropertyValues = 
    			menuItemAdditionalPropertyValues;
    	this.additionalPropertyConstraints = 
    			additionalPropertyConstraints;
    }

    /**
     * Compare this product item with another product item
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this MenuItem, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof ProductItemSimple ) ) return false;
        ProductItemSimple menuItem = (ProductItemSimple) o;
        return key.equals(menuItem.key);
    }

}