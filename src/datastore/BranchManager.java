/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.PostalAddress;

import exceptions.InexistentObjectException;
import exceptions.InvalidFieldFormatException;
import exceptions.InvalidFieldSelectionException;
import exceptions.MissingRequiredFieldsException;

/**
 * This class is used to manage the GAE Datastore operations 
 * (get, put, delete, update) made on the Branch class.
 */
public class BranchManager {
	
	private static final Logger log = 
        Logger.getLogger(BranchManager.class.getName());

	/**
	 * Get a branch using its complex key (includes the Restaurant key as well)
	 * @param key
	 *        : The branch's key
	 * @return Branch 
	 */
	public static Branch getBranch(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Branch branch;
		try  {
			branch = pm.getObjectById(Branch.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return branch;
	}
	
	/**
     * Get ALL the Branches in the datastore from a specific restaurant
     * and returns them in a List structure
     * @param restaurantKey: 
     * 				the key of the restaurant whose branches will be retrieved
     * @return all branches in the datastore belonging to the given restaurant
     * TODO: Fix "touching" of branches
     */
	public static List<Branch> getRestaurantBranches(Key restaurantKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant restaurant = pm.getObjectById(Restaurant.class, restaurantKey);
		
        List<Branch> result = null;
        try {
            result = restaurant.getBranches();
            // Touch each branch
            for (Branch branch : result) {
            	branch.getBranchName();
            }
        } 
        finally {
        	pm.close();
        }

        return result;
    }
	
	/**
    * Add branch to a Restaurant.
    * Add a new branch in the datastore for this Restaurant.
    * @param email
    * 			: the email of the Restaurant where the branch will be added
    * @param branch
    * 			: the branch to be added
    */
	public static void putBranch(Email email, Branch branch) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Restaurant.class.getSimpleName(), email.getEmail());
			Restaurant restaurant = pm.getObjectById(Restaurant.class, key);
			tx.begin();
			restaurant.addBranch(branch);
			tx.commit();
			log.info("Branch \"" + branch.getBranchName() + "\" added to Restaurant \"" + 
					email.getEmail() + "\" in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Add branch to a Restaurant.
    * Add a new branch in the datastore for this Restaurant.
    * @param restaurantKey
    * 			: the key of the Restaurant where the branch will be added
    * @param branch
    * 			: the branch to be added
    */
	public static void putBranch(Key restaurantKey, Branch branch) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Restaurant restaurant = pm.getObjectById(Restaurant.class, restaurantKey);
			tx.begin();
			restaurant.addBranch(branch);
			tx.commit();
			log.info("Branch \"" + branch.getBranchName() + "\" added to Restaurant \"" + 
					restaurant.getRestaurantName() + "\" in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Delete branch.
    * Delete a branch in the datastore.
    * @param key
    * 			: the key of the branch to delete (includes Restaurant key)
    */
	public static void deleteBranch(Key key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Restaurant restaurant = pm.getObjectById(Restaurant.class, key.getParent());
			Branch branch = pm.getObjectById(Branch.class, key);
			String branchName = branch.getBranchName();
			tx.begin();
			restaurant.removeBranch(branch);
			tx.commit();
			log.info("Branch \"" + branchName + "\" deleted from Restaurant \"" + 
					restaurant.getUser().getUserEmail().getEmail() + "\" in datastore.");
		} 
		catch (InexistentObjectException e) {
			e.printStackTrace();
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Update Branch attributes.
    * Updates the given Branch's attributes in the datastore.
    * @param key
    * 			: the key of the Branch whose attributes will be updated
    * @param region
    * 			: the key of the region
    * @param branchName
    * 			: the new name to give to the Branch
    * @param branchAddress
    * 			: the new address to give to the Branch
    * @param branchPhone
    * 			: the new phone to give to the Branch
    * @param hasDelivery
    * 			: whether this branch has delivery service or not
    * @param hasRegularDelivery
    * 			: whether this branch has regular delivery or not
    * @param hasPostalDelivery
    * 			: whether this branch has postal delivery or not
    * @param hasUPSDelivery
    * 			: whether this branch has UPS delivery or not
    * @param hasConvenienceStoreDelivery
    * 			: whether this branch has convenience store delivery or not
    * @param hasTakeOut
    * 			: whether this branch has take-out service or not
    * @param hasTakeIn
    * 			: whether this branch has take-in service or not
    * @param branchEmail
    * 			: the contact email of this branch
	* @throws MissingRequiredFieldsException
	* @throws InvalidFieldFormatException
	* @throws InvalidFieldSelectionException 
    */
	public static void updateBranchAttributes(Key key, Long region, String branchName,
			PostalAddress branchAddress, PhoneNumber branchPhone, Boolean hasDelivery,
			Boolean hasRegularDelivery, Boolean hasPostalDelivery, Boolean hasUPSDelivery,
			Boolean hasConvenienceStoreDelivery, Boolean hasTakeOut, Boolean hasTakeIn,
			Email branchEmail)
			throws MissingRequiredFieldsException, InvalidFieldFormatException, 
			InvalidFieldSelectionException {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Branch branch = pm.getObjectById(Branch.class, key);
			tx.begin();
			branch.setRegion(region);
			branch.setBranchName(branchName);
			branch.setBranchAddress(branchAddress);
			branch.setBranchPhone(branchPhone);
			branch.setHasDelivery(hasDelivery);
			branch.setHasRegularDelivery(hasRegularDelivery);
			branch.setHasPostalDelivery(hasPostalDelivery);
			branch.setHasUPSDelivery(hasUPSDelivery);
			branch.setHasConvenienceStoreDelivery(hasConvenienceStoreDelivery);
			branch.setHasTakeOut(hasTakeOut);
			branch.setHasTakeIn(hasTakeIn);
			branch.setBranchEmail(branchEmail);
			tx.commit();
			log.info("Branch \"" + branchName + "\"'s attributes updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
}
