package twitterVoice.menu;

import twitterVoice.user.TwitterVoiceUser;
import twitterVoice.util.StateConstants;
import twitterVoice.util.TwitterVoiceInputOutput;

/**
 * This class determines the help message to be read out to the user based on
 * the state they are in when they invoked Help.
 * 
 * @author Eli Pincus, Rashmi Raman
 */
public class HelpMenu extends TwitterVoiceInputOutput {

	public TwitterVoiceUser giveMenu(TwitterVoiceUser twitterVoiceUser) {
		twitterVoiceUser = clearUser(twitterVoiceUser);
		if (debug) {
			System.out.println("In help menu");
		}
		int state = twitterVoiceUser.getPrevState();
		
		String helpMessage = "";
		switch (state) {
		case StateConstants.MAIN_MENU:
			helpMessage = "mainMenuHelp";
			break;
		case StateConstants.TWITTER_ACTION_MENU:
			helpMessage = "twitterActionMenuHelp";
			break;
		case StateConstants.READ_ARTICLE_MENU:
			helpMessage = "readArticleMenuHelp";
			break;
		case StateConstants.RESPOND:
			helpMessage = "responseHelp";
		break;
		}
		if (helpMessage == "") {
			state = twitterVoiceUser.getCurrentState();
			
			helpMessage = "";
			switch (state) {
			case StateConstants.MAIN_MENU:
				helpMessage = "mainMenuHelp";
				break;
			case StateConstants.TWITTER_ACTION_MENU:
				helpMessage = "twitterActionMenuHelp";
				break;
			case StateConstants.READ_ARTICLE_MENU:
				helpMessage = "readArticleMenuHelp";
				break;
			case StateConstants.RESPOND:
				helpMessage = "responseHelp";
			break;
			}

		}

		twitterVoiceUser = this.givePrompt(twitterVoiceUser, helpMessage); // read
																			// out
																			// help
																			// message
		twitterVoiceUser.setCurrentState(twitterVoiceUser.getPrevState()); // set
																			// the
																			// current
																			// state
																			// of
																			// the
																			// user
																			// to
																			// what
																			// it
																			// was
																			// before
																			// invoking
																			// help
		twitterVoiceUser.setPrevState(StateConstants.HELP); // now previous
															// state is help
		if (debug)
			System.out.println("In HelpMenu, giveMenu twitterVoiceUser : "
					+ twitterVoiceUser);
		return twitterVoiceUser;
	}

	@Override
	public String getConfirmationMessage(String prevResponse) {
		// TODO Auto-generated method stub
		return null;
	}

}
