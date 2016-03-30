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
 * This class represents the OpinionPollRatingEntry table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class OpinionPollRatingEntry implements Serializable {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String ratingEntry;
    
    @Persistent
    private Integer ratingEntryIndex;

    /**
     * OpinionPollRatingEntry constructor.
     * @param multipleChoiceValue
     * 			: restaurant opinion poll multiple choice value
     * @param ratingEntryIndex
     * 			: rating entry index
     * @throws MissingRequiredFieldsException
     */
    public OpinionPollRatingEntry(String ratingEntry,
    		Integer ratingEntryIndex) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (ratingEntry == null || ratingEntry.trim().isEmpty() ||
    			ratingEntryIndex == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
        this.ratingEntry = ratingEntry;
        this.ratingEntryIndex = ratingEntryIndex;
    }

    /**
     * Get OpinionPollRatingEntry key.
     * @return rating entry's key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Get rating entry.
     * @return ratingEntry
     */
    public String getRatingEntry() {
        return ratingEntry;
    }
    
    /**
     * Get rating entry index.
     * @return ratingEntryIndex
     */
    public Integer getRatingEntryIndex() {
        return ratingEntryIndex;
    }

    
    /**
     * Compare this rating entry with another rating entry
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this RatingEntry, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof OpinionPollRatingEntry ) ) return false;
        OpinionPollRatingEntry opinionPollRatingEntry = (OpinionPollRatingEntry) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(opinionPollRatingEntry.getKey()));
    }
    
    /**
     * Set rating entry.
     * @param ratingEntry
     * 			: rating entry value
     * @throws MissingRequiredFieldsException
     */
    public void setRatingEntry(String ratingEntry)
    		throws MissingRequiredFieldsException {
    	if (ratingEntry == null || ratingEntry.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Rating entry is missing.");
    	}
    	this.ratingEntry = ratingEntry;
    }
    
    /**
     * Set rating entry index.
     * @param ratingEntryIndex
     * 			: rating entry index
     * @throws MissingRequiredFieldsException
     */
    public void setRatingEntryIndex(Integer ratingEntryIndex)
    		throws MissingRequiredFieldsException {
    	if (ratingEntryIndex == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Rating entry Index is missing.");
    	}
    	this.ratingEntryIndex = ratingEntryIndex;
    }

}