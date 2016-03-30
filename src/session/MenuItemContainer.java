/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package session;

import java.io.Serializable;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.KeyFactory;

import datastore.MenuItem;
    
/**
 * This class is used as a temporary container for Menu Items
 * in the Customer shopping cart. It is not stored in the
 * datastore and its information is lost when a session is destroyed.
 * 
 */

@SuppressWarnings("serial")
public class MenuItemContainer implements Serializable {
    
	private static final Logger log = 
	        Logger.getLogger(MenuItemContainer.class.getName());
	
	public MenuItem menuItem;
    public int qty;
    
    /**
     * Default MenuItemContainer constructor.
     */
    public MenuItemContainer() {
        menuItem = null;
        qty = 0;
    }
    
    /**
     * MenuItemContainer constructor.
     * @param menuItem
     * 			: the menu item stored
     */
    public MenuItemContainer(MenuItem menuItem) {
        this.menuItem = menuItem;
        qty = 1;
    }
    
    /**
     * Compare this menuItem with another MenuItem or MenuItemContainer
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this MenuItem, 
	   *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        
    	if ( this == o ) {
        	return true;
        }
        if ( !(o instanceof MenuItem) && !(o instanceof MenuItemContainer) ) {
        	return false;
        }
        
        MenuItem menuItem = o instanceof MenuItem ? (MenuItem) o : ((MenuItemContainer) o).menuItem;
        log.info("are they equal?" + 
        		KeyFactory.keyToString(this.menuItem.getKey()).equals(KeyFactory.keyToString(menuItem.getKey())));
        
        return KeyFactory.keyToString(this.menuItem.getKey())
                .equals(KeyFactory.keyToString(menuItem.getKey()));
    }
}
