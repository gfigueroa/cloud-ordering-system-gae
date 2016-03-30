/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;

/**
 * This class represents a simple version of the OpinionPollRatingEntry table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class OpinionPollRatingEntrySimple implements Serializable {
    
	public String key;
	public String ratingEntry;
	public Integer ratingEntryIndex;
    
    /**
     * OpinionPollRatingEntrySimple constructor.
     * @param key
     * 			: OpinionPollRatingEntry key string
     * @param ratingEntry
     * 			: The rating entry
     * @param ratingEntryIndex
     * 			: The rating entry index
     */
    public OpinionPollRatingEntrySimple(String key, String ratingEntry,
    		Integer ratingEntryIndex) {

    	this.key = key;
    	this.ratingEntry = ratingEntry;
    	this.ratingEntryIndex = ratingEntryIndex;
    }
    
    /**
     * Compare this Opinion Poll Rating Entry with another OpinionPollRatingEntry
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this OpinionPollRatingEntry, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof OpinionPollRatingEntrySimple ) ) return false;
        OpinionPollRatingEntrySimple opres = (OpinionPollRatingEntrySimple) o;
        return this.key.equals(opres.key);
    }
    
}
