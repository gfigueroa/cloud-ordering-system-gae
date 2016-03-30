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
import com.google.appengine.api.datastore.Link;

import exceptions.InexistentObjectException;
import exceptions.InvalidFieldFormatException;
import exceptions.MissingRequiredFieldsException;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

/**
 * This class represents the Restaurant table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class Restaurant implements Serializable {
	
	public enum Service {
		NEWS, PRODUCTS, SERVICE_PROVIDERS, MESSAGES
	}
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent(dependent = "true", defaultFetchGroup = "true")
    private User user;
    
    @Persistent
    private Long restaurantType;
    
    @Persistent
    private Boolean hasNewsService;
    
    @Persistent
    private Boolean hasProductsService;
    
    @Persistent
    private Boolean hasServiceProvidersService;
    
    @Persistent
    private Boolean hasMessagesService;
    
    @Persistent
    private Integer menuVersion;
    
    @Persistent
    private Integer setVersion;
    
    @Persistent
    private Integer menuItemTypeVersion;
    
    @Persistent
    private Integer additionalPropertyVersion;
    
    @Persistent
    private Integer messageVersion;
    
    @Persistent
    private String restaurantName;
    
    @Persistent
    private Integer channelNumber;
    
    @Persistent
    private String restaurantDescription;
    
    @Persistent
    private Link restaurantWebsite;
    
    @Persistent
    private BlobKey restaurantLogo;
    
    @Persistent
    private Date restaurantOpeningTime;
    
    @Persistent
    private Date restaurantClosingTime;
    
    @Persistent
    private String restaurantComments;
    
    @Persistent
    @Element(dependent = "true")
    private ArrayList<MenuItem> menuItems;
    
    @Persistent
    @Element(dependent = "true")
    private ArrayList<Set> sets;
    
    @Persistent
    @Element(dependent = "true")
    private ArrayList<MenuItemType> menuItemTypes;
    
    @Persistent
    @Element(dependent = "true")
    private ArrayList<Branch> branches;
    
    @Persistent
    @Element(dependent = "true")
    private ArrayList<AdditionalPropertyType> additionalPropertyTypes;
    
    @Persistent
    @Element(dependent = "true")
    private ArrayList<RestaurantNews> restaurantNews;
    
    @Persistent
    @Element(dependent = "true")
    private ArrayList<RestaurantOpinionPoll> restaurantOpinionPolls;
    
    @Persistent
    @Element(dependent = "true")
    private ArrayList<Message> messages;
    
    @Persistent
    @Element(dependent = "true")
    private ArrayList<Survey> surveys;
    
    /**
     * Restaurant constructor.
     * @param user
     * 			: the user for this restaurant
     * @param restaurantType
     * 			: restaurant type key
     * @param hasNewsService
     * 			: whether this store has news service or not
     * @param hasProductsService
     * 			: whether this store has products service or not
     * @param hasServiceProvidersService
     * 			: whether this store has service providers service or not
     * @param hasMessagesService
     * 			: whether this store has messages service or not
     * @param restaurantName
     * 			: restaurant name
     * @param channelNumber
     * 			: channel number
     * @parma restaurantDescription
     * 			: restaurant description
     * @param restaurantWebsite
     * 			: restaurant website
     * @param restaurantLogo
     * 			: restaurant logo blob key
     * @param restaurantOpeningTime
     * 			: restaurant opening time
     * @param restaurantClosingTime
     * 			: restaurant closing time
     * @param restaurantComments
     * 			: restaurant comments
     * @throws MissingRequiredFieldsException
     * @throws InvalidFieldFormatException 
     */
    public Restaurant(User user, Long restaurantType, Boolean hasNewsService,
    		Boolean hasProductsService, Boolean hasServiceProvidersService,
    		Boolean hasMessagesService, String restaurantName, 
    		Integer channelNumber,
    		String restaurantDescription, Link restaurantWebsite, 
    		BlobKey restaurantLogo, Date restaurantOpeningTime, 
    		Date restaurantClosingTime, String restaurantComments) 
    		throws MissingRequiredFieldsException {
        
    	// Check "required field" constraints
    	if (user == null || restaurantType == null || hasNewsService == null ||
    			hasProductsService == null || hasServiceProvidersService == null ||
    			hasMessagesService == null || restaurantName == null || 
    			restaurantDescription == null) {
    		throw new MissingRequiredFieldsException(
    				this, "One or more required fields are missing.");
    	}
    	if (restaurantName.trim().isEmpty() || restaurantDescription.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(
    				this, "One or more required fields are missing.");
    	}
    	
    	// Check "required field" contraints for specific types
    	RestaurantType type = RestaurantTypeManager.getRestaurantType(restaurantType);
    	if (type.getStoreSuperType() == RestaurantType.StoreSuperType.VIRTUAL_CHANNEL &&
    			channelNumber == null) {
    		throw new MissingRequiredFieldsException(
    				this, "One or more required fields are missing.");
    	}
    	
    	this.user = user;
    	
    	// Create key with user email
    	this.key = KeyFactory.createKey(Restaurant.class.getSimpleName(), user.getUserEmail().getEmail());
    	
    	this.restaurantType = restaurantType;
    	this.hasNewsService = hasNewsService;
    	this.hasProductsService = hasProductsService;
    	this.hasServiceProvidersService = hasServiceProvidersService;
    	this.hasMessagesService = hasMessagesService;
    	
    	// Create empty lists
    	this.menuItems = new ArrayList<MenuItem>();
    	this.menuItemTypes = new ArrayList<MenuItemType>();
    	this.branches = new ArrayList<Branch>();
    	this.additionalPropertyTypes = new ArrayList<AdditionalPropertyType>();
    	this.restaurantNews = new ArrayList<RestaurantNews>();
    	this.restaurantOpinionPolls = new ArrayList<RestaurantOpinionPoll>();
    	this.surveys = new ArrayList<Survey>();
    	this.messages = new ArrayList<Message>();
    	
    	this.menuVersion = 0; // Initialize the version in 0
    	this.setVersion = 0; // Initialize the version in 0
    	this.menuItemTypeVersion = 0; // Initialize the version in 0
    	this.additionalPropertyVersion = 0; // Initialize the version in 0
    	this.messageVersion = 0; // Initialize the version in 0
    	
    	this.restaurantName = restaurantName;
    	this.channelNumber = channelNumber;
    	this.restaurantDescription = restaurantDescription;
        this.restaurantWebsite = restaurantWebsite;
        this.restaurantLogo = restaurantLogo;
        this.restaurantOpeningTime = restaurantOpeningTime;
        this.restaurantClosingTime = restaurantClosingTime;
        
        this.restaurantComments = restaurantComments;
    }

    /**
     * Get service from string.
     * @param serviceString
     * 			: the string representing the service
     * @return the service enum type which the given string represents
     */
    public static Service getServiceFromString(String serviceString) {
    	if (serviceString == null) {
    		return null;
    	}
    	if (serviceString.equalsIgnoreCase("news")) {
    		return Service.NEWS;
    	}
    	else if (serviceString.equalsIgnoreCase("products")) {
    		return Service.PRODUCTS;
    	}
    	else if (serviceString.equalsIgnoreCase("service_providers")) {
    		return Service.SERVICE_PROVIDERS;
    	}
    	else if (serviceString.equalsIgnoreCase("messages")) {
    		return Service.MESSAGES;
    	}
    	else {
    		return null;
    	}
    }
    
    /**
     * Get Restaurant key.
     * @return restaurant key
     */
    public Key getKey() {
        return key;
    }
    
    /**
     * Get Restaurant user.
     * @return restaurant user
     */
    public User getUser() {
        return user;
    }
    
    /**
     * Get Restaurant Type key.
     * @return restaurant type key
     */
    public Long getRestaurantType() {
        return restaurantType;
    }
    
    /**
     * Has news service.
     * @return whether this store has news service or not
     */
    public Boolean hasNewsService() {
    	if (hasNewsService == null) {
    		return false;
    	}
        return hasNewsService;
    }
    
    /**
     * Has products service.
     * @return whether this store has products service or not
     */
    public Boolean hasProductsService() {
    	if (hasProductsService == null) {
    		return false;
    	}
        return hasProductsService;
    }
    
    /**
     * Has service providers service.
     * @return whether this store has service providers service or not
     */
    public Boolean hasServiceProvidersService() {
    	if (hasServiceProvidersService == null) {
    		return false;
    	}
        return hasServiceProvidersService;
    }
    
    /**
     * Has messages service.
     * @return whether this store has messages service or not
     */
    public Boolean hasMessagesService() {
    	if (hasMessagesService == null) {
    		return false;
    	}
        return hasMessagesService;
    }
    
    /**
     * Get Restaurant MenuItem list.
     * @return restaurant menu items
     */
    public ArrayList<MenuItem> getMenuItems() {
        SystemCountersManager.increaseCounter(
        		SystemCounters.Counter.PRODUCT_ITEM, 
        		(long) menuItems.size());
    	return menuItems;
        // TODO : check this code
    }
    
    /**
     * Get Restaurant Set list
     * @return restaurant sets
     */
    public ArrayList<Set> getSets() {
    	return sets;
    }
    
    /**
     * Get Restaurant MenuItemType list.
     * @return restaurant menu item types
     */
    public ArrayList<MenuItemType> getMenuItemTypes() {
        return menuItemTypes;
    }
    
    /**
     * Get Restaurant Branch list.
     * @return restaurant branches
     */
    public ArrayList<Branch> getBranches() {
        return branches;
    }
    
    /**
     * Get Additional Property Type list.
     * @return restaurant additional property types
     */
    public ArrayList<AdditionalPropertyType> getAdditionalPropertyTypes() {
        return additionalPropertyTypes;
    }
    
    /**
     * Get Restaurant News list.
     * @return restaurant news
     */
    public ArrayList<RestaurantNews> getRestaurantNews() {
        return restaurantNews;
    }
    
    /**
     * Get Restaurant Opinion Poll list.
     * @return restaurant opinion polls
     */
    public ArrayList<RestaurantOpinionPoll> getRestaurantOpinionPolls() {
        return restaurantOpinionPolls;
    }
    
    /**
     * Get Restaurant Survey list.
     * @return restaurant surveys
     */
    public ArrayList<Survey> getSurveys() {
        return surveys;
    }
    
    /**
     * Get Message list.
     * @return messages
     */
    public ArrayList<Message> getMessages() {
        return messages;
    }
    
    /**
     * Get Restaurant's menu version.
     * The menu version increases by 1 each time a modification
     * is made to the restaurant menu items.
     * @return menu version
     */
    public Integer getMenuVersion() {
        return menuVersion;
    }
    
    /**
     * Get Restaurant's set version.
     * The set version increases by 1 each time a modification
     * is made to the restaurant sets
     * @return
     */
    public Integer getSetVersion() {
    	if (setVersion == null) {
    		setVersion = 0;
    	}
    	return setVersion;
    }
    
    /**
     * Get Restaurant's menu item type version.
     * The menu item type version increases by 1 each time a modification
     * is made to the restaurant menu item types.
     * @return menu item type version
     */
    public Integer getMenuItemTypeVersion() {
        return menuItemTypeVersion;
    }
    
    /**
     * Get Restaurant's additional property version.
     * The additional property version increases by 1 each time a modification
     * is made to the restaurant additional property.
     * @return additional property type version
     */
    public Integer getAdditionalPropertyVersion() {
    	if (additionalPropertyVersion == null) {
    		additionalPropertyVersion = 0;
    	}
        return additionalPropertyVersion;
    }
    
    /**
     * Get Restaurant's message version.
     * The message version increases by 1 each time a modification
     * is made to the messages.
     * @return additional property type version
     */
    public Integer getMessageVersion() {
    	if (messageVersion == null) {
    		messageVersion = 0;
    	}
        return messageVersion;
    }
    
    /**
     * Get Restaurant name.
     * @return restaurant name
     */
    public String getRestaurantName() {
        return restaurantName;
    }
    
    /**
     * Get Channel number.
     * @return channel number
     */
    public Integer getChannelNumber() {
        return channelNumber;
    }
    
    /**
     * Get Restaurant description.
     * @return restaurant description
     */
    public String getRestaurantDescription() {
        return restaurantDescription;
    }
    
    /**
     * Get Restaurant web site.
     * @return restaurant web site
     */
    public Link getRestaurantWebsite() {
        return restaurantWebsite;
    }
    
    /**
     * Get Restaurant logo.
     * @return restaurant logo blobkey
     */
    public BlobKey getRestaurantLogo() {
        return restaurantLogo;
    }
    
    /**
     * Get Restaurant opening time.
     * @return restaurant opening time
     */
    public Date getRestaurantOpeningTime() {
        return restaurantOpeningTime;
    }
    
    /**
     * Get Restaurant closing time.
     * @return restaurant opening time
     */
    public Date getRestaurantClosingTime() {
        return restaurantClosingTime;
    }
    
    /**
     * Get Restaurant comments.
     * @return restaurant comments
     */
    public String getRestaurantComments() {
    	return restaurantComments;
    }
    
    /**
     * Compare this restaurant with another Restaurant
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this Restaurant, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof Restaurant ) ) return false;
        Restaurant r = (Restaurant) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(r.getKey()));
    }
    
    /**
     * Add a new menuItem to the restaurant's menu.
     * @param menuItem
     * 			: new menuItem to be added
     */
    public void addMenuItem(MenuItem menuItem) {
    	this.menuItems.add(menuItem);
    }
    
    /**
     * Add a new set to the restaurant's menu.
     * @param set
     * 			: new set to be added
     * @param set
     */
    public void addSet(Set set) {
    	this.sets.add(set);
    }
    
    /**
     * Add a list of menuItem to the restaurant's menu.
     * @param menuItems
     * 			: new menu items to be added
     */
    public void addMenuItemAll(ArrayList<MenuItem> menuItems) {
    	this.menuItems.addAll(menuItems);
    }
    
    /**
     * Add a new menuItemType to the restaurant.
     * @param menuItem
     * 			: new menuItemType to be added
     */
    public void addMenuItemType(MenuItemType menuItemType) {
    	this.menuItemTypes.add(menuItemType);
    }
    
    /**
     * Add a new branch to the restaurant.
     * @param branch
     * 			: new branch to be added
     */
    public void addBranch(Branch branch) {
    	this.branches.add(branch);
    }
    
    /**
     * Add a new additional property type to this restaurant.
     * @param additionalPropertyType
     * 			: new additional property type to be added
     */
    public void addAdditionalPropertyType(
    		AdditionalPropertyType additionalPropertyType) {
    	this.additionalPropertyTypes.add(additionalPropertyType);
    }
    
    /**
     * Add a new restaurant news to this restaurant.
     * @param restaurantNews
     * 			: new restaurant news to be added
     */
    public void addRestaurantNews(RestaurantNews restaurantNews) {
    	this.restaurantNews.add(restaurantNews);
    }
    
    /**
     * Add a new restaurant opinion poll to this restaurant.
     * @param restaurantOpinionPoll
     * 			: new restaurant opinion poll to be added
     */
    public void addRestaurantOpinionPoll(
    		RestaurantOpinionPoll restaurantOpinionPoll) {
    	this.restaurantOpinionPolls.add(
    			restaurantOpinionPoll);
    }
    
    /**
     * Add a new survey to this restaurant.
     * @param survey
     * 			: new survey to be added
     */
    public void addSurvey(Survey survey) {
    	this.surveys.add(survey);
    }
    
    /**
     * Add a new message to this restaurant.
     * @param message
     * 			: new message to be added
     */
    public void addMessage(Message message) {
    	this.messages.add(message);
    }
    
    /**
     * Remove a menuItem from the restaurant's menu.
     * @param menuItem
     * 			: menuItem to be removed
     * @throws InexistentObjectException
     */
    public void removeMenuItem(MenuItem menuItem) 
    		throws InexistentObjectException {
    	if (!menuItems.remove(menuItem)) {
    		throw new InexistentObjectException(
    				MenuItem.class, "Menu Item not found!");
    	}
    }
    
    /**
     * Remove a set from the restaurant's menu.
     * @param set
     * 			: set to be removed
     * @throws InexistentObjectException 
     */
    public void removeSet(Set set) 
    		throws InexistentObjectException {
    	if (!sets.remove(set)) {
    		throw new InexistentObjectException(
    				Set.class, "Set not found!");
    	}
    }
    
    /**
     * Remove a menuItemType from the restaurant.
     * @param menuItemType
     * 			: menuItemType to be removed
     * @throws InexistentObjectException
     */
    public void removeMenuItemType(MenuItemType menuItemType) 
    		throws InexistentObjectException {
    	if (!menuItemTypes.remove(menuItemType)) {
    		throw new InexistentObjectException(
    				MenuItemType.class, "Menu Item Type not found!");
    	}
    }
    
    /**
     * Remove a branch from the restaurant.
     * @param branch
     * 			: branch to be removed
     * @throws InexistentObjectException
     */
    public void removeBranch(Branch branch) 
    		throws InexistentObjectException {
    	if (!branches.remove(branch)) {
    		throw new InexistentObjectException(
    				Branch.class, "Branch not found!");
    	}
    }
    
    /**
     * Remove an additional property type from the restaurant.
     * @param additionalPropertyType
     * 			: additional property type to be removed
     * @throws InexistentObjectException
     */
    public void removeAdditionalPropertyType(
    		AdditionalPropertyType additionalPropertyType) 
    		throws InexistentObjectException {
    	if (!additionalPropertyTypes.remove(additionalPropertyType)) {
    		throw new InexistentObjectException
    				(AdditionalPropertyType.class, 
    						"Additional property type not found!");
    	}
    }
    
    /**
     * Remove news from the restaurant.
     * @param restaurantNews
     * 			: restaurant news to be removed
     * @throws InexistentObjectException
     */
    public void removeRestaurantNews(RestaurantNews restaurantNews) 
    		throws InexistentObjectException {
    	if (!this.restaurantNews.remove(restaurantNews)) {
    		throw new InexistentObjectException
    				(RestaurantNews.class, "Restaurant News not found!");
    	}
    }
    
    /**
     * Remove opinion poll from the restaurant.
     * @param restaurantOpinionPoll
     * 			: restaurant opinion poll to be removed
     * @throws InexistentObjectException
     */
    public void removeRestaurantOpinionPoll(
    		RestaurantOpinionPoll restaurantOpinionPoll) 
    		throws InexistentObjectException {
    	if (!this.restaurantOpinionPolls.remove(restaurantOpinionPoll)) {
    		throw new InexistentObjectException
    				(RestaurantOpinionPoll.class, 
    						"Restaurant Opinion Poll not found!");
    	}
    }
    
    /**
     * Remove survey from the restaurant.
     * @param survey
     * 			: survey to be removed
     * @throws InexistentObjectException
     */
    public void removeSurvey(Survey survey) 
    		throws InexistentObjectException {
    	if (!this.surveys.remove(survey)) {
    		throw new InexistentObjectException
    				(Survey.class, "Survey not found!");
    	}
    }
    
    /**
     * Remove message from the restaurant.
     * @param message
     * 			: message to be removed
     * @throws InexistentObjectException
     */
    public void removeMessage(Message message) 
    		throws InexistentObjectException {
    	if (!this.messages.remove(message)) {
    		throw new InexistentObjectException
    				(Message.class, "Message not found!");
    	}
    }
    
    /**
     * Update the Menu Version number by 1.
     */
    public void updateMenuVersion() {
    	menuVersion++;
    }
    
    /**
     * Update the Set Version number by 1.
     */
    public void updateSetVersion() {
    	if (setVersion == null) {
    		setVersion = 0;
    	}
    	setVersion++;
    }
    
    /**
     * Update the Menu Item Type Version number by 1.
     */
    public void updateMenuItemTypeVersion() {
    	menuItemTypeVersion++;
    }
    
    /**
     * Update the Additional Property Version number by 1.
     */
    public void updateAdditionalPropertyVersion() {
    	if (additionalPropertyVersion == null) {
    		additionalPropertyVersion = 0;
    	}
    	additionalPropertyVersion++;
    }
    
    /**
     * Update the Message Version number by 1.
     */
    public void updateMessageVersion() {
    	if (messageVersion == null) {
    		messageVersion = 0;
    	}
    	messageVersion++;
    }
    
    /**
     * Set Restaurant type.
     * @param restaurantType
     * 			: restaurant type key
     * @throws MissingRequiredFieldsException
     */
    public void setRestaurantType(Long restaurantType) 
    		throws MissingRequiredFieldsException {
    	if (restaurantType == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Restaurant type is missing.");
    	}
    	this.restaurantType = restaurantType;
    }
    
    /**
     * Set has news service.
     * @param hasNewsService
     * 			: whether this store has news service or not
     * @throws MissingRequiredFieldsException 
     */
    public void setHasNewsService(Boolean hasNewsService) 
    		throws MissingRequiredFieldsException {
    	if (hasNewsService == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Has news service is missing.");
    	}
    	this.hasNewsService = hasNewsService;
    }
    
    /**
     * Set has products service.
     * @param hasProductsService
     * 			: whether this store has products service or not
     * @throws MissingRequiredFieldsException 
     */
    public void setHasProductsService(Boolean hasProductsService) 
    		throws MissingRequiredFieldsException {
    	if (hasProductsService == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Has products service is missing.");
    	}
    	this.hasProductsService = hasProductsService;
    }
    
    /**
     * Set has service providers service.
     * @param hasServiceProvidersService
     * 			: whether this store has service providers service or not
     * @throws MissingRequiredFieldsException 
     */
    public void setHasServiceProvidersService
    (Boolean hasServiceProvidersService) 
    		throws MissingRequiredFieldsException {
    	if (hasServiceProvidersService == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Has service providers service is missing.");
    	}
    	this.hasServiceProvidersService = hasServiceProvidersService;
    }
    
    /**
     * Set has messages service.
     * @param hasMessagesService
     * 			: whether this store has messages service or not
     * @throws MissingRequiredFieldsException 
     */
    public void setHasMessagesService (Boolean hasMessagesService) 
    		throws MissingRequiredFieldsException {
    	if (hasMessagesService == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Has messages service is missing.");
    	}
    	this.hasMessagesService = hasMessagesService;
    }
     
    /**
     * Set Restaurant name.
     * @param restaurantName
     * 			: restaurant name
     * @throws MissingRequiredFieldsException
     */
    public void setRestaurantName(String restaurantName)
    		throws MissingRequiredFieldsException {
    	if (restaurantName == null || restaurantName.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Restaurant name is missing.");
    	}
    	this.restaurantName = restaurantName;
    }
    
    /**
     * Set Channel number.
     * @param channelNumber
     * 			: channelNumber
     * @throws MissingRequiredFieldsException
     */
    public void setChannelNumber(RestaurantType.StoreSuperType storeSuperType,
    		Integer channelNumber)
    		throws MissingRequiredFieldsException {
    	if (storeSuperType == RestaurantType.StoreSuperType.VIRTUAL_CHANNEL &&
    			channelNumber == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Channel number is missing.");
    	}
    	this.channelNumber = channelNumber;
    }
    
    /**
     * Set Restaurant description.
     * @param restaurantDescription
     * 			: restaurant description
     * @throws MissingRequiredFieldsException
     */
    public void setRestaurantDescription(String restaurantDescription)
    		throws MissingRequiredFieldsException {
    	if (restaurantDescription == null || 
    			restaurantDescription.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Restaurant Description is missing.");
    	}
    	this.restaurantDescription = restaurantDescription;
    }
    
    /**
     * Set Restaurant web site.
     * @param restaurantWebsite
     * 			: restaurant web site
     */
    public void setRestaurantWebsite(Link restaurantWebsite) {
    	this.restaurantWebsite = restaurantWebsite;
    }
    
    /**
     * Set Restaurant logo.
     * @param restaurantLogo
     * 			: restaurant logo blob key
     */
    public void setRestaurantLogo(BlobKey restaurantLogo) {
    	this.restaurantLogo = restaurantLogo;
    }
    
    /**
     * Set Restaurant opening time.
     * @param restaurantOpeningTime
     * 			: restaurant opening time
     */
    public void setRestaurantOpeningTime(Date restaurantOpeningTime) {
    	this.restaurantOpeningTime = restaurantOpeningTime;
    }
    
    /**
     * Set Restaurant closing time.
     * @param restaurantClosingTime
     * 			: restaurant closing time
     */
    public void setRestaurantClosingTime(Date restaurantClosingTime) {
    	this.restaurantClosingTime = restaurantClosingTime;
    }
    
    /**
     * Set Restaurant comments.
     * @param restaurantComments
     * 			: restaurant comments
     */
    public void setRestaurantComments(String restaurantComments) {
    	this.restaurantComments = restaurantComments;
    }
    
    /**
     * Return Restaurant information as a String.
     * @return String representation of this Restaurant
     */
    public String toString() {
    	String information = "";
    	
    	information += this.user.toString() + "\n";
    	
    	if (this.key == null) {
    		information += "Restaurant key: NULL (object is transient) \n";
    	}
    	else {
    		information += "Restaurant key: " + this.key.toString() + "\n";
    	}
    	
    	information += "Restaurant menu version: " + this.menuVersion + "\n";
    	information += "Restaurant menu item type version: " + this.menuItemTypeVersion + "\n";
    	information += "Restaurant additional property version: " + this.additionalPropertyVersion + "\n";
    	information += "Restaurant message version: " + this.messageVersion + "\n";
    	information += "Restaurant name: " + this.restaurantName + "\n";
    	information += "Restaurant website: " + this.restaurantWebsite + "\n";
    	information += "Restaurant logo blobkey: " + this.restaurantLogo + "\n";
    	information += "Restaurant opening time: " + this.restaurantOpeningTime + "\n";
    	information += "Restaurant closing time: " + this.restaurantClosingTime + "\n";
    	information += "Restaurant comments: " + this.restaurantComments;
    	
    	return information;
    }
    
}
