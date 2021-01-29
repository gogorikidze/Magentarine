package app;

import java.awt.Color;
import java.io.StringWriter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.JTextPane;

public class Scripter {
	static ScriptEngine engine = new ScriptEngineManager().getEngineByName("Nashorn");
	private String prerequisites = "console = {log: print,warn: print, error: print};Console = {log: print,warn: print, error: print}; startTime = new Date().getTime();"; //adds console functional to engine
	private String postrequisites = "\n;endTime = new Date().getTime(); console.log('Execution time: '+(endTime-startTime)+' ms');";
	private Console console;
	JTextPane textPane;
	StringWriter stringWriter = new StringWriter();
	
	public Scripter(Console consoleToSet, JTextPane textPaneToSet) {
		console = consoleToSet;
		textPane = textPaneToSet;
	}
	public void executeAndOutput(String code, Color errorColor){ //executes code and outputs to console
		stringWriter.getBuffer().setLength(0);
		engine.getContext().setWriter(stringWriter);
		try {
			engine.eval(prerequisites+code+postrequisites);
			console.displayLine("\n"+stringWriter, Color.white);
		}catch (Exception e){
			console.displayLine("\n"+e.toString().replaceFirst("javax.script.ScriptException: ", ""), errorColor);//removes repetitive java error string
		}
	}
}