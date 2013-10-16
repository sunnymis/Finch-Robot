package code;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

/**
 * This class is where the different method calls on the 
 * Finch take place to make sure the Finch is performing its
 * various tasks.
 *
 */
public class Testing {

	public static void main(String[] args){
		
		FinchController fc = new FinchController(new Finch());
		
		fc.addTerminateButton();
		// TEST DIFFERENT METHODS BENEATH THIS LINE
		fc.drawPerimeter();

	}
}
