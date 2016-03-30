/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents a simple version of the CustomerRestaurantOpinionPoll
 * or CustomerSurveyOpinionPoll table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class OpinionPollResultsSimple implements Serializable {
    
	public static class FreeResponse {
		public String userEmail;
		public String date;
		public String response;
		
		/**
		 * FreeResponse constructor.
		 * @param userEmail
		 * @param date
		 * @param response
		 */
		public FreeResponse(String userEmail, String date,
				String response) {
			
			this.userEmail = userEmail;
			this.date = date;
			this.response = response;
		}
	}
	
	public String key;
	public String response;
	public ArrayList<FreeResponse> freeResponseResults;
	public String lastResponse;
    
    /**
     * OpinionPollResultsSimple constructor.
     * @param key
     * 			: OpinionPoll Key String
     * @param response
     * 			: The response
     * @param freeResponseResults
     * 			: The free response results
     * @param lastResponse
     * 			: The last response given by a user for Free Response 
     * 			opinion polls
     */
    public OpinionPollResultsSimple(String key, String response,
    		ArrayList<FreeResponse> freeResponseResults,
    		String lastResponse) {

    	this.key = key;
    	this.response = response;
    	this.freeResponseResults = freeResponseResults;
    	this.lastResponse = lastResponse;
    }
    
    /**
     * Compare this OpinionPollResultsSimple with another OpinionPollResultsSimple
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this OpinionPollResultsSimple, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof OpinionPollResultsSimple ) ) return false;
        OpinionPollResultsSimple opres = (OpinionPollResultsSimple) o;
        return this.key.equals(opres.key);
    }
    
}
