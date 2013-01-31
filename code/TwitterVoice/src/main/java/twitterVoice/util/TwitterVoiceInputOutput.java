package twitterVoice.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import twitterVoice.functions.TwitterVoiceFunction;
import twitterVoice.user.TwitterVoiceUser;
import twitterVoice.util.RuntimeInterface;
import twitterVoice.util.SystemPromptProperties;
/** This abstract class is a skeletal menu class but provides utility methods 
 * common to all menus. 

 * @author Eli Pincus, Rashmi Raman*/

public abstract class TwitterVoiceInputOutput {

	public Properties prompts = null;
	protected static boolean debug =false;
	
	
	
	
	public TwitterVoiceInputOutput() {	
		init();
	
	}
	
	private void init(){
		this.prompts = SystemPromptProperties
				.getInstance().getSystemPrompts(); //load systemPrompts.properties for various messages
		//if(debug )
			//SystemPromptProperties.displayProperties();	
		
		
	}
	
	//clear output and error before next interaction
	public TwitterVoiceUser clearUser(TwitterVoiceUser twitterVoiceUser){
		twitterVoiceUser.setCurrentOuptut(null);
		twitterVoiceUser.setCurrentError("");
		return twitterVoiceUser;
	}

	//invoke TTS script with message key and WAV file
	public TwitterVoiceUser givePrompt(TwitterVoiceUser user, String promptKey){
		if(promptKey!=null){
			
			String message = this.prompts.getProperty(promptKey); // get actual message from properties
			
			logOutput(user, message);
			//message=message.trim();
			message = message.replace(' ', '_'); //no whitespaces in command line arg
			RuntimeInterface.invokeTTSScript(RuntimeInterface.perlScript, message,user.getOutputWav());
			RuntimeInterface.invokeASRScript(RuntimeInterface.playScript, user.getOutputWav()); //play WAV file created by Festival
			user = getLatestOutputFile(user);
		}
		return user;
		
	}
		
	private void logOutput(TwitterVoiceUser user, String message) {
		
		Date date = new Date(); DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
		String text = dateFormat.format(date) + System.getProperty("line.separator") + "User state :" + StateConstants.getState(user.getCurrentState()) + System.getProperty("line.separator");
		text = text + "System : " + message + System.getProperty("line.separator") + user.getOutputWav() + System.getProperty("line.separator");	
		System.out.println("Output :" + text);
		user.getLog().log(text);
		
	}

	//increment the WAV file number for the next output - for logging purposes
	private TwitterVoiceUser getLatestOutputFile(TwitterVoiceUser user) {
		if(debug)
			System.out.println("TwitterVoiceInputOutput getLatestOutputFile");
		String outputFile = user.getOutputWav();
		//Integer num = new Integer(inputFile.substring(51, inputFile.indexOf('.'))).intValue();
				Integer num = new Integer(outputFile.substring(outputFile.lastIndexOf("/")+7, outputFile.indexOf('.'))).intValue();
				num++;
				String temp = num.toString();
				//String newInputFile = inputFile.substring(0, 51) + temp + inputFile.substring(inputFile.indexOf('.'));
				String newOutputFile = outputFile.substring(0, outputFile.lastIndexOf("/")+7) + temp + outputFile.substring(outputFile.indexOf('.'));
				
		user.setOutputWav(newOutputFile);
		if(debug)
			System.out.println("In getLatestOutputFile twitterVoiceUser : " +user);
		return user;
	}

	/**
	 * @see givePrompt
	 * @param user
	 * @param message
	 * @return
	 */
	public TwitterVoiceUser giveOutput(TwitterVoiceUser user, String message){	
		if(debug)
			System.out.println("TwitterVoiceInputOutput giveOutput");
		logOutput(user, message);
		message = message.replace(' ', '_');
		RuntimeInterface.invokeTTSScript(RuntimeInterface.perlScript, message,user.getOutputWav());
		RuntimeInterface.invokeASRScript(RuntimeInterface.playScript, user.getOutputWav());
		user = getLatestOutputFile(user);		
		if(debug)
			System.out.println("In giveOutput twitterVoiceUser : " +user);
		return user;
	}
	
	//invoke autorecord
	public TwitterVoiceUser getInput(TwitterVoiceUser user){
		if(debug)
			System.out.println("TwitterVoiceInputOutput getInput");
		RuntimeInterface.invokeASRScript(RuntimeInterface.listenScript, "500");		
		RuntimeInterface.renameScript(RuntimeInterface.autoRecordPyFile, user.getInputWav());
		
		if(debug)
			System.out.println("In getInput twitterVoiceUser : " +user);
		return user;
	}
	
	//increment input WAV file before next interaction
	protected TwitterVoiceUser getLatestInputFile(TwitterVoiceUser user) {
		if(debug)
			System.out.println("TwitterVoiceInputOutput getLatestInputFile");
		String inputFile = user.getInputWav();
		
		//Integer num = new Integer(inputFile.substring(51, inputFile.indexOf('.'))).intValue();
		Integer num = new Integer(inputFile.substring(inputFile.lastIndexOf("/")+7, inputFile.indexOf('.'))).intValue();
		num++;
		String temp = num.toString();
		//String newInputFile = inputFile.substring(0, 51) + temp + inputFile.substring(inputFile.indexOf('.'));
		String newInputFile = inputFile.substring(0, inputFile.lastIndexOf("/")+7) + temp + inputFile.substring(inputFile.indexOf('.'));
		user.setInputWav(newInputFile);
		if(debug)
			System.out.println("In getLatestInputFile twitterVoiceUser : " +user);
		return user;
	}

	
	//invoke ASR Python script with user input
	public String interpretInput(String theScript,TwitterVoiceUser user){
		if(debug)
			System.out.println("TwitterVoiceInputOutput interpretInput");
		String concept = RuntimeInterface.invokeASRScript(theScript, user.getInputWav());
		if(debug)
			System.out.println("TwitterVoiceInputOutput interpretInput concept : " + concept);
		logInput(user, concept);
		return concept;
	}
	
	private void logInput(TwitterVoiceUser user, String concept) {
		
		String lines[] = concept.split(System.getProperty("line.separator"));
		String recognizedString = "";
		String [] concepts = new String[2]; for(int j=0;j<2;j++) concepts[j]="";
		
		for(int i = 0,j=0; i<lines.length;i++){
			if(lines[i].contains("ASR Result:"))
					recognizedString = lines[i].substring(lines[i].indexOf("ASR Result:"));
			else if((lines[i].contains("Twitter Handle"))||
					(lines[i].contains("Command"))||
					(lines[i].contains("Twitter Action"))||
					(lines[i].contains("Confirmation"))||
					(lines[i].contains("Response"))){
				concepts[j] = lines[i];
				j++;
			}
		}
		Date date = new Date(); DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
		String text = dateFormat.format(date) + System.getProperty("line.separator") + "User state :" + StateConstants.getState(user.getCurrentState()) + System.getProperty("line.separator");
		text = text + "Recognized String [" + recognizedString + "]" + System.getProperty("line.separator");
		text = text + "Semantics : " + System.getProperty("line.separator");
		for(int j=0;j<2;j++){
			if(concepts[j]!=""){
				text += concepts[j] + "     ";
			}
		}
		text += System.getProperty("line.separator") + user.getInputWav() + System.getProperty("line.separator");
		System.out.println("Input : " + text);
		user.getLog().log(text);
		
	}

	public abstract TwitterVoiceUser giveMenu(TwitterVoiceUser user);
	
	public abstract String getConfirmationMessage(String prevResponse);

}
