package twitterVoice.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
/** This class provides utility methods to get properties from systemPrompts.proerties for various messages to users 

 * @author Eli Pincus, Rashmi Raman*/

public class SystemPromptProperties {
	
	private Properties systemPrompts = null;
	private static SystemPromptProperties systemPromptProperties = null;
	
	public static SystemPromptProperties getInstance(){
		if(systemPromptProperties == null){
			init();			
		}
		return systemPromptProperties;
	}

	private static void init() {
		systemPromptProperties = new SystemPromptProperties();
		Properties systemPrompts = new Properties();
		InputStream in = SystemPromptProperties.class.getResourceAsStream("/twitterVoice/systemPrompts.properties");
		try {
			systemPrompts.load(in);
			in.close();
			systemPromptProperties.setSystemPrompts(systemPrompts);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public Properties getSystemPrompts() {
		return systemPrompts;
	}

	public void setSystemPrompts(Properties systemPrompts) {
		this.systemPrompts = systemPrompts;
	}
	
	public static void displayProperties() {
		Enumeration e = getInstance().getSystemPrompts().propertyNames();

		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			System.out.println(key + " -- "
					+ getInstance().getSystemPrompts().getProperty(key));
		}
		
	}


}
