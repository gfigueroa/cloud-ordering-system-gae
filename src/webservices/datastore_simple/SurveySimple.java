/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;
import java.util.ArrayList;

import datastore.Survey;

/**
 * This class represents a simple version of the Survey table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class SurveySimple implements Serializable {
    
	public String key;
	public String storeKey;
	public Survey.Type surveyType;
	public String surveyTitle;
	public String surveyDescription;
	public String validationCode;
	public Integer currentResponses;
	public Boolean userAlreadyResponded;
	public String surveyEndingDate;
	public Integer surveyPriority;
	public Boolean publicResults;
	public ArrayList<SurveyOpinionPollSimple> opinionPolls;
    
    /**
     * SurveySimple constructor.
     * @param key
     * 			: Survey key string
     * @param storeKey
     * 			: The key of the store to which this survey belongs
     * @param surveyType
     * 			: The type of survey
     * @param surveyTitle
     * 			: The title of the survey
     * @param surveyDescription
     * 			: The description of the survey
     * @param validationCode
     * 			: The validation code for this survey
     * @param currentResponses
     * 			: The current number of times this survey has been answered
     * @param userAlreadyResponded
     * 			: Whether the user already responded this survey or not
     * @param surveyEndingDate
     * 			: The date this survey will finish
     * @param surveyPriority
     * 			: The priority for this survey
     * @param publicResults
     * 			: Whether this survey's results are public or not
     * @param opinionPolls
     * 			: This survey's opinion polls
     */
    public SurveySimple(String key, String storeKey, Survey.Type surveyType,
    		String surveyTitle, String surveyDescription, String validationCode,
    		Integer currentResponses, Boolean userAlreadyResponded,
    		String surveyEndingDate, Integer surveyPriority, 
    		Boolean publicResults, ArrayList<SurveyOpinionPollSimple> opinionPolls) {

    	this.key = key;
    	this.storeKey = storeKey;
    	this.surveyType = surveyType;
    	this.surveyTitle = surveyTitle;
    	this.surveyDescription = surveyDescription;
    	this.validationCode = validationCode;
    	this.currentResponses = currentResponses;
    	this.userAlreadyResponded = userAlreadyResponded;
    	this.surveyEndingDate = surveyEndingDate;
    	this.surveyPriority = surveyPriority;
    	this.publicResults = publicResults;
    	this.opinionPolls = opinionPolls;
    }
    
    /**
     * Compare this survey with another Survey
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this Survey, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof SurveySimple ) ) return false;
        SurveySimple s = (SurveySimple) o;
        return this.key.equals(s.key);
    }
    
}
