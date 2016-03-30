/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;

/**
 * This class represents a simple version of the TypeSetMenuItem table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class TypeSetMenuItemSimple implements Serializable {

    public String key;
    public String menuItemKey;
    public Double price;

    /**
     * TypeSetMenuItemSimple constructor.
     * @param key:
     * 			: typeSetMenuItem key
     * @param menuItemKeyh
     * 			: the menu item key
     * @param price
     * 			: the price
     */
    public TypeSetMenuItemSimple(String key, String menuItemKey,
    		Double price) {
    	
    	this.key = key;
    	this.menuItemKey = menuItemKey;
    	this.price = price;
    }

    /**
     * Compare this type set menu item with another type set menu item
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this TypeSetMenuItem, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof TypeSetMenuItemSimple ) ) return false;
        TypeSetMenuItemSimple typeSetMenuItem = (TypeSetMenuItemSimple) o;
        return key.equals(typeSetMenuItem.key);
    }

}