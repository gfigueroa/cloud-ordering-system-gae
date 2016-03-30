/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import exceptions.InvalidFieldFormatException;
import exceptions.MissingRequiredFieldsException;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

/**
 * This class represents the Set table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class Set implements Serializable {

	public static enum SetType {
		FIXED_SET, TYPE_SET
	}
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
	private Integer setNumber;
	
    @Persistent
    private SetType setType;
    
    @Persistent
    private String setName;
    
    @Persistent
    private String setDescription;
    
    @Persistent
    private Double setPrice;
    
    @Persistent
    private Boolean hasFixedPrice;
    
    @Persistent
    private Double setDiscount; // Percentage
    
    @Persistent
    private BlobKey setImage;
    
    @Persistent
    private Integer setServingTime;

    @Persistent
    private Boolean isAvailable;
    
    @Persistent
    private Integer setServiceTime;
    // 0: all day, 1: breakfast (07:00 - 12:00), 
    // 2: lunch (12:00 - 16:30), 3: dinner (16:30 - 23:30), 
    // 4: 1 + 2, 5: 1 + 3, 6: 2 + 3
    
    @Persistent
    private Date setTime;
    
    @Persistent
    private String setComments;
    
    @Persistent
    private ArrayList<Key> menuItems;
    
    @Persistent(defaultFetchGroup = "true")
    @Element(dependent = "true")
    private ArrayList<TypeSetMenuItem> typeSetMenuItems;

    /**
     * Set constructor.
     * @param setNumber
     * 			: set number
     * @param setType
     * 			: set type
     * @param setName
     * 			: set name
     * @param setDescription
     * 			: set description
     * @param setPrice
     * 			: set price
     * @param hasFixedPrice
     * 			: set has fixed price
     * @param setDiscount
     * 			: set discount
     * @param setImage
     * 			: set image blob key
     * @param setServingTime
     * 			: set serving time
     * @param isAvailable
     * 			: set is available
     * @param setServiceTime
     * 			: set service time (0 to 6)
     * @param setComments
     * 			: set comments
     * @param menuItems
     * 			: set menu items (for fixed sets)
     * @param typeSetMenuItems
     * 			: set menu items (for type sets)
     * @throws MissingRequiredFieldsException
     * @throws InvalidFieldFormatException
     */
    public Set(Integer setNumber, SetType setType, String setName, 
    		String setDescription, Double setPrice, Boolean hasFixedPrice, 
    		Double setDiscount, BlobKey setImage, Integer setServingTime, 
    		Boolean isAvailable, Integer setServiceTime, String setComments,
    		ArrayList<Key> menuItems, ArrayList<TypeSetMenuItem> typeSetMenuItems) 
		throws MissingRequiredFieldsException, InvalidFieldFormatException {
        
    	// Check "required field" constraints
    	if (setType == null || setName == null || hasFixedPrice == null ||
    			isAvailable == null || setServiceTime == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	if (setName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
    	// Check required field contraints for specific set types
    	if (setType == SetType.FIXED_SET) {
    		if (setPrice == null) {
    			throw new MissingRequiredFieldsException(this.getClass(), 
        				"One or more required fields are missing.");
    		}
    		if (menuItems.isEmpty()) {
    			throw new MissingRequiredFieldsException(this.getClass(), 
        				"One or more required fields are missing.");
    		}
    	}
    	if (setType == SetType.TYPE_SET) {
    		if (typeSetMenuItems.isEmpty()) {
    			throw new MissingRequiredFieldsException(this.getClass(), 
        				"One or more required fields are missing.");
    		}
    	}
    	
    	// Check discount value
    	if (setDiscount != null) {
	    	if (setDiscount < 0 || setDiscount > 1) {
	    		throw new InvalidFieldFormatException(this.getClass(), 
	    				"Invalid set discount (must be between 0 and 1).");
	    	}
    	}
    	// Check service time
    	if (setServiceTime < 0 || setServiceTime > 6) {
    		throw new InvalidFieldFormatException(this.getClass(), 
    				"Invalid set service time (must be between 0 and 6).");
    	}
    	
    	this.setNumber = setNumber;
    	this.setType = setType;
    	this.setName = setName;
    	this.setDescription = setDescription;
    	this.setPrice = setPrice;
    	this.hasFixedPrice = hasFixedPrice;
    	this.setDiscount = setDiscount;
    	this.setImage = setImage;
    	this.setServingTime = setServingTime;
    	this.isAvailable = isAvailable;
    	this.setServiceTime = setServiceTime;
    	
    	Date now = new Date();
    	this.setTime = now;

        this.setComments = setComments;
        
        this.menuItems = menuItems;
        this.typeSetMenuItems = typeSetMenuItems;
    }

    /**
     * Get Set key.
     * @return set key
     */
    public Key getKey() {
        return key;
    }
    
    /**
     * Get set number
     * @return set number
     */
    public Integer getSetNumber() {
    	return setNumber;
    }
    
    /**
     * Get Set type.
     * @return set type
     */
    public SetType getSetType() {
        return setType;
    }
    
    /**
     * Get Set type as string.
     * @return set type string representation
     * @return
     */
    public String getSetTypeString() {
    	switch (setType) {
    		case FIXED_SET:
    			return "Fixed Set";
    		case TYPE_SET:
    			return "Type Set";
    		default: 
    			return "";
    	}
    }
    
    /**
     * Get set type from string
     * @param setTypeString
     * 			: the set type represented as a string
     * @return the corresponding set type
     */
    public static SetType getSetTypeFromString(String setTypeString) {
    	
    	if (setTypeString == null) {
    		return null;
    	}
    	
    	if (setTypeString.equalsIgnoreCase("fixed_set")) {
    		return SetType.FIXED_SET;
    	}
    	else if (setTypeString.equalsIgnoreCase("type_set")) {
    		return SetType.TYPE_SET;
    	}
    	else {
    		return null;
    	}
    }
    
    /**
     * Get Set name.
     * @return set name
     */
    public String getSetName() {
        return setName;
    }
    
    /**
     * Get Set description.
     * @return set description
     */
    public String getSetDescription() {
        return setDescription;
    }
    
    /**
     * Get Set price.
     * @return set price
     */
    public Double getSetPrice() {
        return setPrice;
    }
    
    /**
     * Get whether this set has a fixed price or not
     * @return true if the set has a fixed price,
     * 			false otherwise
     */
    public Boolean hasFixedPrice() {
    	return hasFixedPrice;
    }
    
    /**
     * Get Set discount.
     * @return set discount
     */
    public Double getSetDiscount() {
        return setDiscount;
    }
    
    /**
     * Get Set image.
     * @return set image blobkey
     */
    public BlobKey getSetImage() {
        return setImage;
    }
    
    /**
     * Get Set serving time.
     * @return set serving time in minutes
     */
    public Integer getSetServingTime() {
        return setServingTime;
    }
    
    /**
     * Is available.
     * @return true if the set is currently available, false otherwise
     */
    public Boolean isAvailable() {
        return isAvailable;
    }
    
    /**
     * Get Set service time.
     * @return set service time
     */
    public Integer getSetServiceTime() {
        return setServiceTime;
    }
    
    /**
     * Get Set time.
     * @return time of set creation
     */
    public Date getMenuTime() {
        return setTime;
    }
    
    /**
     * Get Set comments.
     * @return set comments
     */
    public String getSetComments() {
    	return setComments;
    }
    
    /**
     * Get set menu items (for fixed sets)
     * @return set menu items' keys
     */
    public ArrayList<Key> getMenuItems() {
    	return menuItems;
    }
    
    /**
     * Get type set menu items (for type sets)
     * @return type set menu items
     */
    public ArrayList<TypeSetMenuItem> getTypeSetMenuItems() {
    	return typeSetMenuItems;
    }
    
    /**
     * Compare this set with another set
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this Set, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof Set ) ) return false;
        Set set = (Set) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(set.getKey()));
    }

    /**
     * Set Set number
     * @param setNumber
     * 			: set number
     * @throws MissingRequiredFieldsException 
     */
    public void setSetNumber(Integer setNumber) 
    		throws MissingRequiredFieldsException {
    	if (setNumber == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Set number is missing.");
    	}
    	this.setNumber = setNumber;
    }
    
    /**
     * Set Set name.
     * @param setName
     * 			: set name
     * @throws MissingRequiredFieldsException
     */
    public void setSetName(String setName)
    		throws MissingRequiredFieldsException {
    	if (setName == null || setName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Set name is missing.");
    	}
    	this.setName = setName;
    }
    
    
    /**
     * Set Set description.
     * @param setDescription
     * 			: set description
     */
    public void setSetDescription(String setDescription) {
    	this.setDescription = setDescription;
    }
    
    /**
     * Set Set price.
     * @param setPrice
     * 			: set price
     * @throws MissingRequiredFieldsException 
     */
    public void setSetPrice(Double setPrice) 
    		throws MissingRequiredFieldsException {
    	if (setPrice == null && setType == SetType.FIXED_SET) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Missing set price.");
    	}
    	this.setPrice = setPrice;
    }
    
    /**
     * Set has fixed price.
     * @param hasFixedPrice
     * 			: whether this set has a fixed price or not
     * @throws MissingRequiredFieldsException 
     */
    public void setHasFixedPrice(Boolean hasFixedPrice) 
    		throws MissingRequiredFieldsException {
    	if (hasFixedPrice == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Missing Has Fixed Price.");
    	}
    	this.hasFixedPrice = hasFixedPrice;
    }
    
    /**
     * Set Set discount.
     * @param setDiscount
     * 			: set discount
     * @throws InvalidFieldFormatException
     */
    public void setSetDiscount(Double setDiscount) 
    		throws InvalidFieldFormatException {
    	// Check discount value
    	if (setDiscount != null) {
	    	if (setDiscount < 0 || setDiscount > 1) {
	    		throw new InvalidFieldFormatException(this.getClass(), 
	    				"Invalid set discount (must be between 0 and 1).");
	    	}
    	}
    	this.setDiscount = setDiscount;
    }
    
    /**
     * Set Set image.
     * @param setImage
     * 			: set image blobkey
     */
    public void setSetImage(BlobKey setImage) {
    	this.setImage = setImage;
    }
    
    /**
     * Set Set serving time.
     * @param setServingTime
     * 			: set serving time
     */
    public void setSetServingTime(Integer setServingTime) {
    	this.setServingTime = setServingTime;
    }
    
    /**
     * Set Set is available.
     * @param isAvailable
     * 			: true if set is currently available, false otherwise
     * @throws MissingRequiredFieldsException 
     */
    public void setIsAvailable(Boolean isAvailable) 
    		throws MissingRequiredFieldsException {
    	if (isAvailable == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Set is available is missing.");
    	}
    	this.isAvailable = isAvailable;
    }
    
    /**
     * Set Set service time.
     * @param setServiceTime
     * 			: set service time
     * @throws InvalidFieldFormatException 
     * @throws MissingRequiredFieldsException 
     */
    public void setSetServiceTime(Integer setServiceTime)
    		throws InvalidFieldFormatException, 
    		MissingRequiredFieldsException {
    	// Check service time
    	if (setServiceTime == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Set Service Time is missing.");
    	}
    	if (setServiceTime < 0 || setServiceTime > 6) {
    		throw new InvalidFieldFormatException(this.getClass(), 
    				"Invalid set service time (must be between 0 and 6).");
    	}
    	this.setServiceTime = setServiceTime;
    }
    
    /**
     * Set Set comments.
     * @param setComments
     * 			: set comments
     */
    public void setSetComments(String setComments) {
    	this.setComments = setComments;
    }
    
    /**
     * Set set menu items.
     * Deletes current set menu items and replaces 
     * them with new list.
     * @param setMenuItems
     * 			: the new menu items
     * @throws MissingRequiredFieldsException 
     */
    public void setMenuItems(ArrayList<Key> menuItems) 
    		throws MissingRequiredFieldsException {
    	
    	// Check required field contraints for specific set types
    	if (setType == SetType.FIXED_SET) {
    		if (menuItems.isEmpty()) {
    			throw new MissingRequiredFieldsException(this.getClass(), 
        				"One or more required fields are missing.");
    		}
    	}

    	// First, empty the list
    	this.menuItems.clear();
    	
    	// Second, replace the current list
    	for (Key menuItemKey : menuItems) {
    		this.menuItems.add(menuItemKey);
    	}
    }
    
    /**
     * Set type set menu items.
     * Deletes the current type set menu items and replaces
     * them with new list.
     * @param typeSetMenuItems
     * 			: the new type set menu items
     * @throws MissingRequiredFieldsException 
     */
    public void setTypeSetMenuItems(
    		ArrayList<TypeSetMenuItem> typeSetMenuItems) 
    				throws MissingRequiredFieldsException {
    	
    	// Check required field contraints for specific set types
    	if (setType == SetType.TYPE_SET) {
    		if (typeSetMenuItems.isEmpty()) {
    			throw new MissingRequiredFieldsException(this.getClass(), 
        				"One or more required fields are missing.");
    		}
    	}
    	
    	// First, empty the list
    	this.typeSetMenuItems.clear();
    	
    	// Second, replace the current list
    	for (TypeSetMenuItem item : typeSetMenuItems) {
    		this.typeSetMenuItems.add(item);
    	}
    }
    
    /**
     * Return Set information as a String.
     * @return String representation of this Set
     */
    public String toString() {
    	String information = "";

    	if (this.key == null) {
    		information += "Set key: NULL (object is transient) \n";
    	}
    	else {
    		information += "Set key: " + this.key.toString() + "\n";
    	}
    	information += "SetType key: " + this.setType + "\n";
    	information += "Set name: " + this.setName + "\n";
    	information += "Set price: " + this.setPrice + "\n";
    	information += "Set description: " + this.setDescription + "\n";
    	information += "Set image blobkey: " + this.setImage + "\n";
    	information += "Set serving time (in minutes): " + this.setServingTime + "\n";
    	information += "Set is available: " + this.isAvailable + "\n";
    	information += "Set time of creation: " + this.setTime + "\n";
    	information += "Set comments: " + this.setComments;
    	
    	return information;
    }

}