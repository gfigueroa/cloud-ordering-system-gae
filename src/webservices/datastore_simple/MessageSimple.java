/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices.datastore_simple;

import java.io.Serializable;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Text;

import datastore.Message;

/**
 * This class represents a simple version of the Message table.
 * It is kept simple to return only some information to mobile apps.
 * 
 */

@SuppressWarnings("serial")
public class MessageSimple implements Serializable {
    
	public String key;
	public Message.MessageType messageType;
	public String messageTitle;
	public String messageAuthor;
	public Text messageTextContent;
	public BlobKey messageMultimediaContent;
	public String messageURL;
	public String messageEndingDate;
    
    /**
     * MessageSimple constructor.
     * @param key
     * 			: Message key string
     * @param messageType
     * 			: The message type
     * @param messageTitle
     * 			: The title of the message
     * @param messageAuthor
     * 			: The author of the message
     * @param messageTextContent
     * 			: The text content of the message
     * @param messageMultimediaContent
     * 			: The multimedia content of the message
     * @param messageURL
     * 			: The message URL
     * @param messageEndingDate
     * 			: The date this message will finish
     */
    public MessageSimple(String key, Message.MessageType messageType,
    		String messageTitle, String messageAuthor,
    		Text messageTextContent, BlobKey messageMultimediaContent,
    		String messageURL, String messageEndingDate) {

    	this.key = key;
    	this.messageType = messageType;
    	this.messageTitle = messageTitle;
    	this.messageAuthor = messageAuthor;
    	this.messageTextContent = messageTextContent;
    	this.messageMultimediaContent = messageMultimediaContent;
    	this.messageURL = messageURL;
    	this.messageEndingDate = messageEndingDate;
    }
    
    /**
     * Compare this message with another Message
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this Message, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if ( !(o instanceof MessageSimple ) ) return false;
        MessageSimple m = (MessageSimple) o;
        return this.key.equals(m.key);
    }
    
}
