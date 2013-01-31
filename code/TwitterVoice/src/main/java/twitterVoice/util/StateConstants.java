package twitterVoice.util;
/** This class stores constants which the user can be in during an interaction. 

 * @author Eli Pincus, Rashmi Raman*/

public abstract class StateConstants {
	
	public static final int NO_STATE = 0;
	public static final int GREETING = 0;
	public static final int MAIN_MENU = 1;
	public static final int TWITTER_VOICE_LIST = 2;
	public static final int READ_TWEETS = 3;
	public static final int TRENDING_TOPICS = 4;
	public static final int NEWS_FEED = 5;
	public static final int TWITTER_ACTION_MENU = 6;
	public static final int READ_ARTICLE = 7;
	public static final int READ_ARTICLE_MENU = 8;
	public static final int RESPOND = 9;
	public static final int FAVORITE = 10;
	public static final int RETWEET = 11;
	public static final int READ_MORE_TWEETS = 12;
	
	public static final int HELP = 99;
	public static final int EXIT = 999;
	public static final int ERROR = -1;
	
	public static String getState(int state){
		String stateString = "";
		
		switch (state){		
		case GREETING:
			stateString = "GREETING";
			break;
		case MAIN_MENU:
			stateString = "MAIN_MENU";
			break;
		case TWITTER_VOICE_LIST:
			stateString = "TWITTER_VOICE_LIST";
			break;
		case READ_TWEETS:
			stateString = "READ_TWEETS";
			break;
		case TRENDING_TOPICS:
			stateString = "TRENDING_TOPICS";
			break;
		case NEWS_FEED:
			stateString = "NEWS_FEED";
			break;
		case TWITTER_ACTION_MENU:
			stateString = "TWITTER_ACTION_MENU";
			break;
		case READ_ARTICLE:
			stateString = "READ_ARTICLE";
			break;
		case READ_ARTICLE_MENU:
			stateString = "READ_ARTICLE_MENU";
			break;
		case RESPOND:
			stateString = "RESPOND";
			break;
		case FAVORITE:
			stateString = "FAVORITE";
			break;
		case RETWEET:
			stateString = "RETWEET";
			break;
		case READ_MORE_TWEETS:
			stateString = "READ_MORE_TWEETS";
			break;
		case HELP:
			stateString = "HELP";
			break;
		case EXIT:
			stateString = "EXIT";
			break;
		case ERROR:
			stateString = "ERROR";
			break;

		}
		
		return stateString;
	}
	
	

}
