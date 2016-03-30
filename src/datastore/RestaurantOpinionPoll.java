/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import exceptions.InexistentObjectException;
import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the RestaurantOpinionPoll table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class RestaurantOpinionPoll implements Serializable {

	public static enum Status {
		INACTIVE, ACTIVE, EXPIRED
	}
	
	public static enum Type {
		BINARY, RATING, MULTIPLE_CHOICE, FREE_RESPONSE
	}
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private Type restaurantOpinionPollType;

    @Persistent
    private String restaurantOpinionPollTitle;
    
    @Persistent
    private String restaurantOpinionPollContent;
    
    @Persistent
    private Integer currentClicks;
    
    @Persistent
    private Date restaurantOpinionPollCreationDate;
    
    @Persistent
    private Date restaurantOpinionPollStartingDate;
    
    @Persistent
    private Date restaurantOpinionPollEndingDate;
    
    @Persistent
    private Integer restaurantOpinionPollPriority;
    
    @Persistent
    private Boolean publicResults;
    
    @Persistent
    private BlobKey restaurantOpinionPollImage;
    
    @Persistent
    private String binaryChoice1;
    
    @Persistent
    private String binaryChoice2;
    
    @Persistent
    private Integer ratingLowValue;
    
    @Persistent
    private Integer ratingHighValue;
    
    @Persistent(defaultFetchGroup = "true")
    @Element(dependent = "true")
    private ArrayList<OpinionPollRatingEntry> ratingEntries;
    
    @Persistent
    private Boolean allowMultipleSelection;
    
    @Persistent(defaultFetchGroup = "true")
    @Element(dependent = "true")
    private ArrayList<RestaurantOpinionPollMultipleChoiceValue> multipleChoiceValues;

    /**
     * RestaurantOpinionPoll constructor.
     * @param restaurantOpinionPollType
     * 			: opinion poll type
     * @param restaurantOpinionPollTitle
     * 			: opinion poll title
     * @param restaurantOpinionPollContent
     * 			: opinion poll content
     * @param restaurantOpinionPollStartingDate
     * 			: the date this opinion poll will start to be available
     * @param restaurantOpinionPollEndingDate
     * 			: the date this opinion poll will finish
     * @param restaurantOpinionPollPriority
     * 			: the priority of this opinion poll
     * @param publicResults
     * 			: whether the results of this opinion poll are public or not
     * @param restaurantOpinionPollImage
     * 			: the image of this opinion poll
     * @param binaryChoice1
     * 			: binary choice #1 for binary type
     * @param binaryChoice2
     * 			: binary choice #2 for binary type
     * @param ratingLowValue
     * 			: lowest possible value in a rating type
     * @param ratingHighVvalue
     * 			: highest possible value in a rating type
     * @param allowMultipleSelection
     * 			: whether this opinion poll allows multiple selection (for multiple choice type)
     * @param multipleChoiceValues
     * 			: list of multiple choice values
     * @throws MissingRequiredFieldsException
     */
    public RestaurantOpinionPoll(Type restaurantOpinionPollType, 
    		String restaurantOpinionPollTitle, String restaurantOpinionPollContent, 
    		Date restaurantOpinionPollStartingDate, Date restaurantOpinionPollEndingDate, 
    		Integer restaurantOpinionPollPriority, Boolean publicResults,
    		BlobKey restaurantOpinionPollImage, String binaryChoice1, 
    		String binaryChoice2, Integer ratingLowValue, 
    		Integer ratingHighValue, ArrayList<OpinionPollRatingEntry> ratingEntries,
    		Boolean allowMultipleSelection,
    		ArrayList<RestaurantOpinionPollMultipleChoiceValue> multipleChoiceValues) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (restaurantOpinionPollType == null || restaurantOpinionPollTitle == null || 
    			restaurantOpinionPollContent == null || restaurantOpinionPollStartingDate == null || 
    			restaurantOpinionPollEndingDate == null || restaurantOpinionPollPriority == null ||
    			publicResults == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	if (restaurantOpinionPollTitle.trim().isEmpty() ||
    			restaurantOpinionPollContent.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
    	// Check required fields for specific opinion poll types
    	if (restaurantOpinionPollType == Type.BINARY) {
    		if (binaryChoice1 == null || binaryChoice1.trim().isEmpty() ||
    				binaryChoice2 == null || binaryChoice2.trim().isEmpty()) {
    			throw new MissingRequiredFieldsException(this.getClass(), 
        				"One or more required fields are missing.");
    		}
    	}
    	if (restaurantOpinionPollType == Type.RATING) {
    		if (ratingLowValue == null || ratingHighValue == null) {
    			throw new MissingRequiredFieldsException(this.getClass(), 
        				"One or more required fields are missing.");
    		}
    	}
    	if (restaurantOpinionPollType == Type.MULTIPLE_CHOICE) {
    		if (allowMultipleSelection == null || multipleChoiceValues == null ||
    				multipleChoiceValues.isEmpty()) {
    			throw new MissingRequiredFieldsException(this.getClass(), 
        				"One or more required fields are missing.");
    		}
    	}
    	
    	this.restaurantOpinionPollType = restaurantOpinionPollType;
    	this.restaurantOpinionPollTitle = restaurantOpinionPollTitle;
        this.restaurantOpinionPollContent = restaurantOpinionPollContent;
        this.currentClicks = 0;
        this.restaurantOpinionPollCreationDate = new Date();
        this.restaurantOpinionPollStartingDate = restaurantOpinionPollStartingDate;
        this.restaurantOpinionPollEndingDate = restaurantOpinionPollEndingDate;
        this.restaurantOpinionPollPriority = restaurantOpinionPollPriority;
        this.publicResults = publicResults;
        this.restaurantOpinionPollImage = restaurantOpinionPollImage;
        this.binaryChoice1 = binaryChoice1;
        this.binaryChoice2 = binaryChoice2;
        this.ratingLowValue = ratingLowValue;
        this.ratingHighValue = ratingHighValue;
        this.ratingEntries = ratingEntries;
        this.allowMultipleSelection = allowMultipleSelection;
        this.multipleChoiceValues = multipleChoiceValues;
    }
    
    /**
     * Get RestaurantOpinionPoll key.
     * @return Restaurant OpinionPoll key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Get RestaurantOpinionPoll type.
     * @return Restaurant OpinionPoll type
     */
    public Type getRestaurantOpinionPollType() {
        return restaurantOpinionPollType;
    }
    
    /**
     * Get RestaurantOpinionPoll type string.
     * @return Restaurant OpinionPoll type as a string
     */
    public String getRestaurantOpinionPollTypeString() {
        switch (restaurantOpinionPollType) {
        	case BINARY:
        		return "Binary";
        	case RATING:
        		return "Rating";
        	case MULTIPLE_CHOICE:
        		return "Multiple Choice";
        	case FREE_RESPONSE:
        		return "Free Response";
        	default:
        		return null;
        }
    }
    
    /**
     * Get RestaurantOpinionPoll type from string.
     * @param the opinion poll type as a string
     * @return Restaurant OpinionPoll type
     */
    public static Type getRestaurantOpinionPollTypeFromString(String opinionPollTypeString) {
    	if (opinionPollTypeString == null) {
    		return null;
    	}
    	
    	if (opinionPollTypeString.equalsIgnoreCase("binary")) {
    		return Type.BINARY;
    	}
    	else if (opinionPollTypeString.equalsIgnoreCase("rating")) {
    		return Type.RATING;
    	}
    	else if (opinionPollTypeString.equalsIgnoreCase("multiple_choice")) {
    		return Type.MULTIPLE_CHOICE;
    	}
    	else if (opinionPollTypeString.equalsIgnoreCase("free_response")) {
    		return Type.FREE_RESPONSE;
    	}
    	else {
    		return null;
    	}
    }
    
    /**
     * Get RestaurantOpinionPoll title.
     * @return Restaurant OpinionPoll title
     */
    public String getRestaurantOpinionPollTitle() {
        return restaurantOpinionPollTitle;
    }
    
    /**
     * Get RestaurantOpinionPoll content.
     * @return Restaurant OpinionPoll content
     */
    public String getRestaurantOpinionPollContent() {
        return restaurantOpinionPollContent;
    }
    
    /**
     * Get Current number of clicks.
     * @return The number of users that have clicked this opinion poll
     */
    public Integer getCurrentClicks() {
        return currentClicks;
    }
    
    /**
     * Get the date this opinion poll was created
     * @return The date this opinion poll was created
     */
    public Date getRestaurantOpinionPollCreationDate() {
        return restaurantOpinionPollCreationDate;
    }
    
    /**
     * Get the date when this opinion poll will be available
     * @return The date when this opinion poll will be available
     */
    public Date getRestaurantOpinionPollStartingDate() {
        return restaurantOpinionPollStartingDate;
    }
    
    /**
     * Get the date when this opinion poll will be stop being available
     * @return The date when this opinion poll will expire
     */
    public Date getRestaurantOpinionPollEndingDate() {
        return restaurantOpinionPollEndingDate;
    }
    
    /**
     * Get opinion poll priority.
     * @return The priority of this opinion poll represented as a number
     */
    public Integer getRestaurantOpinionPollPriority() {
        return restaurantOpinionPollPriority;
    }
    
    /**
     * Results are public.
     * @return Whether this opinion poll's results are public or not
     */
    public Boolean resultsArePublic() {
    	if (publicResults == null) {
    		return true;
    	}
        return publicResults;
    }
    
    /**
     * Get RestaurantOpinionPoll image.
     * @return opinion poll image blobkey
     */
    public BlobKey getRestaurantOpinionPollImage() {
        return restaurantOpinionPollImage;
    }
    
    /**
     * Get binary choice 1.
     * @return Binary choice 1 for BINARY type
     */
    public String getBinaryChoice1() {
        return binaryChoice1;
    }
    
    /**
     * Get binary choice 2.
     * @return Binary choice 2 for BINARY type
     */
    public String getBinaryChoice2() {
        return binaryChoice2;
    }
    
    /**
     * Get rating low value.
     * @return The lowest value for RATING type
     */
    public Integer getRatingLowValue() {
        return ratingLowValue;
    }
    
    /**
     * Get rating high value.
     * @return The highest value for RATING type
     */
    public Integer getRatingHighValue() {
        return ratingHighValue;
    }
    
    /**
     * Get rating entries for RATING type.
     * @return The list of rating entries
     */
    public ArrayList<OpinionPollRatingEntry> getRatingEntries() {
        return ratingEntries;
    }
    
    /**
     * Get whether this opinion poll allows multiple selection
     * (for MULTIPLE_SELECTION type)
     * @return True if this opinion poll allows multiple selection,
     * 			False otherwise
     */
    public Boolean allowsMultipleSelection() {
        return allowMultipleSelection;
    }
    
    /**
     * Get multiple choice values for MULTIPLE_CHOICE type.
     * @return The possible list of multiple choice values
     */
    public ArrayList<RestaurantOpinionPollMultipleChoiceValue> getMultipleChoiceValues() {
        return multipleChoiceValues;
    }
    
    /**
     * Get the current status of this opinion poll
     * @return The current status of this opinion poll
     */
    public Status getCurrentStatus() {
        // INACTIVE, ACTIVE, EXPIRED
    	
    	Date now = new Date();
    	if (now.compareTo(restaurantOpinionPollStartingDate) < 0) {
    		return Status.INACTIVE;
    	}
    	else if (now.compareTo(restaurantOpinionPollEndingDate) > 0) {
    		return Status.EXPIRED;
    	}
    	else {
    		return Status.ACTIVE;
    	}
    }
    
    /**
     * Compare this Restaurant OpinionPoll with another Restaurant OpinionPoll
     * @param o
     * 			: the object to compare
     * @return true if the object to compare is equal to this RestaurantOpinionPoll, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof RestaurantOpinionPoll ) ) return false;
        RestaurantOpinionPoll restaurantOpinionPoll = (RestaurantOpinionPoll) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(restaurantOpinionPoll.getKey()));
    }
    
    /**
     * Add a new click to this opinion poll
     * @return True if this opinion poll is still ACTIVE, False otherwise
     * 
     */
    public boolean addClick() {
    	if (getCurrentStatus() == Status.ACTIVE) {
    		currentClicks++;
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    /**
     * Set Restaurant OpinionPoll title.
     * @param restaurantOpinionPollTitle
     * 			: the title of this opinion poll
     * @throws MissingRequiredFieldsException
     */
    public void setRestaurantOpinionPollTitle(String restaurantOpinionPollTitle)
    		throws MissingRequiredFieldsException {
    	if (restaurantOpinionPollTitle == null || restaurantOpinionPollTitle.isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"opinion poll title is missing.");
    	}
    	this.restaurantOpinionPollTitle = restaurantOpinionPollTitle;
    }
    
    /**
     * Set Restaurant OpinionPoll Content.
     * @param restaurantOpinionPollContent
     * 			: the Content of this opinion poll
     * @throws MissingRequiredFieldsException
     */
    public void setRestaurantOpinionPollContent(String restaurantOpinionPollContent)
    		throws MissingRequiredFieldsException {
    	if (restaurantOpinionPollContent == null || restaurantOpinionPollContent.isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"opinion poll Content is missing.");
    	}
    	this.restaurantOpinionPollContent = restaurantOpinionPollContent;
    }
    
    /**
     * Set Restaurant OpinionPoll Starting Date.
     * @param restaurantOpinionPollStartingDate
     * 			: the date this opinion poll will be available
     * @throws MissingRequiredFieldsException
     */
    public void setRestaurantOpinionPollStartingDate(Date restaurantOpinionPollStartingDate)
    		throws MissingRequiredFieldsException {
    	if (restaurantOpinionPollStartingDate == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"opinion poll starting date is missing.");
    	}
    	this.restaurantOpinionPollStartingDate = restaurantOpinionPollStartingDate;
    }
    
    /**
     * Set Restaurant OpinionPoll Ending Date.
     * @param restaurantOpinionPollEndingDate
     * 			: the date this opinion poll will stop (expire)
     * @throws MissingRequiredFieldsException
     */
    public void setRestaurantOpinionPollEndingDate(Date restaurantOpinionPollEndingDate)
    		throws MissingRequiredFieldsException {
    	if (restaurantOpinionPollEndingDate == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"opinion poll ending date is missing.");
    	}
    	this.restaurantOpinionPollEndingDate = restaurantOpinionPollEndingDate;
    }
    
    /**
     * Set Restaurant OpinionPoll priority.
     * @param restaurantOpinionPollPriority
     * 			: the priority given to this opinion poll (as a number)
     * @throws MissingRequiredFieldsException
     */
    public void setRestaurantOpinionPollPriority(Integer restaurantOpinionPollPriority)
    		throws MissingRequiredFieldsException {
    	if (restaurantOpinionPollPriority == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"opinion poll priority is missing.");
    	}
    	this.restaurantOpinionPollPriority = restaurantOpinionPollPriority;
    }
    
    /**
     * Set public results.
     * @param publicResults
     * 			: whether these results are public or not
     * @throws MissingRequiredFieldsException 
     */
    public void setPublicResults(Boolean publicResults) throws MissingRequiredFieldsException {
    	if (publicResults == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Public results is missing.");
    	}
    	this.publicResults = publicResults;
    }
    
    /**
     * Set RestaurantOpinionPoll image.
     * @param restaurantOpinionPollImage
     * 			: opinion poll image blobkey
     */
    public void setRestaurantOpinionPollImage(BlobKey restaurantOpinionPollImage) {
    	this.restaurantOpinionPollImage = restaurantOpinionPollImage;
    }
    
    /**
     * Set Binary Choice 1.
     * @param binaryChoice1
     * 			: binary choice 1 for BINARY type
     * @throws MissingRequiredFieldsException
     */
    public void setBinaryChoice1(String binaryChoice1)
    		throws MissingRequiredFieldsException {
    	if (restaurantOpinionPollType == Type.BINARY) {
	    	if (binaryChoice1 == null || binaryChoice1.trim().isEmpty()) {
	    		throw new MissingRequiredFieldsException(this.getClass(), 
	    				"Binary choice 1 is missing.");
	    	}
    	}
    	this.binaryChoice1 = binaryChoice1;
    }
    
    /**
     * Set Binary Choice 2.
     * @param binaryChoice2
     * 			: binary choice 2 for BINARY type
     * @throws MissingRequiredFieldsException
     */
    public void setBinaryChoice2(String binaryChoice2)
    		throws MissingRequiredFieldsException {
    	if (restaurantOpinionPollType == Type.BINARY) {
	    	if (binaryChoice2 == null || binaryChoice2.trim().isEmpty()) {
	    		throw new MissingRequiredFieldsException(this.getClass(), 
	    				"Binary choice 2 is missing.");
	    	}
    	}
    	this.binaryChoice2 = binaryChoice2;
    }
    
    /**
     * Set rating low value.
     * @param ratingLowValue
     * 			: lowest possible low value for RATING type
     * @throws MissingRequiredFieldsException
     */
    public void setRatingLowValue(Integer ratingLowValue)
    		throws MissingRequiredFieldsException {
    	if (restaurantOpinionPollType == Type.RATING) {
	    	if (ratingLowValue == null) {
	    		throw new MissingRequiredFieldsException(this.getClass(), 
	    				"Rating low value is missing.");
	    	}
    	}
    	this.ratingLowValue = ratingLowValue;
    }
    
    /**
     * Set rating high value.
     * @param ratingHighValue
     * 			: highest possible low value for RATING type
     * @throws MissingRequiredFieldsException
     */
    public void setRatingHighValue(Integer ratingHighValue)
    		throws MissingRequiredFieldsException {
    	if (restaurantOpinionPollType == Type.RATING) {
	    	if (ratingHighValue == null) {
	    		throw new MissingRequiredFieldsException(this.getClass(), 
	    				"Rating high value is missing.");
	    	}
    	}
    	this.ratingHighValue = ratingHighValue;
    }
    
    /**
     * Set allow multiple choice selection.
     * @param allowMultipleChoiceSelection
     * 			: whether this opinion poll allows multiple selection
     * 				for MULTIPLE_CHOICE type 
     * @throws MissingRequiredFieldsException
     */
    public void setAllowMultipleChoiceSelection(Boolean allowMultipleChoiceSelection)
    		throws MissingRequiredFieldsException {
    	if (restaurantOpinionPollType == Type.MULTIPLE_CHOICE) {
	    	if (allowMultipleChoiceSelection == null) {
	    		throw new MissingRequiredFieldsException(this.getClass(), 
	    				"Allow multiple choice selection is missing.");
	    	}
    	}
    	this.allowMultipleSelection = allowMultipleChoiceSelection;
    }
    
    /**
     * Add a new multipleChoiceValue to this opinion poll
     * (for MULTIPLE_CHOICE type)
     * @param multipleChoiceValue
     * 			: new multipleChoiceValue to be added
     */
    public void addMultipleChoiceValue(
    			RestaurantOpinionPollMultipleChoiceValue multipleChoiceValue) {
    	this.multipleChoiceValues.add(multipleChoiceValue);
    }
    
    /**
     * Remove a multipleChoiceValue from this opinion poll
     * @param multipleChoiceValue
     * 			: multipleChoiceValue to be removed
     * @throws InexistentObjectException
     */
    public void removeMultipleChoiceValue(RestaurantOpinionPollMultipleChoiceValue multipleChoiceValue) 
    		throws InexistentObjectException {
    	if (!multipleChoiceValues.remove(multipleChoiceValue)) {
    		throw new InexistentObjectException(
    				RestaurantOpinionPollMultipleChoiceValue.class, "Multiple choice value not found!");
    	}
    }

}