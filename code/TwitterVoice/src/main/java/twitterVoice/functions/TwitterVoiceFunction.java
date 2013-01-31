package twitterVoice.functions;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterVoiceFunction {
	
	public static Twitter twitter;
	public static TwitterVoiceFunction instance;

	
	
	public static TwitterVoiceFunction getTwitterVoiceFunction() {	
		if(instance == null)
			init();
		return instance;
			
	}

	public Twitter getTwitter() {
		return twitter;
	}

	public void setTwitter(Twitter twitter) {
		TwitterVoiceFunction.twitter = twitter;
	}

	private static void init(){
			
			instance = new TwitterVoiceFunction();
			ConfigurationBuilder cb = new ConfigurationBuilder();
	    	cb.setDebugEnabled(true)
	    	  .setOAuthConsumerKey("eGh7bG3kpzJCi1hEXmZw")
	    	  .setOAuthConsumerSecret("Egc5U2x20gPspPbyKoRaMg0AQshqRFyiwvpEgFKkY")
	    	  .setOAuthAccessToken("574165433-zrLyh4SfIGzoirT7vtD7PBjl1fPmgXY64wmTqqQ3")
	    	  .setOAuthAccessTokenSecret("xSGSdDM1KGF5oPYVlSlfFBlQLcx00L15nidXHDjDYX0");
	    	TwitterFactory tf = new TwitterFactory(cb.build());
	    	twitter = tf.getInstance();
	    	instance.setTwitter(twitter);
			
			
		}
	}
	
	


