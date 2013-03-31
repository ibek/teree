package org.teree.client;

import org.teree.shared.data.tree.TreeType;

public class Settings {

	public static final String HOST = "http://www.teree.org/"; // for Openshift
	//public static final String HOST = "http://127.0.0.1:8080/teree/"; // for localhost

	public static final String HOME_LINK = "home";
	public static final String EXPLORE_LINK = "explore";
	public static final String SEARCH_LINK = EXPLORE_LINK + "/search=";
	public static final String VIEW_LINK = "view/oid=";
	public static final String HELP_LINK = "help";
	public static final String EDIT_LINK = "edit";
	public static final String LOGIN_LINK = "login";
	public static final String FAILED_LOGIN_LINK = "login?failed";
	public static final String JOIN_LINK = "join";
	public static final String TAUTH_LINK = "tauth";
	public static final String USERHOME_LINK = "user/id=";
	public static final String SETTINGS_LINK = "settings";
	public static final String CHANGE_LOGS_LINK = "changeLogs";
	
	public static final String OID_PARAM = "/oid=";
	
	public static final String COOKIE_SESSION_ID = "JSESSIONID";

    public static final int NODE_MIN_WIDTH = 50;
    public static final int NODE_MAX_WIDTH = 400;
    public static final int NODE_MIN_HEIGHT = 18;
    public static final int NODE_DEFAULT_WIDTH = 200; // for empty node
    public static final int NODE_DEFAULT_HEIGHT = 40; // for empty node

    public static final int SAMPLE_MAX_WIDTH = 300;
    public static final int SAMPLE_MAX_HEIGHT = 200;

    public static final int SCENE_HEIGHT_LESS = 100;
    
	public static final double ICON_WIDTH = 15.0;
	
	public static final int SCHEME_COUNT_IN_EXPLORER = 10;
    
    public static final TreeType DEFAULT_SCHEME_TYPE = TreeType.MindMap;
    
}
