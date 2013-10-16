	package code;
	
	import java.awt.Dimension;
	import javax.swing.JFrame;
	import javax.swing.JMenu;
	import javax.swing.JMenuBar;

	public class GUI {
	private JFrame _frame;
	private GUIPanel _panel;
	
	/**
	 * The GUI for the program is the on screen tracker for the
	 * Finch robot.
	 *
	 * A frame is made on your screen, and a menu bar
	 * is made with a menu for the game.
	 */
	public GUI() {
		
	// CREATES AND ORGANIZE THE WINDOW
	   _frame = new JFrame("FINCH TRACKER");
	   _frame.setPreferredSize(new Dimension(700,500));
	   _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   JMenuBar menuBar = new JMenuBar();
	   _frame.setJMenuBar(menuBar);
	   JMenu game = new JMenu("Game");
	   menuBar.add(game);

	   _frame.setBackground(java.awt.Color.WHITE);
	   GUIPanel panel = new GUIPanel();
	   _panel = panel;
	   _frame.pack();
	   _frame.add(panel);
	   _frame.setVisible(true);
	}
	
	public void drawSide(long l){
		_panel.paintWall(_panel.getGraphics(), l);
		
	}
	
	
}

