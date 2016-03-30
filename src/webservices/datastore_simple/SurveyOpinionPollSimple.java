/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;
import java.util.ArrayList;

import datastore.SurveyOpinionPoll;

/**
 * This class represents a simple version of the SurveyOpinionPoll table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class SurveyOpinionPollSimple implements Serializable {
    
	public String key;
	public SurveyOpinionPoll.Type opinionPollType;
	public String opinionPollTitle;
	public String opinionPollContent;
	public String binaryChoice1;
	public String binaryChoice2;
	public Integer ratingLowValue;
	public Integer ratingHighValue;
	public ArrayList<OpinionPollRatingEntrySimple> ratingEntries;
	public Boolean allowMultipleSelection;
	public ArrayList<String> multipleChoiceValues;
    
    /**
     * SurveyOpinionPollSimple constructor.
     * @param key
     * 			: SurveyOpinionPoll key string
     * @param opinionPollType
     * 			: The type of opinion poll
     * @param opinionPollTitle
     * 			: The title of the Survey OpinionPoll
     * @param opinionPollContent
     * 			: The content of the Survey OpinionPoll
     * @param binaryChoice1
     * 			: Binary choice 1
     * @param binaryChoice2
     * 			: Binary choice 2
     * @param ratingLowValue
     * 			: Lowest rating value
     * @param ratingHighValue
     * 			: Highest rating value
     * @param ratingEntries
     * 			: The rating entries for rating type
     * @param allowMultipleSelection
     * 			: Allow multiple selection or not
     * @param multipleChoiceValues
     * 			: The possible values for multiple choice type
     */
    public SurveyOpinionPollSimple(String key,
    		SurveyOpinionPoll.Type opinionPollType,
    		String opinionPollTitle, String opinionPollContent,
    		String binaryChoice1, String binaryChoice2, Integer ratingLowValue,
    		Integer ratingHighValue, ArrayList<OpinionPollRatingEntrySimple> ratingEntries,
    		Boolean allowMultipleChoiceSelection,
    		ArrayList<String> multipleChoiceValues) {

    	this.key = key;
    	this.opinionPollType = opinionPollType;
    	this.opinionPollTitle = opinionPollTitle;
    	this.opinionPollContent = opinionPollContent;
    	this.binaryChoice1 = binaryChoice1;
    	this.binaryChoice2 = binaryChoice2;
    	this.ratingLowValue = ratingLowValue;
    	this.ratingHighValue = ratingHighValue;
    	this.ratingEntries = ratingEntries;
    	this.allowMultipleSelection = allowMultipleChoiceSelection;
    	this.multipleChoiceValues = multipleChoiceValues;
    }
    
    /**
     * Compare this Survey OpinionPoll with another SurveyOpinionPoll
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this SurveyOpinionPoll, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof SurveyOpinionPollSimple ) ) return false;
        SurveyOpinionPollSimple sop = (SurveyOpinionPollSimple) o;
        return this.key.equals(sop.key);
    }
    
}
