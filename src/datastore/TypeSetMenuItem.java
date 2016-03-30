/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.io.Serializable;

import com.google.appengine.api.datastore.Key;

import exceptions.MissingRequiredFieldsException;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

/**
 * This class represents the TypeSetMenuItem table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class TypeSetMenuItem implements Serializable {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private Key menuItem;
    
    @Persistent
    private Double typeSetMenuItemPrice;

    /**
     * TypeSetMenuItem constructor.
     * @param menuItem
     * 			: menu item key
     * @param typeSetMenuItemPrice
     * 			: typeSetMenuItem price
     * @throws MissingRequiredFieldsException
     */
    public TypeSetMenuItem(Key menuItem, Double typeSetMenuItemPrice) 
		throws MissingRequiredFieldsException {
        
    	// Check "required field" constraints
    	if (menuItem == null || typeSetMenuItemPrice == null) {
    		throw new MissingRequiredFieldsException(
    				this.getClass(), "One or more required fields are missing.");
    	}

    	this.menuItem = menuItem;
    	this.typeSetMenuItemPrice = typeSetMenuItemPrice;
    }

    /**
     * Get TypeSetMenuItem key.
     * @return typeSetMenuItem key
     */
    public Key getKey() {
        return key;
    }
    
    /**
     * Get menu item.
     * @return menu item key
     */
    public Key getMenuItem() {
        return menuItem;
    }
    
    /**
     * Get TypeSetMenuItem price.
     * @return type set menu item price
     */
    public Double getTypeSetMenuItemPrice() {
        return typeSetMenuItemPrice;
    }

}