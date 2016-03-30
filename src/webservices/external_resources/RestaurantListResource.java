/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.external_resources;

import java.util.ArrayList;
import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import util.DateManager;
import webservices.datastore_simple.BranchSimple;
import webservices.datastore_simple.RestaurantSimple;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Link;

import datastore.Branch;
import datastore.BranchManager;
import datastore.RegionManager;
import datastore.Restaurant;
import datastore.RestaurantManager;

/**
 * This class represents the list of restaurants 
 * as a Resource with only one representation
 */

public class RestaurantListResource extends ServerResource {

	/**
	 * Returns the simple restaurant list as a JSON object.
	 * @return An ArrayList of RestaurantSimple in JSON format
	 */
    @Get("json")
    public ArrayList<RestaurantSimple> toJson() {
        // we will be returning a list of elements
        ArrayList<Restaurant> restaurants =
        		new ArrayList<Restaurant>(RestaurantManager.getAllRestaurants());
        
        ArrayList<RestaurantSimple> simpleRestaurants = new ArrayList<RestaurantSimple>();
        for (Restaurant restaurant : restaurants) {
        	ArrayList<BranchSimple> simpleBranches = new ArrayList<BranchSimple>();
        	List<Branch> branches = BranchManager.getRestaurantBranches(restaurant.getKey());
        	for (Branch branch : branches) {
        		BranchSimple branchSimple = new BranchSimple(KeyFactory.keyToString(branch.getKey()),
        				RegionManager.getRegion(branch.getRegion()).getRegionName(),
        				branch.getBranchName(),
        				branch.getBranchAddress(),
        				branch.getBranchPhone(),
        				branch.hasDelivery(),
        				branch.hasRegularDelivery() != null ? branch.hasRegularDelivery() : false,
        				branch.hasPostalDelivery() != null ? branch.hasPostalDelivery() : false,
        				branch.hasUPSDelivery() != null ? branch.hasUPSDelivery() : false,
        				branch.hasConvenienceStoreDelivery() != null ? branch.hasConvenienceStoreDelivery() : false,
        				branch.hasTakeOut(),
        				branch.hasTakeIn()
        				);
        		simpleBranches.add(branchSimple);
        	}
        	
        	RestaurantSimple restaurantSimple = new RestaurantSimple(
        			KeyFactory.keyToString(restaurant.getKey()),
        			restaurant.getRestaurantType().toString(),
        			restaurant.getRestaurantName(),
        			restaurant.getRestaurantDescription(),
        			restaurant.getRestaurantWebsite()  != null ? 
        					restaurant.getRestaurantWebsite() : new Link(""),
        			restaurant.getRestaurantLogo() != null ? 
        					restaurant.getRestaurantLogo() : new BlobKey(""),
        			DateManager.printDateAsTime24(restaurant.getRestaurantOpeningTime()),
        			DateManager.printDateAsTime24(restaurant.getRestaurantClosingTime()),
        			simpleBranches);
        	simpleRestaurants.add(restaurantSimple);
        }
        
        return simpleRestaurants;
    }

}