/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents a simple version of the OrderDetailSet table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class OrderDetailSetSimple implements Serializable {

	public String key;
	public String setKey;
	public List<String> typeSetMenuItemKeys;
	public Integer quantity;

    /**
     * OrderDetailSetSimple constructor.
     * @param key:
     * 			: order detail set key
     * @param setKey
     * 			: set key
     * @param typeSetMenuItemKeys
     * 			: the type set menu item keys chosen by the
     * 			  the customer (for Type Sets)
     * @param quantity
     * 			: quantity
     */
    public OrderDetailSetSimple(String key, String setKey, 
    		List<String> typeSetMenuItemKeys, Integer quantity) {
    	
    	this.key = key;
    	this.setKey = setKey;
    	this.typeSetMenuItemKeys = typeSetMenuItemKeys;
    	this.quantity = quantity;
    }

    /**
     * Compare this order detail set with another order detail set
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this OrderDetailSet, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof OrderDetailSetSimple ) ) return false;
        OrderDetailSetSimple orderDetailSet = (OrderDetailSetSimple) o;
        return key.equals(orderDetailSet.key);
    }

}