package twitterVoice.menu;

import twitterVoice.functions.twitterActions.TwitterActions;
import twitterVoice.user.TwitterVoiceUser;
import twitterVoice.util.RuntimeInterface;
import twitterVoice.util.StateConstants;
import twitterVoice.util.TwitterVoiceInputOutput;

/**
 * This class invokes various functionalities associated with the TwitterVoice
 * Action menu take user input, pass to ASR, determine the action to be taken,
 * invoke relevant model class, send output to TTS, update state of the user.
 * 
 * @author Eli Pincus, Rashmi Raman
 */

public class TwitterActionMenu extends TwitterVoiceInputOutput {

	public static boolean debug = false;

	public TwitterVoiceUser giveMenu(TwitterVoiceUser twitterVoiceUser) {
		twitterVoiceUser = clearUser(twitterVoiceUser);
		if (debug)
			System.out.println("Twitter action menu");

		twitterVoiceUser = this.givePrompt(twitterVoiceUser, "twitterAction"); // read
																				// TwitterAction
																				// menu
		twitterVoiceUser = this.getInput(twitterVoiceUser); // get user choice
		String prevResponse = this.interpretInput(
				RuntimeInterface.pythonActionMenuScript, twitterVoiceUser); // send
																			// choice
																			// to
																			// ASR
		if (prevResponse.contains("None") || prevResponse.contains("Twitter Action: UNSPECIFIED")) {
			twitterVoiceUser = this.givePrompt(twitterVoiceUser, "inputError");
			twitterVoiceUser.setPrevState(twitterVoiceUser.getCurrentState());
			twitterVoiceUser = clearUser(twitterVoiceUser);
			twitterVoiceUser.setCurrentState(StateConstants.TWITTER_ACTION_MENU);
			twitterVoiceUser = getLatestInputFile(twitterVoiceUser);

		} else {

			twitterVoiceUser = getLatestInputFile(twitterVoiceUser);

			twitterVoiceUser = this.giveOutput(twitterVoiceUser,
					getConfirmationMessage(prevResponse)); // read confirmation
															// message
			twitterVoiceUser = this.getInput(twitterVoiceUser); // get
																// confirmation
			String confirmation = this.interpretInput(
					RuntimeInterface.pythonConfScript, twitterVoiceUser); // send
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
				twitterVoiceUser.setCurrentState(StateConstants.TWITTER_ACTION_MENU);
				twitterVoiceUser = getLatestInputFile(twitterVoiceUser);

			} else {

				// system understanding was correct
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
					else if (prevResponse.contains("Twitter Action: RETWEET")) {
						twitterVoiceUser
								.setCurrentState(StateConstants.RETWEET);
					} else if (prevResponse
							.contains("Twitter Action: FAVORITE")) {
						twitterVoiceUser
								.setCurrentState(StateConstants.FAVORITE);
					} else if (prevResponse.contains("Twitter Action: RESPOND")) {
						twitterVoiceUser
								.setCurrentState(StateConstants.RESPOND);
					} else if (prevResponse.contains("Twitter Action: CONTINUE")) {
						twitterVoiceUser.setContinue(true);
						twitterVoiceUser.setCurrentState(StateConstants.READ_MORE_TWEETS);
					} else if (prevResponse.contains("EXIT")
							|| prevResponse.contains("Command: QUIT")) {
						twitterVoiceUser.setCurrentState(StateConstants.EXIT);
					} else if (prevResponse.contains("HELP")) {
						twitterVoiceUser.setCurrentState(StateConstants.HELP);
					}
				} else if (confirmation.contains("EXIT")
						|| confirmation.contains("QUIT")) {
					twitterVoiceUser.setCurrentState(StateConstants.EXIT);
				} else if (confirmation.contains("HELP")) {
					twitterVoiceUser.setCurrentState(StateConstants.HELP);
				}

				else {
					twitterVoiceUser
							.setCurrentState(StateConstants.TWITTER_ACTION_MENU);
				}
			}
		}
		if (debug)
			System.out
					.println("In TwitterActionMenu, giveMenu twitterVoiceUser : "
							+ twitterVoiceUser);
		return twitterVoiceUser;

	}

	// Retweet
	public TwitterVoiceUser retweet(TwitterVoiceUser twitterVoiceUser) {
		if (debug)
			System.out.println("Retweet");
		twitterVoiceUser = clearUser(twitterVoiceUser);
		twitterVoiceUser = TwitterActions.retweet(twitterVoiceUser);
		String output = (String) twitterVoiceUser.getCurrentOuptut();
		String error = twitterVoiceUser.getCurrentError();
		if (error != "") { // Error, read message, go back to main menu
			twitterVoiceUser = this.giveOutput(twitterVoiceUser, error);
			twitterVoiceUser.setPrevState(twitterVoiceUser.getCurrentState());
			twitterVoiceUser.setCurrentState(StateConstants.TWITTER_ACTION_MENU);

		} else { // give status, go back to reading more tweets
			twitterVoiceUser = this.giveOutput(twitterVoiceUser, output);
			twitterVoiceUser.setPrevState(twitterVoiceUser.getCurrentState());
			twitterVoiceUser.setCurrentState(StateConstants.READ_MORE_TWEETS);
		}
		if (debug)
			System.out
					.println("In TwitterActionMenu, retweet twitterVoiceUser : "
							+ twitterVoiceUser);
		return twitterVoiceUser;

	}

	// Favorite
	public TwitterVoiceUser favorite(TwitterVoiceUser twitterVoiceUser) {
		if (debug)
			System.out.println("Favorite");
		twitterVoiceUser = clearUser(twitterVoiceUser);
		twitterVoiceUser = TwitterActions.favorite(twitterVoiceUser);
		String output = (String) twitterVoiceUser.getCurrentOuptut();
		String error = twitterVoiceUser.getCurrentError();
		if (error != "") { // error, read message, go back to main menu
			twitterVoiceUser = this.giveOutput(twitterVoiceUser, error);
			twitterVoiceUser.setPrevState(twitterVoiceUser.getCurrentState());
			twitterVoiceUser.setCurrentState(StateConstants.TWITTER_ACTION_MENU);

		} else { // otherwise, give status message, continue reading tweets
			twitterVoiceUser = this.giveOutput(twitterVoiceUser, output);
			twitterVoiceUser.setPrevState(twitterVoiceUser.getCurrentState());
			twitterVoiceUser.setCurrentState(StateConstants.READ_MORE_TWEETS);
		}
		twitterVoiceUser = clearUser(twitterVoiceUser);
		if (debug)
			System.out
					.println("In TwitterActionMenu, favorite twitterVoiceUser : "
							+ twitterVoiceUser);
		return twitterVoiceUser;

	}

	// Respond
	public TwitterVoiceUser respond(TwitterVoiceUser twitterVoiceUser) {
		if (debug)
			System.out.println("Respond");
		twitterVoiceUser = clearUser(twitterVoiceUser);

		twitterVoiceUser = this.givePrompt(twitterVoiceUser, "responseList"); // read
																				// out
																	// response
																				// templates
		twitterVoiceUser = this.getInput(twitterVoiceUser); // get user response
		String response = this.interpretInput(
				RuntimeInterface.pythonResponseScript, twitterVoiceUser); // send
																			// to
																			// ASR

		
		twitterVoiceUser = getLatestInputFile(twitterVoiceUser);

		twitterVoiceUser.setPrevState(twitterVoiceUser.getCurrentState());
		if (response.contains("HELP")) {
			twitterVoiceUser.setCurrentState(StateConstants.HELP);
		}else if (response.contains("None") || response.contains("UNSPECIFIED")){
			twitterVoiceUser = this.givePrompt(twitterVoiceUser,
					"inputError");
			twitterVoiceUser.setPrevState(twitterVoiceUser
					.getCurrentState());
			twitterVoiceUser = clearUser(twitterVoiceUser);
			twitterVoiceUser.setCurrentState(StateConstants.RESPOND);
			twitterVoiceUser = getLatestInputFile(twitterVoiceUser);

		}
		else {
			if (response.contains("THOUGHTFUL")
					|| response.contains("FIRST")) {
				twitterVoiceUser = TwitterActions.respond(twitterVoiceUser, 0);
			}else if (response.contains("WONDERFUL") ||response.contains("GREAT") || response.contains("SECOND")) {
				twitterVoiceUser = TwitterActions.respond(twitterVoiceUser, 1);
				
			} else if (response.contains("THINK ABOUT")
					|| response.contains("THIRD")) {
				twitterVoiceUser = TwitterActions.respond(twitterVoiceUser, 2);
			} 
			else if (response.contains("SUPPORT")
					|| response.contains("LAST")) {
				twitterVoiceUser = TwitterActions.respond(twitterVoiceUser, 3);
			 
			}

			String output = (String) twitterVoiceUser.getCurrentOuptut();
			String error = twitterVoiceUser.getCurrentError();
			if (error != "") { // error, read message, go back to main menu
				twitterVoiceUser = this.giveOutput(twitterVoiceUser, error);
				twitterVoiceUser.setPrevState(twitterVoiceUser
						.getCurrentState());
				twitterVoiceUser.setCurrentState(StateConstants.TWITTER_ACTION_MENU);

			} else { // read status, continue reading tweets
				twitterVoiceUser = this.giveOutput(twitterVoiceUser, output);
				twitterVoiceUser.setPrevState(twitterVoiceUser
						.getCurrentState());
				twitterVoiceUser
						.setCurrentState(StateConstants.READ_MORE_TWEETS);
			}
		}
		twitterVoiceUser = clearUser(twitterVoiceUser);
		if (debug)
			System.out
					.println("In TwitterActionMenu, respond twitterVoiceUser : "
							+ twitterVoiceUser);
		return twitterVoiceUser;
	}

	public TwitterVoiceUser readMoreTweets(TwitterVoiceUser twitterVoiceUser) {
		if (debug)
			System.out.println("Read more tweets");
		twitterVoiceUser = clearUser(twitterVoiceUser);
		if(!twitterVoiceUser.isContinue()){
		twitterVoiceUser = this.givePrompt(twitterVoiceUser, "readMoreTweets"); // continue
																				// reading
																				// tweets?
		twitterVoiceUser = this.getInput(twitterVoiceUser); // get user choice
		String prevResponse = this.interpretInput(
				RuntimeInterface.pythonConfScript, twitterVoiceUser); // send
		if (prevResponse.contains("YES")
				|| prevResponse.contains("YEAH")
				|| prevResponse.contains("OK")
				|| prevResponse.contains("RIGHT")
				|| prevResponse.contains("YUP")
				|| prevResponse.contains("YEP")
				|| prevResponse.contains("CORRECT")						
				) {
			twitterVoiceUser = this.givePrompt(twitterVoiceUser,
					"ok");
			twitterVoiceUser.setPrevState(twitterVoiceUser.getCurrentState());	
			if (twitterVoiceUser.isNewsFeed())
				twitterVoiceUser.setCurrentState(StateConstants.NEWS_FEED);
			else
				twitterVoiceUser.setCurrentState(StateConstants.READ_TWEETS);
		twitterVoiceUser = clearUser(twitterVoiceUser);
		twitterVoiceUser = getLatestInputFile(twitterVoiceUser);
		}else{
			twitterVoiceUser.setPrevState(twitterVoiceUser.getCurrentState());	
			twitterVoiceUser.setCurrentState(StateConstants.MAIN_MENU);
			twitterVoiceUser = clearUser(twitterVoiceUser);
			twitterVoiceUser = getLatestInputFile(twitterVoiceUser);
		}
		}else{
			twitterVoiceUser.setPrevState(twitterVoiceUser.getCurrentState());	
			if (twitterVoiceUser.isNewsFeed())
				twitterVoiceUser.setCurrentState(StateConstants.NEWS_FEED);
			else
				twitterVoiceUser.setCurrentState(StateConstants.READ_TWEETS);
			twitterVoiceUser = clearUser(twitterVoiceUser);
			twitterVoiceUser.setContinue(false);
		}
		if (debug)
			System.out
					.println("In TwitterActionMenu, read more tweets twitterVoiceUser : "
							+ twitterVoiceUser.getCurrentState());
		return twitterVoiceUser;
	}

	// Construct confirmation message based on user choice
	@Override
	public String getConfirmationMessage(String prevResponse) {
		if (debug)
			System.out.println("TwitterAction menu confirmation");
		String confirmationMessage = "";
		if (prevResponse.contains("Twitter Action: RETWEET")) {
			confirmationMessage = "You said you would like to retweet this tweet right";
		} else if (prevResponse.contains("EXIT")
				|| prevResponse.contains("QUIT")) {
			confirmationMessage = "You said you would like to exit right";
		} else if (prevResponse.contains("Twitter Action: FAVORITE")) {
			confirmationMessage = "You said you would like to favorite this tweet right";
		} else if (prevResponse.contains("Twitter Action: RESPOND")) {
			confirmationMessage = "You said you would like to respond to this tweet right";
		} else if (prevResponse.contains("HELP")) {
			confirmationMessage = "You said you would like some help right";
		} else if (prevResponse.contains("Twitter Action: CONTINUE")) {
			confirmationMessage = "You said you would like to continue reading tweets right";

		}
		if (debug)
			System.out.println("Twitter action menu confirmation : "
					+ confirmationMessage);
		return confirmationMessage;
	}

}
