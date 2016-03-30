/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;

/**
 * This class represents a simple version of the AdditionalPropertyConstraint table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class AdditionalPropertyConstraintSimple implements Serializable {

    public String key;
    public String menuItemAdditionalPropertyValueKey1;
    public String menuItemAdditionalPropertyValueKey2;

    /**
     * AdditionalPropertyConstraintSimple constructor.
     * @param key
     * 			: additionalPropertyConstraint key
     * @param menuItemAdditionalPropertyValueKey1
     * 			: menuItemAdditionalPropertyValue1 key
     * @param menuItemAdditionalPropertyValueKey2
     * 			: menuItemAdditionalPropertyValue2 key
     */
    public AdditionalPropertyConstraintSimple(String key, 
    		String menuItemAdditionalPropertyValueKey1, 
    		String menuItemAdditionalPropertyValueKey2) {
    	
    	this.key = key;
    	this.menuItemAdditionalPropertyValueKey1 = menuItemAdditionalPropertyValueKey1;
    	this.menuItemAdditionalPropertyValueKey2 = menuItemAdditionalPropertyValueKey2;
    }

    /**
     * Compare this additional property constraint with another additional property constraint
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this AdditionalPropertyConstraintSimple, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof AdditionalPropertyConstraintSimple ) ) return false;
        AdditionalPropertyConstraintSimple additionalPropertyConstraint = (AdditionalPropertyConstraintSimple) o;
        return key.equals(additionalPropertyConstraint.key);
    }

}