/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a simple version of the OrderDetailMenuItemAdditionalProperty table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class OrderDetailMenuItemAdditionalPropertySimple implements Serializable {

	public String key;
	public ArrayList<String> menuItemAdditionalPropertyValueKeys;
	public Integer quantity;

    /**
     * OrderDetailMenuItemAdditionalPropertySimple constructor.
     * @param key:
     * 			: order detail menu item additional property key
     * @param menuItemAdditionalPropertyValueKey
     * 			: menu item additional property value key
     * @param quantity
     * 			: quantity
     */
    public OrderDetailMenuItemAdditionalPropertySimple(String key, 
    		ArrayList<String> menuItemAdditionalPropertyValueKeys, 
    		Integer quantity) {
    	
    	this.key = key;
    	this.menuItemAdditionalPropertyValueKeys = 
    			menuItemAdditionalPropertyValueKeys;
    	this.quantity = quantity;
    }

    /**
     * Compare this order detail with another order detail
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this 
     * 			OrderDetailMenuItemAdditionalProperty, false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof OrderDetailMenuItemAdditionalPropertySimple ) ) return false;
        OrderDetailMenuItemAdditionalPropertySimple orderDetailMenuItemAdditionalProperty = 
        		(OrderDetailMenuItemAdditionalPropertySimple) o;
        return key.equals(orderDetailMenuItemAdditionalProperty.key);
    }

}