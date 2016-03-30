/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package servlets;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.BlobUtils;
import util.DateManager;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.PostalAddress;

import datastore.Administrator;
import datastore.AdministratorManager;
import datastore.Customer;
import datastore.CustomerManager;
import datastore.Restaurant;
import datastore.RestaurantManager;
import datastore.RestaurantType;
import datastore.RestaurantTypeManager;
import datastore.SmartCashTransaction;
import datastore.SmartCashTransactionManager;
import datastore.SystemManager;
import datastore.User;
import exceptions.InvalidFieldFormatException;
import exceptions.MissingRequiredFieldsException;
import exceptions.ObjectExistsInDatastoreException;

/**
 * This servlet class is used to add, delete and update
 * users (Admin, Restaurant or Customer) in the system.
 * 
 */

@SuppressWarnings("serial")
public class ManageUserServlet extends HttpServlet {

    private static final Logger log = 
        Logger.getLogger(ManageUserServlet.class.getName());
    
    private static final BlobstoreService blobstoreService = 
    	BlobstoreServiceFactory.getBlobstoreService();
    
    // JSP file locations
    private static final String addAdminJSP = "/admin/addAdmin.jsp";
    private static final String addCustomerJSP = "/admin/addCustomer.jsp";
    private static final String addCustomerSmartCashJSP = 
    		"/admin/addSmartCash.jsp";
    private static final String addRestaurantJSP = "/admin/addRestaurant.jsp";
    private static final String autoCustomerJSP = "/customer/autoCustomer.jsp";
    private static final String editAdminJSP = "/admin/editAdmin.jsp";
    private static final String editAdminPasswordJSP = "/admin/editAdminPassword.jsp";
    private static final String editCustomerJSPAdmin = "/admin/editCustomer.jsp";
    private static final String editCustomerJSPCustomer = 
    		"/customer/editCustomer.jsp";
    private static final String editCustomerPasswordJSPAdmin = 
    		"/admin/editCustomerPassword.jsp";
    private static final String editCustomerPasswordJSPCustomer = 
    		"/customer/editCustomerPassword.jsp";
    private static final String editCustomerSmartCashJSP = "/admin/editSmartCash.jsp";
    private static final String editRestaurantJSPAdmin = "/admin/editRestaurant.jsp";
    private static final String editRestaurantJSPRestaurant = 
    		"/restaurant/editRestaurant.jsp";
    private static final String editRestaurantPasswordJSP = 
    		"/restaurant/editRestaurantPassword.jsp";
    private static final String listAdminJSP = "/admin/listAdmin.jsp";
    private static final String listCustomerJSP = "/admin/listCustomer.jsp";
    private static final String listRestaurantJSP = "/admin/listRestaurant.jsp";  
    private static final String loginJSP = "/login.jsp";

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
                throws IOException {
    	
    	HttpSession session = req.getSession(true);
        User user = (User) session.getAttribute("user");
        
        // Check that an administrator is carrying out the action
	    if (user == null || user.getUserType() != User.Type.ADMINISTRATOR) {
	    	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	        return;
	    }
    	
        // Lets check the action required by the jsp
        String action = req.getParameter("action");

        if (action.equals("delete")) {
        	
            // retrieve the key     
        	String keyString = req.getParameter("k");
      		Key key = KeyFactory.stringToKey(keyString);

            // deleting a user
            switch(req.getParameter("u_type").charAt(0)){
                case 'A':
                    Administrator admin = 
                        AdministratorManager.getAdministrator(key);
                    AdministratorManager.deleteAdministrator(admin);
                    resp.sendRedirect(listAdminJSP + "?msg=success&action=" + action);
                    return;
                case 'R':
                    Restaurant restaurant = RestaurantManager.getRestaurant(key);
                    SystemManager.updateRestaurantListVersion();
                    SystemManager.updateStoreListVersion(restaurant);
                    RestaurantTypeManager.updateStoreTypeVersion(restaurant.getRestaurantType());
                    RestaurantManager.deleteRestaurant(restaurant);
                    resp.sendRedirect(listRestaurantJSP + "?msg=success&action=" + action);
                    break;
                case 'C':
                    Customer customer = CustomerManager.getCustomer(key);
                    CustomerManager.deleteCustomer(customer);
                    resp.sendRedirect(listCustomerJSP + "?msg=success&action=" + action);
                    break;
                default:
                    assert(false); // there is no other type
            }
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) 
                throws IOException {
    	
    	// Get the current session
    	HttpSession session = req.getSession(true);
    	User user = (User) session.getAttribute("user");
    	
        // Lets check the action required by the jsp
        String action = req.getParameter("action");

        if (action.equals("add")) {
        	
            // User fields
            User.Type type;
            char typeChar = req.getParameter("u_type").charAt(0);
            User neoUser;
            Email userEmail = new Email(req.getParameter("u_email"));
            String userPassword = req.getParameter("u_password");
            
            switch(typeChar){
                case 'A':
                	
                    // Bypass security
                    if (req.getParameter("bypass") != null && req.getParameter("bypass").equals("true")) {
                    	log.info("Security bypassed");
                    }
                    else {
	                	// Check that an administrator is carrying out the action
	                    if (user == null || user.getUserType() != User.Type.ADMINISTRATOR) {
	                    	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	                        return;
	                    }
                    }
                	
                    type = User.Type.ADMINISTRATOR;
                    String administratorName = req.getParameter("a_name");
                    String administratorComments = req.getParameter("a_comments");
                           
                    try {                
                        neoUser = new User(userEmail,
                        		userPassword,
                                type);
                        Administrator admin = new Administrator(neoUser,
                                administratorName,
                                administratorComments);
                        AdministratorManager.putAdministrator(admin);
                        
                        resp.sendRedirect(listAdminJSP + "?msg=success&action=" + action);
                    }
                    catch (MissingRequiredFieldsException mrfe) {
                        resp.sendRedirect(addAdminJSP  + "?etype=MissingInfo");
                        return;
                    }
                    catch (InvalidFieldFormatException iffe) {
                        resp.sendRedirect(addAdminJSP + "?etype=EmailFormat");
                        return;
                    }
                    catch (ObjectExistsInDatastoreException oede) {
                        resp.sendRedirect(addAdminJSP + "?etype=AlreadyExists");
                        return;
                    }
                    catch (Exception ex) {
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }

                    break;
                case 'R':
                	
                	// Check that an administrator is carrying out the action
                    if (user == null || user.getUserType() != User.Type.ADMINISTRATOR) {
                    	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                	
                    type = User.Type.RESTAURANT;
                    
                    String restaurantTypeString = req.getParameter("r_type");
            		Long restaurantType = null;
            		RestaurantType.StoreSuperType storeSuperType = null;
            		if (!restaurantTypeString.isEmpty()) {
            			restaurantType = Long.parseLong(restaurantTypeString);
            			RestaurantType storeType = RestaurantTypeManager.getRestaurantType(restaurantType);
            			storeSuperType = storeType.getStoreSuperType();
            		}
            		
            		Boolean hasNewsService = true; // All stores have news service
            		Boolean hasProductsService = req.getParameter("has_ps") != null ? true : false;
            		Boolean hasServiceProvidersService = req.getParameter("has_sps") != null ? true : false;
            		Boolean hasMessagesService = req.getParameter("has_ms") != null ? true : false;
            		
            		String restaurantName = req.getParameter("r_name");
            		
            		String channelNumberString = req.getParameter("c_number");
            		Integer channelNumber = null;
            		if (channelNumberString != null && !channelNumberString.isEmpty()) {
            			channelNumber = Integer.parseInt(channelNumberString);
            		}
            		
            		String restaurantDescription = req.getParameter("r_description");
                    Link website = new Link(req.getParameter("r_website"));
                    
                    BlobKey logoKey = BlobUtils.assignBlobKey(req, "r_logo", blobstoreService);
                    
                    String openingHourString = req.getParameter("r_oth");
                    String openingMinutesString = req.getParameter("r_otm");
                    int openingHour = Integer.parseInt(openingHourString);
                    int openingMinutes = Integer.parseInt(openingMinutesString);
                    Date openingTime = DateManager.getDateValue(openingHour, openingMinutes);
                    
                    String closingHourString = req.getParameter("r_cth");
                    String closingMinutesString = req.getParameter("r_ctm");
                    int closingHour = Integer.parseInt(closingHourString);
                    int closingMinutes = Integer.parseInt(closingMinutesString);
                    Date closingTime = DateManager.getDateValue(closingHour, closingMinutes);
                    
                    String restaurantComments = req.getParameter("r_comments");
                    
                    try {                
                        neoUser = new User(userEmail,
                                userPassword,
                                type);

                        Restaurant restaurant = new Restaurant(
                        		neoUser,
                        		restaurantType,
                        		hasNewsService,
                        		hasProductsService,
                        		hasServiceProvidersService,
                        		hasMessagesService,
                                restaurantName,
                                channelNumber,
                                restaurantDescription,
                                website,
                                logoKey,
                                openingTime,
                                closingTime,
                                restaurantComments);
                        
                        RestaurantManager.putRestaurant(restaurant);
                        SystemManager.updateRestaurantListVersion();
                        SystemManager.updateStoreListVersion(storeSuperType);
                        RestaurantTypeManager.updateStoreTypeVersion(restaurantType);
                        
                        resp.sendRedirect(listRestaurantJSP + "?msg=success&action=" + action);
                    } 
                    catch (MissingRequiredFieldsException mrfe) {
                    	if (logoKey != null)
                    		blobstoreService.delete(logoKey);
                        resp.sendRedirect(addRestaurantJSP + "?etype=MissingInfo");
                        return;
                    } 
                    catch (InvalidFieldFormatException iffe) {
                    	if (logoKey != null)
                    		blobstoreService.delete(logoKey);
                        resp.sendRedirect(addRestaurantJSP + "?etype=Format");
                        return;
                    } 
                    catch (ObjectExistsInDatastoreException oede) {
                    	if (logoKey != null)
                    		blobstoreService.delete(logoKey);
                        resp.sendRedirect(addRestaurantJSP + "?etype=AlreadyExists");
                        return;
                    } 
                    catch (Exception ex) {
                    	if (logoKey != null)
                    		blobstoreService.delete(logoKey);
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                    
                    break;
                // Smart Cash
                case 'S':
                	
                	SmartCashTransaction.SmartCashTransactionType transactionType =
            		SmartCashTransaction.SmartCashTransactionType.CREDIT;
            
		            String customerKeyString = req.getParameter("c_key");
                	Key customerKey = KeyFactory.stringToKey(customerKeyString);
		            Double amount = Double.parseDouble(req.getParameter("c_amount"));
		            
		            try {          
		                SmartCashTransaction transaction = new SmartCashTransaction(
		                		transactionType,
		                		customerKey,
		                		null,
		                		amount
		                		);
		                
		                SmartCashTransactionManager.putSmartCashTransaction(transaction);
		                
		                resp.setStatus(HttpServletResponse.SC_CREATED);
		                resp.sendRedirect(editCustomerSmartCashJSP + 
		                		"?k=" + customerKeyString +
		                		"&status=success");
		            } 
		            catch (MissingRequiredFieldsException mrfe) {
		            	resp.sendRedirect(addCustomerSmartCashJSP + 
		            			"?k=" + customerKeyString +
		            			"&etype=MissingInfo");
		                return;
		            } 
		            catch (Exception ex) {
		                log.log(Level.SEVERE, ex.toString());
		                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
		                		"Internal server error.");
		                return;
		            }
                	
                    break;
                case 'C':

                    // Check who is carrying out the action
                    String jsp;
                    String language = req.getParameter("lang");
                    if (user == null) {
                        jsp = autoCustomerJSP;
                    }
                    else if (user.getUserType() == User.Type.ADMINISTRATOR){
                        jsp = addCustomerJSP;
                    }
                    else {
                    	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                    
                    type = User.Type.CUSTOMER;
                    
                    String customerName = req.getParameter("c_name");
                    PhoneNumber customerPhone = new PhoneNumber(req.getParameter("c_phone"));
                    
                    Customer.Gender gender =
                            req.getParameter("c_gender").charAt(0) == 'M' 
                            ? Customer.Gender.MALE : Customer.Gender.FEMALE;
                    
                    PostalAddress address = null;
                    String customerAddress1 = req.getParameter("c_address1");
            		String customerAddress2 = req.getParameter("c_address2");
            		if (customerAddress2.trim().isEmpty()) {
            			address = new PostalAddress(customerAddress1);
            		}
            		else {
            			address = new PostalAddress(
                            customerAddress1 + " " + customerAddress2);
            		}

                    String customerComments = req.getParameter("c_comments");
                    
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
                        
                        if (user != null)
                            resp.sendRedirect(listCustomerJSP + "?msg=success&action=" + action);
                        else
                            resp.sendRedirect(loginJSP + "?msg=success&action=" + action);
                    } 
                    catch (MissingRequiredFieldsException mrfe) {
                        resp.sendRedirect(
                                jsp + "?etype=MissingInfo" + 
                                (language != null ? "&lang=" + language : ""));
                        return;
                    } 
                    catch (InvalidFieldFormatException iffe) {
                        resp.sendRedirect(
                                jsp + "?etype=Format" +
                                (language != null ? "&lang=" + language : ""));
                        return;
                    } 
                    catch (ObjectExistsInDatastoreException oede) {
                        resp.sendRedirect(
                                jsp + "?etype=AlreadyExists" +
                                (language != null ? "&lang=" + language : ""));
                        return;
                    } 
                    catch (Exception ex) {
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }

                    break;
                default:
                    assert(false); // there are no other types
            }
        } 
        else if (action.equals("update")) {
        	
            char typeChar = req.getParameter("u_type").charAt(0);
            char updateTypeChar = req.getParameter("update_type").charAt(0);
            Email userEmail = new Email(req.getParameter("u_email"));
            
            // Check that a user is carrying out the action
            if (user == null) {
            	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            
            switch (typeChar) {
                case 'A':
                	
                	// Check that an administrator is carrying out the action
                    if (user.getUserType() != User.Type.ADMINISTRATOR) {
                    	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                	
                	String administratorKeyString = req.getParameter("k");
                	
                	// Check if we want to update the information or password
                	if (updateTypeChar == 'I') {
                	
	                	String administratorName = req.getParameter("a_name");
	                	String administratorComments = req.getParameter("a_comments");
	                	
	                    try {
	                        AdministratorManager.updateAdministratorAttributes(
	                                userEmail,
	                                administratorName,
	                                administratorComments);
	
	                        resp.sendRedirect(editAdminJSP + "?k=" + administratorKeyString + "&readonly=true&msg=success&action=" + action + 
	                        		"&update_type=" + updateTypeChar);
	                    } 
	                    catch (MissingRequiredFieldsException mrfe) {
	                        resp.sendRedirect(editAdminJSP + "?etype=MissingInfo&k="
	                                + administratorKeyString);
	                    }
	                    catch (Exception ex) {
	                        log.log(Level.SEVERE, ex.toString());
	                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	                    }
                	}
                	else if (updateTypeChar == 'P') {
                		String newPassword = req.getParameter("u_password");
	                	
	                    try {
	                        AdministratorManager.updateAdministratorPassword(
	                                userEmail,
	                                newPassword
	                                );
	
	                        resp.sendRedirect(editAdminJSP + "?k=" + administratorKeyString +
	                        		"&msg=success&action=" + action + 
	                        		"&update_type=" + updateTypeChar);
	                    } 
	                    catch (MissingRequiredFieldsException mrfe) {
	                        resp.sendRedirect(editAdminPasswordJSP + "?etype=MissingInfo&k="
	                                + administratorKeyString);
	                    }
	                    catch (Exception ex) {
	                        log.log(Level.SEVERE, ex.toString());
	                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	                    }
                	}
                	
                    return;
                case 'R':
                    
                	String restaurantKeyString = req.getParameter("k");
                	
                    // Check that an administrator or a restaurant are carrying out the action
                    String editRestaurantJSP;
                    String successUpdateRestaurantJSP;
                    String successUpdateRestaurantPasswordJSP;
                    if (user.getUserType() == User.Type.ADMINISTRATOR) {
                    	editRestaurantJSP = editRestaurantJSPAdmin;
                    	successUpdateRestaurantJSP = editRestaurantJSP   + 
                    			"?k=" + restaurantKeyString+ "&readonly=true&msg=success&action=" + action + "&update_type=" + updateTypeChar;
                    	successUpdateRestaurantPasswordJSP = editRestaurantJSPAdmin + "?k=" +
                    			restaurantKeyString + "&msg=success&action=" + action + 
                    			"&update_type=" + updateTypeChar;
                    }
                    else if (user.getUserType() == User.Type.RESTAURANT) {
                    	editRestaurantJSP = editRestaurantJSPRestaurant;
                    	successUpdateRestaurantJSP = editRestaurantJSPRestaurant  + 
                    			"?msg=success&action=" + action + "&update_type=" + updateTypeChar;
                    	successUpdateRestaurantPasswordJSP = editRestaurantJSPRestaurant + 
                    			"?msg=success&action=" + action + "&update_type=" + updateTypeChar;
                    }
                    else {
                    	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    	return;
                    }	
                	
                	// Check if we want to update the information or password
                	if (updateTypeChar == 'I') {
                	
	                	String restaurantTypeString = req.getParameter("r_type");
	            		Long restaurantType = null;
	            		Long originalStoreType;
	            		RestaurantType.StoreSuperType storeSuperType = null;
	            		Boolean hasNewsService = true; // All stores have news service
	            		Boolean hasProductsService;
	            		Boolean hasServiceProvidersService;
	            		Boolean hasMessagesService;
	            		
	            		Restaurant restaurant = RestaurantManager.getRestaurant(
            					KeyFactory.stringToKey(restaurantKeyString));
	            		originalStoreType = restaurant.getRestaurantType();
	            		
	            		// If user type is ADMINISTRATOR
	            		if (restaurantTypeString != null) {
		            		if (!restaurantTypeString.isEmpty()) {
		            			restaurantType = Long.parseLong(restaurantTypeString);
		            			RestaurantType storeType =
		            					RestaurantTypeManager.getRestaurantType(restaurantType);
		            			storeSuperType = storeType.getStoreSuperType();
		            		}
		            		hasProductsService = req.getParameter("has_ps") != null ? true : false;
		            		hasServiceProvidersService = req.getParameter("has_sps") != null ? true : false;
		            		hasMessagesService = req.getParameter("has_ms") != null ? true : false;
	            		}
	            		// If user type is RESTAURANT
	            		else {
	            			restaurantType = restaurant.getRestaurantType();
	            			RestaurantType storeType = RestaurantTypeManager.getRestaurantType(restaurantType);
	            			storeSuperType = storeType.getStoreSuperType();
		            		hasProductsService = restaurant.hasProductsService();
		            		hasServiceProvidersService = restaurant.hasServiceProvidersService();
		            		hasMessagesService = restaurant.hasMessagesService();
	            		}
	            		
	            		String restaurantName = req.getParameter("r_name");
	            		
	            		String channelNumberString = req.getParameter("c_number");
	            		Integer channelNumber = null;
	            		if (channelNumberString != null && !channelNumberString.isEmpty()) {
	            			channelNumber = Integer.parseInt(channelNumberString);
	            		}
	            		
	            		String restaurantDescription = req.getParameter("r_description");
	                	
	                	Link restaurantWebsite = new Link(req.getParameter("r_website"));

	                    BlobKey logoKey = BlobUtils.assignBlobKey(req, "r_logo", blobstoreService);
	                    boolean sameLogo = false;
	                    if (logoKey == null) {
	                    	logoKey = restaurant.getRestaurantLogo();
	                    	sameLogo = true;
	                    	log.info("No logo uploaded in restaurant \"" + req.getParameter("u_email") + 
	                    			"\". Using previous logo.");
	                    }
	                    
	                    String openingHourString = req.getParameter("r_oth");
	                    String openingMinutesString = req.getParameter("r_otm");
	                    int openingHour = Integer.parseInt(openingHourString);
	                    int openingMinutes = Integer.parseInt(openingMinutesString);
	                    Date openingTime = DateManager.getDateValue(openingHour, openingMinutes);
	                    
	                    String closingHourString = req.getParameter("r_cth");
	                    String closingMinutesString = req.getParameter("r_ctm");
	                    int closingHour = Integer.parseInt(closingHourString);
	                    int closingMinutes = Integer.parseInt(closingMinutesString);
	                    Date closingTime = DateManager.getDateValue(closingHour, closingMinutes);
	                    
	                    String restaurantComments = req.getParameter("r_comments");
	                	
	                	try {
	                        
	                        RestaurantManager.updateRestaurantAttributes(
	                                userEmail,
	                                storeSuperType,
	                                restaurantType,
	                                hasNewsService,
	                                hasProductsService,
	                                hasServiceProvidersService,
	                                hasMessagesService,
	                                restaurantName,
	                                channelNumber,
	                                restaurantDescription,
	                                restaurantWebsite,
	                                logoKey,
	                                openingTime,
	                                closingTime,
	                                restaurantComments);
	                        SystemManager.updateRestaurantListVersion();
	                        SystemManager.updateStoreListVersion(storeSuperType);
	                        RestaurantTypeManager.updateStoreTypeVersion(restaurantType);
	                        // Update original Store Type Version only if it changed
	                        if (!restaurantType.equals(originalStoreType)) {
	                        	RestaurantTypeManager.updateStoreTypeVersion(originalStoreType);
	                        }
	
	                        resp.sendRedirect(successUpdateRestaurantJSP);
	                        
	                    }
	                	catch (MissingRequiredFieldsException mrfe) {
	                    	if (!sameLogo) {
	                    		blobstoreService.delete(logoKey);
	                    	}
	                        resp.sendRedirect(
	                                editRestaurantJSP + "?etype=MissingInfo&k="
	                                + restaurantKeyString);
	                    } 
	                	catch (Exception ex) {
	                		if (!sameLogo) {
	                    		blobstoreService.delete(logoKey);
	                    	}
	                        log.log(Level.SEVERE, ex.toString());
	                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	                    }
                	}
                	else if (updateTypeChar == 'P') {
                		String newPassword = req.getParameter("u_password");
	                	
	                	try {
	                        
	                        RestaurantManager.updateRestaurantPassword(
	                                userEmail,
	                                newPassword);
	
	                        resp.sendRedirect(successUpdateRestaurantPasswordJSP);
	                        
	                    }
	                	catch (MissingRequiredFieldsException mrfe) {
	                        resp.sendRedirect(
	                        		editRestaurantPasswordJSP + "?etype=MissingInfo&r_key="
	                                + restaurantKeyString);
	                    } 
	                	catch (Exception ex) {
	                        log.log(Level.SEVERE, ex.toString());
	                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	                    }
                	}
                	
                    return;
                case 'C':
                    
                	String customerKeyString = req.getParameter("k");
                	
                	// Check that an administrator or a customer are carrying out the action
                    String editCustomerJSP;
                    String editCustomerPasswordJSP;
                    String successUpdateCustomerJSP;
                    String successUpdateCustomerPasswordJSP;
                    if (user.getUserType() == User.Type.ADMINISTRATOR) {
                    	editCustomerJSP = editCustomerJSPAdmin;
                    	editCustomerPasswordJSP = editCustomerPasswordJSPAdmin;
                    	successUpdateCustomerJSP = editCustomerJSP  + 
                    			"?k=" + customerKeyString + "&readonly=true&msg=success&action=" + action  + "&update_type=" + updateTypeChar;
                    	successUpdateCustomerPasswordJSP = editCustomerJSPAdmin  + "?k=" +
                    			customerKeyString + "&msg=success&action=" + action  + "&update_type=" + updateTypeChar;
                    			
                    }
                    else if (user.getUserType() == User.Type.CUSTOMER) {
                    	editCustomerJSP = editCustomerJSPCustomer;
                    	editCustomerPasswordJSP = editCustomerPasswordJSPCustomer;
                    	successUpdateCustomerJSP = editCustomerJSPCustomer  + 
                    			"?msg=success&action=" + action  + "&update_type=" + updateTypeChar;
                    	successUpdateCustomerPasswordJSP = editCustomerJSPCustomer  + 
                    			"?msg=success&action=" + action  + "&update_type=" + updateTypeChar;
                    }
                    else {
                    	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    	return;
                    }
                	
                	// Check if we want to update the information or password
                	if (updateTypeChar == 'I') {
	                	String customerName = req.getParameter("c_name");
	                	PhoneNumber customerPhone = new PhoneNumber(req.getParameter("c_phone"));
	                	
	                	PostalAddress address = null;
	                	String customerAddress1 = req.getParameter("c_address1");
	            		String customerAddress2 = req.getParameter("c_address2");
	            		if (customerAddress2.trim().isEmpty()) {
	            			address = new PostalAddress(customerAddress1);
	            		}
	            		else {
	            			address = new PostalAddress(
	                            customerAddress1 + " " + customerAddress2);
	            		}
	                    
	                    String customerComments = req.getParameter("c_comments");
	                    
	                	try {
	                        Customer.Gender gender =
	                            req.getParameter("c_gender").charAt(0) == 'M' 
	                            ? Customer.Gender.MALE : Customer.Gender.FEMALE;
	
	                        CustomerManager.updateCustomerAttributes(
	                                userEmail,
	                                customerName,
	                                customerPhone,
	                                gender,
	                                address,
	                                customerComments);
	
	                        resp.sendRedirect(successUpdateCustomerJSP);
	                    } 
	                	catch (MissingRequiredFieldsException mrfe) {
	                        resp.sendRedirect(
	                                editCustomerJSP + "?etype=MissingInfo&k="
	                                + customerKeyString);
	                    } 
	                	catch (InvalidFieldFormatException iffe) {
	                        resp.sendRedirect(
	                                editCustomerJSP + "?etype=Format&k="
	                                + customerKeyString);
	                    } 
	                	catch (Exception ex) {
	                        log.log(Level.SEVERE, ex.toString());
	                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	                    }
                	}
                	else if (updateTypeChar == 'P') {
                		String newPassword = req.getParameter("u_password");
	                    
	                	try {
	                        CustomerManager.updateCustomerPassword(
	                                userEmail,
	                                newPassword);
	
	                        resp.sendRedirect(successUpdateCustomerPasswordJSP);
	                    } 
	                	catch (MissingRequiredFieldsException mrfe) {
	                        resp.sendRedirect(
	                                editCustomerPasswordJSP + "?etype=MissingInfo&k="
	                                + customerKeyString);
	                    }
	                	catch (Exception ex) {
	                        log.log(Level.SEVERE, ex.toString());
	                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	                    }
                	}
	                	
                    return;
                
                default:
                    assert(false); // no other types
            }
        }
    }
}
