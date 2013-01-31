package twitterVoice.user;

import java.util.Arrays;

import twitter4j.Tweet;
import twitter4j.User;
import twitterVoice.util.LogRecorder;

/** This bean stores information about the current user 
 * @author Eli Pincus, Rashmi Raman*/

public class TwitterVoiceUser {
	
	/** Name of the user */
	private String name;
	
	/** User's twitter Handle */
	private String twitterHandle;
	
	/** User's current state. */
	private int currentState;
	
	/** User's prev state */
	private int prevState;
	
	/** String to store the latest outout WAV file played to user - for logging */
	private String outputWav;
	
	/** Current tweet read to user , may need to action on this tweet! */
	private Tweet currentTweet;
	
	/** Who tweeted this tweet .*/
	private User readTweetsBy;
	
	/** Latest output based on interaction */
	private Object currentOuptut;
	
	/** Latest error message */
	private String currentError;
	
	/** Is user reading news feed? */
	private boolean isNewsFeed = false;
	
	private LogRecorder log = null;
	
	private long[] readTweets = null;
	
	private boolean isContinue = false;
	
	public boolean isContinue() {
		return isContinue;
	}
	public void setContinue(boolean isContinue) {
		this.isContinue = isContinue;
	}
	public long[] getReadTweets() {
		return readTweets;
	}
	public void setReadTweets(long[] readTweets) {
		this.readTweets = readTweets;
	}
	public LogRecorder getLog() {
		return log;
	}
	public void setLog(LogRecorder log) {
		this.log = log;
	}
	public boolean isNewsFeed() {
		return isNewsFeed;
	}
	public void setNewsFeed(boolean isNewsFeed) {
		this.isNewsFeed = isNewsFeed;
	}
	public Object getCurrentOuptut() {
		return currentOuptut;
	}
	public void setCurrentOuptut(Object currentOuptut) {
		this.currentOuptut = currentOuptut;
	}
	public String getCurrentError() {
		return currentError;
	}
	public void setCurrentError(String currentError) {
		this.currentError = currentError;
	}
	
	@Override
	public String toString() {
		if(currentTweet!=null && readTweetsBy!=null){
		return "TwitterVoiceUser [name=" + name + ", twitterHandle="
				+ twitterHandle + ", currentState=" + currentState
				+ ", prevState=" + prevState + ", outputWav=" + outputWav
				+ ", currentTweet=" + currentTweet.getText() + ", readTweetsBy="
				+ readTweetsBy.getName() + ", currentOuptut=" + currentOuptut
				+ ", currentError=" + currentError + ", isNewsFeed="
				+ isNewsFeed  + ", inputWav=" + inputWav + "]";
		}else{
			return "TwitterVoiceUser [name=" + name + ", twitterHandle="
					+ twitterHandle + ", currentState=" + currentState
					+ ", prevState=" + prevState + ", outputWav=" + outputWav
				    + ", currentOuptut=" + currentOuptut
					+ ", currentError=" + currentError + ", isNewsFeed="
					+ isNewsFeed  + ", inputWav=" + inputWav + "]";
		}
	}
	public User getReadTweetsBy() {
		return readTweetsBy;
	}
	public void setReadTweetsBy(User readTweetsBy) {
		this.readTweetsBy = readTweetsBy;
	}
	public Tweet getCurrentTweet() {
		return currentTweet;
	}
	public void setCurrentTweet(Tweet currentTweet) {
		this.currentTweet = currentTweet;
	}
	public String getTwitterHandle() {
		return twitterHandle;
	}
	public void setTwitterHandle(String twitterHandle) {
		this.twitterHandle = twitterHandle;
	}
	
	public String getOutputWav() {
		return outputWav;
	}
	public void setOutputWav(String outputWav) {
		this.outputWav = outputWav;
	}
	public String getInputWav() {
		return inputWav;
	}
	public void setInputWav(String inputWav) {
		this.inputWav = inputWav;
	}

	private String inputWav;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCurrentState() {
		return currentState;
	}
	public void setCurrentState(int currentState) {
		this.currentState = currentState;
	}
	public int getPrevState() {
		return prevState;
	}
	public void setPrevState(int prevState) {
		this.prevState = prevState;
	}
	
	public TwitterVoiceUser(String name, String twitterHandle, int currentState, int prevState, String path) {
		super();
		this.name = name;
		this.twitterHandle = twitterHandle;
		this.currentState = currentState;
		this.prevState = prevState;
		this.inputWav = path + "/audio/listen0.wav";
		this.outputWav = path + "/audio/output0.wav";		
		this.log = new LogRecorder(path + "/log/enp2102_rr2779_twitterVoiceLog.txt");
		this.readTweets = new long[20];
		this.currentError = "";
		this.currentOuptut = "";
		for(int i=0;i<20;i++) readTweets[i] = 0;
		
	}
	
	
}