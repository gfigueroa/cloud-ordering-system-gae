/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Key;

/**
 * This class is used to manage items in the Customer cart
 * 
 */

public class CartManager {
    
    /**
     * Get this customer's menu items in cart (in this session).
     * @param session:
     * 			the user session
     * @return cart menu items
     */
    @SuppressWarnings("unchecked")
	public static List<MenuItemContainer> getCart(HttpSession session) {
        List<MenuItemContainer> cartMenuItems = 
        		(List<MenuItemContainer>) session.getAttribute("cart");
        if (cartMenuItems == null)
            cartMenuItems = new ArrayList<MenuItemContainer>();
        return cartMenuItems;
    }
    
    /**
     * Get this customer's menu items in cart (in this session) for the 
     * specified restaurant.
     * @param session:
     * 			the user session
     * @param restaurant:
     * 			the restaurant key
     * @return cart menu items corresponding to the given restaurant
     */
    @SuppressWarnings("unchecked")
	public static List<MenuItemContainer> getCart(HttpSession session, Key restaurant) {
        List<MenuItemContainer> cartMenuItems = 
        		(List<MenuItemContainer>) session.getAttribute("cart");
        if (cartMenuItems == null)
            cartMenuItems = new ArrayList<MenuItemContainer>();
        
        ArrayList<MenuItemContainer> finalMenuItems = new ArrayList<MenuItemContainer>();
        for (MenuItemContainer menuItem : cartMenuItems) {
        	if (menuItem.menuItem.getKey().getParent().equals(restaurant)) {
        		finalMenuItems.add(menuItem);
        	}
        }
                
        return finalMenuItems;
    }
	
    /**
     * Get all menu items in this customer's shopping cart grouped by
     * restaurant key.
     * @param session:
     * 			the user session
     * @return a HashMap of the menu items in this customer's shopping cart
     * 			grouped by restaurant key
     */
	@SuppressWarnings("unchecked")
	public static HashMap<Key, ArrayList<MenuItemContainer>> 
    		getCartGroupedByRestaurant(HttpSession session) {
		
		List<MenuItemContainer> cartMenuItems = 
        		(List<MenuItemContainer>) session.getAttribute("cart");
		
		HashMap<Key, ArrayList<MenuItemContainer>> menuItemMap = 
				new HashMap<Key, ArrayList<MenuItemContainer>>();
    	
		if (cartMenuItems != null) {
	        for (MenuItemContainer cartMenuItem : cartMenuItems) {
	        	
	        	Key restaurantKey = cartMenuItem.menuItem.getKey().getParent();
	        	ArrayList<MenuItemContainer> menuItemContainerArrayList = menuItemMap.get(restaurantKey);
	        	if (menuItemContainerArrayList == null) {
	        		menuItemContainerArrayList = new ArrayList<MenuItemContainer>();
	        	}
	        	menuItemContainerArrayList.add(cartMenuItem);
	        	menuItemMap.put(restaurantKey, menuItemContainerArrayList);
	        }
		}
		
        return menuItemMap;
    }
	
	/**
     * Update this session's cart with the given menu item containers
     * @param session:
     * 			the user session
     * @param cartMenuItems:
     * 			the new menu item containers to assign to the cart
     */
	public static void updateCart(HttpSession session, 
			List<MenuItemContainer> cartMenuItems) {
		
    	session.setAttribute("cart", cartMenuItems);
    }
	
	/**
     * Empty this session's cart belonging to the given restaurant
     * @param session:
     * 			the user session
     * @param restaurant:
     * 			the key of the restaurant whose menu items will be removed from the cart
     */
	@SuppressWarnings("unchecked")
	public static void emptyRestaurantCart(HttpSession session, 
			Key restaurantKey) {
		
		List<MenuItemContainer> cartMenuItems = 
        		(List<MenuItemContainer>) session.getAttribute("cart");
		
		cartMenuItems.removeAll(getCart(session, restaurantKey));
		
    	updateCart(session, cartMenuItems);
    }
	
}
