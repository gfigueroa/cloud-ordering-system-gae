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
 * This class represents the AdditionalPropertyValue table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class AdditionalPropertyValue implements Serializable {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String additionalPropertyValueName;

    /**
     * AdditionalPropertyValue constructor.
     * @param additionalPropertyValueName
     * 			: menu item value name
     * @throws MissingRequiredFieldsException
     */
    public AdditionalPropertyValue(String additionalPropertyValueName) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (additionalPropertyValueName == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	if (additionalPropertyValueName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
        this.additionalPropertyValueName = additionalPropertyValueName;
    }

    /**
     * Get AdditionalPropertyValue key.
     * @return additional property value key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Get AdditionalPropertyValue name.
     * @return additional property value name
     */
    public String getAdditionalPropertyValueName() {
        return additionalPropertyValueName;
    }
    
    /**
     * Compare this additional property value with another additional property value
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this AdditionalPropertyValue, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof AdditionalPropertyValue ) ) return false;
        AdditionalPropertyValue additionalPropertyValue = (AdditionalPropertyValue) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(additionalPropertyValue.getKey()));
    }
    
    /**
     * Set AdditionalPropertyValue name.
     * @param additionalPropertyValueName
     * 			: additional property value name
     * @throws MissingRequiredFieldsException
     */
    public void setAdditionalPropertyValueName(String additionalPropertyValueName)
    		throws MissingRequiredFieldsException {
    	if (additionalPropertyValueName == null || additionalPropertyValueName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Additional property value name is missing.");
    	}
    	this.additionalPropertyValueName = additionalPropertyValueName;
    }

}