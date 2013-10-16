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
    // THIS IS THE COLOR OF THE CARD ON THE BACK OF FINCH 1
    private String _colorOnFinchF1;
    // THIS IS THE COLOR OF THE CARD ON THE BACK OF FINCH 1
    private String _colorOnFinchF2;
    // THIS IS THE COLOR OF THE CARD ON THE BACK OF FINCH 1
    private String _colorOnFinchF3;
    // THIS IS THE COLOR OF THE CARD ON THE BACK OF FINCH 1
    private String _colorOnFinchF4;
    
	// MAX AND MIN VALUES OF THE WEBCAM COORDINATES
    private int _xMin;
    private int _xMax;
    private int _yMin;
    private int _yMax;
    
   
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
    	// THIS CAN BE CHANGED TO MATCH THE ACTUAL COLOR OF THE PAPER ON TOP OF THE FINCH
    	// ***IMPORTANT*** THESE COLORS DESIGNATE THE ORDER OF THE FINCHES WITHIN THE PROGRAM
    	_colorOnFinchF1 = "blue";
    	_colorOnFinchF2 = "green";
    	_colorOnFinchF3 = "pink";
    	_colorOnFinchF4 = "red";
    	
    	// FURTHER FROM THE DOOR
        _xMin = 18;
        // CLOSER TO THE DOOR
        _xMax = 315;
        // CLOSER TO THE BOARD
        _yMin = 70;
        // FURTHER FROM THE BOARD
        _yMax = 220;
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
	 * @return an integer X-Coordinate of the body of the finch
	 */
	public int getBodyXCoordinate(char c){
		HashMap<String,FinchInfo> map = _cam.objects();
		// WE INITIALIZE THE VALUE TO 1 TO BEGIN WITH, DEFAULT IS THE LEAD FINCH
		FinchInfo i = map.get(_colorOnFinchF1);
		switch(c){
		// WE SWITCH AROUND BLUE GREEN PINK AND RED
		case 'b': 	i = map.get(_colorOnFinchF1); break;
		case 'g':	i = map.get(_colorOnFinchF2); break;
		case 'p':	i = map.get(_colorOnFinchF3); break;
		case 'r':	i = map.get(_colorOnFinchF4); break;
		}
		return i._beakLocation.x;
	}
	
	/**
	 * This method gets the Y-Coordinate of the body of the finch
	 * @return an integer Y-Coordinate of the body of the finch
	 */
	public int getBodyYCoordinate(char c){
		HashMap<String,FinchInfo> map = _cam.objects();
		// WE INITIALIZE THE VALUE TO 1 TO BEGIN WITH, DEFAULT IS THE LEAD FINCH
		FinchInfo i = map.get(_colorOnFinchF1);
		switch(c){
		// WE SWITCH AROUND BLUE GREEN PINK AND RED
		case 'b': 	i = map.get(_colorOnFinchF1); break;
		case 'g':	i = map.get(_colorOnFinchF2); break;
		case 'p':	i = map.get(_colorOnFinchF3); break;
		case 'r':	i = map.get(_colorOnFinchF4); break;
		}
		return i._beakLocation.y;
	}
	
	/**
	 * This method turns the finch a certain number of degrees, about the point between both wheels
	 * @param degrees - The number of degrees that the finch will turn, where left turn is positive
	 */
	private void turn(int degrees){

		
		// ENSURES THAT THE WHEELS ARE STOPPED PRIOR TO TURNING
		_finch1.stopWheels();
		
		// .3 SECOND BUFFER
		this.doFor(0);
		
		// THIS ROTATES THE FINCH, BASED ON THE DEGREES ARGUMENT
		if (degrees >= 0){
			int amount = 9 * degrees;
			_finch1.setWheelVelocities(-109, 102, amount);
		}
		else{
			int amount = -9 * degrees;
			_finch1.setWheelVelocities(102, -103, amount);
		}
		
		_finch1.stopWheels();
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
				this.turn(-5);	
			}
		}
		else {
			while (_finch1.getLeftLightSensor() > _finch1.getRightLightSensor()){
				this.turn(5);
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
	    				this.turn(number);
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
	    				this.turn(number);
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
    
    
    
    
    // WE NEED TO CREATE A REFERENCE TO THIS (THE FINCH CONTROLLER) TO USE IT IN INNER CLASSES OF THIS METHOD
	FinchController _fc = this;
	// WE ALSO NEED REFERENCES TO THE PREVIOUS X AND Y COORDINATES
	ArrayList<Integer> _leaderXCoordinateHistory;
	ArrayList<Integer> _leaderYCoordinateHistory;
    public void convoy(){
    	
    	_leaderXCoordinateHistory = new ArrayList<Integer>();
    	_leaderYCoordinateHistory = new ArrayList<Integer>();
    	
    	// SETTING THE COLOR OF THE BEAKS OF THE FINCHES TO MATCH THE COLOR ON THIER BACKS
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
		
		// GET THE COORDINATES FOR THE BLUE FINCH, THE LEADER
		_leaderXCoordinateHistory.add(this.getBodyXCoordinate('b'));
		_leaderYCoordinateHistory.add(this.getBodyYCoordinate('b'));
		System.out.println("X: " + _leaderXCoordinateHistory.get(0) + "  Y: " + _leaderYCoordinateHistory.get(0));
		
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
			
				// AS LONG AS WE HAVE ANOTHER FINCH
				if (!(_finch2 == null)){
					//while(_fc.getBodyXCoordinate('g')< leaderPreviousX){
						
					//}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				_finch1.stopWheels();
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
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // Code that Matt and Sunny developed for the traversing the maze

//    public boolean traverseMaze(){
//
//    	int range = 20;
//    	
//    	int _xcoord = 1;
//    			
//    		// The perch is position (0,0)
//    		// Going up along the short side, the finch would end up at (0,10)
//    		// etc...
//    		// The corner directly diagonal from the perch is position (10,10)
//    			
//    			
//    			double[][] array = new double[10][10];
//    			
//    			
//    			
//    			// PAUSE FOR .5 SECONDS BEOFORE PROGRAM EXECUTION
//    			this.doFor(500);
//    			
//    			//turn left then move forward along shorter side
//    			_finch.setWheelVelocities(0,200);
//    			_finch.setLED(0, 200, 0);
//    			_finch.setWheelVelocities(120, 150);
//    			
//    			// MOVE FOR AT LEAST .5 SECONDS BEFORE DETECTING A WALL
//    			this.doFor(500);
//    			
//    			// WHILE THERE IS NOT AN OBSTACLE (CHECKS EVERY .1 SECONDS)
//    			while (!_finch.isObstacle()){
//    				this.doFor(100);
//    			}
//    			
//    			// AFTER IT SENSES AN OBSTALCLE, THIS DELAY ACTS AS A BUFFER SO THE ROBOT CAN CHECK TO SEE IF 
//    			// IT BOTH SENSORS DETACT AN OBSTACLE
//    			this.doFor(300);
//    			
//    			// IF IT HITS A CORNER, DO THIS
//    			if (_finch.isObstacleRightSide()){
//    				
//    				// CHANGE COLOR OF BEAK, STOP, AND WAIT FOR .4 SECONDS
//    				_finch.stopWheels();
//    				_finch.setLED(0, 0, 200);
//    				this.doFor(400);
//    				
//    				// BACK UP A LITTLE BIT
//    				//****** This code my need to be reversed because
//    				//now the finch is traveling using the perimeter code
//    				//and its left side is facing the wall rather than the
//    				//right side 
//    				_finch.setWheelVelocities(-100, -150);
//    				this.doFor(650);
//    				
//    				// PERFORM THE TURN
//    				_finch.setWheelVelocities(-100, 150);
//    				this.doFor(400);
//    				
//    			}
//    			
//    			// IF IT'S NOT AT A CORNER, BUT RATHER ALONG A WALL
//    			else{
//    				
//    				// BACK UP A LITTLE BIT
//    				_finch.setWheelVelocities(-150, -130);
//    				this.doFor(400);
//    				
//    				// TURN AWAY FROM THE WALL
//    				_finch.setWheelVelocities(-90, 90);
//    				this.doFor(280);
//    			}
//    			while(this.getBodyXCoordinate()<1){
//    				_finch.setWheelVelocities(150, 120);
//    				this.doFor(400);
//    			}
//    			if(this.getBodyXCoordinate()>1){
//    				// CHANGE COLOR OF BEAK, STOP, AND WAIT FOR .4 SECONDS
//    				_finch.stopWheels();
//    				_finch.setLED(0, 0, 200);
//    				this.doFor(400);
//    				
//    				// BACK UP A LITTLE BIT
//    				_finch.setWheelVelocities(-100, -150);
//    				this.doFor(650);
//    				
//    				// PERFORM THE TURN,
//    				_finch.setWheelVelocities(-100, 150);
//    				this.doFor(400);
//    			}
//    			
//    			
//    			/**
//    			 * Now the finch is facing downwards at coordinate (1,10)
//    			 * it moves along the x axis until it reaches (1,0)
//    			 * then it'll 
//    			 */
//    			//not sure if we need the following; 
//    			
//    			while(this.getBodyXCoordinate() <= _xcoord +1){
//    				_finch.setWheelVelocities(150,120);
//    				this.doFor(400);
//    			}
//    			
//    			_finch.stop();
//    			_this.doFor(200);
//    			_finch.turnWhichWay();
//    			
//    			while(this.getBodyYCoordinate() != 10 && this.getBodyXCoordinate() != 10){
//    				_finch.moveAlongY();
//    			}	
//    				
//    		return array;
//    	}
//    		/**
//    		 * Move the finch along the Y axis. if it goes over the buffer
//    		 * zone then it knows to stop, back up and turn back in the correct
//    		 * direction		
//    		 * @param _xcoord 
//    		 * 				- the coordinate we ideally want to be on
//    		 * getXCoordinate() is the current position of the finch
//    		 */
//    			
//    		public void moveAlongY(Integer _xcoord){
//    			double buffer = 0.2;
//    			//if(_finch.getYCOordinate()<topOrBottom){
//    				while(_finch.getBodyXCoordinate()> _xcoord - buffer && _finch.getBodyXCoordinate()< _xcoord + buffer){
//    					_finch.setWheelVelocities(150,120);
//    				}
//    				_finch.stop();
//    				//_finch.getYCoordinate() && _finch.getXCoordinate();
//    				if(_xcoord.compareTo(this.getBodyXCoordinate()) > 0){
//    					//we are currently under our desired x coordinate 
//    					//turn left a bit, then move. do for certain time
//    					_finch.moveAlongY();
//    				}
//    				if(_xcoord.compareTo(this.getBodyXCoordinate()) < 0){
//    					//we are currently over our desired x coordinate 
//    					//turn right bit, then move. do for certain time
//    					_finch.moveAlongY();
//    				}
//    				if(_finch.isObstacleLeftSide() && _finch.isObstacleRightSide()){
//    					this.turnWhichWay();
//    				}
//    				
//    		}
//    		
//    		/**
//    		 * This method determines whether the finch is at the top 
//    		 * or the bottom of the bird cage. depending on its position it knows
//    		 * which direction to turn
//    		 */
//    			public void turnWhichWay(){
//    				if(_finch.getBodyYCoordinate()>8){
//    					//perform right turn, using turn method in FInchCOntroller
//    					_xcoord = _xcoord+1;
//    					
//    				}
//    				if(_finch.getBodyYCoordinate()<2){
//    					//perform left turn, using turn method in FInchCOntroller
//    					_xcoord = _xcoord + 1;
//    				}
//    				
//    				
//    			}
//    			
//    	/**
//    	 * If a heat source were to be found the finch should stop, find the
//    	 * x and y coordinate and store it in the array
//    	 */
//    	}
//
//
}