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
import webservices.datastore_simple.OrderSimple;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.KeyFactory;

import datastore.Branch;
import datastore.BranchManager;
import datastore.Order;
import datastore.OrderManager;
import datastore.Restaurant;
import datastore.RestaurantManager;

/**
 * This class represents the list of orders
 * as a Resource with only one representation
 */

public class OrderListResource extends ServerResource {
	
	private static final Logger log = 
	        Logger.getLogger(OrderListResource.class.getName());

	/**
	 * Returns the order list as a JSON object.
	 * @return An ArrayList of Order in JSON format
	 */
    @Get("json")
    public ArrayList<OrderSimple> toJson() {
    	
    	String queryInfo = (String) getRequest().getAttributes()
                .get("queryinfo");
            
        char searchBy = queryInfo.charAt(0);
        String searchEmailString = queryInfo.substring(2);
        Email searchEmail = new Email(searchEmailString);
        
        log.info("Query: " + searchBy + "=" + searchEmailString);
    	
        //List<Order> orders = OrderManager.getAllModifiedOrdersFromCustomer(searchEmail);
        List<Order> orders = OrderManager.getAllOrdersFromCustomer(searchEmail);
        ArrayList<OrderSimple> ordersSimple = new ArrayList<OrderSimple>();
        for (Order order : orders) {
        	
        	Restaurant restaurant = RestaurantManager.getRestaurant(order.getBranch().getParent());
        	Branch branch = BranchManager.getBranch(order.getBranch());
        	String restaurantName = restaurant != null ? restaurant.getRestaurantName() : "Not Available"; // TODO: Remove later
        	String branchName = branch != null ? branch.getBranchName() : "Not Available"; // TODO: Remove later
        	
        	OrderSimple orderSimple = new OrderSimple(
        			order.getKey().toString(),
        			order.getOrderTypeString(),
        			DateManager.printDateAsString(order.getOrderTime()),
        			KeyFactory.keyToString(order.getBranch()),
        			order.getOrderStatusString(),
        			restaurantName, // TODO: Remove later
        			branchName // TODO: Remove later
        			);
        	ordersSimple.add(orderSimple);
        }
        
        return ordersSimple;
    }

}