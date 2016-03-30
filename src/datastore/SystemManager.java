/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import exceptions.MissingRequiredFieldsException;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the System class.
 * 
 */

public class SystemManager {
	
	private static final Logger log = Logger.getLogger(SystemManager.class.getName());
	
	/**
     * Get System instance from the datastore.
     * @return The only System instance there should be
     */
	public static System getSystem() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        System system = null;
			
        try {
        	List<System> systems = SystemManager.getAllSystems();
        	// Create system if it hasn't been created yet
        	if (systems == null || systems.isEmpty()) {
        		system = new System();
        		pm.makePersistent(system);
        	}
        	else {
    			system = systems.get(0);
    		}
        } 
        finally {
        	pm.close();
        }

        return system;
    }
	
	/**
     * Get all System instances from the datastore.
     * @return All System instances
     * TODO: Make "touching" of systems more efficient
     */
	@SuppressWarnings("unchecked")
	public static List<System> getAllSystems() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(System.class);

        List<System> systems = null;
        try {
        	systems = (List<System>) query.execute();
            // touch all elements
            for (System s : systems)
                s.getSystemTime();
        } 
        finally {
        	pm.close();
            query.closeAll();
        }

        return systems;
    }
	
	/**
     * Get Restaurant List Version from the system.
     * @return restaurant list version
     */
	public static int getRestaurantListVersion() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        int restaurantListVersion = 0;
			
        try {
        	List<System> systems = SystemManager.getAllSystems();
    		if (systems != null && !systems.isEmpty()) {
    			restaurantListVersion = systems.get(0).getRestaurantListVersion();
    		}
        } 
        finally {
        	pm.close();
        }

        return restaurantListVersion;
    }
	
	/**
     * Get Restaurant Type List Version from the system.
     * @return Restaurant type list version
     */
	public static int getRestaurantTypeListVersion() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        int restaurantTypeListVersion = 0;
			
        try {
        	List<System> systems = SystemManager.getAllSystems();
    		if (systems != null && !systems.isEmpty()) {
    			restaurantTypeListVersion = systems.get(0).getRestaurantTypeListVersion();
    		}
        } 
        finally {
        	pm.close();
        }

        return restaurantTypeListVersion;
    }
	
	/**
    * Update Restaurant List Version.
    * Updates the restaurant list version (add 1)
    */
	public static void updateRestaurantListVersion() {	
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			List<System> systems = SystemManager.getAllSystems();
			System system = null;
			tx.begin();
			// Create system if it hasn't been created yet
			if (systems == null || systems.isEmpty()) {
				system = new System();
				pm.makePersistent(system);
			}
			else {
				system = pm.getObjectById(System.class, systems.get(0).getKey());
			}
			system.updateRestaurantListVersion();
			tx.commit();
			log.info("System \"" + system.getKey() + "\": Restaurant List Version updated to version " +
					system.getRestaurantListVersion() + " in the datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Update Store List Version.
    * Updates the store list version (add 1) of the given store super type
    * @param superStoreType
    * 			: the store super type version to update
    */
	public static void updateStoreListVersion(RestaurantType.StoreSuperType storeSuperType) {	
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			List<System> systems = SystemManager.getAllSystems();
			System system = null;
			tx.begin();
			// Create system if it hasn't been created yet
			if (systems == null || systems.isEmpty()) {
				system = new System();
				pm.makePersistent(system);
			}
			else {
				system = pm.getObjectById(System.class, systems.get(0).getKey());
			}
			system.updateStoreListVersion(storeSuperType);
			tx.commit();
			log.info("System \"" + system.getKey() + "\": Store List Version for " + 
					storeSuperType + " updated to version " +
					system.getStoreListVersion(storeSuperType) + " in the datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Update Store List Version.
    * Updates the store list version (add 1) of the 
    * store super type of the given store (restaurant)
    * @param store
    * 			: the store which contains the store super type whose version will be updated
    */
	public static void updateStoreListVersion(Restaurant store) {	
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		
		RestaurantType restaurantType = pm.getObjectById(
				RestaurantType.class, store.getRestaurantType());
		
		try {
			List<System> systems = SystemManager.getAllSystems();
			System system = null;
			tx.begin();
			// Create system if it hasn't been created yet
			if (systems == null || systems.isEmpty()) {
				system = new System();
				pm.makePersistent(system);
			}
			else {
				system = pm.getObjectById(System.class, systems.get(0).getKey());
			}
			system.updateStoreListVersion(restaurantType.getStoreSuperType());
			tx.commit();
			log.info("System \"" + system.getKey() + "\": Store List Version for " + 
					restaurantType.getStoreSuperType() + " updated to version " +
					system.getStoreListVersion(restaurantType.getStoreSuperType()) + 
					" in the datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Update Restaurant Type List Version.
    * Updates the restaurant type list version (add 1)
    */
	public static void updateRestaurantTypeListVersion() {	
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			List<System> systems = SystemManager.getAllSystems();
			System system = null;
			tx.begin();
			// Create system if it hasn't been created yet
			if (systems == null || systems.isEmpty()) {
				system = new System();
				pm.makePersistent(system);
			}
			else {
				system = pm.getObjectById(System.class, systems.get(0).getKey());
			}
			system.updateRestaurantTypeListVersion();
			tx.commit();
			log.info("System \"" + system.getKey() + "\": Restaurant Type List Version updated to version " +
					system.getRestaurantTypeListVersion() + " in the datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Update System attributes.
    * Update the different System variables in the datastore.
    * @param oldestAppVersionSupported
    * 			: the new oldestAppVersionSupported to be updated
	 * @throws MissingRequiredFieldsException 
    */
	public static void updateSystemAttributes(Integer oldestAppVersionSupported1,
			Integer oldestAppVersionSupported2, Integer oldestAppVersionSupported3) 
			throws MissingRequiredFieldsException {	
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			List<System> systems = SystemManager.getAllSystems();
			System system = null;
			tx.begin();
			// Create system if it hasn't been created yet
			if (systems == null || systems.isEmpty()) {
				system = new System();
				pm.makePersistent(system);
			}
			else {
				system = pm.getObjectById(System.class, systems.get(0).getKey());
			}
			system.setOldestAppVersionSupported1(oldestAppVersionSupported1);
			system.setOldestAppVersionSupported2(oldestAppVersionSupported2);
			system.setOldestAppVersionSupported3(oldestAppVersionSupported3);
			tx.commit();
			log.info("System \"" + system.getKey() + "\": oldestAppVersionSupported updated" +
					" in the datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
}

