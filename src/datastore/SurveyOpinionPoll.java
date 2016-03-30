/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.io.Serializable;
import java.util.ArrayList;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import exceptions.InexistentObjectException;
import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the SurveyOpinionPoll table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class SurveyOpinionPoll implements Serializable {
	
	public static enum Type {
		BINARY, RATING, MULTIPLE_CHOICE, FREE_RESPONSE
	}
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private Type surveyOpinionPollType;

    @Persistent
    private String surveyOpinionPollTitle;
    
    @Persistent
    private String surveyOpinionPollContent;
    
    @Persistent
    private Integer currentClicks;
    
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
    private ArrayList<SurveyOpinionPollRatingEntry> ratingEntries;
    
    @Persistent
    private Boolean allowMultipleSelection;
    
    @Persistent(defaultFetchGroup = "true")
    @Element(dependent = "true")
    private ArrayList<SurveyOpinionPollMultipleChoiceValue> multipleChoiceValues;

    /**
     * SurveyOpinionPoll constructor.
     * @param surveyOpinionPollType
     * 			: opinion poll type
     * @param surveyOpinionPollTitle
     * 			: opinion poll title
     * @param surveyOpinionPollContent
     * 			: opinion poll content
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
    public SurveyOpinionPoll(Type surveyOpinionPollType, String surveyOpinionPollTitle, 
    		String surveyOpinionPollContent,String binaryChoice1, 
    		String binaryChoice2, Integer ratingLowValue, Integer ratingHighValue, 
    		ArrayList<SurveyOpinionPollRatingEntry> ratingEntries, Boolean allowMultipleSelection,
    		ArrayList<SurveyOpinionPollMultipleChoiceValue> multipleChoiceValues) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (surveyOpinionPollType == null || surveyOpinionPollTitle == null || 
    			surveyOpinionPollContent == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	if (surveyOpinionPollTitle.trim().isEmpty() ||
    			surveyOpinionPollContent.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
    	// Check required fields for specific opinion poll types
    	if (surveyOpinionPollType == Type.BINARY) {
    		if (binaryChoice1 == null || binaryChoice1.trim().isEmpty() ||
    				binaryChoice2 == null || binaryChoice2.trim().isEmpty()) {
    			throw new MissingRequiredFieldsException(this.getClass(), 
        				"One or more required fields are missing.");
    		}
    	}
    	if (surveyOpinionPollType == Type.RATING) {
    		if (ratingLowValue == null || ratingHighValue == null) {
    			throw new MissingRequiredFieldsException(this.getClass(), 
        				"One or more required fields are missing.");
    		}
    	}
    	if (surveyOpinionPollType == Type.MULTIPLE_CHOICE) {
    		if (allowMultipleSelection == null || multipleChoiceValues == null ||
    				multipleChoiceValues.isEmpty()) {
    			throw new MissingRequiredFieldsException(this.getClass(), 
        				"One or more required fields are missing.");
    		}
    	}
    	
    	this.surveyOpinionPollType = surveyOpinionPollType;
    	this.surveyOpinionPollTitle = surveyOpinionPollTitle;
        this.surveyOpinionPollContent = surveyOpinionPollContent;
        this.currentClicks = 0;
        this.binaryChoice1 = binaryChoice1;
        this.binaryChoice2 = binaryChoice2;
        this.ratingLowValue = ratingLowValue;
        this.ratingHighValue = ratingHighValue;
        this.ratingEntries = ratingEntries;
        this.allowMultipleSelection = allowMultipleSelection;
        this.multipleChoiceValues = multipleChoiceValues;
    }
    
    /**
     * Get SurveyOpinionPoll key.
     * @return Survey OpinionPoll key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Get SurveyOpinionPoll type.
     * @return Survey OpinionPoll type
     */
    public Type getSurveyOpinionPollType() {
        return surveyOpinionPollType;
    }
    
    /**
     * Get SurveyOpinionPoll type string.
     * @return Survey OpinionPoll type as a string
     */
    public String getSurveyOpinionPollTypeString() {
        switch (surveyOpinionPollType) {
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
     * Get SurveyOpinionPoll type from string.
     * @param the opinion poll type as a string
     * @return Survey OpinionPoll type
     */
    public static Type getSurveyOpinionPollTypeFromString(String opinionPollTypeString) {
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
     * Get SurveyOpinionPoll title.
     * @return Survey OpinionPoll title
     */
    public String getSurveyOpinionPollTitle() {
        return surveyOpinionPollTitle;
    }
    
    /**
     * Get SurveyOpinionPoll content.
     * @return Survey OpinionPoll content
     */
    public String getSurveyOpinionPollContent() {
        return surveyOpinionPollContent;
    }
    
    /**
     * Get Current number of clicks.
     * @return The number of users that have clicked this opinion poll
     */
    public Integer getCurrentClicks() {
        return currentClicks;
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
    public ArrayList<SurveyOpinionPollRatingEntry> getRatingEntries() {
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
    public ArrayList<SurveyOpinionPollMultipleChoiceValue> getMultipleChoiceValues() {
        return multipleChoiceValues;
    }
    
    /**
     * Compare this Survey OpinionPoll with another Survey OpinionPoll
     * @param o
     * 			: the object to compare
     * @return true if the object to compare is equal to this SurveyOpinionPoll, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof SurveyOpinionPoll ) ) return false;
        SurveyOpinionPoll surveyOpinionPoll = (SurveyOpinionPoll) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(surveyOpinionPoll.getKey()));
    }
    
    /**
     * Add a new click to this opinion poll
     * 
     */
    public void addClick() {
    	currentClicks++;
    }
    
    /**
     * Set Survey OpinionPoll title.
     * @param surveyOpinionPollTitle
     * 			: the title of this opinion poll
     * @throws MissingRequiredFieldsException
     */
    public void setSurveyOpinionPollTitle(String surveyOpinionPollTitle)
    		throws MissingRequiredFieldsException {
    	if (surveyOpinionPollTitle == null || surveyOpinionPollTitle.isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"opinion poll title is missing.");
    	}
    	this.surveyOpinionPollTitle = surveyOpinionPollTitle;
    }
    
    /**
     * Set Survey OpinionPoll Content.
     * @param surveyOpinionPollContent
     * 			: the Content of this opinion poll
     * @throws MissingRequiredFieldsException
     */
    public void setSurveyOpinionPollContent(String surveyOpinionPollContent)
    		throws MissingRequiredFieldsException {
    	if (surveyOpinionPollContent == null || surveyOpinionPollContent.isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"opinion poll Content is missing.");
    	}
    	this.surveyOpinionPollContent = surveyOpinionPollContent;
    }
    
    /**
     * Set Binary Choice 1.
     * @param binaryChoice1
     * 			: binary choice 1 for BINARY type
     * @throws MissingRequiredFieldsException
     */
    public void setBinaryChoice1(String binaryChoice1)
    		throws MissingRequiredFieldsException {
    	if (surveyOpinionPollType == Type.BINARY) {
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
    	if (surveyOpinionPollType == Type.BINARY) {
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
    	if (surveyOpinionPollType == Type.RATING) {
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
    	if (surveyOpinionPollType == Type.RATING) {
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
    	if (surveyOpinionPollType == Type.MULTIPLE_CHOICE) {
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
    			SurveyOpinionPollMultipleChoiceValue multipleChoiceValue) {
    	this.multipleChoiceValues.add(multipleChoiceValue);
    }
    
    /**
     * Remove a multipleChoiceValue from this opinion poll
     * @param multipleChoiceValue
     * 			: multipleChoiceValue to be removed
     * @throws InexistentObjectException
     */
    public void removeMultipleChoiceValue(SurveyOpinionPollMultipleChoiceValue multipleChoiceValue) 
    		throws InexistentObjectException {
    	if (!multipleChoiceValues.remove(multipleChoiceValue)) {
    		throw new InexistentObjectException(
    				SurveyOpinionPollMultipleChoiceValue.class, "Multiple choice value not found!");
    	}
    }

}