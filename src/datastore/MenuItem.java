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

import exceptions.InexistentObjectException;
import exceptions.InvalidFieldFormatException;
import exceptions.MissingRequiredFieldsException;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

/**
 * This class represents the MenuItem table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class MenuItem implements Serializable {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private Key menuItemType;
    
    @Persistent
    private String menuItemName;
    
    @Persistent
    private Double menuItemPrice;
    
    @Persistent
    private Double menuItemDiscount; // Percentage
    
    @Persistent
    private String menuItemDescription;
    
    @Persistent
    private BlobKey menuItemImage;
    
    @Persistent
    private Integer menuItemServingTime;

    @Persistent
    private Boolean isAvailable;
    
    @Persistent
    private Integer menuItemServiceTime;
    // 0: all day, 1: breakfast (07:00 - 12:00), 
    // 2: lunch (12:00 - 16:30), 3: dinner (16:30 - 23:30), 
    // 4: 1 + 2, 5: 1 + 3, 6: 2 + 3
    
    @Persistent
    private Date menuItemTime;
    
    @Persistent
    private String menuItemComments;
    
    @Persistent
    @Element(dependent = "true")
    private ArrayList<MenuItemAdditionalPropertyValue> menuItemAdditionalPropertyValues;
    
    @Persistent
    @Element(dependent = "true")
    private ArrayList<AdditionalPropertyConstraint> additionalPropertyConstraints;

    /**
     * MenuItem constructor.
     * @param menuItemType
     * 			: menuItem type
     * @param menuItemName
     * 			: menuItem name
     * @param menuItemPrice
     * 			: menuItem price
     * @param menuItemDiscount
     * 			: menuItem discount
     * @param menuItemDescription
     * 			: menuItem description
     * @param menuItemImage
     * 			: menuItem image blob key
     * @param menuItemServingTime
     * 			: menuItem serving time
     * @param isAvailable
     * 			: menuItem is available
     * @param menuItemServiceTime
     * 			: menuItem service time (0 to 6)
     * @param menuItemComments
     * 			: menuItem comments
     * @throws MissingRequiredFieldsException
     * @throws InvalidFieldFormatException
     */
    public MenuItem(Key menuItemType, String menuItemName, Double menuItemPrice, Double menuItemDiscount,
    		String menuItemDescription, BlobKey menuItemImage, Integer menuItemServingTime, 
    		Boolean isAvailable, Integer menuItemServiceTime, String menuItemComments) 
		throws MissingRequiredFieldsException, InvalidFieldFormatException {
        
    	// Check "required field" constraints
    	if (menuItemType == null || menuItemName == null || menuItemPrice == null ||
    			menuItemDescription == null || isAvailable == null ||
    			menuItemServiceTime == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), "One or more required fields are missing.");
    	}
    	if (menuItemName.trim().isEmpty() || menuItemDescription.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), "One or more required fields are missing.");
    	}
    	
    	// Check discount value
    	if (menuItemDiscount != null) {
	    	if (menuItemDiscount < 0 || menuItemDiscount > 1) {
	    		throw new InvalidFieldFormatException(this.getClass(), 
	    				"Invalid menu item discount (must be between 0 and 1).");
	    	}
    	}
    	// Check service time
    	if (menuItemServiceTime < 0 || menuItemServiceTime > 6) {
    		throw new InvalidFieldFormatException(this.getClass(), 
    				"Invalid menu item service time (must be between 0 and 6).");
    	}
    	
    	this.menuItemType = menuItemType;

    	this.menuItemName = menuItemName;
    	this.menuItemPrice = menuItemPrice;
    	this.menuItemDiscount = menuItemDiscount;
    	this.menuItemDescription = menuItemDescription;
    	this.menuItemImage = menuItemImage;
    	this.menuItemServingTime = menuItemServingTime;
    	this.isAvailable = isAvailable;
    	this.menuItemServiceTime = menuItemServiceTime;
    	
    	Date now = new Date();
    	this.menuItemTime = now;

        this.menuItemComments = menuItemComments;
        
        // Create empty MenuItemAdditionalPropertyValue list
        this.menuItemAdditionalPropertyValues = new ArrayList<MenuItemAdditionalPropertyValue>();
        this.additionalPropertyConstraints = new ArrayList<AdditionalPropertyConstraint>();
    }

    /**
     * Get MenuItem key.
     * @return menuItem key
     */
    public Key getKey() {
        return key;
    }
    
    /**
     * Get MenuItem type.
     * @return menu item type
     */
    public Key getMenuItemType() {
        return menuItemType;
    }
    
    /**
     * Get MenuItem name.
     * @return menu item name
     */
    public String getMenuItemName() {
        return menuItemName;
    }
    
    /**
     * Get MenuItem price.
     * @return menu item price
     */
    public Double getMenuItemPrice() {
        return menuItemPrice;
    }
    
    /**
     * Get MenuItem discount.
     * @return menu item discount
     */
    public Double getMenuItemDiscount() {
        return menuItemDiscount;
    }

    /**
     * Get MenuItem description.
     * @return menu item description
     */
    public String getMenuItemDescription() {
        return menuItemDescription;
    }
    
    /**
     * Get MenuItem image.
     * @return menu item image blobkey
     */
    public BlobKey getMenuItemImage() {
        return menuItemImage;
    }
    
    /**
     * Get MenuItem serving time.
     * @return menu item serving time in minutes
     */
    public Integer getMenuItemServingTime() {
        return menuItemServingTime;
    }
    
    /**
     * Is available.
     * @return true if the menu item is currently available, false otherwise
     */
    public Boolean isAvailable() {
        return isAvailable;
    }
    
    /**
     * Get MenuItem service time.
     * @return menu item service time
     */
    public Integer getMenuItemServiceTime() {
        return menuItemServiceTime;
    }
    
    /**
     * Get MenuItem time.
     * @return time of menu item creation
     */
    public Date getMenuTime() {
        return menuItemTime;
    }
    
    /**
     * Get MenuItem comments.
     * @return menuItem comments
     */
    public String getMenuItemComments() {
    	return menuItemComments;
    }
    
    /**
     * Get menu item additional property values
     * @return menu item additional property values
     */
    public ArrayList<MenuItemAdditionalPropertyValue> 
    		getMenuItemAdditionalPropertyValues() {
    	return menuItemAdditionalPropertyValues;
    }
    
    /**
     * Get Additional Property Constraint list.
     * @return restaurant additional property constraints
     */
    public ArrayList<AdditionalPropertyConstraint> 
    		getAdditionalPropertyConstraints() {
        return additionalPropertyConstraints;
    }
    
    /**
     * Compare this menu item with another menu item
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this MenuItem, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof MenuItem ) ) return false;
        MenuItem menuItem = (MenuItem) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(menuItem.getKey()));
    }
    
    /**
     * Set MenuItem type.
     * @param menuItemType
     * 			: menuItem type key
     * @throws MissingRequiredFieldsException
     */
    public void setMenuItemType(Key menuItemType) 
    		throws MissingRequiredFieldsException {
    	if (menuItemType == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Menu item type is missing.");
    	}
    	this.menuItemType = menuItemType;
    }
    
    /**
     * Set MenuItem name.
     * @param menuItemName
     * 			: menuItem name
     * @throws MissingRequiredFieldsException
     */
    public void setMenuItemName(String menuItemName)
    		throws MissingRequiredFieldsException {
    	if (menuItemName == null || menuItemName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Menu item name is missing.");
    	}
    	this.menuItemName = menuItemName;
    }
    
    /**
     * Set MenuItem price.
     * @param menuItemPrice
     * 			: menuItem price
     * @throws MissingRequiredFieldsException 
     */
    public void setMenuItemPrice(Double menuItemPrice) 
    		throws MissingRequiredFieldsException {
    	if (menuItemPrice == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Missing menuItem price.");
    	}
    	this.menuItemPrice = menuItemPrice;
    }
    
    /**
     * Set MenuItem discount.
     * @param menuItemDiscount
     * 			: menuItem discount
     * @throws InvalidFieldFormatException
     */
    public void setMenuItemDiscount(Double menuItemDiscount) 
    		throws InvalidFieldFormatException {
    	// Check discount value
    	if (menuItemDiscount != null) {
	    	if (menuItemDiscount < 0 || menuItemDiscount > 1) {
	    		throw new InvalidFieldFormatException(this.getClass(), 
	    				"Invalid menu item discount (must be between 0 and 1).");
	    	}
    	}
    	this.menuItemDiscount = menuItemDiscount;
    }
    
    /**
     * Set MenuItem description.
     * @param menuItemDescription
     * 			: menuItem description
     * @throws MissingRequiredFieldsException
     */
    public void setMenuItemDescription(String menuItemDescription)
    		throws MissingRequiredFieldsException {
    	if (menuItemDescription == null || menuItemDescription.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Menu item description is missing.");
    	}
    	this.menuItemDescription = menuItemDescription;
    }
    
    /**
     * Set MenuItem image.
     * @param menuItemImage
     * 			: menuItem image blobkey
     */
    public void setMenuItemImage(BlobKey menuItemImage) {
    	this.menuItemImage = menuItemImage;
    }
    
    /**
     * Set MenuItem serving time.
     * @param menuItemServingTime
     * 			: menuItem serving time
     */
    public void setMenuItemServingTime(Integer menuItemServingTime) {
    	this.menuItemServingTime = menuItemServingTime;
    }
    
    /**
     * Set MenuItem is available.
     * @param isAvailable
     * 			: true if menuItem is currently available, false otherwise
     * @throws MissingRequiredFieldsException 
     */
    public void setIsAvailable(Boolean isAvailable) 
    		throws MissingRequiredFieldsException {
    	if (isAvailable == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Menu item is available is missing.");
    	}
    	this.isAvailable = isAvailable;
    }
    
    /**
     * Set MenuItem service time.
     * @param menuItemServiceTime
     * 			: menu item service time
     * @throws InvalidFieldFormatException 
     * @throws MissingRequiredFieldsException 
     */
    public void setMenuItemServiceTime(Integer menuItemServiceTime)
    		throws InvalidFieldFormatException, 
    		MissingRequiredFieldsException {
    	// Check service time
    	if (menuItemServiceTime == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Menu Item Service Time is missing.");
    	}
    	if (menuItemServiceTime < 0 || menuItemServiceTime > 6) {
    		throw new InvalidFieldFormatException(this.getClass(), 
    				"Invalid menu item service time (must be between 0 and 6).");
    	}
    	this.menuItemServiceTime = menuItemServiceTime;
    }
    
    /**
     * Set MenuItem comments.
     * @param menuItemComments
     * 			: menuItem comments
     */
    public void setMenuItemComments(String menuItemComments) {
    	this.menuItemComments = menuItemComments;
    }
    
    /**
     * Set menu item additional property values.
     * Deletes current menu item additional property values and replaces 
     * them with new list.
     * @param menuItemAdditionalPropertyValues
     */
    public void setMenuItemAdditionalPropertyValues(
    		ArrayList<MenuItemAdditionalPropertyValue> menuItemAdditionalPropertyValues) {
    	
    	// First, empty the list
    	this.menuItemAdditionalPropertyValues.clear();
    	
    	// Second, replace the current list
    	for (MenuItemAdditionalPropertyValue miapv : menuItemAdditionalPropertyValues) {
    		this.menuItemAdditionalPropertyValues.add(miapv);
    	}
    }
    
    /**
     * Add additional property constraint.
     * @param additionalPropertyConstraint
     * 			: the additional property constraint to add
     */
    public void addAdditionalPropertyConstraint(
    		AdditionalPropertyConstraint additionalPropertyConstraint) {
    	
    	this.additionalPropertyConstraints.add(additionalPropertyConstraint);
    }
    
    /**
     * Remove an additional property constraint from menu item.
     * @param additionalPropertyConstraint
     * 			: additional property constraint to remove
     * @throws InexistentObjectException
     */
    public void removeAdditionalPropertyConstraint(
    		AdditionalPropertyConstraint additionalPropertyConstraint) 
    				throws InexistentObjectException {
    	if (!additionalPropertyConstraints.remove(additionalPropertyConstraint)) {
    		throw new InexistentObjectException(AdditionalPropertyConstraint.class, 
    				"Additional Property Constraint not found!");
    	}
    }
    
    /**
     * Return MenuItem information as a String.
     * @return String representation of this MenuItem
     */
    public String toString() {
    	String information = "";

    	if (this.key == null) {
    		information += "MenuItem key: NULL (object is transient) \n";
    	}
    	else {
    		information += "MenuItem key: " + this.key.toString() + "\n";
    	}
    	information += "MenuItemType key: " + this.menuItemType + "\n";
    	information += "MenuItem name: " + this.menuItemName + "\n";
    	information += "MenuItem price: " + this.menuItemPrice + "\n";
    	information += "MenuItem description: " + this.menuItemDescription + "\n";
    	information += "MenuItem image blobkey: " + this.menuItemImage + "\n";
    	information += "MenuItem serving time (in minutes): " + this.menuItemServingTime + "\n";
    	information += "MenuItem is available: " + this.isAvailable + "\n";
    	information += "MenuItem time of creation: " + this.menuItemTime + "\n";
    	information += "MenuItem comments: " + this.menuItemComments;
    	
    	return information;
    }

}