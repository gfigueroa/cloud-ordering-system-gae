/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
 */

//Copyright 2011, Google Inc. All Rights Reserved.
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.

package servlets;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.DateManager;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;

import datastore.DMSV;
import datastore.DMSVManager;
import datastore.Restaurant;
import datastore.RestaurantManager;
import datastore.RestaurantType;
import datastore.RestaurantTypeManager;
import datastore.SystemManager;
import datastore.User;
import exceptions.InvalidFieldFormatException;
import exceptions.MissingRequiredFieldsException;
import exceptions.ObjectExistsInDatastoreException;

/**
 * This servlet responds to the request corresponding to orders. The Class
 * places the order.
 * 
 * @author
 */
@SuppressWarnings("serial")
public class FileUploadServlet extends HttpServlet {

	private static final Logger logger = Logger
			.getLogger(FileUploadServlet.class.getCanonicalName());

	// JSP file locations

	private static final String addRestaurantJSP = "/admin/addRestaurant.jsp";
	private static final String listRestaurantJSP = "/admin/listRestaurant.jsp";
	private static final String addDMSV = "/admin/addDMSV.jsp";
	private static final String listDMSV = "/admin/ListDMSV.jsp";

	/**
	 * Read the contents of uploaded file and add the xml as a string in the
	 * Task Queue, processed using backend
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		
/*		DataInputStream dataIn = null;
		int formDataLength;
		byte dataBytes[] ;
		int byteRead = 0;
		int totalBytesRead = 0;
		
		String file;
		String delims_line = "";
		String delims_field = "";
		String[] line ;
		String[] field;
		String test = "";*/
		DataInputStream dataIn = new DataInputStream(req.getInputStream());
		int formDataLength = req.getContentLength();
		byte dataBytes[] = new byte[formDataLength];
		int byteRead = 0;
		int totalBytesRead = 0;
		
		String file = new String(dataBytes);
		String delims_line = "[\n]";
		String delims_field = "[,]";
		String[] line = file.split(delims_line);
		String[] field;
		switch(req.getParameter("type").charAt(0)){
		//Retail Store
		case 'R':
		
			String restaurantSuperTypeString;
			String restaurantTypeString;
			String restaurantNameString;
			String restaurantDescriptionString;
			String userEmailString;
			String userPasswordString;
			String websiteString;
			String imageString;
			String openingHourString;
			String openingMinutesString;
			String closingHourString;
			String closingMinutesString;
			String restaurantCommentsString;
	
			req.setAttribute("Accept", "text/html; charset=UTF-8");
			dataIn = new DataInputStream(req.getInputStream());
			// we are taking the length of Content type data
			formDataLength = req.getContentLength();
			dataBytes = new byte[formDataLength];
			byteRead = 0;
			totalBytesRead = 0;
			// this loop converting the uploaded file into byte code
			while (totalBytesRead < formDataLength) {
				byteRead = dataIn.read(dataBytes, totalBytesRead, formDataLength);
				totalBytesRead += byteRead;
			}
			file = new String(dataBytes);
	
			delims_line = "[\n]";
			delims_field = "[,]";
			line = file.split(delims_line);
			System.out.println(line.length);
			for (int i = 0; i < line.length; i++) {
				if (line[i].trim().length() > 0) {
					line[i] = line[i].trim().replaceAll("\"", "");
	
					field = line[i].split(delims_field);
					// System.out.println(field[1].trim().substring(0, 1));
					System.out.println(field.length);
					if (field.length >= 13) {
						restaurantSuperTypeString = field[0].trim();
						restaurantTypeString = field[1].trim();
						restaurantNameString = field[2].trim();
						restaurantDescriptionString = field[3].trim();
						userEmailString = field[4].trim();
						userPasswordString = field[5].trim();
						websiteString = field[6].trim();
						imageString = field[7].trim();
						openingHourString = field[8].trim();
						openingMinutesString = field[9].trim();
						closingHourString = field[10].trim();
						closingMinutesString = field[11].trim();
						restaurantCommentsString = field[12].trim();
						System.out.println(restaurantSuperTypeString.replaceAll(
								"� ", ""));
						System.out
								.println(restaurantTypeString.replaceAll(" ", ""));
						System.out
								.println(restaurantNameString.replaceAll(" ", ""));
						System.out.println(restaurantDescriptionString.replaceAll(
								" ", ""));
						System.out.println(userEmailString.replaceAll(" ", ""));
						System.out.println(userPasswordString.replaceAll(" ", ""));
						System.out.println(websiteString.replaceAll(" ", ""));
						System.out.println(imageString.replaceAll(" ", ""));
						System.out.println(openingHourString.replaceAll(" ", ""));
						System.out
								.println(openingMinutesString.replaceAll(" ", ""));
						System.out.println(closingHourString.replaceAll(" ", ""));
						System.out
								.println(closingMinutesString.replaceAll(" ", ""));
						System.out.println(restaurantCommentsString.replaceAll(
								"\\s", ""));
	
						User.Type type;
	
						User neoUser;
						Email userEmail = new Email("burger@a.com");
						String userPassword = userPasswordString;
	
						type = User.Type.RESTAURANT;
	
						restaurantTypeString = "25";
						Long restaurantType = null;
						RestaurantType.StoreSuperType storeSuperType = null;
						if (!restaurantTypeString.isEmpty()) {
							restaurantType = Long.parseLong(restaurantTypeString);
							RestaurantType storeType = RestaurantTypeManager
									.getRestaurantType(restaurantType);
							storeSuperType = storeType.getStoreSuperType();
						}
	
						Boolean hasNewsService = true; // All stores have news
														// service
						Boolean hasProductsService = false;
						Boolean hasServiceProvidersService = false;
						Boolean hasMessagesService = false;
	
						String restaurantName = restaurantNameString;
						Integer channelNumber = 0;
						String restaurantDescription = restaurantDescriptionString;
						Link website = new Link(websiteString);
	
						BlobKey logoKey = null;
	
						openingHourString = "08";
						openingMinutesString = "00";
						int openingHour = Integer.parseInt(openingHourString);
						int openingMinutes = Integer.parseInt(openingMinutesString);
						Date openingTime = DateManager.getDateValue(openingHour,
								openingMinutes);
	
						closingHourString = "16";
						closingMinutesString = "00";
						int closingHour = Integer.parseInt(closingHourString);
						int closingMinutes = Integer.parseInt(closingMinutesString);
						Date closingTime = DateManager.getDateValue(closingHour,
								closingMinutes);
	
						String restaurantComments = restaurantCommentsString;
	
						try {
							neoUser = new User(userEmail, userPassword, type);
	
							Restaurant restaurant = new Restaurant(neoUser,
									restaurantType, hasNewsService,
									hasProductsService, hasServiceProvidersService,
									hasMessagesService,
									restaurantName, channelNumber,
									restaurantDescription, website,
									logoKey, openingTime, closingTime,
									restaurantComments);
	
							RestaurantManager.putRestaurant(restaurant);
							SystemManager.updateRestaurantListVersion();
							SystemManager.updateStoreListVersion(storeSuperType);
	
						} catch (MissingRequiredFieldsException mrfe) {
							resp.sendRedirect(addRestaurantJSP
									+ "?etype=MissingInfo");
							return;
						} catch (InvalidFieldFormatException iffe) {
							resp.sendRedirect(addRestaurantJSP + "?etype=Format");
							return;
						} catch (ObjectExistsInDatastoreException oede) {
							resp.sendRedirect(addRestaurantJSP
									+ "?etype=AlreadyExists");
							return;
						} catch (Exception ex) {
							logger.log(Level.SEVERE, ex.toString());
							resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						}
	
						// */
					}
				}
			}
	
			resp.sendRedirect(listRestaurantJSP + "?msg=success&action=add");
			break;
		
			//DMSV
		case 'D':
			
			Text field1=null;
			Text field2=null;
			Text field3=null;
			Text field4=null;
			Text field5=null;
			Text field6=null;
			Text field7=null;
			Text field8=null;
			Text field9=null;
			Text field10=null;
			Text field11=null;
			Text field12=null;
			Text field13=null;
			Text field14=null;
			Text field15=null;
			Text field16=null;
			String datetime=null;
	
			req.setAttribute("Accept", "text/html; charset=UTF-8");
/*			dataIn = new DataInputStream(req.getInputStream());
			// we are taking the length of Content type data
			formDataLength = req.getContentLength();
		System.out.println(formDataLength);
			dataBytes = new byte[formDataLength];
			byteRead = 0;
			totalBytesRead = 0;
			// this loop converting the uploaded file into byte code
			while (totalBytesRead < formDataLength) {
				byteRead = dataIn.read(dataBytes, totalBytesRead, formDataLength);
				totalBytesRead += byteRead;
				
			}*/
			BufferedReader in = new BufferedReader(
			         new InputStreamReader(req.getInputStream(), "UTF-8"));
			String tmp="";
			while((tmp=in.readLine()) != null){
				file= file + tmp;
			//System.out.println(file);
			}
			
		//System.out.println(file);
				
			delims_field = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
			line = file.split(delims_field);
			int field_number=0;
		System.out.println(line.length);
			for (int i = 0; i < line.length; i++) {
				if (line[i].trim().length() > 0) {
				
					if(field_number==0){ 
						datetime = line[i].trim().replaceAll("\"", "");
						System.out.println(datetime + "length:"+ datetime.length());
					}
					if(field_number==1)	field1 = new Text(line[i].trim().replaceAll("\"", ""));
					if(field_number==2)	field2 = new Text(line[i].trim().replaceAll("\"", ""));
					if(field_number==3)	field3 = new Text(line[i].trim().replaceAll("\"", ""));
					if(field_number==4){	field4 = new Text(line[i].trim().replaceAll("\"", ""));
						////System.out.println(field4);
					}
					if(field_number==5)	field5 = new Text(line[i].trim().replaceAll("\"", ""));
					if(field_number==6)	field6 = new Text(line[i].trim().replaceAll("\"", ""));
					if(field_number==7)	field7 = new Text(line[i].trim().replaceAll("\"", ""));
					if(field_number==8)	field8 = new Text(line[i].trim().replaceAll("\"", ""));
					if(field_number==9)	field9 = new Text(line[i].trim().replaceAll("\"", ""));
					if(field_number==10)	field10 = new Text(line[i].trim().replaceAll("\"", ""));
					if(field_number==11)	field11 = new Text(line[i].trim().replaceAll("\"", ""));
					if(field_number==12)	field12 = new Text(line[i].trim().replaceAll("\"", ""));
					if(field_number==13)	field13 = new Text(line[i].trim().replaceAll("\"", ""));
					if(field_number==14)	field14 = new Text(line[i].trim().replaceAll("\"", ""));
					if(field_number==15)	field15 = new Text(line[i].trim().replaceAll("\"", ""));
					if(field_number==16)	field16 = new Text(line[i].trim().replaceAll("\"", ""));
						
						field_number++;

					
				}
				if(field_number==17){ 
					field_number=0;

					
					
					DateFormat formatter ;			
					Date releaseDate = null ;
					formatter = new SimpleDateFormat("dd-MMM-yy");
					try {
						if(datetime.length()<10){
							releaseDate = (Date)formatter.parse(datetime);
						}else{
							String tmpdate=datetime.substring(datetime.length()-8);
							////System.out.println(tmpdate + " length:"+ tmpdate.length());
							releaseDate = (Date)formatter.parse(tmpdate);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
					try{
						DMSV dmsv = new DMSV(releaseDate,field1,field2,field3,field4,
								field5,field6,field7,field8,field9,field10,field11,
								field12,field13,field14,field15,field16);
						DMSVManager.putDMSV(dmsv);
					}                    
					catch (MissingRequiredFieldsException mrfe) {
                    	resp.sendRedirect(addDMSV + "?etype=MissingInfo");
                        return;
                    }
                    catch (Exception ex) {
                    	logger.log(Level.SEVERE, ex.toString());
                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
				}
			}
	
			resp.sendRedirect(listDMSV + "?msg=success&action=add");
			break;
							
		}
		
	
	}
}
