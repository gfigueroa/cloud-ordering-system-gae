/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import com.google.appengine.api.datastore.Key;

/**
 * This class is used to manage the GAE Datastore operations 
 * (get, put, delete, update) made on the TypeSetMenuItem class.
 */
public class TypeSetMenuItemManager {

	/**
	 * Get a typeSetMenuItem using its complex key
	 * @param key
	 *        : The typeSetMenuItem's key
	 * @return TypeSetMenuItem 
	 */
	public static TypeSetMenuItem getTypeSetMenuItem(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		TypeSetMenuItem typeSetMenuItem;
		try  {
			typeSetMenuItem = pm.getObjectById(TypeSetMenuItem.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return typeSetMenuItem;
	}

}
