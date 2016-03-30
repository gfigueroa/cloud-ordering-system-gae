/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.PostalAddress;

import exceptions.InexistentObjectException;
import exceptions.InvalidFieldFormatException;
import exceptions.MissingRequiredFieldsException;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import util.FieldValidator;

/**
 * This class represents the Order table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@PersistenceCapable
public class Order {

	// Enumerator for status
	public static enum Status {
		UNPROCESSED, PROCESSING, ACCEPTED, CHANGE_REQUEST,
		READY, EN_ROUTE, CANCELLED, CLOSED, PAYMENT_REVOKED,
		REVOKED, UNCLAIMED, CLOSED_CANCELLED
	}

	// Enumerator for order type
	public static enum OrderType {
		DELIVERY, TAKE_OUT, TAKE_IN, NEWS, OPINION
	}
	
	// Enumerator for payment type
	public static enum OrderPaymentType {
		CASH, COUPON, STAMP, SMART_CASH, NO_PAYMENT
	}
	
	// Enumerator for delivery type
	public static enum OrderDeliveryType {
		REGULAR, POSTAL, UPS, CONVENIENCE_STORE
	}
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long key;

    @Persistent
    private Key customer;
    
    @Persistent
    private Key branch;
    
    @Persistent
    private Key restaurantNews;
    
    @Persistent
    private Key restaurantOpinionPoll;
    
    @Persistent
    private Date orderTime;
    
    @Persistent
	private Status status;
    
    @Persistent
	private OrderType orderType;
    
    @Persistent
	private OrderPaymentType orderPaymentType;
    
    @Persistent
    private OrderDeliveryType orderDeliveryType;
    
    @Persistent
    private PhoneNumber orderContactPhone;
    
    @Persistent
    private GeoPt customerLocation;
    
    @Persistent
    private PostalAddress orderDeliveryAddress;
    
    @Persistent
    private Double orderDeliveryFee;
    
    @Persistent
    private Date timeToServe;
    
    @Persistent
    private Integer numberOfPeople;
    
    @Persistent
    private Double orderTotal;
    
    @Persistent
    private String orderComments;
    
    @Persistent
    private String cancellationComments;

    @Persistent
    Boolean modified;
    
    @Persistent(defaultFetchGroup = "true")
    @Element(dependent = "true")
    private ArrayList<OrderDetail> orderDetails;
    
    @Persistent(defaultFetchGroup = "true")
    @Element(dependent = "true")
    private ArrayList<OrderDetailMenuItemAdditionalProperty>
    		orderAdditionalPropertyDetails;
    
    @Persistent(defaultFetchGroup = "true")
    @Element(dependent = "true")
    private ArrayList<OrderDetailSet> orderSetDetails;

    /**
     * Order constructor.
     * @param customer
     * 			: customer key
     * @param branch
     * 			: branch key
     * @param restaurantNews
     * 			: restaurant news
     * @param restaurantOpinionPoll
     * 			: restaurant opinion poll
     * @param orderType
     * 			: the type of order
     * @param orderPaymentType
     * 			: the type of payment
     * @param orderDeliveryType
     * 			: the type of delivery for this order
     * @param orderContactPhone
     * 			: contact phone of the customer who made the order
     * @param customerLocation
     * 			: the location of the customer at the time of ordering
     * @param orderDeliveryAddress
     * 			: the address where the order will be delivered
     * @param orderDeliveryFee
     * 			: the order delivery fee
     * @param timeToServe
     * 			: the customer's preferred time for serving
     * @param numberOfPeople
     * 			: the number of people in case of take-in orders
     * @param orderComments
     * 			: order comments
     * @throws MissingRequiredFieldsException
     * @throws InvalidFieldFormatException,
     */
    public Order(Key customer, Key branch, Key restaurantNews, Key restaurantOpinionPoll,
    		OrderType orderType, OrderPaymentType orderPaymentType,
    		OrderDeliveryType orderDeliveryType,PhoneNumber orderContactPhone, 
    		GeoPt customerLocation, PostalAddress orderDeliveryAddress, Double orderDeliveryFee,
    		Date timeToServe, Integer numberOfPeople, String orderComments) 
    				throws MissingRequiredFieldsException, InvalidFieldFormatException {
        
    	// Check "required field" constraints
    	if (customer == null || branch == null || orderType == null || 
    			orderPaymentType == null || orderContactPhone == null || timeToServe == null) {
    		throw new MissingRequiredFieldsException(this.getClass(),
    				"Exception in Order constructor: one or more required fields are missing.");
    	}
    	if (orderContactPhone.getNumber().trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(),
    				"Exception in Order constructor: one or more required fields are missing.");
    	}
    	
    	// Check order type dependencies
    	switch (orderType) {
    		case DELIVERY:
    			if (orderDeliveryAddress == null || orderDeliveryAddress.getAddress().isEmpty() ||
    					orderDeliveryType == null) {
    				throw new MissingRequiredFieldsException(this.getClass(),
    	    				"Exception in Order constructor: one or more required fields are missing.");
    			}
    			break;
    		case TAKE_IN:
    			if (numberOfPeople == null) {
    				throw new MissingRequiredFieldsException(this.getClass(),
    	    				"Exception in Order constructor: one or more required fields are missing.");
    			}
    			break;
    		case NEWS:
    			if (restaurantNews == null) {
    				throw new MissingRequiredFieldsException(this.getClass(),
    	    				"Exception in Order constructor: one or more required fields are missing.");
    			}
    			break;
    		case OPINION:
    			if (restaurantOpinionPoll == null) {
    				throw new MissingRequiredFieldsException(this.getClass(),
    	    				"Exception in Order constructor: one or more required fields are missing.");
    			}
    			break;
    	}
    	
    	// Check phone number format
    	if (!FieldValidator.isValidPhoneNumber(orderContactPhone.getNumber())) {
    		throw new InvalidFieldFormatException(this.getClass(), 
    				"Invalid contact phone number.");
    	}
    	
    	this.customer = customer;
    	this.branch = branch;
    	this.restaurantNews = restaurantNews;
    	this.restaurantOpinionPoll = restaurantOpinionPoll;
    	
    	Date now = new Date();
    	this.orderTime = now;
    	
    	this.status = Status.UNPROCESSED;
    	this.orderType = orderType;
    	this.orderPaymentType = orderPaymentType;
    	this.orderDeliveryType = orderDeliveryType;
    	this.orderContactPhone = orderContactPhone;
    	this.customerLocation = customerLocation;
    	this.orderDeliveryAddress = orderDeliveryAddress;
    	this.orderDeliveryFee = orderDeliveryFee;
    	this.timeToServe = timeToServe;
    	this.numberOfPeople = numberOfPeople;
    	this.orderComments = orderComments;
    	this.modified = true;
    	
    	this.orderDetails = new ArrayList<OrderDetail>();
    	this.orderAdditionalPropertyDetails = 
    			new ArrayList<OrderDetailMenuItemAdditionalProperty>();
    	this.orderSetDetails = new ArrayList<OrderDetailSet>();
    }

    /**
     * Get Order key.
     * @return order key
     */
    public Long getKey() {
        return key;
    }
    
    /**
     * Get Order customer.
     * @return customer who made the order
     */
    public Key getCustomer() {
        return customer;
    }
    
    /**
     * Get Order branch.
     * @return branch to which the order was made
     */
    public Key getBranch() {
        return branch;
    }
    
    /**
     * Get Restaurant News.
     * @return restaurant news (key) to which this order belongs to 
     * 			(in case of type NEWS)
     */
    public Key getRestaurantNews() {
        return restaurantNews;
    }
    
    /**
     * Get Restaurant Opinion Poll.
     * @return restaurant opinion poll (key) to which this order belongs to 
     * 			(in case of type OPINION)
     */
    public Key getRestaurantOpinionPoll() {
        return restaurantOpinionPoll;
    }
     
    /**
     * Get Order time.
     * @return Order time of creation
     */
    public Date getOrderTime() {
    	return orderTime;
    }
    
    /**
     * Get current Order status.
     * @return current status of the order
     */
    public Status getOrderStatus() {
    	return status;
    }

    /**
     * Get current Order status as stored in datastore.
     * @return current status of the order as string
     */
    public String getOrderStatusString() {
    	
    	switch (status) {
    		case UNPROCESSED:
    			return "Unprocessed";
    		case PROCESSING:
    			return "Processing";
    		case ACCEPTED:
    			return "Accepted";
    		case CHANGE_REQUEST:
    			return "Change Request";
    		case READY:
    			return "Ready";
    		case EN_ROUTE:
    			return "En-Route";
    		case CANCELLED:
    			return "Cancelled";
    		case CLOSED:
    			return "Closed";
    		case PAYMENT_REVOKED:
    			return "Payment Received";
    		case REVOKED:
    			return "Revoked";
    		case UNCLAIMED:
    			return "Unclaimed";
    		case CLOSED_CANCELLED:
    			return "Closed and Cancelled";
    		default:
    			return "";
    	}
    }
    
    /**
     * Get the Status enum type from a string
     * @param the string corresponding to the Status
     * @return the corresponding Status value
     */
    public static Status statusFromString(String statusString) {
    	if (statusString.equalsIgnoreCase("unprocessed")) {
    		return Status.UNPROCESSED;
    	}
    	else if (statusString.equalsIgnoreCase("processing")) {
    		return Status.PROCESSING;
    	}
    	else if (statusString.equalsIgnoreCase("accepted")) {
    		return Status.ACCEPTED;
    	}
    	else if (statusString.equalsIgnoreCase("change_request")) {
    		return Status.CHANGE_REQUEST;
    	}
    	else if (statusString.equalsIgnoreCase("ready")) {
    		return Status.READY;
    	}
    	else if (statusString.equalsIgnoreCase("en_route")) {
    		return Status.EN_ROUTE;
    	}
    	else if (statusString.equalsIgnoreCase("cancelled")) {
    		return Status.CANCELLED;
    	}
    	else if (statusString.equalsIgnoreCase("closed")) {
    		return Status.CLOSED;
    	}
    	else if (statusString.equalsIgnoreCase("payment_revoked")) {
    		return Status.PAYMENT_REVOKED;
    	}
    	else if (statusString.equalsIgnoreCase("revoked")) {
    		return Status.REVOKED;
    	}
    	else if (statusString.equalsIgnoreCase("unclaimed")){
    		return Status.UNCLAIMED;
    	}
    	else if (statusString.equalsIgnoreCase("closed_cancelled")) {
    		return Status.CLOSED_CANCELLED;
    	}
    	else { 
    		return null;
    	}
    }
    
    /**
     * Order is still open.
     * @return true if the status of the order is not CLOSED, 
     * PAYMENT_REVOKED, REVOKED, CANCELLED_CLOSED or UNCLAIMED, 
     * false otherwise
     */
    public Boolean isStillOpen() {
    	if (this.status == Status.CLOSED || 
    			this.status == Status.PAYMENT_REVOKED ||
    			this.status == Status.REVOKED || 
    			this.status == Status.CLOSED_CANCELLED || 
    			this.status == Status.UNCLAIMED) {
    		return false;
    	}
    	else {
    		return true;
    	}
    }
    
    /**
     * Get Order type.
     * @return type of order
     */
    public OrderType getOrderType() {
        return orderType;
    }
    
    /**
     * Get Order type as a String.
     * @return type of order as a string
     */
    public String getOrderTypeString() {
    	switch (orderType) {
    		case DELIVERY:
    			return "Delivery";
    		case TAKE_OUT:
    			return "Take-Out";
    		case TAKE_IN:
    			return "Dine-In";
    		case NEWS:
    			return "News";
    		case OPINION:
    			return "Opinion-Poll";	
    		default:
    			return "";
    	}
    }
    
    /**
     * Get the OrderType enum type from a string
     * @param the string corresponding to the OrderType
     * @return the corresponding OrderType value
     */
    public static OrderType getOrderTypeFromString(String orderTypeString) {
    	if (orderTypeString.equalsIgnoreCase("delivery")) {
    		return OrderType.DELIVERY;
    	}
    	else if (orderTypeString.equalsIgnoreCase("takeout")) {
    		return OrderType.TAKE_OUT;
    	}
    	else if (orderTypeString.equalsIgnoreCase("takein")) {
    		return OrderType.TAKE_IN;
    	}
    	else if (orderTypeString.equalsIgnoreCase("news")) {
    		return OrderType.NEWS;
    	}
    	else if (orderTypeString.equalsIgnoreCase("opinion")) {
    		return OrderType.OPINION;
    	}
    	else { 
    		return null;
    	}
    }
    
    /**
     * Get Order payment type.
     * @return order payment type
     */
    public OrderPaymentType getOrderPaymentType() {
    	return orderPaymentType;
    }
    
    /**
     * Get Order payment type string.
     * @return a string representation of the order payment type
     */
    public String getOrderPaymentTypeString() {
    	if (orderPaymentType == null) {
    		return "";
    	}
    	
    	switch (orderPaymentType) {
    		case CASH:
    			return "Cash";
    		case COUPON:
    			return "Coupon";
    		case STAMP:
    			return "Stamp";
    		case SMART_CASH:
    			return "Smart Cash";
    		case NO_PAYMENT:
    			return "No Payment";
    		default:
    			return "";
    	}
    }
    
    /**
     * Get Order payment type from string
     * @param orderPaymentTypeString
     * 			: the string representation of the payment type
     * @return payment type
     */
    public static OrderPaymentType getOrderPaymentTypeFromString(
    		String orderPaymentTypeString) {
    	
    	if (orderPaymentTypeString == null) {
    		return null;
    	}
    	
    	if (orderPaymentTypeString.equalsIgnoreCase("cash")) {
    		return OrderPaymentType.CASH;
    	}
    	else if (orderPaymentTypeString.equalsIgnoreCase("coupon")) {
    		return OrderPaymentType.COUPON;
    	}
    	else if (orderPaymentTypeString.equalsIgnoreCase("stamp")) {
    		return OrderPaymentType.STAMP;
    	}
    	else if (orderPaymentTypeString.equalsIgnoreCase("smart_cash")) {
    		return OrderPaymentType.SMART_CASH;
    	}
    	else if (orderPaymentTypeString.equalsIgnoreCase("no_payment")) {
    		return OrderPaymentType.NO_PAYMENT;
    	}
    	else {
    		return null;
    	}
    }
    
    /**
     * Get Order delivery type.
     * @return type of order delivery
     */
    public OrderDeliveryType getOrderDeliveryType() {
        return orderDeliveryType;
    }
    
    /**
     * Get Order delivery type as a String.
     * @return type of order delivery as a string
     */
    public String getOrderDeliveryTypeString() {
    	if (orderDeliveryType == null)
    		return "";
    	
    	switch (orderDeliveryType) {
    		case REGULAR:
    			return "Regular";
    		case POSTAL:
    			return "Postal";
    		case UPS:
    			return "UPS";
    		case CONVENIENCE_STORE:
    			return "Convenience Store";
    		default:
    			return "";
    	}
    }
    
    /**
     * Get the OrderDeliveryType enum type from a string
     * @param the string corresponding to the OrderDeliveryType
     * @return the corresponding OrderDeliveryType value
     */
    public static OrderDeliveryType getOrderDeliveryTypeFromString(
    		String orderDeliveryTypeString) {
    	
    	if (orderDeliveryTypeString == null)
    		return null;
    	
    	if (orderDeliveryTypeString.equalsIgnoreCase("regular")) {
    		return OrderDeliveryType.REGULAR;
    	}
    	else if (orderDeliveryTypeString.equalsIgnoreCase("postal")) {
    		return OrderDeliveryType.POSTAL;
    	}
    	else if (orderDeliveryTypeString.equalsIgnoreCase("ups")) {
    		return OrderDeliveryType.UPS;
    	}
    	else if (orderDeliveryTypeString.equalsIgnoreCase("convenience_store")) {
    		return OrderDeliveryType.CONVENIENCE_STORE;
    	}
    	else { 
    		return null;
    	}
    }
    
    /**
     * Get Order contact phone.
     * @return contact phone of the customer who made the order
     */
    public PhoneNumber getOrderContactPhone() {
        return orderContactPhone;
    }
    
    /**
     * Get Order customer location.
     * @return customer location
     */
    public GeoPt getCustomerLocation() {
    	return customerLocation;
    }
    
    /**
     * Get Order delivery address.
     * @return the address where the order should be delivered
     */
    public PostalAddress getOrderDeliveryAddress() {
        return orderDeliveryAddress;
    }
    
    /**
     * Get Order delivery fee.
     * @return the delivery fee (if any)
     */
    public Double getOrderDeliveryFee() {
        return orderDeliveryFee;
    }
    
    /**
     * Get Time to Serve.
     * @return the time when this order should be served
     */
    public Date getTimeToServe() {
        return timeToServe;
    }
    
    /**
     * Get number of people.
     * @return the number of people going to the restaurant if order
     * 			type is Take-In
     */
    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }
    
    /**
     * Calculate the Order subtotal using current Menu Item prices
     * and discounts.
     * @return Order subtotal
     */
    private double calculateOrderSubtotal() {
    	double subtotal = 0;
    	
    	// Add for order details
    	for (OrderDetail detail : orderDetails) {
    		MenuItem menuItem = 
    				MenuItemManager.getMenuItem(detail.getMenuItem());
    		
    		double discount = 
    				menuItem.getMenuItemDiscount() != null ? 
    						menuItem.getMenuItemPrice() * menuItem.getMenuItemDiscount() : 
    						0;
    		
    		subtotal += detail.getOrderDetailQuantity() * 
    				(menuItem.getMenuItemPrice() - discount);
    	}
    	
    	// Add for order additional property details
    	for (OrderDetailMenuItemAdditionalProperty detail : 
    		orderAdditionalPropertyDetails) {
    		
    		double additionalCharge = 0;
    		
    		ArrayList<MenuItemAdditionalPropertyValue> menuItemAdditionalPropertyValues = 
    				new ArrayList<MenuItemAdditionalPropertyValue>();
    		for (Key key : detail.getMenuItemAdditionalPropertyValues()) {
    			MenuItemAdditionalPropertyValue menuItemAdditionalPropertyValue =
        				MenuItemAdditionalPropertyValueManager.getMenuItemAdditionalPropertyValue(
        						key);
    			menuItemAdditionalPropertyValues.add(menuItemAdditionalPropertyValue);
    			
    			additionalCharge += 
    					menuItemAdditionalPropertyValue.getAdditionalCharge() != null ?
    					menuItemAdditionalPropertyValue.getAdditionalCharge() : 0;
    		} 
    		
    		MenuItem menuItem =
    				MenuItemManager.getMenuItem(
    						menuItemAdditionalPropertyValues.get(0).getKey().getParent());
    		
    		double discount =
    				menuItem.getMenuItemDiscount() != null ?
    						menuItem.getMenuItemPrice() * menuItem.getMenuItemDiscount() :
    						0;
    		
    		subtotal += detail.getOrderDetailMenuItemAdditionalPropertyQuantity() *
    				(menuItem.getMenuItemPrice() - discount);
    		subtotal += detail.getOrderDetailMenuItemAdditionalPropertyQuantity() * 
    				additionalCharge;
    	}
    	
    	// Add for order set details
    	for (OrderDetailSet detail : orderSetDetails) {

    		Set set = SetManager.getSet(detail.getSet());
    		
    		if (set.hasFixedPrice()) {
    			double discount = set.getSetDiscount() != null ?
    					set.getSetPrice() * set.getSetDiscount() : 0;
    					
    			subtotal += detail.getOrderDetailSetQuantity() *
    					(set.getSetPrice() - discount);
    		}
    		else {
    			// TODO
    		}
    	}
    	
    	return subtotal;
    }
    
    /**
     * Calculate the Order total using current Menu Item prices
     * and discounts.
     * @return Order total
     */
    private double calculateOrderTotal() {
    	double additionalCharges =
    			orderDeliveryFee != null ? orderDeliveryFee : 0;

    	return calculateOrderSubtotal() + additionalCharges;
    }
    
    /**
     * Get order total.
     * @return the fixed total of this order. The 
     * 			fixed total uses the prices and discounts
     * 			of menu items at the time of the order.
     */
    public Double getOrderTotal() {
        return orderTotal;
    }
    
    /**
     * Get Order comments.
     * @return order comments
     */
    public String getOrderComments() {
    	return orderComments;
    }
    
    /**
     * Get Order cancellation comments.
     * @return cancellation comments
     */
    public String getCancellationComments() {
    	return cancellationComments;
    }
    
    /**
     * Order has been modified.
     * @return true if the order status or comments have been modified, false otherwise
     */
    public Boolean isModified() {
    	return modified;
    }
    
    /**
     * Get Order details.
     * @return List of order details
     */
    public ArrayList<OrderDetail> getOrderDetails() {
    	return orderDetails;
    }
    
    /**
     * Get Order additional property details.
     * @return List of order additional property details
     */
    public ArrayList<OrderDetailMenuItemAdditionalProperty> 
    		getOrderAdditionalPropertyDetails() {
    	return orderAdditionalPropertyDetails;
    }
    
    /**
     * Get Order set details.
     * @return List of order set details
     */
    public ArrayList<OrderDetailSet> getOrderSetDetails() {
    	return orderSetDetails;
    }
    
    /**
     * Get total number of items in the order
     * @return Total number of items in the order
     */
    public int getOrderTotalItems() {
    	int totalItems = 0;
    	
    	for (OrderDetail detail : orderDetails) {
    		totalItems += detail.getOrderDetailQuantity();
    	}
    	
    	for (OrderDetailMenuItemAdditionalProperty detail : 
    			orderAdditionalPropertyDetails) {
    		totalItems += 
    				detail.getOrderDetailMenuItemAdditionalPropertyQuantity();
    	}
    	
    	for (OrderDetailSet detail : orderSetDetails) {
    		totalItems += detail.getOrderDetailSetQuantity();
    	}
    	
    	return totalItems;
    }
    
    /**
     * Set Order status.
     * @param status
     * 			: status of the order
     * @throws MissingRequiredFieldsException 
     */
    public void setOrderStatus(Status orderStatus) 
    		throws MissingRequiredFieldsException {
    	// Check "required field" constraints
    	if (orderStatus == null) {
    		throw new MissingRequiredFieldsException(this.getClass(),
					"Missing order status.");
    	}
    	this.status = orderStatus;
    	
    	this.modified = true;
    }
    
    /**
     * Set Order type.
     * @param orderType
     * 			: type of order
     * @throws MissingRequiredFieldsException
     */
    public void setOrderType(OrderType orderType) 
    		throws MissingRequiredFieldsException {
    	// Check "required field" constraints
    	if (orderType == null) {
    		throw new MissingRequiredFieldsException(this.getClass(),
					"Missing order type.");
    	}
    	this.orderType = orderType;
    }
    
    /**
     * Set Order payment type.
     * @param orderPaymentType
     * 			: order payment type
     * @throws MissingRequiredFieldsException
     */
    public void setOrderPaymentType(OrderPaymentType orderPaymentType) 
    		throws MissingRequiredFieldsException {
    	// Check "required field" constraints
    	if (orderPaymentType == null) {
    		throw new MissingRequiredFieldsException(this.getClass(),
					"Missing order payment type.");
    	}
    	this.orderPaymentType = orderPaymentType;
    }
    
    /**
     * Set Order delivery type.
     * @param orderDeliveryType
     * 			: order Delivery type
     * @throws MissingRequiredFieldsException
     */
    public void setOrderDeliveryType(OrderDeliveryType orderDeliveryType) 
    		throws MissingRequiredFieldsException {
    	// Check "required field" constraints
    	
    	if (orderType == OrderType.DELIVERY) {
	    	if (orderDeliveryType == null) {
	    		throw new MissingRequiredFieldsException(this.getClass(),
						"Missing order Delivery type.");
	    	}
    	}
    	this.orderDeliveryType = orderDeliveryType;
    }
    
    /**
     * Set Order contact phone.
     * @param orderContactPhone
     * 			: contact phone of the customer who made the order
     * @throws InvalidFieldFormatException 
     * @throws MissingRequiredFieldsException 
     */
    public void setOrderContactPhone(PhoneNumber orderContactPhone) 
    		throws InvalidFieldFormatException, 
    		MissingRequiredFieldsException {
    	// Check "required field" constraints
    	if (orderContactPhone == null || orderContactPhone.getNumber().trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(),
					"Missing order contact phone.");
    	}
    	// Check phone number format
    	if (!FieldValidator.isValidPhoneNumber(orderContactPhone.getNumber())) {
    		throw new InvalidFieldFormatException(this.getClass(), 
    				"Invalid contact phone number.");
    	}
    	this.orderContactPhone = orderContactPhone;
    }
    
    /**
     * Set customer location.
     * @param customerLocation
     * 			: the location of the customer at the time of modification
     */
    public void setCustomerLocation(GeoPt customerLocation) {
    	this.customerLocation = customerLocation;
    }
    
    /**
     * Set Order delivery address.
     * @param orderDeliveryAddress
     * 			: delivery address
     */
    public void setOrderDeliveryAddress(PostalAddress orderDeliveryAddress) {
    	this.orderDeliveryAddress = orderDeliveryAddress;
    }
    
    /**
     * Set Order delivery fee.
     * @param orderDeliveryFee
     * 			: delivery fee
     * @throws MissingRequiredFieldsException 
     */
    public void setOrderDeliveryFee(Double orderDeliveryFee) 
    		throws MissingRequiredFieldsException {
    	if (orderType == Order.OrderType.DELIVERY) {
    		if (orderDeliveryFee == null) {
    			throw new MissingRequiredFieldsException(this.getClass(),
    					"Missing order delivery fee.");
    		}
    	}
    	this.orderDeliveryFee = orderDeliveryFee;
    }
    
    /**
     * Set Time to serve.
     * @param timeToServe
     * 			: the time to serve the order
     * @throws MissingRequiredFieldsException
     */
    public void setTimeToServe(Date timeToServe) 
    		throws MissingRequiredFieldsException {
    	// Check "required field" constraints
    	if (timeToServe == null) {
    		throw new MissingRequiredFieldsException(this.getClass(),
					"Missing time to serve.");
    	}
    	this.timeToServe = timeToServe;
    }
    
    /**
     * Set number of people.
     * @param numberOfPeople
     * 			: the number of people going to the restaurant
     */
    public void setNumberOfPeople(Integer numberOfPeople) {
    	this.numberOfPeople = numberOfPeople;
    }
    
    /**
     * Set order total.
     * Fixes the order total with the current menu item
     * prices and discounts.
     */
    public void setOrderTotal() {
    	this.orderTotal = calculateOrderTotal();
    }
    
    /**
     * Set Order comments.
     * @param orderComments
     * 			: order comments
     */
    public void setOrderComments(String orderComments) {
    	this.orderComments = orderComments;
    	this.modified = true;
    }
    
    /**
     * Set Order cancellation comments.
     * @param cancellationComments
     * 			: cancellation comments
     */
    public void setCancellationComments(String cancellationComments) {
    	this.cancellationComments = cancellationComments;
    	this.modified = true;
    }
    
    /**
     * Set Order has been modified.
     * @param modified
     * 			: order has been modified
     */
    public void setModified(boolean modified) {
    	this.modified = modified;
    }
    
    /**
     * Add order detail.
     * @param orderDetail
     * 			: order detail to add
     */
    public void addOrderDetail(OrderDetail orderDetail) {
    	this.orderDetails.add(orderDetail);
    	
    	setOrderTotal();
    }
    
    /**
     * Remove a detail line from the order
     * @param orderDetail
     * 			: the orderDetail to be removed
     * @throws InexistentObjectException
     */
    public void removeOrderDetail(OrderDetail orderDetail) 
    		throws InexistentObjectException {
    	
    	if (!orderDetails.remove(orderDetail)) {
    		throw new InexistentObjectException(
    				OrderDetail.class, "Order detail not found!");
    	}
    	
    	setOrderTotal();
    }
    
    /**
     * Set order details.
     * Deletes current order details and replaces 
     * them with new list.
     * @param orderDetails
     */
    public void setOrderDetails(List<OrderDetail> orderDetails) {
    	
    	// First, empty the list
    	this.orderDetails.clear();
    	
    	// Second, replace the current list
    	for (OrderDetail orderDetail : orderDetails) {
    		this.orderDetails.add(orderDetail);
    	}
    	
    	//TODO: Add this later
    	//setOrderTotal();
    }
    
    /**
     * Add order additional property detail.
     * @param orderDetailMenuItemAdditionalProperty
     * 			: order additional property detail to add
     */
    public void addOrderAdditionalPropertyDetail(
    		OrderDetailMenuItemAdditionalProperty 
    		orderDetailMenuItemAdditionalProperty) {
    	this.orderAdditionalPropertyDetails.add(
    			orderDetailMenuItemAdditionalProperty);
    	
    	// TODO: Add this later
    	setOrderTotal();
    }
    
    /**
     * Remove an additional property detail line from the order
     * @param orderAdditionalPropertyDetail
     * 			: the orderAdditionalPropertyDetail to be removed
     * @throws InexistentObjectException
     */
    public void removeOrderAdditionalPropertyDetail(
    		OrderDetailMenuItemAdditionalProperty orderAdditionalPropertyDetail) 
    		throws InexistentObjectException {
    	
    	if (!orderAdditionalPropertyDetails.remove(
    			orderAdditionalPropertyDetail)) {
    		throw new InexistentObjectException(
    				OrderDetailMenuItemAdditionalProperty.class, 
    				"Order Additional Property detail not found!");
    	}
    	
    	setOrderTotal();
    }
    
    /**
     * Set order additional property details.
     * Deletes current order additional property details and replaces 
     * them with new list.
     * @param orderAdditionalPropertyDetails
     */
    public void setOrderAdditionalPropertyDetails(
    		List<OrderDetailMenuItemAdditionalProperty> orderAdditionalPropertyDetails) {
    	
    	// First, empty the list
    	this.orderAdditionalPropertyDetails.clear();
    	
    	// Second, replace the current list
    	for (OrderDetailMenuItemAdditionalProperty orderAdditionalPropertyDetail : 
    			orderAdditionalPropertyDetails) {
    		this.orderAdditionalPropertyDetails.add(orderAdditionalPropertyDetail);
    	}
    	
    	//TODO: Add this later
    	//setOrderTotal();
    }
    
    /**
     * Add order set detail.
     * @param orderDetailSet
     * 			: order set detail to add
     */
    public void addOrderSetDetail(OrderDetailSet orderDetailSet) {
    	this.orderSetDetails.add(orderDetailSet);
    	
    	setOrderTotal();
    }
    
    /**
     * Remove a set detail line from the order
     * @param orderDetailSet
     * 			: the orderSetDetail to be removed
     * @throws InexistentObjectException 
     */
    public void removeOrderSetDetail(OrderDetailSet orderSetDetail) 
    		throws InexistentObjectException {
    	
    	if (!orderSetDetails.remove(orderSetDetail)) {
    		throw new InexistentObjectException(
    				OrderDetailSet.class, 
    				"Order set detail not found!");
    	}
    	
    	setOrderTotal();
    }
    
    /**
     * Set order set details.
     * Deletes current order set details and replaces 
     * them with new list.
     * @param orderDetails
     */
    public void setOrderSetDetails(List<OrderDetailSet> orderSetDetails) {
    	
    	// First, empty the list
    	this.orderSetDetails.clear();
    	
    	// Second, replace the current list
    	for (OrderDetailSet orderSetDetail : orderSetDetails) {
    		this.orderSetDetails.add(orderSetDetail);
    	}
    	
    	//TODO: Add this later
    	//setOrderTotal();
    }
}