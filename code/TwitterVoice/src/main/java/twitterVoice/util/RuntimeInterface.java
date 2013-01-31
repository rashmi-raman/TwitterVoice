package twitterVoice.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * This class provides utility methods to invoke various processes.
 * 
 * @author Eli Pincus, Rashmi Raman
 */

public abstract class RuntimeInterface {

	public static final String perlScript = "/proj/speech/users/cs4706/rr2779/partc/tts_latest.pl";
	public static final String playScript = "play";
	public static String listenScript = "/proj/speech/tools/autorecord/record.py";
	public static final String pythonMainMenuScript = "/proj/speech/users/cs4706/asrhw/rr2779/recognize_concepts_main.py";
	public static final String pythonActionMenuScript = "/proj/speech/users/cs4706/asrhw/rr2779/recognize_concepts_actions.py";
	public static final String pythonLinkScript = "/proj/speech/users/cs4706/asrhw/rr2779/recognize_concepts_link.py";
	public static final String pythonConfScript = "/proj/speech/users/cs4706/asrhw/rr2779/recognize_concepts_conf.py";
	public static final String pythonResponseScript = "/proj/speech/users/cs4706/asrhw/rr2779/recognize_concepts_resp.py";
	public static String autoRecordPyFile = "/proj/speech/users/cs4706/rr2779/part3/output.wav";
	public static boolean debug = false;

	public static void setFile(String host, String path){
		if(host.contains("gatto"))
			listenScript = "/proj/speech/tools/autorecord-64bit/record.py";
		else
			listenScript = "/proj/speech/tools/autorecord/record.py";
		autoRecordPyFile = path+"/output.wav";
	}
	
	// Invoking festival
	public static String invokeTTSScript(String theScript, String theSentence,
			String theOutputWav) {
		
		String[] commandLineArg = null;

		commandLineArg = new String[4];
		commandLineArg[0] = "perl";
		commandLineArg[1] = theScript;
		commandLineArg[2] = theSentence;
		commandLineArg[3] = theOutputWav;

		String outputBuffer = "";
		try {
			Runtime r = Runtime.getRuntime();
			if (debug)
				System.out.println("About to run : " + commandLineArg[0]
						+ commandLineArg[1] + commandLineArg[2]
						+ commandLineArg[3]);
			Process p = r.exec(commandLineArg);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			int returnCode = p.waitFor();
			if (debug)
				System.out
						.println("returnCode from the script : " + returnCode);

			while (br.ready()) {
				outputBuffer += br.readLine();

			}
			

		} catch (Exception e) {
			String cause = e.getMessage();
			if (cause.equals("script: not found"))
				System.out
						.println("Problem with executing script " + theScript);
		}
		return outputBuffer;
	}

	// invoke Python script for ASR
	public static String invokeASRScript(String theScript, String theInputWav) {
		String[] commandLineArg = null;

		commandLineArg = new String[2];
		commandLineArg[0] = theScript;
		commandLineArg[1] = theInputWav;

		String outputBuffer = "";
		try {
			Runtime r = Runtime.getRuntime();
			if (debug)
				System.out.println("About to run : " + commandLineArg[0] + " "
						+ commandLineArg[1]);
			Process p = r.exec(commandLineArg);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			int returnCode = p.waitFor();
			if (debug)
				System.out
						.println("returnCode from the script : " + returnCode);

			while (br.ready()) {
				outputBuffer += br.readLine() + System.getProperty("line.separator");

			}
			

		} catch (Exception e) {
			String cause = e.getMessage();
			if (cause.equals("script: not found"))
				System.out
						.println("Problem with executing script " + theScript);
		}
		return outputBuffer;
	}

	// renaming input file since autorecord always creates a file called
	// output.wav and we want to maintain all the input WAV files
	
	
	
	public static void mkdirScript(String path) {
		
		try{
		File f = new File(path);
	    f.mkdir();
		
		
	   
		} catch (Exception e) {
			String cause = e.getMessage();
			
			if (cause.equals("script: not found"))
				System.out.println("Problem with executing rename script ");
		}

		
	}

	// renaming input file since autorecord always creates a file called
	// output.wav and we want to maintain all the input WAV files
	public static String renameScript(String origName, String newName) {
		String[] commandLineArg = null;

		commandLineArg = new String[3];
		commandLineArg[0] = "mv";
		commandLineArg[1] = origName;
		commandLineArg[2] = newName;

		String outputBuffer = "";
		try {
			Runtime r = Runtime.getRuntime();
			if (debug)
				System.out.println("About to run : " + commandLineArg[0] + " "
						+ commandLineArg[1] + " " + commandLineArg[2]);
			Process p = r.exec(commandLineArg);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			int returnCode = p.waitFor();
			if (debug)
				System.out
						.println("returnCode from the script : " + returnCode);

			while (br.ready()) {
				outputBuffer += br.readLine();

			}
			

		} catch (Exception e) {
			String cause = e.getMessage();
			if (cause.equals("script: not found"))
				System.out.println("Problem with executing rename script ");
		}
		return outputBuffer;
	}

}