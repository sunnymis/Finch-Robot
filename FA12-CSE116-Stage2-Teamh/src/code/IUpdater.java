package code;

import java.awt.Container;

import javax.swing.JLabel;

public abstract class IUpdater {

	private JLabel _label;
	private String _baseString;
	
	public IUpdater(JLabel l, String s, Container c) {
		_label = l;
		_baseString = s;
		c.add(l);
	}
	
	public final void update() {
		_label.setText(_baseString + readSensor());
	}

	protected abstract String readSensor();
	
}
