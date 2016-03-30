/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/
package webbrowser;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.PostalAddress;

import java.io.IOException;

import java.util.logging.Logger;
import java.util.Date;
import java.util.List;
import java.util.HashSet;

import javax.servlet.http.*;

import session.CartManager;
import session.MenuItemContainer;
import util.DateManager;

import datastore.MenuItem;
import datastore.Order;
import datastore.OrderDetail;
import datastore.OrderManager;
import datastore.CustomerManager;
import datastore.User;
import datastore.MenuItemManager;
import exceptions.InvalidFieldFormatException;
import exceptions.MissingRequiredFieldsException;

/**
 * This servlet class is used to manage session variables
 * from the user.
 * 
 */

@SuppressWarnings("serial")
public class addToCar extends HttpServlet {
	
    private static final Logger log = 
        Logger.getLogger(addToCar.class.getName());
    
    // JSP file locations
    private static final String shoppingCartJSP = "/webbrowser/restaurant/restaurant/car_check.jsp";
    private static final String shoppingListJSP = "/webbrowser/restaurant/restaurant/car_check.jsp";
    private static final String customerListMenuItemJSP = "webbrowser/restaurant/restaurant/menu_list.jsp";
    private static final String login = "/webbrowser/restaurant/member/login.jsp";
    
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp) 
                throws IOException {
        
    	Key menuItemKey = KeyFactory.stringToKey(req.getParameter("k"));
    	MenuItem menuItem = MenuItemManager.getMenuItem(menuItemKey);
    	
        // create session information
        HttpSession session = req.getSession(true);
        assert(session != null);
        User user = (User) session.getAttribute("user");
        
        // Check that a customer is carrying out the action
	    if (user == null || user.getUserType() != User.Type.CUSTOMER) {
	    	//resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	    	resp.sendRedirect(login+"?r_key="+KeyFactory.keyToString(menuItemKey.getParent()));
	        return;
	    }
	    String menuItemTypeString = req.getParameter("mit_key");
	    String variableString = req.getParameter("v");
        
        // Manage customer cart
	    if (variableString.equals("cart")) {
	    	
	    	List<MenuItemContainer> cartMenuItems = CartManager.getCart(session);
	    	
	    	String action = req.getParameter("action");
	    	
	    	// Append items to the cart
            if (action.equals("append")) {
            	
            	menuItemKey = KeyFactory.stringToKey(req.getParameter("k"));
            	menuItem = MenuItemManager.getMenuItem(menuItemKey);
            	
                // we first look for the menuItem in the list
                boolean found = false;
                for (MenuItemContainer container : cartMenuItems) {
                    if (container.equals(menuItem)) {
                        container.qty++;
                        found = true;
                        break;
                    }
                }
                
                // if it isn't already in, then we will have
                // to add a new container
                if (!found)
                    cartMenuItems.add(new MenuItemContainer(menuItem));
                    
                session.setAttribute("cart", cartMenuItems);
                
                if (found) {
                	resp.sendRedirect(shoppingCartJSP + "?r_key=" + 
                			KeyFactory.keyToString(menuItemKey.getParent()) + "&msg=success&action=append");
                }
                else {
                	resp.sendRedirect(customerListMenuItemJSP + "?r_key=" + KeyFactory.keyToString(menuItemKey.getParent()) +
                			"&mit_key="+ menuItemTypeString +"&action=append");
                }
                
                return;
            }
            // Subtract items from the cart
            else if (action.equals("subtract")) {

            	menuItemKey = KeyFactory.stringToKey(req.getParameter("k"));
            	menuItem = MenuItemManager.getMenuItem(menuItemKey);
            	
                // we first look for the menuItem in the list        
                for (MenuItemContainer container : cartMenuItems) {
                    if (container.equals(menuItem)) {
                        if (container.qty > 1)
                            container.qty--;
                        break;
                    }
                }
                
                session.setAttribute("cart", cartMenuItems);
                log.info("removed!");
                resp.sendRedirect(shoppingCartJSP + "?r_key=" + 
            			KeyFactory.keyToString(menuItemKey.getParent()) + "&msg=success&action=subtract");
                return;
            }
            // View the cart
            else if (action.equals("view")) {
            	
            	HashSet<Key> restaurants = new HashSet<Key>();
            	
            	// We check how many different restaurants are in the order
            	for (MenuItemContainer container : cartMenuItems) {
            		menuItemKey = container.menuItem.getKey();
            		restaurants.add(menuItemKey.getParent());
            	}
            	
            	if (restaurants.size() > 1 || restaurants.size() < 1) {
            		resp.sendRedirect(shoppingListJSP);
            	}
            	else if (restaurants.size() == 1) {
            		
            		Key[] restaurantArray = restaurants.toArray(new Key[0]);
            		
            		resp.sendRedirect(shoppingCartJSP  + "?r_key=" + 
                			KeyFactory.keyToString(restaurantArray[0]));
            	}
            }else if(action.equals("remove")){
        		cartMenuItems = CartManager.getCart(session);
        		
        		menuItemKey = KeyFactory.stringToKey(req.getParameter("k"));
        		
                cartMenuItems.remove(new MenuItemContainer(MenuItemManager.getMenuItem(menuItemKey)));
                    
                CartManager.updateCart(session, cartMenuItems);
                log.info("removed!");
                
                resp.sendRedirect(shoppingCartJSP + "?r_key=" + 
            			KeyFactory.keyToString(menuItemKey.getParent()) + "&msg=success&action=remove");
                
                return;
            }
        }
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp) 
                throws IOException {
                
        // create session information
        HttpSession session = req.getSession(true);
        assert(session != null);
        User user = (User) session.getAttribute("user");
        
        // Check that a customer is carrying out the action
	    if (user == null || user.getUserType() != User.Type.CUSTOMER) {
	    	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	        return;
	    }

	    String variableString = req.getParameter("v");
	    
        // Manage customer cart
        if (variableString.equals("cart")) {

        	String action = req.getParameter("action");
        	
            // Delete items from the cart
        	if (action.equals("remove")) {
                
        		List<MenuItemContainer> cartMenuItems = CartManager.getCart(session);
        		
        		Key menuItemKey = KeyFactory.stringToKey(req.getParameter("k"));
        		
                cartMenuItems.remove(new MenuItemContainer(MenuItemManager.getMenuItem(menuItemKey)));
                    
                CartManager.updateCart(session, cartMenuItems);
                log.info("removed!");
                
                resp.sendRedirect(shoppingCartJSP + "?r_key=" + 
            			KeyFactory.keyToString(menuItemKey.getParent()) + "&msg=success&action=remove");
                
                return;
            }
            // Check-out
        	else if (action.equals("buy")) {
        		
                
        		String restaurantKeyString = req.getParameter("r_key");
        		resp.getWriter().println(restaurantKeyString);
        		Key restaurantKey = KeyFactory.stringToKey(restaurantKeyString);
        		Key branchKey = KeyFactory.stringToKey(req.getParameter("b_key"));

                Order.OrderType orderType = Order.getOrderTypeFromString(req.getParameter("o_type"));
                Order.OrderPaymentType orderPaymentType = Order.OrderPaymentType.CASH;
                Order.OrderDeliveryType orderDeliveryType = 
                		Order.getOrderDeliveryTypeFromString(req.getParameter("o_delivery_type"));
                
                PhoneNumber orderPhone = new PhoneNumber(req.getParameter("o_phone"));
                GeoPt customerLocation = null;
                
                PostalAddress address = null;
                String deliveryAddress1 = req.getParameter("d_address1");
        		String deliveryAddress2 = req.getParameter("d_address2");
        		if (deliveryAddress1 != null && deliveryAddress2 != null) {
	        		if (deliveryAddress2.trim().isEmpty()) {
	        			address = new PostalAddress(deliveryAddress1);
	        		}
	        		else {
	        			address = new PostalAddress(
	                        deliveryAddress1 + " " + deliveryAddress2);
	        		}
        		}
        		
        		String timeToServeString = req.getParameter("s_time");
        		Date timeToServe = null;
        		if (!timeToServeString.isEmpty()) {	
        			timeToServe = DateManager.getDateValue(timeToServeString);
        			log.info("TIME STRING: " + timeToServeString);
	        		log.info("TIME TO SERVE: " + DateManager.printDateAsString(timeToServe));
        		}
        		
        		String numberOfPeopleString = req.getParameter("n_people");
        		Integer numberOfPeople = null;
        		if (numberOfPeopleString != null) {
	        		if (!numberOfPeopleString.isEmpty()) {
	        			numberOfPeople = Integer.parseInt(numberOfPeopleString);
	        		}
        		}
        		
        		String orderComments = req.getParameter("o_comments");
                
        		List<MenuItemContainer> cartMenuItems = CartManager.getCart(session, restaurantKey);
        		
                try {
                	
                    Order order = new Order(CustomerManager.getCustomer(user).getKey(), 
                                      branchKey,
                                      null,
                                      null,
                                      orderType,
                                      orderPaymentType,
                                      orderDeliveryType,
                                      orderPhone,
                                      customerLocation,
                                      address,
                                      0.0,
                                      timeToServe,
                                      numberOfPeople,
                                      orderComments);
                    resp.getWriter().print("Done");
                    for (MenuItemContainer container : cartMenuItems) {
                    	OrderDetail detail = new OrderDetail(container.menuItem.getKey(), container.qty, "");
                    	order.addOrderDetail(detail);
                    }
                    
                    OrderManager.putOrder(order);
                    
                    // now empty the cart for this restaurant
                    CartManager.emptyRestaurantCart(session, restaurantKey);
                    
                    resp.sendRedirect(shoppingListJSP + "?msg=success&action=buy");
                }
                catch (MissingRequiredFieldsException mrfe) {
                    resp.sendRedirect(shoppingCartJSP + "?etype=MissingInfo" 
                    		+ "&r_key=" + restaurantKeyString);
                    return;
                }
                catch (InvalidFieldFormatException iffe) {
                    resp.sendRedirect(shoppingCartJSP + "?etype=Format" 
                    		+ "&r_key=" + restaurantKeyString);
                    return;
                }
                catch (Exception e) {
                    log.severe(e.toString());
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    e.printStackTrace();
                }
               
                return;
            }
        }
    }

}
