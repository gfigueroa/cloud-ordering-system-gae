/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the OrderDetail table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@PersistenceCapable
public class OrderDetail {
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private Key menuItem;
    
    @Persistent
    private Integer orderDetailQuantity;
    
    @Persistent
    private String orderDetailComments;

    /**
     * OrderDetail constructor.
     * @param menuItem
     * 			: the corresponding menuItem for this order detail
     * @param orderDetailQuantity
     * 			: the quantity of the menuItem for this order detail
     * @param orderDetailComments
     * 			: order detail comments
     * @throws MissingRequiredFieldsException
     */
    public OrderDetail(Key menuItem, Integer orderDetailQuantity,
    		String orderDetailComments) throws MissingRequiredFieldsException {
    	
    	if (menuItem == null || orderDetailQuantity == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
        this.menuItem = menuItem;
        this.orderDetailQuantity = orderDetailQuantity;
        this.orderDetailComments = orderDetailComments;
    }

    /**
     * Get OrderDetail key.
     * @return order detail key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Get menuItem for this order detail.
     * @return menuItem key
     */
    public Key getMenuItem() {
        return menuItem;
    }
    
    /**
     * Get OrderDetail quantity.
     * @return order detail quantity
     */
    public Integer getOrderDetailQuantity() {
        return orderDetailQuantity;
    }
    
    /**
     * Get OrderDetail comments.
     * @return order detail comments
     */
    public String getOrderDetailComments() {
    	return orderDetailComments;
    }
    
    /**
     * Set OrderDetail menuItem.
     * @param menuItem
     * 			: order detail menuItem key
     * @throws MissingRequiredFieldsException 
     */
    public void setMenuItem(Key menuItem) throws MissingRequiredFieldsException {
    	if (menuItem == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Missing menuItem key.");
    	}
    	this.menuItem = menuItem;
    }
    
    /**
     * Set OrderDetail quantity.
     * @param orderDetailQuantity
     * 			: order detail quantity
     * @throws MissingRequiredFieldsException 
     */
    public void setOrderDetailQuantity(Integer orderDetailQuantity) throws MissingRequiredFieldsException {
    	if (orderDetailQuantity == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Missing order detail quantity.");
    	}
    	this.orderDetailQuantity = orderDetailQuantity;
    }
    
    /**
     * Set OrderDetail comments.
     * @param orderDetailComments
     * 			: order detail comments
     */
    public void setOrderDetailComments(String orderDetailComments) {
    	this.orderDetailComments = orderDetailComments;
    }
    
}
