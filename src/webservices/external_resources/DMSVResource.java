/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.external_resources;

import java.util.Date;
import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import util.DateManager;
import webservices.datastore_simple.DMSVSimple;
import datastore.DMSV;
import datastore.DMSVManager;

/**
 * This class represents the list of DMSV
 * as a Resource with only one representation
 */

public class DMSVResource extends ServerResource {

	/**
	 * Returns today's DMSV as a JSON object.
	 * @return An instance of DMSV in JSON format
	 */
    @Get("json")
    public DMSVSimple toJson() {
    	
    	Date today = new Date();
    	List<DMSV> dmsvs = DMSVManager.getDMSVsInDate(today);

    	DMSVSimple dmsvSimple = null;
    	if (!dmsvs.isEmpty()) {
    		DMSV dmsv = dmsvs.get(0);
    		dmsvSimple = new DMSVSimple(
    				dmsv.getKey(),
    				DateManager.printDateAsString(
    						dmsv.getDMSVReleaseDate()),
    				dmsv.getField1().getValue(),
    				dmsv.getField2().getValue(),
    				dmsv.getField3().getValue(),
    				dmsv.getField4().getValue(),
    				dmsv.getField5().getValue(),
    				dmsv.getField6().getValue(),
    				dmsv.getField7().getValue(),
    				dmsv.getField8().getValue(),
    				dmsv.getField9().getValue(),
    				dmsv.getField10().getValue(),
    				dmsv.getField11().getValue(),
    				dmsv.getField12().getValue(),
    				dmsv.getField13().getValue(),
    				dmsv.getField14().getValue(),
    				dmsv.getField15().getValue(),
    				dmsv.getField16().getValue()
    				);
    	}
    	
    	return dmsvSimple;
    }

}