/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the MenuItemType table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class MenuItemType implements Serializable {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String menuItemTypeName;
    
    @Persistent
    private String menuItemTypeDescription;
    
    @Persistent
    private Integer productItemTypeVersion;

    /**
     * MenuItemType constructor.
     * @param menuItemTypeName
     * 			: menu item type name
     * @param menuItemTypeDescription
     * 			: menu item type description
     * @throws MissingRequiredFieldsException
     */
    public MenuItemType(String menuItemTypeName, String menuItemTypeDescription) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (menuItemTypeName == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	if (menuItemTypeName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
        this.menuItemTypeName = menuItemTypeName;
        this.menuItemTypeDescription = menuItemTypeDescription;
        this.productItemTypeVersion = 0; // Initialize the version in 0
    }

    /**
     * Get MenuItemType key.
     * @return menu item type key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Get MenuItemType name.
     * @return menu item type name
     */
    public String getMenuItemTypeName() {
        return menuItemTypeName;
    }

    /**
     * Get MenuItemType description.
     * @return menu item type description
     */
    public String getMenuItemTypeDescription() {
    	return menuItemTypeDescription;
    }
    
    /**
     * Get ProductItemType version.
     * @return the product item type version
     */
    public Integer getProductItemTypeVersion() {
    	if (productItemTypeVersion == null) {
    		productItemTypeVersion = 0;
    	}
    	return productItemTypeVersion;
    }
    
    /**
     * Compare this menu item type with another menu item type
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this MenuItemType, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof MenuItemType ) ) return false;
        MenuItemType menuItemType = (MenuItemType) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(menuItemType.getKey()));
    }
    
    /**
     * Set MenuItemType name.
     * @param menuItemTypeName
     * 			: menu item type name
     * @throws MissingRequiredFieldsException
     */
    public void setMenuItemTypeName(String menuItemTypeName)
    		throws MissingRequiredFieldsException {
    	if (menuItemTypeName == null || menuItemTypeName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Menu item type name is missing.");
    	}
    	this.menuItemTypeName = menuItemTypeName;
    }
    
    /**
     * Set MenuItemType description.
     * @param menuItemTypeDescription
     * 			: menu item type description
     */
    public void setMenuItemTypeDescription(String menuItemTypeDescription) {
    	this.menuItemTypeDescription = menuItemTypeDescription;
    }
    
    /**
     * Update product item type version by 1.
     * The product item type version is updated every time a product item
     * belonging to this product item type is added, deleted or modified.
     */
    public void updateProductItemTypeVersion() {
    	if (productItemTypeVersion == null) {
    		productItemTypeVersion = 0;
    	}
    	productItemTypeVersion++;
    }
}