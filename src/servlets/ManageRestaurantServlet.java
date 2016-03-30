/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import com.google.appengine.api.datastore.Text;

import datastore.AdditionalPropertyConstraint;
import datastore.AdditionalPropertyConstraintManager;
import datastore.AdditionalPropertyType;
import datastore.AdditionalPropertyTypeManager;
import datastore.AdditionalPropertyValue;
import datastore.AdditionalPropertyValueManager;
import datastore.Branch;
import datastore.BranchManager;
import datastore.MenuItem;
import datastore.MenuItemAdditionalPropertyValue;
import datastore.MenuItemAdditionalPropertyValueManager;
import datastore.MenuItemManager;
import datastore.MenuItemType;
import datastore.MenuItemTypeManager;
import datastore.Message;
import datastore.MessageManager;
import datastore.OpinionPollRatingEntry;
import datastore.Order;
import datastore.OrderManager;
import datastore.RestaurantManager;
import datastore.RestaurantNews;
import datastore.RestaurantNewsManager;
import datastore.RestaurantOpinionPoll;
import datastore.RestaurantOpinionPollManager;
import datastore.RestaurantOpinionPollMultipleChoiceValue;
import datastore.Set;
import datastore.SetManager;
import datastore.SmartCashTransaction;
import datastore.SmartCashTransactionManager;
import datastore.Survey;
import datastore.SurveyManager;
import datastore.SurveyOpinionPoll;
import datastore.SurveyOpinionPollManager;
import datastore.SurveyOpinionPollMultipleChoiceValue;
import datastore.SurveyOpinionPollRatingEntry;
import datastore.SystemManager;
import datastore.TypeSetMenuItem;
import datastore.User;
import exceptions.InvalidFieldFormatException;
import exceptions.InvalidFieldSelectionException;
import exceptions.MissingRequiredFieldsException;

/**
 * This servlet class is used to add, delete and update
 * different objects related to restaurants.
 * 
 */

@SuppressWarnings("serial")
public class ManageRestaurantServlet extends HttpServlet {

    private static final Logger log = 
        Logger.getLogger(ManageRestaurantServlet.class.getName());
    
    private static final BlobstoreService blobstoreService = 
    	BlobstoreServiceFactory.getBlobstoreService();
    
    // JSP file locations
    private static final String addAdditionalPropertyTypeJSP = "/restaurant/addAdditionalPropertyType.jsp";
    private static final String addAdditionalPropertyValueJSP = "/restaurant/addAdditionalPropertyValues.jsp";
    private static final String addBranchJSP = "/restaurant/addBranch.jsp";
    private static final String addMenuItemJSP = "/restaurant/addMenuItem.jsp";
    private static final String addMenuItemTypeJSP = "/restaurant/addMenuItemType.jsp";
    private static final String addMessagesJSP = "/restaurant/addMessages.jsp";
    private static final String addRestaurantNewsJSP = "/restaurant/addNews.jsp";
    private static final String addRestaurantOpinionPollJSP = "/restaurant/addOpinionPoll.jsp";
    private static final String addSetsJSP = "/restaurant/addSets.jsp";
    private static final String addSurveyJSP = "/restaurant/addSurvey.jsp";
    private static final String editAdditionalPropertyTypeJSP = "/restaurant/editAdditionalPropertyType.jsp";
    private static final String editAdditionalPropertyValueJSP = "/restaurant/editAdditionalPropertyValues.jsp";
    private static final String editBranchJSP = "/restaurant/editBranch.jsp";
    private static final String editMenuItemJSP = "/restaurant/editMenuItem.jsp";
    private static final String editMenuItemTypeJSP = "/restaurant/editMenuItemType.jsp";
    private static final String editMessagesJSP = "/restaurant/editMessages.jsp";
    private static final String editOrderJSP = "/restaurant/editOrder.jsp";
    private static final String editProductItemPropertyJSP = "/restaurant/editProductItemProperty.jsp";
    private static final String editProductItemPropertyConstraintsJSP = "/restaurant/editProductItemPropertyConstraints.jsp";
    private static final String editRestaurantNewsJSP= "/restaurant/editNews.jsp";
    private static final String editRestaurantOpinionPollJSP = "/restaurant/editOpinionPoll.jsp";
    private static final String editSetsJSP = "/restaurant/editSets.jsp";
    private static final String editSurveyJSP = "/restaurant/editSurvey.jsp";
    private static final String listAdditionalPropertyTypeJSP = "/restaurant/listAdditionalPropertyType.jsp";   
    private static final String listAdditionalPropertyValueJSP = "/restaurant/listAdditionalPropertyValues.jsp";
    private static final String listBranchJSP = "/restaurant/listBranch.jsp";
    private static final String listMenuItemJSP = "/restaurant/listMenuItem.jsp";  
    private static final String listMenuItemTypeJSP = "/restaurant/listMenuItemType.jsp";
    private static final String listMessagesJSP = "/restaurant/listMessages.jsp";
    private static final String listOrderJSP = "/restaurant/listOrder.jsp"; 
    private static final String listRestaurantNewsJSP = "/restaurant/listNews.jsp";
    private static final String listRestaurantOpinionPollJSP = "/restaurant/listOpinionPoll.jsp";
    private static final String listSetsJSP = "/restaurant/listSets.jsp";
    private static final String listSurveyJSP = "/restaurant/listSurvey.jsp";

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
                throws IOException {
    	
    	// Check that an actual user is carrying out the action
    	HttpSession session = req.getSession(true);
        User user = (User) session.getAttribute("user");
        if (user == null) {
        	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
    	
        // Lets check the action required by the jsp
        String action = req.getParameter("action");

        if (action.equals("delete")) {
            // deleting something
            switch(req.getParameter("type").charAt(0)){
            	// Menu Item Types
                case 'T':
                	// Check that the user is a Restaurant
                	if (user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                	
                	Key menuItemTypeKey = KeyFactory.stringToKey(req.getParameter("k"));

                    MenuItemTypeManager.deleteMenuItemType(menuItemTypeKey);

                    resp.sendRedirect(listMenuItemTypeJSP + "?msg=success&action=" + action);
                	break;
                // Menu Items
                case 'I':
                	// Check that the user is a Restaurant
                	if (user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                	
                	Key menuItemKey = KeyFactory.stringToKey(req.getParameter("k"));
                	MenuItem productItem = MenuItemManager.getMenuItem(menuItemKey);
                	
                	MenuItemTypeManager.updateProductItemTypeVersion(productItem.getMenuItemType());

                    MenuItemManager.deleteMenuItem(menuItemKey);
                    
                    resp.sendRedirect(listMenuItemJSP + "?msg=success&action=" + action);
                    break;
                // Sets
                case 'E':
                    // Check that the user is a Restaurant
                	if (user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                	
                	Key setKey = KeyFactory.stringToKey(req.getParameter("k"));

                    SetManager.deleteSet(setKey);
                    
                    resp.sendRedirect(listSetsJSP + "?msg=success&action=" + action);
                    break;
                // Additional Property Types
                case 'P':
                	// Check that the user is a Restaurant
                	if (user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                	
                	Key additionalPropertyTypeKey = KeyFactory.stringToKey(req.getParameter("k"));

                    AdditionalPropertyTypeManager.deleteAdditionalPropertyType(additionalPropertyTypeKey);
                    
                    resp.sendRedirect(listAdditionalPropertyTypeJSP + "?msg=success&action=" + action);
                    break;
                // Additional Property Values
                case 'V':
                	// Check that the user is a Restaurant
                	if (user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                	
                	Key additionalPropertyValueKey = KeyFactory.stringToKey(req.getParameter("k"));
                	additionalPropertyTypeKey = additionalPropertyValueKey.getParent();

                    AdditionalPropertyValueManager.deleteAdditionalPropertyValue(additionalPropertyValueKey);
                    
                    resp.sendRedirect(listAdditionalPropertyValueJSP + "?apt_k=" +
                    		KeyFactory.keyToString(additionalPropertyTypeKey) +
                    		"&msg=success&action=" + action);
                    break;
                // Branches
                case 'B':
                	Key branchKey = KeyFactory.stringToKey(req.getParameter("k"));
                	
                	Key restaurantKey = branchKey.getParent();
                	String restaurantKeyString = KeyFactory.keyToString(restaurantKey);
                	 
                	// Check the user type carrying out the action
                	String redirectURL = "";
                	if (user.getUserType() == User.Type.ADMINISTRATOR) {
                		redirectURL = listBranchJSP + "?r_key=" +
                				restaurantKeyString + "&msg=success&action=" + action;
                	}
                	else if (user.getUserType() == User.Type.RESTAURANT){
                		redirectURL = listBranchJSP + "?msg=success&action=" + action;
                	}
                	else {
                        resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}

                	BranchManager.deleteBranch(branchKey);
                	SystemManager.updateRestaurantListVersion();
                    
                	resp.sendRedirect(redirectURL);
                	
                    break;
                 // Restaurant News
                 case 'N':
                	
                	Key restaurantNewsKey = KeyFactory.stringToKey(req.getParameter("k"));
                	restaurantKey = restaurantNewsKey.getParent();
                	restaurantKeyString = KeyFactory.keyToString(restaurantKey);
                	
                	// Check the user type carrying out the action
                 	if (user.getUserType() == User.Type.ADMINISTRATOR) {
                 		redirectURL = listRestaurantNewsJSP + "?r_key=" +
                 				restaurantKeyString + "&msg=success&action=" + action;
                 	}
                 	else if (user.getUserType() == User.Type.RESTAURANT){
                 		redirectURL = listRestaurantNewsJSP + 
                 				"?msg=success&action=" + action;
                 	}
                 	else {
                         resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                         return;
                 	}

                    RestaurantNewsManager.deleteRestaurantNews(restaurantNewsKey);
                    
                    resp.sendRedirect(redirectURL);
                    break;
                 // Restaurant Opinion Poll
                 case 'L':
                	
                	Key opinionPollKey = KeyFactory.stringToKey(req.getParameter("k"));     		
                	
                	Key parentKey = opinionPollKey.getParent();
                	String parentKeyString = KeyFactory.keyToString(parentKey);
                	String parent = parentKey.getKind();
                	
                	// Check the user type carrying out the action
                 	if (user.getUserType() == User.Type.ADMINISTRATOR) {
                 		if (parent.equalsIgnoreCase("Restaurant")) {
	                 		redirectURL = listRestaurantOpinionPollJSP + "?r_key=" +
	                 				parentKeyString + "&msg=success&action=" + action;

	                 		RestaurantOpinionPollManager.deleteRestaurantOpinionPoll(opinionPollKey);
                 		}
                 		else {
                 			redirectURL = listRestaurantOpinionPollJSP + "?sk=" +
                 					parentKeyString + "&msg=success&action=" + action;
                 			
                 			SurveyOpinionPollManager.deleteSurveyOpinionPoll(opinionPollKey);
                 		}
                 	}
                 	else if (user.getUserType() == User.Type.RESTAURANT){
                 		if (parent.equalsIgnoreCase("Restaurant")) {
	                 		redirectURL = listRestaurantOpinionPollJSP + 
	                 				"?msg=success&action=" + action;
	                 		
	                 		RestaurantOpinionPollManager.deleteRestaurantOpinionPoll(opinionPollKey);
                 		}
                 		else {
                 			redirectURL = listRestaurantOpinionPollJSP + "?sk=" + 
	                 				parentKeyString + "&msg=success&action=" + action;
                 			
                 			SurveyOpinionPollManager.deleteSurveyOpinionPoll(opinionPollKey);
                 		}
                 	}
                 	else {
                         resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                         return;
                 	}
   
                    resp.sendRedirect(redirectURL);
                    break;
                // Orders
                case 'O':
                 	// Check that the user is a Restaurant
                 	if (user.getUserType() != User.Type.RESTAURANT) {
                 		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                 	}
                 	
                 	Long orderKey = Long.parseLong(req.getParameter("k"));
                 	OrderManager.deleteOrder(orderKey);
                     
                    resp.sendRedirect(listOrderJSP + "?msg=success&action=" + action +
                    		"&type=H");
                    break;
                 // Restaurant Opinion Poll
                case 'S':
               	
	               	Key surveyKey = KeyFactory.stringToKey(req.getParameter("k"));
	               	Key storeKey = surveyKey.getParent();
	               	String storeKeyString = KeyFactory.keyToString(storeKey);
	               	
	               	// Check the user type carrying out the action
                	if (user.getUserType() == User.Type.ADMINISTRATOR) {
                		redirectURL = listSurveyJSP + "?r_key=" +
                				storeKeyString + "&msg=success&action=" + action;
                	}
                	else if (user.getUserType() == User.Type.RESTAURANT){
                		redirectURL = listSurveyJSP + 
                				"?msg=success&action=" + action;
                	}
                	else {
                        resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}

                   SurveyManager.deleteSurvey(surveyKey);
                   
                   resp.sendRedirect(redirectURL);
                   break;
                // Message
                case 'G':
                	// Check that the user is a Restaurant
                 	if (user.getUserType() != User.Type.RESTAURANT) {
                 		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                 	}
                 	
                 	Key messageKey = KeyFactory.stringToKey(req.getParameter("k"));
                 	MessageManager.deleteMessage(messageKey);
                     
                    resp.sendRedirect(listMessagesJSP + "?msg=success&action=" + action);
                    break;
                default:
                    assert(false); // there is no other type
            }
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) 
                throws IOException {
    	
    	// Check that an actual user is carrying out the action
    	HttpSession session = req.getSession(true);
        User user = (User) session.getAttribute("user");
        if (user == null) {
        	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
    	
        // Let's check the action required by the jsp
        String action = req.getParameter("action");

        if (action.equals("add")) {
            // what are we adding?
            switch(req.getParameter("type").charAt(0)){
            	// Menu Item Types
                case 'T':
                	
                	// Check that the user is a Restaurant
                	if (user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                	
                	String keepAddingString = req.getParameter("keep_adding");
                	boolean keepAdding = keepAddingString == null ? false : true;
                	
                	String menuItemTypeName = req.getParameter("mit_name");
                	String menuItemTypeDescription = req.getParameter("mit_description");
                	
                    try {                
                        MenuItemType type = new MenuItemType(
                                menuItemTypeName,
                                menuItemTypeDescription);
                        MenuItemTypeManager.putMenuItemType(user.getUserEmail(), type);
                        
                        if (keepAdding) {
                        	resp.sendRedirect(addMenuItemTypeJSP + "?msg=success&action=" + action);
                        }
                        else {
                        	resp.sendRedirect(listMenuItemTypeJSP + "?msg=success&action=" + action);
                        }
                    }
                    catch (MissingRequiredFieldsException mrfe) {
                        resp.sendRedirect(addMenuItemTypeJSP + "?etype=MissingInfo");
                        return;
                    } 
                    catch (Exception ex) {
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                    
                    break;
                // Menu Items
                case 'I':
                	
                	// Check that the user is a Restaurant
                	if (user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                	
                	keepAddingString = req.getParameter("keep_adding");
                	keepAdding = keepAddingString == null ? false : true;
                    
                    String menuItemTypeString = req.getParameter("mit_id");
            		Key menuItemType = null;
            		if (!menuItemTypeString.isEmpty()) {
            			menuItemType = KeyFactory.stringToKey(menuItemTypeString);
            		}
                    
                    String menuItemName = req.getParameter("mi_name");
                    
                    String priceString = req.getParameter("mi_price");
            		Double price = null;
            		if (!priceString.isEmpty()) {
            			price = Double.parseDouble(priceString);
            		}
            		
            		String discountString = req.getParameter("mi_discount");
            		Double discount = null;
            		if (!discountString.isEmpty()) {
            			discount = Double.parseDouble(discountString);
            		}
            		
            		String menuItemDescription = req.getParameter("mi_description");

            		BlobKey imageKey = BlobUtils.assignBlobKey(req, "mi_image", blobstoreService);
            		
            		String servingTimeString = req.getParameter("mi_serving_time");
            		Integer servingTime = null;
            		if (!servingTimeString.isEmpty()) {
            			servingTime = Integer.parseInt(servingTimeString);
            		}
            		
            		Boolean isAvailable = req.getParameter("mi_isavailable").charAt(0)
                            == 'Y' ? true : false;
            		
            		String serviceTimeString = req.getParameter("mi_service_time");
            		Integer serviceTime = null;
            		if (!serviceTimeString.isEmpty()) {
            			serviceTime = Integer.parseInt(serviceTimeString);
            		}
            		
            		String menuItemComments = req.getParameter("mi_comments");
                    
                    try {                
                        MenuItem menuItem = new MenuItem(
                                menuItemType,
                                menuItemName,
                                price,
                                discount,
                                menuItemDescription,
                                imageKey,
                                servingTime,
                                isAvailable,
                                serviceTime,
                                menuItemComments);
                        
                        MenuItemManager.putMenuItem(user.getUserEmail(), menuItem);
                        
                        MenuItemTypeManager.updateProductItemTypeVersion(menuItemType);
                        
                        if (keepAdding) {
                        	resp.sendRedirect(addMenuItemJSP + "?msg=success&action=" + action);
                        }
                        else {
                        	resp.sendRedirect(listMenuItemJSP + "?msg=success&action=" + action);
                        }
                    }
                    catch (MissingRequiredFieldsException mrfe) {
                    	if (imageKey != null)
                    		blobstoreService.delete(imageKey);
                        resp.sendRedirect(addMenuItemJSP + "?etype=MissingInfo");
                        return;
                    }
                    catch (InvalidFieldFormatException iffe) {
                    	if (imageKey != null)
                    		blobstoreService.delete(imageKey);
                        resp.sendRedirect(addMenuItemJSP + "?etype=Format");
                        return;
                    }
                    catch (Exception ex) {
                    	if (imageKey != null)
                    		blobstoreService.delete(imageKey);
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }

                    break;
                // Sets
                case 'E':

                	// Check that the user is a Restaurant
                	if (user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                	
                	String saveValuesString = req.getParameter("save_values");
                	Boolean saveValues = saveValuesString == null ? false : true;                	
                	
                	keepAddingString = req.getParameter("keep_adding");
                	keepAdding = keepAddingString == null ? false : true;
                	
                    String setNumberString = req.getParameter("set_number");
                    Integer setNumber = null;
                    if (!setNumberString.isEmpty()) {
                    	setNumber = Integer.parseInt(setNumberString);
                    }
                    
                    String setTypeString = req.getParameter("set_type");
                    Set.SetType setType = Set.getSetTypeFromString(setTypeString);
                    
                    String setName = req.getParameter("set_name");
                    
                    String setDescription = req.getParameter("set_description");
                    
                    String setPriceString = req.getParameter("set_price");
            		Double setPrice = null;
            		if (setPriceString != null) {
	            		if (!setPriceString.isEmpty()) {
	            			setPrice = Double.parseDouble(setPriceString);
	            		}
            		}
            		
            		//String hasFixedPriceString = req.getParameter("set_fixed_price");
            		//Boolean hasFixedPrice = Boolean.parseBoolean(hasFixedPriceString);
            		// TODO: Change later
            		Boolean hasFixedPrice = true;
            		
            		String setDiscountString = req.getParameter("set_discount");
            		Double setDiscount = null;
            		if (!setDiscountString.isEmpty()) {
            			setDiscount = Double.parseDouble(setDiscountString);
            		}

            		imageKey = BlobUtils.assignBlobKey(req, "set_image", blobstoreService);
                    
            		servingTimeString = req.getParameter("set_serving_time");
            		servingTime = null;
            		if (!servingTimeString.isEmpty()) {
            			servingTime = Integer.parseInt(servingTimeString);
            		}
            		
            		isAvailable = req.getParameter("set_is_available").charAt(0)
                            == 'Y' ? true : false;
            		
            		serviceTimeString = req.getParameter("set_service_time");
            		serviceTime = null;
            		if (!serviceTimeString.isEmpty()) {
            			serviceTime = Integer.parseInt(serviceTimeString);
            		}
            		
            		String setComments = req.getParameter("set_comments");
            		
            		// Menu Item Keys
            		ArrayList<Key> menuItemKeys = new ArrayList<Key>();
            		ArrayList<TypeSetMenuItem> typeSetMenuItems = new ArrayList<TypeSetMenuItem>();
            		if (setType == Set.SetType.FIXED_SET) {
            			for (int i = 1; i <= 25; i++) {
            				String menuItemKeyString = req.getParameter("mi_id_tf_" + i);
            				
            				if (!menuItemKeyString.equals("none")) {
            					Key menuItemKey = KeyFactory.stringToKey(menuItemKeyString);
            					int amount = Integer.parseInt(req.getParameter("no_product_items_" + i));
            					for (int j = 0; j < amount; j++) {
            						menuItemKeys.add(menuItemKey);
            					}
            				}
            			}
            		}
            		else if (setType == Set.SetType.TYPE_SET) {
            			for (int i = 1; i <= 25; i++) {
            				
            				// Check if a menu item type was selected
            				String menuItemTypeKeyString = req.getParameter("mit_tt_" + i);
            				if (!menuItemTypeKeyString.equals("none")) {
	            				
            					// Check if all menu items for this type were selected
            					boolean includeAllItems = Boolean.parseBoolean(req.getParameter("set_tt_fixed_" + i));
            					if (includeAllItems) {
            						menuItemType = KeyFactory.stringToKey(menuItemTypeKeyString);
            						List<MenuItem> menuItems = MenuItemManager.getAllMenuItemsByType(menuItemType);
            						
            						for (MenuItem menuItem : menuItems) {
	            						try {
			            					TypeSetMenuItem typeSetMenuItem = new TypeSetMenuItem(
			            							menuItem.getKey(),
			            							menuItem.getMenuItemPrice()
			            							);
			            					
			            					typeSetMenuItems.add(typeSetMenuItem);
		            					}
		            					catch (MissingRequiredFieldsException mrfe) {
		                                	if (imageKey != null)
		                                		blobstoreService.delete(imageKey);
		                                    resp.sendRedirect(addSetsJSP + "?etype=MissingInfo");
		                                    return;
		                                }
		            					catch (Exception ex) {
		                                	if (imageKey != null)
		                                		blobstoreService.delete(imageKey);
		                                    log.log(Level.SEVERE, ex.toString());
		                                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		                                }
            						}
            					}
            					// If only some menu items were selected
            					else {
	            					for (int j = 1; j <= 10; j++) {
		            				
			            				String menuItemKeyString = req.getParameter("mi_id_tt_" + ((i * 100) + j));
			            				
			            				// Check if a menu item was selected
			            				if (!menuItemKeyString.equals("none")) {
			            					
			            					Key menuItemKey = KeyFactory.stringToKey(menuItemKeyString);
			            					
			            					// Check if a specific price was given to the menu item
			            					priceString = req.getParameter("set_tt_mi_" + ((i * 100) + j) + "_price");
			            					price = null;
			            					if (priceString != null) {
				            					if (!priceString.isEmpty()) {
				            						price = Double.parseDouble(priceString);
				            					}
			            					}
			            					else {
			            						MenuItem menuItem = MenuItemManager.getMenuItem(menuItemKey);
			            						price = menuItem.getMenuItemPrice();
			            					}
			            					
			            					try {
				            					TypeSetMenuItem typeSetMenuItem = new TypeSetMenuItem(
				            							menuItemKey,
				            							price
				            							);
				            					
				            					typeSetMenuItems.add(typeSetMenuItem);
			            					}
			            					catch (MissingRequiredFieldsException mrfe) {
			                                	if (imageKey != null)
			                                		blobstoreService.delete(imageKey);
			                                    resp.sendRedirect(addSetsJSP + "?etype=MissingInfo");
			                                    return;
			                                }
			            					catch (Exception ex) {
			                                	if (imageKey != null)
			                                		blobstoreService.delete(imageKey);
			                                    log.log(Level.SEVERE, ex.toString());
			                                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			                                }
			            				}
		            				}
            					}
            				}
            			}
            		}

                    try {
                    	
                        Set set = new Set(
                        		setNumber, 
                        		setType,
                        		setName,
                        		setDescription,
                        		setPrice,
                        		hasFixedPrice,
                        		setDiscount,
                        		imageKey,
                        		servingTime,
                        		isAvailable,
                        		serviceTime,
                        		setComments,
                        		menuItemKeys,
                        		typeSetMenuItems
                        		);
                        
                        SetManager.putSet(user.getUserEmail(), set);
                        Key setKey = set.getKey();
                        if (keepAdding) {
                        	if(saveValues){
                        		resp.sendRedirect(addSetsJSP + "?sk=" + KeyFactory.keyToString(setKey) + "&msg=success&action=" + action);                        		
                        	}else{
                        		resp.sendRedirect(addSetsJSP + "?msg=success&action=" + action);
                        	}
                        }
                        else {
                        	resp.sendRedirect(listSetsJSP + "?msg=success&action=" + action);
                        }
                    }
                    catch (MissingRequiredFieldsException mrfe) {
                    	if (imageKey != null)
                    		blobstoreService.delete(imageKey);
                        resp.sendRedirect(addSetsJSP + "?etype=MissingInfo");
                        return;
                    }
                    catch (InvalidFieldFormatException iffe) {
                    	if (imageKey != null)
                    		blobstoreService.delete(imageKey);
                        resp.sendRedirect(addSetsJSP + "?etype=Format");
                        return;
                    }
                    catch (Exception ex) {
                    	if (imageKey != null)
                    		blobstoreService.delete(imageKey);
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }

                    break;
                // Additional Property Types
                case 'P':
                	// Check that the user is a Restaurant
                	if (user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                	
                	keepAddingString = req.getParameter("keep_adding");
                	keepAdding = keepAddingString == null ? false : true;

                	String additionalPropertyTypeName = req.getParameter("apt_name");
                	String additionalPropertyTypeDescription = req.getParameter("apt_description");

                    try {                
                        AdditionalPropertyType type = new AdditionalPropertyType(
                        		additionalPropertyTypeName,
                        		additionalPropertyTypeDescription);
                        AdditionalPropertyTypeManager.putAdditionalPropertyType(user.getUserEmail(), type);
                        
                        if (keepAdding) {
                        	resp.sendRedirect(addAdditionalPropertyTypeJSP + "?msg=success&action=" + action);
                        }
                        else {
                        	resp.sendRedirect(listAdditionalPropertyTypeJSP + "?msg=success&action=" + action);
                        }
                    }
                    catch (MissingRequiredFieldsException mrfe) {
                        resp.sendRedirect(addAdditionalPropertyTypeJSP + "?etype=MissingInfo");
                        return;
                    } 
                    catch (Exception ex) {
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                    
                    break;
                // Additional Property Values
                case 'V':
                	// Check that the user is a Restaurant
                	if (user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                	
                	keepAddingString = req.getParameter("keep_adding");
                	keepAdding = keepAddingString == null ? false : true;

                	String additionalPropertyTypeKeyString = req.getParameter("apt_k");
                	Key additionalPropertyTypeKey = KeyFactory.stringToKey(additionalPropertyTypeKeyString);
                	
                	String additionalPropertyValueName = req.getParameter("apv_name");

                    try {
                        AdditionalPropertyValue value = new AdditionalPropertyValue(
                        		additionalPropertyValueName);
                        AdditionalPropertyValueManager.putAdditionalPropertyValue(additionalPropertyTypeKey, value);
                        
                        if (keepAdding) {
                        	resp.sendRedirect(addAdditionalPropertyValueJSP + "?apt_k=" +
                        			additionalPropertyTypeKeyString + "&msg=success&action=" + action);
                        }
                        else {
                        	resp.sendRedirect(listAdditionalPropertyValueJSP + "?apt_k=" +
                        			additionalPropertyTypeKeyString + "&msg=success&action=" + action);
                        }
                    }
                    catch (MissingRequiredFieldsException mrfe) {
                        resp.sendRedirect(addAdditionalPropertyValueJSP + "?apt_k=" + additionalPropertyTypeKeyString +
                        		"&etype=MissingInfo");
                        return;
                    } 
                    catch (Exception ex) {
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                    
                    break;
                // Branches
                case 'B':
                	
                	// Check the user type carrying out the action
                	String restaurantKeyString;
                	Key restaurantKey;
                	if (user.getUserType() == User.Type.ADMINISTRATOR) {
                		restaurantKeyString = req.getParameter("r_key");
                		restaurantKey = KeyFactory.stringToKey(restaurantKeyString);
                	}
                	else if (user.getUserType() == User.Type.RESTAURANT) {
                		restaurantKey = RestaurantManager.getRestaurant(user).getKey();
                		restaurantKeyString = KeyFactory.keyToString(restaurantKey);
                	}
                	else {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                	
                	keepAddingString = req.getParameter("keep_adding");
                	keepAdding = keepAddingString == null ? false : true;
                    
                    String regionString = req.getParameter("b_region");
            		Long region = null;
            		if (!regionString.isEmpty()) {
            			region = Long.parseLong(regionString);
            		}
                    
                    String branchName = req.getParameter("b_name");
                    
                    String branchAddress1 = req.getParameter("b_address1");
            		String branchAddress2 = req.getParameter("b_address2");
            		PostalAddress address;
            		if (branchAddress2.trim().isEmpty()) {
            			address = new PostalAddress(branchAddress1);
            		}
            		else {
            			address = new PostalAddress(
            					branchAddress1 + " " + branchAddress2);
            		}
                    
                    PhoneNumber branchPhoneNumber = new PhoneNumber(req.getParameter("b_phone"));
                    
                    Boolean hasDelivery = Boolean.parseBoolean(req.getParameter("has_delivery"));
                    
                    String hasRegularDeliveryString = req.getParameter("has_regular_delivery");
                    Boolean hasRegularDelivery = null;
                    if (hasRegularDeliveryString != null) {
                    	hasRegularDelivery = Boolean.parseBoolean(hasRegularDeliveryString);
                    }
                    
                    String hasPostalDeliveryString = req.getParameter("has_postal_delivery");
                    Boolean hasPostalDelivery = null;
                    if (hasPostalDeliveryString != null) {
                    	hasPostalDelivery = Boolean.parseBoolean(hasPostalDeliveryString);
                    }
                    
                    String hasUPSDeliveryString = req.getParameter("has_ups_delivery");
                    Boolean hasUPSDelivery = null;
                    if (hasUPSDeliveryString != null) {
                    	hasUPSDelivery = Boolean.parseBoolean(hasUPSDeliveryString);
                    }
                    
                    String hasConvenienceStoreDeliveryString = req.getParameter("has_cs_delivery");
                    Boolean hasConvenienceStoreDelivery = null;
                    if (hasConvenienceStoreDeliveryString != null) {
                    	hasConvenienceStoreDelivery = 
                    			Boolean.parseBoolean(hasConvenienceStoreDeliveryString);
                    }
                    
                    Boolean hasTakeOut = Boolean.parseBoolean(req.getParameter("has_takeout"));
                    Boolean hasTakeIn = Boolean.parseBoolean(req.getParameter("has_takein"));
                    
                    Email branchEmail = new Email(req.getParameter("b_email"));
                    
                    try {                
                        Branch branch = new Branch(
                                region,
                                branchName,
                                address,
                                branchPhoneNumber,
                                hasDelivery,
                                hasRegularDelivery,
                                hasPostalDelivery,
                                hasUPSDelivery,
                                hasConvenienceStoreDelivery,
                                hasTakeOut,
                                hasTakeIn,
                                branchEmail);
                        BranchManager.putBranch(restaurantKey, branch);
                        SystemManager.updateRestaurantListVersion();
                        
                    	if (keepAdding) {
                    		resp.sendRedirect(addBranchJSP + "?r_key=" + 
                    				restaurantKeyString + "&msg=success&action=" + action);
                    	}
                    	else {
                    		resp.sendRedirect(listBranchJSP + "?r_key=" + 
                    				restaurantKeyString + "&msg=success&action=" + action);
                    	}
                    }
                    catch (MissingRequiredFieldsException mrfe) {
                    	resp.sendRedirect(addBranchJSP + "?etype=MissingInfo&r_key=" +
                    			restaurantKeyString);
                        return;
                    }
                    catch (InvalidFieldFormatException iffe) {
                    	resp.sendRedirect(addBranchJSP + "?etype=Format&r_key=" +
                    			restaurantKeyString);
                        return;
                    }
                    catch (InvalidFieldSelectionException ifse) {
                    	resp.sendRedirect(addBranchJSP + "?etype=Selection&r_key=" +
                    			restaurantKeyString);
                        return;
                    }
                    catch (Exception ex) {
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }

                    break;
                // Restaurant News
                case 'N':
                	
                	// Check that the user is a Restaurant
                	if (user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                	
                	keepAddingString = req.getParameter("keep_adding");
                	keepAdding = keepAddingString == null ? false : true;
                	
                	String restaurantNewsTitle = req.getParameter("rn_title");
                	String restaurantNewsContent = req.getParameter("rn_content");
                	
                	Boolean allowResponse = 
                			Boolean.parseBoolean(req.getParameter("rn_allow_response"));
                	
                	String maxClicksString = req.getParameter("max_clicks");
                	Integer maxClicks = null;
                	if (maxClicksString != null) {
	                	if (!maxClicksString.isEmpty()) {
	                		maxClicks = Integer.parseInt(maxClicksString);
	                	}
                	}
                	
                	if (req.getParameter("u_responses") != null) {
                		maxClicks = Integer.MAX_VALUE;
                	}
                	
                	String restaurantNewsStartingDateString = req.getParameter("rn_start");
            		Date restaurantNewsStartingDate = null;
            		if (!restaurantNewsStartingDateString.isEmpty()) {	
            			restaurantNewsStartingDate = DateManager.getDateValue(restaurantNewsStartingDateString);
            		}
            		
            		String restaurantNewsEndingDateString = req.getParameter("rn_end");
            		Date restaurantNewsEndingDate = null;
            		if (!restaurantNewsEndingDateString.isEmpty()) {	
            			restaurantNewsEndingDate = DateManager.getDateValue(restaurantNewsEndingDateString);
            		}
            		
            		Integer restaurantNewsPriority = 0;
            		
            		Boolean isPrivate = 
            				Boolean.parseBoolean(req.getParameter("rn_is_private"));

            		imageKey = BlobUtils.assignBlobKey(req, "rn_image", blobstoreService);
                	
                    try {          
                    	RestaurantNews restaurantNews = new RestaurantNews(
                    			restaurantNewsTitle,
                    			restaurantNewsContent,
                    			allowResponse,
                    			maxClicks,
                    			restaurantNewsStartingDate,
                    			restaurantNewsEndingDate,
                    			restaurantNewsPriority,
                    			isPrivate,
                    			imageKey
                    			);

                        RestaurantNewsManager.putRestaurantNews(user.getUserEmail(), restaurantNews);
                        
                        if (keepAdding) {
                        	resp.sendRedirect(addRestaurantNewsJSP + "?msg=success&action=" + action);
                        }
                        else {
                        	resp.sendRedirect(listRestaurantNewsJSP + "?msg=success&action=" + action);
                        }
                    }
                    catch (MissingRequiredFieldsException mrfe) {
                    	if (imageKey != null)
                    		blobstoreService.delete(imageKey);
                        resp.sendRedirect(addRestaurantNewsJSP + "?etype=MissingInfo");
                        return;
                    }
                    catch (Exception ex) {
                    	if (imageKey != null)
                    		blobstoreService.delete(imageKey);
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                    
                    break;
                // Restaurant Opinion Poll
                case 'L':
                	
                	// Check that the user is a Restaurant
                	if (user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                	
                	// Check if parent is Survey or Store
                	String surveyKeyString = req.getParameter("sk");
                	Key surveyKey = null;
                	Survey survey = null;
                	String parent;
                	if (surveyKeyString != null) {
                		surveyKey = KeyFactory.stringToKey(surveyKeyString);
                		survey = SurveyManager.getSurvey(surveyKey);
                		parent = "Survey";
                	}
                	else {
                		parent = "Restaurant";
                	}
                	
                	keepAddingString = req.getParameter("keep_adding");
                	keepAdding = keepAddingString == null ? false : true;
                	
                	String opinionPollTypeString = req.getParameter("op_type");
                	RestaurantOpinionPoll.Type restaurantOpinionPollType = null;
                	SurveyOpinionPoll.Type surveyOpinionPollType = null;
                	if (parent.equalsIgnoreCase("Restaurant")) {
                		restaurantOpinionPollType = RestaurantOpinionPoll.getRestaurantOpinionPollTypeFromString(
                					opinionPollTypeString);
                	}
                	else {
                		surveyOpinionPollType = SurveyOpinionPoll.getSurveyOpinionPollTypeFromString(
                				opinionPollTypeString);
                	}
                	
                	String opinionPollTitle = req.getParameter("op_title");
                	String opinionPollContent = req.getParameter("op_content");
                	
                	Date restaurantOpinionPollStartingDate = null, restaurantOpinionPollEndingDate = null;
                	if (parent.equals("Restaurant")) {
	                	String restaurantOpinionPollStartingDateString = req.getParameter("op_start");
	            		restaurantOpinionPollStartingDate = null;
	            		if (!restaurantOpinionPollStartingDateString.isEmpty()) {	
	            			restaurantOpinionPollStartingDate = 
	            					DateManager.getDateValue(restaurantOpinionPollStartingDateString);
	            		}
	            		
	            		String restaurantOpinionPollEndingDateString = req.getParameter("op_end");
	            		restaurantOpinionPollEndingDate = null;
	            		if (!restaurantOpinionPollEndingDateString.isEmpty()) {	
	            			restaurantOpinionPollEndingDate = 
	            					DateManager.getDateValue(restaurantOpinionPollEndingDateString);
	            		}
                	}
            		
            		Integer restaurantOpinionPollPriority = 0;
            		
            		Boolean publicResults = parent.equalsIgnoreCase("Restaurant") ?
            				Boolean.parseBoolean(req.getParameter("op_public_results")) :
            					survey.resultsArePublic();

            		imageKey = BlobUtils.assignBlobKey(req, "op_image", blobstoreService);
                	
                    String binaryChoice1 = req.getParameter("op_binary1");
                    String binaryChoice2 = req.getParameter("op_binary2");
                    
                    String ratingLowValueString = req.getParameter("op_rating_lo");
                	Integer ratingLowValue = null;
                	if (ratingLowValueString != null) {
	                	if (!ratingLowValueString.isEmpty()) {
	                		ratingLowValue = Integer.parseInt(ratingLowValueString);
	                	}
                	}
                	
                	String ratingHighValueString = req.getParameter("op_rating_hi");
                	Integer ratingHighValue = null;
                	if (ratingHighValueString != null) {
	                	if (!ratingHighValueString.isEmpty()) {
	                		ratingHighValue = Integer.parseInt(ratingHighValueString);
	                	}
                	}
                	
                	ArrayList<OpinionPollRatingEntry> restaurantRatingEntries = 
                			new ArrayList<OpinionPollRatingEntry>();
                	ArrayList<SurveyOpinionPollRatingEntry> surveyRatingEntries = 
                			new ArrayList<SurveyOpinionPollRatingEntry>();
                	
                	String allowMultipleSelectionString = req.getParameter("op_allow_multi");
                	Boolean allowMultipleSelection = null;
                	if (allowMultipleSelectionString != null) {
	                	if (!allowMultipleSelectionString.isEmpty()) {
	                		allowMultipleSelection = Boolean.parseBoolean(allowMultipleSelectionString);
	                	}
                	}
                	
                	String multipleChoiceValuesString1 = req.getParameter("op_multi_values1");
                	String multipleChoiceValuesString2 = req.getParameter("op_multi_values2");
                	String multipleChoiceValuesString3 = req.getParameter("op_multi_values3");
                	String multipleChoiceValuesString4 = req.getParameter("op_multi_values4");
                	String multipleChoiceValuesString5 = req.getParameter("op_multi_values5");
                	String multipleChoiceValuesString6 = req.getParameter("op_multi_values6");
                	String multipleChoiceValuesString7 = req.getParameter("op_multi_values7");
                	String multipleChoiceValuesString8 = req.getParameter("op_multi_values8");
                	String multipleChoiceValuesString9 = req.getParameter("op_multi_values9");
                	String multipleChoiceValuesString10 = req.getParameter("op_multi_values10");
                	String multipleChoiceValuesString11 = req.getParameter("op_multi_values11");
                	String multipleChoiceValuesString12 = req.getParameter("op_multi_values12");
                	String multipleChoiceValuesString13 = req.getParameter("op_multi_values13");
                	String multipleChoiceValuesString14 = req.getParameter("op_multi_values14");
                	String multipleChoiceValuesString15 = req.getParameter("op_multi_values15");

                    try {
                    	
                    	if (parent.equalsIgnoreCase("Restaurant")) {
	                    	// Add multiple choice values
	                    	ArrayList<RestaurantOpinionPollMultipleChoiceValue> multipleChoiceValues = 
	                    			new ArrayList<RestaurantOpinionPollMultipleChoiceValue>();
	                    	if (multipleChoiceValuesString1 != null) {
	                    		if (!multipleChoiceValuesString1.isEmpty())
	                    			multipleChoiceValues.add(new RestaurantOpinionPollMultipleChoiceValue(multipleChoiceValuesString1));
	                    		if (!multipleChoiceValuesString2.isEmpty())
	                    			multipleChoiceValues.add(new RestaurantOpinionPollMultipleChoiceValue(multipleChoiceValuesString2));
	                    		if (!multipleChoiceValuesString3.isEmpty())
	                    			multipleChoiceValues.add(new RestaurantOpinionPollMultipleChoiceValue(multipleChoiceValuesString3));
	                    		if (!multipleChoiceValuesString4.isEmpty())
	                    			multipleChoiceValues.add(new RestaurantOpinionPollMultipleChoiceValue(multipleChoiceValuesString4));
	                    		if (!multipleChoiceValuesString5.isEmpty())
	                    			multipleChoiceValues.add(new RestaurantOpinionPollMultipleChoiceValue(multipleChoiceValuesString5));
	                    		if (!multipleChoiceValuesString6.isEmpty())
	                    			multipleChoiceValues.add(new RestaurantOpinionPollMultipleChoiceValue(multipleChoiceValuesString6));
	                    		if (!multipleChoiceValuesString7.isEmpty())
	                    			multipleChoiceValues.add(new RestaurantOpinionPollMultipleChoiceValue(multipleChoiceValuesString7));
	                    		if (!multipleChoiceValuesString8.isEmpty())
	                    			multipleChoiceValues.add(new RestaurantOpinionPollMultipleChoiceValue(multipleChoiceValuesString8));
	                    		if (!multipleChoiceValuesString9.isEmpty())
	                    			multipleChoiceValues.add(new RestaurantOpinionPollMultipleChoiceValue(multipleChoiceValuesString9));
	                    		if (!multipleChoiceValuesString10.isEmpty())
	                    			multipleChoiceValues.add(new RestaurantOpinionPollMultipleChoiceValue(multipleChoiceValuesString10));
	                    		if (!multipleChoiceValuesString11.isEmpty())
	                    			multipleChoiceValues.add(new RestaurantOpinionPollMultipleChoiceValue(multipleChoiceValuesString11));
	                    		if (!multipleChoiceValuesString12.isEmpty())
	                    			multipleChoiceValues.add(new RestaurantOpinionPollMultipleChoiceValue(multipleChoiceValuesString12));
	                    		if (!multipleChoiceValuesString13.isEmpty())
	                    			multipleChoiceValues.add(new RestaurantOpinionPollMultipleChoiceValue(multipleChoiceValuesString13));
	                    		if (!multipleChoiceValuesString14.isEmpty())
	                    			multipleChoiceValues.add(new RestaurantOpinionPollMultipleChoiceValue(multipleChoiceValuesString14));
	                    		if (!multipleChoiceValuesString15.isEmpty())
	                    			multipleChoiceValues.add(new RestaurantOpinionPollMultipleChoiceValue(multipleChoiceValuesString15));
	                    	}
	                    	
	                    	RestaurantOpinionPoll restaurantOpinionPoll = new RestaurantOpinionPoll(
	                    			restaurantOpinionPollType,
	                    			opinionPollTitle,
	                    			opinionPollContent,
	                    			restaurantOpinionPollStartingDate,
	                    			restaurantOpinionPollEndingDate,
	                    			restaurantOpinionPollPriority,
	                    			publicResults,
	                    			imageKey,
	                    			binaryChoice1,
	                    			binaryChoice2,
	                    			ratingLowValue,
	                    			ratingHighValue,
	                    			restaurantRatingEntries,
	                    			allowMultipleSelection,
	                    			multipleChoiceValues
	                    			);

	                    	RestaurantOpinionPollManager.putRestaurantOpinionPoll(
	                    			user.getUserEmail(), restaurantOpinionPoll);
                    	}
                    	else {
                    		// Add multiple choice values
	                    	ArrayList<SurveyOpinionPollMultipleChoiceValue> multipleChoiceValues = 
	                    			new ArrayList<SurveyOpinionPollMultipleChoiceValue>();
	                    	if (multipleChoiceValuesString1 != null) {
	                    		if (!multipleChoiceValuesString1.isEmpty())
	                    			multipleChoiceValues.add(new SurveyOpinionPollMultipleChoiceValue(multipleChoiceValuesString1));
	                    		if (!multipleChoiceValuesString2.isEmpty())
	                    			multipleChoiceValues.add(new SurveyOpinionPollMultipleChoiceValue(multipleChoiceValuesString2));
	                    		if (!multipleChoiceValuesString3.isEmpty())
	                    			multipleChoiceValues.add(new SurveyOpinionPollMultipleChoiceValue(multipleChoiceValuesString3));
	                    		if (!multipleChoiceValuesString4.isEmpty())
	                    			multipleChoiceValues.add(new SurveyOpinionPollMultipleChoiceValue(multipleChoiceValuesString4));
	                    		if (!multipleChoiceValuesString5.isEmpty())
	                    			multipleChoiceValues.add(new SurveyOpinionPollMultipleChoiceValue(multipleChoiceValuesString5));
	                    		if (!multipleChoiceValuesString6.isEmpty())
	                    			multipleChoiceValues.add(new SurveyOpinionPollMultipleChoiceValue(multipleChoiceValuesString6));
	                    		if (!multipleChoiceValuesString7.isEmpty())
	                    			multipleChoiceValues.add(new SurveyOpinionPollMultipleChoiceValue(multipleChoiceValuesString7));
	                    		if (!multipleChoiceValuesString8.isEmpty())
	                    			multipleChoiceValues.add(new SurveyOpinionPollMultipleChoiceValue(multipleChoiceValuesString8));
	                    		if (!multipleChoiceValuesString9.isEmpty())
	                    			multipleChoiceValues.add(new SurveyOpinionPollMultipleChoiceValue(multipleChoiceValuesString9));
	                    		if (!multipleChoiceValuesString10.isEmpty())
	                    			multipleChoiceValues.add(new SurveyOpinionPollMultipleChoiceValue(multipleChoiceValuesString10));
	                    		if (!multipleChoiceValuesString11.isEmpty())
	                    			multipleChoiceValues.add(new SurveyOpinionPollMultipleChoiceValue(multipleChoiceValuesString11));
	                    		if (!multipleChoiceValuesString12.isEmpty())
	                    			multipleChoiceValues.add(new SurveyOpinionPollMultipleChoiceValue(multipleChoiceValuesString12));
	                    		if (!multipleChoiceValuesString13.isEmpty())
	                    			multipleChoiceValues.add(new SurveyOpinionPollMultipleChoiceValue(multipleChoiceValuesString13));
	                    		if (!multipleChoiceValuesString14.isEmpty())
	                    			multipleChoiceValues.add(new SurveyOpinionPollMultipleChoiceValue(multipleChoiceValuesString14));
	                    		if (!multipleChoiceValuesString15.isEmpty())
	                    			multipleChoiceValues.add(new SurveyOpinionPollMultipleChoiceValue(multipleChoiceValuesString15));
	                    	}
	                    	
	                    	SurveyOpinionPoll surveyOpinionPoll = new SurveyOpinionPoll(
	                    			surveyOpinionPollType,
	                    			opinionPollTitle,
	                    			opinionPollContent,
	                    			binaryChoice1,
	                    			binaryChoice2,
	                    			ratingLowValue,
	                    			ratingHighValue,
	                    			surveyRatingEntries,
	                    			allowMultipleSelection,
	                    			multipleChoiceValues
	                    			);

	                    	SurveyOpinionPollManager.putSurveyOpinionPoll(
	                    			surveyKey, surveyOpinionPoll);
                    	}

                        if (keepAdding) {
                        	if (parent.equalsIgnoreCase("Restaurant")) {
	                        	resp.sendRedirect(addRestaurantOpinionPollJSP + 
	                        			"?msg=success&action=" + action);
                        	}
                        	else {
                        		resp.sendRedirect(addRestaurantOpinionPollJSP + 
	                        			"?sk=" + surveyKeyString + "&msg=success&action=" + action);
                        	}
                        }
                        else {
                        	if (parent.equalsIgnoreCase("Restaurant")) {
	                        	resp.sendRedirect(listRestaurantOpinionPollJSP + 
	                        			"?msg=success&action=" + action);
                        	}
                        	else {
                        		resp.sendRedirect(listRestaurantOpinionPollJSP + 
	                        			"?sk=" + surveyKeyString + "&msg=success&action=" + action);
                        	}
                        }
                    }
                    catch (MissingRequiredFieldsException mrfe) {
                    	if (imageKey != null)
                    		blobstoreService.delete(imageKey);
                    	if (parent.equalsIgnoreCase("Restaurant")) {
	                    	resp.sendRedirect(addRestaurantOpinionPollJSP + 
	                        		"?etype=MissingInfo");
                    	}
                    	else {
                    		resp.sendRedirect(addRestaurantOpinionPollJSP + 
	                        		"?sk=" + surveyKeyString + "&etype=MissingInfo");
                    	}
                        return;
                    }
                    catch (Exception ex) {
                    	if (imageKey != null)
                    		blobstoreService.delete(imageKey);
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                    
                    break;
                // Survey
                case 'S':
                	
                	// Check that the user is a Restaurant
                	if (user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                	
                	keepAddingString = req.getParameter("keep_adding");
                	keepAdding = keepAddingString == null ? false : true;
                	
                	String surveyTypeString = req.getParameter("sv_type");
                	Survey.Type surveyType = 
                			Survey.getSurveyTypeFromString(
                					surveyTypeString);
                	
                	String surveyTitle = req.getParameter("sv_title");
                	String surveyDescription = req.getParameter("sv_description");
                	
                	String validationCode = req.getParameter("sv_code");
                	
                	String surveyStartingDateString = req.getParameter("sv_start");
            		Date surveyStartingDate = null;
            		if (!surveyStartingDateString.isEmpty()) {	
            			surveyStartingDate = 
            					DateManager.getDateValue(surveyStartingDateString);
            		}
            		
            		String surveyEndingDateString = req.getParameter("sv_end");
            		Date surveyEndingDate = null;
            		if (!surveyEndingDateString.isEmpty()) {	
            			surveyEndingDate = 
            					DateManager.getDateValue(surveyEndingDateString);
            		}
            		
            		Integer surveyPriority = 0;
            		
            		publicResults = 
            				Boolean.parseBoolean(req.getParameter("sv_public_results"));

                    try {
                    	survey = new Survey(surveyType,
                    			surveyTitle,
                    			surveyDescription,
                    			validationCode,
                    			surveyStartingDate,
                    			surveyEndingDate,
                    			surveyPriority,
                    			publicResults
                    			);
                    	
                    	SurveyManager.putSurvey(user.getUserEmail(), survey);

                        if (keepAdding) {
                        	resp.sendRedirect(addSurveyJSP + 
                        			"?msg=success&action=" + action);
                        }
                        else {
                        	resp.sendRedirect(listSurveyJSP + 
                        			"?msg=success&action=" + action);
                        }
                    }
                    catch (MissingRequiredFieldsException mrfe) {
                        resp.sendRedirect(addSurveyJSP + 
                        		"?etype=MissingInfo");
                        return;
                    }
                    catch (Exception ex) {
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                    
                    break;
                // Messages
                case 'G':
                	
                	// Check that the user is a Restaurant
                	if (user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                	
                	keepAddingString = req.getParameter("keep_adding");
                	keepAdding = keepAddingString == null ? false : true;
                	
                	String messageTypeString = req.getParameter("m_type");
                	Message.MessageType messageType = 
                			Message.getMessageTypeFromString(messageTypeString);
                	
                	String messageTitle = req.getParameter("m_title");
                	String messageAuthor = req.getParameter("m_author");
                	
                	String messageTextContentString = req.getParameter("m_t_content");
                	Text messageTextContent = new Text(messageTextContentString);

            	    BlobKey messageMultimediaContentKey = BlobUtils.assignBlobKey(req, "m_m_content", blobstoreService);
                	
                	String messageStartingDateString = req.getParameter("m_start");
            		Date messageStartingDate = null;
            		if (!messageStartingDateString.isEmpty()) {	
            			messageStartingDate = DateManager.getDateValue(messageStartingDateString);
            		}
            		
            		// If a file was uploaded, then the URL will be the link to the file download link
            		String messageURLString;
            		if (messageMultimediaContentKey == null) {
            			messageURLString = req.getParameter("m_url");
            		}
            		else {
            			String urlLocation = req.getParameter("url_location");
            			messageURLString = urlLocation +
            					"/fileDownload?file_id=" +
            					messageMultimediaContentKey.getKeyString();
            		}
            		Link messageURL = new Link(messageURLString);
            		
            		String messageEndingDateString = req.getParameter("m_end");
            		Date messageEndingDate = null;
            		if (!messageEndingDateString.isEmpty()) {	
            			messageEndingDate = DateManager.getDateValue(messageEndingDateString);
            		}
                	
                    try {          
                    	Message message = new Message(
                    			messageType,
                    			messageTitle,
                    			messageAuthor,
                    			messageTextContent,
                    			messageMultimediaContentKey,
                    			messageURL,
                    			messageStartingDate,
                    			messageEndingDate
                    			);

                        MessageManager.putMessage(user.getUserEmail(), message);
                        
                        if (keepAdding) {
                        	resp.sendRedirect(addMessagesJSP + "?msg=success&action=" + action);
                        }
                        else {
                        	resp.sendRedirect(listMessagesJSP + "?msg=success&action=" + action);
                        }
                    }
                    catch (MissingRequiredFieldsException mrfe) {
                    	if (messageMultimediaContentKey != null)
                    		blobstoreService.delete(messageMultimediaContentKey);
                        resp.sendRedirect(addMessagesJSP + "?etype=MissingInfo");
                        return;
                    }
                    catch (Exception ex) {
                    	if (messageMultimediaContentKey != null)
                    		blobstoreService.delete(messageMultimediaContentKey);
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                    
                    break;
                default:
                    assert(false); // there are no other types
            }
        }
        else if (action.equals("update")) {
            switch (req.getParameter("type").charAt(0)) {
            	// Menu Item Types
                case 'T':
                	
                	// Check that the user is a Restaurant
                	if (user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                    
                	String menuItemTypeKeyString = req.getParameter("k");
                	Key menuItemTypeKey = KeyFactory.stringToKey(menuItemTypeKeyString);
                	String menuItemTypeName = req.getParameter("mit_name");
                	String menuItemTypeDescription = req.getParameter("mit_description");
                	
                    try {                
                        MenuItemTypeManager.updateMenuItemTypeAttributes(
                                menuItemTypeKey,
                                menuItemTypeName,
                                menuItemTypeDescription);
                        
                        resp.sendRedirect(editMenuItemTypeJSP + "?k=" + 
                        		menuItemTypeKeyString + 
                        		"&readonly=true&msg=success&action=" + action);
                    } 
                    catch (MissingRequiredFieldsException mrfe) {
                        resp.sendRedirect(editMenuItemTypeJSP + "?etype=MissingInfo&k="
                                + menuItemTypeKeyString);
                        return;
                    } 
                    catch (Exception ex) {
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                    
                    return;
                // Menu Items
                case 'I':
                	
                	// Check that the user is a Restaurant
                	if (user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                    
                	String menuItemKeyString = req.getParameter("k");
                    Key menuItemKey = KeyFactory.stringToKey(menuItemKeyString);
                    MenuItem menuItem = MenuItemManager.getMenuItem(menuItemKey);
                    
                    Key originalProductItemType = menuItem.getMenuItemType();
                    
                    String menuItemTypeString = req.getParameter("mit_id");
            		Key menuItemType = null;
            		if (!menuItemTypeString.isEmpty()) {
            			menuItemType = KeyFactory.stringToKey(menuItemTypeString);
            		}
            		
            		String menuItemName = req.getParameter("mi_name");
            		
            		String priceString = req.getParameter("mi_price");
            		Double price = null;
            		if (!priceString.isEmpty()) {
            			price = Double.parseDouble(priceString);
            		}
            		
            		String discountString = req.getParameter("mi_discount");
            		Double discount = null;
            		if (!discountString.isEmpty()) {
            			discount = Double.parseDouble(discountString);
            		}
            		
            		String menuItemDescription = req.getParameter("mi_description");

                    BlobKey imageKey = BlobUtils.assignBlobKey(req, "mi_image", blobstoreService);
                    boolean sameImage = false;
                    if (imageKey == null) {
                    	imageKey = menuItem.getMenuItemImage();
                    	sameImage = true;
                    	log.info("No image uploaded in menuItem \"" + menuItem.getMenuItemName() +
    						"\". Using previous image.");
                    }
                    
                    String servingTimeString = req.getParameter("mi_serving_time");
            		Integer servingTime = null;
            		if (!servingTimeString.isEmpty()) {
            			servingTime = Integer.parseInt(servingTimeString);
            		}
            		
            		Boolean isAvailable = req.getParameter("mi_isavailable").charAt(0)
                        	== 'Y' ? true : false;
            		
            		String serviceTimeString = req.getParameter("mi_service_time");
            		Integer serviceTime = null;
            		if (!serviceTimeString.isEmpty()) {
            			serviceTime = Integer.parseInt(serviceTimeString);
            		}
            		
            		String menuItemComments = req.getParameter("mi_comments");
                    
                    try {                
                        MenuItemManager.updateMenuItemAttributes(menuItemKey, 
                        		menuItemType, 
                        		menuItemName, 
                        		price,
                        		discount,
                                menuItemDescription, 
                                imageKey,
                                servingTime,
                                isAvailable,
                                serviceTime,
                                menuItemComments);
                        
                        MenuItemTypeManager.updateProductItemTypeVersion(menuItemType);
                        /* Update original product item type version only if the product item
                         * only if the product item type changed
                         */
                        if (!originalProductItemType.equals(menuItemType)) {
                        	MenuItemTypeManager.updateProductItemTypeVersion(originalProductItemType);
                        }
                        
                        resp.sendRedirect(editMenuItemJSP + "?k=" + menuItemKeyString
                        		+ "&readonly=true&msg=success&action=" + action);
                        
                    }
                    catch (MissingRequiredFieldsException mrfe) {
                    	if (!sameImage) {
                    		blobstoreService.delete(imageKey);
                    	}
                        resp.sendRedirect(editMenuItemJSP + "?etype=MissingInfo&k=" +
                        		menuItemKeyString);
                        return;
                    }
                    catch (InvalidFieldFormatException iffe) {
                    	if (!sameImage) {
                    		blobstoreService.delete(imageKey);
                    	}
                        resp.sendRedirect(editMenuItemJSP + "?etype=Format&k=" +
                        		menuItemKeyString);
                        return;
                    }
                    catch (Exception ex) {
                    	if (!sameImage) {
                    		blobstoreService.delete(imageKey);
                    	}
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                    
                    return;
                // Sets
                case 'E':
                	
                	// Check that the user is a Restaurant
                	if (user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                    
                	String setKeyString = req.getParameter("k");
                    Key setKey = KeyFactory.stringToKey(setKeyString);
                    Set set = SetManager.getSet(setKey);
                	
                    String setNumberString = req.getParameter("set_number");
                    Integer setNumber = null;
                    if (!setNumberString.isEmpty()) {
                    	setNumber = Integer.parseInt(setNumberString);
                    }
                    
                    String setTypeString = req.getParameter("set_type");
                    Set.SetType setType = Set.getSetTypeFromString(setTypeString);
                    
                    String setName = req.getParameter("set_name");
                    
                    String setDescription = req.getParameter("set_description");
                    
                    String setPriceString = req.getParameter("set_price");
            		Double setPrice = null;
            		if (setPriceString != null) {
	            		if (!setPriceString.isEmpty()) {
	            			setPrice = Double.parseDouble(setPriceString);
	            		}
            		}
            		
            		//String hasFixedPriceString = req.getParameter("set_fixed_price");
            		//Boolean hasFixedPrice = Boolean.parseBoolean(hasFixedPriceString);
            		// TODO: Change later
            		Boolean hasFixedPrice = true;
            		
            		String setDiscountString = req.getParameter("set_discount");
            		Double setDiscount = null;
            		if (!setDiscountString.isEmpty()) {
            			setDiscount = Double.parseDouble(setDiscountString);
            		}

                    imageKey = BlobUtils.assignBlobKey(req, "set_image", blobstoreService);
                    sameImage = false;
                    if (imageKey == null) {
                    	imageKey = set.getSetImage();
                    	sameImage = true;
                    	log.info("No image uploaded in set \"" + set.getSetName() +
    						"\". Using previous image.");
                    }
                    
            		servingTimeString = req.getParameter("set_serving_time");
            		servingTime = null;
            		if (!servingTimeString.isEmpty()) {
            			servingTime = Integer.parseInt(servingTimeString);
            		}
            		
            		isAvailable = req.getParameter("set_is_available").charAt(0)
                            == 'Y' ? true : false;
            		
            		serviceTimeString = req.getParameter("set_service_time");
            		serviceTime = null;
            		if (!serviceTimeString.isEmpty()) {
            			serviceTime = Integer.parseInt(serviceTimeString);
            		}
            		
            		String setComments = req.getParameter("set_comments");
            		
            		// Menu Item Keys
            		ArrayList<Key> menuItemKeys = new ArrayList<Key>();
            		ArrayList<TypeSetMenuItem> typeSetMenuItems = new ArrayList<TypeSetMenuItem>();
            		if (setType == Set.SetType.FIXED_SET) {
            			for (int i = 1; i <= 25; i++) {
            				menuItemKeyString = req.getParameter("mi_id_tf_" + i);
            				
            				if (!menuItemKeyString.equals("none")) {
            					menuItemKey = KeyFactory.stringToKey(menuItemKeyString);
            					int amount = Integer.parseInt(req.getParameter("no_product_items_" + i));
            					for (int j = 0; j < amount; j++) {
            						menuItemKeys.add(menuItemKey);
            					}
            				}
            			}
            		}
            		else if (setType == Set.SetType.TYPE_SET) {
            			for (int i = 1; i <= 25; i++) {
            				
            				// Check if a menu item type was selected
            				menuItemTypeKeyString = req.getParameter("mit_tt_" + i);
            				if (!menuItemTypeKeyString.equals("none")) {
	            				
            					// Check if all menu items for this type were selected
            					boolean includeAllItems = Boolean.parseBoolean(req.getParameter("set_tt_fixed_" + i));
            					if (includeAllItems) {
            						menuItemType = KeyFactory.stringToKey(menuItemTypeKeyString);
            						List<MenuItem> menuItems = MenuItemManager.getAllMenuItemsByType(menuItemType);
            						
            						for (MenuItem item : menuItems) {
	            						try {
			            					TypeSetMenuItem typeSetMenuItem = new TypeSetMenuItem(
			            							item.getKey(),
			            							item.getMenuItemPrice()
			            							);
			            					
			            					typeSetMenuItems.add(typeSetMenuItem);
		            					}
		            					catch (MissingRequiredFieldsException mrfe) {
		                                	if (imageKey != null)
		                                		blobstoreService.delete(imageKey);
		                                    resp.sendRedirect(addSetsJSP + "?etype=MissingInfo");
		                                    return;
		                                }
		            					catch (Exception ex) {
		                                	if (imageKey != null)
		                                		blobstoreService.delete(imageKey);
		                                    log.log(Level.SEVERE, ex.toString());
		                                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		                                }
            						}
            					}
            					// If only some menu items were selected
            					else {
	            					for (int j = 1; j <= 10; j++) {
		            				
			            				menuItemKeyString = req.getParameter("mi_id_tt_" + ((i * 100) + j));
			            				
			            				// Check if a menu item was selected
			            				if (!menuItemKeyString.equals("none")) {
			            					
			            					menuItemKey = KeyFactory.stringToKey(menuItemKeyString);
			            					
			            					// Check if a specific price was given to the menu item
			            					priceString = req.getParameter("set_tt_mi_" + ((i * 100) + j) + "_price");
			            					price = null;
			            					if (priceString != null) {
				            					if (!priceString.isEmpty()) {
				            						price = Double.parseDouble(priceString);
				            					}
			            					}
			            					else {
			            						menuItem = MenuItemManager.getMenuItem(menuItemKey);
			            						price = menuItem.getMenuItemPrice();
			            					}
			            					
			            					try {
				            					TypeSetMenuItem typeSetMenuItem = new TypeSetMenuItem(
				            							menuItemKey,
				            							price
				            							);
				            					
				            					typeSetMenuItems.add(typeSetMenuItem);
			            					}
			            					catch (MissingRequiredFieldsException mrfe) {
			                                	if (imageKey != null)
			                                		blobstoreService.delete(imageKey);
			                                    resp.sendRedirect(addSetsJSP + "?etype=MissingInfo");
			                                    return;
			                                }
			            					catch (Exception ex) {
			                                	if (imageKey != null)
			                                		blobstoreService.delete(imageKey);
			                                    log.log(Level.SEVERE, ex.toString());
			                                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			                                }
			            				}
		            				}
            					}
            				}
            			}
            		}
                                                           
                    try {
                        SetManager.updateSetAttributes(
                        		setKey, 
                        		setNumber, 
                        		setName, 
                        		setDescription, 
                        		setPrice, 
                        		hasFixedPrice, 
                        		setDiscount, 
                        		imageKey, 
                        		servingTime, 
                        		isAvailable, 
                        		serviceTime, 
                        		setComments, 
                        		menuItemKeys, 
                        		typeSetMenuItems);
                        
                        resp.sendRedirect(editSetsJSP + "?k=" + 
                        setKeyString + "&readonly=true&msg=success&action=" + action);
                        
                    } 
                    catch (MissingRequiredFieldsException mrfe) {
                    	if (!sameImage) {
                    		blobstoreService.delete(imageKey);
                    	}
                        resp.sendRedirect(editSetsJSP + "?etype=MissingInfo&k=" +
                        		setKeyString);
                        return;
                    }
                    catch (InvalidFieldFormatException iffe) {
                    	if (!sameImage) {
                    		blobstoreService.delete(imageKey);
                    	}
                        resp.sendRedirect(editSetsJSP + "?etype=Format&k=" +
                        		setKeyString);
                        return;
                    }
                    catch (Exception ex) {
                    	if (!sameImage) {
                    		blobstoreService.delete(imageKey);
                    	}
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                    
                    return;
                // Additional Property Types
                case 'P':
                	
                	// Check that the user is a Restaurant
                	if (user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                    
                	String additionalPropertyTypeKeyString = req.getParameter("k");
                	Key additionalPropertyTypeKey = KeyFactory.stringToKey(additionalPropertyTypeKeyString);
                	String additionalPropertyTypeName = req.getParameter("apt_name");
                	String additionalPropertyTypeDescription = req.getParameter("apt_description");
                	
                    try {                
                    	AdditionalPropertyTypeManager.updateAdditionalPropertyTypeAttributes(
                    			additionalPropertyTypeKey,
                    			additionalPropertyTypeName,
                    			additionalPropertyTypeDescription);
                        
                        resp.sendRedirect(editAdditionalPropertyTypeJSP + "?k=" + additionalPropertyTypeKeyString
                        		+ "&readonly=true&msg=success&action=" + action);
                    }
                    catch (MissingRequiredFieldsException mrfe) {
                        resp.sendRedirect(editAdditionalPropertyTypeJSP + "?etype=MissingInfo&k="
                                + additionalPropertyTypeKeyString);
                        return;
                    } 
                    catch (Exception ex) {
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }

                    return;
                // Additional Property Values
                case 'V':
                	
                	// Check that the user is a Restaurant
                	if (user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                    
                	String additionalPropertyValueKeyString = req.getParameter("k");
                	Key additionalPropertyValueKey = KeyFactory.stringToKey(additionalPropertyValueKeyString);
                	String additionalPropertyValueName = req.getParameter("apv_name");
                	
                    try {                
                    	AdditionalPropertyValueManager.updateAdditionalPropertyValueAttributes(
                    			additionalPropertyValueKey,
                    			additionalPropertyValueName);
                        
                        resp.sendRedirect(editAdditionalPropertyValueJSP + "?k=" + 
                        		additionalPropertyValueKeyString + "&readonly=true&msg=success&action=" + action);
                    }
                    catch (MissingRequiredFieldsException mrfe) {
                        resp.sendRedirect(editAdditionalPropertyValueJSP + "?etype=MissingInfo&k="
                                + additionalPropertyValueKeyString);
                        return;
                    } 
                    catch (Exception ex) {
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }

                    return;
                // Menu Item Additional Property Values
                case 'M':
                	
                	// Check that the user is a Restaurant
                	if (user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                    
                	menuItemKeyString = req.getParameter("mi_k");
                	menuItemKey = KeyFactory.stringToKey(menuItemKeyString);
                	
                	int count = Integer.parseInt(req.getParameter("count"));
                		
                	ArrayList<Key> additionalPropertyValueKeys = new ArrayList<Key>();
                	ArrayList<Double> additionalCharges = new ArrayList<Double>();
                	// Parse menu item additional property values
                	for (int i = 0 ; i < count; i++) {
                		if (req.getParameter("apv_checked" + i) != null) {
                			
                			additionalPropertyValueKeyString = req.getParameter("apv_k" + i);
                			additionalPropertyValueKey = KeyFactory.stringToKey(additionalPropertyValueKeyString);
                			additionalPropertyValueKeys.add(additionalPropertyValueKey);
                			
                			String additionalChargeString = req.getParameter("apv_price" + i);
                			Double additionalCharge = null;
                			if (!additionalChargeString.isEmpty()) {
                				additionalCharge = Double.parseDouble(additionalChargeString);
                			}
                			additionalCharges.add(additionalCharge);
                		}
                	}
                	
                    try {
                    	
                    	// Add menu item additional property values
                    	ArrayList<MenuItemAdditionalPropertyValue> menuItemAdditionalPropertyValues = 
                    			new ArrayList<MenuItemAdditionalPropertyValue>();
                    	for (int i = 0; i < additionalPropertyValueKeys.size(); i++) {
                    		MenuItemAdditionalPropertyValue miapv = new MenuItemAdditionalPropertyValue(
                    				additionalPropertyValueKeys.get(i),
                    				additionalCharges.get(i));
                    		menuItemAdditionalPropertyValues.add(miapv);
                    	}
                    	
                    	MenuItemAdditionalPropertyValueManager.setMenuItemAdditionalPropertyValues(
                    			menuItemKey, 
                    			menuItemAdditionalPropertyValues);
                        
                        resp.sendRedirect(editProductItemPropertyJSP + "?mi_k=" + 
                        		menuItemKeyString + "&readonly=true&msg=success&action=" + action);
                    }
                    catch (Exception ex) {
                    	ex.printStackTrace();
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }

                    return;
                // Additional Property Constraints
                case 'C':
                	
                	// Check that the user is a Restaurant
                	if (user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                    
                	String menuItemAdditionalPropertyValueKeyString = req.getParameter("miapv_k");
                	Key menuItemAdditionalPropertyValueKey = 
                			KeyFactory.stringToKey(menuItemAdditionalPropertyValueKeyString);
                	
                	menuItemKey = menuItemAdditionalPropertyValueKey.getParent();
                	
                	count = Integer.parseInt(req.getParameter("count"));
                	
                	ArrayList<Key> menuItemAdditionalPropertyValueKeys = new ArrayList<Key>();
                	ArrayList<Key> menuItemAdditionalPropertyValueKeysToDelete = new ArrayList<Key>();
                	// Parse menu item additional property values
                	for (int i = 0 ; i < count; i++) {
                		String miapvKeyString = req.getParameter("miapv_k" + i);
            			Key miapvKey = KeyFactory.stringToKey(miapvKeyString);
                		if (req.getParameter("apc_checked" + i) != null) {
                			menuItemAdditionalPropertyValueKeys.add(miapvKey);
                		}
                		else {
                			menuItemAdditionalPropertyValueKeysToDelete.add(miapvKey);
                		}
                	}
                	
                    try {
                    	
                    	// Add menu item additional property constraints
                    	ArrayList<AdditionalPropertyConstraint> additionalPropertyConstraints = 
                    			new ArrayList<AdditionalPropertyConstraint>();
                    	for (int i = 0; i < menuItemAdditionalPropertyValueKeys.size(); i++) {
                    		
                    		AdditionalPropertyConstraint additionalPropertyConstraint =
                    				new AdditionalPropertyConstraint(
                    						menuItemAdditionalPropertyValueKey,
                    						menuItemAdditionalPropertyValueKeys.get(i)
                    						);
                    		
                    		additionalPropertyConstraints.add(additionalPropertyConstraint);
                    	}
                    	
                    	AdditionalPropertyConstraintManager.putAdditionalPropertyConstraints(
                    			menuItemKey,
                    			additionalPropertyConstraints);
                    	
                    	// Delete menu item additional property constraints not selected
                    	for (Key miapvKeyToDelete : menuItemAdditionalPropertyValueKeysToDelete) {
                    		AdditionalPropertyConstraintManager.deleteAdditionalPropertyConstraints(
                    				menuItemAdditionalPropertyValueKey, 
                    				miapvKeyToDelete);
                    	}
                        
                        resp.sendRedirect(editProductItemPropertyConstraintsJSP + "?miapv_k=" + 
                        		menuItemAdditionalPropertyValueKeyString + 
                        		"&msg=success&action=" + action);
                    }
                    catch (Exception ex) {
                    	ex.printStackTrace();
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }

                    return;
                // Branches
                case 'B':
                	
                	// Check the user type carrying out the action
                	if (user.getUserType() != User.Type.ADMINISTRATOR && user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                	
                	String branchKeyString = req.getParameter("k");
                    Key branchKey = KeyFactory.stringToKey(branchKeyString);
                    
                    //Key restaurantKey = branchKey.getParent();
                    //String restaurantKeyString = KeyFactory.keyToString(restaurantKey);
                    
                    String regionString = req.getParameter("b_region");
            		Long region = null;
            		if (!regionString.isEmpty()) {
            			region = Long.parseLong(regionString);
            		}
                    
                    String branchName = req.getParameter("b_name");
                    
                    String branchAddress1 = req.getParameter("b_address1");
            		String branchAddress2 = req.getParameter("b_address2");
            		PostalAddress address;
            		if (branchAddress2.trim().isEmpty()) {
            			address = new PostalAddress(branchAddress1);
            		}
            		else {
            			address = new PostalAddress(
            					branchAddress1 + " " + branchAddress2);
            		}
                    
                    PhoneNumber branchPhoneNumber = new PhoneNumber(req.getParameter("b_phone"));
                    
                    Boolean hasDelivery = Boolean.parseBoolean(req.getParameter("has_delivery"));
                    
                    String hasRegularDeliveryString = req.getParameter("has_regular_delivery");
                    Boolean hasRegularDelivery = null;
                    if (hasRegularDeliveryString != null) {
                    	hasRegularDelivery = Boolean.parseBoolean(hasRegularDeliveryString);
                    }
                    
                    String hasPostalDeliveryString = req.getParameter("has_postal_delivery");
                    Boolean hasPostalDelivery = null;
                    if (hasPostalDeliveryString != null) {
                    	hasPostalDelivery = Boolean.parseBoolean(hasPostalDeliveryString);
                    }
                    
                    String hasUPSDeliveryString = req.getParameter("has_ups_delivery");
                    Boolean hasUPSDelivery = null;
                    if (hasUPSDeliveryString != null) {
                    	hasUPSDelivery = Boolean.parseBoolean(hasUPSDeliveryString);
                    }
                    
                    String hasConvenienceStoreDeliveryString = req.getParameter("has_cs_delivery");
                    Boolean hasConvenienceStoreDelivery = null;
                    if (hasConvenienceStoreDeliveryString != null) {
                    	hasConvenienceStoreDelivery = 
                    			Boolean.parseBoolean(hasConvenienceStoreDeliveryString);
                    }
                    
                    Boolean hasTakeOut = Boolean.parseBoolean(req.getParameter("has_takeout"));
                    Boolean hasTakeIn = Boolean.parseBoolean(req.getParameter("has_takein"));
                    
                    Email branchEmail = new Email(req.getParameter("b_email"));
                    
                    try {                
                        BranchManager.updateBranchAttributes(branchKey, 
                        		region,
                        		branchName,
                        		address,
                        		branchPhoneNumber,
                        		hasDelivery,
                        		hasRegularDelivery,
                        		hasPostalDelivery,
                        		hasUPSDelivery,
                        		hasConvenienceStoreDelivery,
                        		hasTakeOut,
                        		hasTakeIn,
                        		branchEmail);
                        SystemManager.updateRestaurantListVersion();
                        
                        resp.sendRedirect(editBranchJSP + "?k=" + branchKeyString +
                        		"&readonly=true&msg=success&action=" + action);

                    }
                    catch (MissingRequiredFieldsException mrfe) {
                    	resp.sendRedirect(editBranchJSP + "?etype=MissingInfo&k=" +
                    			branchKeyString);
                        return;
                    }
                    catch (InvalidFieldFormatException iffe) {
                    	resp.sendRedirect(editBranchJSP + "?etype=Format&k=" +
                    			branchKeyString);
                        return;
                    }
                    catch (InvalidFieldSelectionException ifse) {
                    	resp.sendRedirect(editBranchJSP + "?etype=Selection&k=" +
                    			branchKeyString);
                        return;
                    }
                    catch (Exception ex) {
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                    break;
                // Restaurant News
                case 'N':
                	
                	String restaurantNewsKeyString = req.getParameter("k");
                    Key restaurantNewsKey = KeyFactory.stringToKey(restaurantNewsKeyString);
                    RestaurantNews restaurantNews = RestaurantNewsManager.getRestaurantNews(restaurantNewsKey);
                    
                	// Check the user type carrying out the action
                	if (user.getUserType() != User.Type.ADMINISTRATOR && user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                	
                	String restaurantNewsTitle = req.getParameter("rn_title");
                	String restaurantNewsContent = req.getParameter("rn_content");
                	
                	String maxClicksString = req.getParameter("max_clicks");
                	Integer maxClicks = null;
                	if (maxClicksString != null) {
	                	if (!maxClicksString.isEmpty()) {
	                		maxClicks = Integer.parseInt(maxClicksString);
	                	}
                	}
                	else {
                		maxClicks = Integer.MAX_VALUE;
                	}
                	
                	String restaurantNewsStartingDateString = req.getParameter("rn_start");
            		Date restaurantNewsStartingDate = null;
            		if (!restaurantNewsStartingDateString.isEmpty()) {	
            			restaurantNewsStartingDate = DateManager.getDateValue(restaurantNewsStartingDateString);
            		}
            		
            		String restaurantNewsEndingDateString = req.getParameter("rn_end");
            		Date restaurantNewsEndingDate = null;
            		if (!restaurantNewsEndingDateString.isEmpty()) {	
            			restaurantNewsEndingDate = DateManager.getDateValue(restaurantNewsEndingDateString);
            		}
            		
            		String restaurantNewsPriorityString = req.getParameter("rn_priority");
            		Integer restaurantNewsPriority = null;
                	if (restaurantNewsPriorityString != null) {
	                	if (!restaurantNewsPriorityString.isEmpty()) {
	                		restaurantNewsPriority = Integer.parseInt(restaurantNewsPriorityString);
	                	}
                	}
                	else {
                		restaurantNewsPriority = restaurantNews.getRestaurantNewsPriority();
                	}
                	
                	Boolean isPrivate = req.getParameter("rn_is_private") != null ?
            				Boolean.parseBoolean(req.getParameter("rn_is_private")) :
            					restaurantNews.isPrivate();
            		
                	sameImage = true;
                	if (user.getUserType() == User.Type.RESTAURANT) {
	                    imageKey = BlobUtils.assignBlobKey(req, "rn_image", blobstoreService);
	                    sameImage = false;
	                    if (imageKey == null) {
	                    	imageKey = restaurantNews.getRestaurantNewsImage();
	                    	sameImage = true;
	                    	log.info("No image uploaded in restaurant news \"" + restaurantNews.getRestaurantNewsTitle() +
	    						"\". Using previous image.");
	                    }
                	}
                	else {
                		imageKey = restaurantNews.getRestaurantNewsImage();
                	}
                	
                    try {          
                    	RestaurantNewsManager.updateRestaurantNewsAttributes(
                    			restaurantNewsKey, 
                    			restaurantNewsTitle, 
                    			restaurantNewsContent, 
                    			maxClicks, 
                    			restaurantNewsStartingDate, 
                    			restaurantNewsEndingDate, 
                    			restaurantNewsPriority, 
                    			isPrivate,
                    			imageKey);
                        
                    	resp.sendRedirect(editRestaurantNewsJSP + "?k=" + 
                    						restaurantNewsKeyString +
                                		"&readonly=true&msg=success&action=" + action);
                    }
                    catch (MissingRequiredFieldsException mrfe) {
                    	if (!sameImage) {
                    		blobstoreService.delete(imageKey);
                    	}
                        resp.sendRedirect(editRestaurantNewsJSP + "?etype=MissingInfo&k=" +
                        		restaurantNewsKeyString);
                        return;
                    }
                    catch (Exception ex) {
                    	if (!sameImage) {
                    		blobstoreService.delete(imageKey);
                    	}
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        ex.printStackTrace();
                    }
                    
                    break;
                // Opinion Polls
                case 'L':
                	
                	String opinionPollKeyString = req.getParameter("k");
                    Key opinionPollKey = KeyFactory.stringToKey(opinionPollKeyString);
                    String parent = opinionPollKey.getParent().getKind();
                    
                    RestaurantOpinionPoll restaurantOpinionPoll = parent.equalsIgnoreCase("Restaurant") ?
                    		RestaurantOpinionPollManager.getRestaurantOpinionPoll(opinionPollKey) : null;
                    SurveyOpinionPoll surveyOpinionPoll = parent.equalsIgnoreCase("Survey") ?
                            SurveyOpinionPollManager.getSurveyOpinionPoll(opinionPollKey) : null;
                	
                	// Check the user type carrying out the action
                	if (user.getUserType() != User.Type.ADMINISTRATOR && user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                	
                	String opinionPollTitle = req.getParameter("op_title");
                	String opinionPollContent = req.getParameter("op_content");
                	
            		Date restaurantOpinionPollStartingDate = null;
            		Date restaurantOpinionPollEndingDate = null;
            		Boolean publicResults = null;
            		Integer restaurantOpinionPollPriority = null;
                	if (parent.equalsIgnoreCase("Restaurant")) {
	                	String restaurantOpinionPollStartingDateString = req.getParameter("op_start");
	            		if (!restaurantOpinionPollStartingDateString.isEmpty()) {	
	            			restaurantOpinionPollStartingDate = 
	            					DateManager.getDateValue(restaurantOpinionPollStartingDateString);
	            		}

	            		String restaurantOpinionPollEndingDateString = req.getParameter("op_end");
	            		if (!restaurantOpinionPollEndingDateString.isEmpty()) {	
	            			restaurantOpinionPollEndingDate = 
	            					DateManager.getDateValue(restaurantOpinionPollEndingDateString);
	            		}
	            		
	                	publicResults = req.getParameter("op_public_results") != null ?
	            				Boolean.parseBoolean(req.getParameter("op_public_results")) :
	            					restaurantOpinionPoll.resultsArePublic();
	            				
                		String restaurantOpinionPollPriorityString = req.getParameter("op_priority");
    	                if (!restaurantOpinionPollPriorityString.isEmpty()) {
    	                	restaurantOpinionPollPriority = 
    	                			Integer.parseInt(restaurantOpinionPollPriorityString);
    	                }
                	}
            		
                	sameImage = true;
                	imageKey = null;
                	if (parent.equalsIgnoreCase("Restaurant")) {
	                	if (user.getUserType() == User.Type.RESTAURANT) {
		                    imageKey = BlobUtils.assignBlobKey(req, "op_image", blobstoreService);		                    
		                    sameImage = false;
		                    if (imageKey == null) {
		                    	imageKey = restaurantOpinionPoll.getRestaurantOpinionPollImage();
		                    	sameImage = true;
		                    	log.info("No image uploaded in restaurant opinion poll \"" + 
		                    			restaurantOpinionPoll.getRestaurantOpinionPollTitle() +
		                    			"\". Using previous image.");
		                    }
	                	}
	                	else {
	                		imageKey = restaurantOpinionPoll.getRestaurantOpinionPollImage();
	                	}
                	}
	                	
                    String binaryChoice1 = req.getParameter("op_binary1");
                    String binaryChoice2 = req.getParameter("op_binary2");
                    
                    String ratingLowValueString = req.getParameter("op_rating_lo");
                	Integer ratingLowValue = null;
                	if (ratingLowValueString != null) {
	                	if (!ratingLowValueString.isEmpty()) {
	                		ratingLowValue = Integer.parseInt(ratingLowValueString);
	                	}
                	}
                	
                	String ratingHighValueString = req.getParameter("op_rating_hi");
                	Integer ratingHighValue = null;
                	if (ratingHighValueString != null) {
	                	if (!ratingHighValueString.isEmpty()) {
	                		ratingHighValue = Integer.parseInt(ratingHighValueString);
	                	}
                	}
                	
                	String allowMultipleSelectionString = req.getParameter("op_allow_multi");
                	Boolean allowMultipleSelection = null;
                	if (allowMultipleSelectionString != null) {
	                	if (!allowMultipleSelectionString.isEmpty()) {
	                		allowMultipleSelection = Boolean.parseBoolean(allowMultipleSelectionString);
	                	}
                	}
                	else {
                		if (parent.equalsIgnoreCase("Restaurant")) {
                			allowMultipleSelection = restaurantOpinionPoll.allowsMultipleSelection();
                		}
                		else {
                			allowMultipleSelection = surveyOpinionPoll.allowsMultipleSelection();
                		}
                	}
                	
                    try {
                    	
                    	if (parent.equalsIgnoreCase("Restaurant")) {
                    	
	                    	RestaurantOpinionPollManager.updateRestaurantOpinionPollAttributes(
	                    			opinionPollKey, 
	                    			opinionPollTitle, 
	                    			opinionPollContent, 
	                    			restaurantOpinionPollStartingDate, 
	                    			restaurantOpinionPollEndingDate, 
	                    			restaurantOpinionPollPriority,
	                    			publicResults,
	                    			imageKey,
	                    			binaryChoice1,
	                    			binaryChoice2,
	                    			ratingLowValue,
	                    			ratingHighValue,
	                    			allowMultipleSelection
	                    			);

	            			resp.sendRedirect(editRestaurantOpinionPollJSP +
		                    		"?k=" + opinionPollKeyString + 
		                			"&readonly=true&msg=success&action=" + action);
                    	}
                    	else {
                    		SurveyOpinionPollManager.updateSurveyOpinionPollAttributes(
                    				opinionPollKey, 
                    				opinionPollTitle, 
                    				opinionPollContent, 
                    				binaryChoice1, 
                    				binaryChoice2, 
                    				ratingLowValue, 
                    				ratingHighValue, 
                    				allowMultipleSelection);
                    		
	                    	resp.sendRedirect(editRestaurantOpinionPollJSP +
	                    			"?k=" + opinionPollKeyString + "&sk=" + KeyFactory.keyToString(opinionPollKey.getParent()) + 
	                				"&readonly=true&msg=success&action=" + action);
                    	}
                        
                    }
                    catch (MissingRequiredFieldsException mrfe) {
                    	if (!sameImage) {
                    		blobstoreService.delete(imageKey);
                    	}
                    	if (parent.equalsIgnoreCase("Restaurant")) {
	                        resp.sendRedirect(editRestaurantOpinionPollJSP + "?etype=MissingInfo&k=" +
	                        		opinionPollKeyString);
                    	}
                    	else {
                    		resp.sendRedirect(editRestaurantOpinionPollJSP + "?etype=MissingInfo&k=" +
	                        		opinionPollKeyString + "&sk=" + KeyFactory.keyToString(opinionPollKey.getParent()));
                    	}
                        return;
                    }
                    catch (Exception ex) {
                    	if (!sameImage) {
                    		blobstoreService.delete(imageKey);
                    	}
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        ex.printStackTrace();
                    }
                    
                    break;
                // Orders
                case 'O':
                	
                	// Check the user type carrying out the action
                	if (user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                	
                    Long orderKey = Long.parseLong(req.getParameter("k"));
                    
                    Order order = OrderManager.getOrder(orderKey);
                    
                    Order.Status orderStatus = Order.statusFromString(req.getParameter("o_status"));
                    String orderComments = req.getParameter("o_comments");
                    
                    String deliveryFeeString = req.getParameter("d_fee");
                    Double deliveryFee = null;
                    if (deliveryFeeString != null && !deliveryFeeString.isEmpty()) {
                    	deliveryFee = Double.parseDouble(deliveryFeeString);
                    }
                    
                    try {                
                        OrderManager.updateOrderAttributes(orderKey, 
                        		orderStatus,
                        		deliveryFee,
                        		orderComments);
                        
                        // Add Smart Cash Transaction if payment type is Smart Cash and
                        // order is closed.
                        if (order.getOrderPaymentType() == Order.OrderPaymentType.SMART_CASH &&
                        		orderStatus == Order.Status.CLOSED) {
        	        		SmartCashTransaction transaction =
        	        				new SmartCashTransaction(
        	        						SmartCashTransaction.SmartCashTransactionType.DEBIT,
        	        						order.getCustomer(),
        	        						order.getKey(),
        	        						order.getOrderTotal()
        	        						);
        	        		
        	        		SmartCashTransactionManager.putSmartCashTransaction(transaction);
                        }
                        
                        resp.sendRedirect(editOrderJSP + "?k=" + 
                        		req.getParameter("k") + 
                        		"&readonly=true&msg=success&action=" + action);
                    } 
                    catch (MissingRequiredFieldsException mrfe) {
                        resp.sendRedirect(editOrderJSP + "?etype=MissingInfo&k=" +
                        		orderKey);
                        return;
                    } 
                    catch (Exception ex) {
                        log.log(Level.SEVERE, ex.toString());
                    }
                    
                    return;
                // Survey
                case 'S':
                	
                	// Check the user type
                	if (user.getUserType() != User.Type.RESTAURANT && 
                			user.getUserType() != User.Type.ADMINISTRATOR) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}

                	String surveyKeyString = req.getParameter("k");
                	Key surveyKey = KeyFactory.stringToKey(surveyKeyString);
                	
                	String surveyTitle = req.getParameter("sv_title");
                	String surveyDescription = req.getParameter("sv_description");
                	
                	String validationCode = req.getParameter("sv_code");
                	
                	String surveyStartingDateString = req.getParameter("sv_start");
            		Date surveyStartingDate = null;
            		if (!surveyStartingDateString.isEmpty()) {	
            			surveyStartingDate = 
            					DateManager.getDateValue(surveyStartingDateString);
            		}
            		
            		String surveyEndingDateString = req.getParameter("sv_end");
            		Date surveyEndingDate = null;
            		if (!surveyEndingDateString.isEmpty()) {	
            			surveyEndingDate = 
            					DateManager.getDateValue(surveyEndingDateString);
            		}
            		
            		String surveyPriorityString = req.getParameter("sv_priority");
            		Integer surveyPriority = null;
            		if (!surveyPriorityString.isEmpty()) {
            			surveyPriority = Integer.parseInt(surveyPriorityString);
            		}
            		
            		publicResults = 
            				Boolean.parseBoolean(req.getParameter("sv_public_results"));
            		
                    try {
                    	
                    	SurveyManager.updateSurveyAttributes(surveyKey, 
                    			surveyTitle, 
                    			surveyDescription, 
                    			validationCode, 
                    			surveyStartingDate, 
                    			surveyEndingDate, 
                    			surveyPriority, 
                    			publicResults);

                		resp.sendRedirect(editSurveyJSP + 
                        		"?k="+surveyKeyString+"&readonly=true&msg=success&action=" + action);
                    }
                    catch (MissingRequiredFieldsException mrfe) {
                        resp.sendRedirect(editSurveyJSP + 
                        		"?k=" + surveyKeyString + "&etype=MissingInfo");
                        return;
                    }
                    catch (Exception ex) {
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                    
                    break;
                // Messages
                case 'G':
                	
                	String messageKeyString = req.getParameter("k");
                    Key messageKey = KeyFactory.stringToKey(messageKeyString);
                    Message message = MessageManager.getMessage(messageKey);
                    
                    // Check that the user is a Restaurant
                	if (user.getUserType() != User.Type.RESTAURANT) {
                		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	}
                	
                	String messageTypeString = req.getParameter("m_type");
                	Message.MessageType messageType = 
                			Message.getMessageTypeFromString(messageTypeString);
                	
                	String messageTitle = req.getParameter("m_title");
                	String messageAuthor = req.getParameter("m_author");
                	
                	String messageTextContentString = req.getParameter("m_t_content");
                	Text messageTextContent = new Text(messageTextContentString);

	                BlobKey messageMultimediaContentKey = BlobUtils.assignBlobKey(req, "m_m_content", blobstoreService);    
	                boolean sameMultimediaContent = false;
	                if (messageMultimediaContentKey == null) {
	                	messageMultimediaContentKey = message.getMessageMultimediaContent();
	                    sameMultimediaContent = true;
	                    log.info("No multimedia content uploaded in message \"" + message.getMessageTitle() +
	                    		"\". Using previous content.");
	                }
	                
            		// If a file was uploaded, then the URL will be the link to the file download link
            		String messageURLString;
            		if (messageMultimediaContentKey == null) {
            			messageURLString = req.getParameter("m_url");
            		}
            		else {
            			String urlLocation = req.getParameter("url_location");
            			messageURLString = urlLocation +
            					"/fileDownload?file_id=" +
            					messageMultimediaContentKey.getKeyString();
            		}
            		Link messageURL = new Link(messageURLString);
                	
                	String messageStartingDateString = req.getParameter("m_start");
            		Date messageStartingDate = null;
            		if (!messageStartingDateString.isEmpty()) {	
            			messageStartingDate = DateManager.getDateValue(messageStartingDateString);
            		}
            		
            		String messageEndingDateString = req.getParameter("m_end");
            		Date messageEndingDate = null;
            		if (!messageEndingDateString.isEmpty()) {	
            			messageEndingDate = DateManager.getDateValue(messageEndingDateString);
            		}

                    try {          
                    	MessageManager.updateMessageAttributes(
                    			messageKey, 
                    			messageType, 
                    			messageTitle, 
                    			messageAuthor, 
                    			messageTextContent, 
                    			messageMultimediaContentKey,
                    			messageURL,
                    			messageStartingDate, 
                    			messageEndingDate);
                        
                    	resp.sendRedirect(editMessagesJSP + "?k=" + 
                    						messageKeyString +
                                		"&readonly=true&msg=success&action=" + action);
                    }
                    catch (MissingRequiredFieldsException mrfe) {
                    	if (!sameMultimediaContent) {
                    		blobstoreService.delete(messageMultimediaContentKey);
                    	}
                        resp.sendRedirect(editMessagesJSP + "?etype=MissingInfo&k=" +
                        		messageKeyString);
                        return;
                    }
                    catch (Exception ex) {
                    	if (!sameMultimediaContent) {
                    		blobstoreService.delete(messageMultimediaContentKey);
                    	}
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        ex.printStackTrace();
                    }
                    
                    break;
                default:
                    assert(false); // no other types
            }
        }
    }
}
