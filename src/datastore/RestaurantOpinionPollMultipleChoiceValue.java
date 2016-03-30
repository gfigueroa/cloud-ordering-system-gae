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
 * This class represents the RestaurantOpinionPollMultipleChoiceValue table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class RestaurantOpinionPollMultipleChoiceValue implements Serializable {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String multipleChoiceValue;

    /**
     * RestaurantOpinionPollMultipleChoiceValue constructor.
     * @param multipleChoiceValue
     * 			: restaurant opinion poll multiple choice value
     * @throws MissingRequiredFieldsException
     */
    public RestaurantOpinionPollMultipleChoiceValue(String multipleChoiceValue) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (multipleChoiceValue == null || multipleChoiceValue.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
        this.multipleChoiceValue = multipleChoiceValue;
    }

    /**
     * Get RestaurantOpinionPollMultipleChoiceValue key.
     * @return restaurant opinion poll multiple choice value key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Get multiple Choice Value.
     * @return multipleChoiceValue
     */
    public String getMultipleChoiceValue() {
        return multipleChoiceValue;
    }

    
    /**
     * Compare this restaurant opinion poll multiple choice value with another restaurant opinion poll multiple choice value
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this RestaurantOpinionPollMultipleChoiceValue, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof RestaurantOpinionPollMultipleChoiceValue ) ) return false;
        RestaurantOpinionPollMultipleChoiceValue restaurantOpinionPollMultipleChoiceValue = (RestaurantOpinionPollMultipleChoiceValue) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(restaurantOpinionPollMultipleChoiceValue.getKey()));
    }
    
    /**
     * Set multiple Choice Value.
     * @param multipleChoiceValue
     * 			: multiple Choice Value
     * @throws MissingRequiredFieldsException
     */
    public void setMultipleChoiceValue(String multipleChoiceValue)
    		throws MissingRequiredFieldsException {
    	if (multipleChoiceValue == null || multipleChoiceValue.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"multiple Choice Value is missing.");
    	}
    	this.multipleChoiceValue = multipleChoiceValue;
    }

}