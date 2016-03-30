/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Link;

import datastore.RestaurantType;


/**
 * This class represents a simple version of the Store table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class StoreSimple implements Serializable {
    
	public String key;
	public RestaurantType.StoreSuperType storeSuperType;
	public String storeType;
	public Boolean hasNewsService;
	public Boolean hasProductsService;
	public Boolean hasServiceProvidersService;
	public Boolean hasMessagesService;
	public String storeName;
	public Integer channelNumber;
	public String storeDescription;
	public Link storeWebsite;
	public BlobKey storeLogo;
	public String storeOpeningTime; 
	public String storeClosingTime;
	public ArrayList<BranchSimple> branches;
    
    /**
     * StoreSimple constructor.
     * @param key
     * 			: store key
     * @param RestaurantType.StoreSuperType storeSuperType
     * 			: store super type
     * @param storeType
     * 			: store type key
     * @param hasNewsService
     * 			: has news service
     * @param hasProductsService
     * 			: has products service
     * @param hasServiceProvidersService
     * 			: has service providers service
     * @param hasMessagesService
     * 			: has messages service
     * @param storeName
     * 			: store name
     * @param channelNumber
     * 			: channel number
     * @param storeDescription
     * 			: store description
     * @param storeWebsite
     * 			: store website
     * @param storeLogo
     * 			: store logo blob key
     * @param storeOpeningTime
     * 			: store opening time
     * @param storeClosingTime
     * 			: store closing time
     * @param branches
     * 			: store branches
     */
    public StoreSimple(String key, 
    		RestaurantType.StoreSuperType storeSuperType,
    		String storeType,
    		Boolean hasNewsService,
    		Boolean hasProductsService,
    		Boolean hasServiceProvidersService,
    		Boolean hasMessagesService,
    		String storeName, Integer channelNumber,
    		String storeDescription,
    		Link storeWebsite, BlobKey storeLogo, 
    		String storeOpeningTime, String storeClosingTime,
    		ArrayList<BranchSimple> branches) {

    	this.key = key;
    	this.storeSuperType = storeSuperType;
    	this.storeType = storeType;
    	this.hasNewsService = hasNewsService;
    	this.hasProductsService = hasProductsService;
    	this.hasServiceProvidersService = hasServiceProvidersService;
    	this.hasMessagesService = hasMessagesService;
    	this.storeName = storeName;
    	this.channelNumber = channelNumber;
    	this.storeDescription = storeDescription;
        this.storeWebsite = storeWebsite;
        this.storeLogo = storeLogo;
        this.storeOpeningTime = storeOpeningTime;
        this.storeClosingTime = storeClosingTime;
        this.branches = branches;
    }
    
    /**
     * Compare this store with another Store
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this Store, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof StoreSimple ) ) return false;
        StoreSimple r = (StoreSimple) o;
        return this.key.equals(r.key);
    }
    
}
