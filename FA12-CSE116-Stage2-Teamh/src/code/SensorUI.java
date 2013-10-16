package code;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

public class SensorUI {
	
	public static void main(String[] args) {
		final Finch bird = new Finch();
		ArrayList<IUpdater> list = new ArrayList<IUpdater>();
		JFrame w = new JFrame("Finch sensors");
		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(200,200));
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		w.add(p);

		list.add(new IUpdater(new JLabel(), "Light (L): ", p) {
			@Override public String readSensor() {
				return "" + bird.getLeftLightSensor();
			}
		});
		
		list.add(new IUpdater(new JLabel(), "Light (R): ", p) {
			@Override public String readSensor() {
				return "" + bird.getRightLightSensor();
			}
		});
		
		list.add(new IUpdater(new JLabel(), "Temp: ", p) {
			@Override public String readSensor() {
				return "" + bird.getTemperature();
			}
		});
		
		list.add(new IUpdater(new JLabel(), "Acceleration (X): ", p) {
			@Override public String readSensor() {
				return "" + bird.getXAcceleration();
			}
		});
		
		list.add(new IUpdater(new JLabel(), "Acceleration (Y): ", p) {
			@Override public String readSensor() {
				return "" + bird.getYAcceleration();
			}
		});
		
		list.add(new IUpdater(new JLabel(), "Acceleration (Z): ", p) {
			@Override public String readSensor() {
				return "" + bird.getZAcceleration();
			}
		});
		
		list.add(new IUpdater(new JLabel(), "Obstacle (L): ", p) {
			@Override public String readSensor() {
				return "" + bird.getObstacleSensors()[0];
			}
		});
		
		list.add(new IUpdater(new JLabel(), "Obstacle (R): ", p) {
			@Override public String readSensor() {
				return "" + bird.getObstacleSensors()[1];
			}
		});
		
		list.add(new IUpdater(new JLabel(), "Upside down: ", p) {
			@Override public String readSensor() {
				return "" + bird.isFinchUpsideDown();
			}
		});
		
		list.add(new IUpdater(new JLabel(), "Shaken: ", p) {
			@Override public String readSensor() {
				return "" + bird.isShaken();
			}
		});
		
		w.setVisible(true);
		w.pack();
		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		while (true) {
			for (IUpdater i : list) {
				i.update();
			}
		}
	}
	
}
