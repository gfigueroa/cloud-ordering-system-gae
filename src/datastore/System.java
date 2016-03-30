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

import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the System table.
 * The System table stores some version numbers required by the Mobile App to download
 * data from the web server.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class System implements Serializable {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long key;

    @Persistent
    private Integer restaurantListVersion;
    
    @Persistent
    private Date restaurantListTimestamp;
    
    @Persistent
    private Integer restaurantTypeListVersion;
    
    @Persistent
    private Date restaurantTypeListTimestamp;
    
    @Persistent
    private Integer storeListVersionFoodDrink;
    
    @Persistent
    private Date storeListTimestampFoodDrink;
    
    @Persistent
    private Integer storeListVersionShopping;
    
    @Persistent
    private Date storeListTimestampShopping;
    
    @Persistent
    private Integer storeListVersionPolls;
    
    @Persistent
    private Date storeListTimestampPolls;
    
    @Persistent
    private Integer storeListVersionSalon;
    
    @Persistent
    private Date storeListTimestampSalon;
    
    @Persistent
    private Integer storeListVersionGodDwellingPlace;
    
    @Persistent
    private Date storeListTimestampGodDwellingPlace;
    
    @Persistent
    private Integer storeListVersionVirtualChannel;
    
    @Persistent
    private Date storeListTimestampVirtualChannel;
    
    @Persistent
    private Integer oldestAppVersionSupported1;
    
    @Persistent
    private Integer oldestAppVersionSupported2;
    
    @Persistent
    private Integer oldestAppVersionSupported3;
    
    @Persistent
    private Date systemTime;

    /**
     * System constructor.
     */
    public System() {
    	restaurantListVersion = 0;
    	restaurantListTimestamp = new Date();
    	restaurantTypeListVersion = 0;
    	restaurantTypeListTimestamp = new Date();
    	storeListVersionFoodDrink = 0;
    	storeListTimestampFoodDrink = new Date();
    	storeListVersionShopping = 0;
    	storeListTimestampShopping = new Date();
    	storeListVersionPolls = 0;
    	storeListTimestampPolls = new Date();
    	storeListVersionSalon = 0;
    	storeListTimestampSalon = new Date();
    	storeListVersionGodDwellingPlace = 0;
    	storeListTimestampGodDwellingPlace = new Date();
    	storeListVersionVirtualChannel = 0;
    	storeListTimestampVirtualChannel = new Date();
    	oldestAppVersionSupported1 = 1;
    	oldestAppVersionSupported2 = 0;
    	oldestAppVersionSupported3 = 0;
    	
    	Date now = new Date();
    	this.systemTime = now;
    }

    /**
     * Get System key.
     * @return system key
     */
    public Long getKey() {
        return key;
    }

    /**
     * Get Restaurant List Version.
     * @return restaurant list version
     */
    public Integer getRestaurantListVersion() {
        return restaurantListVersion;
    }
    
    /**
     * Get Restaurant Type List Version.
     * @return restaurant type list version
     */
    public Integer getRestaurantTypeListVersion() {
        return restaurantTypeListVersion;
    }
    
    /**
     * Get Store List Version.
     * @param storeSuperType
     * 			: store super type
     * @return store list version given the store super type
     */
    public Integer getStoreListVersion(
    		RestaurantType.StoreSuperType storeSuperType) {
    	switch (storeSuperType) {
    		case FOOD_DRINK:
    			return storeListVersionFoodDrink != null ? 
    					storeListVersionFoodDrink : 0;
    		case SHOPPING:
    			return storeListVersionShopping != null ?
    					storeListVersionShopping : 0;
    		case POLLS:
    			return storeListVersionPolls != null ?
    					storeListVersionPolls : 0;
    		case SALON:
    			return storeListVersionSalon != null ?
    					storeListVersionSalon : 0;
    		case GOD_DWELLING_PLACE:
    			return storeListVersionGodDwellingPlace != null ?
    					storeListVersionGodDwellingPlace : 0;
    		case VIRTUAL_CHANNEL:
    			return storeListVersionVirtualChannel != null ?
    					storeListVersionVirtualChannel : 0;
    		default:
    			return null;
    	}
    }
    
    /**
     * Get oldest mobile app version supported by this
     * server version.
     * @return oldest app version supported
     */
    public String getOldestAppVersionSupportedString() {
        if (oldestAppVersionSupported1 == null ||
        		oldestAppVersionSupported2 == null ||
        		oldestAppVersionSupported3 == null) {
        	return "";
        }
        else {
	    	return oldestAppVersionSupported1 + "." + 
	        		oldestAppVersionSupported2 + "." + 
	        		oldestAppVersionSupported3;
        }
    }
    
    /**
     * Get first digit of oldest mobile app version supported 
     * by this server version.
     * @return first digit of oldest app version supported
     */
    public Integer getOldestAppVersionSupported1() {
        return oldestAppVersionSupported1;
    }
    
    /**
     * Get second digit of oldest mobile app version supported 
     * by this server version.
     * @return second digit of oldest app version supported
     */
    public Integer getOldestAppVersionSupported2() {
        return oldestAppVersionSupported2;
    }
    
    /**
     * Get third digit of oldest mobile app version supported 
     * by this server version.
     * @return third digit of oldest app version supported
     */
    public Integer getOldestAppVersionSupported3() {
        return oldestAppVersionSupported3;
    }
    
    /**
     * Get time when last update was made.
     * @return system time
     */
    public Date getSystemTime() {
        return systemTime;
    }
    
    /**
     * Compare this system instance with another syste,
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this System, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof System ) ) return false;
        System system = (System) o;
        return (this.getKey() == system.getKey());
    }
    
    /**
     * Update the Restaurant List Version number by 1.
     */
    public void updateRestaurantListVersion() {
    	restaurantListVersion++;
    	systemTime = new Date();
    }
    
    /**
     * Update the Restaurant Type List Version number by 1.
     */
    public void updateRestaurantTypeListVersion() {
    	restaurantTypeListVersion++;
    	systemTime = new Date();
    }
    
    /**
     * Update the Store List version number by 1 given the
     * store super type
     * @param storeSuperType
     * 			: store super type
     */
    public void updateStoreListVersion(
    		RestaurantType.StoreSuperType storeSuperType) {
    	
    	if (storeSuperType == null) {
    		return;
    	}
    	
    	switch (storeSuperType) {
    		case FOOD_DRINK:
    			if (storeListVersionFoodDrink == null) {
    				storeListVersionFoodDrink = 1;
    			}
    			else {
    				storeListVersionFoodDrink++;
    			}
    			break;
    		case SHOPPING:
    			if (storeListVersionShopping == null) {
    				storeListVersionShopping = 1;
    			}
    			else {
    				storeListVersionShopping++;
    			}
    			break;
    		case POLLS:
    			if (storeListVersionPolls == null) {
    				storeListVersionPolls = 1;
    			}
    			else {
    				storeListVersionPolls++;
    			}
    			break;
    		case SALON:
    			if (storeListVersionSalon == null) {
    				storeListVersionSalon = 1;
    			}
    			else {
    				storeListVersionSalon++;
    			}
    			break;
    		case GOD_DWELLING_PLACE:
    			if (storeListVersionGodDwellingPlace == null) {
    				storeListVersionGodDwellingPlace = 1;
    			}
    			else {
    				storeListVersionGodDwellingPlace++;
    			}
    		case VIRTUAL_CHANNEL:
    			if (storeListVersionVirtualChannel == null) {
    				storeListVersionVirtualChannel = 1;
    			}
    			else {
    				storeListVersionVirtualChannel++;
    			}
    		default:
    			return;
    	}
		systemTime = new Date();
    }
    
    /**
     * Set first digit of the 
     * oldest app version supported by this server version.
     * @param oldestAppVersionSupported1
     * 			: the first digit of the oldest app version 
     * 			  supported by this server
     * @throws MissingRequiredFieldsException 
     */
    public void setOldestAppVersionSupported1(Integer oldestAppVersionSupported1) 
    		throws MissingRequiredFieldsException {
    	// Check required field constraint
    	if (oldestAppVersionSupported1 == null) {
    		throw new MissingRequiredFieldsException(
    				this, "Missing oldest app version supported digit 1.");
    	}
    	this.oldestAppVersionSupported1 = oldestAppVersionSupported1;
    	systemTime = new Date();
    }
    
    /**
     * Set second digit of the 
     * oldest app version supported by this server version.
     * @param oldestAppVersionSupported2
     * 			: the second digit of the oldest app version 
     * 			  supported by this server
     * @throws MissingRequiredFieldsException 
     */
    public void setOldestAppVersionSupported2(Integer oldestAppVersionSupported2) 
    		throws MissingRequiredFieldsException {
    	// Check required field constraint
    	if (oldestAppVersionSupported2 == null) {
    		throw new MissingRequiredFieldsException(
    				this, "Missing oldest app version supported digit 2.");
    	}
    	this.oldestAppVersionSupported2 = oldestAppVersionSupported2;
    	systemTime = new Date();
    }
    
    /**
     * Set third digit of the 
     * oldest app version supported by this server version.
     * @param oldestAppVersionSupported3
     * 			: the third digit of the oldest app version 
     * 			  supported by this server
     * @throws MissingRequiredFieldsException 
     */
    public void setOldestAppVersionSupported3(Integer oldestAppVersionSupported3) 
    		throws MissingRequiredFieldsException {
    	// Check required field constraint
    	if (oldestAppVersionSupported3 == null) {
    		throw new MissingRequiredFieldsException(
    				this, "Missing oldest app version supported digit 3.");
    	}
    	this.oldestAppVersionSupported3 = oldestAppVersionSupported3;
    	systemTime = new Date();
    }
    
}