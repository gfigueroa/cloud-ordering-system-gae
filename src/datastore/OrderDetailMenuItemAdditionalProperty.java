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
 * This class represents the OrderDetailMenuItemAdditionalProperty table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@PersistenceCapable
public class OrderDetailMenuItemAdditionalProperty {
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private ArrayList<Key> menuItemAdditionalPropertyValues;
    
    @Persistent
    private Integer orderDetailMenuItemAdditionalPropertyQuantity;
    
    @Persistent
    private String orderDetailMenuItemAdditionalPropertyComments;
    
    /**
     * OrderDetailMenuItemAdditionalProperty constructor.
     * @param menuItemAdditionalPropertyValues
     * 			: the corresponding menuItem additional property values
     * 			 for this order detail menu item additional property
     * @param orderDetailMenuItemAdditionalPropertyQuantity
     * 			: the quantity of the menuItem for this order detail 
     * 			 menu item additional property
     * @param orderDetailMenuItemAdditionalPropertyComments
     * 			: order detail menu item additional property comments
     * @throws MissingRequiredFieldsException
     */
    public OrderDetailMenuItemAdditionalProperty(ArrayList<Key> menuItemAdditionalPropertyValues, 
    		Integer orderDetailMenuItemAdditionalPropertyQuantity,
    		String orderDetailMenuItemAdditionalPropertyComments) 
    				throws MissingRequiredFieldsException {
    	
    	if (menuItemAdditionalPropertyValues == null || 
    			menuItemAdditionalPropertyValues.isEmpty() ||
    			orderDetailMenuItemAdditionalPropertyQuantity == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
        this.menuItemAdditionalPropertyValues = 
        		menuItemAdditionalPropertyValues;
        this.orderDetailMenuItemAdditionalPropertyQuantity = 
        		orderDetailMenuItemAdditionalPropertyQuantity;
        this.orderDetailMenuItemAdditionalPropertyComments = 
        		orderDetailMenuItemAdditionalPropertyComments;
    }

    /**
     * Get OrderDetailMenuItemAdditionalProperty key.
     * @return order detail menu item additional property key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Get menuItemAdditionalPropertyValue keys for this 
     * order detail menu item additional property.
     * @return menuItem key
     */
    public ArrayList<Key> getMenuItemAdditionalPropertyValues() {
        return menuItemAdditionalPropertyValues;
    }
    
    /**
     * Get OrderDetailMenuItemAdditionalProperty quantity.
     * @return order detail menu item additional property quantity
     */
    public Integer getOrderDetailMenuItemAdditionalPropertyQuantity() {
        return orderDetailMenuItemAdditionalPropertyQuantity;
    }
    
    /**
     * Get OrderDetailMenuItemAdditionalProperty comments.
     * @return order detail menu item additional property comments
     */
    public String getOrderDetailMenuItemAdditionalPropertyComments() {
    	return orderDetailMenuItemAdditionalPropertyComments;
    }
}
