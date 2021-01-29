package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IndexFinder {
	private HashMap<String, Pattern> patternMap = new HashMap<String, Pattern>();
	private Matcher matcher;
	private Pattern pattern;
	
	public ArrayList<Integer> getIndexes(String teststring, String tofind){
		ArrayList<Integer> foundIndexes = new ArrayList<Integer>();
		foundIndexes.clear();
		
		//checks if the text starts with tofind
	    matcher =  getPattern("^"+tofind+"\\W").matcher(teststring);
	    while (matcher.find()) {
	        foundIndexes.add(matcher.start());
	    }
		
	    //checks if the text contains [non-word]tofind[non-word]
		matcher = getPattern("(?=(\\W"+tofind+"\\W))").matcher(teststring);
	    
	    while (matcher.find()) {
	        foundIndexes.add(matcher.start() + 1);
	    }
	    
	    return foundIndexes;
	}
	private Pattern getPattern(String regex) {
		pattern = patternMap.get(regex);
		if(pattern == null) {
			pattern = Pattern.compile(regex);
			patternMap.put(regex, pattern);
		}
		return pattern;
	}
	public int getCount(String teststring, String tofind){
		int count = 0;
		int index = teststring.indexOf(tofind);
	    while (index >=0){
	        count += 1;
	        index = teststring.indexOf(tofind, index+tofind.length())   ;
	    }
		return count;
	}
	public ArrayList<int[]> findFromTo(String teststring, String from, String to, boolean DoNewlinesCount, int startoffset, int endoffset) {
		ArrayList<int[]> foundIndexesFromTo = new ArrayList<int[]>();
		
		foundIndexesFromTo.clear();
		
		String anyChar;
		if(DoNewlinesCount) {
			anyChar = "[\\s\\S]";
		}else {
			anyChar = ".";
		}
		
		matcher = getPattern(from+"("+anyChar+"*?)"+to).matcher(teststring);
	    
	    while (matcher.find()) {
	    	int[] match = {matcher.start()+startoffset, matcher.end()+endoffset};
	    	foundIndexesFromTo.add(match);
	    }
	    return foundIndexesFromTo;
	}
	public ArrayList<int[]> findFromTo(String teststring, String from, String to) {
		return findFromTo(teststring, from, to, true, 0, 0);
	}
}
