/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package datastore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.Text;

import exceptions.InexistentObjectException;
import exceptions.MissingRequiredFieldsException;

/**
 * This class is used to manage the GAE Datastore operations (get, put, delete, update)
 * made on the Message class.
 * 
 */

public class MessageManager {
	
	private static final Logger log = 
        Logger.getLogger(MessageManager.class.getName());
	
	/**
     * Get a Message instance from the datastore given the Message key.
     * @param key
     * 			: the Message's key
     * @return Message instance, null if Message is not found
     */
	public static Message getMessage(Key key) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Message message;
		try  {
			message = pm.getObjectById(Message.class, key);
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
		pm.close();
		return message;
	}
	
	/**
     * Get ALL the messages in the datastore from a specific store
     * and returns them in a List structure
     * @param storeKey: 
     * 				the key of the store whose message will be retrieved
     * @param ascendingOrder:
     * 				whether the list should be sorted
	 * 				in ascending order or not
     * @return all messages in the datastore belonging to the given store
     * TODO: Fix "touching" of messages
     */
	public static List<Message> getAllMessagesFromStore(Key storeKey,
			boolean ascendingOrder) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant restaurant = pm.getObjectById(Restaurant.class, storeKey);
		
        List<Message> result = null;
        try {
            result = restaurant.getMessages();
            // Touch each branch
            for (Message message : result) {
            	message.getKey();
            }
        } 
        finally {
        	pm.close();
        }

        result = sortMessages(result, ascendingOrder);
        return result;
    }
	
	/**
     * Get inactive messages in the datastore from a specific store
     * and returns them in a List structure
     * @param storeKey: 
     * 				the key of the store whose messages will be retrieved
     * @param ascendingOrder:
     * 				whether the list should be sorted
	 * 				in ascending order or not
     * @return all messages that are "INACTIVE" belonging to the given store
     */
	public static List<Message> getInactiveMessagesFromStore(Key storeKey,
			boolean ascendingOrder) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant restaurant = pm.getObjectById(Restaurant.class, storeKey);
		
        List<Message> result = null;
        ArrayList<Message> finalResult = new ArrayList<Message>();
        try {
            result = restaurant.getMessages();
            for (Message message : result) {
            	if (message.getCurrentStatus() == Message.Status.INACTIVE) {
            		finalResult.add(message);
            	}
            }
        }
        finally {
        	pm.close();
        }

        finalResult = (ArrayList<Message>) sortMessages(finalResult, ascendingOrder);
        return finalResult;
    }
	
	/**
     * Get active messages in the datastore from a specific store
     * and returns them in a List structure
     * @param storeKey: 
     * 				the key of the store whose messages will be retrieved
     * @param ascendingOrder:
     * 				whether the list should be sorted
	 * 				in ascending order or not
     * @return all messages that are "ACTIVE" belonging to the given store
     */
	public static List<Message> getActiveMessagesFromStore(Key storeKey,
			boolean ascendingOrder) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant restaurant = pm.getObjectById(Restaurant.class, storeKey);
		
        List<Message> result = null;
        ArrayList<Message> finalResult = new ArrayList<Message>();
        try {
            result = restaurant.getMessages();
            for (Message message : result) {
            	if (message.getCurrentStatus() == Message.Status.ACTIVE) {
            		finalResult.add(message);
            	}
            }
        }
        finally {
        	pm.close();
        }

        finalResult = (ArrayList<Message>) sortMessages(finalResult, ascendingOrder);
        return finalResult;
    }
	
	/**
     * Get expired message in the datastore from a specific store
     * and returns them in a List structure
     * @param storeKey: 
     * 				the key of the store whose message will be retrieved
     * @param ascendingOrder:
     * 				whether the list should be sorted
	 * 				in ascending order or not
     * @return all messages that are "EXPIRED" belonging to the given store
     */
	public static List<Message> getExpiredMessagesFromStore(Key storeKey,
			boolean ascendingOrder) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant restaurant = pm.getObjectById(Restaurant.class, storeKey);
		
        List<Message> result = null;
        ArrayList<Message> finalResult = new ArrayList<Message>();
        try {
            result = restaurant.getMessages();
            for (Message message : result) {
            	if (message.getCurrentStatus() == Message.Status.EXPIRED) {
            		finalResult.add(message);
            	}
            }
        }
        finally {
        	pm.close();
        }

        finalResult = (ArrayList<Message>) sortMessages(finalResult, ascendingOrder);
        return finalResult;
    }
	
	/**
     * Put Message into datastore.
     * Stores the given Message instance in the datastore for this
     * store.
     * @param email
     * 			: the email of the Store where the message will be added
     * @param message
     * 			: the Message instance to store
     */
	public static void putMessage(Email email, Message message) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Key key = KeyFactory.createKey(Restaurant.class.getSimpleName(), email.getEmail());
			Restaurant restaurant = pm.getObjectById(Restaurant.class, key);
			tx.begin();
			restaurant.addMessage(message);
			restaurant.updateMessageVersion();
			tx.commit();
			log.info("Message \"" + message.getMessageTitle() + 
				"\" stored successfully in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
     * Put Message into datastore.
     * Stores the given Message instance in the datastore for this
     * store.
     * @param storeKey
     * 			: the key of the Store where the message will be added
     * @param message
     * 			: the Message instance to store
     */
	public static void putMessage(Key storeKey, Message message) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Restaurant restaurant = 
					pm.getObjectById(Restaurant.class, storeKey);
			tx.begin();
			restaurant.addMessage(message);
			restaurant.updateMessageVersion();
			tx.commit();
			log.info("Message \"" + message.getMessageTitle() + 
				"\" stored successfully in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
    * Delete Message from datastore.
    * Deletes the Message corresponding to the given key
    * from the datastore calling the PersistenceManager's 
    * deletePersistent() method.
    * @param key
    * 			: the key of the Message instance to delete
    */
	public static void deleteMessage(Key key) {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try {
			Restaurant restaurant = pm.getObjectById(Restaurant.class, key.getParent());
			Message message = pm.getObjectById(Message.class, key);
			String messageContent = message.getMessageTitle();
			tx.begin();
			restaurant.removeMessage(message);
			restaurant.updateMessageVersion();
			tx.commit();
			log.info("Message \"" + messageContent + 
                     "\" deleted successfully from datastore.");
		}
		catch (InexistentObjectException e) {
			e.printStackTrace();
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
    * Update Message attributes.
    * Update's the given Message's attributes in the datastore.
    * @param key
    * 			: the key of the Message whose attributes will be updated
    * @param messageType
    * 			: the type of the message
    * @param messageTitle
    * 			: the title of the message
    * @param messageAuthor
    * 			: the author of the message
    * @param messageTextContent
    * 			: the text content of the message
    * @param messageMultimediaContent
    * 			: the multimedia content of the message
    * @param messageURL
    * 			: the message URL
    * @param messageStartingDate
    * 			: the starting date of this message
    * @param messageEndingDate
    * 			: the ending date of this message
	* @throws MissingRequiredFieldsException 
    */
	public static void updateMessageAttributes(
			Key key, Message.MessageType messageType, 
			String messageTitle, String messageAuthor, 
			Text messageTextContent, BlobKey messageMultimediaContent,
			Link messageURL, Date messageStartingDate, Date messageEndingDate) 
                       throws MissingRequiredFieldsException {	
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Restaurant restaurant = pm.getObjectById(Restaurant.class, key.getParent());
		
		Transaction tx = pm.currentTransaction();
		try {
			Message message = pm.getObjectById(Message.class, key);
			tx.begin();
			message.setMessageType(messageType);
			message.setMessageTitle(messageTitle);
			message.setMessageAuthor(messageAuthor);
			message.setMessageTextContent(messageTextContent, 
					messageMultimediaContent, messageURL);
			message.setMessageMultimediaContent(messageMultimediaContent,
					messageTextContent, messageURL);
			message.setMessageURL(messageURL, 
					messageTextContent, messageMultimediaContent);
			message.setMessageStartingDate(messageStartingDate);
			message.setMessageEndingDate(messageEndingDate);
			
			restaurant.updateMessageVersion();
			tx.commit();
			log.info("Message \"" + message.getMessageTitle() + 
                     "\"'s attributes updated in datastore.");
		}
		finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
	 * Sort the given message list by release date using the
	 * BubbleSort algorithm.
	 * @param messages:
	 * 				the list of messages to sort
	 * @param ascendingOrder:
	 * 				whether the list should be sorted
	 * 				in ascending order or not
	 * @return the list of messages sorted by release date
	 */
	public static List<Message> sortMessages(List<Message> messages, 
			boolean ascendingOrder) {
		
		for (int i = 0; i < messages.size(); i++) {
			for (int j = 1; j < (messages.size() - i); j++) {
				Date message1Date = messages.get(j - 1).getMessageStartingDate();
				Date message2Date = messages.get(j).getMessageStartingDate();
				if (ascendingOrder) {
					if (message1Date.compareTo(message2Date) > 0) {
						Message tempMessage = messages.get(j - 1);
						messages.set(j - 1, messages.get(j));
						messages.set(j, tempMessage);
					}
				}
				else {
					if (message1Date.compareTo(message2Date) < 0) {
						Message tempMessage = messages.get(j - 1);
						messages.set(j - 1, messages.get(j));
						messages.set(j, tempMessage);
					}
				}
			}
		}
		
		return messages;
	}
	
}
