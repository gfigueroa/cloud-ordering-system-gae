/*
Copyright (c) 2011, 智慧人科技服務股份有限公司 (Smart Personalized Service Technology, Inc.) 
All rights reserved.
*/

package util;

import java.util.HashMap;
import java.io.Serializable;
/**
 * Utility class for storing terminologies in different languages 
 * for the web server pages.
 * 
 */

public class Dictionary implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2389998361033740665L;

	public static enum Language {
		ENGLISH, CHINESE
	}
	
	public static String getLanguageString(Language language) {
		if (language == null) {
			return "";
		}
		
		switch (language) {
			case ENGLISH:
				return "EN";
			case CHINESE:
				return "CH";
			default:
				return "";
		}
	}
	
	private HashMap<String, Term> terms;
	
	/**
	 * Private constructor prevents instantiation from other classes.
	 */
	private Dictionary() {
		terms = new HashMap<String, Term>();
		loadTerms();
	}
	
	/**
	 * Get an instance of the Dictionary class.
	 * @return an instance of Dictionary
	 */
	public static Dictionary getInstance() {
		return DictionaryHolder.instance;
	}
	
	/**
	 * DictionaryHolder is loaded on the first execution of 
	 * Dictionary.getInstance() or the first access to 
	 * Dictionary.INSTANCE, not before.
	 */
	private static class DictionaryHolder {
		public static final Dictionary instance = new Dictionary();
	}
	
	/**
	 * Obtains the translation of this English term
	 * into the language given as parameter.
	 * @param englishTerm
	 * 			: the term in English to be translated
	 * @param language
	 * 			: the language desired for the translation
	 * @return the translation of this English term into the 
	 * 			desired language
	 */
	public String translate(String englishTerm, Language language) {
		
		Term term = terms.get(englishTerm);
		if (term == null) {
			return "";
		}
		
		switch (language) {
			case ENGLISH:
				return term.englishTerm;
			case CHINESE:
				return term.chineseTerm;
			default:
				return "";	
		}
	}
	
	/**
	 * Loads all the terms to be used by the dictionary.
	 */
	private void loadTerms() {
		terms.put("A BRANCH MUST OFFER AT LEAST ONE OF THE THREE DIFFERENT SERVICES", new Term("A branch must offer at least one of the three different services", "一間店家至少要提供3種不同服務類型"));
		terms.put("A NEW ACCOUNT", new Term("a new account", "新帳號"));
		terms.put("ACCEPTED", new Term("Accepted", "受理"));
		terms.put("ACTIONS", new Term("Actions", "操作"));
		terms.put("ACTIVE", new Term("ACTIVE", "進行中"));
		terms.put("ACTIVE NEWS", new Term("Active News", "新聞活動進行中"));
		terms.put("ACTIVE OPINION POLLS", new Term("Active Opinion Polls", "民調進行中"));
		terms.put("ACTIVE SURVEYS", new Term("Active Surveys", "進行中民調"));
		terms.put("ADD A BRANCH", new Term("Add a branch", "新增分店"));
		terms.put("ADD A RETAIL STORE", new Term("Add a Retail Store", "新增店家"));
		terms.put("ADD ADDITIONAL PROPERTY TYPE", new Term("Add Additional Property Type", "新增選項類型"));
		terms.put("ADD ADMINISTRATOR", new Term("Add Administrator", "新增管理者"));
		terms.put("ADD CUSTOMER", new Term("Add Customer", "新增顧客"));
		terms.put("ADD NEW ADDITIONAL PROPERTY TYPE", new Term("Add New Additional Property Type", "增加選項類型"));
		terms.put("ADD NEW ADMINISTRATOR", new Term("Add New Administrator", "新增管理者"));
		terms.put("ADD NEW BRANCH", new Term("Add New Branch", "新增店家"));
		terms.put("ADD NEW CUSTOMER", new Term("Add New Customer", "新增顧客"));
		terms.put("ADD NEW PRODUCT ITEM", new Term("Add New Product Item", "新增商品項目"));
		terms.put("ADD NEW PRODUCT TYPE", new Term("Add New Product Type", "新增商品類型"));
		terms.put("ADD NEW REGION", new Term("Add New Region", "新增地區"));
		terms.put("ADD NEW RETAIL STORE", new Term("Add New Retail Store", "新增店家"));
		terms.put("ADD NEW RETAIL STORE TYPE", new Term("Add New Retail Store Type", "新增店家類型"));
		terms.put("ADD NEWS", new Term("Add News", "新增新聞"));
		terms.put("ADD OPINION POLL", new Term("Add Opinion Poll", "新增民調"));
		terms.put("ADD PRODUCT ITEM", new Term("Add Product Item", "新增商品"));
		terms.put("ADD PRODUCT TYPE", new Term("Add Product Type", "新增商品類型"));
		terms.put("ADD REGION", new Term("Add Region", "新增地區"));
		terms.put("ADD RETAIL STORE NEWS", new Term("Add Retail Store News", "新增店家新聞"));
		terms.put("ADD RETAIL STORE OPINION POLL", new Term("Add Retail Store Opinion Poll", "新增店家民調"));
		terms.put("ADD RETAIL STORE TYPE", new Term("Add Retail Store Type", "新增店家類型"));
		terms.put("ADD SURVEY", new Term("Add Survey", "新增民調"));
		terms.put("ADD SURVEY OPINION POLL", new Term("Add Survey Opinion Poll", "新增題組民調"));
		terms.put("ADDITIONAL PROPERTIES", new Term("Additional Properties", "附加選項"));
		terms.put("ADDITIONAL PROPERTY NAME", new Term("Additional Property Name", "類型名稱"));
		terms.put("ADDITIONAL PROPERTY SUCCESSFULLY UPDATED", new Term("Additional Property successfully updated", "附加選項上傳成功"));
		terms.put("ADDITIONAL PROPERTY TYPE INFORMATION", new Term("Additional Property Type Information", "附加選項類型資訊"));
		terms.put("ADDITIONAL PROPERTY TYPE SUCCESSFULLY ADDED TO THE RETAIL STORE", new Term("Additional property type successfully added to the retail store", "附加選項新增成功"));
		terms.put("ADDRESS", new Term("Address", "地址"));
		terms.put("ADMINISTRATOR LIST", new Term("Administrator List", "管理者名單"));
		terms.put("ADMINISTRATOR NAME", new Term("Administrator Name", "管理者名稱"));
		terms.put("ADMINISTRATOR PASSWORD CHANGED SUCCESSFULLY", new Term("Administrator password changed successfully", "管理者密碼修改成功"));
		terms.put("ADMINISTRATORS", new Term("Administrators", "管理者"));
		terms.put("AGREE", new Term("Agree", "同意"));
		terms.put("ALL DAY", new Term("All day", "整天"));
		terms.put("ALLOW RESPONSES", new Term("Allow Responses", "接受回應"));
		terms.put("ALLOWS RESPONSE", new Term("Allows Response", "接受回應"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS ADDITIONAL PROPERTY TYPE", new Term("Are you sure you want to delete this Additional Property Type", "確定刪除此附加選項"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS ADMINISTRATOR", new Term("Are you sure you want to delete this Administrator", "確定刪除此管理者"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS BRANCH", new Term("Are you sure you want to delete this Branch", "確定刪除此店家"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS CUSTOMER", new Term("Are you sure you want to delete this Customer", "確定刪除此顧客"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS NEW", new Term("Are you sure you want to delete this New", "確定刪除這則新聞"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS NEWS", new Term("Are you sure you want to delete this News", "是否確定刪除此新聞"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS OPINION POLL", new Term("Are you sure you want to delete this Opinion Poll", "確定刪除此民調"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS ORDER", new Term("Are you sure you want to delete this Order", "確定刪除此訂單"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS PRODUCT ITEM", new Term("Are you sure you want to delete this Product Item", "確定刪除此項商品"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS PRODUCT ITEM TYPE", new Term("Are you sure you want to delete this Product Item Type", "確定刪除此項商品類型"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS REGION", new Term("Are you sure you want to delete this Region", "確定刪除此地區"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS RETAIL STORE", new Term("Are you sure you want to delete this Retail Store", "確定刪除此店家"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS RETAIL STORE TYPE", new Term("Are you sure you want to delete this Retail Store Type", "確定刪除此店家類型"));
		terms.put("ARE YOU SURE YOU WANT TO DELETE THIS SURVEY", new Term("Are you sure you want to delete this Survey", "確定刪除這份題組民調"));
		terms.put("AVAILABLE", new Term("Available", "供應中"));
		terms.put("BACK TO SURVEYS", new Term("Back to Surveys", "回到題組民調"));
		terms.put("BATCH UPLOAD", new Term("Batch Upload", "資料上傳"));
		terms.put("BINARY", new Term("Binary", "二分選項"));
		terms.put("BINARY CHOICE 1", new Term("Binary Choice 1", "二分選項1"));
		terms.put("BINARY CHOICE 2", new Term("Binary Choice 2", "二分選項2"));
		terms.put("BONUS", new Term("Bonus", "紅利點數"));
		terms.put("BRANCH", new Term("Branch", "分店"));
		terms.put("BRANCH EMAIL", new Term("Branch Email", "分店email"));
		terms.put("BRANCH INFORMATION", new Term("Branch Information", "分店資訊"));
		terms.put("BRANCH NAME", new Term("Branch Name", "分店名稱"));
		terms.put("BRANCH SUCCESSFULLY ADDED TO THE RETAIL STORE", new Term("Branch successfully added to the retail store", "開店成功"));
		terms.put("BRANCH SUCCESSFULLY UPDATED", new Term("Branch successfully updated", "開店上傳成功"));
		terms.put("BRANCHES", new Term("Branches", "開店"));
		terms.put("BREAKFAST (07:00 - 12:00)", new Term("Breakfast (07:00 - 12:00)", "早餐 (07:00 – 12:00)"));
		terms.put("BREAKFAST + DINNER", new Term("Breakfast + Dinner", "早餐+晚餐"));
		terms.put("BREAKFAST + LUNCH", new Term("Breakfast + Lunch", "早餐+午餐"));
		terms.put("CANCELLED", new Term("Cancelled", "顧客取消"));
		terms.put("CHANGE PASSWORD", new Term("Change password", "更改密碼"));
		terms.put("CHINESE", new Term("Chinese", "中文"));
		terms.put("CHOOSE FILE", new Term("Choose File", "選擇檔案"));
		terms.put("CLINIC", new Term("Clinic", "診所"));
		terms.put("CLOSE", new Term("Close", "關閉"));
		terms.put("CLOSED", new Term("Closed", "銀貨兩訖"));
		terms.put("CLOSING TIME", new Term("Closing Time", "休息時間"));
		terms.put("COMMENTS", new Term("Comments", "補充說明"));
		terms.put("CONFIGURATION", new Term("Configuration", "配置"));
		terms.put("CONFIRM PASSWORD", new Term("Confirm Password", "密碼確認"));
		terms.put("CONTENT", new Term("Content", "內容"));
		terms.put("CONTINUE ADDING ADDITIONAL PROPERTY TYPES", new Term("Continue adding additional property types", "繼續新增選項類型"));
		terms.put("CONTINUE ADDING BRANCHES", new Term("Continue adding branches", "繼續新增店家"));
		terms.put("CONTINUE ADDING OPINION POLLS", new Term("Continue adding opinion polls", "繼續新增民調"));
		terms.put("CONTINUE ADDING PRODUCT ITEMS", new Term("Continue adding Product Items", "繼續新增商品"));
		terms.put("CONTINUE ADDING PRODUCT TYPES", new Term("Continue adding product types", "繼續新增商品類型"));
		terms.put("CONTINUE ADDING RETAIL STORE NEWS", new Term("Continue adding retail store news", "繼續新增店家新聞"));
		terms.put("CONTINUE ADDING SURVEYS", new Term("Continue adding surveys", "繼續新增題組民調"));
		terms.put("COUPONS", new Term("Coupons", "優惠券"));
		terms.put("CREATE", new Term("Create", "建立"));
		terms.put("CREATE A NEW ACCOUNT", new Term("Create a New Account", "建立新帳號"));
		terms.put("CREATE YOUR ACCOUNT TO GAIN ACCESS TO THE SYSTEM", new Term("Create your account to gain access to the system", "初次使用，請建立一個新帳號登入系統。"));
		terms.put("CREATION DATE", new Term("Creation Date", "建立日期"));
		terms.put("CURRENT ORDERS", new Term("Current Orders", "未結束訂單"));
		terms.put("CURRENT RESPONSES", new Term("Current Responses", "目前回應"));
		terms.put("CURRENT STATISTICS", new Term("Current Statistics", "當前統計"));
		terms.put("CUSTOMER E-MAIL", new Term("Customer E-mail", "顧客e-mail"));
		terms.put("CUSTOMER LIST", new Term("Customer List", "顧客名單"));
		terms.put("CUSTOMER NAME", new Term("Customer Name", "顧客名稱"));
		terms.put("CUSTOMER PASSWORD CHANGED SUCCESSFULLY", new Term("Customer password changed successfully", "顧客密碼修改成功"));
		terms.put("CUSTOMERS", new Term("Customers", "顧客"));
		terms.put("DATE", new Term("Date", "日期"));
		terms.put("DELETE", new Term("Delete", "刪除"));
		terms.put("DELIVERY", new Term("Delivery", "外送"));
		terms.put("DELIVERY ADDRESS", new Term("Delivery Address", "送達地址"));
		terms.put("DELIVERY FEE", new Term("Delivery Fee", "外送費"));
		terms.put("DELIVERY METHOD", new Term("Delivery Method", "外送方式"));
		terms.put("DELIVERY TYPE", new Term("Delivery Type", "外送方式"));
		terms.put("DESCRIPTION", new Term("Description", "描述"));
		terms.put("DINE-IN", new Term("Dine-In", "在店內用"));
		terms.put("DINNER (16:30 - 23:30)", new Term("Dinner (16:30 – 23:30)", "晚餐 (16:30 – 23:30)"));
		terms.put("DISAGREE", new Term("Disagree", "不同意"));
		terms.put("DISCOUNT", new Term("Discount", "折扣"));
		terms.put("DISCOUNT(BETWEEN 0-1)", new Term("Discount(between 0-1)", "折扣(0～1) 9折=0.1, 8折=0.2, 85折=0.15, 7折=0.3…"));
		terms.put("DMSV (DAILY MULTI SPIRITUAL VITAMINE)", new Term("DMSV (Daily Multi Spiritual Vitamine)", "每日綜合心靈維他命"));
		terms.put("EDIT", new Term("Edit", "編輯"));
		terms.put("EDIT A BRANCH", new Term("Edit a Branch", "編輯分店"));
		terms.put("EDIT ADDITIONAL PROPERTY TYPE", new Term("Edit Additional Property Type", "編輯附加選項"));
		terms.put("EDIT ADMINISTRATOR", new Term("Edit Administrator", "編輯管理者"));
		terms.put("EDIT ADMINISTRATOR PASSWORD", new Term("Edit Administrator Password", "編輯管理者密碼"));
		terms.put("EDIT CUSTOMER", new Term("Edit Customer", "編輯顧客資料"));
		terms.put("EDIT PRODUCT ITEM", new Term("Edit Product Item", "編輯商品"));
		terms.put("EDIT PRODUCT TYPE", new Term("Edit Product Type", "編輯商品類型"));
		terms.put("EDIT PROFILE", new Term("Edit Profile", "編輯資訊"));
		terms.put("EDIT REGION", new Term("Edit Region", "編輯地區"));
		terms.put("EDIT RETAIL STORE", new Term("Edit Retail Store", "編輯店家資訊"));
		terms.put("EDIT RETAIL STORE NEWS", new Term("Edit Retail Store News", "編輯店家新聞"));
		terms.put("EDIT RETAIL STORE OPINION POLL", new Term("Edit Retail Store Opinion Poll", "編輯店家民調"));
		terms.put("EDIT RETAIL STORE PASSWORD", new Term("Edit Retail Store Password", "設定店家密碼"));
		terms.put("EDIT RETAIL STORE PROFILE", new Term("Edit Retail store profile", "編輯店家資訊"));
		terms.put("EDIT RETAIL STORE TYPE", new Term("Edit Retail Store Type", "編輯店家類型"));
		terms.put("EDIT SURVEY", new Term("Edit Survey", "編輯題組民調"));
		terms.put("EDIT SURVEY OPINION POLL", new Term("Edit Survey Opinion Poll", "編輯題組民調"));
		terms.put("EDIT SYSTEM", new Term("Edit System", "編輯系統"));
		terms.put("E-MAIL", new Term("E-mail", "E-mail"));
		terms.put("EMAIL CONFIRMATION", new Term("Email Confirmation", "Email確認"));
		terms.put("EN_ROUTE", new Term("En_Route", "商品送出"));
		terms.put("ENDING DATE", new Term("Ending Date", "結束日期"));
		terms.put("ENGLISH", new Term("English", "英文"));
		terms.put("EN-ROUTE", new Term("En-Route", "商品送出"));
		terms.put("EXPIRATION DATE", new Term("Expiration Date", "結束日期"));
		terms.put("EXPIRED", new Term("EXPIRED", "結束"));
		terms.put("FALSE", new Term("false", "假"));
		terms.put("FEMALE", new Term("Female", "女性"));
		terms.put("FOOD - ACTIVE NEWS", new Term("Food - Active News", "餐飲-新聞活動進行中"));
		terms.put("FOOD - ACTIVE OPINION POLLS", new Term("Food - Active Opinion Polls", "餐飲-民調進行中"));
		terms.put("FOOD AND DRINK", new Term("Food and Drink", "餐飲"));
		terms.put("FREE RESPONSE", new Term("Free Response", "自由填答"));
		terms.put("GENDER", new Term("Gender", "性別"));
		terms.put("GLOBAL", new Term("Global", "公布在最新消息"));
		terms.put("GLOBAL SURVEY", new Term("Global Survey", "公布在最新消息"));
		terms.put("GOD DWELLING PLACE", new Term("God Dwelling Place", "豐盛居所"));
		terms.put("HAS CONVENIENCE STORE DELIVERY", new Term("Has Convenience Store Delivery", "超商取貨"));
		terms.put("HAS DELIVERY SERVICE", new Term("Has Delivery Service", "外送服務"));
		terms.put("HAS DINE-IN SERVICE", new Term("Has Dine-In Service", "在店內用"));
		terms.put("HAS POSTAL DELIVERY", new Term("Has Postal Delivery", "郵寄"));
		terms.put("HAS POSTAL OFFICE DELIVERY", new Term("Has Postal Office Delivery", "郵寄"));
		terms.put("HAS REGULAR DELIVERY", new Term("Has Regular Delivery", "一般外送"));
		terms.put("HAS TAKE-OUT SERVICE", new Term("Has Take-Out Service", "到店取貨"));
		terms.put("HAS UPS DELIVERY", new Term("Has UPS Delivery", "宅配"));
		terms.put("IMAGE", new Term("Image", "商品圖片"));
		terms.put("INACTIVE", new Term("INACTIVE", "未開始"));
		terms.put("INACTIVE NEWS", new Term("Inactive News", "新聞活動未開始"));
		terms.put("INACTIVE OPINION POLLS", new Term("Inactive Opinion Polls", "民調未開始"));
		terms.put("INACTIVE SURVEYS", new Term("Inactive Surveys", "未開始民調"));
		terms.put("INVITATION", new Term("Invitation", "不公開"));
		terms.put("INVITATION SURVEY", new Term("Invitation Survey", "不公開"));
		terms.put("LANGUAGE", new Term("Language", "語言"));
		terms.put("LOG IN", new Term("Log in", "登入"));
		terms.put("LOGO", new Term("Logo", "商標圖片"));
		terms.put("LOGOUT", new Term("Logout", "登出"));
		terms.put("LUNCH (12:00 - 16:30)", new Term("Lunch (12:00 – 16:30)", "午餐 (12:00 – 16:30)"));
		terms.put("LUNCH + DINNER", new Term("Lunch + Dinner", "午餐+晚餐"));
		terms.put("MAKE RESULTS PUBLIC", new Term("Make results public", "公開結果"));
		terms.put("MALE", new Term("Male", "男性"));
		terms.put("MAX. RESPONSES", new Term("Max. Responses", "回應人數上限"));
		terms.put("MEMBERSHIPS", new Term("Memberships", "會員"));
		terms.put("MENU ITEM TYPE VERSION", new Term("Menu Item Type Version", "商品類型版本"));
		terms.put("MENU VERSION", new Term("Menu Version", "商品目錄版本"));
		terms.put("MULTI-CHOICE VALUES", new Term("Multi-Choice Values", "選項內容"));
		terms.put("MULTIMEDIA CONTENT", new Term("Multimedia Content", "多媒體內容"));
		terms.put("MULTIPLE CHOICE", new Term("Multiple Choice", "多重選項"));
		terms.put("MULTIPLE SELECTION", new Term("Multiple Selection", "複選"));
		terms.put("MULTIPLE-CHOICE NO. ITEMS", new Term("Multiple-Choice No. Items", "選項數目"));
		terms.put("MUSIC RECOMMENDATION", new Term("Music Recommendation", "音樂推薦"));
		terms.put("NAME", new Term("Name", "會員名稱"));
		terms.put("NAME & CONTACT INFORMATION", new Term("Name & Contact Information", "名稱 & 連絡資訊"));
		terms.put("NEUTRAL", new Term("Neutral", "沒意見"));
		terms.put("NEW PASSWORD", new Term("New Password", "設定新密碼"));
		terms.put("NEWS", new Term("News", "新聞"));
		terms.put("NEWS & POLLS", new Term("News & Polls", "新聞&民調"));
		terms.put("NEWS AND OPINION POLLS", new Term("News and Opinion Polls", "新聞民調"));
		terms.put("NEWS CONTENT", new Term("News Content", "新聞內容"));
		terms.put("NEWS HISTORY", new Term("News History", "新聞紀錄"));
		terms.put("NEWS IS PRIVATE", new Term("News is private", "公布在店家"));
		terms.put("NEWS SUCCESSFULLY CREATED", new Term("News successfully created", "新聞新增成功"));
		terms.put("NEWS SUCCESSFULLY UPDATED", new Term("News successfully updated", "新聞上傳成功"));
		terms.put("NEWS TITLE", new Term("News Title", "新聞標題"));
		terms.put("NO", new Term("No", "無"));
		terms.put("NO FILE CHOSEN", new Term("No file chosen", "未選擇檔案"));
		terms.put("NUMBER OF PEOPLE", new Term("Number of People", "人數"));
		terms.put("OLDEST APP VERSION SUPPORTED", new Term("Oldest App version supported", "舊版APP支援"));
		terms.put("OPENING TIME", new Term("Opening Time", "營業時間"));
		terms.put("OPINION POLL CONTENT", new Term("Opinion Poll Content", "民調內容"));
		terms.put("OPINION POLL HISTORY", new Term("Opinion Poll History", "民調紀錄"));
		terms.put("OPINION POLL PRIORITY", new Term("Opinion Poll Priority", "民調順位"));
		terms.put("OPINION POLL SUCCESSFULLY CREATED", new Term("Opinion poll successfully created", "民調新增成功"));
		terms.put("OPINION POLL SUCCESSFULLY UPDATED", new Term("Opinion Poll successfully updated", "民調上傳成功"));
		terms.put("OPINION POLL TITLE", new Term("Opinion Poll Title", "民調標題"));
		terms.put("OPINION POLL TYPE", new Term("Opinion Poll Type", "民調類型"));
		terms.put("OPINION POLLS", new Term("Opinion Polls", "民調"));
		terms.put("OPINION-POLL", new Term("Opinion-Poll", "民調"));
		terms.put("ORDER CONTACT PHONE", new Term("Order Contact Phone", "連絡電話"));
		terms.put("ORDER DETAIL", new Term("Order Detail", "訂單明細"));
		terms.put("ORDER HISTORY", new Term("Order History", "已結束訂單"));
		terms.put("ORDER INFORMATION", new Term("Order Information", "訂單資訊"));
		terms.put("ORDER NUMBER", new Term("Order Number", "訂單編號"));
		terms.put("ORDER STATUS", new Term("Order Status", "訂單狀態"));
		terms.put("ORDER SUCCESSFULLY UPDATED", new Term("Order successfully updated", "訂單上傳成功"));
		terms.put("ORDER TIME", new Term("Order Time", "訂購時間"));
		terms.put("ORDER TYPE", new Term("Order Type", "訂單類型"));
		terms.put("ORDERS", new Term("Orders", "訂單"));
		terms.put("PASSWORD", new Term("Password", "密碼"));
		terms.put("PHONE NUMBER", new Term("Phone number", "電話"));
		terms.put("POLL CURRENT RESPONSES", new Term("Poll Current Responses", "目前民調"));
		terms.put("PRICE", new Term("Price", "價格"));
		terms.put("PRINT", new Term("Print", "列印"));
		terms.put("PRIORITY", new Term("Priority", "優先順位"));
		terms.put("PRIVATE", new Term("Private", "公布在店家"));
		terms.put("PRIVATE SURVEY", new Term("Private Survey", "公布在店家"));
		terms.put("PROCESS", new Term("Process", "處理"));
		terms.put("PROCESS ORDER", new Term("Process Order", "處理訂單"));
		terms.put("PROCESSING", new Term("Processing", "處理中"));
		terms.put("PRODUCT ITEM LIST", new Term("Product Item List", "商品目錄"));
		terms.put("PRODUCT ITEM NAME", new Term("Product Item Name", "商品名稱"));
		terms.put("PRODUCT ITEM SUCCESSFULLY ADDED TO THE RETAIL STORE'S MENU", new Term("Product item successfully added to the retail store's menu", "商品成功新增至商品目錄"));
		terms.put("PRODUCT ITEM SUCCESSFULLY UPDATED", new Term("Product item successfully updated", "商品上傳成功"));
		terms.put("PRODUCT ITEM TYPE", new Term("Product Item Type", "商品類型"));
		terms.put("PRODUCT ITEM TYPE SUCCESSFULLY ADDED TO THE RETAIL STORE", new Term("Product item type successfully added to the retail store", "商品類型成功新增至店家"));
		terms.put("PRODUCT ITEM TYPE SUCCESSFULLY UPDATED", new Term("Product item type successfully updated", "商品類型成功上傳"));
		terms.put("PRODUCT ITEMS", new Term("Product Items", "商品項目"));
		terms.put("PRODUCT TYPE", new Term("Product Type", "商品類型"));
		terms.put("PRODUCT TYPE INFORMATION", new Term("Product Type Information", "商品類型資訊"));
		terms.put("PRODUCT TYPE LIST", new Term("Product Type List", "商品類型目錄"));
		terms.put("PRODUCT TYPE NAME", new Term("Product Type Name", "類型名稱"));
		terms.put("PRODUCT TYPES", new Term("Product Types", "商品類型"));
		terms.put("PRODUCTS", new Term("Products", "商品"));
		terms.put("PROPERTY TYPE NAME", new Term("Property Type Name", "類型名稱"));
		terms.put("QUANTITY", new Term("Quantity", "數量"));
		terms.put("QUESTION", new Term("Question", "問題"));
		terms.put("RATING", new Term("Rating", "評分選項"));
		terms.put("RATING HIGH VALUE", new Term("Rating High Value", "最高分"));
		terms.put("RATING LOW VALUE", new Term("Rating Low Value", "最低分"));
		terms.put("READY", new Term("Ready", "備妥"));
		terms.put("REGION", new Term("Region", "地區"));
		terms.put("REGION INFORMATION", new Term("Region Information", "地區資訊"));
		terms.put("REGION LIST", new Term("Region List", "地區列表"));
		terms.put("REGION NAME", new Term("Region Name", "地區名稱"));
		terms.put("REGIONS", new Term("Regions", "地區"));
		terms.put("RELEASE DATE", new Term("Release Date", "發布日期"));
		terms.put("RETAIL STORE", new Term("Retail Store", "店家"));
		terms.put("RETAIL STORE BRANCHES", new Term("Retail Store Branches", "開店"));
		terms.put("RETAIL STORE INFORMATION", new Term("Retail Store Information", "店家資訊"));
		terms.put("RETAIL STORE LIST", new Term("Retail Store List", "店家名單"));
		terms.put("RETAIL STORE NAME", new Term("Retail Store Name", "店家名稱"));
		terms.put("RETAIL STORE NEWS", new Term("Retail Store News", "店家新聞"));
		terms.put("RETAIL STORE NEWS INFORMATION", new Term("Retail Store News Information", "店家新聞"));
		terms.put("RETAIL STORE OPINION POLL", new Term("Retail Store Opinion Poll", "店家民調"));
		terms.put("RETAIL STORE OPINION POLL INFORMATION", new Term("Retail Store Opinion Poll Information", "店家民調資訊"));
		terms.put("RETAIL STORE PASSWORD CHANGED SUCCESSFULLY", new Term("Retail Store password changed successfully", "店家密碼修改成功"));
		terms.put("RETAIL STORE PROFILE", new Term("Retail Store Profile", "店家資訊"));
		terms.put("RETAIL STORE SUCCESSFULLY UPDATED", new Term("Retail Store successfully updated", "店家上傳成功"));
		terms.put("RETAIL STORE TYPE", new Term("Retail Store type", "店家類型"));
		terms.put("RETAIL STORE TYPE INFORMATION", new Term("Retail Store Type Information", "店家類型資訊"));
		terms.put("RETAIL STORE TYPE LIST", new Term("Retail Store Type List", "店家類型目錄"));
		terms.put("RETAIL STORE TYPES", new Term("Retail Store Types", "店家類型"));
		terms.put("REVOKED", new Term("Revoked", "預留取消"));
		terms.put("SALON", new Term("Salon", "沙龍"));
		terms.put("SELECT GENDER", new Term("Select Gender", "選擇性別"));
		terms.put("SELECT REGION", new Term("Select Region", "選擇地區"));
		terms.put("SELECT RETAIL STORE TYPE", new Term("Select Retail Store Type", "選擇店家類型"));
		terms.put("SELECT TYPE", new Term("Select Type", "選擇類型"));
		terms.put("SERVICE PROVIDERS", new Term("Service Providers", "服務供應者"));
		terms.put("SERVICE TIME", new Term("Service Time", "供應時段"));
		terms.put("SERVICE TYPE", new Term("Service Type", "服務類型"));
		terms.put("SERVICES", new Term("Services", "服務"));
		terms.put("SERVING TIME (MINUTES)", new Term("Serving time (minutes)", "準備時間 (分鐘)"));
		terms.put("SHOPPING", new Term("Shopping", "商城"));
		terms.put("SIGN IN", new Term("Sign in", "登入"));
		terms.put("SMART BONUS", new Term("Smart Bonus", "智慧紅利"));
		terms.put("SMART CASH", new Term("Smart Cash", "智慧現金"));
		terms.put("SOME OR ALL OF THE INFORMATION PROVIDED DOES NOT MATCH ANY USER IN OUR DATABASE", new Term("Some or all of the information provided does not match any user in our database", "所提供資訊與會員資料庫沒有完全符合"));
		terms.put("STAMPS", new Term("Stamps", "印花標誌"));
		terms.put("STARTING DATE", new Term("Starting Date", "開始日期"));
		terms.put("STATUS", new Term("Status", "狀態"));
		terms.put("STORE LIST VERSION", new Term("Store list version", "店家名單"));
		terms.put("STORE LIST VERSION (FOOD AND DRINK)", new Term("Store list version (Food and Drink)", "店家名單(餐飲類)"));
		terms.put("STORE LIST VERSION (NEWS/POLLS)", new Term("Store list version (News/Polls)", "店家名單(新聞/民調)"));
		terms.put("STORE LIST VERSION (SALONS)", new Term("Store list version (Salons)", "店家名單(沙龍)"));
		terms.put("STORE LIST VERSION (SHOPPING)", new Term("Store list version (Shopping)", "店家名單(商城)"));
		terms.put("STORE TYPE LIST VERSION", new Term("Store type list version", "店家類型"));
		terms.put("STRONGLY AGREE", new Term("Strongly Agree", "非常同意"));
		terms.put("STRONGLY DISAGREE", new Term("Strongly Disagree", "非常不同意"));
		terms.put("SUBTOTAL", new Term("Subtotal", "小計"));
		terms.put("SURVEY", new Term("Survey", "題組民調"));
		terms.put("SURVEY DESCRIPTION", new Term("Survey Description", "題組描述"));
		terms.put("SURVEY HISTORY", new Term("Survey History", "民調紀錄"));
		terms.put("SURVEY SUCCESSFULLY CREATED", new Term("Survey successfully created", "題組民調新增成功"));
		terms.put("SURVEY SUCCESSFULLY UPDATED", new Term("Survey successfully updated", "題組民調上傳成功"));
		terms.put("SURVEY TITLE", new Term("Survey Title", "民調標題"));
		terms.put("SURVEY TYPE", new Term("Survey Type", "民調類型"));
		terms.put("SURVEYS", new Term("Surveys", "題組民調"));
		terms.put("SYSTEM", new Term("System", "系統"));
		terms.put("SYSTEM INFORMATION", new Term("System Information", "系統資訊"));
		terms.put("SYSTEM LAST MODIFIED ON", new Term("System last modified on", "上次修改"));
		terms.put("SYSTEM UPDATED SUCCESSFULLY", new Term("System updated successfully", "系統更新成功"));
		terms.put("TAKE-OUT", new Term("Take-out", "到店取貨"));
		terms.put("THE DELIVERY FEE YOU ENTERED IS NOT VALID", new Term("The delivery fee you entered is not valid", "您輸入的外送費用無效"));
		terms.put("THE DISCOUNT MUST BE A DECIMAL VALUE BETWEEN 0 AND 1", new Term("The discount must be a decimal value between 0 and 1", "折扣必須是0~1之間的小數"));
		terms.put("THE DISCOUNT YOU ENTERED IS NOT VALID", new Term("The discount you entered is not valid", "您輸入的折扣無效"));
		terms.put("THE EMAIL OR PHONE YOU PROVIDED DOES NOT CONFORM TO THE STANDARD FORMATS (YOU CAN TRY SOMETHING LIKE 0975384927 AND USER@DOMAIN.COM)", new Term("The email or phone you provided does not conform to the standard formats (you can try something like 0975384927 and user@domain.com)", "您輸入的email及電話格式不符,請確認電話號碼沒有其他符號,emai輸入完整"));
		terms.put("THE EMAIL YOU PROVIDED DOES NOT CONFORM TO THE STANDARD EMAIL FORMAT", new Term("The email you provided does not conform to the standard email format", "您輸入的email格式不符"));
		terms.put("THE EMAIL YOU PROVIDED DOES NOT CONFORM TO THE STANDARD FORMATS (YOU CAN TRY SOMETHING LIKE USER@DOMAIN.COM)", new Term("The email you provided does not conform to the standard formats (you can try something like user@domain.com)", "您輸入的email格式不符,請確認是否輸入完整"));
		terms.put("THE EMAIL YOU PROVIDED IS ALREADY BEING USED IN THE SYSTEM", new Term("The email you provided is already being used in the system", "您輸入的email已存在"));
		terms.put("THE MAX RESPONSES YOU ENTERED ARE NOT VALID.", new Term("The max responses you entered are not valid.", "您輸入的回應上限無效"));
		terms.put("THE PASSWORD YOU ENTERED DOESN'T MATCH THE CONFIRMATION PASSWORD", new Term("The password you entered doesn't match the confirmation password", "您輸入的密碼不正確"));
		terms.put("THE PHONE YOU PROVIDED DOES NOT CONFORM TO THE STANDARD FORMATS (YOU CAN TRY SOMETHING LIKE (123)456-7890)", new Term("The phone you provided does not conform to the standard formats (you can try something like (123)456-7890)", "您輸入的電話格式不符,請確認電話號碼沒有其符號"));
		terms.put("THE PRICE YOU ENTERED IS NOT VALID", new Term("The price you entered is not valid", "您輸入的價格無效"));
		terms.put("THE RATING HIGH VALUE YOU ENTERED IS NOT VALID", new Term("The rating high value you entered is not valid", "您輸入的最高分無效"));
		terms.put("THE RATING LOW VALUE YOU ENTERED IS NOT VALID", new Term("The rating low value you entered is not valid", "您輸入的最低分無效"));
		terms.put("THE SERVICE TIME YOU ENTERED IS NOT VALID", new Term("The service time you entered is not valid", "您輸入的供應時段無效"));
		terms.put("THE SERVING TIME YOU ENTERED IS NOT VALID", new Term("The serving time you entered is not valid", "您輸入的準備時間無效"));
		terms.put("THE VALIDATION CODE YOU ENTERED IS NOT VALID", new Term("The validation code you entered is not valid", "您輸入的驗證碼無效"));
		terms.put("TIME TO SERVE", new Term("Time to Serve", "取貨/送貨時間"));
		terms.put("TO ACCESS YOUR ACCOUNT", new Term("to access your account", "請輸入您的帳號"));
		terms.put("TOTAL", new Term("Total", "總計"));
		terms.put("TRUE", new Term("true", "真"));
		terms.put("UNCLAIMED", new Term("Unclaimed", "訂貨未取"));
		terms.put("UNIT PRICE", new Term("Unit Price", "單價"));
		terms.put("UNLIMITED", new Term("Unlimited", "無上限"));
		terms.put("UNPROCESSED", new Term("Unprocessed", "未處理"));
		terms.put("UPDATE", new Term("Update", "儲存"));
		terms.put("USE DEFAULT RATING VALUES? (1 TO 5)", new Term("Use default Rating values? (1 to 5)", "使用評分?(1到5)"));
		terms.put("USER INFORMATION", new Term("User Information", "會員資訊"));
		terms.put("USER REGISTERED SUCCESSFULLY", new Term("User registered successfully", "會員註冊成功"));
		terms.put("USER TYPE", new Term("User Type", "會員類型"));
		terms.put("USERNAME", new Term("Username", "帳號"));
		terms.put("VALIDATION CODE", new Term("Validation Code", "認證碼"));
		terms.put("VALUE:1 (LOWEST)", new Term("Value:1 (Lowest)", "評分:1(最低)"));
		terms.put("VALUE:2", new Term("Value:2", "評分:2"));
		terms.put("VALUE:3", new Term("Value:3", "評分:3"));
		terms.put("VALUE:4", new Term("Value:4", "評分:4"));
		terms.put("VALUE:5 (HIGHEST)", new Term("Value:5 (Highest)", "評分:5(最高)"));
		terms.put("VIEW", new Term("View", "檢閱"));
		terms.put("VIEW A BRANCH", new Term("View a branch", "檢閱分店"));
		terms.put("VIEW ADDITIONAL PROPERTY TYPE", new Term("View Additional Property Type", "檢閱附加選項類型"));
		terms.put("VIEW ADMINISTRATOR", new Term("View Administrator", "檢閱管理者"));
		terms.put("VIEW CUSTOMER", new Term("View Customer", "檢閱顧客資料"));
		terms.put("VIEW ORDER", new Term("View Order", "檢閱訂單"));
		terms.put("VIEW POLLS", new Term("View Polls", "檢閱民調"));
		terms.put("VIEW PRODUCT ITEM", new Term("View Product Item", "檢閱商品"));
		terms.put("VIEW PRODUCT TYPE", new Term("View Product Type", "檢閱商品類型"));
		terms.put("VIEW REGION", new Term("View Region", "檢閱地區資訊"));
		terms.put("VIEW RETAIL STORE", new Term("View Retail Store", "檢閱店家"));
		terms.put("VIEW RETAIL STORE NEWS", new Term("View Retail Store News", "檢閱店家新聞"));
		terms.put("VIEW RETAIL STORE OPINION POLL", new Term("View Retail Store Opinion Poll", "檢閱店家民調"));
		terms.put("VIEW RETAIL STORE TYPE", new Term("View Retail Store Type", "檢閱店家類型"));
		terms.put("VIEW SURVEY", new Term("View Survey", "檢閱題組民調"));
		terms.put("VIEW SURVEY OPINION POLL", new Term("View Survey Opinion Poll", "檢閱題組民調"));
		terms.put("VIEW SYSTEM", new Term("View System", "檢閱系統"));
		terms.put("WEBSITE", new Term("Website", "網址"));
		terms.put("WELCOME", new Term("Welcome", "歡迎"));
		terms.put("YES", new Term("Yes", "有"));
		terms.put("YOU ARE MISSING SOME ESSENTIAL INFORMATION NEEDED BY THE SYSTEM", new Term("You are missing some essential information needed by the system", "資料沒有完全符合"));
		terms.put("YOU ARE MISSING SOME ESSENTIAL INFORMATION NEEDED BY THE SYSTEM TO REGISTER", new Term("You are missing some essential information needed by the system to register", "您尚有必填資料未輸入"));

	}

}
