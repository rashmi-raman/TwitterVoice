package twitterVoice.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class LogRecorder {
	
	private File logFile;
	private Writer output = null;

	public LogRecorder(String fileName) {
		 		 
		logFile = new File(fileName);
		
		   try {
			   if(!logFile.exists())
					  logFile.createNewFile();
				
			output = new BufferedWriter(new FileWriter(logFile));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		  
	}
	
	public void log(String text){
		try {
			this.output.write(text);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public void close(){
		try {
			this.output.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public Writer getOutput() {
		return output;
	}

	public void setOutput(Writer output) {
		this.output = output;
	}

	public File getLogFile() {
		return logFile;
	}

	public void setLogFile(File logFile) {
		this.logFile = logFile;
	}

	

}
