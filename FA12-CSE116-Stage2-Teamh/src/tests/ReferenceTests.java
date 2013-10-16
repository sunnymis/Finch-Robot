package tests;

/**
 * The reference test class contains the suite of the JUnit tests, whose primary 
 * goal is to see whether or not the Finches finish their actions or not in each
 * of the four methods within the class.    
 */

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

public class ReferenceTests {
	
	private code.FinchController _testFinch;

	@Before
	public void initializeFinch() {
		Finch bird = new Finch();
		_testFinch = new code.FinchController(bird);
	}
	
//	@Test
//	public void testMovePerimeter() {
//		boolean expected = true;
//		boolean actual = _testFinch.movePerimeter();
//		assertTrue("I expected the boolean result to be returned to be true, but the result returned was instead false, implying that the perimeter movement was not fully completed or it was interrupted.",expected == actual);	
//	}
	
//	@Test
//	public void testSearchBright(){
//		boolean expected = true;
//		boolean actual = _testFinch.searchBright();
//		assertTrue("I expected the boolean result to be returned to be true, but the result returned was instead false, implying that the search for the brightest light was not fully completed or it was interrupted.",expected == actual);	
//	}
	
	@Test
	public void testSearchHot(){
		boolean expected = true;
		boolean actual = _testFinch.searchHot();
		assertTrue("I expected the boolean result to be returned to be true, but the result returned was instead false, implying that the search for the highest temperature was not fully completed or it was interrupted.",expected == actual);	
	}
	
	@Test
	public void testRememberPath(){
		boolean expected = true;
		boolean actual = _testFinch.rememberPath();
		assertTrue("I expected the boolean result to be returned to be true, but the result returned was instead false, implying that the I/O process was not fully completed or it was interrupted.",expected == actual);	
	}
	
	@Test
	public void testReadPath(){
		boolean expected = true;
		boolean actual = _testFinch.readPath();
		assertTrue("I expected the boolean result to be returned to be true, but the result returned was instead false, implying that the I/O process was not fully completed or it was interrupted.",expected == actual);	
	}
}
