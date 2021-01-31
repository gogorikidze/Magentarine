package app;
import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.UIManager;
import javax.swing.undo.UndoManager;

import java.awt.Window.Type;
import java.awt.Color;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import java.awt.Label;
import javax.swing.JTextArea;

public class app {
	private FileManager fileManager = new FileManager();
	private ColorHighlighter highlighter;
	private Theme theme = new Theme();
	private UndoManager undoManager;
	private InputManager inputManager;
	
	private JFrame WindowFrame;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					app window = new app();
					window.WindowFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public app() {
		initialize();
	}
	public void initialize() {
		WindowFrame = new JFrame();
		WindowFrame.setType(Type.POPUP);
		WindowFrame.setFont(UIManager.getFont("Button.font"));
		WindowFrame.setTitle("Magentarine");
		WindowFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
		WindowFrame.setBounds(100, 100, 600, 400);
		WindowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		WindowFrame.getContentPane().setLayout(null);
		WindowFrame.getContentPane().setBackground(new Color(245,245,245));
		
		JTextPane textPane = new JTextPane();
		textPane.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 13));
		textPane.setForeground(theme.fontColor);
		textPane.setBackground(theme.backgroundColor);
		textPane.setBounds(0, 0, WindowFrame.getWidth() - 20, 100);
		textPane.setMargin(new Insets(0,5,0,5));
		
		undoManager = new UndoManager();
		textPane.getDocument().addUndoableEditListener(undoManager);
		
		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setBounds(20, 0, WindowFrame.getWidth() - 20, 100);
		WindowFrame.getContentPane().add(scrollPane);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
		scrollPane.setBackground(theme.backgroundColor);
		scrollPane.getVerticalScrollBar().setOpaque(false);
		scrollPane.setBorder(BorderFactory.createLineBorder(null, 0));
		
		
		JTextArea lineDisplay = new JTextArea();
		lineDisplay.setEditable(false);
		lineDisplay.setMargin(new Insets(0,5,0,5));
		lineDisplay.setFont(new Font("Lucida Sans Unicode", Font.PLAIN, 13));
		lineDisplay.setForeground(theme.fontColor);
		lineDisplay.setBackground(theme.backgroundColor);
		lineDisplay.setText("1");
		lineDisplay.setSize(Math.max(lineDisplay.getPreferredSize().width, 20), lineDisplay.getWidth());
		
		String lineText = "1";
		for(int i = 1; i < 3000; i++) { lineText += "\n" + (i+1);}
		lineDisplay.setText(lineText);
		lineDisplay.setSize(Math.max(20, lineDisplay.getPreferredSize().width), lineDisplay.getHeight());
		
		JScrollPane lineScrollPane = new JScrollPane(lineDisplay);
		lineScrollPane.setBounds(0, 0, 20, 100);
		WindowFrame.getContentPane().add(lineScrollPane);
		lineScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		lineScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		lineScrollPane.setBackground(theme.backgroundColor);
		lineScrollPane.getVerticalScrollBar().setOpaque(false);
		lineScrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, theme.fontColor));
		lineScrollPane.getVerticalScrollBar().setModel(scrollPane.getVerticalScrollBar().getModel());
		
		JTextPane consoleArea = new JTextPane();
		consoleArea.setForeground(theme.backgroundColor);
		consoleArea.setBackground(theme.fontColor);
		consoleArea.setFont(new Font("Lucida Sans Unicode", Font.PLAIN, 11));
		consoleArea.setToolTipText("none");
		consoleArea.setEditable(false);
		consoleArea.setBounds(0, 120, WindowFrame.getWidth(), 150);
		consoleArea.setMargin(new Insets(3,20,0,20));
		
		JScrollPane consoleScrollPane = new JScrollPane(consoleArea);
		consoleScrollPane.setBounds(0, 120, WindowFrame.getWidth(), 150);
		WindowFrame.getContentPane().add(consoleScrollPane);
		consoleScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		consoleScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		consoleScrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
		consoleScrollPane.setBackground(theme.fontColor);
		consoleScrollPane.getVerticalScrollBar().setOpaque(false);
		consoleScrollPane.setBorder(BorderFactory.createLineBorder(null, 0));
		
		
		topMenuSetup(textPane);
		
		highlighter = new ColorHighlighter(textPane);
		Console console = new Console(consoleArea);
		Scripter scripter = new Scripter(console, textPane);
		inputManager = new InputManager(textPane);
		
		Label nashornEngineVersion = new Label("Nashorn Engine");
		nashornEngineVersion.setBounds(0, 120, 584, 12);
		WindowFrame.getContentPane().add(nashornEngineVersion);
		
		nashornEngineVersion.setText("Nashorn Engine (v" + Scripter.engine.getFactory().getEngineVersion() + ") output:");
		
		
		Timer timer = new Timer(300, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				scripter.executeAndOutput(textPane.getText(), theme.errorColor);
				highlighter.highlight(textPane, theme);
				((Timer)evt.getSource()).stop();
			}
		});
		textPane.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				inputManager.input(e.getKeyChar());
				timer.restart();
			}
		});
		
		ComponentListener windowComponentListener = new ComponentListener(){
			public void componentResized(ComponentEvent arg0) {
				int headerHeight = 0;
				int consoleHeight = 150;
				int editorHeight = WindowFrame.getContentPane().getHeight() - consoleHeight - headerHeight - nashornEngineVersion.getHeight();
				
				nashornEngineVersion.setBounds(0, headerHeight+editorHeight, WindowFrame.getWidth(), nashornEngineVersion.getHeight());
				lineScrollPane.setBounds(0, headerHeight, lineDisplay.getWidth(), editorHeight);
				lineDisplay.setSize(Math.max(lineDisplay.getPreferredSize().width, 20), lineDisplay.getWidth());
				scrollPane.setBounds(lineScrollPane.getWidth(), headerHeight, WindowFrame.getWidth()-15 - lineScrollPane.getWidth(), editorHeight);
				textPane.setSize(WindowFrame.getWidth()-15 - lineScrollPane.getWidth(), editorHeight);
				consoleScrollPane.setBounds(0, headerHeight + editorHeight+ nashornEngineVersion.getHeight(), WindowFrame.getWidth()-15, consoleHeight);
		    }@Override public void componentHidden(ComponentEvent e) {}@Override public void componentMoved(ComponentEvent e) {}@Override public void componentShown(ComponentEvent e) {}
		};
		WindowFrame.addComponentListener(windowComponentListener);
	}
	void topMenuSetup(JTextPane textPane){
		JMenuBar menuBar = new JMenuBar();
		WindowFrame.setJMenuBar(menuBar);
		
		//File menu
		JMenu fileMenu = new JMenu("File");
		fileMenu.setBounds(294, 0, 67, 22);
		menuBar.add(fileMenu);
		
		JMenuItem saveItem = new JMenuItem("Save");
		saveItem.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				fileManager.save(textPane);
				highlighter.highlight(textPane, theme);
			}
		});
		fileMenu.add(saveItem);
		
		JMenuItem openItem = new JMenuItem("Open");
		openItem.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				fileManager.open(textPane);
				highlighter.highlight(textPane, theme);
			}
		});
		fileMenu.add(openItem);
		
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				WindowFrame.dispose();
			}
		});
		fileMenu.add(exitItem);
		
		//Edit menu
		JMenu editMenu = new JMenu("Edit");
		editMenu.setBounds(20, 0, 67, 22);
		menuBar.add(editMenu);
		
		JMenuItem undoItem = new JMenuItem("Undo");
		undoItem.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				undoManager.undo();
			}
		});
		editMenu.add(undoItem);
		
		JMenuItem redoItem = new JMenuItem("Redo");
		redoItem.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				undoManager.redo();
			}
		});
		editMenu.add(redoItem);
		
		JMenuItem copyItem = new JMenuItem("Copy");
		copyItem.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				textPane.copy();
			}
		});
		editMenu.add(copyItem);
		
		JMenuItem pasteItem = new JMenuItem("Paste");
		pasteItem.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				textPane.paste();
			}
		});
		editMenu.add(pasteItem);
		
		JMenuItem cutItem = new JMenuItem("Cut");
		cutItem.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				textPane.cut();
			}
		});
		editMenu.add(cutItem);
		
		JMenuItem selectAllItem = new JMenuItem("Select All");
		selectAllItem.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				textPane.selectAll();
			}
		});
		editMenu.add(selectAllItem);
	}
}