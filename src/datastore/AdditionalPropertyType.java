/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.io.Serializable;
import java.util.ArrayList;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import exceptions.InexistentObjectException;
import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the AdditionalPropertyType table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class AdditionalPropertyType implements Serializable {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String additionalPropertyTypeName;
    
    @Persistent
    private String additionalPropertyTypeDescription;
    
    @Persistent
    @Element(dependent = "true")
    private ArrayList<AdditionalPropertyValue> additionalPropertyValues;

    /**
     * AdditionalPropertyType constructor.
     * @param additionalPropertyTypeName
     * 			: menu item type name
     * @param additionalPropertyTypeDescription
     * 			: menu item type description
     * @throws MissingRequiredFieldsException
     */
    public AdditionalPropertyType(String additionalPropertyTypeName, String additionalPropertyTypeDescription) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (additionalPropertyTypeName == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	if (additionalPropertyTypeName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
        this.additionalPropertyTypeName = additionalPropertyTypeName;
        this.additionalPropertyTypeDescription = additionalPropertyTypeDescription;
        this.additionalPropertyValues = new ArrayList<AdditionalPropertyValue>();
    }

    /**
     * Get AdditionalPropertyType key.
     * @return additional property type key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Get AdditionalPropertyType name.
     * @return additional property type name
     */
    public String getAdditionalPropertyTypeName() {
        return additionalPropertyTypeName;
    }

    /**
     * Get AdditionalPropertyType description.
     * @return additional property type description
     */
    public String getAdditionalPropertyTypeDescription() {
    	return additionalPropertyTypeDescription;
    }
    
    /**
     * Get AdditionalProperty values.
     * @return the possible values this additional property type may have
     */
    public ArrayList<AdditionalPropertyValue> getAdditionalPropertyValues() {
    	return additionalPropertyValues;
    }
    
    /**
     * Compare this additional property type with another additional property type
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this AdditionalPropertyType, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof AdditionalPropertyType ) ) return false;
        AdditionalPropertyType additionalPropertyType = (AdditionalPropertyType) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(additionalPropertyType.getKey()));
    }
    
    /**
     * Set AdditionalPropertyType name.
     * @param additionalPropertyTypeName
     * 			: additional property type name
     * @throws MissingRequiredFieldsException
     */
    public void setAdditionalPropertyTypeName(String additionalPropertyTypeName)
    		throws MissingRequiredFieldsException {
    	if (additionalPropertyTypeName == null || additionalPropertyTypeName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Additional property type name is missing.");
    	}
    	this.additionalPropertyTypeName = additionalPropertyTypeName;
    }
    
    /**
     * Set AdditionalPropertyType description.
     * @param additionalPropertyTypeDescription
     * 			: additional property type description
     */
    public void setAdditionalPropertyTypeDescription(String additionalPropertyTypeDescription) {
    	this.additionalPropertyTypeDescription = additionalPropertyTypeDescription;
    }
    
    /**
     * Add a new additional property value to this type
     * @param additionalPropertyValue
     * 			: additional property value to add
     */
    public void addAdditionalPropertyValue(AdditionalPropertyValue additionalPropertyValue) {
    	this.additionalPropertyValues.add(additionalPropertyValue);
    }
    
    /**
     * Remove an additional property value from this type
     * @param additionalPropertyValue
     * 			: additional property value to be removed
     * @throws InexistentObjectException
     */
    public void removeAdditionalPropertyValue(AdditionalPropertyValue additionalPropertyValue) 
    		throws InexistentObjectException {
    	if (!additionalPropertyValues.remove(additionalPropertyValue)) {
    		throw new InexistentObjectException(AdditionalPropertyValue.class, "Additional property value not found!");
    	}
    }
}