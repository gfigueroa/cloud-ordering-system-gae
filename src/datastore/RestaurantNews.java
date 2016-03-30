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

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the RestaurantNews table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class RestaurantNews implements Serializable {

	public static enum Status {
		INACTIVE, ACTIVE, ACTIVE_MAXED, EXPIRED, EXPIRED_MAXED
	}
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String restaurantNewsTitle;
    
    @Persistent
    private String restaurantNewsContent;
    
    @Persistent
    private Boolean allowResponse;
    
    @Persistent
    private Integer currentClicks;
    
    @Persistent
    private Integer maxClicks;
    
    @Persistent
    private Date restaurantNewsCreationDate;
    
    @Persistent
    private Date restaurantNewsStartingDate;
    
    @Persistent
    private Date restaurantNewsEndingDate;
    
    @Persistent
    private Integer restaurantNewsPriority;
    
    @Persistent
    private Boolean isPrivate;
    
    @Persistent
    private BlobKey restaurantNewsImage;

    /**
     * RestaurantNews constructor.
     * @param restaurantNewsTitle
     * 			: restaurant news title
     * @param restaurantNewsContent
     * 			: restaurant news content
     * @param allowResponse
     * 			: whether this news allows response or not
     * @param maxClicks
     * 			: max clicks permitted for this news
     * @param restaurantNewsStartingDate
     * 			: the date this news will start to be available
     * @param restaurantNewsEndingDate
     * 			: the date this news will finish
     * @param restaurantNewsPriority
     * 			: the priority of this news
     * @param isPrivate
     * 			: whether this news is private or not
     * @param restaurantNewsImage
     * 			: the image of this news
     * @throws MissingRequiredFieldsException
     */
    public RestaurantNews(String restaurantNewsTitle, String restaurantNewsContent, 
    		Boolean allowResponse, Integer maxClicks,
    		Date restaurantNewsStartingDate, Date restaurantNewsEndingDate, 
    		Integer restaurantNewsPriority, Boolean isPrivate,
    		BlobKey restaurantNewsImage) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (restaurantNewsTitle == null || restaurantNewsContent == null || 
    			allowResponse == null || restaurantNewsStartingDate == null || 
    			restaurantNewsEndingDate == null || restaurantNewsPriority == null ||
    			isPrivate == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	if (restaurantNewsTitle.trim().isEmpty() ||
    			restaurantNewsContent.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
    	// Checked "required fields" for specific types
    	if (allowResponse) {
    		if (maxClicks == null) {
    			throw new MissingRequiredFieldsException(this.getClass(), 
        				"One or more required fields are missing.");
    		}
    	}
    	
    	this.restaurantNewsTitle = restaurantNewsTitle;
        this.restaurantNewsContent = restaurantNewsContent;
        this.allowResponse = allowResponse;
        this.currentClicks = 0;
        this.maxClicks = maxClicks;
        this.restaurantNewsCreationDate = new Date();
        this.restaurantNewsStartingDate = restaurantNewsStartingDate;
        this.restaurantNewsEndingDate = restaurantNewsEndingDate;
        this.restaurantNewsPriority = restaurantNewsPriority;
        this.isPrivate = isPrivate;
        this.restaurantNewsImage = restaurantNewsImage;
    }

    /**
     * Get RestaurantNews key.
     * @return Restaurant News key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Get RestaurantNews title.
     * @return Restaurant News title
     */
    public String getRestaurantNewsTitle() {
        return restaurantNewsTitle;
    }
    
    /**
     * Get RestaurantNews content.
     * @return Restaurant News content
     */
    public String getRestaurantNewsContent() {
        return restaurantNewsContent;
    }
    
    /**
     * Whether this news allows user response or not
     * @return True if news allows user response, False otherwise
     */
    public Boolean allowsResponse() {
        return allowResponse;
    }
    
    /**
     * Get Current number of clicks.
     * @return The number of users that have clicked this news
     */
    public Integer getCurrentClicks() {
        return currentClicks;
    }
    
    /**
     * Get max number of clicks.
     * @return The max number of users that can click this news
     */
    public Integer getMaxClicks() {
        return maxClicks;
    }
    
    /**
     * Get the date this news was created
     * @return The date this news was created
     */
    public Date getRestaurantNewsCreationDate() {
        return restaurantNewsCreationDate;
    }
    
    /**
     * Get the date when this news will be available
     * @return The date when this news will be available
     */
    public Date getRestaurantNewsStartingDate() {
        return restaurantNewsStartingDate;
    }
    
    /**
     * Get the date when this news will be stop being available
     * @return The date when this news will expire
     */
    public Date getRestaurantNewsEndingDate() {
        return restaurantNewsEndingDate;
    }
    
    /**
     * Get news priority.
     * @return The priority of this news represented as a number
     */
    public Integer getRestaurantNewsPriority() {
        return restaurantNewsPriority;
    }
    
    /**
     * News is private.
     * @return True if this news is private, false otherwise
     */
    public Boolean isPrivate() {
    	// Return false if isPrivate is null, as originally all news
    	// were public.
    	if (isPrivate == null) {
    		return false;
    	}
        return isPrivate;
    }
    
    /**
     * Get RestaurantNews image.
     * @return restaurant news image blobkey
     */
    public BlobKey getRestaurantNewsImage() {
        return restaurantNewsImage;
    }
    
    /**
     * Get the current status of this restaurant news
     * @return The current status of this news
     */
    public Status getCurrentStatus() {
        // INACTIVE, ACTIVE, ACTIVE_MAXED, EXPIRED, EXPIRED_MAXED

    	boolean tempAllowResponse;
    	if (allowResponse == null) {
    		tempAllowResponse = true;
    	}
    	else {
    		tempAllowResponse = allowResponse;
    	}
    	
    	Date now = new Date();
    	if (now.compareTo(restaurantNewsStartingDate) < 0) {
    		return Status.INACTIVE;
    	}
    	if (tempAllowResponse) {
	    	if (now.compareTo(restaurantNewsEndingDate) > 0) {
				if (currentClicks < maxClicks) {
					return Status.EXPIRED;
				}
				else {
					return Status.EXPIRED_MAXED;
				}
			}
			else {
				if (currentClicks < maxClicks) {
					return Status.ACTIVE;
				}
				else {
					return Status.ACTIVE_MAXED;
				}
			}
    	}
    	else {
    		if (now.compareTo(restaurantNewsEndingDate) > 0) {
    			return Status.EXPIRED;
    		}
    		else {
    			return Status.ACTIVE;
    		}
    	}
    }
    
    /**
     * Compare this Restaurant News with another Restaurant News
     * @param o
     * 			: the object to compare
     * @return true if the object to compare is equal to this RestaurantNews, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof RestaurantNews ) ) return false;
        RestaurantNews restaurantNews = (RestaurantNews) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(restaurantNews.getKey()));
    }
    
    /**
     * Add a new click to this news
     * @return True if this news is still ACTIVE, False otherwise
     * 
     */
    public boolean addClick() {
    	if (getCurrentStatus() == Status.ACTIVE ) {
    		currentClicks++;	
    		return true;
    	}
    	else if (getCurrentStatus() == Status.ACTIVE_MAXED) {
    		currentClicks++;
    		return false;
    	}
    	else {
    		return false;
    	}
    }
    
    /**
     * Set Restaurant News title.
     * @param restaurantNewsTitle
     * 			: the title of this news
     * @throws MissingRequiredFieldsException
     */
    public void setRestaurantNewsTitle(String restaurantNewsTitle)
    		throws MissingRequiredFieldsException {
    	if (restaurantNewsTitle == null || restaurantNewsTitle.isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Restaurant news title is missing.");
    	}
    	this.restaurantNewsTitle = restaurantNewsTitle;
    }
    
    /**
     * Set Restaurant News Content.
     * @param restaurantNewsContent
     * 			: the Content of this news
     * @throws MissingRequiredFieldsException
     */
    public void setRestaurantNewsContent(String restaurantNewsContent)
    		throws MissingRequiredFieldsException {
    	if (restaurantNewsContent == null || restaurantNewsContent.isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Restaurant news Content is missing.");
    	}
    	this.restaurantNewsContent = restaurantNewsContent;
    }
    
    /**
     * Set Restaurant News max clicks.
     * @param maxClicks
     * 			: the max number of users that can click on this news
     * @throws MissingRequiredFieldsException
     */
    public void setMaxClicks(Integer maxClicks)
    		throws MissingRequiredFieldsException {
    	if (allowResponse == null || allowResponse) {
	    	if (maxClicks == null) {
	    		throw new MissingRequiredFieldsException(this.getClass(), 
	    				"Max clicks is missing.");
	    	}
    	}
    	this.maxClicks = maxClicks;
    }
    
    /**
     * Set Restaurant News Starting Date.
     * @param restaurantNewsStartingDate
     * 			: the date this news will be available
     * @throws MissingRequiredFieldsException
     */
    public void setRestaurantNewsStartingDate(Date restaurantNewsStartingDate)
    		throws MissingRequiredFieldsException {
    	if (restaurantNewsStartingDate == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Restaurant news starting date is missing.");
    	}
    	this.restaurantNewsStartingDate = restaurantNewsStartingDate;
    }
    
    /**
     * Set Restaurant News Ending Date.
     * @param restaurantNewsEndingDate
     * 			: the date this news will stop (expire)
     * @throws MissingRequiredFieldsException
     */
    public void setRestaurantNewsEndingDate(Date restaurantNewsEndingDate)
    		throws MissingRequiredFieldsException {
    	if (restaurantNewsEndingDate == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Restaurant news ending date is missing.");
    	}
    	this.restaurantNewsEndingDate = restaurantNewsEndingDate;
    }
    
    /**
     * Set Restaurant News priority.
     * @param restaurantNewsPriority
     * 			: the priority given to this news (as a number)
     * @throws MissingRequiredFieldsException
     */
    public void setRestaurantNewsPriority(Integer restaurantNewsPriority)
    		throws MissingRequiredFieldsException {
    	if (restaurantNewsPriority == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Restaurant news priority is missing.");
    	}
    	this.restaurantNewsPriority = restaurantNewsPriority;
    }
    
    /**
     * Set News is private.
     * @param isPrivate
     * 			: whether this news is private or not
     * @throws MissingRequiredFieldsException
     */
    public void setIsPrivate(Boolean isPrivate)
    		throws MissingRequiredFieldsException {
    	if (isPrivate == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Is Private is missing.");
    	}
    	this.isPrivate = isPrivate;
    }
    
    /**
     * Set RestaurantNews image.
     * @param restaurantNewsImage
     * 			: restaurant news image blobkey
     */
    public void setRestaurantNewsImage(BlobKey restaurantNewsImage) {
    	this.restaurantNewsImage = restaurantNewsImage;
    }

}