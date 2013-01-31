package twitterVoice.menu;

import java.util.ArrayList;
import java.util.List;

import twitter4j.PagableResponseList;
import twitter4j.Tweet;
import twitter4j.User;
import twitterVoice.functions.readTweets.GetTweets;
import twitterVoice.functions.trending.GetTrendingTopics;
import twitterVoice.functions.twitterVoiceList.GetTwitterVoiceList;
import twitterVoice.user.TwitterVoiceUser;
import twitterVoice.util.RelativeDate;
import twitterVoice.util.RuntimeInterface;
import twitterVoice.util.StateConstants;
import twitterVoice.util.StringNormalizer;
import twitterVoice.util.TwitterVoiceInputOutput;

/**
 * This class invokes various functionalities associated with the TwitterVoice
 * Main menu take user input, pass to ASR, determine the action to be taken,
 * invoke relevant model class, send output to TTS, update state of the user.
 * 
 * @author Eli Pincus, Rashmi Raman
 */

public class MainMenu extends TwitterVoiceInputOutput {

	public MainMenu() {
		super();
	}

	/**
	 * Method to read out greeting message Prev state : NO_STATE Next State :
	 * MAIN_MENU
	 * 
	 * @param twitterVoiceUser
	 * @return
	 */
	public TwitterVoiceUser greetUser(TwitterVoiceUser twitterVoiceUser) {
		if (debug)
			System.out.println("Greeting message");
		twitterVoiceUser = this.givePrompt(twitterVoiceUser, "greeting");
		twitterVoiceUser.setPrevState(twitterVoiceUser.getCurrentState());
		twitterVoiceUser.setCurrentState(StateConstants.MAIN_MENU);
		return twitterVoiceUser;
	}

	/**
	 * Method to read out main menu Once menu is read out, user input is taken,
	 * processed, action is determined, output provided and state updated
	 * 
	 * @param twitterVoiceUser
	 * @return
	 */
	public TwitterVoiceUser giveMenu(TwitterVoiceUser twitterVoiceUser) {
		if (debug)
			System.out.println("Main menu");
		twitterVoiceUser = this.givePrompt(twitterVoiceUser, "main"); // read
																		// out
																		// main
																		// menu
		twitterVoiceUser = this.getInput(twitterVoiceUser); // get user choice
		String prevResponse = this.interpretInput(
				RuntimeInterface.pythonMainMenuScript, twitterVoiceUser); // send
																			// to
																			// ASR
		if (prevResponse.contains("None") || prevResponse.contains("Command: UNSPECIFIED") || (prevResponse.contains("Twitter Handle: UNSPECIFIED") && prevResponse.contains("Command: TWEETS BY"))) {
			twitterVoiceUser = this.givePrompt(twitterVoiceUser, "inputError");
			twitterVoiceUser.setPrevState(twitterVoiceUser.getCurrentState());
			twitterVoiceUser = clearUser(twitterVoiceUser);
			twitterVoiceUser.setCurrentState(StateConstants.MAIN_MENU);
			twitterVoiceUser = getLatestInputFile(twitterVoiceUser);

		} else {
			twitterVoiceUser = getLatestInputFile(twitterVoiceUser);

			twitterVoiceUser = this.giveOutput(twitterVoiceUser,
					getConfirmationMessage(prevResponse)); // ask confirmation
			twitterVoiceUser = this.getInput(twitterVoiceUser); // get
																// confirmation
			String confirmation = this.interpretInput(
					RuntimeInterface.pythonConfScript, twitterVoiceUser);// send
																			// confirmation
																			// to
																			// ASR

			twitterVoiceUser = getLatestInputFile(twitterVoiceUser);
			if (confirmation.contains("None") || confirmation.contains("UNSPECIFIED")) {
				twitterVoiceUser = this.givePrompt(twitterVoiceUser,
						"inputError");
				twitterVoiceUser.setPrevState(twitterVoiceUser
						.getCurrentState());
				twitterVoiceUser = clearUser(twitterVoiceUser);
				twitterVoiceUser.setCurrentState(StateConstants.MAIN_MENU);
				twitterVoiceUser = getLatestInputFile(twitterVoiceUser);

			} else {
				// user confirmed that system understanding was correct, hence
				// update state of the user to pass back to dialog manager
				if (confirmation.contains("YES")
						|| confirmation.contains("YEAH")
						|| confirmation.contains("OK")
						|| confirmation.contains("RIGHT")
						|| confirmation.contains("YUP")
						|| confirmation.contains("YEP")
						|| confirmation.contains("CORRECT")						
						) {
					twitterVoiceUser = this.givePrompt(twitterVoiceUser,
							"ok");
					
					
					twitterVoiceUser.setPrevState(twitterVoiceUser
							.getCurrentState());
					if(prevResponse.contains("HELP")){
						twitterVoiceUser.setPrevState(twitterVoiceUser.getCurrentState());
						twitterVoiceUser.setCurrentState(StateConstants.HELP);
					}
					else if (prevResponse.contains("Command: LIST")) {
						twitterVoiceUser
								.setCurrentState(StateConstants.TWITTER_VOICE_LIST);
					} else if (prevResponse
							.contains("Command: TRENDING TOPICS")) {
						twitterVoiceUser
								.setCurrentState(StateConstants.TRENDING_TOPICS);
					} else if (prevResponse.contains("Command: EXIT")
							|| prevResponse.contains("Command: QUIT")) {
						twitterVoiceUser.setCurrentState(StateConstants.EXIT);
					} else if (prevResponse.contains("Command: NEWS FEED")) {
						twitterVoiceUser
								.setCurrentState(StateConstants.NEWS_FEED);
					} else if (prevResponse.contains("Command: TWEETS BY")) {
						twitterVoiceUser
								.setCurrentState(StateConstants.READ_TWEETS);
						twitterVoiceUser.setReadTweetsBy(getUser(prevResponse,
								twitterVoiceUser));
					}
				} else if (confirmation.contains("EXIT")
						|| confirmation.contains("QUIT")) {
					twitterVoiceUser.setCurrentState(StateConstants.EXIT);
				} else if (confirmation.contains("HELP")) {
					twitterVoiceUser.setPrevState(twitterVoiceUser
							.getCurrentState());
					twitterVoiceUser.setCurrentState(StateConstants.HELP);
				} else {
					twitterVoiceUser.setPrevState(twitterVoiceUser
							.getCurrentState());
					twitterVoiceUser.setCurrentState(StateConstants.MAIN_MENU);
				}
				twitterVoiceUser = clearUser(twitterVoiceUser);
			}
		}
		if (debug)
			System.out.println("In MainMenu, giveMenu twitterVoiceUser : "
					+ twitterVoiceUser);
		return twitterVoiceUser;
	}

	// Utility method to get TwitterUser given a string containing the Twitter
	// Name
	private User getUser(String response, TwitterVoiceUser twitterVoiceUser) {
		twitterVoiceUser = clearUser(twitterVoiceUser);
		boolean alreadyUserName = false;
		if (debug) {
			System.out.println("In MainMenu getUser");
		}
		
		String twitterName = "";
		if (response.indexOf("Command") > -1) {
			twitterName = response.substring(
					response.indexOf("Twitter Handle: "),
					response.indexOf("Command"));
			twitterName = twitterName.substring(twitterName.indexOf(':') + 2);
		} else {
			twitterName = response;
			alreadyUserName = true;
		}
		
		PagableResponseList<User> usres = GetTwitterVoiceList
				.getTwitterVoiceList(twitterVoiceUser);
		User user = null;
		String error = twitterVoiceUser.getCurrentError();
		
		if (error != "") {
			twitterVoiceUser = this.giveOutput(twitterVoiceUser, error);
			twitterVoiceUser.setPrevState(twitterVoiceUser.getCurrentState());
			twitterVoiceUser.setCurrentState(StateConstants.MAIN_MENU);
		} else {
			//twitterName = twitterName.trim();
			
			if (alreadyUserName) {
				
				for (User list : usres) {
					if (list.getScreenName().equalsIgnoreCase(twitterName)){
						
						user = list;
					}
				}
			} else {
				
				for (User list : usres) {
					
					if (twitterName.contains(list.getName().toUpperCase())){
						
						user = list;
					}
				}
			}
		}
		if (debug)
			System.out.println("In MainMenu, getUser User : " + user.getName());
		return user;
	}

	public String getTwitterHandle(String response,
			TwitterVoiceUser twitterVoiceUser) {
		if (debug) {
			System.out.println("In MainMenu getTwitterHandle");
		}
		String twitterName = response.substring(
				response.indexOf("Twitter Handle :"),
				response.indexOf("Command"));
		twitterName = twitterName.substring(twitterName.indexOf(':') + 2);
		PagableResponseList<User> usres = GetTwitterVoiceList
				.getTwitterVoiceList(twitterVoiceUser);
		String twitterHandle = "";
		for (User list : usres) {
			if (response.contains(list.getName())) {
				twitterHandle = list.getScreenName();
			}
		}
		if (debug)
			System.out.println("In MainMenu, getTwitterHandle twitterHandle : "
					+ twitterHandle);
		return twitterHandle;
	}

	// Get TwitterVoiceList
	public TwitterVoiceUser giveTwitterVoiceList(
			TwitterVoiceUser twitterVoiceUser) {

		twitterVoiceUser = clearUser(twitterVoiceUser);

		if (debug)
			System.out.println("Twitter Voice List");
		twitterVoiceUser = GetTwitterVoiceList
				.getTwitterVoiceListOutput(twitterVoiceUser);
		String output = (String) twitterVoiceUser.getCurrentOuptut();
		String error = twitterVoiceUser.getCurrentError();

		if (error != "") { // read out error message and go back to main menu
			twitterVoiceUser = this.giveOutput(twitterVoiceUser, error);
			twitterVoiceUser.setPrevState(twitterVoiceUser.getCurrentState());
			twitterVoiceUser.setCurrentState(StateConstants.MAIN_MENU);

		} else { // no error, give output and go back to main menu
			twitterVoiceUser = this.givePrompt(twitterVoiceUser,
					"twitterVoiceList");
			twitterVoiceUser = this.giveOutput(twitterVoiceUser, output);
			twitterVoiceUser.setPrevState(twitterVoiceUser.getCurrentState());
			twitterVoiceUser.setCurrentState(StateConstants.MAIN_MENU);
		}
		twitterVoiceUser = clearUser(twitterVoiceUser);
		if (debug)
			System.out.println("In MainMenu, giveMenu twitterVoiceUser : "
					+ twitterVoiceUser);
		return twitterVoiceUser;
	}

	// Get trending topics
	public TwitterVoiceUser giveTrendingTopics(TwitterVoiceUser twitterVoiceUser) {
		if (debug)
			System.out.println("Trending topics");

		twitterVoiceUser = clearUser(twitterVoiceUser);

		twitterVoiceUser = GetTrendingTopics
				.getTrendingTopics(twitterVoiceUser);
		String output = (String) twitterVoiceUser.getCurrentOuptut();
		String error = twitterVoiceUser.getCurrentError();
		if (error != "") { // read out error message and go back to main menu
			twitterVoiceUser = this.giveOutput(twitterVoiceUser, output);
			twitterVoiceUser.setPrevState(twitterVoiceUser.getCurrentState());
			twitterVoiceUser.setCurrentState(StateConstants.MAIN_MENU);

		} else { // read out output, go back to main menu
			twitterVoiceUser = this.givePrompt(twitterVoiceUser,
					"trendingTopics");
			twitterVoiceUser = this.giveOutput(twitterVoiceUser, output);
			twitterVoiceUser.setPrevState(twitterVoiceUser.getCurrentState());
			twitterVoiceUser.setCurrentState(StateConstants.MAIN_MENU);
		}
		twitterVoiceUser = clearUser(twitterVoiceUser);
		if (debug)
			System.out.println("In MainMenu, giveMenu twitterVoiceUser : "
					+ twitterVoiceUser);
		return twitterVoiceUser;
	}

	// Get feed i.e. tweets by members of the TwitterVoice list
	public TwitterVoiceUser giveNewsFeed(TwitterVoiceUser twitterVoiceUser) {
		if (debug)
			System.out.println("News feed");
		twitterVoiceUser = clearUser(twitterVoiceUser);
		PagableResponseList<User> usres = GetTwitterVoiceList
				.getTwitterVoiceList(twitterVoiceUser);
		List<Tweet> newsFeed = new ArrayList<Tweet>();
		String error = twitterVoiceUser.getCurrentError();
		if (error != "") {
			twitterVoiceUser = this.giveOutput(twitterVoiceUser, error);
			twitterVoiceUser.setPrevState(twitterVoiceUser.getCurrentState());
			twitterVoiceUser = clearUser(twitterVoiceUser);
			twitterVoiceUser.setCurrentState(StateConstants.MAIN_MENU);
		} else {
			for (User list : usres) {
				twitterVoiceUser.setReadTweetsBy(list);
				List<Tweet> tweets = (List<Tweet>) GetTweets.getTweets(
						twitterVoiceUser).getCurrentOuptut();
				for (Tweet tweet : tweets) {
					newsFeed.add(tweet);
				}
			}
			twitterVoiceUser = getLatestTweet(newsFeed, twitterVoiceUser); // read
																			// latest
																			// tweet
																			// out
																			// of
																			// tweets
																			// collected
			Tweet tweet = twitterVoiceUser.getCurrentTweet();
			String tweeter = getTwitterName(tweet, usres);
			String relDate = RelativeDate.getRelativeDate(tweet.getCreatedAt()); // five
																					// minutes
																					// ago
			String output = stripText(tweet.getText()); // remove stuff that TTS
														// cannot process

			twitterVoiceUser = this.giveOutput(twitterVoiceUser, tweeter + " "
					+ "tweeted this " + relDate);
			twitterVoiceUser = this.giveOutput(twitterVoiceUser, output); // read
																			// out
																			// tweet
			twitterVoiceUser.setNewsFeed(true); // news feed and not give me
												// tweets by XXX
			twitterVoiceUser.setPrevState(twitterVoiceUser.getCurrentState());
			if (twitterVoiceUser.getCurrentTweet().getURLEntities() != null) {
				twitterVoiceUser
						.setCurrentState(StateConstants.READ_ARTICLE_MENU); // there's
																			// a
																			// link,
																			// so
																			// need
																			// to
																			// ask
																			// user
																			// if
																			// he
																			// wishes
																			// to
																			// read
																			// article
			} else
				twitterVoiceUser
						.setCurrentState(StateConstants.TWITTER_ACTION_MENU); // no
																				// link,
																				// so
																				// user
																				// can
																				// act
																				// upon
																				// the
																				// tweet
		}
		twitterVoiceUser = clearUser(twitterVoiceUser);
		if (debug)
			System.out.println("In MainMenu, giveMenu twitterVoiceUser : "
					+ twitterVoiceUser);
		return twitterVoiceUser;
	}

	private String stripText(String text) {
		String finalText = "";
		if (debug)
			System.out.println("Main menu stripText");
		if (text.indexOf("http") > -1) {
			text = text.substring(0, text.indexOf("http"));
		}
		
		finalText = StringNormalizer.normalize(text);
		finalText = finalText.replace("RT ", "Retweeting ");
		finalText = finalText.replace("MT ", "Modified tweet ");
		finalText = finalText.replace("HT ", "Tip of the hat to ");
		if (debug)
			System.out.println("In MainMenu, striptext text : " + finalText);
		return finalText;
	}

	private String getTwitterName(Tweet tweet, PagableResponseList<User> usres) {
		if (debug)
			System.out.println("Main menu getTwitterName");
		String tweeter = tweet.getFromUser();
		String tweeterName = null;
		for (User list : usres) {
			if (list.getName().equalsIgnoreCase(tweeter)) {
				tweeterName = list.getName();
			}
		}
		if (debug)
			System.out.println("Main menu getTwitterName tweeterName : "
					+ tweeterName);
		return tweeterName;
	}

	private boolean present(long needle, long[] haystack) {
		boolean isPresent = false;
		for (int i = 0; i < 20 && !isPresent; i++) {
			if (haystack[i] == needle)
				isPresent = true;
		}
		if(debug)
		System.out.println("In present : " + isPresent);
		return isPresent;
	}

	// Get the latest tweet based on timestamp
	private TwitterVoiceUser getLatestTweet(List<Tweet> newsFeed,
			TwitterVoiceUser twitterVoiceUser) {
		Tweet latestTweet = null;
		if (debug)
			System.out.println("Main menu getLatestTweet");
		for (Tweet tweet : newsFeed) {
 			if (latestTweet == null)
 				latestTweet = tweet;
 			else if(present(latestTweet.getId(),
 								twitterVoiceUser.getReadTweets())){
 				latestTweet = null;
 			}
 			else  				
 				if (latestTweet.getCreatedAt().before(tweet.getCreatedAt())
 						&& !present(tweet.getId(),
 								twitterVoiceUser.getReadTweets()) && tweet.getFromUser().equalsIgnoreCase(twitterVoiceUser.getReadTweetsBy().getScreenName())) {
 					latestTweet = tweet;
 				
 			}
 		}
		if (debug)
			System.out.println("Main menu getLatestTweet latestTweet : "
					+ latestTweet.getText() + latestTweet.getCreatedAt());
		twitterVoiceUser = setLatestTweet(twitterVoiceUser, latestTweet);
		return twitterVoiceUser;
	}

	private TwitterVoiceUser setLatestTweet(TwitterVoiceUser twitterVoiceUser,
			Tweet latestTweet) {
		twitterVoiceUser.setCurrentTweet(latestTweet);
		twitterVoiceUser.setReadTweetsBy(getUser(latestTweet.getFromUser(),
				twitterVoiceUser)); // set the Twitter user whose tweet we are
									// going to read
		int i = 0;
		long[] temp = twitterVoiceUser.getReadTweets();
		for (i = 0; i < 20 && temp[i] != 0; i++);
		if(debug)
		System.out.println("in setLatestTweet : " + i);	
		temp[i] = latestTweet.getId();
		twitterVoiceUser.setReadTweets(temp);
		if(debug)
			System.out.println("In set latest tweet, twitterUser is : " + twitterVoiceUser);
		return twitterVoiceUser;
	}

	// Read tweets by XXX, similar logic to news feed
	public TwitterVoiceUser giveTweets(TwitterVoiceUser twitterVoiceUser) {
		if (debug)
			System.out.println("Main menu giveTweets");
		twitterVoiceUser = clearUser(twitterVoiceUser);
		List<Tweet> newsFeed = new ArrayList<Tweet>();
		List<Tweet> tweets = (List<Tweet>) GetTweets
				.getTweets(twitterVoiceUser).getCurrentOuptut();
		for (Tweet tweet : tweets) {
			newsFeed.add(tweet);
		}

		twitterVoiceUser = getLatestTweet(newsFeed, twitterVoiceUser);
		Tweet tweet = twitterVoiceUser.getCurrentTweet();
		twitterVoiceUser.setCurrentTweet(tweet);
		twitterVoiceUser.setReadTweetsBy(getUser(tweet.getFromUser(),
				twitterVoiceUser));

		String relDate = RelativeDate.getRelativeDate(tweet.getCreatedAt());
		System.out.println("Actual date : " + tweet.getCreatedAt() + " Relative date : " + relDate);
		String output = stripText(tweet.getText());
		twitterVoiceUser.setNewsFeed(false);
		
		twitterVoiceUser = this.giveOutput(twitterVoiceUser, twitterVoiceUser
				.getReadTweetsBy().getName() + " " + "tweeted this " + relDate);
		twitterVoiceUser = this.giveOutput(twitterVoiceUser, output);
		twitterVoiceUser.setPrevState(twitterVoiceUser.getCurrentState());
		if (twitterVoiceUser.getCurrentTweet().getURLEntities().length >0) {
			twitterVoiceUser.setCurrentState(StateConstants.READ_ARTICLE_MENU);
		} else {
			twitterVoiceUser
					.setCurrentState(StateConstants.TWITTER_ACTION_MENU);
		}
		twitterVoiceUser = clearUser(twitterVoiceUser);
		if (debug)
			System.out.println("In MainMenu, giveMenu twitterVoiceUser : "
					+ twitterVoiceUser);
		return twitterVoiceUser;
	}

	public void giveExitMessage(TwitterVoiceUser twitterVoiceUser) {
		twitterVoiceUser = this.givePrompt(twitterVoiceUser, "exit");

	}

	// Construct confirmation message based on user input
	@Override
	public String getConfirmationMessage(String prevResponse) {
		if (debug)
			System.out.println("Main menu confirmation");
		String confirmationMessage = "";
		if (prevResponse.contains("Command: LIST")) {
			confirmationMessage = "You said you would like to read your twitter voice list right?";
		} else if (prevResponse.contains("Command: TRENDING TOPICS")) {
			confirmationMessage = "you said you would like to hear trending topics right";
		} else if (prevResponse.contains("EXIT")
				|| prevResponse.contains("QUIT")) {
			confirmationMessage = "You said you would like to exit right";
		} else if (prevResponse.contains("Command: NEWS FEED")) {
			confirmationMessage = "You said you would like your news feed right";
		} else if (prevResponse.contains("EXIT")
				|| prevResponse.contains("QUIT")) {
			confirmationMessage = "You said you would like to exit right";
		} else if (prevResponse.contains("HELP")) {
			confirmationMessage = "You said you would like some help right";
		} else if (prevResponse.contains("Command: TWEETS BY")) {
			String twitterName = prevResponse.substring(
					prevResponse.indexOf("Twitter Handle: "),
					prevResponse.indexOf("Command"));
			twitterName = twitterName.substring(twitterName.indexOf(':') + 2);
			
			confirmationMessage = "you said you would like to read tweets by "
					+ twitterName + " right";

		}
		if (debug)
			System.out.println("Main menu confirmation : "
					+ confirmationMessage);
		return confirmationMessage;

	}

}
