package tests;


import static org.junit.Assert.*;

import org.junit.Test;

import code.FinchController;
import edu.cmu.ri.createlab.terk.robot.finch.Finch;
import edu.cmu.ri.createlab.terk.robot.finch.FinchInterface;

/**
* @author(s) Mack W,Hector R,Sunny M, Matthew c.
* This is a Finch Controller test which tests the findBrightLight() method.
*
*/

public class BrightLightTest { 

	@Test
	public void testBrightLight() {
		FinchController fc = new FinchController(new Finch());
		int expected = 234;
		int actual = fc.findBrightLight();
		assertTrue("The expected value was:" +expected+ "but instead you recieved" +actual , expected <= actual);
	}

}