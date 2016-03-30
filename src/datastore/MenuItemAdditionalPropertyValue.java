/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the MenuItemAdditionalPropertyValue table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class MenuItemAdditionalPropertyValue implements Serializable {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private Key additionalPropertyValue;
    
    @Persistent
    private Double additionalCharge;

    /**
     * MenuItemAdditionalPropertyValue constructor.
     * @param additionalPropertyValue
     * 			: additional property value key
     * @param additionalCharge
     * 			: any additional charge incurred
     * @throws MissingRequiredFieldsException
     */
    public MenuItemAdditionalPropertyValue(Key additionalPropertyValue,
    		Double additionalCharge) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (additionalPropertyValue == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
        this.additionalPropertyValue = additionalPropertyValue;
        this.additionalCharge = additionalCharge;
    }

    /**
     * Get MenuItemAdditionalPropertyValue key.
     * @return additional property value key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Get AdditionalPropertyValue key.
     * @return additional property value key
     */
    public Key getAdditionalPropertyValue() {
        return additionalPropertyValue;
    }
    
    /**
     * Get Additional charge.
     * @return additional charge
     */
    public Double getAdditionalCharge() {
        return additionalCharge;
    }
    
    /**
     * Compare this menu item additional property value with another menu item additional property value
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this MenuItemAdditionalPropertyValue, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof MenuItemAdditionalPropertyValue ) ) return false;
        MenuItemAdditionalPropertyValue menuItemAdditionalPropertyValue = (MenuItemAdditionalPropertyValue) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(menuItemAdditionalPropertyValue.getKey()));
    }
    
    /**
     * Set additional charge
     * @param additionalCharge
     * 			: any additional charge incurred
     */
    public void setAdditionalCharge(Double additionalCharge) {
    	this.additionalCharge = additionalCharge;
    }

}