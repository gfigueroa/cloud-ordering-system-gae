/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import exceptions.InvalidFieldFormatException;
import exceptions.MissingRequiredFieldsException;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

/**
 * This class represents the Coupon table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class Coupon implements Serializable {

	public static enum Status {
		INACTIVE, ACTIVE, EXPIRED
	}
	
	public static enum CouponType {
		PRODUCT_ITEMS, CASH
	}
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
    @Persistent
    private CouponType couponType;
    
    @Persistent
    private String couponTitle;
    
    @Persistent
    private Double couponPrice;
    
    @Persistent
    private Double couponCash;

    @Persistent
    private Date couponCreationDate;
    
    @Persistent
    private Date couponStartingDate;
    
    @Persistent
    private Date couponEndingDate;
    
    @Persistent
    private ArrayList<Key> menuItems;

    /**
     * Coupon constructor.
     * @param couponType
     * 			: coupon type
     * @param couponTitle
     * 			: coupon title
     * @param couponPrice
     * 			: coupon price
     * @param couponCash
     * 			: coupon cash (for cash coupons)
     * @param couponStartingDate
     * 			: the coupon starting date
     * @param couponEndingDate
     * 			: the coupon ending date
     * @param menuItems
     * 			: coupon menu items (for product item coupons)
     * @throws MissingRequiredFieldsException
     * @throws InvalidFieldFormatException
     */
    public Coupon(CouponType couponType, String couponTitle, 
    		Double couponPrice, Double couponCash,
    		Date couponStartingDate, Date couponEndingDate,
    		ArrayList<Key> menuItems) 
		throws MissingRequiredFieldsException, InvalidFieldFormatException {
        
    	// Check "required field" constraints
    	if (couponType == null || couponTitle == null || couponPrice == null ||
    			couponStartingDate == null || couponEndingDate == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	if (couponTitle.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
    	// Check required field contraints for specific coupon types
    	if (couponType == CouponType.CASH) {
    		if (couponCash == null) {
    			throw new MissingRequiredFieldsException(this.getClass(), 
        				"One or more required fields are missing.");
    		}
    	}
    	if (couponType == CouponType.PRODUCT_ITEMS) {
    		if (menuItems.isEmpty()) {
    			throw new MissingRequiredFieldsException(this.getClass(), 
        				"One or more required fields are missing.");
    		}
    	}

    	this.couponType = couponType;
    	this.couponTitle = couponTitle;
    	this.couponPrice = couponPrice;
    	this.couponCash = couponCash;
    	
    	Date now = new Date();
    	this.couponCreationDate = now;

        this.couponStartingDate = couponStartingDate;
        this.couponEndingDate = couponEndingDate;
        
        this.menuItems = menuItems;
    }

    /**
     * Get Coupon key.
     * @return coupon key
     */
    public Key getKey() {
        return key;
    }
    
    /**
     * Get Coupon type.
     * @return coupon type
     */
    public CouponType getCouponType() {
        return couponType;
    }
    
    /**
     * Get Coupon type as string.
     * @return coupon type string representation
     * @return
     */
    public String getCouponTypeString() {
    	switch (couponType) {
    		case PRODUCT_ITEMS:
    			return "Product Items";
    		case CASH:
    			return "Cash";
    		default: 
    			return "";
    	}
    }
    
    /**
     * Get coupon type from string
     * @param couponTypeString
     * 			: the coupon type represented as a string
     * @return the corresponding coupon type
     */
    public static CouponType getCouponTypeFromString(String couponTypeString) {
    	
    	if (couponTypeString == null) {
    		return null;
    	}
    	
    	if (couponTypeString.equalsIgnoreCase("product_items")) {
    		return CouponType.PRODUCT_ITEMS;
    	}
    	else if (couponTypeString.equalsIgnoreCase("cash")) {
    		return CouponType.CASH;
    	}
    	else {
    		return null;
    	}
    }
    
    /**
     * Get Coupon title.
     * @return coupon title
     */
    public String getCouponTitle() {
        return couponTitle;
    }
    
    /**
     * Get Coupon price.
     * @return coupon price
     */
    public Double getCouponPrice() {
        return couponPrice;
    }
    
    /**
     * Get Coupon cash.
     * @return coupon cash
     */
    public Double getCouponCash() {
        return couponCash;
    }
    
    /**
     * Get coupon creation date.
     * @return coupon creation date
     */
    public Date getCouponCreationDate() {
    	return couponCreationDate;
    }
    
    /**
     * Get coupon starting date.
     * @return coupon starting date
     */
    public Date getCouponStartingDate() {
    	return couponStartingDate;
    }
    
    /**
     * Get coupon ending date.
     * @return coupon ending date
     */
    public Date getCouponEndingDate() {
    	return couponEndingDate;
    }
    
    /**
     * Get coupon menu items (for fixed coupons)
     * @return coupon menu items' keys
     */
    public ArrayList<Key> getMenuItems() {
    	return menuItems;
    }
    
    /**
     * Compare this coupon with another coupon
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this Coupon, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof Coupon ) ) return false;
        Coupon coupon = (Coupon) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(coupon.getKey()));
    }
    
    /**
     * Set Coupon title.
     * @param couponTitle
     * 			: coupon title
     * @throws MissingRequiredFieldsException
     */
    public void setCouponTitle(String couponTitle)
    		throws MissingRequiredFieldsException {
    	if (couponTitle == null || couponTitle.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Coupon title is missing.");
    	}
    	this.couponTitle = couponTitle;
    }
    
    /**
     * Set Coupon price.
     * @param couponPrice
     * 			: coupon price
     * @throws MissingRequiredFieldsException 
     */
    public void setCouponPrice(Double couponPrice) 
    		throws MissingRequiredFieldsException {
    	if (couponPrice == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Missing coupon price.");
    	}
    	this.couponPrice = couponPrice;
    }
    
    /**
     * Set Coupon cash.
     * @param couponCash
     * 			: coupon cash
     * @throws MissingRequiredFieldsException 
     */
    public void setCouponCash(Double couponCash) 
    		throws MissingRequiredFieldsException {
    	if (couponCash == null && couponType == Coupon.CouponType.CASH) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Missing coupon cash.");
    	}
    	this.couponCash = couponCash;
    }
    
    /**
     * Set Coupon starting date.
     * @param couponStartingDate
     * @throws MissingRequiredFieldsException 
     */
    public void setCouponStartingDate(Date couponStartingDate) 
    		throws MissingRequiredFieldsException {
    	if (couponStartingDate == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Missing coupon starting date.");
    	}
    	this.couponStartingDate = couponStartingDate;
    }
    
    /**
     * Set Coupon ending date.
     * @param couponEndingDate
     * @throws MissingRequiredFieldsException 
     */
    public void setCouponEndingDate(Date couponEndingDate) 
    		throws MissingRequiredFieldsException {
    	if (couponEndingDate == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Missing coupon ending date.");
    	}
    	this.couponEndingDate = couponEndingDate;
    }
    
    /**
     * Set Coupon menu items.
     * Deletes current coupon menu items and replaces 
     * them with new list.
     * @param couponMenuItems
     * @throws MissingRequiredFieldsException 
     */
    public void couponMenuItems(ArrayList<Key> menuItems) 
    		throws MissingRequiredFieldsException {
    	
    	if (menuItems.isEmpty() && couponType == 
    			Coupon.CouponType.PRODUCT_ITEMS) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Missing menu items.");
    	}
    	
    	// First, empty the list
    	this.menuItems.clear();
    	
    	// Second, replace the current list
    	for (Key menuItemKey : menuItems) {
    		this.menuItems.add(menuItemKey);
    	}
    }

}