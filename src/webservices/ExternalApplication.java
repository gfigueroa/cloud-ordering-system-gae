/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package webservices;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import webservices.external_resources.AdditionalPropertiesResource;
import webservices.external_resources.CloudSyncResource;
import webservices.external_resources.CustomerProfileResource;
import webservices.external_resources.CustomerSmartCashResource;
import webservices.external_resources.DMSVResource;
import webservices.external_resources.ProductItemQueryResource;
import webservices.external_resources.ProductItemTypeListResource;
import webservices.external_resources.MessagesResource;
import webservices.external_resources.MyFavoritesResource;
import webservices.external_resources.OpinionPollResultsResource;
import webservices.external_resources.OrderDetailsResource;
import webservices.external_resources.OrderListResource;
import webservices.external_resources.PrivateNewsListResource;
import webservices.external_resources.RestaurantListResource;
import webservices.external_resources.RestaurantNewsListResource;
import webservices.external_resources.RestaurantOpinionPollListResource;
import webservices.external_resources.StoreTypeListResource;
import webservices.external_resources.RestaurantVersionsResource;
import webservices.external_resources.SetsResource;
import webservices.external_resources.StoreListResource;
import webservices.external_resources.SurveyListResource;
import webservices.external_resources.SystemResource;
import webservices.external_resources.UserGroupsResource;
import webservices.external_resources.UserRecommendationsResource;

/**
 * This class represents an instance of the Restlet Application
 */

public class ExternalApplication extends Application {

    /**
     * Creates a root Restlet that will receive all incoming calls
     * @return A router instance
     */
    @Override
    public Restlet createInboundRoot() {
        // Create a router Restlet that routes each call to a
        // new instance of each Resource
        Router router = new Router(getContext());
        
        // Define route to SystemResource
        router.attach("/system", SystemResource.class); // 2.1
        
        // Define route to StoreListResource
        router.attach("/storeList/{queryinfo}", 
        		StoreListResource.class); // 2.2
        // Define route to RestaurantListResource
        router.attach("/restaurantList", 
        		RestaurantListResource.class); // 1.9.2 // TODO: DEPRECATED Remove later
        
        // Define route to RestaurantVersionsResource
        router.attach("/restaurantVersions/{queryinfo}", 
        		RestaurantVersionsResource.class); // 2.3 // TODO: DEPRECATED Remove later
        
        // Define route to MenuItemQueryResource
        router.attach("/menuItemQuery/{queryinfo}", 
        		ProductItemQueryResource.class); // 2.4
        
        // Define route to MenuItemTypeListResource
        router.attach("/menuItemTypeList/{queryinfo}", 
        		ProductItemTypeListResource.class); // 2.5
        
        // Define route to OrderListResource
        router.attach("/orderList/{queryinfo}", 
        		OrderListResource.class); // 2.6
        router.attach("/orderList_Test/{queryinfo}", 
        		OrderListResource.class); // 1.9.6 // TODO: DEPRECATED Remove later
        
        // Define route to OrderDetailsResource
        router.attach("/orderDetails/{queryinfo}", 
        		OrderDetailsResource.class); // 2.7
        router.attach("/orderDetails_Test/{queryinfo}", 
        		OrderDetailsResource.class); // 1.9.7 // TODO: DEPRECATED Remove later
        
        // Define route to RestaurantTypeListResource
        router.attach("/restaurantTypeList", 
        		StoreTypeListResource.class); // 2.9
        router.attach("/restaurantTypeList/{queryinfo}", 
        		StoreTypeListResource.class); // 2.9 with no parameters
        
        // Define route to RestaurantNewsListResource
        router.attach("/restaurantNewsList/{queryinfo}", 
        		RestaurantNewsListResource.class); // 2.10
        
        // Define route to RestaurantOpinionPollListResource
        router.attach("/restaurantOpinionPollList/{queryinfo}", 
        		RestaurantOpinionPollListResource.class); // 2.11
        
        // Define route to CustomerProfileResource
        router.attach("/customerProfile/{queryinfo}", 
        		CustomerProfileResource.class); // 2.12
        
        // Define route to SurveyListResource
        router.attach("/surveyList/{queryinfo}", 
        		SurveyListResource.class); // 2.13
        
        // Define route to PrivateNewsListResource
        router.attach("/privateNewsList/{queryinfo}", 
        		PrivateNewsListResource.class); // 2.14
        
        // Define route to OpinionPollResultsResource
        router.attach("/opinionPollResults/{queryinfo}", 
        		OpinionPollResultsResource.class); // 2.15
        
        // Define route to AdditionalPropertiesResource
        router.attach("/additionalProperties/{queryinfo}", 
        		AdditionalPropertiesResource.class); // 2.16
        
        // Define route to SetsResource
        router.attach("/sets/{queryinfo}", SetsResource.class); // 2.17
        
        // Define route to MessagesResource
        router.attach("/messages/{queryinfo}", MessagesResource.class); // 2.20
        
        // Define route to DMSVResource
        router.attach("/dmsv", DMSVResource.class); // 2.21
        
        // Define route to CustomerSmartCashResource
        router.attach("/customerSmartCash/{queryinfo}", 
        		CustomerSmartCashResource.class); // 2.22
        
        // Define route to CloudSyncResource
        router.attach("/cloudSync/{queryinfo}", CloudSyncResource.class); // 2.23
        
        // Define route to UserGroupResource
        router.attach("/userGroups/{queryinfo}", UserGroupsResource.class); // 2.24

        // Define route to UserRecommendationsResource
        router.attach("/userRecommendations/{queryinfo}", 
        		UserRecommendationsResource.class); // 2.25
        
        // Define route to MyFavoritesResource
        router.attach("/myFavorites/{queryinfo}", 
        		MyFavoritesResource.class); // 2.26
        
        return router;
    }

}
