/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.external_resources;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PostalAddress;

import util.DateManager;
import webservices.datastore_simple.OrderDetailMenuItemAdditionalPropertySimple;
import webservices.datastore_simple.OrderDetailSetSimple;
import webservices.datastore_simple.OrderDetailSimple;
import webservices.datastore_simple.OrderSimple2;

import datastore.Branch;
import datastore.BranchManager;
import datastore.MenuItem;
import datastore.MenuItemManager;
import datastore.Order;
import datastore.OrderDetail;
import datastore.OrderDetailMenuItemAdditionalProperty;
import datastore.OrderDetailSet;
import datastore.OrderManager;
import datastore.Restaurant;
import datastore.RestaurantManager;

/**
 * This class represents an order
 * as a Resource with only one representation
 */

public class OrderDetailsResource extends ServerResource {

	private static final Logger log = 
	        Logger.getLogger(OrderDetailsResource.class.getName());
	
	/**
	 * Returns an order as a JSON object.
	 * @return An instance of Order in JSON format
	 */
    @Get("json")
    public OrderSimple2 toJson() {
        
        String queryInfo = (String) getRequest().getAttributes()
                .get("queryinfo");
            
        char searchBy = queryInfo.charAt(0);
        String searchKeyString = queryInfo.substring(2);
        
        log.info("Query: " + searchBy + "=" + searchKeyString);
        
        Long searchKey = Long.parseLong(searchKeyString);
        
        Order order = OrderManager.getOrder(searchKey);
        
        Restaurant restaurant = 
        		RestaurantManager.getRestaurant(order.getBranch().getParent());
    	Branch branch = BranchManager.getBranch(order.getBranch());
    	String restaurantName = restaurant != null ? 
    			restaurant.getRestaurantName() : "Not Available"; // TODO: Remove later
    	String branchName = branch != null ? 
    			branch.getBranchName() : "Not Available"; // TODO: Remove later
        
        // Create comments for NEWS and OPINION POLLS
        String orderComments = OrderManager.createOrderComments(order);
        
        // Order Details
        ArrayList<OrderDetailSimple> orderDetailsSimple = 
        		new ArrayList<OrderDetailSimple>();
        for (OrderDetail orderDetail : order.getOrderDetails()) {
        	
        	MenuItem menuItem = 
        			MenuItemManager.getMenuItem(orderDetail.getMenuItem()); //TODO: Remove later
        	
        	OrderDetailSimple orderDetailSimple = new OrderDetailSimple(
        					KeyFactory.keyToString(orderDetail.getKey()),
        					KeyFactory.keyToString(orderDetail.getMenuItem()),
        					orderDetail.getOrderDetailQuantity(),
        					menuItem != null ? 
        							menuItem.getMenuItemName() : "Not Available" // TODO: Remove later
        			);
        	orderDetailsSimple.add(orderDetailSimple);
        }
        
        // Order Additional Property Details
        ArrayList<OrderDetailMenuItemAdditionalPropertySimple> orderAdditionalPropertyDetailsSimple = 
        		new ArrayList<OrderDetailMenuItemAdditionalPropertySimple>();
        for (OrderDetailMenuItemAdditionalProperty orderAdditionalPropertyDetail : 
        		order.getOrderAdditionalPropertyDetails()) {
        	// Property values
        	ArrayList<String> propertyValueKeyStrings = new ArrayList<String>();
        	for (Key key : orderAdditionalPropertyDetail.getMenuItemAdditionalPropertyValues()) {
        		propertyValueKeyStrings.add(KeyFactory.keyToString(key));
        	}
        	        	
        	OrderDetailMenuItemAdditionalPropertySimple orderAdditionalPropertyDetailSimple =
        			new OrderDetailMenuItemAdditionalPropertySimple(
        					KeyFactory.keyToString(orderAdditionalPropertyDetail.getKey()),
        					propertyValueKeyStrings,
        					orderAdditionalPropertyDetail.getOrderDetailMenuItemAdditionalPropertyQuantity()
        					);
        	orderAdditionalPropertyDetailsSimple.add(orderAdditionalPropertyDetailSimple);
        }
        
        // Order Set Details
        ArrayList<OrderDetailSetSimple> orderSetDetailsSimple = 
        		new ArrayList<OrderDetailSetSimple>();
        for (OrderDetailSet orderDetailSet : order.getOrderSetDetails()) {
        	
        	// Get TypeSetMenuItem keys
        	ArrayList<String> typeSetMenuItemKeys = new ArrayList<String>();
        	if (orderDetailSet.getTypeSetMenuItems() != null) {
	        	for (Key key : orderDetailSet.getTypeSetMenuItems()) {
	        		typeSetMenuItemKeys.add(KeyFactory.keyToString(key));
	        	}
        	}
        	
        	OrderDetailSetSimple orderDetailSetSimple =
        			new OrderDetailSetSimple(
        					KeyFactory.keyToString(orderDetailSet.getKey()),
        					KeyFactory.keyToString(orderDetailSet.getSet()),
        					typeSetMenuItemKeys,
        					orderDetailSet.getOrderDetailSetQuantity()
        					);
        	
        	orderSetDetailsSimple.add(orderDetailSetSimple);
        }
        
        OrderSimple2 orderSimple = new OrderSimple2(
        		order.getKey().toString(),
        		order.getOrderStatusString(),
        		order.getRestaurantNews() != null ? 
        				KeyFactory.keyToString(order.getRestaurantNews()) : "",
                order.getRestaurantOpinionPoll() != null ? 
                		KeyFactory.keyToString(order.getRestaurantOpinionPoll()) : "",
        		DateManager.printDateAsString(order.getOrderTime()),
        		KeyFactory.keyToString(order.getBranch()),
        		order.getOrderTypeString(),
        		order.getOrderPaymentTypeString(),
        		order.getOrderDeliveryTypeString(),
        		order.getOrderDeliveryAddress() != null ? 
        				order.getOrderDeliveryAddress() : new PostalAddress(""),
        		order.getOrderDeliveryFee()  != null ? order.getOrderDeliveryFee() : 0.0,
        		DateManager.printDateAsString(order.getTimeToServe()),
        		order.getNumberOfPeople() != null ? order.getNumberOfPeople() : 0,
        		order.getOrderContactPhone(),
        		order.getOrderTotal() != null ? order.getOrderTotal() : 0,
        		orderComments,
        		restaurantName, // TODO: Remove later
    			branchName, // TODO: Remove later
    			orderDetailsSimple,
    			orderAdditionalPropertyDetailsSimple,
    			orderSetDetailsSimple
        		);
 
        return orderSimple;
    }
}