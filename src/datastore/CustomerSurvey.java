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
 * This class represents the CustomerSurvey table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class CustomerSurvey implements Serializable {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long key;
	
	@Persistent
    private Key customer;
	
	@Persistent
	private Key survey;
	
	@Persistent
	private Integer responseOrder;

    /**
     * CustomerSurvey constructor.
     * @param customer
     * 			: the customer key
     * @param survey
     * 			: the survey key
     * @param responseOrder
     * 			: the response order to this survey
     * @throws MissingRequiredFieldsException
     */
    public CustomerSurvey(Key customer, Key survey,
    		Integer responseOrder) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (customer == null || survey == null || responseOrder == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
    	this.customer = customer;
    	this.survey = survey;
    	this.responseOrder = responseOrder;
    }
    
    /**
     * Get CustomerSurvey key.
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
     * Get survey.
     * @return Survey key
     */
    public Key getSurvey() {
        return survey;
    }
    
    /**
     * Get response order.
     * @return The order in which the customer responded to 
     * 			this survey
     */
    public Integer getResponseOrder() {
        return responseOrder;
    }
    
    /**
     * Compare this Customer Survey with another Customer Survey
     * @param o
     * 			: the object to compare
     * @return true if the object to compare is equal to this Customer Survey, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof CustomerSurvey ) ) return false;
        CustomerSurvey customerSurvey = (CustomerSurvey) o;
        return this.getKey().equals(customerSurvey.getKey());
    }

}