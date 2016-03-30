/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the RestaurantType table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@PersistenceCapable
public class RestaurantType {

	// Enumerator for super type
	public static enum StoreSuperType {
		FOOD_DRINK, SHOPPING, POLLS, SALON, 
		GOD_DWELLING_PLACE, VIRTUAL_CHANNEL
	}
	@Persistent
	private StoreSuperType storeSuperType;
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long key;

    @Persistent
    private String restaurantTypeName;
    
    @Persistent
    private String restaurantTypeDescription;
    
    @Persistent
    private Integer storeTypeVersion;

    /**
     * RestaurantType constructor.
     * @param storeSuperType
     * 			: store super type
     * @param restaurantTypeName
     * 			: restaurant type name
     * @param restaurantTypeDescription
     * 			: restaurant type description
     * @throws MissingRequiredFieldsException
     */
    public RestaurantType(StoreSuperType storeSuperType, 
    		String restaurantTypeName, String restaurantTypeDescription) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (storeSuperType == null || restaurantTypeName == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	if (restaurantTypeName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
    	this.storeSuperType = storeSuperType;
        this.restaurantTypeName = restaurantTypeName;
        this.restaurantTypeDescription = restaurantTypeDescription;
        this.storeTypeVersion = 0; // Initialize the version in 0
    }

    /**
     * Get RestaurantType key.
     * @return restaurant type key
     */
    public Long getKey() {
        return key;
    }

    /**
     * Get store super type.
     * @return the store super type
     */
    public StoreSuperType getStoreSuperType() {
    	if (storeSuperType == null) {
    		//return StoreSuperType.FOOD_DRINK;
    		return null;
    	}
    	return storeSuperType;
    }
    
    /**
     * Get store super type as string.
     * @return the store super type as a string
     */
    public String getStoreSuperTypeString() {
    	if (storeSuperType == null) {
    		return "Not assigned yet";
    	}
    	switch (storeSuperType) {
    		case FOOD_DRINK:
    			return "Food and Drink";
    		case SHOPPING:
    			return "Shopping";
    		case POLLS:
    			return "News and Opinion Polls";
    		case SALON:
    			return "Salon";
    		case GOD_DWELLING_PLACE:
    			return "God Dwelling Place";
    		case VIRTUAL_CHANNEL:
    			return "Virtual Channel";
    		default:
    			return "";
    	}
    }
    
    /**
     * Get store super type from string.
     * @param a string representation of the super type
     * @return the store super type
     */
    public static StoreSuperType getStoreSuperTypeFromString(
    		String storeSuperTypeString) {
    	if (storeSuperTypeString == null ||
    			storeSuperTypeString.isEmpty()) {
    		return null;
    	}
    	if (storeSuperTypeString.equalsIgnoreCase("food_drink")) {
    		return StoreSuperType.FOOD_DRINK;
    	}
    	else if (storeSuperTypeString.equalsIgnoreCase("shopping")) {
    		return StoreSuperType.SHOPPING;
    	}
    	else if (storeSuperTypeString.equalsIgnoreCase("polls")) {
    		return StoreSuperType.POLLS;
    	}
    	else if (storeSuperTypeString.equalsIgnoreCase("salon")) {
    		return StoreSuperType.SALON;
    	}
    	else if (storeSuperTypeString.equalsIgnoreCase("god_dwelling_place")) {
    		return StoreSuperType.GOD_DWELLING_PLACE;
    	}
    	else if (storeSuperTypeString.equalsIgnoreCase("virtual_channel")) {
    		return StoreSuperType.VIRTUAL_CHANNEL;
    	}
    	else {
    		return null;
    	}
    }
    
    /**
     * Get RestaurantType name.
     * @return restaurant type name
     */
    public String getRestaurantTypeName() {
        return restaurantTypeName;
    }

    /**
     * Get RestaurantType description.
     * @return restaurant type description
     */
    public String getRestaurantTypeDescription() {
    	return restaurantTypeDescription;
    }
    
    /**
     * Get StoreType version.
     * @return store type version
     */
    public Integer getStoreTypeVersion() {
    	if (storeTypeVersion == null) {
    		storeTypeVersion = 0;
    	}
    	return storeTypeVersion;
    }
    
    /**
     * Set StoreSuperType.
     * @param storeSuperType
     * 			: store super type
     * @throws MissingRequiredFieldsException
     */
    public void setStoreSuperType(StoreSuperType storeSuperType)
    		throws MissingRequiredFieldsException {
    	if (storeSuperType == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Store super type is missing.");
    	}
    	this.storeSuperType = storeSuperType;
    }
    
    /**
     * Set RestaurantType name.
     * @param restaurantTypeName
     * 			: restaurant type name
     * @throws MissingRequiredFieldsException
     */
    public void setRestaurantTypeName(String restaurantTypeName)
    		throws MissingRequiredFieldsException {
    	if (restaurantTypeName == null || restaurantTypeName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Menu item type name is missing.");
    	}
    	this.restaurantTypeName = restaurantTypeName;
    }
    
    /**
     * Set RestaurantType description.
     * @param restaurantTypeDescription
     * 			: restaurant type description
     */
    public void setRestaurantTypeDescription(String restaurantTypeDescription) {
    	this.restaurantTypeDescription = restaurantTypeDescription;
    }
    
    /**
     * Update store type version by 1.
     * The store type version is updated every time a store
     * belonging to this store type is added, deleted or modified.
     */
    public void updateStoreTypeVersion() {
    	if (storeTypeVersion == null) {
    		storeTypeVersion = 0;
    	}
    	this.storeTypeVersion++;
    }
}