/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.ArrayList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the OrderDetailSet table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@PersistenceCapable
public class OrderDetailSet {
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private Key set;
    
    @Persistent
    private ArrayList<Key> typeSetMenuItems; // For Type Sets
    
    @Persistent
    private Integer orderDetailSetQuantity;
    
    @Persistent
    private String orderDetailSetComments;
    
    /**
     * OrderDetailSet constructor.
     * @param set
     * 			: the corresponding set key for this order detail set
     * @param orderDetailSetQuantity
     * 			: the quantity of the menuItem for this order detail set
     * @param orderDetailSetComments
     * 			: order detail set comments
     * @throws MissingRequiredFieldsException
     */
    public OrderDetailSet(Key set, ArrayList<Key> typeSetMenuItems,
    		Integer orderDetailSetQuantity,
    		String orderDetailSetComments) 
    				throws MissingRequiredFieldsException {
    	
    	if (set == null || orderDetailSetQuantity == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
        this.set = set;
        this.typeSetMenuItems = typeSetMenuItems;
        this.orderDetailSetQuantity = 
        		orderDetailSetQuantity;
        this.orderDetailSetComments = 
        		orderDetailSetComments;
    }

    /**
     * Get OrderDetailSet key.
     * @return order detail set key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Get set key for this 
     * order detail set.
     * @return set key
     */
    public Key getSet() {
        return set;
    }
    
    /**
     * Get type set menu item keys chosen for
     * this order detail set
     * @return type set menu item keys
     */
    public ArrayList<Key> getTypeSetMenuItems() {
    	return typeSetMenuItems;
    }
    
    /**
     * Get OrderDetailSet quantity.
     * @return order detail set quantity
     */
    public Integer getOrderDetailSetQuantity() {
        return orderDetailSetQuantity;
    }
    
    /**
     * Get OrderDetailSet comments.
     * @return order detail set comments
     */
    public String getOrderDetailSetComments() {
    	return orderDetailSetComments;
    }
}
