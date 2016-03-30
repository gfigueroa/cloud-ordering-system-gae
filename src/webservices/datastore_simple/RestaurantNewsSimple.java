/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;

import com.google.appengine.api.blobstore.BlobKey;

/**
 * This class represents a simple version of the RestaurantNews table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class RestaurantNewsSimple implements Serializable {
    
	public String key;
	public String restaurantKey;
	public String restaurantNewsTitle;
	public String restaurantNewsContent;
	public Boolean allowResponse;
	public Integer currentClicks;
	public Integer maxClicks;
	public Boolean userAlreadyClicked;
	public String restaurantNewsEndingDate;
	public Integer restaurantNewsPriority;
	public BlobKey restaurantNewsImage;
    
    /**
     * RestaurantNewsSimple constructor.
     * @param key
     * 			: RestaurantNews key string
     * @param restaurantKey
     * 			: Key of the restaurant whose this news belongs to
     * @param restaurantNewsTitle
     * 			: The title of the restaurant News
     * @param restaurantNewsContent
     * 			: The content of the restaurant News
     * @param allowResponse
     * 			: Whether this news allows user response or not
     * @param currentClicks
     * 			: The current number of times this news has been clicked
     * @param maxClicks
     * 			: The maximum number of allowed clicks for this news
     * @param userAlreadyClicked
     * 			: Whether the user already clicked the news or not
     * @param restaurantNewsEndingDate
     * 			: The date this news will finish
     * @param restaurantNewsPriority
     * 			: The priority for this news
     * @param restaurantNewsImage
     * 			: This news' image
     */
    public RestaurantNewsSimple(String key, String restaurantKey,
    		String restaurantNewsTitle, String restaurantNewsContent,
    		Boolean allowResponse, Integer currentClicks, Integer maxClicks,
    		Boolean userAlreadyClicked, String restaurantNewsEndingDate,
    		Integer restaurantNewsPriority, BlobKey restaurantNewsImage) {

    	this.key = key;
    	this.restaurantKey = restaurantKey;
    	this.restaurantNewsTitle = restaurantNewsTitle;
    	this.restaurantNewsContent = restaurantNewsContent;
    	this.allowResponse = allowResponse;
    	this.currentClicks = currentClicks;
    	this.maxClicks = maxClicks;
    	this.userAlreadyClicked = userAlreadyClicked;
    	this.restaurantNewsEndingDate = restaurantNewsEndingDate;
    	this.restaurantNewsPriority = restaurantNewsPriority;
    	this.restaurantNewsImage = restaurantNewsImage;
    }
    
    /**
     * Compare this restaurant News with another RestaurantNews
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this RestaurantNews, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof RestaurantNewsSimple ) ) return false;
        RestaurantNewsSimple r = (RestaurantNewsSimple) o;
        return this.key.equals(r.key);
    }
    
}
