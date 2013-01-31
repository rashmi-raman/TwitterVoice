package twitterVoice.functions.readTweets;

import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitterVoice.functions.TwitterVoiceFunction;
import twitterVoice.user.TwitterVoiceUser;
/** This class invokes the Twitter API to get tweets by a particular TwitterHandle.
 * Invokes a Twitter search query from:twitterHandle 
 * @author Eli Pincus, Rashmi Raman*/
public class GetTweets {
	public static boolean debug = false;
	 public static TwitterVoiceUser getTweets(TwitterVoiceUser twitterVoiceUser) {
	        
	        Twitter twitter = TwitterVoiceFunction.getTwitterVoiceFunction().getTwitter();
	        List<Tweet> tweets = null;
	        try {
	        	QueryResult result = null;
	        	result = twitter.search(new Query("from:" + twitterVoiceUser.getReadTweetsBy().getScreenName()));
	        	
	            tweets = result.getTweets();
	            for (Tweet tweet : tweets) {
	            	if(debug)
	                System.out.println("@" + tweet.getFromUser() + " - " + tweet.getText());
	            }
	            twitterVoiceUser.setCurrentOuptut(tweets); //set list of tweets as output
	        } catch (TwitterException te) {
	            twitterVoiceUser.setCurrentError("Could not get tweets");
	            
	        }
	        return twitterVoiceUser;
	    }
	 }
