/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;

import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.PostalAddress;

/**
 * This class represents a simple version of the Branch table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class BranchSimple implements Serializable {
    
	public String key;
	public String regionName;
	public String branchName;
	public PostalAddress branchAddress;
	public PhoneNumber branchPhone;
	public Boolean hasDelivery;
	public Boolean hasRegularDelivery;
	public Boolean hasPostalDelivery;
	public Boolean hasUPSDelivery;
	public Boolean hasConvenienceStoreDelivery;
	public Boolean hasTakeOut;
	public Boolean hasTakeIn;
    
    /**
     * BranchSimple constructor.
     * @param branchKey
     * 			: branch key
     * @param regionName
     * 			: branch region name
     * @param branchName
     * 			: branch name
     * @param branchAddress
     * 			: branch address
     * @param branchPhone
     * 			: branch phone
     * @param hasDelivery
     * 			: branch has delivery
     * @param hasRegularDelivery
     * 			: branch has regular delivery
     * @param hasPostalDelivery
     * 			: branch has postal delivery
     * @param hasUPSDelivery
     * 			: branch has UPS delivery
     * @param hasConvenienceStoreDelivery
     * 			: branch has convenience store delivery
     * @param hasTakeOut
     * 			: branch has take-out
     * @param hasTakeIn
     * 			: branch has take-in
     */
    public BranchSimple(String key, String regionName, String branchName, 
    		PostalAddress branchAddress, PhoneNumber branchPhone, 
    		Boolean hasDelivery, Boolean hasRegularDelivery, Boolean hasPostalDelivery,
    		Boolean hasUPSDelivery, Boolean hasConvenienceStoreDelivery,
    		Boolean hasTakeOut, Boolean hasTakeIn) {

    	this.key = key;
    	this.regionName = regionName;
    	this.branchName = branchName;
        this.branchAddress = branchAddress;
        this.branchPhone = branchPhone;
        this.hasDelivery = hasDelivery;
        this.hasRegularDelivery = hasRegularDelivery;
        this.hasPostalDelivery = hasPostalDelivery;
        this.hasUPSDelivery = hasUPSDelivery;
        this.hasConvenienceStoreDelivery = hasConvenienceStoreDelivery;
        this.hasTakeOut = hasTakeOut;
        this.hasTakeIn = hasTakeIn;
    }
    
    /**
     * Compare this branch with another Branch
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this Branch, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof BranchSimple ) ) return false;
        BranchSimple r = (BranchSimple) o;
        return this.key.equals(r.key);
    }
    
}
