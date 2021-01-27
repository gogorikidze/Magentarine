package app;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JTextPane;

public class ColorHighlighter {
	IndexFinder indexFinder = new IndexFinder();
	
	StyleContext styleContext;
	AttributeSet mainTextColor;
	JTextPane textPane;
	JTextPane bufferPane;
	
	
	public ColorHighlighter(JTextPane textPaneToSet, JTextPane bufferPaneToSet) {
		styleContext = StyleContext.getDefaultStyleContext();
		
		textPane = textPaneToSet;
		bufferPane = bufferPaneToSet;
		textPane.setCaretColor(textPaneToSet.getForeground());
		mainTextColor = colorToAS(textPaneToSet.getForeground());
	}
	public ColorHighlighter(JTextPane textPaneToSet) {
		styleContext = StyleContext.getDefaultStyleContext();
		
		textPane = textPaneToSet;
		textPane.setCaretColor(textPaneToSet.getForeground());
		mainTextColor = colorToAS(textPaneToSet.getForeground());
	}
	public void clearColors() {  //selects the whole text from [0] to [length - 1] and applies white color
		String text = textPane.getText();
        
		textPane.setSelectionStart(0);
		textPane.setSelectionEnd(text.length());
		textPane.setCharacterAttributes( mainTextColor, false);
	}
	public void colorString(int start, int end, AttributeSet color) { // applies color from [start] to [end]
		textPane.setSelectionStart(start);
		textPane.setSelectionEnd(end);
		textPane.setCharacterAttributes(color, false);
	}
	public AttributeSet colorToAS(Color color) { //creates Attributeset from [Color.color]
		return styleContext.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);
	}
	public void highlightGroup(ArrayList<String> toHighLight, Color color) throws BadLocationException { // applies color to
		for(int i = 0; i < toHighLight.size(); i++) {
			ArrayList<Integer> indexes = indexFinder.getIndexes(textPane.getDocument().getText(0, textPane.getDocument().getLength()), toHighLight.get(i));
			for(int ii = 0; ii < indexes.size(); ii++) {
				colorString(indexes.get(ii), indexes.get(ii)+toHighLight.get(i).length(), colorToAS(color));
			}
		}
	}
	public void colorFromTo(String from, String to, Color color, boolean DoNewlinesCount, int startoffset, int endoffset) throws BadLocationException {
		//colors from [string] to [string], [DoNewlineCount] dictates if regex can detect on multiple lines.
		//on startoffset -1 '$string%' will rerturn '$string', on 0 it will return 'string', on 1 it will only return 'tring'
		//on startoffset -1 '$string%' will rerturn 'strin', on 0 it will return 'string', on 1 it will only return 'string%'
		ArrayList<int[]> indexes = indexFinder.findFromTo(textPane.getDocument().getText(0, textPane.getDocument().getLength()), from, to, DoNewlinesCount, startoffset, endoffset);
		for(int i = 0; i < indexes.size(); i++) {
			colorString(indexes.get(i)[0], indexes.get(i)[1], colorToAS(color));
		}
	}
	public void colorFromTo(String from, String to, Color color) throws BadLocationException {
		colorFromTo(from, to, color, true, 0, 0);
	}
	public void highlightMeasurements(ArrayList<String> toHighLight, Color color) throws BadLocationException {
		for(int i = 0; i < toHighLight.size(); i++) {
			colorFromTo("\\W", toHighLight.get(i), color);
		}
	}
	public void highlight(JTextPane textPane, Theme theme) {
		try {
			int selectionStart = textPane.getSelectionStart(); int selectionEnd = textPane.getSelectionEnd(); // stores the selection for later
			int caretPosition = textPane.getCaretPosition(); // stores the starting caret position for later
			clearColors(); // clear the text of any coloring every everytime before applying colors again
			
			colorFromTo("\\.", "\\W", theme.propertyColor, false, 0, -1);
			
			//applies colors to specific words if they turn out to be in structure of [\\w]term[\\w]
			ArrayList<String> keywords = new ArrayList<String>(Arrays.asList("Console","console","await","break","case","catch","class","const","continue","debugger","default","delete","do","else","enum","export","extends","false","finally","for","function","if","implements","import","in","instanceof","interface","let","new","null","package","private","protected","public","return","super","switch","static","this","throw","try","True","typeof","var","void","while","with","yield"));
			
			highlightGroup(keywords, theme.keywordColor);
			
			//colors strings
			colorFromTo("\"", "\"", theme.stringColor, false, 0, 0);
			colorFromTo("\'", "\'", theme.stringColor, false, 0, 0);
			
			//colors comments
			colorFromTo("//", "\n", theme.commentColor);
			colorFromTo("/\\*", "\\*/", theme.commentColor);
			
			textPane.setCaretPosition(caretPosition); //puts the caret to starting position | otherwise the caret would end up at last color change point as colorString() uses selection for coloring
			textPane.setSelectionStart(selectionStart); textPane.setSelectionEnd(selectionEnd); //puts selection to what it was befor color painting messed it up
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	public String customString(int length) {
		char[] symbols = new char[length];
		Arrays.fill(symbols, '*');
		return new String(symbols);
	}
}
