package app;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileManager {
	public FileManager() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){
			
		}
	}
	public void open(JTextPane editorPane){
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("JavaScript Files", "js");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(chooser);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	if(getExtension(chooser.getSelectedFile().toString()).contentEquals("js")) {
	    		try {
	    			String file = "";
	    			FileInputStream fis = new FileInputStream(chooser.getSelectedFile().toString());
	    		    InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
	    		    BufferedReader reader = new BufferedReader(isr);
	    		    
	    			int r=0;
	    			while((r=reader.read())!=-1){  
	    				file += (char)r;
	    			}
	    			System.out.print(file);
	    			editorPane.setText(file);
	    		}catch(Exception e) {
	    			System.out.print("Error wih reading"+e);
	    		}
	    	}else {
	    		System.out.print("Wrong file extension: "+getExtension(chooser.getSelectedFile().toString()));
	    	}
	    }
	}
	public void save(JTextPane editorPane){
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("JavaScript Files", "js");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showSaveDialog(chooser);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	if(getExtension(chooser.getSelectedFile().toString()).contentEquals("js")) {
	    		try {
	    			FileWriter myWriter = new FileWriter(chooser.getSelectedFile().toString());
	    		      myWriter.write(editorPane.getText());
	    		      myWriter.flush();
	    		      myWriter.close();
	    		      System.out.println("Successfully wrote to the file.");
	    		}catch(Exception e) {
	    			System.out.print("Error wih reading"+e);
	    		}
	    	}else {
	    		System.out.print("Wrong file extension: "+getExtension(chooser.getSelectedFile().toString()));
	    	}
	    }
	}
	public String getExtension(String filename) {
	    int index = filename.lastIndexOf('.');
	    if(index > 0) {
	        String extension = filename.substring(index + 1);
	        return  extension;
	    }
	    return  "none";
	}
}
