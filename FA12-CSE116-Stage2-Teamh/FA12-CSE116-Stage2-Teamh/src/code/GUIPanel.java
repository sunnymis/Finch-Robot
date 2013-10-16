package code;

import java.awt.Color;

import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * The main panel within the frame, that will hold the finch  
 * and wall representations in the game.
 *
 */
public class GUIPanel extends JPanel {
	
	private int _direction;
	private int _xValue;
	private int _yValue;
	int[] _lineValues = new int[16];
	
	public GUIPanel(){
		_direction = 0;
		_xValue = 0;
		_yValue = 0;
		for (int i = 0; i < 15; i = i + 1){
			_lineValues[i] = 0;
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		paintFinch(g);

	}	
	
	public void paintFinch(Graphics g){
		super.paintComponent(g);
		
		// THIS IS SETTING THE COLOR FO OUR "PAINTBRUSH"
		g.setColor(Color.RED);
		
		// THIS IS PAINTING A RECTANGEL OBJECT
		//g.fillRect(60,370,10,10);
		// USE VARAIBLES AS POSITION AND USE TIMER TO CHANGE THE POSITION
	}
	
	public void paintWall(Graphics g, long l){
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		switch (_direction){
		case 0: 
			g.fillRect(60, 400, (int) l/50, 10);
			_lineValues[0] = 60;
			_lineValues[1] = 400;
			_lineValues[2] = (int) l/50;
			_lineValues[3] = 10;
			
			_xValue = (int) l/50;
			_direction ++;
			break;
		case 1:
			g.fillRect(_lineValues[0], _lineValues[1], _lineValues[2], _lineValues[3]);
			g.fillRect(50 + _xValue, 400 - (int) l/50, 10, (int) l/50);
			_lineValues[4] = 50 + _xValue;
			_lineValues[5] = 400 - (int) l/50;
			_lineValues[6] = 10;
			_lineValues[7] = (int) l/50;
			_direction ++;
			_yValue = 400 - (int) l/50;
			break;
		case 2:
			g.fillRect(_lineValues[0], _lineValues[1], _lineValues[2], _lineValues[3]);
			g.fillRect(_lineValues[4], _lineValues[5], _lineValues[6], _lineValues[7]);
			g.fillRect(_lineValues[4]-(int) l/50, _yValue, (int) l/50, 10);
			_lineValues[8] = _lineValues[4]-(int) l/50;
			_lineValues[9] = _yValue;
			_lineValues[10] = (int) l/50;
			_lineValues[11] = 10;
			_xValue = (int) l/50;
			_direction ++;
			break;
		case 3:
			g.fillRect(_lineValues[0], _lineValues[1], _lineValues[2], _lineValues[3]);
			g.fillRect(_lineValues[4], _lineValues[5], _lineValues[6], _lineValues[7]);
			g.fillRect(_lineValues[8], _lineValues[9], _lineValues[10], _lineValues[11]);
			g.fillRect(_lineValues[8], _yValue, 10, (int) l/50);
			_direction ++;
			break;
		}
;
	}
	
	public Graphics getGraphics(){
		return super.getGraphics();
	}
	
	
}