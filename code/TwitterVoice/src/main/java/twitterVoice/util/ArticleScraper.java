package twitterVoice.util;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ArticleScraper {

	//String input1;
	String link;

	
	public ArticleScraper(String link) {
		super();
		this.link = link;
		
	}
	
	public String scrapeArticle(){
		String article = "";
		if(this.link.contains("nyti") || this.link.contains("Krug")){
			try {
				article = getTweetLinkArticleNYT();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			try {
				article = getArticleScraperLinkArticleHuff();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return article;	
	}

	public boolean checkIfLink() {
		if (this.link == null) {
			return false;
		} else {
			return true;
		}
	}

	public String getTweetLinkArticleNYT() throws IOException {
	    boolean shrt=false;
	    if(this.link.substring(7,11).equalsIgnoreCase("nyti") || this.link.substring(7,11).equalsIgnoreCase("krug")){
			String[] days = { "first", "second", "third", "fourth", "fifth",
					"sixth", "seventh", "eighth", "ninth", "tenth", "eleventh",
					"twelfth", "thirteenth", "fourteenth", "fifteenth",
					"sixteenth", "seventeenth", "eighteenth", "nineteenth",
					"twentieth", "twenty first", "twenty second", "twenty third",
					"twenty fourth", "twenty fifth", "twenty sixth",
					"twenty seventh", "twenty eighth", "twenty ninth", "thirtieth",
					"thirty first" };

			Document doc = Jsoup.connect(this.link).get();
			String title = doc.title();
			String author = doc.select("meta[name=author]").first().attr("content");
			String firstPa;
			String datePubl;
			StringTokenizer dateParser;
			Elements firstP;
			firstP = doc.getElementsByClass("articleBody");
			Elements element = doc.getElementsByClass("dateline");
			datePubl = element.text();
			Element temp = firstP.first();
			if (temp == null) {
				datePubl = doc.getElementsByClass("date").text();
				dateParser = new StringTokenizer(datePubl);
				String first = dateParser.nextToken();
				String second = dateParser.nextToken();
				second = second.substring(0, 1);
				int sec = Integer.parseInt(second);
				boolean match = true;
				int c = 0;
				while (match) {
					if (sec == c + 1) {
						second = days[c];
						match = false;
					}
					c++;
				}

				String fourth = dateParser.nextToken();
				fourth = fourth.substring(0, 4);
				fourth = ArticleScraper.convertYear(fourth);
				datePubl = "on" + " " + first + " " + second + " " + fourth;
				firstP = doc.getElementsByClass("entry-content");
				firstPa = firstP.first().text();
				StringTokenizer parser = new StringTokenizer(firstPa);
				firstPa = "";
				int totalTok= parser.countTokens();
				if (totalTok >= 32) {
					for (int i = 0; i <= 25; i++) {
						firstPa = firstPa + " " + parser.nextToken();
					}
				} else {	
					for (int i = 0; i < totalTok; i++) {
						firstPa = firstPa + parser.nextToken();
						shrt=true;
					}
				}
			} else {
				firstPa = firstP.first().text();
				dateParser = new StringTokenizer(datePubl);
				String first = dateParser.nextToken();
				String second = dateParser.nextToken();
				String third = dateParser.nextToken();
				third = third.substring(0, 1);
				int thir = Integer.parseInt(third);
				boolean match = true;
				int c = 0;
				while (match) {
					if (thir == c + 1) {
						third = days[c];
						match = false;
					}
					c++;
				}

				String fourth = dateParser.nextToken();
				fourth = ArticleScraper.convertYear(fourth);
				datePubl = first + " " + second + " " + third + " " + fourth;
			}
			if(shrt)
			{
				firstPa=StringNormalizer.normalize(firstPa);
				title=StringNormalizer.normalize(title);
				String output = author + " wrote the article entitled " + title + " "
					 + datePubl;
				return output;
			}
			else
			{		
			firstPa=StringNormalizer.normalize(firstPa);
			title=StringNormalizer.normalize(title);
			String output = author + " wrote the article entitled " + title + " "
				 + datePubl + " writing " + firstPa;
			return output;
			}
	    }
	    else{
	    	String notCorrect = "Sorry, the article link is not supported.";
			return notCorrect;
	    }
		}// end method
	public static String convertYear(String n) {
		int year = Integer.parseInt(n);
		String[] years = { "nineteen hundred and ninety",
				"nineteen hundred and ninety one",
				"nineteen hundred and ninety two",
				"nineteen hundred and ninety three",
				"nineteen hundred and ninety four",
				"nineteen hundred and ninety five",
				"nineteen hundred and ninety six",
				"nineteen hundred and ninety seven",
				"nineteen hundred and ninety eight",
				"nineteen hundred and ninety nine", "two thousand",
				"two thousand and one", "two thousand and two",
				"two thousand and three", "two thousand and four",
				"two thousand and five", "two thousand and six",
				"two thousand and seven", "two thousand and eight",
				"two thousand and nine", "two thousand and ten",
				"two thousand and eleven", "two thousand and twelve",
				"two thousand and thirteen", "two thousand and fourteen" };
		int y = 1990;
		int i = 0;
		String wYear = "default";
		boolean match = true;
		while (match) {
			if (year == y) {
				wYear = years[i];
				match = false;
			}
			y++;
			i++;
		}
		return wYear;
	}

	public String getArticleScraperLinkArticleHuff() throws IOException {
		if (this.link.substring(7, 11).equalsIgnoreCase("huff")) {
			String[] days = { "first", "second", "third", "fourth", "fifth",
					"sixth", "seventh", "eighth", "ninth", "tenth", "eleventh",
					"twelfth", "thirteenth", "fourteenth", "fifteenth",
					"sixteenth", "seventeenth", "eighteenth", "nineteenth",
					"twentieth", "twenty first", "twenty second",
					"twenty third", "twenty fourth", "twenty fifth",
					"twenty sixth", "twenty seventh", "twenty eighth",
					"twenty ninth", "thirtieth", "thirty first" };
			Document doc = Jsoup.connect(this.link).get();
			String title = doc.select("meta[property=og:title]").first()
					.attr("content");
			String datePubl = doc.select("meta[name=publish_date]").first()
					.attr("content");
			String temp = datePubl.substring(5, 7);
			int day = Integer.parseInt(temp);
			boolean match = true;
			String dy = "default";
			int c = 0;
			while (match) {
				if (day == c + 1) {
					dy = days[c];
					match = false;
				}
				c++;
			}

			StringTokenizer toker = new StringTokenizer(datePubl);
			toker.nextToken();
			toker.nextToken();
			String month = toker.nextToken();
			String year = toker.nextToken();
			String yr = ArticleScraper.convertYear(year);
			datePubl = month + " " + dy + " " + yr;
			Elements firstP = doc.getElementsByClass("entry_body_text");
			String firstPara = firstP.first().text();
			StringTokenizer toker2 = new StringTokenizer(firstPara);
			String outFirstPara = toker2.nextToken();
			if (toker2.countTokens() >= 25) {
				for (int i = 1; i < 25; i++) {
					outFirstPara = outFirstPara + " " + toker2.nextToken();
				}
			} else {
				for (int i = 1; i < toker2.countTokens(); i++) {
					outFirstPara = outFirstPara + " " + toker2.nextToken();
				}
			}
			outFirstPara=StringNormalizer.normalize(outFirstPara);
			String author = doc.select("meta[name=author]").first()
					.attr("content");
			title=StringNormalizer.normalize(title);
			String output = author + " wrote the article entitled " + title
					+ " on " + datePubl + " writing " + outFirstPara;
			output= output.replace("nytimes.com", "the new york times");
			return output;
		} else {
			String notCorrect = "Sorry, the article link is not supported.";
			return notCorrect;
		}

	}
	
	
	
    


}// end class
