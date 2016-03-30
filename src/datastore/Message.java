/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;

import exceptions.MissingRequiredFieldsException;

/**
 * This class represents the Message table.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class Message implements Serializable {

	public static enum Status {
		INACTIVE, ACTIVE, EXPIRED
	}
	
	public static enum MessageType {
		TEXT, MULTIMEDIA
	}
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private MessageType messageType;

    @Persistent
    private String messageTitle;
    
    @Persistent
    private String messageAuthor;
    
    @Persistent
    private Text messageTextContent;
    
    @Persistent
    private BlobKey messageMultimediaContent;
    
    @Persistent
    private Link messageURL;
    
    @Persistent
    private Date messageCreationDate;
    
    @Persistent
    private Date messageStartingDate;
    
    @Persistent
    private Date messageEndingDate;

    /**
     * Message constructor.
     * @param messageType
     * 			: message type
     * @param messageTitle
     * 			: message title
     * @param messageAuthor
     * 			: message author
     * @param messageTextContent
     * 			: message text content
     * @param messageMultimediaContent
     * 			: message multimedia content
     * @param messageURL
     * 			: message URL
     * @param messageStartingDate
     * 			: the date this message will start to be available
     * @param messageEndingDate
     * 			: the date this message will finish
     * @throws MissingRequiredFieldsException
     */
    public Message(MessageType messageType, String messageTitle, 
    		String messageAuthor, Text messageTextContent,
    		BlobKey messageMultimediaContent, Link messageURL,
    		Date messageStartingDate, Date messageEndingDate) 
    		throws MissingRequiredFieldsException {
    	
    	// Check "required field" constraints
    	if (messageType == null || messageTitle == null || messageAuthor == null || 
    			messageStartingDate == null || messageEndingDate == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	if (messageTitle.trim().isEmpty() ||
    			messageAuthor.trim().isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	if ((messageTextContent == null || messageTextContent.getValue().isEmpty()) &&
    			messageMultimediaContent == null && 
    			(messageURL == null || messageURL.getValue().isEmpty())) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	    	
    	this.messageType = messageType;
    	this.messageTitle = messageTitle;
        this.messageAuthor = messageAuthor;;
        this.messageTextContent = messageTextContent;
        this.messageMultimediaContent = messageMultimediaContent;
        this.messageURL = messageURL;
        this.messageCreationDate = new Date();
        this.messageStartingDate = messageStartingDate;
        this.messageEndingDate = messageEndingDate;
    }

    /**
     * Get Message key.
     * @return Message key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Get message type.
     * @return Message type
     */
    public MessageType getMessageType() {
    	return messageType;
    }
    
    /**
     * Get the message type's string representation
     * @return The message type as a string
     */
    public String getMessageTypeString() {
    	switch (messageType) {
    		case TEXT:
    			return "Text";
    		case MULTIMEDIA:
    			return "Multimedia";
    		default:
    			return "";
    	}
    }
    
    /**
     * Get the message type from its string representation.
     * @param messageTypeString
     * 			: the message type string to convert
     * @return messageType
     */
    public static MessageType getMessageTypeFromString(
    		String messageTypeString) {
    	
    	if (messageTypeString == null) {
    		return null;
    	}
    	
    	if (messageTypeString.equalsIgnoreCase("text")) {
    		return MessageType.TEXT;
    	}
    	else if (messageTypeString.equals("multimedia")) {
    		return MessageType.MULTIMEDIA;
    	}
    	else {
    		return null;
    	}
    }
    
    /**
     * Get Message title.
     * @return Message title
     */
    public String getMessageTitle() {
        return messageTitle;
    }
    
    /**
     * Get Message author.
     * @return Message author
     */
    public String getMessageAuthor() {
    	return messageAuthor;
    }
    
    /**
     * Get Message text content.
     * @return Message text content
     */
    public Text getMessageTextContent() {
        return messageTextContent;
    }
    
    /**
     * Get Message multimedia content.
     * @return Message multimedia content
     */
    public BlobKey getMessageMultimediaContent() {
        return messageMultimediaContent;
    }
    
    /**
     * Get Message URL.
     * @return Message URL.
     */
    public Link getMessageURL() {
    	return messageURL;
    }
    
    /**
     * Get the date this message was created
     * @return The date this message was created
     */
    public Date getMessageCreationDate() {
        return messageCreationDate;
    }
    
    /**
     * Get the date when this message will be available
     * @return The date when this message will be available
     */
    public Date getMessageStartingDate() {
        return messageStartingDate;
    }
    
    /**
     * Get the date when this message will be stop being available
     * @return The date when this message will expire
     */
    public Date getMessageEndingDate() {
        return messageEndingDate;
    }
    
    /**
     * Get the current status of this message
     * @return The current status of this message
     */
    public Status getCurrentStatus() {
        // INACTIVE, ACTIVE, EXPIRED
    	
    	Date now = new Date();
    	if (now.compareTo(messageStartingDate) < 0) {
    		return Status.INACTIVE;
    	}
    	else {
    		if (now.compareTo(messageEndingDate) > 0) {
    			return Status.EXPIRED;
    		}
    		else {
    			return Status.ACTIVE;
    		}
    	}
    }
    
    /**
     * Compare this Message with another Message
     * @param o
     * 			: the object to compare
     * @return true if the object to compare is equal to this Message, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof Message ) ) return false;
        Message message = (Message) o;
        return KeyFactory.keyToString(this.getKey())
                .equals(KeyFactory.keyToString(message.getKey()));
    }
    
    /**
     * Set Message type.
     * @param messageType
     * 			: the type of this message
     * @throws MissingRequiredFieldsException
     */
    public void setMessageType(MessageType messageType)
    		throws MissingRequiredFieldsException {
    	if (messageType == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Message Type is missing.");
    	}
    	this.messageType = messageType;
    }
    
    /**
     * Set Message title.
     * @param messageTitle
     * 			: the title of this message
     * @throws MissingRequiredFieldsException
     */
    public void setMessageTitle(String messageTitle)
    		throws MissingRequiredFieldsException {
    	if (messageTitle == null || messageTitle.isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Message title is missing.");
    	}
    	this.messageTitle = messageTitle;
    }
    
    /**
     * Set Message Author.
     * @param messageAuthor
     * 			: the Author of this message
     * @throws MissingRequiredFieldsException
     */
    public void setMessageAuthor(String messageAuthor)
    		throws MissingRequiredFieldsException {
    	if (messageAuthor == null || messageAuthor.isEmpty()) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Message title is missing.");
    	}
    	this.messageAuthor = messageAuthor;
    }
    
    /**
     * Set Message text Content.
     * @param messageTextContent
     * 			: the Text Content of this message
     * @throws MissingRequiredFieldsException
     */
    public void setMessageTextContent(Text messageTextContent, 
    		BlobKey messageMultimediaContent, Link messageURL)
    		throws MissingRequiredFieldsException {
    	if ((messageTextContent == null || 
    			messageTextContent.getValue().isEmpty()) &&
    			messageMultimediaContent == null &&
    			(messageURL == null || messageURL.getValue().isEmpty())) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	this.messageTextContent = messageTextContent;
    }
    
    /**
     * Set Message Multimedia Content.
     * @param messageMultimediaContent
     * 			: the Multimedia Content of this message
     * @throws MissingRequiredFieldsException
     */
    public void setMessageMultimediaContent(BlobKey messageMultimediaContent,
    		Text messageTextContent, Link messageURL)
    		throws MissingRequiredFieldsException {
    	if ((messageTextContent == null || 
    			messageTextContent.getValue().isEmpty()) &&
    			messageMultimediaContent == null &&
    			(messageURL == null || messageURL.getValue().isEmpty())) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	this.messageMultimediaContent = messageMultimediaContent;
    }
    
    /**
     * Set Message URL.
     * @param messageURL
     * 			: the Message URL
     * @throws MissingRequiredFieldsException 
     */
    public void setMessageURL(Link messageURL, Text messageTextContent,
    		BlobKey messageMultimediaContent) 
    		throws MissingRequiredFieldsException {
    	if ((messageTextContent == null || 
    			messageTextContent.getValue().isEmpty()) &&
    			messageMultimediaContent == null &&
    			(messageURL == null || messageURL.getValue().isEmpty())) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"One or more required fields are missing.");
    	}
    	this.messageURL = messageURL;
    }
    
    /**
     * Set Message Starting Date.
     * @param messageStartingDate
     * 			: the date this message will be available
     * @throws MissingRequiredFieldsException
     */
    public void setMessageStartingDate(Date messageStartingDate)
    		throws MissingRequiredFieldsException {
    	if (messageStartingDate == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Message starting date is missing.");
    	}
    	this.messageStartingDate = messageStartingDate;
    }
    
    /**
     * Set Message Ending Date.
     * @param messageEndingDate
     * 			: the date this message will stop (expire)
     * @throws MissingRequiredFieldsException
     */
    public void setMessageEndingDate(Date messageEndingDate)
    		throws MissingRequiredFieldsException {
    	if (messageEndingDate == null) {
    		throw new MissingRequiredFieldsException(this.getClass(), 
    				"Message ending date is missing.");
    	}
    	this.messageEndingDate = messageEndingDate;
    }
}