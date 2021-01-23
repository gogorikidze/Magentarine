package app;

import java.awt.Color;

import javax.swing.JTextPane;

public class Console {
	JTextPane pane;
	ColorHighlighter highlighter;
	
	public Console(JTextPane consolePane) {
		pane = consolePane;
		highlighter = new ColorHighlighter(pane);
	}
	public void displayLine(String line, Color color) {
		pane.setText(line);
		highlighter.colorString(0, pane.getText().length(), highlighter.colorToAS(color));
	}
	public void displayLine(String line) {
		pane.setText(line);
	}
}