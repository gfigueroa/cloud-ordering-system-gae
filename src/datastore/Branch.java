/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.PostalAddress;

import datastore.Order.OrderType;

import exceptions.InvalidFieldFormatException;
import exceptions.InvalidFieldSelectionException;
import exceptions.MissingRequiredFieldsException;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import util.FieldValidator;

/**
 * This class represents the Branch table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class Branch implements Serializable {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
	@Persistent
    private Long region;
	
    @Persistent
    private String branchName;
    
    @Persistent
    private PostalAddress branchAddress;
    
    @Persistent
    private PhoneNumber branchPhone;

    @Persistent
    private Boolean hasDelivery;
    
    @Persistent
    private Boolean hasRegularDelivery;
    
    @Persistent
    private Boolean hasPostalDelivery;
    
    @Persistent
    private Boolean hasUPSDelivery;
    
    @Persistent
    private Boolean hasConvenienceStoreDelivery;
    
    @Persistent
    private Boolean hasTakeOut;
    
    @Persistent
    private Boolean hasTakeIn;
    
    @Persistent
    private Email branchEmail;
    
    @Persistent
    private Date branchTime;

    /**
     * Branch constructor.
     * @param region
     * 			: region key
     * @param branchName
     * 			: branch name
     * @param branchAddress
     * 			: branch address
     * @param branchPhone
     * 			: branch phone
     * @param hasDelivery
     * 			: whether or not the branch has delivery service
     * @param hasRegularDelivery
     * 			: whether or not the branch has regular delivery
     * @param hasPostalDelivery
     * 			: whether or not the branch has postal service delivery
     * @param hasUPSDelivery
     * 			: whether or not the branch has UPS service delivery
     * @param hasConvenienceStoreDelivery
     * 			: whether or not the branch has convenience store delivery
     * @param hasTakeOut
     * 			: whether or not the branch has take-out service
     * @param hasTakeIn
     * 			: whether or not the branch has take-in service
     * @param branchEmail
     * 			: the contact email for this branch
     * @throws MissingRequiredFieldsException
     * @throws InvalidFieldFormatException
     * @throws InvalidFieldSelectionException 
     */
    public Branch(Long region, String branchName, PostalAddress branchAddress, PhoneNumber branchPhone,
    		Boolean hasDelivery, Boolean hasRegularDelivery, Boolean hasPostalDelivery,
    		Boolean hasUPSDelivery, Boolean hasConvenienceStoreDelivery, Boolean hasTakeOut, 
    		Boolean hasTakeIn, Email branchEmail) 
		throws MissingRequiredFieldsException, InvalidFieldFormatException, InvalidFieldSelectionException {
        
    	// Check "required field" constraints
    	if (region == null || branchName == null || branchAddress == null || branchPhone == null ||
    			hasDelivery == null || hasTakeOut == null || hasTakeIn == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	if (branchName.trim().isEmpty() || branchAddress.getAddress().trim().isEmpty() ||
    			branchPhone.getNumber().trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
    	// Check phone number format
    	if (!FieldValidator.isValidPhoneNumber(branchPhone.getNumber())) {
    		throw new InvalidFieldFormatException(this.getClass(), 
    				"Invalid phone number.");
    	}
    	// Check email format
    	if (!FieldValidator.isValidEmailAddress(branchEmail.getEmail())) {
    		throw new InvalidFieldFormatException(this.getClass(), 
    				"Invalid e-mail address.");
    	}
    	
    	// Check available order type services
    	if (hasDelivery == false && hasTakeOut == false && hasTakeIn == false) {
    		throw new InvalidFieldSelectionException(this.getClass(), 
    				"The three available order types may not all be false.");
    	}
    	
    	// Check required field constraints for delivery types
    	if (hasDelivery == true) {
    		if (hasRegularDelivery == null || hasPostalDelivery == null ||
    				hasUPSDelivery == null || hasConvenienceStoreDelivery == null) {
    			throw new MissingRequiredFieldsException(this.getClass(), 
        				"One or more required fields are missing.");
    		}
    	}

    	this.region = region;
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
    	this.branchEmail = branchEmail;
    	
    	Date now = new Date();
    	this.branchTime = now;
    }

    /**
     * Get Branch key.
     * @return branch key
     */
    public Key getKey() {
        return key;
    }
    
    /**
     * Get Branch region.
     * @return branch region key
     */
    public Long getRegion() {
        return region;
    }
    
    /**
     * Get Branch name.
     * @return branch name
     */
    public String getBranchName() {
        return branchName;
    }
    
    /**
     * Get Branch address.
     * @return branch address
     */
    public PostalAddress getBranchAddress() {
        return branchAddress;
    }
    
    /**
     * Get Branch phone.
     * @return branch phone
     */
    public PhoneNumber getBranchPhone() {
        return branchPhone;
    }
    
    /**
     * Get has delivery.
     * @return true if branch has delivery service, false otherwise
     */
    public Boolean hasDelivery() {
        return hasDelivery;
    }
    
    /**
     * Get has regular delivery.
     * @return true if branch has regular delivery service, 
     * 			false otherwise
     */
    public Boolean hasRegularDelivery() {
        return hasRegularDelivery;
    }
    
    /**
     * Get has postal delivery.
     * @return true if branch has postal delivery service, 
     * 			false otherwise
     */
    public Boolean hasPostalDelivery() {
        return hasPostalDelivery;
    }
    
    /**
     * Get has UPS delivery.
     * @return true if branch has UPS delivery service, 
     * 			false otherwise
     */
    public Boolean hasUPSDelivery() {
        return hasUPSDelivery;
    }
    
    /**
     * Get has convenience store delivery.
     * @return true if branch has convenience store delivery service, 
     * 			false otherwise
     */
    public Boolean hasConvenienceStoreDelivery() {
        return hasConvenienceStoreDelivery;
    }
    
    /**
     * Get has take-out.
     * @return true if branch has take-out service, false otherwise
     */
    public Boolean hasTakeOut() {
        return hasTakeOut;
    }
    
    /**
     * Get has take-in.
     * @return true if branch has take-in service, false otherwise
     */
    public Boolean hasTakeIn() {
        return hasTakeIn;
    }
    
    /**
     * Whether this branch offers this order type or not
     * @param the order type to check
     * @return true if the restaurant offers this order type,
     * 				false otherwise.
     */
    public boolean hasOrderType(OrderType orderType) {
    	switch (orderType) {
    		case DELIVERY:
    			return hasDelivery();
    		case TAKE_OUT:
    			return hasTakeOut();
    		case TAKE_IN:
    			return hasTakeIn();
    		default:
    			return false;
    	}
    }
    
    /**
     * Return the first available order type offered by this branch
     * in the following order: delivery, take-out, take-in
     * @return the first available order type available in this branch
     */
    public Order.OrderType getFirstAvailableOrderType() {
    	if (hasDelivery()) {
    		return Order.OrderType.DELIVERY;
    	}
    	if (hasTakeOut()) {
    		return Order.OrderType.TAKE_OUT;
    	}
    	if (hasTakeIn()) {
    		return Order.OrderType.TAKE_IN;
    	}
    	return null;
    }
    
    /**
     * Get branch creation time.
     * @return the time this branch was created
     */
    public Date getBranchTime() {
        return branchTime;
    }
    
    /**
     * Get branch email.
     * @return the email address of this branch
     */
    public Email getBranchEmail() {
        return branchEmail;
    }
    
    /**
     * Compare this branch with another branch
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this Branch, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof Branch ) ) return false;
        Branch branch = (Branch) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(branch.getKey()));
    }
    
    /**
     * Set Branch region.
     * @param branchRegion
     * 			: branch region key
     * @throws MissingRequiredFieldsException
     */
    public void setRegion(Long region) 
    		throws MissingRequiredFieldsException {
    	if (region == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Region is missing.");
    	}
    	this.region = region;
    }
     
    /**
     * Set Branch name.
     * @param branchName
     * 			: branch name
     * @throws MissingRequiredFieldsException
     */
    public void setBranchName(String branchName)
    		throws MissingRequiredFieldsException {
    	if (branchName == null || branchName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Branch name is missing.");
    	}
    	this.branchName = branchName;
    }
    
    /**
     * Set Branch address.
     * @param branchAddress
     * 			: branch address
     * @throws MissingRequiredFieldsException
     */
    public void setBranchAddress(PostalAddress branchAddress)
    		throws MissingRequiredFieldsException {
    	if (branchAddress == null || branchAddress.getAddress().trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Branch address is missing.");
    	}
    	this.branchAddress= branchAddress;
    }
    
    /**
     * Set Branch phone.
     * @param branchPhone
     * 			: branch phone
     * @throws InvalidFieldFormatException 
     * @throws MissingRequiredFieldsException
     */
    public void setBranchPhone(PhoneNumber branchPhone)
    		throws MissingRequiredFieldsException, InvalidFieldFormatException {
    	if (branchPhone == null || branchPhone.getNumber().trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Branch phone is missing.");
    	}
    	
    	// Check phone number format
    	if (!FieldValidator.isValidPhoneNumber(branchPhone.getNumber())) {
    		throw new InvalidFieldFormatException(this.getClass(), "Invalid phone number.");
    	}
    	this.branchPhone = branchPhone;
    }

    /**
     * Set Branch has delivery.
     * @param hasDelivery
     * 			: true if branch has delivery service, false otherwise
     * @throws MissingRequiredFieldsException 
     * @throws InvalidFieldSelectionException 
     */
    public void setHasDelivery(Boolean hasDelivery) throws MissingRequiredFieldsException, 
    		InvalidFieldSelectionException {
    	if (hasDelivery == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Has delivery is missing.");
    	}
    	if (hasDelivery == false && hasTakeOut == false && hasTakeIn == false) {
    		throw new InvalidFieldSelectionException(this.getClass(), 
    				"The three available order types may not all be false.");
    	}
    	this.hasDelivery = hasDelivery;
    }
    
    /**
     * Set Branch has regular delivery.
     * @param hasRegularDelivery
     * 			: true if branch has regular delivery service, false otherwise
     * @throws MissingRequiredFieldsException 
     */
    public void setHasRegularDelivery(Boolean hasRegularDelivery) 
    		throws MissingRequiredFieldsException {
    	if (hasDelivery) {
    		if (hasRegularDelivery == null) {
    			throw new MissingRequiredFieldsException(this.getClass(), 
        				"Has regular delivery is missing.");
    		}
    	}
    	this.hasRegularDelivery = hasRegularDelivery;
    }
    
    /**
     * Set Branch has postal delivery.
     * @param hasPostalDelivery
     * 			: true if branch has postal delivery service, false otherwise
     * @throws MissingRequiredFieldsException 
     */
    public void setHasPostalDelivery(Boolean hasPostalDelivery) 
    		throws MissingRequiredFieldsException{
    	if (hasDelivery) {
    		if (hasPostalDelivery == null) {
    			throw new MissingRequiredFieldsException(this.getClass(), 
        				"Has postal delivery is missing.");
    		}
    	}
    	this.hasPostalDelivery = hasPostalDelivery;
    }
    
    /**
     * Set Branch has UPS delivery.
     * @param hasUPSDelivery
     * 			: true if branch has UPS delivery service, false otherwise
     * @throws MissingRequiredFieldsException 
     */
    public void setHasUPSDelivery(Boolean hasUPSDelivery) 
    		throws MissingRequiredFieldsException{
    	if (hasDelivery) {
    		if (hasUPSDelivery == null) {
    			throw new MissingRequiredFieldsException(this.getClass(), 
        				"Has UPS delivery is missing.");
    		}
    	}
    	this.hasUPSDelivery = hasUPSDelivery;
    }
    
    /**
     * Set Branch has convenience store delivery.
     * @param hasConvenienceStoreDelivery
     * 			: true if branch has convenience store delivery service, false otherwise
     * @throws MissingRequiredFieldsException 
     */
    public void setHasConvenienceStoreDelivery(Boolean hasConvenienceStoreDelivery) 
    		throws MissingRequiredFieldsException{
    	if (hasDelivery) {
    		if (hasConvenienceStoreDelivery == null) {
    			throw new MissingRequiredFieldsException(this.getClass(), 
        				"Has convenience store delivery is missing.");
    		}
    	}
    	this.hasConvenienceStoreDelivery = hasConvenienceStoreDelivery;
    }
    
    /**
     * Set Branch has take-out.
     * @param hasTakeOut
     * 			: true if branch has take-out service, false otherwise
     * @throws MissingRequiredFieldsException 
     * @throws InvalidFieldSelectionException 
     */
    public void setHasTakeOut(Boolean hasTakeOut) throws MissingRequiredFieldsException, 
    		InvalidFieldSelectionException {
    	if (hasTakeOut == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Has take-out is missing.");
    	}
    	if (hasDelivery == false && hasTakeOut == false && hasTakeIn == false) {
    		throw new InvalidFieldSelectionException(this.getClass(), 
    				"The three available order types may not all be false.");
    	}
    	this.hasTakeOut = hasTakeOut;
    }
    
    /**
     * Set Branch has take-in.
     * @param hasTakeIn
     * 			: true if branch has take-in service, false otherwise
     * @throws MissingRequiredFieldsException 
     * @throws InvalidFieldSelectionException 
     */
    public void setHasTakeIn(Boolean hasTakeIn) throws MissingRequiredFieldsException, 
    		InvalidFieldSelectionException {
    	if (hasTakeIn == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Has take-in is missing.");
    	}
    	if (hasDelivery == false && hasTakeOut == false && hasTakeIn == false) {
    		throw new InvalidFieldSelectionException(this.getClass(), 
    				"The three available order types may not all be false.");
    	}
    	this.hasTakeIn = hasTakeIn;
    }
    
    /**
     * Set Branch email.
     * @param branchEmail
     * 			: branch email
     * @throws InvalidFieldFormatException 
     */
    public void setBranchEmail(Email branchEmail)
    		throws InvalidFieldFormatException {
    	
    	// Check email format
    	if (!FieldValidator.isValidEmailAddress(branchEmail.getEmail())) {
    		throw new InvalidFieldFormatException(this.getClass(), "Invalid email address.");
    	}
    	this.branchEmail = branchEmail;
    }

}