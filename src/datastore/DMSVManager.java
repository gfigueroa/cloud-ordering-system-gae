/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.logging.Logger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.appengine.api.datastore.Text;

import util.DateManager;

import exceptions.MissingRequiredFieldsException;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the DMSV class.
 * 
 */

public class DMSVManager {
	
	private static final Logger log = 
        Logger.getLogger(DMSVManager.class.getName());
	
	/**
     * Get a DMSV instance from the datastore given the DMSV key.
     * @param key
     * 			: the DMSV's key
     * @return DMSV instance, null if DMSV is not found
     */
	public static DMSV getDMSV(Long key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		DMSV dmsv;
		try  {
			dmsv = pm.getObjectById(DMSV.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return dmsv;
	}

	/**
	 * Get all DMSVs corresponding to the given month and year.
	 * Month values go from 0 (January) to 11 (December).
	 * @param month:
	 * 				the month corresponding to the desired DMSVs
	 * @param year:
	 * 				the year corresponding to the desired DMSVs
	 * @return a list of DMSVs corresponding to the given month and year
	 * TODO: fix touching of DMSVs
	 */
	@SuppressWarnings("unchecked")
	public static List<DMSV> getDMSVsInMonth(int month, int year) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Date startDate = DateManager.getDateValue(
				String.valueOf(month + 1) + "/1/" + year + " 00:00:01");
		Date endDate = DateManager.getDateValue(
				String.valueOf(month + 1) + "/" + 
				DateManager.getLastDayOfMonth(month, year) + 
				"/" + year + " 23:59:59");
		
        Query query = pm.newQuery(DMSV.class);
    	query.setFilter("dmsvReleaseDate >= startDate && " +
    			"dmsvReleaseDate <= endDate");
    	query.setOrdering("dmsvReleaseDate asc");
        query.declareParameters(Date.class.getName() + " startDate, " +
        		Date.class.getName() + " endDate");
        
        try {
        	List<DMSV> dmsvList = (List<DMSV>) query.execute(startDate, endDate);
        	
        	// Touch DMSVs
        	for (DMSV dmsv : dmsvList) {
        		dmsv.getKey();
        	}
        	
        	return dmsvList;
        }
        finally {
        	pm.close();
            query.closeAll();
        }
	}
	
	/**
	 * Get all DMSVs corresponding to the given date.
	 * @param date:
	 * 				the date corresponding to the desired DMSVs
	 * @return a list of DMSVs corresponding to the given date
	 * TODO: fix touching of DMSVs
	 */
	@SuppressWarnings("unchecked")
	public static List<DMSV> getDMSVsInDate(Date date) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);
		
		Date startDate = DateManager.getDateValue(
				String.valueOf(month + 1) + "/" +
				day + "/" + 
				year + 
				" 00:00:01");
		Date endDate = DateManager.getDateValue(
				String.valueOf(month + 1) + "/" + 
				day + "/" + 
				year + " 23:59:59");
		
        Query query = pm.newQuery(DMSV.class);
    	query.setFilter("dmsvReleaseDate >= startDate && " +
    			"dmsvReleaseDate <= endDate");
    	query.setOrdering("dmsvReleaseDate desc");
        query.declareParameters(Date.class.getName() + " startDate, " +
        		Date.class.getName() + " endDate");
        
        try {
        	List<DMSV> dmsvList = (List<DMSV>) query.execute(startDate, endDate);
        	
        	// Touch DMSVs
        	for (DMSV dmsv : dmsvList) {
        		dmsv.getKey();
        	}
        	
        	return dmsvList;
        }
        finally {
        	pm.close();
            query.closeAll();
        }
	}
	
	/**
     * Put DMSV into datastore.
     * Stores the given DMSV instance in the datastore.
     * @param dmsv
     * 			: the DMSV instance to store
     */
	public static void putDMSV(DMSV dmsv) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.makePersistent(dmsv);
			tx.commit();
			log.info("DMSV \"" + dmsv.getDMSVReleaseDate() + 
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
    * Delete DMSV from datastore.
    * Deletes the DMSV corresponding to the given key
    * from the datastore calling the PersistenceManager's 
    * deletePersistent() method.
    * @param key
    * 			: the key of the DMSV instance to delete
    */
	public static void deleteDMSV(Long key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			DMSV dmsv = pm.getObjectById(DMSV.class, key);
			Date dmsvContent = dmsv.getDMSVReleaseDate();
			tx.begin();
			pm.deletePersistent(dmsv);
			tx.commit();
			log.info("DMSV \"" + dmsvContent + 
                     "\" deleted successfully from datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
    * Update DMSV attributes.
    * Update's the given DMSV's attributes in the datastore.
    * @param key
    * 			: the key of the DMSV whose attributes will be updated
    * @param field1
    * @param field2
    * @param field3
    * @param field4
    * @param field5
    * @param field6
    * @param field7
    * @param field8
    * @param field9
    * @param field10
    * @param field11
    * @param field12
    * @param field13
    * @param field14
    * @param field15
    * @param field16
    * @param dmsvReleaseDate
    * 			: the starting date of this dmsv
	* @throws MissingRequiredFieldsException 
    */
	public static void updateDMSVAttributes(
			Long key, Date dmsvReleaseDate,
			Text field1, Text field2,
			Text field3, Text field4,
			Text field5, Text field6,
			Text field7, Text field8,
			Text field9, Text field10,
			Text field11, Text field12,
			Text field13, Text field14,
			Text field15, Text field16) 
                       throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			DMSV dmsv = pm.getObjectById(DMSV.class, key);
			tx.begin();
			dmsv.setDMSVReleaseDate(dmsvReleaseDate);
			dmsv.setField1(field1);
			dmsv.setField2(field2);
			dmsv.setField3(field3);
			dmsv.setField4(field4);
			dmsv.setField5(field5);
			dmsv.setField6(field6);
			dmsv.setField7(field7);
			dmsv.setField8(field8);
			dmsv.setField9(field9);
			dmsv.setField10(field10);
			dmsv.setField11(field11);
			dmsv.setField12(field12);
			dmsv.setField13(field13);
			dmsv.setField14(field14);
			dmsv.setField15(field15);
			dmsv.setField16(field16);
			tx.commit();
			log.info("DMSV \"" + dmsv.getDMSVReleaseDate() + 
                     "\"'s attributes updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
}
