package twitterVoice.functions.trending;

import java.util.List;
import java.util.regex.Pattern;

import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitterVoice.functions.TwitterVoiceFunction;
import twitterVoice.user.TwitterVoiceUser;
import twitterVoice.util.StringNormalizer;
/** This class invokes the Twitter API to get trending topics 
 * For the sake of reducing the potential boredom factor, pulling only 5 topics */
public class GetTrendingTopics{
	public static boolean debug = false;
	public static TwitterVoiceUser getTrendingTopics(TwitterVoiceUser twitterVoiceUser) {
		String trendingTopics = "";
        try {
        	Twitter twitter = TwitterVoiceFunction.getTwitterVoiceFunction().getTwitter();
            List<Trends> trendsList;
            trendsList = twitter.getDailyTrends();
            if(debug)
            System.out.println("Showing daily trends");
            int i =0;
            for (Trends trends : trendsList) {
                
                for (Trend trend : trends.getTrends()) {
                	if(i<5 && !trend.getName().contains("#") && Pattern.matches("[a-zA-Z ]+", trend.getName())){
                    trendingTopics += " " + trend.getName();
                    i++;
                	}
                	
                }
            }
            trendingTopics = StringNormalizer.normalize(trendingTopics);
            if(debug)
            System.out.println(trendingTopics);
            twitterVoiceUser.setCurrentOuptut(trendingTopics); //set list of topics as output
            
            
        } catch (Exception pe) {
            twitterVoiceUser.setCurrentError("Sorry, I could not get trending topics for you");
        }
        
        return twitterVoiceUser;
    }
}
