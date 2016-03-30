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
 * This class represents the CustomerSurveyOpinionPoll table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class CustomerSurveyOpinionPoll implements Serializable {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long key;
	
	@Persistent
    private Key customer;
	
	@Persistent
	private Key surveyOpinionPoll;
	
	@Persistent
	private String response;
	
	@Persistent
	private Integer responseOrder;
	
	@Persistent
	private Date date;

    /**
     * CustomerSurveyOpinionPoll constructor.
     * @param customer
     * 			: the customer key
     * @param surveyOpinionPoll
     * 			: the survey opinion poll key
     * @param response
     * 			: the response to this opinion poll
     * @param responseOrder
     * 			: the response order
     * @throws MissingRequiredFieldsException
     */
    public CustomerSurveyOpinionPoll(Key customer, Key surveyOpinionPoll,
    		String response, Integer responseOrder) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (customer == null || surveyOpinionPoll == null || response == null ||
    			response.trim().isEmpty() || responseOrder == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
    	this.customer = customer;
    	this.surveyOpinionPoll = surveyOpinionPoll;
    	this.response = response;
    	this.responseOrder = responseOrder;
    	this.date = new Date();
    }
    
    /**
     * Get CustomerSurveyOpinionPoll key.
     * @return Survey OpinionPoll key
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
     * Get survey opinion poll.
     * @return Survey opinion poll key
     */
    public Key getSurveyOpinionPoll() {
        return surveyOpinionPoll;
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
     * Compare this Survey OpinionPoll with another Survey OpinionPoll
     * @param o
     * 			: the object to compare
     * @return true if the object to compare is equal to this CustomerSurveyOpinionPoll, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof CustomerSurveyOpinionPoll ) ) return false;
        CustomerSurveyOpinionPoll customerSurveyOpinionPoll = (CustomerSurveyOpinionPoll) o;
        return this.getKey().equals(customerSurveyOpinionPoll.getKey());
    }

}