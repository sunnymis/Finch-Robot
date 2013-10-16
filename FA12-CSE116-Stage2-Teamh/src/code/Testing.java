package code;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.Timer;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;
import finchcam.client.FinchCamService;
import finchcam.shared.FinchInfo;
import finchcam.shared.FinchTracker;

public class Testing {

	public static void main(String[] args){
		
		// CREATES A NEW FINCHCONTOLLER OBJECT, PASSING IN REFERENCES OF UP TO 4 FINCHES (NULL OTHERWISE)
		
		
		FinchController fc = new FinchController(new Finch(), null, null, null);
		fc.addTerminateButton();
		
		
		// TESTS METHODS FROM HERE
		fc.traverseMaze();
	}
}
