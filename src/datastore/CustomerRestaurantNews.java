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
 * This class represents the CustomerRestaurantNews table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class CustomerRestaurantNews implements Serializable {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long key;
	
	@Persistent
    private Key customer;
	
	@Persistent
	private Key restaurantNews;

    /**
     * CustomerRestaurantNews constructor.
     * @param customer
     * 			: the customer key
     * @param restaurantNews
     * 			: the restaurant news key
     * @throws MissingRequiredFieldsException
     */
    public CustomerRestaurantNews(Key customer, Key restaurantNews) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (customer == null || restaurantNews == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
    	this.customer = customer;
    	this.restaurantNews = restaurantNews;
    }
    
    /**
     * Get CustomerRestaurantNews key.
     * @return Restaurant News key
     */
    public Long getKey() {
        return key;
    }
    
    /**
     * Get customer.
     * @return Customer key
     */
    public Key getCustomer() {
        return customer;
    }
    
    /**
     * Get restaurant news.
     * @return Restaurant news key
     */
    public Key getRestaurantNews() {
        return restaurantNews;
    }
    
    /**
     * Compare this Restaurant News with another Restaurant News
     * @param o
     * 			: the object to compare
     * @return true if the object to compare is equal to this CustomerRestaurantNews, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof CustomerRestaurantNews ) ) return false;
        CustomerRestaurantNews customerRestaurantNews = (CustomerRestaurantNews) o;
        return this.getKey().equals(customerRestaurantNews.getKey());
    }

}