/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the CustomerRestaurantOpinionPoll table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class CustomerRestaurantOpinionPoll implements Serializable {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long key;
	
	@Persistent
    private Key customer;
	
	@Persistent
	private Key restaurantOpinionPoll;
	
	@Persistent
	private String response;
	
	@Persistent
	private Integer responseOrder;
	
	@Persistent
	private Date date;

    /**
     * CustomerRestaurantOpinionPoll constructor.
     * @param customer
     * 			: the customer key
     * @param restaurantOpinionPoll
     * 			: the restaurant opinion poll key
     * @param response
     * 			: the response to this opinion poll
     * @param responseOrder
     * 			: the response order
     * @throws MissingRequiredFieldsException
     */
    public CustomerRestaurantOpinionPoll(Key customer, Key restaurantOpinionPoll,
    		String response, Integer responseOrder) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (customer == null || restaurantOpinionPoll == null || response == null ||
    			response.trim().isEmpty() || responseOrder == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
    	this.customer = customer;
    	this.restaurantOpinionPoll = restaurantOpinionPoll;
    	this.response = response;
    	this.responseOrder = responseOrder;
    	this.date = new Date();
    }
    
    /**
     * Get CustomerRestaurantOpinionPoll key.
     * @return Restaurant OpinionPoll key
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
     * Get restaurant opinion poll.
     * @return Restaurant opinion poll key
     */
    public Key getRestaurantOpinionPoll() {
        return restaurantOpinionPoll;
    }
    
    /**
     * Get response.
     * @return The response given
     */
    public String getResponse() {
        return response;
    }
    
    /**
     * Get response order.
     * @return The response order
     */
    public Integer getResponseOrder() {
    	if (responseOrder == null) {
    		return 1;
    	}
        return responseOrder;
    }
    
    /**
     * Get response date.
     * @return The response date
     */
    public Date getDate() {
    	return date;
    }
    
    /**
     * Compare this Restaurant OpinionPoll with another Restaurant OpinionPoll
     * @param o
     * 			: the object to compare
     * @return true if the object to compare is equal to this CustomerRestaurantOpinionPoll, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof CustomerRestaurantOpinionPoll ) ) return false;
        CustomerRestaurantOpinionPoll customerRestaurantOpinionPoll = (CustomerRestaurantOpinionPoll) o;
        return this.getKey().equals(customerRestaurantOpinionPoll.getKey());
    }

}