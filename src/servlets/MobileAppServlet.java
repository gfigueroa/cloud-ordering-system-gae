/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.DateManager;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.PostalAddress;

import datastore.Branch;
import datastore.BranchManager;
import datastore.Customer;
import datastore.CustomerManager;
import datastore.CustomerRestaurantNews;
import datastore.CustomerRestaurantNewsManager;
import datastore.CustomerRestaurantOpinionPoll;
import datastore.CustomerRestaurantOpinionPollManager;
import datastore.CustomerSurvey;
import datastore.CustomerSurveyManager;
import datastore.CustomerSurveyOpinionPoll;
import datastore.CustomerSurveyOpinionPollManager;
import datastore.Order;
import datastore.OrderDetail;
import datastore.OrderDetailMenuItemAdditionalProperty;
import datastore.OrderDetailSet;
import datastore.OrderManager;
import datastore.RestaurantNews;
import datastore.RestaurantNewsManager;
import datastore.RestaurantOpinionPoll;
import datastore.RestaurantOpinionPollManager;
import datastore.Set;
import datastore.SmartCashTransactionManager;
import datastore.Survey;
import datastore.SurveyManager;
import datastore.SurveyOpinionPoll;
import datastore.SurveyOpinionPollManager;
import datastore.User;
import datastore.UserManager;
import exceptions.InvalidFieldFormatException;
import exceptions.MissingRequiredFieldsException;
import exceptions.ObjectExistsInDatastoreException;
import exceptions.UserValidationException;

/**
 * This servlet class is used to serve upload requests
 * from Mobile Apps, such as login information, registration information,
 * profile update and order information
 * 
 */

@SuppressWarnings("serial")
public class MobileAppServlet extends HttpServlet {

    private static final Logger log = 
        Logger.getLogger(MobileAppServlet.class.getName());
    
    // JSP file locations
    private static final String thisServlet = "/mobile";
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	
	    // Lets check the action required by the jsp
	    String status = req.getParameter("status");
	
	    if (status.equals("success")) {
	    	resp.getWriter().println("success");
	    }
	    else if (status.equals("failure")){
	    	resp.getWriter().println("failure");
	    }
	}

    public void doPost(HttpServletRequest req, HttpServletResponse resp) 
                throws IOException {
    	
        // Let's check the action required by the mobile app
        String action = req.getParameter("action");

        if (action.equals("register")) {
        	
            // User fields
            User.Type type = User.Type.CUSTOMER;
            User neoUser;
            Email userEmail = new Email(req.getParameter("u_email"));
            String userPassword = req.getParameter("u_password");

            // Customer fields
            String customerName = req.getParameter("c_name");
            PhoneNumber customerPhone = new PhoneNumber(req.getParameter("c_phone"));
            
            Customer.Gender gender =
                    req.getParameter("c_gender").equalsIgnoreCase("male") 
                    ? Customer.Gender.MALE : Customer.Gender.FEMALE;
            
            PostalAddress address = new PostalAddress(req.getParameter("c_address"));

            String customerComments = "";
            
            try {          
                neoUser = 
                    new User(userEmail,
                        userPassword,
                        type);
                Customer cust = new Customer(neoUser,
                        customerName,
                        customerPhone,
                        gender,
                        address,
                        customerComments);
                CustomerManager.putCustomer(cust);
                
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.sendRedirect(thisServlet + "?status=success");
            } 
            catch (MissingRequiredFieldsException mrfe) {
                resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                		"One or more required fields are missing.");
                return;
            } 
            catch (InvalidFieldFormatException iffe) {
            	resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, 
            			"One or more fields have an invalid format.");
                return;
            } 
            catch (ObjectExistsInDatastoreException oede) {
            	resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, 
            			"This user email has already been registered in the system.");
                return;
            } 
            catch (Exception ex) {
                log.log(Level.SEVERE, ex.toString());
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                		"Internal server error.");
                return;
            }
        }
        else if (action.equals("modify_profile")) {
        	
        	// User fields
            Email userEmail = new Email(req.getParameter("u_email"));

            // Customer fields
            String customerName = req.getParameter("c_name");
            PhoneNumber customerPhone = new PhoneNumber(req.getParameter("c_phone"));
            
            Customer.Gender gender =
                    req.getParameter("c_gender").equalsIgnoreCase("male") 
                    ? Customer.Gender.MALE : Customer.Gender.FEMALE;
            
            PostalAddress address = new PostalAddress(req.getParameter("c_address"));

            String customerComments = "";
            
        	try {

                CustomerManager.updateCustomerAttributes(
                        userEmail,
                        customerName,
                        customerPhone,
                        gender,
                        address,
                        customerComments);

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.sendRedirect(thisServlet + "?status=success");
            } 
        	catch (MissingRequiredFieldsException mrfe) {
                resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                		"One or more required fields are missing.");
                return;
            } 
            catch (InvalidFieldFormatException iffe) {
            	resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, 
            			"One or more fields have an invalid format.");
                return;
            }  
            catch (Exception ex) {
                log.log(Level.SEVERE, ex.toString());
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                		"Internal server error.");
                return;
            }
        }
        else if (action.equals("modify_password")) {
        	
        	// User fields
            Email userEmail = new Email(req.getParameter("u_email"));
            String currentPassword = req.getParameter("curr_pass");
            String newPassword = req.getParameter("new_pass");
            
        	try {
                CustomerManager.updateCustomerPassword(
                		userEmail, 
                		currentPassword, 
                		newPassword);

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.sendRedirect(thisServlet + "?status=success");
            }
        	catch (MissingRequiredFieldsException mrfe) {
                resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                		"One or more required fields are missing.");
                return;
            }
        	catch (UserValidationException uve) {
                resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                		"User e-mail and password don't match.");
                return;
            }
            catch (Exception ex) {
                log.log(Level.SEVERE, ex.toString());
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                		"Internal server error.");
                return;
            }
        }
        else if (action.equals("login")) {
        	
        	// User fields
            Email userEmail = new Email(req.getParameter("u_email"));
            String userPassword = req.getParameter("u_password");
            
        	try {
                Customer customer = CustomerManager.getCustomer(userEmail);
                if (customer.getUser().validateUser(userEmail, userPassword)) {
                	resp.setStatus(HttpServletResponse.SC_CREATED);
                    resp.sendRedirect(thisServlet + "?status=success");
                }
                else {
                	resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                    		"User e-mail and password don't match.");
                    return;
                }
                
            }
            catch (Exception ex) {
                log.log(Level.SEVERE, ex.toString());
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                		"Internal server error.");
                return;
            }
        }
        else if (action.equals("upload_order")) {
        	
        	// User fields
            Email userEmail = new Email(req.getParameter("u_email"));
            Customer customer = CustomerManager.getCustomer(userEmail);
            Key customerKey = customer.getKey();
            
            Key branchKey = KeyFactory.stringToKey(req.getParameter("b_key"));
            
            Order.OrderType orderType = Order.getOrderTypeFromString(req.getParameter("o_type"));
            Order.OrderPaymentType orderPaymentType = req.getParameter("o_p_type") != null ?
            		Order.getOrderPaymentTypeFromString(req.getParameter("o_p_type")) :
            		Order.OrderPaymentType.CASH;		
            Order.OrderDeliveryType orderDeliveryType = 
            		Order.getOrderDeliveryTypeFromString(req.getParameter("o_delivery_type"));
            
            // If payment type is Smart Cash, check customer's balance
            if (orderPaymentType == Order.OrderPaymentType.SMART_CASH) {
            	double customerBalance = 
            			SmartCashTransactionManager.getSmartCashBalanceFromCustomer(customerKey);
            	if (customerBalance <= 0) {
            		resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                    		"Customer smart cash balance is negative or 0, order is invalid.");
                    return;
            	}
            }
            
            PhoneNumber orderPhone = new PhoneNumber(req.getParameter("o_phone"));
            GeoPt customerLocation = null;
            PostalAddress address = new PostalAddress(req.getParameter("d_address"));
            Double orderDeliveryFee = 0.0;
    		
    		String timeToServeString = req.getParameter("s_time");
    		Date timeToServe = null;
    		if (!timeToServeString.isEmpty()) {	
    			timeToServe = DateManager.getDateValue(timeToServeString);
    			log.info("TIME STRING: " + timeToServeString);
        		log.info("TIME TO SERVE: " + DateManager.printDateAsString(timeToServe));
    		}
    		
    		String numberOfPeopleString = req.getParameter("n_people");
    		Integer numberOfPeople = null;
    		if (!numberOfPeopleString.isEmpty()) {
    			numberOfPeople = Integer.parseInt(numberOfPeopleString);
    		}
    		
            // Order details
            // Order detail format: menuItemKey,quantity;menuItemKey,quantity;...
            String orderDetailsString = req.getParameter("o_detail") != null ? 
            		req.getParameter("o_detail") : "";
            
            // Order additional property details
            // Order additional property detail format: 
            // menuItemAdditionalPropertyValueKey1,menuItemAdditionalPropertyValueKey2,...,quantity;
            // ...menuItemAdditionalPropertyValueKey3,menuItemAdditionalPropertyValueKey4,quantity;...
            String orderAdditionalPropertyDetailsString = req.getParameter("o_ap_detail") != null ?
            		req.getParameter("o_ap_detail") : "";
            
            // Order set details
            // Order set detail format:
            // For fixed sets:
            // f,setKey,quantity;f,setKey,quantity;...
            // For type sets with fixed price:
            // t,setKey1,typeSetMenuItem1,typeSetMenuItem2,quantity;t,setKey2,typeSetMenuItem3,quantity;...
            String orderSetDetailsString = req.getParameter("o_s_detail") != null ?
            		req.getParameter("o_s_detail") : "";
            
            try {       	
                Order order = new Order(
                				  customerKey, 
                                  branchKey,
                                  null,
                                  null,
                                  orderType,
                                  orderPaymentType,
                                  orderDeliveryType,
                                  orderPhone,
                                  customerLocation,
                                  address,
                                  orderDeliveryFee,
                                  timeToServe,
                                  numberOfPeople,
                                  "本系統目前所有功能皆處於測試階段，" +
                                		  "訂單在正式版於2012 7月釋出以前並不會被處理，" +
                                		  "感謝您的使用。");
                
                // Now add the details
                if (!orderDetailsString.isEmpty()) {
	                String[] orderDetailTokens = orderDetailsString.split(";");
	                for (String orderDetailToken : orderDetailTokens) {
	                	String[] detailTokens = orderDetailToken.split(",");
	                	String menuItemKeyString = detailTokens[0];
	                	String quantityString = detailTokens[1];
	                	
	                	Key menuItemKey = KeyFactory.stringToKey(menuItemKeyString);
	                	int quantity = Integer.parseInt(quantityString);
	                	OrderDetail orderDetail = new OrderDetail(
	                			menuItemKey, 
	                			quantity, 
	                			"");
	                	order.addOrderDetail(orderDetail);
	                }
                }
                
                // Now add the additional property details
                if (!orderAdditionalPropertyDetailsString.isEmpty()) {
                	String[] orderDetailTokens = 
                			orderAdditionalPropertyDetailsString.split(";");
                	for (String orderDetailToken : orderDetailTokens) {
                		String[] detailTokens = orderDetailToken.split(",");

                		// Parse the menuItemAdditionalPropertyValue keys
                		String[] menuItemAdditionalPropertyValueKeyStrings = 
                				Arrays.copyOfRange(detailTokens, 0, detailTokens.length - 1);
                		
                		ArrayList<Key> menuItemAdditionalPropertyValueKeys =
                				new ArrayList<Key>();
                		for (String menuItemAdditionalPropertyValueKeyString : 
                			menuItemAdditionalPropertyValueKeyStrings) {
                			
                			menuItemAdditionalPropertyValueKeys.add(
                					KeyFactory.stringToKey(
                							menuItemAdditionalPropertyValueKeyString));
                		}
                		
                		String quantityString = detailTokens[detailTokens.length - 1];
                		int quantity = Integer.parseInt(quantityString);
                		
                		OrderDetailMenuItemAdditionalProperty orderAdditionalPropertyDetail =
                				new OrderDetailMenuItemAdditionalProperty(
                						menuItemAdditionalPropertyValueKeys, 
                		        		quantity,
                		        		"");
                		order.addOrderAdditionalPropertyDetail(orderAdditionalPropertyDetail);
                	}
                }   
                
                // Now add the set details
                if (!orderSetDetailsString.isEmpty()) {
                	String[] orderDetailTokens =
                			orderSetDetailsString.split(";");
                	
                	for (String orderDetailToken : orderDetailTokens) {
                		String[] detailTokens = orderDetailToken.split(",");
                		
                		Set.SetType setType = detailTokens[0].equalsIgnoreCase("f") ? 
                				Set.SetType.FIXED_SET : Set.SetType.TYPE_SET;
                		
                		// Fixed Set
                		if (setType == Set.SetType.FIXED_SET) {
                			Key setKey = KeyFactory.stringToKey(detailTokens[1]);
                			int quantity = Integer.parseInt(detailTokens[2]);
                			
                			OrderDetailSet orderDetailSet = new OrderDetailSet(
                					setKey,
                					null,
                					quantity,
                					""
                					);
                			order.addOrderSetDetail(orderDetailSet);
                		}
                		else if (setType == Set.SetType.TYPE_SET) {
                			Key setKey = KeyFactory.stringToKey(detailTokens[1]);
                			
                			// Parse type set menu items
                			ArrayList<Key> typeSetMenuItems = new ArrayList<Key>();
                			for (int i = 2; i < detailTokens.length - 1; i ++) {
                				Key typeSetMenuItemKey = 
                						KeyFactory.stringToKey(detailTokens[i]);
                				typeSetMenuItems.add(typeSetMenuItemKey);
                			}
                			
                			int quantity = Integer.parseInt(
                					detailTokens[detailTokens.length - 1]);
                			
                			OrderDetailSet orderDetailSet = new OrderDetailSet(
                					setKey,
                					typeSetMenuItems,
                					quantity,
                					""
                					);
                			order.addOrderSetDetail(orderDetailSet);
                		}
                	}
                }
                
                OrderManager.putOrder(order);
                
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.sendRedirect(thisServlet + "?status=success");
            }
        	catch (MissingRequiredFieldsException mrfe) {
                resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                		"One or more required fields are missing.");
                return;
            } 
            catch (InvalidFieldFormatException iffe) {
            	resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, 
            			"One or more fields have an invalid format.");
                return;
            }  
            catch (Exception ex) {
                log.log(Level.SEVERE, ex.toString());
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                		"Internal server error.");
                return;
            }
        }
        else if (action.equals("modify_order")) {
        	
        	// User fields
            Email userEmail = new Email(req.getParameter("u_email"));
            String userPassword = req.getParameter("u_password");
            
            // Check if user and password are correct
            Customer customer = CustomerManager.getCustomer(userEmail);
            if (!customer.getUser().validateUser(userEmail, userPassword)) {
                resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                    	"User e-mail and password don't match.");
                return;
            }
            
            Long orderKey = Long.parseLong(req.getParameter("o_key"));

            // Check if user actually made this order
            Order order = OrderManager.getOrder(orderKey);
            Key customerKey = KeyFactory.createKey(Customer.class.getSimpleName(),
            		userEmail.getEmail());
            if (!order.getCustomer().equals(customerKey)) {
            	resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                		"Unauthorized request.");
                return;
            }
            
            // Check if order is still open
            if (!order.isStillOpen()) {
            	resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                		"Order is no longer open.");
                return;
            }
            
            Order.OrderType orderType = Order.getOrderTypeFromString(req.getParameter("o_type"));
            Order.OrderPaymentType orderPaymentType = req.getParameter("o_p_type") != null ?
            		Order.getOrderPaymentTypeFromString(req.getParameter("o_p_type")) :
            		order.getOrderPaymentType();
            Order.OrderDeliveryType orderDeliveryType = 
            		Order.getOrderDeliveryTypeFromString(req.getParameter("o_delivery_type"));
            
            // If payment type is Smart Cash, check customer's balance
            if (orderPaymentType == Order.OrderPaymentType.SMART_CASH) {
            	double customerBalance = 
            			SmartCashTransactionManager.getSmartCashBalanceFromCustomer(customerKey);
            	if (customerBalance <= 0) {
            		resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                    		"Customer smart cash balance is negative or 0, order is invalid.");
                    return;
            	}
            }
            
            PhoneNumber orderPhone = new PhoneNumber(req.getParameter("o_phone"));
            GeoPt customerLocation = null;
            PostalAddress address = new PostalAddress(req.getParameter("d_address"));
    		
    		String timeToServeString = req.getParameter("s_time");
    		Date timeToServe = null;
    		if (!timeToServeString.isEmpty()) {	
    			timeToServe = DateManager.getDateValue(timeToServeString);
    			log.info("TIME STRING: " + timeToServeString);
        		log.info("TIME TO SERVE: " + DateManager.printDateAsString(timeToServe));
    		}
    		
    		String numberOfPeopleString = req.getParameter("n_people");
    		Integer numberOfPeople = null;
    		if (!numberOfPeopleString.isEmpty()) {
    			numberOfPeople = Integer.parseInt(numberOfPeopleString);
    		}
    		
            // Order details
            // Order detail format: menuItemKey,quantity;menuItemKey,quantity;...
            String orderDetailsString = req.getParameter("o_detail") != null ? 
            		req.getParameter("o_detail") : "";
            
            // Order additional property details
            // Order additional property detail format: 
            // menuItemAdditionalPropertyValueKey1,menuItemAdditionalPropertyValueKey2,...,quantity;
            // ...menuItemAdditionalPropertyValueKey3,menuItemAdditionalPropertyValueKey4,quantity;...
            String orderAdditionalPropertyDetailsString = req.getParameter("o_ap_detail") != null ?
            		req.getParameter("o_ap_detail") : "";
            
            // Order set details
            // Order set detail format:
            // For fixed sets:
            // f,setKey,quantity;f,setKey,quantity;...
            // For type sets with fixed price:
            // t,setKey1,typeSetMenuItem1,typeSetMenuItem2,quantity;t,setKey2,typeSetMenuItem3,quantity;...
            String orderSetDetailsString = req.getParameter("o_s_detail") != null ?
            		req.getParameter("o_s_detail") : "";
            
            try {       	   
                // Now add the details
            	ArrayList<OrderDetail> orderDetails = 
            			new ArrayList<OrderDetail>();
                if (!orderDetailsString.isEmpty()) {
	                String[] orderDetailTokens = orderDetailsString.split(";");
	                for (String orderDetailToken : orderDetailTokens) {
	                	String[] detailTokens = orderDetailToken.split(",");
	                	String menuItemKeyString = detailTokens[0];
	                	String quantityString = detailTokens[1];
	                	
	                	Key menuItemKey = KeyFactory.stringToKey(menuItemKeyString);
	                	int quantity = Integer.parseInt(quantityString);
	                	OrderDetail orderDetail = new OrderDetail(
	                			menuItemKey, 
	                			quantity, 
	                			"");
	                	orderDetails.add(orderDetail);
	                }
                }
                
                // Now add the additional property details
                ArrayList<OrderDetailMenuItemAdditionalProperty> orderAdditionalPropertyDetails = 
            			new ArrayList<OrderDetailMenuItemAdditionalProperty>();
                if (!orderAdditionalPropertyDetailsString.isEmpty()) {
                	String[] orderDetailTokens = 
                			orderAdditionalPropertyDetailsString.split(";");
                	for (String orderDetailToken : orderDetailTokens) {
                		String[] detailTokens = orderDetailToken.split(",");

                		// Parse the menuItemAdditionalPropertyValue keys
                		String[] menuItemAdditionalPropertyValueKeyStrings = 
                				Arrays.copyOfRange(detailTokens, 0, detailTokens.length - 1);
                		
                		ArrayList<Key> menuItemAdditionalPropertyValueKeys =
                				new ArrayList<Key>();
                		for (String menuItemAdditionalPropertyValueKeyString : 
                			menuItemAdditionalPropertyValueKeyStrings) {
                			
                			menuItemAdditionalPropertyValueKeys.add(
                					KeyFactory.stringToKey(
                							menuItemAdditionalPropertyValueKeyString));
                		}
                		
                		String quantityString = detailTokens[detailTokens.length - 1];
                		int quantity = Integer.parseInt(quantityString);
                		
                		OrderDetailMenuItemAdditionalProperty orderAdditionalPropertyDetail =
                				new OrderDetailMenuItemAdditionalProperty(
                						menuItemAdditionalPropertyValueKeys, 
                		        		quantity,
                		        		"");
                		orderAdditionalPropertyDetails.add(orderAdditionalPropertyDetail);
                	}
                }
                
                // Now add the set details
                ArrayList<OrderDetailSet> orderSetDetails = 
            			new ArrayList<OrderDetailSet>();
                if (!orderSetDetailsString.isEmpty()) {
                	String[] orderDetailTokens =
                			orderSetDetailsString.split(";");
                	
                	for (String orderDetailToken : orderDetailTokens) {
                		String[] detailTokens = orderDetailToken.split(",");
                		
                		Set.SetType setType = detailTokens[0].equalsIgnoreCase("f") ? 
                				Set.SetType.FIXED_SET : Set.SetType.TYPE_SET;
                		
                		// Fixed Set
                		if (setType == Set.SetType.FIXED_SET) {
                			Key setKey = KeyFactory.stringToKey(detailTokens[1]);
                			int quantity = Integer.parseInt(detailTokens[2]);
                			
                			OrderDetailSet orderDetailSet = new OrderDetailSet(
                					setKey,
                					null,
                					quantity,
                					""
                					);
                			orderSetDetails.add(orderDetailSet);
                		}
                		else if (setType == Set.SetType.TYPE_SET) {
                			Key setKey = KeyFactory.stringToKey(detailTokens[1]);
                			
                			// Parse type set menu items
                			ArrayList<Key> typeSetMenuItems = new ArrayList<Key>();
                			for (int i = 2; i < detailTokens.length - 1; i ++) {
                				Key typeSetMenuItemKey = 
                						KeyFactory.stringToKey(detailTokens[i]);
                				typeSetMenuItems.add(typeSetMenuItemKey);
                			}
                			
                			int quantity = Integer.parseInt(
                					detailTokens[detailTokens.length - 1]);
                			
                			OrderDetailSet orderDetailSet = new OrderDetailSet(
                					setKey,
                					typeSetMenuItems,
                					quantity,
                					""
                					);
                			orderSetDetails.add(orderDetailSet);
                		}
                	}
                }
                
                OrderManager.updateOrderAttributesByCustomer(
                		orderKey, 
                		orderType, 
                		orderPaymentType, 
                		orderDeliveryType, 
                		orderPhone, 
                		customerLocation, 
                		address, 
                		timeToServe, 
                		numberOfPeople, 
                		orderDetails, 
                		orderAdditionalPropertyDetails, 
                		orderSetDetails);
                
                OrderManager.refreshOrderTotal(orderKey);
                
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.sendRedirect(thisServlet + "?status=success");
            }
        	catch (MissingRequiredFieldsException mrfe) {
                resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                		"One or more required fields are missing.");
                return;
            }
            catch (InvalidFieldFormatException iffe) {
            	resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, 
            			"One or more fields have an invalid format.");
                return;
            }
            catch (Exception ex) {
                log.log(Level.SEVERE, ex.toString());
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                		"Internal server error.");
                return;
            }
        }
        else if (action.equals("cancel_order")) {
        	
        	// User fields
            Email userEmail = new Email(req.getParameter("u_email"));
            String userPassword = req.getParameter("u_password");
            
            // Check if user and password are correct
            Customer customer = CustomerManager.getCustomer(userEmail);
            if (!customer.getUser().validateUser(userEmail, userPassword)) {
                resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                    	"User e-mail and password don't match.");
                return;
            }
            
            Long orderKey = Long.parseLong(req.getParameter("o_key"));

            // Check if user actually made this order
            Order order = OrderManager.getOrder(orderKey);
            Key customerKey = KeyFactory.createKey(Customer.class.getSimpleName(),
            		userEmail.getEmail());
            if (!order.getCustomer().equals(customerKey)) {
            	resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                		"Unauthorized request.");
                return;
            }
            
            String cancellationComments = req.getParameter("c_comments");
            
            try {       	
                OrderManager.cancelOrder(orderKey, cancellationComments);
                
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.sendRedirect(thisServlet + "?status=success");
            }
            catch (Exception ex) {
                log.log(Level.SEVERE, ex.toString());
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                		"Internal server error.");
                return;
            }
        }
        else if (action.equals("click_news")) {
        	
        	// User fields
            Email userEmail = new Email(req.getParameter("u_email"));
            String password = req.getParameter("u_password");
            
            // Check email and password
            User user = UserManager.getUser(userEmail.getEmail(), password);
            if (user == null) {
            	resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                		"User e-mail and password don't match.");
                return;
            }

            Key restaurantNewsKey = KeyFactory.stringToKey(req.getParameter("n_key"));
            
            Key restaurantKey = restaurantNewsKey.getParent();
            List<Branch> branches = BranchManager.getRestaurantBranches(restaurantKey);
            if (branches.isEmpty()) {
            	log.log(Level.SEVERE, "This restaurant has no available branches.");
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                		"Internal server error.");
                return;
            }
            Key branchKey = branches.get(0).getKey(); // Get first available branch
            
            Order.OrderType orderType = Order.OrderType.NEWS;
            Order.OrderPaymentType orderPaymentType = Order.OrderPaymentType.NO_PAYMENT;
            
            PhoneNumber orderPhone = new PhoneNumber(req.getParameter("o_phone"));
            GeoPt customerLocation = null;
    		
    		Date timeToServe = new Date(); // Now
    		
    		// Check if user already clicked on this news
            // TODO: To add later
    		//if (RestaurantNewsManager.customerAlreadyClickedNews(user.getKey().getParent(), restaurantNewsKey)) {
            //	resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
            //    		"User has already clicked on this news once.");
            //    return;
            //}
            
            try {
            	
            	CustomerRestaurantNews crn = 
            			new CustomerRestaurantNews(user.getKey().getParent(), restaurantNewsKey);
            	CustomerRestaurantNewsManager.putCustomerRestaurantNews(crn);
            	
            	// Check if the news hasn't been maxed out
            	if (RestaurantNewsManager.addRestaurantNewsClick(restaurantNewsKey)) {
            		RestaurantNews news = RestaurantNewsManager.getRestaurantNews(restaurantNewsKey);
            		
	                Order order = new Order(user.getKey().getParent(), 
	                                  branchKey,
	                                  restaurantNewsKey,
	                                  null,
	                                  orderType,
	                                  orderPaymentType,
	                                  null,
	                                  orderPhone,
	                                  customerLocation,
	                                  null,
	                                  null,
	                                  timeToServe,
	                                  null,
	                                  //您是第13位回應此新聞。共19位已回應。
	                                  "您是第 " + news.getCurrentClicks() + " 位回應此新聞。\n");
	                OrderManager.putOrder(order);
	                
	                resp.setStatus(HttpServletResponse.SC_OK);
	                resp.sendRedirect(thisServlet + "?status=success");
            	}
            	else {
            		resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                    		"This news has reached the maximum number of clicks.");
            		return;
            	}
            }
        	catch (MissingRequiredFieldsException mrfe) {
                resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                		"One or more required fields are missing.");
                return;
            }
            catch (InvalidFieldFormatException iffe) {
            	resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, 
            			"One or more fields have an invalid format.");
                return;
            }
            catch (Exception ex) {
                log.log(Level.SEVERE, ex.toString());
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                		"Internal server error.");
                return;
            }
        }
        else if (action.equals("opinion_poll")) {
        	
        	// User fields
            Email userEmail = new Email(req.getParameter("u_email"));
            String password = req.getParameter("u_password");
            
            // Check email and password
            User user = UserManager.getUser(userEmail.getEmail(), password);
            if (user == null) {
            	resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                		"User e-mail and password don't match.");
                return;
            }
            
            Customer customer = CustomerManager.getCustomer(user);

            Key restaurantOpinionPollKey = KeyFactory.stringToKey(req.getParameter("op_key"));
            
            // Check if user already clicked on this opinion poll
            // TODO: To add later
            //if (RestaurantOpinionPollManager.customerAlreadyClickedOpinionPoll(user.getKey().getParent(), restaurantOpinionPollKey)) {
            //	resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
            //    		"User has already clicked on this opinion poll once.");
            //    return;
            //}
            
            String answer = req.getParameter("answer");
            
            Key restaurantKey = restaurantOpinionPollKey.getParent();
            List<Branch> branches = BranchManager.getRestaurantBranches(restaurantKey);
            if (branches.isEmpty()) {
            	log.log(Level.SEVERE, "This restaurant has no available branches.");
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                		"Internal server error.");
                return;
            }
            Key branchKey = branches.get(0).getKey(); // Get first available branch
            
            Order.OrderType orderType = Order.OrderType.OPINION;
            Order.OrderPaymentType orderPaymentType = Order.OrderPaymentType.NO_PAYMENT;
            
            PhoneNumber orderContactPhone = customer.getCustomerPhone();
            GeoPt customerLocation = null;
            Date timeToServe = new Date();
            
            try {
            	
            	RestaurantOpinionPollManager.addRestaurantOpinionPollClick(restaurantOpinionPollKey);
            	
            	RestaurantOpinionPoll opinion = 
            			RestaurantOpinionPollManager.getRestaurantOpinionPoll(restaurantOpinionPollKey);
            	
            	CustomerRestaurantOpinionPoll customerOpinion = 
            			new CustomerRestaurantOpinionPoll(
            					customer.getKey(), 
            					opinion.getKey(), 
            					answer, 
            					opinion.getCurrentClicks());
            	CustomerRestaurantOpinionPollManager.putCustomerRestaurantOpinionPoll(customerOpinion);
            	
                Order order = new Order(user.getKey().getParent(), 
                                  branchKey,
                                  null,
                                  restaurantOpinionPollKey,
                                  orderType,
                                  orderPaymentType,
                                  null,
                                  orderContactPhone,
                                  customerLocation,
                                  null,
                                  null,
                                  timeToServe,
                                  null,
                                  "您是第 " + opinion.getCurrentClicks() + " 位回應此新聞。\n");
                OrderManager.putOrder(order);
                
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.sendRedirect(thisServlet + "?status=success");
            }
        	catch (MissingRequiredFieldsException mrfe) {
                resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                		"One or more required fields are missing.");
                return;
            }
            catch (Exception ex) {
                log.log(Level.SEVERE, ex.toString());
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                		"Internal server error.");
                return;
            }
        }
        else if (action.equals("survey")) {
        	
        	// User fields
            Email userEmail = new Email(req.getParameter("u_email"));
            String password = req.getParameter("u_password");
            
            // Check email and password
            User user = UserManager.getUser(userEmail.getEmail(), password);
            if (user == null) {
            	resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                		"User e-mail and password don't match.");
                return;
            }
            
            Customer customer = CustomerManager.getCustomer(user);

            Key surveyKey = KeyFactory.stringToKey(req.getParameter("s_key"));
            
            // Check if user already responded this survey
            // TODO: To add later
            //if (SurveyManager.customerAlreadyClickedSurvey(customer.getKey(), surveyKey))
            //	resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
            //    		"User has already responded to this survey.");
            //    return;
            //}
            
            String answerString = req.getParameter("answers");
            String[] answers = answerString.split(";");
            
            try {
            	SurveyManager.addSurveyClick(surveyKey);
            	
            	Survey survey = SurveyManager.getSurvey(surveyKey);
            	
            	CustomerSurvey customerSurvey = 
            			new CustomerSurvey(customer.getKey(), surveyKey, survey.getCurrentClicks());
            	CustomerSurveyManager.putCustomerSurvey(customerSurvey);
            	
            	// Answers for each opinion poll
            	for (String answer : answers) {
            		String opinionPollKeyString = answer.substring(0, answer.indexOf(","));
            		Key opinionPollKey = KeyFactory.stringToKey(opinionPollKeyString);
            		
            		String restOfAnswer = answer.substring(answer.indexOf(",") + 1);
            		
            		SurveyOpinionPollManager.addSurveyOpinionPollClick(opinionPollKey);
                	
                	SurveyOpinionPoll opinion = 
                			SurveyOpinionPollManager.getSurveyOpinionPoll(opinionPollKey);
                	
                	CustomerSurveyOpinionPoll customerOpinionPoll = 
                			new CustomerSurveyOpinionPoll(
                					customer.getKey(), 
                					opinion.getKey(), 
                					restOfAnswer, 
                					opinion.getCurrentClicks());
                	CustomerSurveyOpinionPollManager.putCustomerSurveyOpinionPoll(customerOpinionPoll);
            	}
                
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.sendRedirect(thisServlet + "?status=success");
            }
        	catch (MissingRequiredFieldsException mrfe) {
                resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                		"One or more required fields are missing.");
                return;
            }
            catch (Exception ex) {
                log.log(Level.SEVERE, ex.toString());
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                		"Internal server error.");
                return;
            }
        }
    }
}
