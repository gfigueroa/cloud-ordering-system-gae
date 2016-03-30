/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.appengine.api.blobstore.BlobKey;

import datastore.RestaurantOpinionPoll;

/**
 * This class represents a simple version of the RestaurantOpinionPoll table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class RestaurantOpinionPollSimple implements Serializable {
    
	public String key;
	public String restaurantKey;
	public RestaurantOpinionPoll.Type restaurantOpinionPollType;
	public String restaurantOpinionPollTitle;
	public String restaurantOpinionPollContent;
	public Integer currentClicks;
	public String restaurantOpinionPollEndingDate;
	public Integer restaurantOpinionPollPriority;
	public Boolean publicResults;
	public BlobKey restaurantOpinionPollImage;
	public String binaryChoice1;
	public String binaryChoice2;
	public Integer ratingLowValue;
	public Integer ratingHighValue;
	public ArrayList<OpinionPollRatingEntrySimple> ratingEntries;
	public Boolean allowMultipleSelection;
	public ArrayList<String> multipleChoiceValues;
    
    /**
     * RestaurantOpinionPollSimple constructor.
     * @param key
     * 			: RestaurantOpinionPoll key string
     * @param restaurantKey
     * 			: Key of the restaurant whose this opinion poll belongs to
     * @param restaurantOpinionPollType
     * 			: The type of opinion poll
     * @param restaurantOpinionPollTitle
     * 			: The title of the restaurant OpinionPoll
     * @param restaurantOpinionPollContent
     * 			: The content of the restaurant OpinionPoll
     * @param currentClicks
     * 			: The current number of times this opinion poll has been clicked
     * @param restaurantOpinionPollEndingDate
     * 			: The date this opinion poll will finish
     * @param restaurantOpinionPollPriority
     * 			: The priority for this opinion poll
     * @param publicResults
     * 			: Whether the results of this opinion poll are public
     * @param restaurantOpinionPollImage
     * 			: This opinion poll's image
     * @param binaryChoice1
     * 			: Binary choice 1
     * @param binaryChoice2
     * 			: Binary choice 2
     * @param ratingLowValue
     * 			: Lowest rating value
     * @param ratingHighValue
     * 			: Highest rating value
     * @param ratingEntries
     * 			: The rating entries
     * @param allowMultipleSelection
     * 			: Allow multiple selection or not
     * @param multipleChoiceValues
     * 			: The possible values for multiple choice type
     */
    public RestaurantOpinionPollSimple(String key, String restaurantKey, 
    		RestaurantOpinionPoll.Type restaurantOpinionPollType,
    		String restaurantOpinionPollTitle, String restaurantOpinionPollContent,
    		Integer currentClicks, String restaurantOpinionPollEndingDate,
    		Integer restaurantOpinionPollPriority, Boolean publicResults,
    		BlobKey restaurantOpinionPollImage,
    		String binaryChoice1, String binaryChoice2, Integer ratingLowValue,
    		Integer ratingHighValue, ArrayList<OpinionPollRatingEntrySimple> ratingEntries,
    		Boolean allowMultipleChoiceSelection,
    		ArrayList<String> multipleChoiceValues) {

    	this.key = key;
    	this.restaurantKey = restaurantKey;
    	this.restaurantOpinionPollType = restaurantOpinionPollType;
    	this.restaurantOpinionPollTitle = restaurantOpinionPollTitle;
    	this.restaurantOpinionPollContent = restaurantOpinionPollContent;
    	this.currentClicks = currentClicks;
    	this.restaurantOpinionPollEndingDate = restaurantOpinionPollEndingDate;
    	this.restaurantOpinionPollPriority = restaurantOpinionPollPriority;
    	this.publicResults = publicResults;
    	this.restaurantOpinionPollImage = restaurantOpinionPollImage;
    	this.binaryChoice1 = binaryChoice1;
    	this.binaryChoice2 = binaryChoice2;
    	this.ratingLowValue = ratingLowValue;
    	this.ratingHighValue = ratingHighValue;
    	this.ratingEntries = ratingEntries;
    	this.allowMultipleSelection = allowMultipleChoiceSelection;
    	this.multipleChoiceValues = multipleChoiceValues;
    }
    
    /**
     * Compare this restaurant OpinionPoll with another RestaurantOpinionPoll
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this RestaurantOpinionPoll, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof RestaurantOpinionPollSimple ) ) return false;
        RestaurantOpinionPollSimple r = (RestaurantOpinionPollSimple) o;
        return this.key.equals(r.key);
    }
    
}
