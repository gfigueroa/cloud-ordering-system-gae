/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;

/**
 * This class represents a simple version of the MenuItemType table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class ProductItemTypeSimple implements Serializable {

    public String key;
    public String menuItemTypeName;
    public String menuItemTypeDescription;
    public Integer productItemTypeVersion;

    /**
     * MenuItemSimple constructor.
     * @param key:
     * 			: menuItemType key
     * @param menuItemTypeName
     * 			: menuItemType name
     * @param menuItemTypeDescription
     * 			: menuItemType description
     * @param productItemTypeVersion
     * 			: productItemType version
     */
    public ProductItemTypeSimple(String key, String menuItemTypeName, 
    		String menuItemTypeDescription, 
    		Integer productItemTypeVersion) {
    	
    	this.key = key;
    	this.menuItemTypeName = menuItemTypeName;
    	this.menuItemTypeDescription = menuItemTypeDescription;
    	this.productItemTypeVersion = productItemTypeVersion;
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
        if (!(o instanceof ProductItemTypeSimple ) ) return false;
        ProductItemTypeSimple menuItemType = (ProductItemTypeSimple) o;
        return key.equals(menuItemType.key);
    }

}