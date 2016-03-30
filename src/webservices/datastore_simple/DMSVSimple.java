/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;

/**
 * This class represents a simple version of the Message table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class DMSVSimple implements Serializable {
    
	public Long key;
	public String dmsvReleaseDate;
	public String field1;
	public String field2;
	public String field3;
	public String field4;
	public String field5;
	public String field6;
	public String field7;
	public String field8;
	public String field9;
	public String field10;
	public String field11;
	public String field12;
	public String field13;
	public String field14;
	public String field15;
	public String field16;
    
    /**
     * MessageSimple constructor.
     * @param key
     * 			: DMSV key
     * @param dmsvReleaseDate
     * 			: the date this DMSV will be released
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
     */
    public DMSVSimple(Long key, String dmsvReleaseDate,
    		String field1, String field2,
    		String field3, String field4,
    		String field5, String field6,
    		String field7, String field8,
    		String field9, String field10,
    		String field11, String field12,
    		String field13, String field14,
    		String field15, String field16) {

    	this.key = key;
    	this.dmsvReleaseDate = dmsvReleaseDate;
    	this.field1 = field1;
    	this.field2 = field2;
    	this.field3 = field3;
    	this.field4 = field4;
    	this.field5 = field5;
    	this.field6 = field6;
    	this.field7 = field7;
    	this.field8 = field8;
    	this.field9 = field9;
    	this.field10 = field10;
    	this.field11 = field11;
    	this.field12 = field12;
    	this.field13 = field13;
    	this.field14 = field14;
    	this.field15 = field15;
    	this.field16 = field16;
    }
    
    /**
     * Compare this message with another Message
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this Message, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof DMSVSimple ) ) return false;
        DMSVSimple dmsv = (DMSVSimple) o;
        return this.key.equals(dmsv.key);
    }
    
}
