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

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

import util.DateManager;
import datastore.DMSV;
import datastore.DMSVManager;
import datastore.SmartCashTransaction;
import datastore.SmartCashTransactionManager;
import datastore.User;
import exceptions.MissingRequiredFieldsException;

/**
 * This servlet class is used for testing
 * 
 */

@SuppressWarnings("serial")
public class TestingServlet extends HttpServlet {

    private static final Logger log = 
        Logger.getLogger(TestingServlet.class.getName());
    
    // JSP file locations
    private static final String uploadDMSVTestJSP = "/testing/uploadDMSVTest.jsp";
    private static final String addSmartCashTestJSP = "/testing/addSmartCashTest.jsp";

    public void doPost(HttpServletRequest req, HttpServletResponse resp) 
                throws IOException {
    	
    	HttpSession session = req.getSession(true);
        User user = (User) session.getAttribute("user");
        
    	// Check that an administrator is carrying out the action
	    if (user == null || user.getUserType() != User.Type.ADMINISTRATOR) {
	    	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	        return;
	    }
    	
        // Let's check the action required by the mobile app
        String action = req.getParameter("action");

        if (action.equals("addDMSV")) {
        	
            String releaseDateString = req.getParameter("r_date");
            Date releaseDate = DateManager.getDateValue(releaseDateString);
            
            Text field1 = new Text(req.getParameter("field1"));
            Text field2 = new Text(req.getParameter("field2"));
            Text field3 = new Text(req.getParameter("field3"));
            Text field4 = new Text(req.getParameter("field4"));
            Text field5 = new Text(req.getParameter("field5"));
            Text field6 = new Text(req.getParameter("field6"));
            Text field7 = new Text(req.getParameter("field7"));
            Text field8 = new Text(req.getParameter("field8"));
            Text field9 = new Text(req.getParameter("field9"));
            Text field10 = new Text(req.getParameter("field10"));
            Text field11 = new Text(req.getParameter("field11"));
            Text field12 = new Text(req.getParameter("field12"));
            Text field13 = new Text(req.getParameter("field13"));
            Text field14 = new Text(req.getParameter("field14"));
            Text field15 = new Text(req.getParameter("field15"));
            Text field16 = new Text(req.getParameter("field16"));
            
            try {          
                DMSV dmsv = new DMSV(
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
                		field16
                		);
                DMSVManager.putDMSV(dmsv);
                
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.sendRedirect(uploadDMSVTestJSP + "?status=success");
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
        else if (action.equals("addSmartCash")) {
        	
            SmartCashTransaction.SmartCashTransactionType transactionType =
            		SmartCashTransaction.SmartCashTransactionType.CREDIT;
            
            Key customerKey = KeyFactory.stringToKey(req.getParameter("c_key"));
            Double amount = Double.parseDouble(req.getParameter("amount"));
            
            try {          
                SmartCashTransaction transaction = new SmartCashTransaction(
                		transactionType,
                		customerKey,
                		null,
                		amount
                		);
                
                SmartCashTransactionManager.putSmartCashTransaction(transaction);
                
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.sendRedirect(addSmartCashTestJSP + "?status=success");
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
