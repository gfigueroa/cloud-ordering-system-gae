/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.appengine.api.datastore.Key;

import exceptions.ObjectExistsInDatastoreException;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the SmartCashTransaction class.
 * 
 */

public class SmartCashTransactionManager {
	
	private static final Logger log = 
        Logger.getLogger(SmartCashTransactionManager.class.getName());
	
	/**
     * Get a SmartCashTransaction instance from the datastore given the SmartCashTransaction key.
     * @param key
     * 			: the SmartCashTransaction's key
     * @return SmartCashTransaction instance, null if SmartCashTransaction is not found
     */
	public static SmartCashTransaction getSmartCashTransaction(Long key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		SmartCashTransaction smartCashTransaction;
		try  {
			smartCashTransaction = pm.getObjectById(SmartCashTransaction.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return smartCashTransaction;
	}

	/**
     * Get ALL the SmartCashTransactions in the database for a specific
     * customer and return them in a List structure
     * @param customerKey:
     * 				the key of the customer whose smartCashTransactions will be retrieved
     * @return all smartCashTransactions in the datastore corresponding to the given customer key
     * TODO: Make more efficient "touching" of the smartCashTransactions
     */
    @SuppressWarnings("unchecked")
	public static List<SmartCashTransaction> getAllSmartCashTransactionsFromCustomer(
			Key customerKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();

        Query query = pm.newQuery(SmartCashTransaction.class);
    	query.setFilter("customer == customerkey");
    	query.setOrdering("date desc");
        query.declareParameters(Key.class.getName() + " customerkey");
        
        try {
        	List<SmartCashTransaction> allSmartCashTransactions = 
        			(List<SmartCashTransaction>) query.execute(customerKey);
        	
        	// Touch transactions
        	for (SmartCashTransaction smartCashTransaction : 
        		allSmartCashTransactions) {
        		smartCashTransaction.getKey();
        	}

        	return allSmartCashTransactions;
        }
        finally {
        	pm.close();
            query.closeAll();
        }
    }
    
    /**
     * Get the current smart cash balance (credits - debits) from a customer.
     * @param customerKey:
     * 				the key of the customer whose smartCashTransactions will be retrieved
     * @return the smart cash balance of the given customer
     */
    public static Double getSmartCashBalanceFromCustomer(
			Key customerKey) {
		
    	List<SmartCashTransaction> transactions = 
    			getAllSmartCashTransactionsFromCustomer(customerKey);
    	Double balance = 0.0;
    	
    	for (SmartCashTransaction transaction : transactions) {
    		if (transaction.getTransactionType() == 
    				SmartCashTransaction.SmartCashTransactionType.DEBIT) {
    			balance -= transaction.getAmount();
    		}
    		else {
    			balance += transaction.getAmount();
    		}
    	}
    	
    	return balance;
    	
    }
	
	/**
     * Put SmartCashTransaction into datastore.
     * SmartCashTransactions the given SmartCashTransaction instance in the datastore calling the 
     * PersistenceManager's makePersistent() method.
     * @param smartCashTransaction
     * 			: the SmartCashTransaction instance to store
     * @throws ObjectExistsInDatastoreException 
     */
	public static void putSmartCashTransaction(SmartCashTransaction smartCashTransaction) 
           throws ObjectExistsInDatastoreException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.makePersistent(smartCashTransaction);
			tx.commit();
			log.info("SmartCashTransaction \"" + smartCashTransaction.getKey() + 
				"\" stored successfully in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Delete SmartCashTransaction from datastore.
    * Deletes the given SmartCashTransaction from the datastore calling the PersistenceManager's
    * deletePersistent() method.
    * @param smartCashTransaction
    * 			: the SmartCashTransaction instance to delete
    */
	public static void deleteSmartCashTransaction(Long smartCashTransactionKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			SmartCashTransaction smartCashTransaction = 
					pm.getObjectById(SmartCashTransaction.class, smartCashTransactionKey);
			tx.begin();
			pm.deletePersistent(smartCashTransaction);
			tx.commit();
			log.info("SmartCashTransaction \"" + 
			smartCashTransactionKey + "\" deleted successfully from datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

}
