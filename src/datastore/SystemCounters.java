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

/**
 * This class represents the SystemCounters table.
 * The SystemCounters table stores the number of times that particular entities in the
 * datastore have been accessed.
 * It is managed as a JDO to be stored in and retrieved from the GAE datastore.
 * 
 */

@SuppressWarnings("serial")
@PersistenceCapable
public class SystemCounters implements Serializable {

	public static enum Counter {
		PRODUCT_ITEM
	}
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long key;

    @Persistent
    private Long productItemCounter;
    
    @Persistent
    private Date systemCountersTime;

    /**
     * SystemCounters constructor.
     */
    public SystemCounters() {
    	productItemCounter = 0L;
    	
    	Date now = new Date();
    	this.systemCountersTime = now;
    }

    /**
     * Get SystemCounters key.
     * @return systemCounters key
     */
    public Long getKey() {
        return key;
    }
    
    /**
     * Get product item counter. This gets the number of times
     * that product items have been read from the datastore.
     * @return product item counter
     */
    public Long getProductItemCounter() {
    	return productItemCounter;
    }

    /**
     * Get time when last update was made.
     * @return systemCounters time
     */
    public Date getSystemCountersTime() {
        return systemCountersTime;
    }
    
    /**
     * Compare this systemCounters instance with another syste,
     * @param o
     * 			: the object to compare
     * @returns true if the object to compare is equal to this SystemCounters, 
	 *			false otherwise
     */
    @Override
    public boolean equals(Object o){
        if ( this == o ) return true;
        if (!(o instanceof SystemCounters ) ) return false;
        SystemCounters systemCounters = (SystemCounters) o;
        return (this.getKey() == systemCounters.getKey());
    }
    
    /**
     * Increase the given counter by the given increment.
     */
    public void increaseCounter(Counter counter, Long increment) {
    	
    	switch (counter) {
    		case PRODUCT_ITEM:
    			increaseProductItemCounter(increment);
    			break;
    		default:
    			return;
    	}
    	
    	systemCountersTime = new Date();
    }
    
    /**
     * Increase the product item counter by the given increment.
     */
    private void increaseProductItemCounter(Long increment) {
    	productItemCounter += increment;
    	systemCountersTime = new Date();
    }
    
}
