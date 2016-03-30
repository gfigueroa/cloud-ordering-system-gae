/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;

/**
 * This class represents a simple version of the OrderDetail table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class OrderDetailSimple implements Serializable {

	public String key;
	public String menuItemKey;
	public Integer quantity;
	public String menuItemName; // TODO: Remove later

    /**
     * OrderDetailSimple constructor.
     * @param key:
     * 			: order detail key
     * @param menuItemKey
     * 			: menu item key
     * @param quantity
     * 			: quantity
     */
    public OrderDetailSimple(String key, String menuItemKey, 
    		Integer quantity, String menuItemName) {
    	
    	this.key = key;
    	this.menuItemKey = menuItemKey;
    	this.quantity = quantity;
    	this.menuItemName = menuItemName; // TODO: Remove later
    }

    /**
     * Compare this order detail with another order detail
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this OrderDetail, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof OrderDetailSimple ) ) return false;
        OrderDetailSimple orderDetail = (OrderDetailSimple) o;
        return key.equals(orderDetail.key);
    }

}