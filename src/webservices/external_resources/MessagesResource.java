/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.external_resources;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import util.DateManager;
import webservices.datastore_simple.MessageSimple;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

import datastore.Message;
import datastore.MessageManager;

/**
 * This class represents the list of message
 * as a Resource with only one representation
 */

public class MessagesResource extends ServerResource {
	
	private static final Logger log = 
	        Logger.getLogger(MessagesResource.class.getName());
	
	/**
	 * Returns the message list as a JSON object.
	 * @return An ArrayList of message in JSON format
	 */
    @Get("json")
    public ArrayList<MessageSimple> toJson() {
    	
    	String queryInfo = (String) getRequest().getAttributes()
                .get("queryinfo");

	    char searchBy = queryInfo.charAt(0);
	    String searchString = queryInfo.substring(2);
	        
	    Key storeKey = KeyFactory.stringToKey(searchString);
	    log.info("Query: " + searchBy + "=" + searchString);

        List<Message> messageList = MessageManager.getActiveMessagesFromStore(storeKey,
        		false);
        ArrayList<MessageSimple> messageListSimple = new ArrayList<MessageSimple>();
        for (Message message : messageList) {
        	
        	MessageSimple messageSimple = new MessageSimple(
        			KeyFactory.keyToString(message.getKey()),
        			message.getMessageType(),
        			message.getMessageTitle(),
        			message.getMessageAuthor(),
        			message.getMessageTextContent() != null ?
        					message.getMessageTextContent() : new Text(""),
        			message.getMessageMultimediaContent() != null ?
        					message.getMessageMultimediaContent() :
        						new BlobKey(""),
        			message.getMessageURL() != null ?
        					message.getMessageURL().getValue() : "",
        			DateManager.printDateAsString(message.getMessageEndingDate())
        			);
        	
        	messageListSimple.add(messageSimple);
        }
        
        return messageListSimple;
    }

}