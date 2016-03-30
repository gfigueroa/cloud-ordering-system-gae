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

public class RestaurantNewsListResource extends ServerResource {
	
	private static final Logger log = 
	        Logger.getLogger(RestaurantNewsListResource.class.getName());
	
	/**
	 * Returns the restaurant news list as a JSON object.
	 * @return An ArrayList of restaurant news in JSON format
	 */
    @Get("json")
    public ArrayList<RestaurantNewsSimple> toJson() {
    	
    	String queryInfo = (String) getRequest().getAttributes()
                .get("queryinfo");
    	
    	Key customerKey = null;
    	if (!queryInfo.equalsIgnoreCase("none")) {     
	        char searchBy = queryInfo.charAt(0);
	        String searchEmailString = queryInfo.substring(2);
	        
	        customerKey = KeyFactory.createKey(Customer.class.getSimpleName(), searchEmailString);
	        
	        log.info("Query: " + searchBy + "=" + searchEmailString);
    	}
        
        List<RestaurantNews> restaurantNewsList = RestaurantNewsManager.getAllPublicActiveRestaurantNews();
        ArrayList<RestaurantNewsSimple> restaurantNewsListSimple = new ArrayList<RestaurantNewsSimple>();
        for (RestaurantNews restaurantNews : restaurantNewsList) {
        	
        	boolean customerAlreadyClickedNews = false;
        	if (!queryInfo.equalsIgnoreCase("none")) {
	        	customerAlreadyClickedNews = 
	        			RestaurantNewsManager.customerAlreadyClickedNews(customerKey, restaurantNews.getKey());
        	}
        	
        	RestaurantNewsSimple restaurantNewsSimple = new RestaurantNewsSimple(
        			KeyFactory.keyToString(restaurantNews.getKey()),
        			KeyFactory.keyToString(restaurantNews.getKey().getParent()),
        			restaurantNews.getRestaurantNewsTitle(),
        			restaurantNews.getRestaurantNewsContent(),
        			restaurantNews.allowsResponse() != null ? restaurantNews.allowsResponse() : false,
        			restaurantNews.getCurrentClicks(),
        			restaurantNews.getMaxClicks() != null ? restaurantNews.getMaxClicks() : 0,
        			customerAlreadyClickedNews,
        			DateManager.printDateAsString(restaurantNews.getRestaurantNewsEndingDate()),
        			restaurantNews.getRestaurantNewsPriority(),
        			restaurantNews.getRestaurantNewsImage() != null ?
        					restaurantNews.getRestaurantNewsImage() : new BlobKey("")
        			);
        	
        	restaurantNewsListSimple.add(restaurantNewsSimple);
        }
        
        return restaurantNewsListSimple;
    }

}