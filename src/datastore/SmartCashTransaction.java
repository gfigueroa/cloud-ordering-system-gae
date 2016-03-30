/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import exceptions.InvalidFieldFormatException;
import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the SmartCashTransaction table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@PersistenceCapable
public class SmartCashTransaction {

	// Enumerator for smartCashTransaction type
	public static enum SmartCashTransactionType {
		DEBIT, CREDIT
	}
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long key;
    
    @Persistent
    private SmartCashTransactionType transactionType;

    @Persistent
    private Key customer;
    
    @Persistent
    private Long orderId;
    
    @Persistent
    private Date date;
    
    @Persistent
    private Double amount;

    /**
     * SmartCashTransaction constructor.
     * @param transactionType
     * 			: the type of transaction
     * @param customer
     * 			: customer key
     * @param orderId
     * 			: order Id (for DEBIT type)
     * @param amount
     * 			: the transaction amount
     * @throws MissingRequiredFieldsException
     * @throws InvalidFieldFormatException,
     */
    public SmartCashTransaction(SmartCashTransactionType transactionType,
    		Key customer, Long orderId, Double amount) 
    				throws MissingRequiredFieldsException {
        
    	// Check "required field" constraints
    	if (transactionType == null || customer == null || amount == null) {
    		throw new MissingRequiredFieldsException(this.getClass(),
    				"Exception in SmartCashTransaction constructor: " +
    				"one or more required fields are missing.");
    	}
    
    	// Check "required field" constraints for transaction types
    	if (transactionType == SmartCashTransactionType.DEBIT) {
    		if (orderId == null) {
    			throw new MissingRequiredFieldsException(this.getClass(),
        				"Exception in SmartCashTransaction constructor: " +
        				"one or more required fields are missing.");
    		}
    	}
    	
    	this.transactionType = transactionType;
    	this.customer = customer;
    	this.orderId = orderId;
    	this.date = new Date();
    	this.amount = amount;
    }

    /**
     * Get SmartCashTransaction key.
     * @return smartCashTransaction key
     */
    public Long getKey() {
        return key;
    }
    
    /**
     * Get transaction type
     * @return the type of transaction
     */
    public SmartCashTransactionType getTransactionType() {
    	return transactionType;
    }
    
    /**
     * Get the transaction type as a string
     * @return a string representation of the transaction type
     */
    public String getTransactionTypeString() {
    	switch (transactionType) {
    		case DEBIT:
    			return "Debit";
    		case CREDIT:
    			return "Credit";
    		default:
    			return null;
    	}
    }
    
    /**
     * Get transaction type from string.
     * @param transactionTypeString
     * 			: a string representation of the transaction type
     * @return the transaction type corresponding to the given string
     */
    public static SmartCashTransactionType getTransactionTypeFromString(
    		String transactionTypeString) {
    	
    	if (transactionTypeString == null) {
    		return null;
    	}
    	
    	if (transactionTypeString.equalsIgnoreCase("debit")) {
    		return SmartCashTransactionType.DEBIT;
    	}
    	else if (transactionTypeString.equalsIgnoreCase("credit")) {
    		return SmartCashTransactionType.CREDIT;
    	}
    	else {
    		return null;
    	}
    }
    
    /**
     * Get SmartCashTransaction customer.
     * @return customer who made the smartCashTransaction
     */
    public Key getCustomer() {
        return customer;
    }

	/**
	 * Get order id (for DEBIT type).
	 * @return the orderId
	 */
	public Long getOrderId() {
		return orderId;
	}

	/**
	 * Get the date of the transaction.
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Get the amount of the transaction.
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}
    
}