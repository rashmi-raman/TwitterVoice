package twitterVoice.functions.twitterActions;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitterVoice.functions.TwitterVoiceFunction;
import twitterVoice.user.TwitterVoiceUser;
/** This class has methods that act on a tweet. 
 * Current functions offered : retweet, favorite, respond using a template response
 * @author Eli Pincus, Rashmi Raman
 *
 */
public class TwitterActions {
	
	public static boolean debug = true;
		
	public static String[] responses = {"Thoughtful Read","Wonderful Great","Need to think about this","I don't support this"};
	
	public static void init(){}
	public static TwitterVoiceUser retweet(TwitterVoiceUser twitterVoiceUser) {
	    try {
	    	
	    	Twitter twitter = TwitterVoiceFunction.getTwitterVoiceFunction().getTwitter();
	        twitter.retweetStatus(twitterVoiceUser.getCurrentTweet().getId());
	        if(debug)
	        System.out.println("Successfully retweeted status " + twitterVoiceUser.getCurrentTweet().getId());
	        twitterVoiceUser.setCurrentOuptut("You have retweeted this tweet by " + twitterVoiceUser.getReadTweetsBy().getName());//set status as output
	    } catch (TwitterException te) {
	        twitterVoiceUser.setCurrentError("Sorry, I couldn't retweet that for you.");
	    }
		return twitterVoiceUser;
	}

	public static TwitterVoiceUser favorite(TwitterVoiceUser twitterVoiceUser) {
		try {
			Twitter twitter = TwitterVoiceFunction.getTwitterVoiceFunction().getTwitter();
			twitter.createFavorite(twitterVoiceUser.getCurrentTweet().getId());
	        if(debug)
	        System.out.println("Successfully favorited status " + twitterVoiceUser.getCurrentTweet().getId());
	        twitterVoiceUser.setCurrentOuptut("You have favorited this tweet by " + twitterVoiceUser.getReadTweetsBy().getName());//set status as output
	    } catch (TwitterException te) {
	        twitterVoiceUser.setCurrentError("Sorry, I couldn't favorite that for you.");
	    }
		return twitterVoiceUser;
	}

	public static TwitterVoiceUser respond(TwitterVoiceUser twitterVoiceUser,
			int i) {
		
		try {
			Twitter twitter = TwitterVoiceFunction.getTwitterVoiceFunction().getTwitter();
	        if(debug)
			System.out.println("In respond : " + responses[i]);
	        twitter.updateStatus(new StatusUpdate("@" + twitterVoiceUser.getReadTweetsBy().getScreenName() + " " + responses[i]).inReplyToStatusId(twitterVoiceUser.getCurrentTweet().getId()));
	        if(debug)
	        System.out.println("Successfully responded to status " + twitterVoiceUser.getCurrentTweet().getId());
	        twitterVoiceUser.setCurrentOuptut("You have responded with " + responses[i] + " to this tweet by " + twitterVoiceUser.getReadTweetsBy().getName());//set status as output
	    } catch (TwitterException te) {
	        twitterVoiceUser.setCurrentError("Sorry, your response was not published.");
	    }
		return twitterVoiceUser;
	}

}