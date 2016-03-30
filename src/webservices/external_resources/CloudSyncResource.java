/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.external_resources;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import util.DateManager;
import webservices.datastore_simple.CloudSyncCommandSimple;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import datastore.Customer;
import datastore.CustomerManager;
import datastore.sheep.CloudSyncCommand;
import datastore.sheep.UserGroup;
import datastore.sheep.UserGroupManager;

/**
 * This class represents a message uplodaded by a sync-master user
 * in the Cloud Sync application. The message downloaded is the last
 * message uploaded by the sync-master.
 */

public class CloudSyncResource extends ServerResource {
	
	private static final Logger log = 
	        Logger.getLogger(CloudSyncResource.class.getName());
	
	/**
	 * Returns a CloudSyncCommand as a JSON object.
	 * @return A CloudSyncCommandSimple instance in JSON format
	 */
    @Get("json")
    public CloudSyncCommandSimple toJson() {
    	
    	String queryInfo = (String) getRequest().getAttributes()
                .get("queryinfo");

	    char searchBy = queryInfo.charAt(0);
	    String searchString = queryInfo.substring(2);
	       
	    Key groupKey = KeyFactory.stringToKey(searchString);
	    log.info("Query: " + searchBy + "=" + searchString);
    	
	    UserGroup userGroup = UserGroupManager.getUserGroup(groupKey);
	    Key cloudSyncOwnerKey = userGroup.getKey().getParent();
	    Customer cloudSyncOwner = CustomerManager.getCustomer(cloudSyncOwnerKey);
	    
	    CloudSyncCommand cloudSyncCommand = cloudSyncOwner.getCloudSyncCommand();
	    
	    Customer cloudSyncMaster = 
	    		CustomerManager.getCustomer(cloudSyncCommand.getCloudSyncCommandMaster());
	    
	    // Check whether the Cloud Sync command was shared to this group or not
	    ArrayList<Key> cloudSyncCommandGroups = cloudSyncCommand.getCloudSyncCommandGroups();
	    if (!cloudSyncCommandGroups.contains(groupKey)) {
	    	// Return empty command
	    	return new CloudSyncCommandSimple(
	    			"",
	    			"",
	    			"",
	    			""
	    			);
	    }
	    
	    CloudSyncCommandSimple cloudSyncCommandSimple = 
	    		new CloudSyncCommandSimple(
	    				KeyFactory.keyToString(cloudSyncCommand.getKey()),
	    				cloudSyncMaster.getUser().getUserEmail().getEmail(),
	    				cloudSyncCommand.getCloudSyncCommandMessage() != null ?
	    						cloudSyncCommand.getCloudSyncCommandMessage() : "",
	    				DateManager.printDateAsString(
	    						cloudSyncCommand.getCloudSyncCommandCreationDate())
	    		);
	    
	    return cloudSyncCommandSimple;
    }

}