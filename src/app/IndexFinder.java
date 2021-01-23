package app;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IndexFinder {
	public ArrayList<Integer> getIndexes(String teststring, String tofind){
		ArrayList<Integer> foundIndexes = new ArrayList<Integer>();
		
		//checks if the text starts with tofind
		Pattern pattern = Pattern.compile("^"+tofind+"\\W");
	    Matcher matcher =  pattern.matcher(teststring);
	    while (matcher.find()) {
	        foundIndexes.add(matcher.start());
	    }
		
	    //checks if the text contains [non-word]tofind[non-word]
		pattern = Pattern.compile("(?=(\\W"+tofind+"\\W))");
		matcher = pattern.matcher(teststring);
	    
	    while (matcher.find()) {
	        foundIndexes.add(matcher.start() + 1);
	    }
	    
	    return foundIndexes;
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
		ArrayList<int[]> foundIndexes = new ArrayList<int[]>();
		
		String anyChar;
		if(DoNewlinesCount) {
			anyChar = "[\\s\\S]";
		}else {
			anyChar = ".";
		}
		
		Pattern pattern = Pattern.compile(from+"("+anyChar+"*?)"+to);
		Matcher matcher = pattern.matcher(teststring);
	    
	    while (matcher.find()) {
	    	int[] match = {matcher.start()+startoffset, matcher.end()+endoffset};
	    	foundIndexes.add(match);
	    }
	    
	    return foundIndexes;
	}
	public ArrayList<int[]> findFromTo(String teststring, String from, String to) {
		return findFromTo(teststring, from, to, true, 0, 0);
	}
}
