/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;

/**
 * This class represents a simple version of the Order table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class OrderSimple implements Serializable {

    public String key;
    public String orderType;
    public String orderTime;
    public String branchKey;
    public String status;
    public String restaurantName; //TODO: Remove later
    public String branchName; //TODO: Remove later

    /**
     * MenuItemSimple constructor.
     * @param key:
     * 			: order key
     * @param orderType
     * 			: order type
     * @param orderTime
     * 			: the time the order was made
     * @param branchKey
     * 			: the key of the branch
     * @param status
     * 			: the status of the order
     */
    public OrderSimple(String key, String orderType,
    		String orderTime, String branchKey, String status,
    		String restaurantName, String branchName) {
    	
    	this.key = key;
    	this.orderType = orderType;
    	this.orderTime = orderTime;
    	this.branchKey = branchKey;
    	this.status = status;
    	this.restaurantName = restaurantName; //TODO: Remove later
    	this.branchName = branchName; //TODO: Remove later
    }

    /**
     * Compare this order with another order
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this Order, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof OrderSimple ) ) return false;
        OrderSimple order = (OrderSimple) o;
        return key.equals(order.key);
    }

}