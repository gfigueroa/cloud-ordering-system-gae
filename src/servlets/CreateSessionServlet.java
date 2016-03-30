/*
 Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package servlets;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.http.*;

import datastore.User;
import datastore.UserManager;

import util.Printer;
import util.Dictionary;

/**
 * This servlet class is used to create and destroy sessions.
 * 
 */

@SuppressWarnings("serial")
public class CreateSessionServlet extends HttpServlet {
    
	private static final Logger log = 
        Logger.getLogger(CreateSessionServlet.class.getName());
	
	// JSP file locations
    private static final String loginJSP = "/login.jsp";
    private static final String listAdminJSP = "/admin/listAdmin.jsp";
    private static final String customerSearchProductsJSP = 
    		"/customer/customerListRestaurant.jsp";
    private static final String listOrderJSP = "/restaurant/listOrder.jsp";
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp) 
    		throws IOException {
        
    	String action = req.getParameter("action");
    	
    	if (action == null) {
    		resp.sendRedirect(loginJSP);
    	}
    	
    	// are we destroying the session?
        if (action.equals("destroy")) {
            HttpSession session = req.getSession(true);
            assert(session != null);

            Printer printer = (Printer) session.getAttribute("printer");
            Dictionary.Language language;
            if (printer != null) {
            	language = printer.getLanguage();
            }
            else {
            	language = Dictionary.Language.ENGLISH;
            }
            
            session.setAttribute("user", null);
            session.setAttribute("cart", null);
            session.setAttribute("printer", null);
            
            resp.sendRedirect(loginJSP + "?lang=" + 
            		Dictionary.getLanguageString(language) + 
            		"&msg=success&action=" + action);
        }
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp) 
    		throws IOException {
        
    	// lets start by checking the user and password fields
        String username = req.getParameter("username");
        String hashedPass = req.getParameter("password");
        String language = req.getParameter("language") != null ?
        		req.getParameter("language") : "CH";
	
        // read our users' data
        
        User user = UserManager.getUser(username, hashedPass);
        
        Printer printer; 
	    if (language.equals("EN")) {
	    	printer = new Printer(Dictionary.Language.ENGLISH);
	    }
	    else {
	    	printer = new Printer(Dictionary.Language.CHINESE);
	    }
        
        if (user == null) {
        	resp.sendRedirect(loginJSP + "?etype=InvalidInfo" +
        			"&lang=" + language);
        }
        else {
            // create session information
            HttpSession session = req.getSession(true);
            assert(session != null);

            session.setAttribute("user", user);
            session.setAttribute("printer", printer);
            // we check the user type to send him to his/her own main page
            switch(user.getUserType()) {
                case ADMINISTRATOR:
                	log.info("User logged in as Administrator");
                    resp.sendRedirect(listAdminJSP + 
                    		"?msg=success&action=login");
                    break;
                case CUSTOMER:
                	log.info("User logged in as Customer");
                    resp.sendRedirect(customerSearchProductsJSP + 
                    		"?msg=success&action=login");
                    break;
                case RESTAURANT:
                	log.info("User logged in as Restaurant");
                    resp.sendRedirect(listOrderJSP + 
                    		"?msg=success&action=login");
                    break;
                default:
                    // there should be no other
                    // type of user
                    assert(false);
            }
        }
    }
    
}
