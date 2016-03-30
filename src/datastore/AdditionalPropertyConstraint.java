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

import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the AdditionalPropertyConstraint table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class AdditionalPropertyConstraint implements Serializable {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private Key menuItemAdditionalPropertyValue1;
    
    @Persistent
    private Key menuItemAdditionalPropertyValue2;

    /**
     * AdditionalPropertyConstraint constructor.
     * @param menuItemAdditionalPropertyValue1
     * 			: menu item additional property value 1
     * @param menuItemAdditionalPropertyValue2
     * 			: menu item additional property value 2
     * @throws MissingRequiredFieldsException
     */
    public AdditionalPropertyConstraint(Key menuItemAdditionalPropertyValue1,
    		Key menuItemAdditionalPropertyValue2) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (menuItemAdditionalPropertyValue1 == null || 
    			menuItemAdditionalPropertyValue2 == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
        this.menuItemAdditionalPropertyValue1 = menuItemAdditionalPropertyValue1;
        this.menuItemAdditionalPropertyValue2 = menuItemAdditionalPropertyValue2;
    }

    /**
     * Get AdditionalPropertyConstraint key.
     * @return additional property constraint key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Get menuItemAdditionalPropertyValue1
     * @return menuItemAdditionalPropertyValue1
     */
    public Key getMenuItemAdditionalPropertyValue1() {
        return menuItemAdditionalPropertyValue1;
    }
    
    /**
     * Get menuItemAdditionalPropertyValue2
     * @return menuItemAdditionalPropertyValue2
     */
    public Key getMenuItemAdditionalPropertyValue2() {
        return menuItemAdditionalPropertyValue2;
    }
    
    /**
     * Compare this additional property constraint with another additional property constraint
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this AdditionalPropertyConstraint, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof AdditionalPropertyConstraint ) ) return false;
        AdditionalPropertyConstraint additionalPropertyConstraint = (AdditionalPropertyConstraint) o;
        return this.getKey() == additionalPropertyConstraint.getKey();
    }

}