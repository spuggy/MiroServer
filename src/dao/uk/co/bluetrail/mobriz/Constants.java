package uk.co.bluetrail.mobriz;


/**
 * Constant values used throughout the application.
 *
 * <p>
 * <a href="Constants.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class Constants {
    //~ Static fields/initializers =============================================


    //named survey ids
    public static final Long Survey_id_Mirov11 = 5L;
    public static final Long Survey_id_Mirov10 = 4L;


    /** The name of the ResourceBundle used in this application */
    public static final String BUNDLE_KEY = "ApplicationResources";

    /** The encryption algorithm key to be used for passwords */
    public static final String ENC_ALGORITHM = "algorithm";

    /** A flag to indicate if passwords should be encrypted */
    public static final String ENCRYPT_PASSWORD = "encryptPassword";

    /** File separator from System properties */
    public static final String FILE_SEP = System.getProperty("file.separator");

    /** User home from System properties */
    public static final String USER_HOME = System.getProperty("user.home") + FILE_SEP;

    /** The name of the configuration hashmap stored in application scope. */
    public static final String CONFIG = "appConfig";

    /** 
     * Session scope attribute that holds the locale set by the user. By setting this key
     * to the same one that Struts uses, we get synchronization in Struts w/o having
     * to do extra work or have two session-level variables.
     */ 
    public static final String PREFERRED_LOCALE_KEY = "org.apache.struts.action.LOCALE";
    
    /**
     * The request scope attribute under which an editable user form is stored
     */
    public static final String USER_KEY = "userForm";

    /**
     * The request scope attribute that holds the user list
     */
    public static final String USER_LIST = "userList";

    /**
     * The request scope attribute for indicating a newly-registered user
     */
    public static final String REGISTERED = "registered";

    /**
     * The name of the Administrator role, as specified in web.xml
     */
    public static final String ADMIN_ROLE = "admin";

    /**
     * The name of the User role, as specified in web.xml
     */
    public static final String USER_ROLE = "user";
    
    public static final String TEAMREPORT_ROLE = "teamreport";

    public static final String DEPARTMENT_ADMIN_ROLE = "departmentadmin" ;
    

    /**
     * The name of the user's role list, a request-scoped attribute
     * when adding/editing a user.
     */
    public static final String USER_ROLES = "userRoles";

    /**
     * The name of the available roles list, a request-scoped attribute
     * when adding/editing a user.
     */
    public static final String AVAILABLE_ROLES = "availableRoles";

    /**
     * The name of the CSS Theme setting.
     */
    public static final String CSS_THEME = "csstheme";




//Account-START
    /**
     * The request scope attribute that holds the account form.
     */
    public static final String ACCOUNT_KEY = "accountForm";

    /**
     * The request scope attribute that holds the account list
     */
    public static final String ACCOUNT_LIST = "accountList";
//Account-END

//Setting-START
    /**
     * The request scope attribute that holds the setting form.
     */
    public static final String SETTING_KEY = "settingForm";

    /**
     * The request scope attribute that holds the setting list
     */
    public static final String SETTING_LIST = "settingList";
//Setting-END

	public static final String SURVEY_LIST = "surveyList";

	public static final String WS_GETSURVEYUPDATES = "GSU";

	public static final String WS_GETSURVEY = "GS";

	public static final String WS_POST_RESPONSE = "PR";

	public static final String WS_POST_IMAGE = "PI";
	
	public static final String WS_LOOKUP = "LK";
	
	public static final String CLIENT_ROLE = "client" ;

	public static final String SYSADMIN_ROLE = "sysadmin";

	public static final String AVAILABLE_USER_ROLES = "availableUserRoles" ;

	public static final String SETTING_DOWNLOADURL = "DOWNLOAD_URL";

    public static final String SETTING_FREE_ASSESSMENT_PROJECT_ID = "FREE_ASSESSMENT_PROJECT_ID";

    public static final String PHOTODIR = "photos";

	public static final String NOTAPPLICABLE = "N/A";

	public static final String BLANKPIN = "tba";

	
	
//LookupDef-START
    /**
     * The request scope attribute that holds the lookupDef form.
     */
    public static final String LOOKUPDEF_KEY = "lookupDefForm";

    /**
     * The request scope attribute that holds the lookupDef list
     */
    public static final String LOOKUPDEF_LIST = "lookupDefList";

    /**
     * The request scope attribute that holds the lookupDefItem form.
     */
    public static final String LOOKUPDEFITEM_KEY = "lookupDefItemForm";

    /**
     * The request scope attribute that holds the lookupDefItem list
     */
    public static final String LOOKUPDEFITEM_LIST = "lookupDefItemList";

	public static final String MAXSEARCH_LIMIT = "20";

	public static final String MAX_SEARCH_DOWNLOAD = "10";

	public static final String MIRO_EXCEPTION = "2" ;
	public static final String MIRO_OK = "1" ;
	public static final String MIRO_DUPE = "3" ;
	public static final String MIRO_NEW = "0" ;
	public static final String MIRO_INVALID = "4";

//	WebPage-START
    /**
     * The request scope attribute that holds the webPage form.
     */
    public static final String WEBPAGE_KEY = "webPageForm";

    /**
     * The request scope attribute that holds the webPage list
     */
    public static final String WEBPAGE_LIST = "webPageList";
//WebPage-END
//  MobrizAlert-START
    /**
     * The request scope attribute that holds the mobrizAlert form.
     */
    public static final String MOBRIZALERT_KEY = "mobrizAlertForm";

    /**
     * The request scope attribute that holds the mobrizAlert list
     */
    public static final String MOBRIZALERT_LIST = "mobrizAlertList";
//MobrizAlert-END
    
    /**
     * The request scope attribute that holds the miroProject form.
     */
    public static final String MIROPROJECT_KEY = "miroProjectForm";

    /**
     * The request scope attribute that holds the miroProject list
     */
    public static final String MIROPROJECT_LIST = "miroProjectList";


//  MiroTransaction-START
    /**
     * The request scope attribute that holds the miroTransaction form.
     */
    public static final String MIROTRANSACTION_KEY = "miroTransactionForm";

    /**
     * The request scope attribute that holds the miroTransaction list
     */
    public static final String MIROTRANSACTION_LIST = "miroTransactionList";
//MiroTransaction-END

	public static final String MIRO_CREDIT_PRICE = "MIRO_CREDIT_PRICE";

	public static final String SHOPPING_CART = "CART";

	public static final String MIROTEAM_LIST = "miroTeamList";

	public static final String PDF = ".pdf";
	
	public static final String JPEG = ".jpg";
	
	public static final String XHTML = ".xhtml";
	
	public static final String html = ".html";
	
	
}





