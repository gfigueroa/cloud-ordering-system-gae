/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package servlets;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.servlet.http.*;

import util.DateManager;

import com.google.appengine.api.datastore.Text;

import datastore.Region;
import datastore.RegionManager;
import datastore.RestaurantType;
import datastore.RestaurantTypeManager;
import datastore.DMSVManager;
import datastore.SystemManager;
import datastore.User;
import exceptions.MissingRequiredFieldsException;

/**
 * This servlet class is used to add, delete and update
 * global objects in the system.
 * 
 */

@SuppressWarnings("serial")
public class ManageGlobalObjectsServlet extends HttpServlet {

    private static final Logger log = 
        Logger.getLogger(ManageGlobalObjectsServlet.class.getName());
    
    // JSP file locations
    private static final String addRegionJSP = "/admin/addRegion.jsp";
    private static final String editRegionJSP = "/admin/editRegion.jsp";
    private static final String listRegionJSP = "/admin/listRegion.jsp";
    private static final String addRestaurantTypeJSP = "/admin/addRestaurantType.jsp";
    private static final String editRestaurantTypeJSP = "/admin/editRestaurantType.jsp";
    private static final String listRestaurantTypeJSP = "/admin/listRestaurantType.jsp";
    //private static final String addDMSVJSP = "/admin/addRestaurantType.jsp";
    private static final String editDMSVJSP = "/admin/editDMSVmonth.jsp";
    private static final String listDMSVJSP = "/admin/listDMSVmonth.jsp";

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
      		Long keyLong;

            // deleting a user
            switch(req.getParameter("type").charAt(0)){
                case 'R':
                	keyLong = Long.parseLong(keyString);
                	//Region region = RegionManager.getRegion(keyLong);
                	RegionManager.deleteRegion(keyLong);
                	resp.sendRedirect(listRegionJSP + "?msg=success&action=delete");
                	break;
                case 'T':
                	keyLong = Long.parseLong(keyString);
                	//RestaurantType restaurantType = RestaurantTypeManager.getRestaurantType(keyLong);
                	RestaurantTypeManager.deleteRestaurantType(keyLong);
                	SystemManager.updateRestaurantTypeListVersion();
                	resp.sendRedirect(listRestaurantTypeJSP + "?msg=success&action=delete");
                	break;
                case 'D':
                	keyLong = Long.parseLong(keyString);
                	
                	//RestaurantType restaurantType = RestaurantTypeManager.getRestaurantType(keyLong);
                	DMSVManager.deleteDMSV(keyLong);
                	resp.sendRedirect(listDMSVJSP + "?month=" + req.getParameter("month") + "&year=" + req.getParameter("year") + "&msg=success&action=delete");
                	break;
                default:
                    assert(false); // there is no other type
            }
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) 
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

        if (action.equals("add")) {

            char typeChar = req.getParameter("type").charAt(0);
        
            switch(typeChar){
                case 'R':
                    String regionName = req.getParameter("r_name");
                    String regionComments = req.getParameter("r_comments");
                           
                    try {                
                        Region neoRegion = new Region(regionName, regionComments);
                        RegionManager.putRegion(neoRegion);
                        
                        resp.sendRedirect(listRegionJSP + "?msg=success&action=add");
                    } 
                    catch (MissingRequiredFieldsException mrfe) {
                        resp.sendRedirect(addRegionJSP  + "?etype=MissingInfo");
                        return;
                    } 
                    catch (Exception ex) {
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }

                    break;
                case 'T':
                	RestaurantType.StoreSuperType storeSuperType = 
                			RestaurantType.getStoreSuperTypeFromString(req.getParameter("ss_type"));
                    String restaurantTypeName = req.getParameter("rt_name");
                    String restaurantTypeDescription = req.getParameter("rt_description");
                           
                    try {                
                        RestaurantType neoRestaurantType = 
                        		new RestaurantType(storeSuperType,
                        				restaurantTypeName, restaurantTypeDescription);
                        RestaurantTypeManager.putRestaurantType(neoRestaurantType);
                        SystemManager.updateRestaurantTypeListVersion();
                        
                        resp.sendRedirect(listRestaurantTypeJSP + "?msg=success&action=add");
                    } 
                    catch (MissingRequiredFieldsException mrfe) {
                        resp.sendRedirect(addRestaurantTypeJSP  + "?etype=MissingInfo");
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
        	
            char typeChar = req.getParameter("type").charAt(0);
            
            switch (typeChar) {
                case 'R':
                	String regionKeyString = req.getParameter("k");
                	Long regionKeyLong = Long.parseLong(regionKeyString);
                	String regionName = req.getParameter("r_name");
                	String regionComments = req.getParameter("r_comments");
                	
                    try {
                        RegionManager.updateRegionAttributes(
                        		regionKeyLong,
                                regionName,
                                regionComments);

                        resp.sendRedirect(editRegionJSP + "?k=" + regionKeyString + "&readonly=true&msg=success&action=update");
                    } 
                    catch (MissingRequiredFieldsException mrfe) {
                        resp.sendRedirect(editRegionJSP + "?etype=MissingInfo&k="
                                + regionKeyString);
                    }
                    catch (Exception ex) {
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                    
                    return;
                case 'T':
                	String restaurantTypeKeyString = req.getParameter("k");
                	Long restaurantTypeKeyLong = Long.parseLong(restaurantTypeKeyString);
                	RestaurantType.StoreSuperType storeSuperType = 
                			RestaurantType.getStoreSuperTypeFromString(req.getParameter("ss_type"));
                	String restaurantTypeName = req.getParameter("rt_name");
                	String restaurantTypeDescription = req.getParameter("rt_description");
                	
                    try {
                        RestaurantTypeManager.updateRestaurantTypeAttributes(
                        		restaurantTypeKeyLong,
                        		storeSuperType,
                        		restaurantTypeName,
                        		restaurantTypeDescription);
                        SystemManager.updateRestaurantTypeListVersion();

                        resp.sendRedirect(editRestaurantTypeJSP + "?k=" + restaurantTypeKeyString + "&readonly=true&msg=success&action=update");
                    } 
                    catch (MissingRequiredFieldsException mrfe) {
                        resp.sendRedirect(editRestaurantTypeJSP + "?etype=MissingInfo&k="
                                + restaurantTypeKeyString);
                    }
                    catch (Exception ex) {
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                    
                    return;
                case 'D':
                	String dmsvKeyString = req.getParameter("k");
                	Date releaseDate = null;
                	releaseDate = DateManager.getDateValue(req.getParameter("d_date"));
                	Long dmsvKeyLong = Long.parseLong(dmsvKeyString);
                	Text field1 = new Text(req.getParameter("d_field_1"));
                	Text field2 = new Text(req.getParameter("d_field_2"));
                	Text field3 = new Text(req.getParameter("d_field_3"));
                	Text field4 = new Text(req.getParameter("d_field_4"));
                	Text field5 = new Text(req.getParameter("d_field_5"));
                	Text field6 = new Text(req.getParameter("d_field_6"));
                	Text field7 = new Text(req.getParameter("d_field_7"));
                	Text field8 = new Text(req.getParameter("d_field_8"));
                	Text field9 = new Text(req.getParameter("d_field_9"));
                	Text field10 = new Text(req.getParameter("d_field_10"));
                	Text field11 = new Text(req.getParameter("d_field_11"));
                	Text field12 = new Text(req.getParameter("d_field_12"));
                	Text field13 = new Text(req.getParameter("d_field_13"));
                	Text field14 = new Text(req.getParameter("d_field_14"));
                	Text field15 = new Text(req.getParameter("d_field_15"));
                	Text field16 = new Text(req.getParameter("d_field_16"));
                    try {
                        DMSVManager.updateDMSVAttributes(
                        		dmsvKeyLong,
                        		releaseDate,
                        		field1,
                        		field2,
                        		field3,
                        		field4,
                        		field5,
                        		field6,
                        		field7,
                        		field8,
                        		field9,
                        		field10,
                        		field11,
                        		field12,
                        		field13,
                        		field14,
                        		field15,
                        		field16);

                        resp.sendRedirect(editDMSVJSP + "?k=" + dmsvKeyString + "&readonly=true&msg=success&action=update");
                    } 
                    catch (MissingRequiredFieldsException mrfe) {
                        resp.sendRedirect(editDMSVJSP + "?etype=MissingInfo&k="
                                + dmsvKeyString);
                    }
                    catch (Exception ex) {
                        log.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                    return;
                default:
                    assert(false); // no other types
            }
        }
    }
}
