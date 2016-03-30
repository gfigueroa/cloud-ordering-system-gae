/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;
import java.util.List;

import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.PostalAddress;


/**
 * This class represents a simple version of the Order table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class OrderSimple2 implements Serializable {

	public String key;
	public String status;
	public String restaurantNewsId;
	public String restaurantOpinionPollId;
    public String orderTime;
    public String branchKey;
    public String orderType;
    public String orderPaymentType;
    public String orderDeliveryType;
    public PostalAddress deliveryAddress;
    public Double orderDeliveryFee;
    public String timeToServe;
    public Integer numberOfPeople;
    public PhoneNumber contactPhone;
    public List<OrderDetailSimple> orderDetails;
    public List<OrderDetailMenuItemAdditionalPropertySimple> 
    		orderAdditionalPropertyDetails;
    public List<OrderDetailSetSimple> orderSetDetails;
    public Double totalPrice;
    public String orderComments;
    public String restaurantName; //TODO: Remove later
    public String branchName; //TODO: Remove later

    /**
     * MenuItemSimple constructor.
     * @param key
     * 			: order key
     * @param status
     * 			: order status
     * @param restaurantNewsId
     * 			: the id of the restaurant news
     * @param restaurantOpinionPollId
     * 			: the id of the opinion poll
     * @param orderTime
     * 			: the time the order was made
     * @param branchKey
     * 			: the key of the branch
     * @param orderType
     * 			: the order type
     * @param orderPaymentType
     * 			: the orderPaymentType
     * @param orderDeliveryType
     * 			: the order delivery type
     * @param deliveryAddress
     * 			: the order delivery address
     * @param orderDeliveryFee
     * 			: the order delivery fee
     * @param timeToServe
     * 			: the time to serve the order
     * @param numberOfPeople
     * 			: the number of people going to the restaurant
     * @param contactPhone
     * 			: the contact phone number
     * @param totalPrice
     * @param orderComments
     * 			: the comments for this order
     * @param orderDetails
     * 			: the order details
     * @param orderAdditionalPropertyDetails
     * 			: the order additional property details
     * @param orderSetDetails
     * 			: the order set details
     */
    public OrderSimple2(String key, String status, String restaurantNewsId,
    		String restaurantOpinionPollId, String orderTime,
    		String branchKey, String orderType, String orderPaymentType,
    		String orderDeliveryType, PostalAddress deliveryAddress, 
    		Double orderDeliveryFee, String timeToServe, Integer numberOfPeople, 
    		PhoneNumber contactPhone, Double totalPrice, String orderComments, 
    		String restaurantName, String branchName, 
    		List<OrderDetailSimple> orderDetails, 
    		List<OrderDetailMenuItemAdditionalPropertySimple> orderAdditionalPropertyDetails,
    		List<OrderDetailSetSimple> orderSetDetails) {
    	
    	this.key = key;
    	this.status = status;
    	this.restaurantNewsId = restaurantNewsId;
    	this.restaurantOpinionPollId = restaurantOpinionPollId;
    	this.orderTime = orderTime;
    	this.branchKey = branchKey;
    	this.orderType = orderType;
    	this.orderPaymentType = orderPaymentType;
    	this.orderDeliveryType = orderDeliveryType;
    	this.deliveryAddress = deliveryAddress;
    	this.orderDeliveryFee = orderDeliveryFee;
    	this.timeToServe = timeToServe;
    	this.numberOfPeople = numberOfPeople;
    	this.contactPhone = contactPhone;
    	this.totalPrice = totalPrice;
    	this.orderComments = orderComments;
    	this.restaurantName = restaurantName; //TODO: Remove later
    	this.branchName = branchName; //TODO: Remove later
    	this.orderDetails = orderDetails;
    	this.orderAdditionalPropertyDetails = orderAdditionalPropertyDetails;
    	this.orderSetDetails = orderSetDetails;
    }

    /**
     * Compare this order with another order
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this Order, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof OrderDetailSimple ) ) return false;
        OrderDetailSimple order = (OrderDetailSimple) o;
        return key.equals(order.key);
    }

}