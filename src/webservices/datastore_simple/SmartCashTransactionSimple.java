/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;

import datastore.SmartCashTransaction;

/**
 * This class represents a simple version of the SmartCashTransaction table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class SmartCashTransactionSimple implements Serializable {

    public Long key;
    public SmartCashTransaction.SmartCashTransactionType 
    		transactionType;
    public Long orderId;
    public String date;
    public Double amount;

    /**
     * MenuItemSimple constructor.
     * @param key
     * 			: smartCashTransaction key
     * @param transactionType
     * 			: smartCashTransaction type
     * @param orderId
     * 			: the order id associated to this transaction
     * @param date
     * 			: the date of the transaction
     * @param amount
     * 			: the amount for this transaction
     */
    public SmartCashTransactionSimple(Long key, 
    		SmartCashTransaction.SmartCashTransactionType transactionType,
    		Long orderId, String date, Double amount) {
    	
    	this.key = key;
    	this.transactionType = transactionType;
    	this.orderId = orderId;
    	this.date = date;
    	this.amount = amount;
    }

    /**
     * Compare this smartCashTransaction with another smartCashTransaction
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this SmartCashTransaction, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof SmartCashTransactionSimple ) ) return false;
        SmartCashTransactionSimple smartCashTransaction = (SmartCashTransactionSimple) o;
        return key.equals(smartCashTransaction.key);
    }

}