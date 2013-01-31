package twitterVoice.menu;

import twitter4j.URLEntity;

import twitterVoice.user.TwitterVoiceUser;
import twitterVoice.util.ArticleScraper;
import twitterVoice.util.RuntimeInterface;
import twitterVoice.util.StateConstants;
import twitterVoice.util.TwitterVoiceInputOutput;

/**
 * This class invokes to read article linked to in the tweet in question
 * 
 * @author Eli Pincus, Rashmi Raman
 */

public class ReadArticleMenu extends TwitterVoiceInputOutput {

	public TwitterVoiceUser giveMenu(TwitterVoiceUser twitterVoiceUser) {
		twitterVoiceUser = clearUser(twitterVoiceUser);
		if (debug)
			System.out.println("Read article menu");

		twitterVoiceUser = this.givePrompt(twitterVoiceUser, "readArticle"); // Ask
																				// user
																				// if
																				// he
																				// wishes
																				// to
																				// read
																				// article
		twitterVoiceUser = this.getInput(twitterVoiceUser); // get input
		String prevResponse = this.interpretInput(
				RuntimeInterface.pythonLinkScript, twitterVoiceUser);// send to
																		// ASR
		if (prevResponse.contains("None") ||prevResponse.contains("UNSPECIFIED")) {
			twitterVoiceUser = this.givePrompt(twitterVoiceUser, "inputError");
			twitterVoiceUser.setPrevState(twitterVoiceUser.getCurrentState());
			twitterVoiceUser = clearUser(twitterVoiceUser);
			twitterVoiceUser.setCurrentState(StateConstants.READ_ARTICLE_MENU);
			twitterVoiceUser = getLatestInputFile(twitterVoiceUser);

		} else {

			twitterVoiceUser = getLatestInputFile(twitterVoiceUser);

			twitterVoiceUser = this.giveOutput(twitterVoiceUser,
					getConfirmationMessage(prevResponse)); // Ask confirmation
			twitterVoiceUser = this.getInput(twitterVoiceUser); // get input
			String confirmation = this.interpretInput(
					RuntimeInterface.pythonConfScript, twitterVoiceUser); // process
																			// confirmation
																			// in
																			// ASR
			twitterVoiceUser = getLatestInputFile(twitterVoiceUser);
			if (confirmation.contains("None") || confirmation.contains("UNSPECIFIED")){
				twitterVoiceUser = this.givePrompt(twitterVoiceUser,
						"inputError");
				twitterVoiceUser.setPrevState(twitterVoiceUser
						.getCurrentState());
				twitterVoiceUser = clearUser(twitterVoiceUser);
				twitterVoiceUser.setCurrentState(StateConstants.READ_ARTICLE_MENU);
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
					twitterVoiceUser.setPrevState(twitterVoiceUser
							.getCurrentState());
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
						
						twitterVoiceUser
								.setCurrentState(StateConstants.READ_ARTICLE); // user
																				// wants
																				// to
																				// read
																				// article
					} else if (prevResponse.contains("EXIT")
							|| prevResponse.contains("Command: QUIT")) {
						twitterVoiceUser.setCurrentState(StateConstants.EXIT);
					} else if(prevResponse.contains("HELP")){
						twitterVoiceUser.setPrevState(twitterVoiceUser.getCurrentState());
						twitterVoiceUser.setCurrentState(StateConstants.HELP);
					
					} else {
						twitterVoiceUser
								.setCurrentState(StateConstants.TWITTER_ACTION_MENU);
					}
				} else if (confirmation.contains("EXIT")
						|| confirmation.contains("QUIT")) {
					twitterVoiceUser.setCurrentState(StateConstants.EXIT);
				} else if (confirmation.contains("HELP")) {
					twitterVoiceUser.setPrevState(twitterVoiceUser
							.getCurrentState());
					twitterVoiceUser.setCurrentState(StateConstants.HELP);
				}
				// user did not want to read article, hence continue reading
				// tweets
				else {
					twitterVoiceUser.setPrevState(twitterVoiceUser
							.getCurrentState());
					twitterVoiceUser
							.setCurrentState(StateConstants.TWITTER_ACTION_MENU);
				}
				twitterVoiceUser = clearUser(twitterVoiceUser);
			}
		}
		if (debug)
			System.out
					.println("In ReadArticleMenu, giveMenu twitterVoiceUser : "
							+ twitterVoiceUser);
		return twitterVoiceUser;
	}

	// Get scraped article from link in tweet
	public TwitterVoiceUser giveArticle(TwitterVoiceUser twitterVoiceUser) {
		if (debug)
			System.out.println("In giveArticle");
		twitterVoiceUser = clearUser(twitterVoiceUser);
		URLEntity[] urlEntities = twitterVoiceUser.getCurrentTweet()
				.getURLEntities();
		String link = urlEntities[0].getExpandedURL().toString();
		ArticleScraper articleScraper = new ArticleScraper(link);
		String article = articleScraper.scrapeArticle();
		
			twitterVoiceUser = this.giveOutput(twitterVoiceUser, article);
			twitterVoiceUser.setPrevState(twitterVoiceUser.getCurrentState());
			twitterVoiceUser.setCurrentState(StateConstants.TWITTER_ACTION_MENU);

		
		twitterVoiceUser = clearUser(twitterVoiceUser);
		if (debug)
			System.out
					.println("In ReadArticleMenu, giveArticle twitterVoiceUser : "
							+ twitterVoiceUser);
		return twitterVoiceUser;
	}

	

	// Construct confirmation page based on user input
	@Override
	public String getConfirmationMessage(String prevResponse) {
		if (debug)
			System.out.println("Main menu confirmation");
		String confirmationMessage = "";
		if (prevResponse.contains("YES")
				|| prevResponse.contains("YEAH")
				|| prevResponse.contains("OK")
				|| prevResponse.contains("RIGHT")
				|| prevResponse.contains("YUP")
				|| prevResponse.contains("YEP")
				|| prevResponse.contains("CORRECT")						
				){
			confirmationMessage = "You said you would like to read this article right";
		} else if (prevResponse.contains("EXIT")
				|| prevResponse.contains("QUIT")) {
			confirmationMessage = "You said you would like to exit right";
		} else if (prevResponse.contains("HELP")) {
			confirmationMessage = "You said you would like some help right";
		} else {
			confirmationMessage = "You said you don't want to read this article right";
		}
		if (debug)
			System.out.println("Read article menu confirmation : "
					+ confirmationMessage);
		return confirmationMessage;
	}

}
