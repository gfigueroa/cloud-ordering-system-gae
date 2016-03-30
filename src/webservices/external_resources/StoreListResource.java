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
import webservices.datastore_simple.BranchSimple;
import webservices.datastore_simple.StoreSimple;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Link;

import datastore.Branch;
import datastore.BranchManager;
import datastore.RegionManager;
import datastore.Restaurant;
import datastore.RestaurantManager;
import datastore.RestaurantType;
import datastore.RestaurantTypeManager;

/**
 * This class represents the list of stores 
 * as a Resource with only one representation
 */

public class StoreListResource extends ServerResource {

	private static final Logger log = 
	        Logger.getLogger(StoreListResource.class.getName());
	
	/**
	 * Returns the simple store list as a JSON object.
	 * @return An ArrayList of StoreSimple in JSON format
	 */
    @Get("json")
    public ArrayList<StoreSimple> toJson() {
    	String queryInfo = (String) getRequest().getAttributes()
                .get("queryinfo");
            
        char searchBy = queryInfo.charAt(0);
        
        List<Restaurant> stores;
        if (searchBy == 't') {
	        String storeSuperType = queryInfo.substring(2);
	        log.info("Query: " + searchBy + "=" + storeSuperType);
	
	        stores = RestaurantManager.getAllStoresBySuperType(
	        				RestaurantType.getStoreSuperTypeFromString(storeSuperType));
        }
        else if (searchBy == 's') {
        	String serviceString = queryInfo.substring(2);
        	Restaurant.Service service = Restaurant.getServiceFromString(serviceString);
	        log.info("Query: " + searchBy + "=" + serviceString);
	        
	        stores = RestaurantManager.getAllStoresWithService(service);
        }
        else if (searchBy == 'k') {
        	String storeTypeString = queryInfo.substring(2);
        	Long storeType = Long.parseLong(storeTypeString);
        	
        	stores = RestaurantManager.getAllRestaurantsByType(storeType);
        }
        else {
        	return null;
        }
        
        ArrayList<StoreSimple> simpleStores = new ArrayList<StoreSimple>();
        for (Restaurant store : stores) {
        	ArrayList<BranchSimple> simpleBranches = new ArrayList<BranchSimple>();
        	List<Branch> branches = BranchManager.getRestaurantBranches(store.getKey());
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
        	
        	RestaurantType storeType = RestaurantTypeManager.getRestaurantType(store.getRestaurantType());
        	StoreSimple storeSimple = new StoreSimple(
        			KeyFactory.keyToString(store.getKey()),
        			storeType.getStoreSuperType(),
        			store.getRestaurantType().toString(),
        			store.hasNewsService(),
        			store.hasProductsService(),
        			store.hasServiceProvidersService(),
        			store.hasMessagesService(),
        			store.getRestaurantName(),
        			store.getChannelNumber() != null ? 
        					store.getChannelNumber() : 0,
        			store.getRestaurantDescription(),
        			store.getRestaurantWebsite() != null ? 
        					store.getRestaurantWebsite() : new Link(""),
        					store.getRestaurantLogo() != null ? 
        							store.getRestaurantLogo() : 
        							new BlobKey(""),
        			DateManager.printDateAsTime24(
        					store.getRestaurantOpeningTime()),
        			DateManager.printDateAsTime24(
        					store.getRestaurantClosingTime()),
        			simpleBranches);
        	simpleStores.add(storeSimple);
        }
        
        return simpleStores;
    }

}