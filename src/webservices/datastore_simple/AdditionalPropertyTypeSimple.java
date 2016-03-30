/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a simple version of the AdditionalPropertyType table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class AdditionalPropertyTypeSimple implements Serializable {

    public String key;
    public String additionalPropertyTypeName;
    public ArrayList<AdditionalPropertyValueSimple> additionalPropertyValues;

    /**
     * AdditionalPropertyTypeSimple constructor.
     * @param key:
     * 			: additionalPropertyType key
     * @param additionalPropertyTypeName
     * 			: additionalPropertyType name
     * @param additionalPropertyValues
     * 			: additionalPropertyValue list
     */
    public AdditionalPropertyTypeSimple(String key, String additionalPropertyTypeName, 
    		ArrayList<AdditionalPropertyValueSimple> additionalPropertyValues) {
    	
    	this.key = key;
    	this.additionalPropertyTypeName = additionalPropertyTypeName;
    	this.additionalPropertyValues = additionalPropertyValues;
    }

    /**
     * Compare this additional property type with another additional property type
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this AdditionalPropertyTypeSimple, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof AdditionalPropertyTypeSimple ) ) return false;
        AdditionalPropertyTypeSimple additionalPropertyType = (AdditionalPropertyTypeSimple) o;
        return key.equals(additionalPropertyType.key);
    }

}