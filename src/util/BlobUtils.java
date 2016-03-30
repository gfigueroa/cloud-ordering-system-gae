/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;

/**
 * Utility class for blob and blobkey management.
 * 
 */

public class BlobUtils {
	
	/**
	 * Returns new a blobkey for an uploaded blob.
	 * If no blob was uploaded, then the original value of the
	 * blobkey is returned (which could also be null).
	 * This method is only used for 
	 * @param req
	 * 			: the HTTP Servlet Request from the Servlet
	 * @param paramName
	 * 			: the name of the HTTP blob parameter
	 * @param blobStoreService
	 * 			: the blobstore service initialized in the calling servlet
	 * @return a new BlobKey for this blob
	 */
	public static BlobKey assignBlobKey(HttpServletRequest req, String paramName, 
			BlobstoreService blobstoreService) {
        java.util.Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        List<BlobKey> blobKeys = blobs.get(paramName);
        if (blobKeys != null) {
        	BlobKey blobKey = blobKeys.get(0);
            final BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
            if (blobKey != null) {
            	if (blobInfo.getSize() > 0) {
            		return blobKey;
            	}
            	else {
            		blobstoreService.delete(blobKey);
            		return null;
            	}
            }
            else {
            	return null;
            }
		}
        else {
        	return null;
        }
	}
}