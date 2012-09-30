package org.teree.client;

import org.teree.client.scheme.SchemeType;

public class Settings {

	public static final String HOME_LINK = "home";
	public static final String EXPLORE_LINK = "explore";
	public static final String VIEW_LINK = "view/oid=";
	public static final String CREATE_LINK = "create";
	public static final String EDIT_LINK = "edit/oid=";
	public static final String LOGIN_LINK = "login";
	public static final String FAILED_LOGIN_LINK = "login?failed";
	public static final String JOIN_LINK = "join";
	public static final String TAUTH_LINK = "tauth";
	public static final String PRIVATE_LINK = "private";
	
	public static final String COOKIE_SESSION_ID = "JSESSIONID";

    public static final int MIN_WIDTH = 50;
    public static final int MAX_WIDTH = 400;
    public static final int MIN_HEIGHT = 18;

    public static final int SAMPLE_MAX_WIDTH = 300;
    public static final int SAMPLE_MAX_HEIGHT = 200;

    public static final int SCENE_HEIGHT_LESS = 80;
    
    public static final SchemeType DEFAULT_SCHEME_TYPE = SchemeType.MindMap;
    
}
