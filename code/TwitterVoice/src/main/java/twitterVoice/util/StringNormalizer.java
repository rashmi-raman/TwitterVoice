package twitterVoice.util;

import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class StringNormalizer {
	
	public static String normalize(String input)
	{
		StringTokenizer parser= new StringTokenizer(input);
		String out = "";
		int numberOfTokens=parser.countTokens();
		for(int i=0; i<numberOfTokens; i++)
		{
			String current= parser.nextToken();
		    if(!(Pattern.matches("[a-zA-Z0-9]+", current)))
				{
				
				}
		    else{
		    	out=out+" "+current;
		    }
		}
		out=out.replace(".com", " dot com");
		return out;			
		
	}
	
}
