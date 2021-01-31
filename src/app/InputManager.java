package app;

import java.util.Arrays;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;

public class InputManager {
	JTextPane textPane;
	
	public InputManager(JTextPane textPaneToSet) {
		textPane = textPaneToSet;
	}
	void input(char key) {
		switch(key) {
		case '\"':
			doubleUp('\"');
			break;
		case '\'':
			doubleUp('\'');
			break;
		case '{':
			doubleUp('}');
			break;
		case '(':
			doubleUp(')');
			break;
		case '\n':
			try { newLine(); } catch (BadLocationException e) { e.printStackTrace(); }
			break;
		}
	}
	void newLine() throws BadLocationException {
		int pointer = textPane.getCaretPosition();
		String text = textPane.getDocument().getText(0, textPane.getDocument().getLength());
		
		//preserves the tab when moving to new line
		String[] lines = text.substring(0, pointer).split("\n");
		int tabs = lines[lines.length-1].split("\t").length - 1;
		textPane.setText(text.substring(0, pointer)+customString(tabs, '\t')+text.substring(pointer, text.length()));
		if(tabs > 0) {
			textPane.setCaretPosition(pointer + tabs);
		}else {
			textPane.setCaretPosition(pointer);
		}
	}
	void doubleUp(char key) {
		int pointer = textPane.getCaretPosition();
		String text = textPane.getText();
		textPane.setText(text.substring(0, pointer)+key+text.substring(pointer, text.length()));
		textPane.setCaretPosition(pointer);
	}
	public String customString(int length, char str) {
		if(length <= 0) {
			return "";
		}else {
			char[] symbols = new char[length];
			Arrays.fill(symbols, str);
			return new String(symbols);
		}
	}
}
