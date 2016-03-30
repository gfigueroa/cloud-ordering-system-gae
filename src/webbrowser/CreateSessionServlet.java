/*
 Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webbrowser;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.http.*;

import datastore.User;
import datastore.UserManager;

/**
 * This servlet class is used to create and destroy sessions.
 * 
 */

@SuppressWarnings("serial")
public class CreateSessionServlet extends HttpServlet {
    
	private static final Logger log = 
        Logger.getLogger(CreateSessionServlet.class.getName());
	
	// JSP file locations
    private static final String loginJSP = "/webbrowser/restaurant/member/login.jsp";
    private static final String listAdminJSP = "/admin/listAdmin.jsp";
    private static final String customerSearchProductsJSP = "/webbrowser/restaurant/restaurant/restaurant_list.jsp";
    private static final String listOrderJSP = "/restaurant/restaurant/listOrder.jsp";
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp) 
    		throws IOException {
        
    	String action = req.getParameter("action");
    	
    	// are we destroying the session?
        if (action.equals("destroy")){
            HttpSession session = req.getSession(true);
            assert(session != null);

            session.setAttribute("user", null);
            session.setAttribute("cart", null);
            session.setAttribute("backurl", null);
            
            resp.sendRedirect(loginJSP + "?msg=success&action=destroy");
        }
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp) 
    		throws IOException {
        
    	HttpSession session = req.getSession(true);
    	
    	// lets start by checking the user and password fields
        String username = req.getParameter("username");
        String hashedPass = req.getParameter("password");
        String usercheckcode = req.getParameter("usercheckcode");
        String systemcheckcode = req.getParameter("systemcheckcode");

        
        if(usercheckcode.equals(systemcheckcode))
        {
        	// read our users' data
        	User user = UserManager.getUser(username, hashedPass);
        	resp.getWriter().println(username);
        	resp.getWriter().println(hashedPass);
        
        	if (user == null) {
        	
        		resp.sendRedirect(loginJSP + "?etype=InvalidInfo");
        	}
        	else {
        		// create session information
        		session = req.getSession(true);
        		assert(session != null);

        		session.setAttribute("user", user);

        		// we check the user type to send him to his/her own main page
        		switch(user.getUserType()){
        			case ADMINISTRATOR:
        				log.info("User logged in as Administrator");
        				resp.sendRedirect(listAdminJSP + "?msg=success&action=login");
        				break;
        			case CUSTOMER:
        				log.info("User logged in as Customer");
        				if(session.getAttribute("backurl")==null){
        					resp.sendRedirect(customerSearchProductsJSP + "?msg=success&action=login");
        				}else{
        					resp.sendRedirect(session.getAttribute("backurl").toString());
        				}
        				
        				
        				break;
        			case RESTAURANT:
        				log.info("User logged in as Restaurant");
        				resp.sendRedirect(listOrderJSP + "?msg=success&action=login");
        				break;
        			default:
        				// there should be no other
        				// type of user
        				assert(false);
        		}
        	}
        }else{
        	resp.sendRedirect(loginJSP + "?etype=ErrorCheckCode");
        }
    }
    
}