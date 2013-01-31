

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import twitterVoice.functions.TwitterVoiceFunction;
import twitterVoice.menu.HelpMenu;
import twitterVoice.menu.MainMenu;
import twitterVoice.menu.ReadArticleMenu;
import twitterVoice.menu.TwitterActionMenu;
import twitterVoice.user.TwitterVoiceUser;
import twitterVoice.util.RuntimeInterface;
import twitterVoice.util.StateConstants;

/** This is the main interface for the TwitterVoice Dialog interaction component. 
 * Based on the state of the user, the next action is determined.
 * @author Eli Pincus, Rashmi Raman */
public class TwitterVoiceDialog {
	static boolean debug = false;

	public static void main(String[] args) {
		
		String path = TwitterVoiceDialog.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String parentPath = (new File(path)).getParentFile().getPath();
		String decodedPath = ""; 
		String hostName = "";
		
		
		try {
			InetAddress addr = InetAddress.getLocalHost();			  
			hostName = addr.getHostName();
			decodedPath = URLDecoder.decode(parentPath, "UTF-8");
			RuntimeInterface.setFile(hostName, decodedPath);
			Date date = new Date(); DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
			String df = dateFormat.format(date);
			RuntimeInterface.mkdirScript(decodedPath + "/" + df);
			decodedPath = decodedPath + "/" + df;
			RuntimeInterface.mkdirScript(decodedPath + "/audio");
			RuntimeInterface.mkdirScript(decodedPath+"/log");
			System.out.println(decodedPath);
		} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();
		}catch (UnknownHostException e) {
		  }	
			  
		
		if (debug)
			System.out.println("Welcome to TwitterVoice");
		
		//initialize menus
		MainMenu mainMenu = new MainMenu();
		TwitterActionMenu twitterActionMenu = new TwitterActionMenu();
		ReadArticleMenu readArticleMenu = new ReadArticleMenu();
		HelpMenu helpMenu = new HelpMenu();
		TwitterVoiceFunction.getTwitterVoiceFunction();
		System.out.println("Runtime interface");
		
		// create a TwitterVoiceUser - initialize to Greeting state
		TwitterVoiceUser twitterVoiceUser = new TwitterVoiceUser("CS4706","cs4706TVoice",
				StateConstants.GREETING, StateConstants.NO_STATE, decodedPath);
		
				
		/* determine action based on user state so long as it is not exit */
		do {

			int state = twitterVoiceUser.getCurrentState();

			switch (state) {
			case StateConstants.GREETING:
				twitterVoiceUser = mainMenu.greetUser(twitterVoiceUser);
				break;
			case StateConstants.MAIN_MENU:
				twitterVoiceUser = mainMenu.giveMenu(twitterVoiceUser);
				break;
			case StateConstants.TWITTER_VOICE_LIST:
				twitterVoiceUser = mainMenu
						.giveTwitterVoiceList(twitterVoiceUser);
				break;
			case StateConstants.TRENDING_TOPICS:
				twitterVoiceUser = mainMenu
						.giveTrendingTopics(twitterVoiceUser);
				break;
			case StateConstants.NEWS_FEED:
				twitterVoiceUser = mainMenu.giveNewsFeed(twitterVoiceUser);
				break;
			case StateConstants.READ_TWEETS:
				twitterVoiceUser = mainMenu.giveTweets(twitterVoiceUser);
				break;
			case StateConstants.TWITTER_ACTION_MENU:
				twitterVoiceUser = twitterActionMenu.giveMenu(twitterVoiceUser);
				break;
			case StateConstants.READ_MORE_TWEETS:
				twitterVoiceUser = twitterActionMenu.readMoreTweets(twitterVoiceUser);
				break;
			case StateConstants.RETWEET:
				twitterVoiceUser = twitterActionMenu.retweet(twitterVoiceUser);
				break;
			case StateConstants.FAVORITE:
				twitterVoiceUser = twitterActionMenu.favorite(twitterVoiceUser);
				break;
			case StateConstants.RESPOND:
				twitterVoiceUser = twitterActionMenu.respond(twitterVoiceUser);
				break;
			case StateConstants.READ_ARTICLE_MENU:
				twitterVoiceUser = readArticleMenu.giveMenu(twitterVoiceUser);
				break;
			case StateConstants.READ_ARTICLE:
				twitterVoiceUser = readArticleMenu
						.giveArticle(twitterVoiceUser);
				break;
			case StateConstants.HELP:
				twitterVoiceUser = helpMenu.giveMenu(twitterVoiceUser);
				break;
			}
		} while (twitterVoiceUser.getCurrentState() != StateConstants.EXIT);
		mainMenu.giveExitMessage(twitterVoiceUser); //Exit application
		twitterVoiceUser.getLog().close();
		System.out.println("Your session with TwitterVoice has ended successfully. To check the audio files and log files please check : ");
		System.out.println("Audio files : " + decodedPath + "/audio");
		System.out.println("Log file : " + decodedPath + "/log");
		System.exit(0);
	}
}
