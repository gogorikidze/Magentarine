package app;
import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.UIManager;
import java.awt.Window.Type;
import java.awt.Color;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

public class app {
	FileManager fileManager = new FileManager();
    ColorHighlighter highlighter;
    Console console;
    Theme theme = new Theme();
	
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
	private void initialize() {
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
		textPane.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 14));
		textPane.setForeground(theme.fontColor);
		textPane.setBackground(theme.backgroundColor);
		textPane.setBounds(0, 20, WindowFrame.getWidth(), 100);
		textPane.setMargin(new Insets(0,5,0,5));
		
		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setBounds(0, 20, WindowFrame.getWidth(), 100);
		WindowFrame.getContentPane().add(scrollPane);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
		scrollPane.setBackground(theme.backgroundColor);
		scrollPane.getVerticalScrollBar().setOpaque(false);
		scrollPane.setBorder(BorderFactory.createLineBorder(null, 0));
		
		JTextPane consoleArea = new JTextPane();
		consoleArea.setForeground(theme.backgroundColor);
		consoleArea.setBackground(theme.fontColor);
		consoleArea.setFont(new Font("Lucida Sans Unicode", Font.PLAIN, 13));
		consoleArea.setToolTipText("none");
		consoleArea.setEditable(false);
		consoleArea.setBounds(0, 120, WindowFrame.getWidth(), 150);
		consoleArea.setMargin(new Insets(3,20,0,20));
		//consoleArea.getDocument().addDocumentListener(new LimitLinesDocumentListener(6));
		
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
		
		textPane.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				scripter.executeAndOutput(textPane.getText(), theme.errorColor);
				highlighter.highlight(textPane, theme);
			}
		});
		
		WindowFrame.addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent arg0) {
				int headerHeight = 0;
				int consoleHeight = 150;
				int editorHeight = WindowFrame.getContentPane().getHeight() - consoleHeight - headerHeight;
				
				scrollPane.setBounds(0, headerHeight, WindowFrame.getWidth()-15, editorHeight);
				consoleScrollPane.setBounds(0, headerHeight + editorHeight, WindowFrame.getWidth()-15, consoleHeight);
				
				consoleScrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
				scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
	        }
			@Override public void componentHidden(ComponentEvent e) {}
			@Override public void componentMoved(ComponentEvent e) {}
			@Override public void componentShown(ComponentEvent e) {}
		});
	}
	void topMenuSetup(JTextPane textPane){
		JMenu fileMenu = new JMenu("File");
		fileMenu.setBounds(294, 0, 67, 22);
		
		JMenuItem saveItem = new JMenuItem("Save");
		saveItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileManager.save(textPane);
				highlighter.highlight(textPane, theme);
			}
		});
		fileMenu.add(saveItem);
		
		JMenuItem openItem = new JMenuItem("Open");
		openItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileManager.open(textPane);
				highlighter.highlight(textPane, theme);
			}
		});
		fileMenu.add(openItem);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		WindowFrame.setJMenuBar(menuBar);
	}
}