/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.external_resources;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import util.DateManager;
import webservices.datastore_simple.RestaurantNewsSimple;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import datastore.Customer;
import datastore.RestaurantNews;
import datastore.RestaurantNewsManager;

/**
 * This class represents the list of restaurant news
 * as a Resource with only one representation
 */

public class PrivateNewsListResource extends ServerResource {
	
	private static final Logger log = 
	        Logger.getLogger(PrivateNewsListResource.class.getName());
	
	/**
	 * Returns the restaurant news list as a JSON object.
	 * @return An ArrayList of restaurant news in JSON format
	 */
    @Get("json")
    public ArrayList<RestaurantNewsSimple> toJson() {
    	
    	String queryInfo = (String) getRequest().getAttributes()
                .get("queryinfo");
    	
    	int indexOfSecondParameter = queryInfo.indexOf('&');
    	String storeKey = "";
    	String customerEmail = "";
    	
    	// If no customer email is given
    	if (indexOfSecondParameter == -1) {
    		// return null if no store key is given
    		if (Character.toLowerCase(queryInfo.charAt(0)) != 's') {
    			return null;
    		}
    		
    		storeKey = queryInfo.substring(2);
    		log.info("s=" + storeKey);
    	}
    	else {
    		char searchByAttribute1 = queryInfo.charAt(0);
        	String searchByValue1 = queryInfo.substring(2, queryInfo.indexOf("&"));
        	char searchByAttribute2 = queryInfo.charAt(queryInfo.indexOf("&") + 1);
        	String searchByValue2 = queryInfo.substring(queryInfo.indexOf("&") + 3);
        	
        	log.info(searchByAttribute1 + "=" + searchByValue1 +
        			" --- " + searchByAttribute2 + "=" + searchByValue2);

        	if (Character.toLowerCase(searchByAttribute1) == 's') {
        		storeKey = searchByValue1;
        	}
        	else if (Character.toLowerCase(searchByAttribute1) == 'c') {
        		customerEmail = searchByValue1;
        	}
        	else {
        		return null;
        	}
        	
        	if (Character.toLowerCase(searchByAttribute2) == 's') {
        		storeKey = searchByValue2;
        	}
        	else if (Character.toLowerCase(searchByAttribute2) == 'c') {
        			customerEmail = searchByValue2;
        	}
        	else {
        		return null;
        	}
    	}
        
        List<RestaurantNews> newsList = RestaurantNewsManager.getPrivateActiveRestaurantNewsFromRestaurant(KeyFactory.stringToKey(storeKey));
        
        ArrayList<RestaurantNewsSimple> newsListSimple = new ArrayList<RestaurantNewsSimple>();
        for (RestaurantNews news : newsList) {
        	
        	boolean customerAlreadyClickedNews = false;
        	if (!customerEmail.isEmpty()) {
        		Key customerKey = KeyFactory.createKey(Customer.class.getSimpleName(), customerEmail);
	        	customerAlreadyClickedNews = 
	        			RestaurantNewsManager.customerAlreadyClickedNews(customerKey, news.getKey());
        	}
        	
        	RestaurantNewsSimple restaurantNewsSimple = new RestaurantNewsSimple(
        			KeyFactory.keyToString(news.getKey()),
        			KeyFactory.keyToString(news.getKey().getParent()),
        			news.getRestaurantNewsTitle(),
        			news.getRestaurantNewsContent(),
        			news.allowsResponse() != null ? news.allowsResponse() : false,
        			news.getCurrentClicks(),
        			news.getMaxClicks() != null ? news.getMaxClicks() : 0,
        			customerAlreadyClickedNews,
        			DateManager.printDateAsString(news.getRestaurantNewsEndingDate()),
        			news.getRestaurantNewsPriority(),
        			news.getRestaurantNewsImage() != null ?
        					news.getRestaurantNewsImage() : new BlobKey("")
        			);
        	
        	newsListSimple.add(restaurantNewsSimple);
        }
        
        return newsListSimple;
    }

}