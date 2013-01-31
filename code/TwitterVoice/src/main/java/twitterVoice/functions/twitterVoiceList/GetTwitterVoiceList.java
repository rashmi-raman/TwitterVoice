package twitterVoice.functions.twitterVoiceList;

import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.UserList;
import twitterVoice.user.TwitterVoiceUser;
/** This class invokes the Twitter API to get the members of a custom created list of
 * Twitter users called the TwitterVoiceList. This is the curated list for the app
 * Invokes a Twitter search query from:twitterHandle 
 * @author Eli Pincus, Rashmi Raman*/

public final class GetTwitterVoiceList {
	private static int listId = 0;
	private static String twitterVoiceList = "";
	private static boolean debug = false;

	public static TwitterVoiceUser getTwitterVoiceListOutput(TwitterVoiceUser twitterVoiceUser) {

		try {
			Twitter twitter = new TwitterFactory().getInstance();
			long cursor = -1;

			PagableResponseList<UserList> lists;
			boolean listFound = false;
			do {
				lists = twitter.getUserLists(twitterVoiceUser.getTwitterHandle(), cursor);
				for (UserList list : lists) {
					if (debug) {
						System.out.println("id:" + list.getId() + ", name:"
								+ list.getName() + ", description:"
								+ list.getDescription() + ", slug:"
								+ list.getSlug() + "");
					}
					if (list.getName().equalsIgnoreCase("Twitter Voice")) {
						listId = list.getId();
						listFound = true;
					}
				}
			} while (!listFound && (cursor = lists.getNextCursor()) != 0);

			PagableResponseList<User> usres;

			do {
				usres = twitter.getUserListMembers(listId, cursor);
				for (User list : usres) {
					twitterVoiceList += " " + list.getName();					
					if(debug)
					System.out.println("@" + list.getName());
				}				
			} while ((cursor = usres.getNextCursor()) != 0);
			twitterVoiceUser.setCurrentOuptut(twitterVoiceList); // return output as a string
		} catch (TwitterException te) {
			twitterVoiceUser.setCurrentError("Could not get TwitterVoiceList");
		}
		
		return twitterVoiceUser;
	}
	
	public static PagableResponseList<User> getTwitterVoiceList(TwitterVoiceUser twitterVoiceUser) {

		try {
			Twitter twitter = new TwitterFactory().getInstance();
			long cursor = -1;

			PagableResponseList<UserList> lists;
			boolean listFound = false;
			do {
				lists = twitter.getUserLists(twitterVoiceUser.getTwitterHandle(), cursor);
				for (UserList list : lists) {
					if (debug) {
						System.out.println("id:" + list.getId() + ", name:"
								+ list.getName() + ", description:"
								+ list.getDescription() + ", slug:"
								+ list.getSlug() + "");
					}
					if (list.getName().equalsIgnoreCase("Twitter Voice")) {
						listId = list.getId();
						listFound = true;
					}
				}
			} while (!listFound && (cursor = lists.getNextCursor()) != 0);

			PagableResponseList<User> usres;

			do {
				usres = twitter.getUserListMembers(listId, cursor);
				for (User list : usres) {										
					if(debug)
					System.out.println("@" + list.getName());
				}				
			} while ((cursor = usres.getNextCursor()) != 0);
			return usres; // return list for further processing
		} catch (TwitterException te) {
			twitterVoiceUser.setCurrentError("Could not get TwitterVoiceList");
		}
		
		return null;
	}
}
