/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;

/**
 * This class represents a simple version of the AdditionalPropertyValue table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class AdditionalPropertyValueSimple implements Serializable {

    public String key;
    public String additionalPropertyValueName;

    /**
     * AdditionalPropertyValueSimple constructor.
     * @param key:
     * 			: additionalPropertyValue key
     * @param additionalPropertyValueName
     * 			: additionalPropertyValue name
     */
    public AdditionalPropertyValueSimple(String key, String additionalPropertyValueName) {
    	
    	this.key = key;
    	this.additionalPropertyValueName = additionalPropertyValueName;
    }

    /**
     * Compare this additional property value with another additional property value
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this AdditionalPropertyValueSimple, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof AdditionalPropertyValueSimple ) ) return false;
        AdditionalPropertyValueSimple additionalPropertyValue = (AdditionalPropertyValueSimple) o;
        return key.equals(additionalPropertyValue.key);
    }

}