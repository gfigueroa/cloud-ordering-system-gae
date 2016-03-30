/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;

/**
 * This class represents a simple version of the MenuItemAdditionalPropertyValue table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class MenuItemAdditionalPropertyValueSimple implements Serializable {

    public String key;
    public String additionalPropertyValueKey;
    public Double additionalCharge;

    /**
     * MenuItemAdditionalPropertyValueSimple constructor.
     * @param key:
     * 			: menuItemAdditionalPropertyValue key
     * @param additionalPropertyValueKey
     * 			: AdditionalPropertyValue key
     * @param additionalCharge
     * 			: additional charge
     */
    public MenuItemAdditionalPropertyValueSimple(String key, String additionalPropertyValueKey,
    		Double additionalCharge) {
    	
    	this.key = key;
    	this.additionalPropertyValueKey = additionalPropertyValueKey;
    	this.additionalCharge = additionalCharge;
    }

    /**
     * Compare this menu item additional property value with another 
     * menu item additional property value
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this MenuItemAdditionalPropertyValueSimple, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof MenuItemAdditionalPropertyValueSimple ) ) return false;
        MenuItemAdditionalPropertyValueSimple menuItemAdditionalPropertyValue = (MenuItemAdditionalPropertyValueSimple) o;
        return key.equals(menuItemAdditionalPropertyValue.key);
    }

}