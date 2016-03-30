/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.external_resources;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import util.DateManager;
import webservices.datastore_simple.CustomerSmartCashSimple;
import webservices.datastore_simple.SmartCashTransactionSimple;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.KeyFactory;

import datastore.Customer;
import datastore.CustomerManager;
import datastore.SmartCashTransaction;
import datastore.SmartCashTransactionManager;

/**
 * This class represents the list of smartCashTransactions from a customer
 * as a Resource with only one representation
 */

public class CustomerSmartCashResource extends ServerResource {
	
	private static final Logger log = 
	        Logger.getLogger(CustomerSmartCashResource.class.getName());

	/**
	 * Returns the customer smart cash transactions as a JSON object.
	 * @return the customer smart cash balance and list of transactions.
	 */
    @Get("json")
    public CustomerSmartCashSimple toJson() {
    	
    	String queryInfo = (String) getRequest().getAttributes()
                .get("queryinfo");
            
    	int indexOfSecondParameter = queryInfo.indexOf('&');
    	String customerEmailString = "";
    	String customerPassword = "";
    	
    	// If no customer email or password are given
    	if (indexOfSecondParameter == -1) {
    		return null;
    	}
    	else {
    		char searchByAttribute1 = queryInfo.charAt(0);
        	String searchByValue1 = queryInfo.substring(2, queryInfo.indexOf("&"));
        	char searchByAttribute2 = queryInfo.charAt(queryInfo.indexOf("&") + 1);
        	String searchByValue2 = queryInfo.substring(queryInfo.indexOf("&") + 3);
        	
        	log.info(searchByAttribute1 + "=" + searchByValue1 +
        			" --- " + searchByAttribute2 + "=" + searchByValue2);

        	if (Character.toLowerCase(searchByAttribute1) == 'c') {
        		customerEmailString = searchByValue1;
        	}
        	else if (Character.toLowerCase(searchByAttribute1) == 'p') {
        		customerPassword = searchByValue1;
        	}
        	else {
        		return null;
        	}
        	
        	if (Character.toLowerCase(searchByAttribute2) == 'c') {
        		customerEmailString = searchByValue2;
        	}
        	else if (Character.toLowerCase(searchByAttribute2) == 'p') {
        			customerPassword = searchByValue2;
        	}
        	else {
        		return null;
        	}
    	}
    	
    	// Check if user and password are correct
    	Email customerEmail = new Email(customerEmailString);
        Customer customer = CustomerManager.getCustomer(customerEmail);
        if (!customer.getUser().validateUser(customerEmail, customerPassword)) {
            return null;
        }
    	
        // Get the smart cash transactions
        ArrayList<SmartCashTransactionSimple> simpleTransactions =
        		new ArrayList<SmartCashTransactionSimple>();
        List<SmartCashTransaction> transactions = 
        		SmartCashTransactionManager.getAllSmartCashTransactionsFromCustomer(
        				customer.getKey());
        for (SmartCashTransaction transaction : transactions) {
        	SmartCashTransactionSimple simpleTransaction =
        			new SmartCashTransactionSimple(
        					transaction.getKey(),
        					transaction.getTransactionType(),
        					transaction.getOrderId() != null ?
        							transaction.getOrderId() : 0,
        					DateManager.printDateAsString(transaction.getDate()),
        					transaction.getAmount()
        					);
        	
        	simpleTransactions.add(simpleTransaction);
        }
        
        // Get other fields
        CustomerSmartCashSimple customerSmartCashSimple = 
        		new CustomerSmartCashSimple(
        				KeyFactory.keyToString(customer.getKey()),
        				SmartCashTransactionManager.getSmartCashBalanceFromCustomer(
        						customer.getKey()),
        				simpleTransactions
        				);
        
        return customerSmartCashSimple;
    }

}