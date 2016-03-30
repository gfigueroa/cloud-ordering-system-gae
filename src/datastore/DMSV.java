/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the DMSV table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class DMSV implements Serializable {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long key;
    
    @Persistent
    private Date dmsvCreationDate;
    
    @Persistent
    private Date dmsvReleaseDate;
    
    @Persistent
    private Text field1;
    
    @Persistent
    private Text field2;
    
    @Persistent
    private Text field3;
    
    @Persistent
    private Text field4;
    
    @Persistent
    private Text field5;
    
    @Persistent
    private Text field6;
    
    @Persistent
    private Text field7;
    
    @Persistent
    private Text field8;
    
    @Persistent
    private Text field9;
    
    @Persistent
    private Text field10;
    
    @Persistent
    private Text field11;
    
    @Persistent
    private Text field12;
    
    @Persistent
    private Text field13;
    
    @Persistent
    private Text field14;
    
    @Persistent
    private Text field15;
    
    @Persistent
    private Text field16;

    /**
     * DMSV constructor.
     * @param dmsvReleaseDate
     * 			: the date this dmsv will start to be available
     * @throws MissingRequiredFieldsException
     */
    public DMSV(Date dmsvReleaseDate, Text field1,
    		Text field2, Text field3,
    		Text field4, Text field5,
    		Text field6, Text field7,
    		Text field8, Text field9,
    		Text field10, Text field11,
    		Text field12, Text field13,
    		Text field14, Text field15,
    		Text field16) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (dmsvReleaseDate == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}

        this.dmsvCreationDate = new Date();
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
     * Get DMSV key.
     * @return DMSV key
     */
    public Long getKey() {
        return key;
    }
    
    /**
     * Get the date this dmsv was created
     * @return The date this dmsv was created
     */
    public Date getDMSVCreationDate() {
        return dmsvCreationDate;
    }
    
    /**
     * Get the date when this dmsv will be available
     * @return The date when this dmsv will be available
     */
    public Date getDMSVReleaseDate() {
        return dmsvReleaseDate;
    }
    
    /**
     * Compare this DMSV with another DMSV
     * @param o
     * 			: the object to compare
     * @return true if the object to compare is equal to this DMSV, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof DMSV ) ) return false;
        DMSV dmsv = (DMSV) o;
        return this.getKey().equals(dmsv.getKey());
    }
    
    /**
     * Set DMSV Release Date.
     * @param dmsvReleaseDate
     * 			: the date this dmsv will be available
     * @throws MissingRequiredFieldsException
     */
    public void setDMSVReleaseDate(Date dmsvReleaseDate)
    		throws MissingRequiredFieldsException {
    	if (dmsvReleaseDate == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"DMSV release date is missing.");
    	}
    	this.dmsvReleaseDate = dmsvReleaseDate;
    }

	/**
	 * @return the field1
	 */
	public Text getField1() {
		return field1;
	}

	/**
	 * @param field1 the field1 to set
	 */
	public void setField1(Text field1) {
		this.field1 = field1;
	}

	/**
	 * @return the field2
	 */
	public Text getField2() {
		return field2;
	}

	/**
	 * @param field2 the field2 to set
	 */
	public void setField2(Text field2) {
		this.field2 = field2;
	}

	/**
	 * @return the field3
	 */
	public Text getField3() {
		return field3;
	}

	/**
	 * @param field3 the field3 to set
	 */
	public void setField3(Text field3) {
		this.field3 = field3;
	}

	/**
	 * @return the field4
	 */
	public Text getField4() {
		return field4;
	}

	/**
	 * @param field4 the field4 to set
	 */
	public void setField4(Text field4) {
		this.field4 = field4;
	}

	/**
	 * @return the field5
	 */
	public Text getField5() {
		return field5;
	}

	/**
	 * @param field5 the field5 to set
	 */
	public void setField5(Text field5) {
		this.field5 = field5;
	}

	/**
	 * @return the field6
	 */
	public Text getField6() {
		return field6;
	}

	/**
	 * @param field6 the field6 to set
	 */
	public void setField6(Text field6) {
		this.field6 = field6;
	}

	/**
	 * @return the field7
	 */
	public Text getField7() {
		return field7;
	}

	/**
	 * @param field7 the field7 to set
	 */
	public void setField7(Text field7) {
		this.field7 = field7;
	}

	/**
	 * @return the field8
	 */
	public Text getField8() {
		return field8;
	}

	/**
	 * @param field8 the field8 to set
	 */
	public void setField8(Text field8) {
		this.field8 = field8;
	}

	/**
	 * @return the field9
	 */
	public Text getField9() {
		return field9;
	}

	/**
	 * @param field9 the field9 to set
	 */
	public void setField9(Text field9) {
		this.field9 = field9;
	}

	/**
	 * @return the field10
	 */
	public Text getField10() {
		return field10;
	}

	/**
	 * @param field10 the field10 to set
	 */
	public void setField10(Text field10) {
		this.field10 = field10;
	}

	/**
	 * @return the field11
	 */
	public Text getField11() {
		return field11;
	}

	/**
	 * @param field11 the field11 to set
	 */
	public void setField11(Text field11) {
		this.field11 = field11;
	}

	/**
	 * @return the field12
	 */
	public Text getField12() {
		return field12;
	}

	/**
	 * @param field12 the field12 to set
	 */
	public void setField12(Text field12) {
		this.field12 = field12;
	}

	/**
	 * @return the field13
	 */
	public Text getField13() {
		return field13;
	}

	/**
	 * @param field13 the field13 to set
	 */
	public void setField13(Text field13) {
		this.field13 = field13;
	}

	/**
	 * @return the field14
	 */
	public Text getField14() {
		return field14;
	}

	/**
	 * @param field14 the field14 to set
	 */
	public void setField14(Text field14) {
		this.field14 = field14;
	}

	/**
	 * @return the field15
	 */
	public Text getField15() {
		return field15;
	}

	/**
	 * @param field15 the field15 to set
	 */
	public void setField15(Text field15) {
		this.field15 = field15;
	}

	/**
	 * @return the field16
	 */
	public Text getField16() {
		return field16;
	}

	/**
	 * @param field16 the field16 to set
	 */
	public void setField16(Text field16) {
		this.field16 = field16;
	}
}