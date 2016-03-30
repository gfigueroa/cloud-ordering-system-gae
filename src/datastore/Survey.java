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
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import exceptions.InexistentObjectException;
import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the Survey table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class Survey implements Serializable {

	public static enum Status {
		INACTIVE, ACTIVE, EXPIRED
	}
	
	public static enum Type {
		PRIVATE, INVITATION, GLOBAL
	}
	
	@NotPersistent
	public static final int VALIDATION_CODE_SIZE = 4;
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private Type surveyType;

    @Persistent
    private String surveyTitle;
    
    @Persistent
    private String surveyDescription;
    
    @Persistent
    private String validationCode;
    
    @Persistent
    private Integer currentClicks;
    
    @Persistent
    private Date surveyCreationDate;
    
    @Persistent
    private Date surveyStartingDate;
    
    @Persistent
    private Date surveyEndingDate;
    
    @Persistent
    private Integer surveyPriority;
    
    @Persistent
    private Boolean publicResults;
    
    @Persistent(defaultFetchGroup = "true")
    @Element(dependent = "true")
    private ArrayList<SurveyOpinionPoll> opinionPolls;

    /**
     * Survey constructor.
     * @param surveyType
     * 			: survey type
     * @param surveyTitle
     * 			: survey title
     * @param surveyDescription
     * 			: survey description
     * @param validationCode
     * 			: survey validation code
     * @param surveyStartingDate
     * 			: the date this survey will start to be available
     * @param surveyEndingDate
     * 			: the date this survey will finish
     * @param surveyPriority
     * 			: the priority of this survey
     * @param publicResults
     * 			: whether to make this survey's results public or not
     * @throws MissingRequiredFieldsException
     */
    public Survey(Type surveyType, 
    		String surveyTitle, String surveyDescription, 
    		String validationCode, Date surveyStartingDate, 
    		Date surveyEndingDate, Integer surveyPriority, 
    		Boolean publicResults) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (surveyType == null || surveyTitle == null || 
    			surveyDescription == null || surveyStartingDate == null || 
    			surveyEndingDate == null || surveyPriority == null || 
    			publicResults == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	if (surveyTitle.trim().isEmpty() ||
    			surveyDescription.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	
    	// Check required field contraints for specific survey types
    	if (surveyType != Type.GLOBAL) {
    		if (validationCode == null || validationCode.trim().isEmpty()) {
    			throw new MissingRequiredFieldsException(this.getClass(), 
        				"One or more required fields are missing.");
    		}
    	}
    	
    	this.surveyType = surveyType;
    	this.surveyTitle = surveyTitle;
        this.surveyDescription = surveyDescription;
        this.validationCode = validationCode;
        this.currentClicks = 0;
        this.surveyCreationDate = new Date();
        this.surveyStartingDate = surveyStartingDate;
        this.surveyEndingDate = surveyEndingDate;
        this.surveyPriority = surveyPriority;
        this.publicResults = publicResults;

        opinionPolls = new ArrayList<SurveyOpinionPoll>();
    }
    
    /**
     * Generates a random validation code with the given
     * number of digits.
     * @param digits
     * 			: the number of digits in the validation code
     * @return a randomly generated validation code
     */
    public static String generateRandomValidationCode(int digits) {
    	String code = "";
    	for (int i = 0; i < digits; i++) {
    		code += (int) (Math.random() * 10);
    	}
    	return code;
    }
    
    /**
     * Get Survey key.
     * @return Survey key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Get Survey type.
     * @return Survey type
     */
    public Type getSurveyType() {
        return surveyType;
    }
    
    /**
     * Get Survey type string.
     * @return Survey type as a string
     */
    public String getSurveyTypeString() {
        switch (surveyType) {
        	case PRIVATE:
        		return "Private";
        	case INVITATION:
        		return "Invitation";
        	case GLOBAL:
        		return "Global";
        	default:
        		return null;
        }
    }
    
    /**
     * Get Survey type from string.
     * @param the survey type as a string
     * @return Survey type
     */
    public static Type getSurveyTypeFromString(String surveyTypeString) {
    	if (surveyTypeString == null) {
    		return null;
    	}
    	
    	if (surveyTypeString.equalsIgnoreCase("private")) {
    		return Type.PRIVATE;
    	}
    	else if (surveyTypeString.equalsIgnoreCase("invitation")) {
    		return Type.INVITATION;
    	}
    	else if (surveyTypeString.equalsIgnoreCase("global")) {
    		return Type.GLOBAL;
    	}
    	else {
    		return null;
    	}
    }
    
    /**
     * Get Survey title.
     * @return Survey title
     */
    public String getSurveyTitle() {
        return surveyTitle;
    }
    
    /**
     * Get Survey description.
     * @return Survey description
     */
    public String getSurveyDescription() {
        return surveyDescription;
    }
    
    /**
     * Get validation code.
     * @return Survey validation code
     */
    public String getValidationCode() {
        return validationCode;
    }
    
    /**
     * Get Current number of clicks.
     * @return The number of users that have clicked this survey
     */
    public Integer getCurrentClicks() {
        return currentClicks;
    }
    
    /**
     * Get the date this survey was created
     * @return The date this survey was created
     */
    public Date getSurveyCreationDate() {
        return surveyCreationDate;
    }
    
    /**
     * Get the date when this survey will be available
     * @return The date when this survey will be available
     */
    public Date getSurveyStartingDate() {
        return surveyStartingDate;
    }
    
    /**
     * Get the date when this survey will be stop being available
     * @return The date when this survey will expire
     */
    public Date getSurveyEndingDate() {
        return surveyEndingDate;
    }
    
    /**
     * Get survey priority.
     * @return The priority of this survey represented as a number
     */
    public Integer getSurveyPriority() {
        return surveyPriority;
    }
    
    /**
     * Get whether this survey's results are public or not.
     * @return true if the survey's results are public,
     * 			false otherwise
     */
    public Boolean resultsArePublic() {
        return publicResults;
    }
    
    /**
     * Get this survey's opinion polls.
     * @return this survey's opinion poll list
     */
    public ArrayList<SurveyOpinionPoll> getOpinionPolls() {
    	return opinionPolls;
    }
    
    /**
     * Get the current status of this survey
     * @return The current status of this survey
     */
    public Status getCurrentStatus() {
        // INACTIVE, ACTIVE, EXPIRED
    	
    	Date now = new Date();
    	if (now.compareTo(surveyStartingDate) < 0) {
    		return Status.INACTIVE;
    	}
    	else if (now.compareTo(surveyEndingDate) > 0) {
    		return Status.EXPIRED;
    	}
    	else {
    		return Status.ACTIVE;
    	}
    }
    
    /**
     * Compare this Survey with another Survey
     * @param o
     * 			: the object to compare
     * @return true if the object to compare is equal to this Survey, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof Survey ) ) return false;
        Survey survey = (Survey) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(survey.getKey()));
    }
    
    /**
     * Add a new click to this survey
     * @return True if this survey is still ACTIVE, False otherwise
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
     * Set Survey title.
     * @param surveyTitle
     * 			: the title of this survey
     * @throws MissingRequiredFieldsException
     */
    public void setSurveyTitle(String surveyTitle)
    		throws MissingRequiredFieldsException {
    	if (surveyTitle == null || surveyTitle.isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Survey title is missing.");
    	}
    	this.surveyTitle = surveyTitle;
    }
    
    /**
     * Set Survey description.
     * @param surveyDescription
     * 			: the description of this survey
     * @throws MissingRequiredFieldsException
     */
    public void setSurveyDescription(String surveyDescription)
    		throws MissingRequiredFieldsException {
    	if (surveyDescription == null || surveyDescription.isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Survey description is missing.");
    	}
    	this.surveyDescription = surveyDescription;
    }
    
    /**
     * Set Survey validation code.
     * @param validationCode
     * 			: the validation code of this survey
     * @throws MissingRequiredFieldsException
     */
    public void setValidationCode(String validationCode)
    		throws MissingRequiredFieldsException {
    	if (this.surveyType != Type.GLOBAL) {
	    	if (validationCode == null || validationCode.trim().isEmpty()) {
	    		throw new MissingRequiredFieldsException(this.getClass(), 
	    				"Validation code is missing.");
	    	}
    	}
    	this.validationCode = validationCode;
    }
    
    /**
     * Set Survey Starting Date.
     * @param surveyStartingDate
     * 			: the date this survey will be available
     * @throws MissingRequiredFieldsException
     */
    public void setSurveyStartingDate(Date surveyStartingDate)
    		throws MissingRequiredFieldsException {
    	if (surveyStartingDate == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Survey starting date is missing.");
    	}
    	this.surveyStartingDate = surveyStartingDate;
    }
    
    /**
     * Set Survey Ending Date.
     * @param surveyEndingDate
     * 			: the date this survey will stop (expire)
     * @throws MissingRequiredFieldsException
     */
    public void setSurveyEndingDate(Date surveyEndingDate)
    		throws MissingRequiredFieldsException {
    	if (surveyEndingDate == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Survey ending date is missing.");
    	}
    	this.surveyEndingDate = surveyEndingDate;
    }
    
    /**
     * Set Survey priority.
     * @param surveyPriority
     * 			: the priority given to this survey (as a number)
     * @throws MissingRequiredFieldsException
     */
    public void setSurveyPriority(Integer surveyPriority)
    		throws MissingRequiredFieldsException {
    	if (surveyPriority == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"survey priority is missing.");
    	}
    	this.surveyPriority = surveyPriority;
    }
    
    /**
     * Set public results.
     * @param publicResults
     * 			: whether this survey's results should be public or not
     * @throws MissingRequiredFieldsException
     */
    public void setPublicResults(Boolean publicResults)
    		throws MissingRequiredFieldsException {
    	if (publicResults == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Survey public results is missing.");
    	}
    	this.publicResults = publicResults;
    }
    
    /**
     * Add a new opinion poll to this survey
     * @param opinionPoll
     * 			: new opinion poll to be added
     */
    public void addOpinionPoll(SurveyOpinionPoll opinionPoll) {
    	this.opinionPolls.add(opinionPoll);
    }
    
    /**
     * Remove an opinion poll from this survey
     * @param opinionPoll
     * 			: opinion poll to be removed
     * @throws InexistentObjectException
     */
    public void removeOpinionPoll(SurveyOpinionPoll opinionPoll) 
    		throws InexistentObjectException {
    	if (!opinionPolls.remove(opinionPoll)) {
    		throw new InexistentObjectException(
    				SurveyOpinionPoll.class, "Opinion Poll not found!");
    	}
    }

}