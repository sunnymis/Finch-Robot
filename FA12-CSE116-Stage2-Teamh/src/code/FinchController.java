package code;

/**
 *  The following class is a full consolidation of the first four objectives of the  
 *  Finch project.  
 *  
 *  Embedded within are the respective methods for a Finch robot 
 *  to move around an enclosed perimeter composed of four sides, scout around the
 *  Finch's enclosure for the highest temperature and brightest light, and register
 *  wheel velocities into a text file and read for wheel velocities from a text file.
 * 
 *  The Stage1Finch class is associated with the FinchInterface and requires an object 
 *  of type FinchInterface to be passed in.   
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;
import edu.cmu.ri.createlab.terk.robot.finch.FinchInterface;
import finchcam.client.FinchCamService;
import finchcam.shared.FinchInfo;
import finchcam.shared.FinchTracker;

public class FinchController {
   
	// THE FINCH INTERFACES THAT WILL BE PASSED INTO THE CONSTRUCTOR, CREATING AN ASSOCIATION RELATIONSHIP BEWTEEN THE FINCH AND CONTROLLER
    private FinchInterface _finch1;
    private FinchInterface _finch2;
    private FinchInterface _finch3;
    private FinchInterface _finch4;
    
    // THE CAMERA OBJECT THAT WILL GIVE US SOME FINCH INFO, IN A COMPOSITION RELATIONSHIP WITH THE FINCHCONTROLLER
    private FinchCamService _cam;
    
	// MAX AND MIN VALUES OF THE WEBCAM COORDINATES
    private int _xMin;
    private int _xMax;
    private int _yMin;
    private int _yMax;
    
    private char _colorOfMainFinch;
   
    /**
     * The constructor's role is to assign the FinchInterface passed into 
     * the constructor to the private variable, _f.
     * @param f - the FinchInterface that is passed into the constructor of the class.
     */
    public FinchController(FinchInterface f1, FinchInterface f2, FinchInterface f3, FinchInterface f4){
    	
    	// CREATES INSTANCE VARIABLES FOR EACH OF THE FINCHES. HOWEVER, WE ONLY DEAL WITH FINCH 1 FOR THE MAJORITY OF THE CODE
    	_finch1 = f1;
    	_finch2 = f2;
    	_finch3 = f3;
    	_finch4 = f4;
    	
    	_cam = new FinchCamService(FinchTracker.HOST);
    	_colorOfMainFinch = 'b';
    	// FURTHER FROM THE DOOR
        _xMin = 18;
        // CLOSER TO THE DOOR
        _xMax = 315;
        // CLOSER TO THE BOARD
        _yMin = 70;
        // FURTHER FROM THE BOARD
        _yMax = 220;
        
    	_leaderXCoordinateHistory = new LinkedList<Integer>();
    	_leaderYCoordinateHistory = new LinkedList<Integer>();
    	
		_finchAngles = new int[3];
    }
    
	/**
	 * This method simply pauses the program for a specified time, while allowing the finch to continue 
	 * functioning
	 * @param time - The time in milliseconds that the program will wait
	 */
	public void doFor(int time){
		_finch1.sleep(time);
	}
	
	/**
	 * This methods adds a terminate button, which allows us to terminate our program that we're currently 
	 * running in a quick an efficient manner
	 */
	public void addTerminateButton(){
		
		// CREATES A NEW JFRAME, INTO WHICH WE WILL PUT THE JBUTTON
		JFrame j = new JFrame();
		// CREATES THE JBUTTON
		JButton b = new JButton("Program Stop");
		
		// ADDS AN ANONYMOUS ACTIONLISTENER TO THE TERMINATE BUTTON, THAT STOPS THE FINCH WHEELS AND
		// QUITS CONNECTION WITH THE FINCH
		b.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				_finch1.stopWheels();
				_finch1.quit();
				System.exit(0);
			}
		});
		// ADDS BUTTON TO JFRAME AND MAKES THE JFRAME VISIBLE
		j.add(b);
		j.setVisible(true);
		j.pack();
	}
	
	/**
	 * This method gets the X-Coordinate of the body of the finch
	 * @param either a 'c', 'g', 'p', or 'r', the blue, green, pink, and red finches respectively
	 * @return an integer X-Coordinate of the body of the finch
	 */
	public int getBodyXCoordinate(char c){
		HashMap<String,FinchInfo> map = _cam.objects();
		// WE INITIALIZE THE VALUE TO 1 TO BEGIN WITH, DEFAULT IS THE LEAD FINCH
		FinchInfo i = map.get("blue");
		switch(c){
		// WE SWITCH AROUND BLUE GREEN PINK AND RED
		case 'b': 	i = map.get("blue"); break;
		case 'g':	i = map.get("green"); break;
		case 'p':	i = map.get("pink"); break;
		case 'r':	i = map.get("red"); break;
		}
		return i._beakLocation.x;
	}
	
	/**
	 * This method gets the Y-Coordinate of the body of the finch
	 * @param either a 'c', 'g', 'p', or 'r', the blue, green, pink, and red finches respectively
	 * @return an integer Y-Coordinate of the body of the finch
	 */
	public int getBodyYCoordinate(char c){
		HashMap<String,FinchInfo> map = _cam.objects();
		// WE INITIALIZE THE VALUE TO 1 TO BEGIN WITH, DEFAULT IS THE LEAD FINCH
		FinchInfo i = map.get("blue");
		switch(c){
		// WE SWITCH AROUND BLUE GREEN PINK AND RED
		case 'b': 	i = map.get("blue"); break;
		case 'g':	i = map.get("green"); break;
		case 'p':	i = map.get("pink"); break;
		case 'r':	i = map.get("red"); break;
		}
		return i._beakLocation.y;
	}
	
	/**
	 * This method gets the x position of the ball
	 * @return - an integer representing the x coordinate of the ball
	 */
	public int getBallXCoordinate(){
		HashMap<String,FinchInfo> map = _cam.objects();
		// WE INITIALIZE THE VALUE TO 1 TO BEGIN WITH, DEFAULT IS THE LEAD FINCH
		FinchInfo i = map.get("ball");
		return i._beakLocation.x;
	}
	
	/**
	 * This method gets the y position of the ball
	 * @return - an integer representing the y coordinate of the ball
	 */
	public int getBallYCoordinate(){
		HashMap<String,FinchInfo> map = _cam.objects();
		// WE INITIALIZE THE VALUE TO 1 TO BEGIN WITH, DEFAULT IS THE LEAD FINCH
		FinchInfo i = map.get("ball");
		return i._beakLocation.y;
	}
	
	/**
	 * This method turns the finch a certain number of degrees, about the point between both wheels
	 * @param degrees - The number of degrees that the finch will turn, where left turn is positive
	 */
	private void turn(int degrees, FinchInterface finch){

		
		// ENSURES THAT THE WHEELS ARE STOPPED PRIOR TO TURNING
		finch.stopWheels();
		
		// .3 SECOND BUFFER
		this.doFor(0);
		
		// THIS ROTATES THE FINCH, BASED ON THE DEGREES ARGUMENT
		if (degrees >= 0){
			int amount = 9 * degrees;
			finch.setWheelVelocities(-109, 102, amount);
		}
		else{
			int amount = -9 * degrees;
			finch.setWheelVelocities(102, -103, amount);
		}
		
		finch.stopWheels();
		this.doFor(10);
	}
	
	/**
	 * This is a private method that simply moves the finch forward for a certain amount of time
	 * @param time - how long the finch will move forwards for
	 */
	private void moveForwards(int time){
		_finch1.setWheelVelocities(100, 100, time);
	}
	
	/**
	 * This is a private method that simply moves the finch backward for a certain amount of time
	 * @param time - how long the finch will move backwards for
	 */
	private void moveBackWards(int time){
		_finch1.setWheelVelocities(-100, -100, time);
	}

	/**
	 * This is the method that causes the finch to traverse the perimeter 
	 * @return integer which tracks the number of corners encountered
	 */
	public int traversePerimeter(){
		
		// COUNTS THE CORNERS THAT IT HAS TURNED
		 int corners = 0;
		 while (corners < 4){
			 int temp = this.perimeterHelperMethod();
			 corners = corners + temp;
		 }
		 
		 // THESE FINAL MOTIONS CAUSE THE FINCH TO ORIENTATE ITSELF AND STOP
		 _finch1.setWheelVelocities(-100, 100);
		 this.doFor(600);
		 _finch1.stopWheels();
		 _finch1.setLED(200,0,0);
		return corners;
	}
	/**
	 * This is a helper method for the traversePerimeter method.
	 * @return - An int with value 0 if the finch senses a parallel wall, value 1 if it senses a corner
	 */
	private int perimeterHelperMethod(){
		
		int corner = 0;
		
		// PAUSE FOR .5 SECONDS BEOFORE PROGRAM EXECUTION
		this.doFor(500);
		
		_finch1.setLED(0, 200, 0);
		_finch1.setWheelVelocities(150, 120);
		
		// MOVE FOR AT LEAST .5 SECONDS BEFORE DETECTING A WALL
		this.doFor(500);
		
		// WHILE THERE IS NOT AN OBSTACLE (CHECKS EVERY .1 SECONDS)
		while (!_finch1.isObstacle()){
			this.doFor(100);
		}
		
		// AFTER IT SENSES AN OBSTALCLE, THIS DELAY ACTS AS A BUFFER SO THE ROBOT CAN CHECK TO SEE IF 
		// IT BOTH SENSORS DETACT AN OBSTACLE
		this.doFor(300);
		
		// IF IT HITS A CORNER, DO THIS
		if (_finch1.isObstacleLeftSide()){
			
			// CHANGE COLOR OF BEAK, STOP, AND WAIT FOR .4 SECONDS
			_finch1.stopWheels();
			_finch1.setLED(0, 0, 200);
			this.doFor(400);
			
			// BACK UP A LITTLE BIT
			_finch1.setWheelVelocities(-100, -150);
			this.doFor(650);
			
			// PERFORM THE TURN
			_finch1.setWheelVelocities(-100, 150);
			this.doFor(400);
			corner = corner + 1;
		}
		
		// IF IT'S NOT AT A CORNER, BUT RATHER ALONG A WALL
		else{
			
			// BACK UP A LITTLE BIT
			_finch1.setWheelVelocities(-150, -130);
			this.doFor(400);
			
			// TURN AWAY FROM THE WALL
			_finch1.setWheelVelocities(-90, 90);
			this.doFor(280);
		}
		
		// RETURNS AN INT VALUE, 0 IF IT HIT A WALL, 1 IF IT HIT A CORNER
		return corner;
	}
    
	/**
	 * This method commands the finch to move underneath the brightest spot in the birdcage
	 * @return integer of the value of the finch's light sensors when the program is completed
	 */
	public int findBrightLight(){
		
		// SETS THE BEAK COLOR TO GREEN
		_finch1.setLED(0, 200, 0);
		
		// THIS IS THE VLAUE THAT THE LIGHT SENSOR NEEDS TO REACH IN ORDER TO STOP THE FINCH
		int maxLightSensorValue = 234;
		
		 // WHILE WE'RE NOT UNDER THE BRIGHT LIGHT
		while (_finch1.getLeftLightSensor() < maxLightSensorValue || _finch1.getRightLightSensor() < maxLightSensorValue){
		
			// IF THERE'S AN OBSTACLE, PERFORM A CERTAIN ACTION (BACK AWAY). THIS IS A PRIVATE HELPER METHOD CALL
			if (_finch1.isObstacle()){
				_finch1.setWheelVelocities(-150,-150);
				this.doFor(400);
			}
			
			// ELSE, MOVE TOWARDS THE LIGHT
			else{
				this.findBrightLightOpenSpace();
			}
		}
		
		// MOVES THE FINCH FORWARD TO CENTER IT UNDER THE LIGHT AT THE END
		_finch1.setWheelVelocities(100, 106);
		this.doFor(350);
		_finch1.stopWheels();
		
		// ONCE THE FINCH HAS FOUND THE BRIGHT LIGHT, SET THE COLOR OF THE BEAK TO BLUE
		_finch1.setLED(0, 0, 200);
		return maxLightSensorValue;
	}
	/**
	 * This helper method moves the finch closer to the light source
	 */
	private void findBrightLightOpenSpace(){

		
		// THIS IS THE CODE FOR GETTING THE FINCH TO FACE THE BRIGHT LIGHT
		if (_finch1.getLeftLightSensor() < _finch1.getRightLightSensor()){
			while (_finch1.getLeftLightSensor() < _finch1.getRightLightSensor()){
				this.turn(-5, _finch1);	
			}
		}
		else {
			while (_finch1.getLeftLightSensor() > _finch1.getRightLightSensor()){
				this.turn(5, _finch1);
			}
		}
		
		// MOVES THE ROBOT FORWARD A LITTLE BIT ONCE IT'S DONE TURNING SO THAT IT DOESN'T GET STUCK MOVING BACK AND FORTH, THESE VALUES REFLECT THE FACT THAT THE WHEEL VELOCITIES ARE DIFFERENT
		_finch1.setWheelVelocities(100, 106);
		this.doFor(250);
		
		// INITIALIZES THE INTEGERS WHICH KEEP TRACK OF THE PREVIOUS LIGHT VALUES
		int leftLightSensorValue = _finch1.getLeftLightSensor();
		int rightLightSensorValue = _finch1.getRightLightSensor();
		
		// WHILE THE NEW VALUES ARE GREATER THAN THE PREVIOUS ONES, MOVE FORWARD
		while ((_finch1.getLeftLightSensor() > leftLightSensorValue) && (_finch1.getRightLightSensor() > rightLightSensorValue)){
			
			// IF THERE'S AN OBSTACLE, STOP MOVING FORWARD
			if (_finch1.isObstacle()){
				break;
			}
			
			// MOVE THE FINCH FORWARD, THESE VALUES REFLECT THE FACT THAT THE WHEEL VELOCITIES ARE DIFFERENT
			_finch1.setWheelVelocities(100,105);
			this.doFor(300);
			
			// UPDATES THE VALUES OF THE LIGHT SENSORS
			leftLightSensorValue = _finch1.getLeftLightSensor();
			rightLightSensorValue = _finch1.getRightLightSensor();	
		}
		
		_finch1.stopWheels();
	}
	
	/**
	 * This method causes the finch to traverse the perimeter and draw the birdcage on the screen, in real time
	 */
	public void drawPerimeter(){
		
		// CREATES A GUI WITHIN THE METHOD SO THAT THE GUI HAS ACCESS TO THE FIELDS AND VALUES OF THE METHOD
		GUI g = new GUI();
		
			// COUNTS THE CORNERS THAT IT HAS TURNED
			 int corners = 0;
			 long startTime = System.currentTimeMillis();
			// INITIALIZE THE ENDTIME VALUE
			 long endTime = 0;
			 
			 // THIS IS THE LIST THAT WILL STORE THE TIME VALUES CORRESPONDING TO THE AMOUNT OF TIME THAT IT TOOK TO TRAVERSE EACH SIDE
			 // THIS CODE ADDS VALUES TO THE LINKEDLIST SO THAT WE DON'T GET NULL POOINTER EXCEPTIONS
			 LinkedList<Long> timeValues = new LinkedList<Long>();
			 for (int i = 0; i < 4; i = i + 1){
				 timeValues.add(i, (long) 0);
			 }

			 while (corners < 4){

				 // THIS METHOD CAUSES THE FINCH TO MOVE ALONG THE WALL, IN ARC PATTERNS. EVERY TIME IT REACHES AN OBSTACLE, THE LOOP ITERATES
				 int temp = this.drawPerimeterHelperMethod();
				 
				 // IF WE HAVE REACHED A CORNER
				 if (temp == 1){
					 
					 // TAKE THE VALUE OF THE TIME
					 endTime = System.currentTimeMillis();
					
					 // THE AMOUNT OF TIME THAT IT TOOK TO TRAVERSE THE INDIVIDUAL SIDES
					 timeValues.set(corners, endTime - startTime);
					 g.drawSide(endTime - startTime);
					 // RESETS THE START TIME VALUE
					 startTime = System.currentTimeMillis();
				 }
				
				 // INCREMENT THE VALUE OF CORNERS
				 corners = corners + temp;
			 }
			 
			 // THESE FINAL MOTIONS CAUSE THE FINCH TO ORIENTATE ITSELF AND STOP
			 _finch1.setWheelVelocities(-100, 100);
			 this.doFor(600);
			 _finch1.stopWheels();
			 _finch1.setLED(200,0,0);
		}
	/**
	 * This is a helper method for the traversePerimeter method.
	 * @return - An int with value 0 if the finch senses a parallel wall, value 1 if it senses a corner
	 */
	private int drawPerimeterHelperMethod(){
			
		int corner = 0;
			
		// PAUSE FOR .5 SECONDS BEOFORE PROGRAM EXECUTION
		this.doFor(500);
			
		_finch1.setLED(0, 200, 0);
		_finch1.setWheelVelocities(150, 120);
			
		// MOVE FOR AT LEAST .5 SECONDS BEFORE DETECTING A WALL
		this.doFor(500);
		
		// WHILE THERE IS NOT AN OBSTACLE (CHECKS EVERY .1 SECONDS)
		while (!_finch1.isObstacle()){
			this.doFor(100);
		}
			
		// AFTER IT SENSES AN OBSTALCLE, THIS DELAY ACTS AS A BUFFER SO THE ROBOT CAN CHECK TO SEE IF 
		// IT BOTH SENSORS DETACT AN OBSTACLE
		this.doFor(300);
			
		// IF IT HITS A CORNER, DO THIS
		if (_finch1.isObstacleLeftSide()){
				
			// CHANGE COLOR OF BEAK, STOP, AND WAIT FOR .4 SECONDS
			_finch1.stopWheels();
			_finch1.setLED(0, 0, 200);
			this.doFor(400);
				
			// BACK UP A LITTLE BIT
			_finch1.setWheelVelocities(-100, -150);
			this.doFor(650);
				
			// PERFORM THE TURN
			_finch1.setWheelVelocities(-100, 150);
			this.doFor(400);
			corner = corner + 1;
		}
			
		// IF IT'S NOT AT A CORNER, BUT RATHER ALONG A WALL
		else{
				
			// BACK UP A LITTLE BIT
			_finch1.setWheelVelocities(-150, -130);
			this.doFor(400);
				
			// TURN AWAY FROM THE WALL
			_finch1.setWheelVelocities(-90, 90);
			this.doFor(280);
		}
			
		// RETURNS AN INT VALUE, 0 IF IT HIT A WALL, 1 IF IT HIT A CORNER
		return corner;
	}
			
	/**
	 * This method is used to command the finch to execute the movements specified in a text file, selected via file selector. The
	 * only valid characters within the text file are the integers 0 - 9 and the characters 'F', 'T', 'B', and '-'. The characters 'F',
	 * 'B', and 'T', indicate forwards, backwards, and turn, respectively. The character '-' is only allowed after the 'T' character 
	 * and prior to any number. The correct format for the text file  is a character 'F', 'B', or 'T' followed by the time of 
	 * travel desired (for forwards and backwards commands) or the number of degrees for the finch should turn (counterclockwise is 
	 * positive, clockwise is negative).
	 * @return true if the method ran properly, false if the file was corrupt
	 */
    public boolean moveFromFile(){

    	// THE VALID COMMANDS FOR THE ROBOT ARE FORWARD, BACK, AND TURN, SPECIFIED BY 'F','B', AND 'T' WITHIN THE TEXT FILE. ALL OTHER CHARACTERS,
    	// OTHER THAN NUMBERS ARE INVALID
    	
    	// THIS GETS THE STRING REPRESENTATION OF THE MOVEMENTS
    	String movements = this.readFile();
    	System.out.println("The movements that will be executed are: " + movements);
    	// IF THE FILE WAS CORRUPT, AND THE STRING IS NULL, RETURN A VALUE OF FALSE
    	if (movements == null){
    		return false;
    	}
    	else if (movements.equals(null)){
    		return false;
    	}
    	
    	// THIS INTEGER WILL KEEP TRACK OF THE NUMBER WE ARE GOING TO FEED TO METHOD CALLS
    	int number = 0;
    	// THIS STRING WILL KEEP TRACK OF THE INT VALUES WE READ FROM THE FILE
    	String numberString = "";
    	// THIS INTEGER WILL KEEP TRACK OF WHAT CHARACTER WE ARE EXAMING IN THE STRING RETRIEVED FROM THE TEXT FILE
    	int placeInString = 0;
    	// WE INITIALIZE THE VALUE OF THE CURRENT INSTRUCTION TO NULL
    	char currentInstruction = 'N';
    	// DEFUALT VALUE FOR THE CHARACTER, WE DON'T EVER USE THIS VALUE
    	char character = '0';
    	
    	// WHILE THERE ARE STILL CHARACTERS LEFT IN THE FILE
    	while (placeInString<movements.length()){

			character = movements.charAt(placeInString);
    		// IF THE CHARACTER IS NOT A NUMBER
    		if (!(character == '0' || character == '1' || character == '2' || character == '3' || character == '4' || character == '5' || character == '6' || character == '7' || character == '8' || character == '9' || character == '-')){
        		
    			// THEN THE CHARACTER MUST BE AN 'F' 'B' OR 'T'
        		if (character == 'F'){
            		currentInstruction = 'F';
            		placeInString++;
       			}
        		else if (character == 'B'){
            		currentInstruction = 'B';
            		placeInString++;
       			}
        		// WE KNOW THAT THE CHARACTER HAS TO BE 'T'
       			else{
            		currentInstruction = 'T';
            		placeInString++;
        		}
   			}
        				
    		// IF THE CHARACTER IS A NUMBER
    		else {

    			// THE FIRST THING WE WANT TO DO IS UPDATE THE NUMBER THAT WE ARE READING FROM THE STRING
    			numberString = numberString + character;
    			
    			// IF WE'VE REACHED THE END OF THE FILE
    			if (placeInString + 1 == movements.length()){
    				number = Integer.parseInt(numberString);
    				// PERFORM A DIFFERENT ACTION BASED ON THE CURRENT INSTRUCTION
    				switch(currentInstruction){
    				case 'F':
    					System.out.println("Moving Forwards for " + number + " milliseconds");
    					this.moveForwards(number);
    					numberString = "";
	    				break;
	    			case 'B':
	    				System.out.println("Moving Backwards for " + number + " milliseconds");
	    				this.moveBackWards(number);
	    				numberString = "";
	    				break; 
	    			case 'T':
	    				System.out.println("Turning " + number + " degrees");
	    				this.turn(number, _finch1);
	    				numberString = "";
	    				break;
	    			// NULL CASE, THERE HASN'T BEEN A MOTION INSTRUCTION IN THE TEXT FILE YET
	    			case 'N':
	    				this.doFor(3);
	    				break;
	    			}
	    			// THIS BREAK STATEMENT EXITS THE IF LOOP
	    			break;
	    			}
	
    			// WE KNOW THAT THEREHAS TO BE A NEXT CHARACTER, SO NOW WE CHECK TO SEE IF IT'S A NUMBER OR LETTER
	    		char nextCharacter = movements.charAt(placeInString + 1);
	    			
	    		if(nextCharacter != '0' && nextCharacter != '1' && nextCharacter != '2' && nextCharacter != '3' && nextCharacter != '4' && nextCharacter != '5' && nextCharacter != '6' && nextCharacter != '7' && nextCharacter != '8' && nextCharacter != '9'){
	    			number = Integer.parseInt(numberString);
	    			switch(currentInstruction){
	    			case 'F':
	    				System.out.println("Moving Forwards for " + number + " milliseconds");
	    				this.moveForwards(number);
	    				numberString = "";
	    				break;
	    			case 'B':
	    				System.out.println("Moving Backwards for " + number + " milliseconds");
	    				this.moveBackWards(number);
	    				numberString = "";
	    				break;
	    			case 'T':
	    				System.out.println("Turning " + number + " degrees");
	    				this.turn(number, _finch1);
	    				numberString = "";
	    				break;
	    			// NULL CASE, THERE HASN'T BEEN A MOTION INSTRUCTION IN THE TEXT FILE YET
	    			case 'N':
	    				this.doFor(3);
	    				break;
	    			}
	    		}
	    		
	    		// INCEREMENT THE PLACE THAT WE ARE EXAMING WITHIN THE TEXT FILE
	    		placeInString++;
    		}
    	// END WHILE LOOP
    	}
		return true;
    } 
    /**
     * This private helper method is used to read the movements specified in a text file, selected via file selector. This method also
     * checks to ensure that the file is not corrupted
     * @return String representation of the movements specified in the text file
     */
    private String readFile() {	
    	// THIS IS THE DIRECTORY FROM WHICH WE WILL LOOK FOR FILES
    	String directory = "C:";
		JFileChooser fileChooser = new JFileChooser();
		File defaultDirectory = new File(directory);
		fileChooser.setCurrentDirectory(defaultDirectory);
		fileChooser.showOpenDialog(new JFrame());
		String s = "";
		Scanner scanner;
		try {
			scanner = new Scanner(fileChooser.getSelectedFile());
			String fileString = scanner.next();
				
				for (int index = 0; index < fileString.length(); index = index + 1){
				char c = fileString.charAt(index);
					if (!(c == '0' || c == '1' ||c == '2' ||c == '3' ||c == '4' ||c == '5' ||c == '6' ||c == '7' ||c == '8' || c == '9'|| c == 'F'|| c == 'B' || c == 'T' || c =='-')){
						System.out.println("The file is corrupt and cannot be read");
						s = null;
						break;
					}
					
					// IF WE READ A NEGATIVE SIGN
					if (fileString.charAt(index) == '-'){ 
					
						// AS LONG AS IT'S NOT THE LAST CHARACTER IN THE STRING (PREVENTS INDEX EXCEPTION)
						if (index + 1 < fileString.length()){
							char next = fileString.charAt(index + 1);
							// IF THE CHARACTER FOLLOWING THE '-' IS NOT A NUMBER
							if( next != '0' && next != '1' && next != '2' && next != '3' && next != '4' && next != '5' && next != '6' && next != '7' && next != '8' && next != '9'){
								System.out.println("The file is corrupt and cannot be read");
								s = null;
								break;
							}
						}
						// AS LONG AS IT'S NOT THE FIRST CHARACTER IN THE STRING (PREVENTS INDEX EXCEPTION)
						if (index > 0){
							char prior = fileString.charAt(index - 1);
							// IF A NEGATIVE SIGN COMES AFTER ANYTHING BUT A 'T'
							if (prior != 'T'){
								System.out.println("The file is corrupt and cannot be read");
								s = null;
								break;
							}
						}
					}
					// IF EVERYTHING IS GOOD, ADD THE CURRENT CHARACTER TO THE STRING
					s = s + c;
				}
			scanner.close();
		} 
		catch (FileNotFoundException e) {
			System.out.println("The file \n \n" + fileChooser.getSelectedFile() + "\n \ndoes not exist.");
		}	
		return s;
	}
    
    // THIS PRIVATE INSTANCE VARIABLE IS USED TO KEEP TRACK OF THE MOVEMENTS FOR WRITING TO A FILE
	private String _writeToFileMovements = "";
    /**
     * This method allows the user to control the finch manually and write the movements to a text file automatically.
     * @return true as long as everything works correctly, false otherwise
     */
    public boolean writeToFile(){
    	
    	// THIS FIRST CHUNK OF CODE CREATES THE BUTTONS AND FRAME IN WHICH THE BUTTONS WILL BE PLACED
		JFrame buttonFrame = new JFrame("RECORDING MOVEMENTS");
		buttonFrame.setLayout(new BorderLayout());
		JButton forwardsButton = new JButton("Forwards");
		JButton backwardsButton = new JButton("Backwards");
		JButton turnRightButton = new JButton("Turn Right");
		JButton turnLeftButton = new JButton("Turn Left");
		JButton finishButton = new JButton("Finish");
		finishButton.setBackground(java.awt.Color.LIGHT_GRAY);
		buttonFrame.add(forwardsButton, BorderLayout.PAGE_START);
		buttonFrame.add(backwardsButton, BorderLayout.CENTER);
		buttonFrame.add(turnLeftButton, BorderLayout.LINE_START);
		buttonFrame.add(turnRightButton, BorderLayout.LINE_END);
		buttonFrame.add(finishButton, BorderLayout.PAGE_END);
		buttonFrame.setVisible(true);
		buttonFrame.pack();
		
		// ANONYMOUS INNER CLASS FOR FORWARDS LISTENER
		forwardsButton.addMouseListener(new MouseListener(){
			double _clickStart = 0.0;
			double _clickStop = 0.0;
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				_clickStart = System.currentTimeMillis();
				_finch1.setWheelVelocities(100, 100);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				_clickStop = System.currentTimeMillis();
				_finch1.stopWheels();
				double time = _clickStop - _clickStart;
				_writeToFileMovements = _writeToFileMovements + "F" + (int) time;
			}
		});

		
		// ANONYMOUS INNER CLASS FOR BACKWARDS LISTENER
		backwardsButton.addMouseListener(new MouseListener(){
			double _clickStart = 0.0;
			double _clickStop = 0.0;
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				_clickStart =  System.currentTimeMillis();
				_finch1.setWheelVelocities(-100, -100);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				_clickStop = System.currentTimeMillis();
				_finch1.stopWheels();
				double time = _clickStop - _clickStart;
				_writeToFileMovements = _writeToFileMovements + "B" + (int) time;
			}
		});
		
		// ANONYMOUS INNER CLASS FOR TURNLEFT LISTENER
		turnLeftButton.addMouseListener(new MouseListener(){
			double _clickStart = 0.0;
			double _clickStop = 0.0;
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				_clickStart =  System.currentTimeMillis();
				_finch1.setWheelVelocities(-100, 100);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				_clickStop = System.currentTimeMillis();
				_finch1.stopWheels();
				double time = _clickStop - _clickStart;
				_writeToFileMovements = _writeToFileMovements + "T" + (int) time/9;
			}
		});
		
		// ANONYMOUS INNER CLASS FOR TURNRIGHT LISTENER
		turnRightButton.addMouseListener(new MouseListener(){
			double _clickStart = 0.0;
			double _clickStop = 0.0;
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				_clickStart =  System.currentTimeMillis();
				_finch1.setWheelVelocities(100, -100);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				_clickStop = System.currentTimeMillis();
				_finch1.stopWheels();
				double time = _clickStop - _clickStart;
				_writeToFileMovements = _writeToFileMovements + "T-" + (int) time/9;
			}
		});
		
		// ANONYMOUS INNER CLASS FOR FINISHBUTTON LISTENER
		finishButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser fileChooser = new JFileChooser();
				
				// THIS SHOULD BE SPECIFIC TO THE COMPUTER BEING USED
				File defaultDirectory = new File("C:");
				fileChooser.setCurrentDirectory(defaultDirectory);
				fileChooser.showSaveDialog(new JFrame());
				FileWriter out = null;
				try {
					String s = fileChooser.getSelectedFile() + ".txt";
					out = new FileWriter(s);
					if (!(s.equalsIgnoreCase(".txt"))){
						System.out.println("The movements recorded to the text file were: " + _writeToFileMovements);
						out.write(_writeToFileMovements);
						_writeToFileMovements = "";
						out.close();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				_finch1.stopWheels();
				_finch1.quit();
				System.exit(0);
			}
			
		});
		
    	return true;
    }
    
    
    
    
    
    
    
    // WE NEED TO CREATE A REFERENCE TO THIS (THE FINCH CONTROLLER) TO USE IT IN INNER CLASSES OF THE CONVOY METHOD
	FinchController _fc = this;
	// WE ALSO NEED REFERENCES TO THE PREVIOUS X AND Y COORDINATES
	LinkedList<Integer> _leaderXCoordinateHistory;
	LinkedList<Integer> _leaderYCoordinateHistory;
	// WE NEED TO KEEP TRACK OF WHICH INSTRUCTION EACH FINCH IS WORKING ON
	int _finch2InstructionIndex;
	int _finch3InstructionIndex;
	int _finch4InstructionIndex;
	// WE ALSO NEED TO KEEP TRACK OF THE ANGLES OF THE FINCHES
	int[] _finchAngles;
	/**
	 * The leader must always be blue, 2nd is green, pink, then red
	 */
    public void convoy(){
    	// EACH FINCH STARTS BY EXECUTING THE FIRST INSTRUCTION
    	_finch2InstructionIndex = 2;
    	_finch3InstructionIndex = 1;
    	_finch4InstructionIndex = 0;
    	//CREATS THE ARRAY INTO WHICH WE SILL STORE ANGLE VALUES
    	// SET THE INITIAL VLAUES OF THE ARRAY TO BE 0
    	for(int i = 0; i < _finchAngles.length; i = i + 1){
    		_finchAngles[i] = 0;
    	}
    	
    	// SETTING THE COLOR OF THE BEAKS OF THE FINCHES TO MATCH THE COLOR ON THIER BACKS, 1st BLUE, 2ND GREEN, 3RD PINK, 4TH RED
    	if (!(_finch1 == null)){
    	_finch1.setLED(0, 0, 200);
    	}
    	if (!(_finch2 == null)){
    	_finch2.setLED(0, 200, 0);
    	}
    	if (!(_finch3 == null)){
    		_finch3.setLED(200, 50, 30);
    	}
    	if (!(_finch4 == null)){
    	_finch4.setLED(200, 0, 0);
    	}

    	// THIS CHUNK OF CODE CREATES THE BUTTONS AND FRAME IN WHICH THE BUTTONS WILL BE PLACED
		JFrame buttonFrame = new JFrame("Convoy controls");
		JLabel label = new JLabel("\n   (Controlling the lead finch)   \n");
		buttonFrame.setLayout(new BorderLayout());
		JButton forwardsButton = new JButton("Forwards");
		JButton turnRightButton = new JButton("Turn Right");
		JButton turnLeftButton = new JButton("Turn Left");
		buttonFrame.add(forwardsButton, BorderLayout.PAGE_START);
		buttonFrame.add(label, BorderLayout.CENTER);
		buttonFrame.add(turnLeftButton, BorderLayout.LINE_START);
		buttonFrame.add(turnRightButton, BorderLayout.LINE_END);
		buttonFrame.setVisible(true);
		buttonFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		buttonFrame.pack();
		
		// THIS ESTABLISHES THE INITIAL COORDINATES TO WHICHT HE FINCHES WILL MOVE. 
		// IT SHOULD MAKE IT SO EACH FINCH IS ALWAYS ONE MOVE BEHIND THE FINCH IN FRONT OF IT
		_leaderXCoordinateHistory.add(this.getBodyXCoordinate('r'));
		_leaderXCoordinateHistory.add(this.getBodyXCoordinate('p'));
		_leaderXCoordinateHistory.add(this.getBodyXCoordinate('g'));
		_leaderXCoordinateHistory.add(this.getBodyXCoordinate('b'));
		
		_leaderYCoordinateHistory.add(this.getBodyYCoordinate('r'));
		_leaderYCoordinateHistory.add(this.getBodyYCoordinate('p'));
		_leaderYCoordinateHistory.add(this.getBodyYCoordinate('g'));
		_leaderYCoordinateHistory.add(this.getBodyYCoordinate('b'));
		
		
		System.out.println("X1 initial: " + _leaderXCoordinateHistory.get(3) + "  Y: " + _leaderYCoordinateHistory.get(3));
		System.out.println("X2 initial: " + _leaderXCoordinateHistory.get(2) + "  Y: " + _leaderYCoordinateHistory.get(2));
		System.out.println("X3 initial: " + _leaderXCoordinateHistory.get(1) + "  Y: " + _leaderYCoordinateHistory.get(1));
		System.out.println("X4 initial: " + _leaderXCoordinateHistory.get(0) + "  Y: " + _leaderYCoordinateHistory.get(0));
		
		// ANONYMOUS INNER CLASS FOR FORWARDS LISTENER
		forwardsButton.addMouseListener(new MouseListener(){
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				
				_finch1.setWheelVelocities(100, 100);
			
				// WE CHECK TO MAKE SURE THAT WE HAVE MORE FINCHES
				if (!(_finch2 == null)){
					_fc.followerMoveToCoordinate(_finch2InstructionIndex, _finch2);
				}
				if (!(_finch3 == null)){
					_fc.followerMoveToCoordinate(_finch3InstructionIndex, _finch3);
				}
				if (!(_finch4 == null)){
					_fc.followerMoveToCoordinate(_finch4InstructionIndex, _finch4);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				_finch1.stopWheels();
				_leaderXCoordinateHistory.add(_fc.getBodyXCoordinate('b'));
				_leaderYCoordinateHistory.add(_fc.getBodyYCoordinate('b'));
			}
		});

		// ANONYMOUS INNER CLASS FOR TURNLEFT LISTENER
		turnLeftButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				_finch1.setWheelVelocities(-100, 100);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				_finch1.stopWheels();
			}
		});
		
		// ANONYMOUS INNER CLASS FOR TURNRIGHT LISTENER
		turnRightButton.addMouseListener(new MouseListener(){
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				_finch1.setWheelVelocities(100, -100);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				_finch1.stopWheels();
			}
		});	
    }
    
    /**
     * This method causes a finch to move to a specific point on the leaders history path
     * @param placeInPathHistory - the particular point which the finch will move to
     * @param finch - the finch that is moving
     * @return - true if it finishes the movement, false if not
     */
    public boolean followerMoveToCoordinate(int placeInPathHistory, FinchInterface finch){
    	
    	
    	// THESE ARE ARBITRARY VLAUES ADDED SO THAT WE COULD TEST OUT THE POINT SYSTEM
//    	_leaderXCoordinateHistory.add(300);
//    	_leaderYCoordinateHistory.add(120);
    	
    	
    	
    	// GETS THE X AND Y VALUES THAT WE WISH TO MOVE TO
    	int xToMoveTo = _leaderXCoordinateHistory.get(placeInPathHistory);
    	int yToMoveTo = _leaderYCoordinateHistory.get(placeInPathHistory);
    	// ANGLE OF THE FINCH THAT WE ARE DEALING WITH, GIVES IT AN INITIAL VALUE
    	int currentAngle = 0;
    	// GIVES C AN INITIAL VALUE
    	char c = 'g';
    	// SELECTS WHICH FINCH WE ARE TRYING TO GET
    	if (finch == _finch2){
    		c = 'g';
    		currentAngle = _finchAngles[0];
    	}
    	else if (finch == _finch3){
    		c = 'p';
    		currentAngle = _finchAngles[1];
    	}
    	else if (finch == _finch4){
    		c = 'r';
    		currentAngle = _finchAngles[2];
    	}
    	// THESE ARE THE COORDINATES THAT WE'RE MOVING FROM
    	int xToMoveFrom = this.getBodyXCoordinate(c);
    	int yToMoveFrom = this.getBodyYCoordinate(c);
    			    	
    	// THE ANGLE THAT THE FINCH SHOULD MOVE AT TO REACH ITS TARGET POINT
    	int instructionAngle = this.computeAngle(xToMoveFrom, yToMoveFrom, xToMoveTo, yToMoveTo);
    	
    	this.turn(instructionAngle-currentAngle, finch);

    	// UPDATES THE ANGLE AND THE INSTRUCITON FOR THE NEXT POINT
    	if (finch == _finch2){
    		_finchAngles[0] = instructionAngle;
    		_finch2InstructionIndex++;
    	}
    	else if (finch == _finch3){
    		_finchAngles[1] = instructionAngle;
    		_finch3InstructionIndex++;
    	}
    	else if (finch == _finch4){
    		_finchAngles[2] = instructionAngle;
    		_finch4InstructionIndex++;
    	}
    	System.out.println("Current Angle: " + currentAngle);
    	System.out.println("Instruction Angle: " + instructionAngle);

		// TELL THE FOLLOWER FINCH TO MOVE FORWARD, IN THE RIGHT DIRECTION
    	finch.setWheelVelocities(100, 100);
    	// CORRECT IT'S ANGLE WHILE IT MOVES TOWARDS THE LEADER FINCH
    	while (Math.abs(this.getBodyXCoordinate(c) - xToMoveTo) > 5 || Math.abs(this.getBodyYCoordinate(c) - yToMoveTo) > 5){
    		int xCoor1 = this.getBodyXCoordinate(c);
    		int yCoor1 = this.getBodyYCoordinate(c);
    		this.doFor(400);
    		int xCoor2 = this.getBodyXCoordinate(c);
    		int yCoor2 = this.getBodyYCoordinate(c);
    		currentAngle = this.computeAngle(xCoor1, yCoor1, xCoor2, yCoor2);
    		System.out.println("Next Angle: " + currentAngle);
    		if (Math.abs(currentAngle-instructionAngle) < 5){
    			finch.setWheelVelocities(100, 100);
    		}
    		if (currentAngle>instructionAngle){
    			finch.setWheelVelocities(110, 90);
    		}
    		if (currentAngle<instructionAngle){
    			finch.setWheelVelocities(90, 110);
    		}
    	}
    	
    	finch.stopWheels();
    	return true;    	
    }
    
    /**
     * This method causes the finch to calculate the angle that it's facing in the birdcage
     * @param finch, the finch whose angle we are finding
     * @param moveBackToInitial, true if the finch should move back to original coordinate, false if not
     * @return an int value of the degree that the finch is facing
     */
    public int getFinchDirection(FinchInterface finch, boolean moveBackToInitial){
		
    	// GIVES C AN INITIAL VALUE
    	char c = 'r';
    	
    	// SELECTS WHICH FINCH WE ARE TRYING TO GET
    	if (finch == _finch1){
    		c = 'b';
    	}
    	else if (finch == _finch2){
    		c = 'g';
    	}
    	else if (finch == _finch3){
    		c = 'p';
    	}
    	else if (finch == _finch4){
    		c = 'r';
    	}
    	int xInitial = this.getBodyXCoordinate(c);
    	int yInitial = this.getBodyYCoordinate(c);		
    	
    	// WE MOVE THE FINCH FORWARD A BIT IN ORDER TO CREATE A LINE BETWEEN TWO POINTS, ENABLING US TO CALCULATE SLOPE/ANGLE
    	// THE DURATION OF THE MOVE FORWARD MAY BE MANIPULATED IN ORDER TO OBTAIN MORE ACCURATE RESULTS
    	finch.setWheelVelocities(250, 250, 200);
    	
    	int xFinal = this.getBodyXCoordinate(c);
    	int yFinal = this.getBodyYCoordinate(c);
    	
    	return this.computeAngle(xInitial, yInitial, xFinal, yFinal);
    }
    
    /**
     * This method returns the angle that the finch is traveling in, as specified by two different x and y values
     * @param xInitial - initial x value
     * @param yInitial - initial y value
     * @param xFinal - ending x value
     * @param yFinal - ending y value
     * @return
     */
    private int computeAngle(int xInitial, int yInitial, int xFinal, int yFinal){
    	
    	int changeInX = xFinal-xInitial;
    	// THESE TWO VALUES ARE SWITCHED BECAUSE Y DECREASES AS IT GOES TOWARDS THE BOARD
    	int changeInY = yInitial-yFinal;
    	
    	double angle = Math.atan((double)changeInY/(double)changeInX) * 360 / (2 * Math.PI);
    	if (xFinal<xInitial && yInitial>yFinal){
    		angle = angle + 180;
    	}
    	else if (xFinal<xInitial && yInitial<yFinal){
    		angle = angle - 180;
    	}
    	else if (xFinal == xInitial && yInitial>yFinal){
    		angle = 90;
    	}
    	else if (xFinal == xInitial && yInitial<yFinal){
    		angle = -90;
    	}
    	else if (yInitial==yFinal && xFinal>xInitial){
    		angle = 0;
    	}
    	else if (yInitial==yFinal && xFinal<xInitial){
    		angle = 180;
    	}
    	System.out.println("Angle: " + angle);
    	return (int) angle;

    }
    
    
    /**
     * This is the method used to enable a team of two finches to play soccer. They will try to score on the side closest to the white board.
     * Finch 1 (blue) will be the offensive robot and Finch 2 (green) is the defensive robot.
     */
    public void finchSoccerOffense(){
    	
    	int xCoordinateOfGoal = 315;
    	
		// THE ANGLES OF THE FINCHES
		_finchAngles[0] = 0;
		
    	while (this.getBallXCoordinate() < xCoordinateOfGoal){
        	
    		_finch1.setLED(0, 0, 200);
    	
    		
        	int finch1X = this.getBodyXCoordinate('b');
        	int finch1Y = this.getBodyYCoordinate('b');
        	int ballX = this.getBallXCoordinate();
        	int ballY = this.getBallYCoordinate();
        	System.out.println("X1: " + finch1X + "   Y1: " + finch1Y + "\nBall: " + ballX + "  " + ballY);
    		
        	int instructionAngle;
        	
        	// FINCH 1 (OFFENSE, BLUE)
        	
        	// IF THE FINCH IS IN FRON TOF THE BALL
        	if(finch1X>=this.getBallXCoordinate()){
        		// WE INCLUDE -25 BECAUSE WE WANT THE OFFENSIVE FINCH TO MOVE BEHIND THE BALL
        		System.out.println("Less than");
        		_finch1.setWheelVelocities(-200,-200);
        	}
        	
        	// IF FINCH1 IS BEHIND THE BALL
        	else{
        		instructionAngle = this.computeAngle(finch1X, finch1Y, ballX, ballY);
        		System.out.println("push ball");
        		this.turn(instructionAngle - _finchAngles[0], _finch1);
        		//POwerKick
        		if (finch1X>265){
        			_finch1.setWheelVelocities(250, 250);
        		}
        		else{
        			_finch1.setWheelVelocities(120, 120);
        		}
        	}
        	
        	int movedFromX = this.getBodyXCoordinate('b');
        	int movedFromY = this.getBodyYCoordinate('b');
        	
        	// MOVE FORWARD FOR A SET AMOUNT OF TIME
        	this.doFor(400);
        	
        	int movedToX = this.getBodyXCoordinate('b');
        	int movedToY = this.getBodyYCoordinate('b');
        	System.out.println(_finchAngles[0]);
        	_finchAngles[0] = this.computeAngle(movedFromX, movedFromY,movedToX,movedToY);
        	
        	if (_finch1.isObstacle()){
        		_finch1.setWheelVelocities(-200, -200, 350);
        	}
    	}
    	
    	_finch1.stopWheels();

    }
   
    public void finchSoccerDefense(){
    	
    	
    	int origFinch1X = this.getBodyXCoordinate('b');
    	int origFinch1Y = this.getBodyYCoordinate('b');
    	int finch1X = this.getBodyXCoordinate('b');
    	int finch1Y = this.getBodyYCoordinate('b');
    	int ballX = this.getBallXCoordinate();
    	int ballY = this.getBallYCoordinate();
    	
    	// WHILE THE BALL IS GENERALLY PAST 
    	while(ballX>finch1X-15){
    		
        	finch1X = this.getBodyXCoordinate('b');
        	finch1Y = this.getBodyYCoordinate('b');
        	ballX = this.getBallXCoordinate();
        	ballY = this.getBallYCoordinate();
        	
        	
    		if (Math.abs(finch1Y - ballY) > 7 && finch1Y<ballY){
    				_finch1.setWheelVelocities(-250, -250);
    		}
    		else if (Math.abs(finch1Y - ballY) > 7 && finch1Y>ballY){
        		if (_finch1.isObstacle()){
        			_finch1.stopWheels();
        		}	
        		else{
        			_finch1.setWheelVelocities(250, 250);
        		}
    		}
    		else{
    			_finch1.stopWheels();
    		}
    	
    		this.doFor(10);
    		
    	}
    	
    	_finch1.stopWheels();
    	_finch1.setLED(200, 0, 0);
    	
    }
    
    
    private int _previousYCoord;
    private int _presentYCoord;
    private int _previousXCoord;
    private int _presentXCoord;
    private boolean _direction;
    private double _maxTemp;
    private int _initalLight;
    
    /**
     * This method causes the finch to travel the entirety of the maze, and after doing so, return to the hottest spot in the mazw
     * @return - an int, the final x coordinate of the finch after it has reached the brightest spot in the cage 
     */
    public int findHotSpot(){

    int range = 20;
      
            // The perch is position (0,0)
            // Going up along the short side, the finch would end up at (0,10)
            // etc...
            // The corner directly diagonal from the perch is position (10,10)
               
               _maxTemp = 0;
                int[] maxTempCoord = new int[2];
                _direction = true;
                int _initalLight = _finch1.getRightLightSensor();
                
                this.turn(90, _finch1);
                this.doFor(500);
                
                _presentXCoord = this.getBodyXCoordinate('b');
                
                while (_presentXCoord < 300){
                	
                while(!(_finch1.isObstacle())){
                	if (_finch1.getTemperature() > _maxTemp && _finch1.getLeftLightSensor() < _initalLight + 50 && _finch1.getRightLightSensor() < _initalLight + 50){
                		maxTempCoord[0] = this.getBodyXCoordinate('b');
                		maxTempCoord[1] = this.getBodyYCoordinate('b');
                		_maxTemp = _finch1.getTemperature();
                		System.out.println("New max temp: " + _maxTemp + "\nCoordinates: " + maxTempCoord[0] + ", " + maxTempCoord[1]);
                	}
                	_previousXCoord = this.getBodyXCoordinate('b');
                    _previousYCoord = this.getBodyYCoordinate('b');
                    _finch1.setWheelVelocities(150, 150);
                    this.doFor(500);
                    _presentYCoord = this.getBodyYCoordinate('b');
                    _presentXCoord = this.getBodyXCoordinate('b');
                    if (_direction){
                        this.turn(-(this.computeAngle(_previousXCoord, _previousYCoord, _presentXCoord, _presentYCoord)-90), _finch1); 
                    }
                    else{
                        this.turn(-(this.computeAngle(_previousXCoord, _previousYCoord, _presentXCoord, _presentYCoord)+90), _finch1); 
                    }

                    
                }
                
                _finch1.setWheelVelocities(-120, -120, 700);
                
                if (_direction){
                	this.turn(-90, _finch1);
                }
                else{
                	this.turn(90, _finch1);
                }
                
                int x = _presentXCoord + 20;
                
                while (_presentXCoord < x){
                	if (_finch1.getTemperature() > _maxTemp && _finch1.getLeftLightSensor() < _initalLight + 50 && _finch1.getRightLightSensor() < _initalLight + 50){
                		maxTempCoord[0] = this.getBodyXCoordinate('b');
                		maxTempCoord[1] = this.getBodyYCoordinate('b');
                		_maxTemp = _finch1.getTemperature();
                		System.out.println("New max temp: " + _maxTemp + "\nCoordinates: " + maxTempCoord[0] + ", " + maxTempCoord[1]);
                	}
                	if (_finch1.isObstacle()){
            			System.out.println("Coordinate of max: " + maxTempCoord[0] + ", " + maxTempCoord[1]);
            			int presentAngle = this.getFinchDirection(_finch1, true);
                		while(Math.abs(maxTempCoord[0] - this.getBodyXCoordinate('b')) > 5){
                			int newAngle = this.computeAngle(this.getBodyXCoordinate('b'),this.getBodyYCoordinate('b'), (int) maxTempCoord[0], (int) maxTempCoord[1]);
                			this.turn((newAngle - presentAngle), _finch1);
                			_finch1.setWheelVelocities(150, 150, 500);
                			presentAngle = newAngle;
                		}
                		_finch1.stopWheels();
                		return this.getBodyXCoordinate('b');
                	}
                	_previousXCoord = this.getBodyXCoordinate('b');
                    _previousYCoord = this.getBodyYCoordinate('b');
                    _finch1.setWheelVelocities(150, 150);
                    this.doFor(500);
                    _presentYCoord = this.getBodyYCoordinate('b');
                    _presentXCoord = this.getBodyXCoordinate('b');
                    this.turn(-(this.computeAngle(_previousXCoord, _previousYCoord, _presentXCoord, _presentYCoord)), _finch1);
                	_presentXCoord = this.getBodyXCoordinate('b');
                }
                
                if (_direction){
                	this.turn(-90, _finch1);
                }
                else{
                	this.turn(90, _finch1);
                }
                _direction = !_direction;
                }
                
				return this.getBodyXCoordinate('b');
    }
}

                
//                // AFTER IT SENSES AN OBSTALCLE, THIS DELAY ACTS AS A BUFFER SO THE ROBOT CAN CHECK TO SEE IF
//                // IT BOTH SENSORS DETACT AN OBSTACLE
//                this.doFor(300);
//               
//                // IF IT HITS A CORNER, DO THIS
//                if (_finch1.isObstacleRightSide()){
//                   
//                    // CHANGE COLOR OF BEAK, STOP, AND WAIT FOR .4 SECONDS
//                    _finch1.stopWheels();
//                    _finch1.setLED(0, 0, 200);
//                    this.doFor(400);
//                   
//                    // BACK UP A LITTLE BIT
//                    //****** This code my need to be reversed because
//                    //now the finch is traveling using the perimeter code
//                    //and its left side is facing the wall rather than the
//                    //right side
//                    _finch1.setWheelVelocities(-100, -150);
//                    this.doFor(650);
//                   
//                    // PERFORM THE TURN
//                    _finch1.setWheelVelocities(100, -150);
//                    this.doFor(400);
//                   
//                }
//               
//                // IF IT'S NOT AT A CORNER, BUT RATHER ALONG A WALL
//                else{
//                   
//                    // BACK UP A LITTLE BIT
//                    _finch1.setWheelVelocities(-130, -150);
//                    this.doFor(400);
//                   
//                    // TURN AWAY FROM THE WALL
//                    _finch1.setWheelVelocities(90, -90);
//                    this.doFor(280);
//                }
//               
//                while(this.getBodyXCoordinate(_colorOfMainFinch)<59){
//                    _finch1.setWheelVelocities(100, 100);
//                    this.doFor(400);
//                }
//                if(this.getBodyXCoordinate(_colorOfMainFinch)>59){
//                    // CHANGE COLOR OF BEAK, STOP, AND WAIT FOR .4 SECONDS
//                    _finch1.stopWheels();
//                    _finch1.setLED(0, 0, 200);
//                    this.doFor(400);
//                   
//                    // BACK UP A LITTLE BIT
//                    _finch1.setWheelVelocities(-100, -100);
//                    this.doFor(650);
//                   
//                    // PERFORM THE TURN,
//                    _finch1.setWheelVelocities(150, -100);
//                    this.doFor(400);
//                }
//               
//               
//                /**
//                 * Now the finch is facing downwards at coordinate (1,10)
//                 * it moves along the x axis until it reaches (1,0)
//                 * then it'll
//                 */
//               
//                while(this.getBodyYCoordinate(_colorOfMainFinch) < 215 && this.getBodyXCoordinate(_colorOfMainFinch) < 300){
//                    this.moveAlongY(_presentXCoord);
//                }   
//                   
//            return true;
//        }
//            /**
//             * Move the finch along the Y axis. if it goes over the buffer
//             * zone then it knows to stop, back up and turn back in the correct
//             * direction       
//             * @param _xcoord
//             *                 - the coordinate we ideally want to be on
//             * getXCoordinate() is the current position of the finch
//             */
//               
//            public void moveAlongY(Integer _xcoord){
//                int range = 20;
//                    if(_finch1.isObstacleLeftSide() && _finch1.isObstacleRightSide()){
//                        this.turnWhichWay();
//                    }
//                    while(this.getBodyXCoordinate(_colorOfMainFinch)> _xcoord - range && this.getBodyXCoordinate(_colorOfMainFinch)< _xcoord + range){
//                        _finch1.setWheelVelocities(120,120);
//                    }
//                    _finch1.stopWheels();
//                    this.doFor(400);
//                    // Decide which way to adjust based on the direction the finch is facing in the y-direction
//                    if(_direction){
//                        if(_xcoord.compareTo(this.getBodyXCoordinate(_colorOfMainFinch)) > 0){
//                            //we are currently under our desired x coordinate
//                            //turn left a bit, then move. do for certain time
//                            _finch1.setWheelVelocities(110, 130);
//                            this.doFor(200);
//                            this.moveAlongY(_xcoord);
//                        }
//                        else{
//                            if(_xcoord.compareTo(this.getBodyXCoordinate(_colorOfMainFinch)) < 0){
//                            //we are currently over our desired x coordinate
//                            //turn right bit, then move. do for certain time
//                            _finch1.setWheelVelocities(130, 110);
//                            this.doFor(200);
//                            this.moveAlongY(_xcoord);
//                            }
//                        }
//                    }
//                    if(_direction==false){
//                        if(_xcoord.compareTo(this.getBodyXCoordinate(_colorOfMainFinch)) < 0){
//                                //we are currently over our desired x coordinate
//                                //turn right bit, then move. do for certain time
//                                _finch1.setWheelVelocities(130, 110);
//                                this.doFor(200);
//                                this.moveAlongY(_xcoord);
//                        }
//                        else{
//                            if(_xcoord.compareTo(this.getBodyXCoordinate(_colorOfMainFinch)) > 0){
//                                //we are currently under our desired x coordinate
//                                //turn left a bit, then move. do for certain time
//                                _finch1.setWheelVelocities(110, 130);
//                                this.doFor(200);
//                                this.moveAlongY(_xcoord);;
//                            }
//                        }
//                    }
//                   
//                   
//            }
//           
//            /**
//             * This method determines whether the finch is at the top
//             * or the bottom of the bird cage. depending on its position it knows
//             * which direction to turn
//             */
//                public void turnWhichWay(){
//                    if(this.getBodyYCoordinate(_colorOfMainFinch)<85){
//                       
//                        // BACK UP A LITTLE BIT
//                        _finch1.setWheelVelocities(-100, -150);
//                        this.doFor(650);
//                        //move forward a bit
//                        _finch1.setWheelVelocities(100, 100);
//                        //turn right again so rear faces the wall
//                        _finch1.setWheelVelocities(100, -150);
//                        this.doFor(400);
//                        //increment xcoord so the finch will move further along the maze
//                        _presentXCoord = _presentXCoord+41;
//                        _direction = false;
//                       
//                    }
//                    if(this.getBodyYCoordinate(_colorOfMainFinch)>200){
//                        //perform left turn, using turn method in FInchCOntroller
//                        _finch1.setWheelVelocities(-150, -100);
//                        this.doFor(650);
//                        //move forward a bit
//                        _finch1.setWheelVelocities(100, 100);
//                        this.doFor(400);
//                        //turn left again so rear faces the wall
//                        _finch1.setWheelVelocities(-100, 150);
//                        this.doFor(400);
//                        //increment xcoord so the finch will move further along the maze
//                        _presentXCoord = _presentXCoord + 41;
//                        _direction = true;
//                    }
//                    _finch1.stopWheels();
//                    this.doFor(300);
//                   
//                   
//                   
//                }
////
////
//}