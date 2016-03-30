/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents a simple version of customer smart cash.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class CustomerSmartCashSimple implements Serializable {
    
	public String customerKey;
	public Double currentBalance;
	public List<SmartCashTransactionSimple> transactions;
    
    /**
     * CustomerSimple constructor.
     * @param customerKey
     * 			: the customer key
     * @param currentBalance
     * 			: the current smart cash balance
     * @param transactions
     * 			: the list of smart cash transactions for this customer
     */
    public CustomerSmartCashSimple(String customerKey, Double currentBalance,
    		List<SmartCashTransactionSimple> transactions) {

    	this.customerKey = customerKey;
    	this.currentBalance = currentBalance;
    	this.transactions = transactions;
    }
    
    /**
     * Compare this customer smart cash simple with another
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this 
     * 			Customer smart cash simple, false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof CustomerSmartCashSimple ) ) return false;
        CustomerSmartCashSimple c = (CustomerSmartCashSimple) o;
        return this.customerKey.equals(c.customerKey);
    }
    
}
