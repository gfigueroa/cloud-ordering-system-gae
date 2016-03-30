/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.PostalAddress;

import exceptions.InvalidFieldFormatException;
import exceptions.MissingRequiredFieldsException;
import exceptions.ObjectExistsInDatastoreException;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the Order class.
 * 
 */

public class OrderManager {
	
	private static final Logger log = 
        Logger.getLogger(OrderManager.class.getName());
	
	/**
     * Get a Order instance from the datastore given the Order key.
     * @param key
     * 			: the Order's key
     * @return Order instance, null if Order is not found
     */
	public static Order getOrder(Long key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Order order;
		try  {
			order = pm.getObjectById(Order.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return order;
	}

	/**
     * Get ALL the Orders in the database for a specific
     * customer and return them in a List structure
     * @param customerKey:
     * 				the key of the customer whose orders will be retrieved
     * @param openOrders:
     * 				whether the OrderManager should only retrieve open orders 
     * 				or closed orders
     * @return all orders in the datastore corresponding to the given customer key
     * TODO: Make more efficient "touching" of the orders
     */
    @SuppressWarnings("unchecked")
	public static List<Order> getAllOrdersFromCustomer(Key customerKey, 
			boolean openOrders) {
		PersistenceManager pm = PMF.get().getPersistenceManager();

        Query query = pm.newQuery(Order.class);
    	query.setFilter("customer == customerkey");
    	query.setOrdering("orderTime desc");
        query.declareParameters(Key.class.getName() + " customerkey");
        
        try {
        	List<Order> allOrders = (List<Order>) query.execute(customerKey);
        	ArrayList<Order> openedOrders = new ArrayList<Order>();
        	ArrayList<Order> closedOrders = new ArrayList<Order>();
        	
        	for (Order order : allOrders) {
        		if (order.isStillOpen()) {
        			openedOrders.add(order);
        		}
        		else {
        			closedOrders.add(order);
        		}
        	}
        	
        	if (openOrders) {
        		return openedOrders;
        	}
        	else {
        		return closedOrders;
        	}
        }
        finally {
        	pm.close();
            query.closeAll();
        }
    }
    
    /**
     * Get ALL the Orders in the database for a specific
     * customer and return them in a List structure
     * @param customerEmail:
     * 				the email of the customer whose orders will be retrieved
     * @param openOrders:
     * 				whether the OrderManager should only retrieve open orders 
     * 				or closed orders
     * @return all orders in the datastore corresponding to the given customer email
     */
    @SuppressWarnings("unchecked")
	public static List<Order> getAllOrdersFromCustomer(Email customerEmail, 
			boolean openOrders) {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Key customerKey = KeyFactory.createKey(Customer.class.getSimpleName(), 
				customerEmail.getEmail());
		
        Query query = pm.newQuery(Order.class);
    	query.setFilter("customer == customerkey");
    	query.setOrdering("orderTime desc");
        query.declareParameters(Key.class.getName() + " customerkey");
        
        try {
        	List<Order> allOrders = (List<Order>) query.execute(customerKey);
        	ArrayList<Order> openedOrders = new ArrayList<Order>();
        	ArrayList<Order> closedOrders = new ArrayList<Order>();
        	
        	for (Order order : allOrders) {
        		if (order.isStillOpen()) {
        			openedOrders.add(order);
        		}
        		else {
        			closedOrders.add(order);
        		}
        	}
        	
        	if (openOrders) {
        		return openedOrders;
        	}
        	else {
        		return closedOrders;
        	}
        }
        finally {
        	pm.close();
            query.closeAll();
        }
    }
    
    /**
     * Get ALL the Orders in the database for a specific
     * customer and return them in a List structure
     * @param customerEmail:
     * 				the email of the customer whose orders will be retrieved
     * @return all orders in the datastore corresponding to the given customer email
     * TODO: Make more efficient "touching" of the orders
     */
    @SuppressWarnings("unchecked")
	public static List<Order> getAllOrdersFromCustomer(Email customerEmail) {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Key customerKey = KeyFactory.createKey(Customer.class.getSimpleName(), 
				customerEmail.getEmail());
		
        Query query = pm.newQuery(Order.class);
    	query.setFilter("customer == customerkey");
    	query.setOrdering("orderTime desc");
        query.declareParameters(Key.class.getName() + " customerkey");
        
        try {
        	List<Order> allOrders = (List<Order>) query.execute(customerKey);
        	
        	// Touch the orders
        	for (Order order : allOrders) {
        		order.getKey();
        	}
        	
        	return allOrders;
        }
        finally {
        	pm.close();
            query.closeAll();
        }
    }
    
    /**
     * Get ALL the Orders in the database for a specific
     * customer and return them in a List structure. The orders returned
     * are only those which have been modified. If the order has not been
     * modified, then it must not be returned as it is assumed that the user
     * already contains it in the mobile device's local database
     * @param customerEmail:
     * 				the email of the customer whose orders will be retrieved
     * @return all orders in the datastore corresponding to the given customer email
     * 				which haven't been modified
     * TODO: Make more efficient "touching" of the orders
     */
    @SuppressWarnings("unchecked")
	public static List<Order> getAllModifiedOrdersFromCustomer(Email customerEmail) {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Key customerKey = KeyFactory.createKey(Customer.class.getSimpleName(), 
				customerEmail.getEmail());
		
        Query query = pm.newQuery(Order.class);
    	query.setFilter("customer == customerkey");
    	query.setOrdering("orderTime desc");
        query.declareParameters(Key.class.getName() + " customerkey");
        
        try {
        	List<Order> allOrders = (List<Order>) query.execute(customerKey);
        	ArrayList<Order> modifiedOrders = new ArrayList<Order>();
        	
        	for (Order order : allOrders) {
        		if (order.isModified() == null || order.isModified()) {
        			order.setModified(false);
        			modifiedOrders.add(order);
        		}
        	}
        	
        	return modifiedOrders;
        }
        finally {
        	pm.close();
            query.closeAll();
        }
    }
	
    /**
     * Get ALL the Orders in the database for a specific
     * restaurant and return them in a List structure
     * @param restaurantKey:
     * 				the key of the restaurant whose orders will be retrieved
     * @param openOrders:
     * 				whether the OrderManager should only retrieve open orders 
     * 				or closed orders
     * @return all orders in the datastore corresponding to the given restaurant key
     * TODO: Make more efficient "touching" of the orders
     */
    @SuppressWarnings("unchecked")
	public static List<Order> getAllOrdersFromRestaurant(Key restaurantKey, 
			boolean openOrders) {
		PersistenceManager pm = PMF.get().getPersistenceManager();

        Query query = pm.newQuery(Order.class);
    	query.setFilter("branch == branchkey");
    	query.setOrdering("orderTime desc");
        query.declareParameters(Key.class.getName() + " branchkey");
        
        try {
        	ArrayList<Order> allOrders = new ArrayList<Order>();
        	ArrayList<Order> openedOrders = new ArrayList<Order>();
        	ArrayList<Order> closedOrders = new ArrayList<Order>();
        	List<Branch> branches = BranchManager.getRestaurantBranches(restaurantKey);
        	for (Branch branch : branches) {	
	        	List<Order> branchOrders = (List<Order>) query.execute(branch.getKey());
	        	allOrders.addAll(branchOrders);
        	}
        	
        	for (Order order : allOrders) {
        		if (order.isStillOpen()) {
        			openedOrders.add(order);
        		}
        		else {
        			closedOrders.add(order);
        		}
        	}
        	
        	if (openOrders) {
        		return openedOrders;
        	}
        	else {
        		return closedOrders;
        	}
        }
        finally {
        	pm.close();
            query.closeAll();
        }
    }
    
    /**
     * Get ALL the Orders in the database for a specific
     * branch and return them in a List structure
     * @param branchKey:
     * 				the key of the branch whose orders will be retrieved
     * @param onlyOpenOrders:
     * 				whether the OrderManager should only retrieve open orders or not
     * @return all orders in the datastore corresponding to the given branch key
     * TODO: Make more efficient "touching" of the orders
     */
    @SuppressWarnings("unchecked")
	public static List<Order> getAllOrdersFromBranch(Key branchKey, 
			boolean onlyOpenOrders) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query = pm.newQuery(Order.class);
    	query.setFilter("branch == branchkey");
    	query.setOrdering("orderTime desc");
        query.declareParameters(Key.class.getName() + " branchkey");
        
        try {
        	List<Order> allOrders = (List<Order>) query.execute(branchKey);
        	ArrayList<Order> openOrders = new ArrayList<Order>();
        	
        	for (Order order : allOrders) {
        		if (order.isStillOpen()) {
        			openOrders.add(order);
        		}
        	}
        	
        	if (onlyOpenOrders) {
        		return openOrders;
        	}
        	else {
        		return allOrders;
        	}
        }
        finally {
        	pm.close();
            query.closeAll();
        }
    }
    
    /**
     * Get ALL the Orders in the database for a specific
     * restaurant and return them grouped by branch
     * @param Key restaurantKey:
     * 				the key of the restaurant whose orders will be retrieved
     * @param openOrders:
     * 				whether the OrderManager should only retrieve 
     * 				open orders or closed orders
     * @return a HashMap of the orders corresponding to the given restaurant key
     * 				grouped by branch
     * TODO: Make more efficient "touching" of the orders
     */
	public static HashMap<Key, ArrayList<Order>> 
    		getAllOrdersGroupedByBranch(Key restaurantKey, boolean openOrders) {
		
    	List<Order> orderList = getAllOrdersFromRestaurant(restaurantKey, openOrders);
    	
    	HashMap<Key, ArrayList<Order>> orderMap = new HashMap<Key, ArrayList<Order>>();
        for (Order order : orderList) {
        	Key branchKey = order.getBranch();
        	ArrayList<Order> orderArrayList = orderMap.get(branchKey);
        	if (orderArrayList == null) {
        		orderArrayList = new ArrayList<Order>();
        	}
        	orderArrayList.add(order);
        	orderMap.put(branchKey, orderArrayList);
        }
        
        return orderMap;
    }
	
	/**
	 * Create the order comments for this order for NEWS and OPINION types.
	 * @param order
	 * 			: the order whose comments will be created
	 * @return A formatted string for the order comments.
	 */
    public static String createOrderComments(Order order) {
    	String orderComments = order.getOrderComments() != null ? 
    			order.getOrderComments() : "";
        
    	// If the type is NEWS
    	if (order.getOrderType() == Order.OrderType.NEWS) {
        	RestaurantNews restaurantNews = 
        			RestaurantNewsManager.getRestaurantNews(order.getRestaurantNews());
        	
        	// Check if the restaurant hasn't been deleted
        	if (restaurantNews != null) {
        		orderComments += "共 " + restaurantNews.getCurrentClicks() + " 位已回應。";
        	}
        	else {
        		orderComments += "This store has been deleted from the system.";
        	}
        }
    	// If the type is OPINION
        else if (order.getOrderType() == Order.OrderType.OPINION) {
        	RestaurantOpinionPoll restaurantOpinionPoll = 
        			RestaurantOpinionPollManager.getRestaurantOpinionPoll(
        					order.getRestaurantOpinionPoll());
        	
        	// Check if the opinion poll hasn't been deleted from the system
        	if (restaurantOpinionPoll != null) {
        		
            	// Check that the results are public
            	if (restaurantOpinionPoll.resultsArePublic()) {
		        	orderComments += "共 " + restaurantOpinionPoll.getCurrentClicks() + 
		        			" 位已回應。\n";
		        	orderComments += 
		        			RestaurantOpinionPollManager.getRestaurantOpinionPollStatistics(
		        					restaurantOpinionPoll);
            	}
        	}
        	else {
        		orderComments += "This opinion poll has been deleted from the system.";
        	}
        }
    	
    	return orderComments;
    }
	
	/**
     * Put Order into datastore.
     * Orders the given Order instance in the datastore calling the 
     * PersistenceManager's makePersistent() method.
     * @param order
     * 			: the Order instance to order
     * @throws ObjectExistsInDatastoreException 
     */
	public static void putOrder(Order order) 
           throws ObjectExistsInDatastoreException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.makePersistent(order);
			tx.commit();
			log.info("Order \"" + order.getKey() + 
				"\" stored successfully in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Delete Order from datastore.
    * Deletes the given Order from the datastore calling the PersistenceManager's
    * deletePersistent() method.
    * @param order
    * 			: the Order instance to delete
    */
	public static void deleteOrder(Long orderKey) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Order order = pm.getObjectById(Order.class, orderKey);
			tx.begin();
			pm.deletePersistent(order);
			tx.commit();
			log.info("Order \"" + orderKey + "\" deleted successfully from datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
    * Update Order attributes.
    * Updates the given Order's attributes in the datastore.
    * @param key
    * 			: the key of the Order whose attributes will be updated
    * @param status
    * 			: the new status for the Order
    * @param orderDeliveryFee
    * 			: the new order delivery fee
    * @param orderComments
    * 			: the new comments to give to the Order
	* @throws MissingRequiredFieldsException  
    */
	public static void updateOrderAttributes(Long key,
											 Order.Status status,
											 Double orderDeliveryFee,
                                             String orderComments) 
			throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Order order = pm.getObjectById(Order.class, key);
			tx.begin();
			order.setOrderStatus(status);
			order.setOrderDeliveryFee(orderDeliveryFee);
			order.setOrderComments(orderComments);
			tx.commit();
			log.info("Order \"" + key + "\"'s attributes updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Update Order attributes by Customer.
    * Customer updates the given Order's attributes in the datastore.
    * @param key
    * 			: the key of the Order whose attributes will be updated
    * @param orderType
    * 			: the order type
    * @param orderPaymentType
    * 			: the order payment type
    * @param orderDeliveryType
    * 			: the order delivery type
    * @param orderContactPhone
    * 			: the contact phone number of the customer
    * @param customerLocation
    * 			: the location of the customer at the moment of modifying
    * @param orderDeliveryAddress
    * 			: the order delivery address (for DELIVERY orders)
    * @param timeToServe
    * 			: the time the order will be served
    * @param numberOfPeople
    * 			: the number of people going to the store (for DINE_IN orders)
    * @param orderDetails
    * 			: the new order details (menu items)
    * @param orderAdditionalPropertyDetails
    * 			: the new order additional property details
    * @param orderSetDetails
    * 			: the new order set details
	* @throws MissingRequiredFieldsException  
	* @throws InvalidFieldFormatException 
    */
	public static void updateOrderAttributesByCustomer(
			Long key,
			Order.OrderType orderType,
			Order.OrderPaymentType orderPaymentType,
			Order.OrderDeliveryType orderDeliveryType,
			PhoneNumber orderContactPhone,
			GeoPt customerLocation,
			PostalAddress orderDeliveryAddress,
			Date timeToServe,
			Integer numberOfPeople,
			List<OrderDetail> orderDetails,
			List<OrderDetailMenuItemAdditionalProperty> 
					orderAdditionalPropertyDetails,
			List<OrderDetailSet> orderSetDetails
			) 
			throws MissingRequiredFieldsException, 
			InvalidFieldFormatException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Order order = pm.getObjectById(Order.class, key);
			tx.begin();
			order.setOrderStatus(Order.Status.CHANGE_REQUEST);
			order.setOrderType(orderType);
			order.setOrderPaymentType(orderPaymentType);
			order.setOrderDeliveryType(orderDeliveryType);
			order.setOrderContactPhone(orderContactPhone);
			order.setCustomerLocation(customerLocation);
			order.setOrderDeliveryAddress(orderDeliveryAddress);
			order.setTimeToServe(timeToServe);
			order.setNumberOfPeople(numberOfPeople);
			order.setOrderDetails(orderDetails);
			order.setOrderAdditionalPropertyDetails(
					orderAdditionalPropertyDetails);
			order.setOrderSetDetails(orderSetDetails);
			tx.commit();
			log.info("Order \"" + key + "\"'s attributes updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Refresh Order total.
    * Refreshes the Order total after modifying the order details
    * @param key
    * 			: the key of the Order whose total will be refreshed
	* @throws MissingRequiredFieldsException  
    */
	public static void refreshOrderTotal(Long key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {
			Order order = pm.getObjectById(Order.class, key);
			order.setOrderTotal();
			log.info("Order \"" + key + "\"'s attributes updated in datastore.");
		}
		finally {
			pm.close();
		}
	}
	
	/**
    * Cancel order (by customer).
    * Cancels the given order and sets the cancellation comments.
    * @param key
    * 			: the key of the Order whose attributes will be updated
    * @param cancellationComments
    * 			: the reasons/comments from the customer for cancelling
    */
	public static void cancelOrder(
			Long key,
			String cancellationComments) 
			throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Order order = pm.getObjectById(Order.class, key);
			tx.begin();
			order.setOrderStatus(Order.Status.CANCELLED);
			order.setCancellationComments(cancellationComments);
			tx.commit();
			log.info("Order \"" + key + "\"' cancelled and attributes " +
					"updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
}
