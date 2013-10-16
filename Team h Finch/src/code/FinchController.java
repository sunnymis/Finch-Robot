package code;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;

import edu.cmu.ri.createlab.terk.robot.finch.FinchInterface;

/**
 * FinchController contains all of the methods that are
 * called on the Finch Robot to make it perform its various actions as well
 * as stopping them.
 *
 */
public class FinchController {

	// DECLARATION OF INSTANCE VARIABLES
	private FinchInterface _finch;
	private ArrayList<ArrayList<Double>> _temperatureCoordinates;
	
	
	/**
	 * This is the constructor for the FinchController object. It is in an association relationship with 
	 * an object of type FinchInterface
	 * @param finch - An object that is of type FinchInterface, to be in an association relationship with the
	 * FinchController object.
	 */
	public FinchController(FinchInterface finch){
		
		_finch = finch;
		_temperatureCoordinates = new ArrayList<ArrayList<Double>>();
		
		//THIS IS HOW WE WILL ACCESS THE X AND Y COORDINATES OF THE ARRAY STORED INT THE ROBOT 
//		_coordinates.get(0).get(0);
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
				_finch.stopWheels();
				_finch.quit();
				System.exit(0);
			}
		});
		
		// ADDS BUTTON TO JFRAME AND MAKES THE JFRAME VISIBLE
		j.add(b);
		j.setVisible(true);
		j.pack();
	}
	
	
	
	/**
	 * This method moves the finch forward for a certain amount of time at a set speed. It does not
	 * keep track of the finch's motion.
	 * @param time - The amount of time that the robot will move forward, in milliseconds
	 */
	 //THIS METHOD MIGHT CHANGE RETURN TYPE VOID TO SOMETHING ELSE
	 //WHY? WE WANT THE FINCH TO TRACK IT SELF.
	private void moveForwardTimeNoTrack(int time){
		
		_finch.setWheelVelocities(150, 150, time);
	}
	
	
	
	/**
	 * This method returns a reference to an object of type FinchInterface
	 * @return - The FinchInterface that is returned
	 */
	// THIS METHOD GIVES US A REFERENCE TO THE FINCH SO THAT WE CAN EASILY CALL METHODS
	// DIRECTLY ON THE FINCH DURING TESTING
	private FinchInterface getFinch(){
		return _finch;
	}
	
	
	
	
	/**
	 * This method moves the finch forward until it reaches an obstacle. Then it stops. It does not
	 * keep track of the finch's motion.
	 */
	private void moveUntilObstalceNoTrack(){
		
		// COUNTER IS A BUFFER SO THAT THERE ACTUALLY HAS TO BE AN OBSTACLE AND NOT A MISREAD
		int counter = 0;
		_finch.setWheelVelocities(150, 150);
		
		while (counter < 3){
			if (_finch.isObstacle()){
				counter = counter + 1;
				System.out.println(counter);
				this.doFor(100);
			}
		}
		_finch.stopWheels();
	}

	
	
	
	/**
	 * This method simply pauses the program for a specified time, while allowing the finch to continue 
	 * functioning
	 * @param time - The time in milliseconds that the program will wait
	 */
	private void doFor(int time){
		_finch.sleep(time);
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
		 _finch.setWheelVelocities(-100, 100);
		 this.doFor(600);
		 _finch.stopWheels();
		 _finch.setLED(200,0,0);
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
		
		_finch.setLED(0, 200, 0);
		_finch.setWheelVelocities(150, 120);
		
		// MOVE FOR AT LEAST .5 SECONDS BEFORE DETECTING A WALL
		this.doFor(500);
		
		// WHILE THERE IS NOT AN OBSTACLE (CHECKS EVERY .1 SECONDS)
		while (!_finch.isObstacle()){
			this.doFor(100);
		}
		
		// AFTER IT SENSES AN OBSTALCLE, THIS DELAY ACTS AS A BUFFER SO THE ROBOT CAN CHECK TO SEE IF 
		// IT BOTH SENSORS DETACT AN OBSTACLE
		this.doFor(300);
		
		// IF IT HITS A CORNER, DO THIS
		if (_finch.isObstacleLeftSide()){
			
			// CHANGE COLOR OF BEAK, STOP, AND WAIT FOR .4 SECONDS
			_finch.stopWheels();
			_finch.setLED(0, 0, 200);
			this.doFor(400);
			
			// BACK UP A LITTLE BIT
			_finch.setWheelVelocities(-100, -150);
			this.doFor(650);
			
			// PERFORM THE TURN
			_finch.setWheelVelocities(-100, 150);
			this.doFor(400);
			corner = corner + 1;
		}
		
		// IF IT'S NOT AT A CORNER, BUT RATHER ALONG A WALL
		else{
			
			// BACK UP A LITTLE BIT
			_finch.setWheelVelocities(-150, -130);
			this.doFor(400);
			
			// TURN AWAY FROM THE WALL
			_finch.setWheelVelocities(-90, 90);
			this.doFor(280);
		}
		
		// RETURNS AN INT VALUE, 0 IF IT HIT A WALL, 1 IF IT HIT A CORNER
		return corner;
	}
	
	
	
	/**
	 * This method turns the finch a cetain number of degrees, about the point between both wheels
	 * @param degrees - The number of degrees that the finch will turn, where left turn is positive
	 */
	private void turn(int degrees){
		
		// ENSURES THAT THE WHEELS ARE STOPPED PRIOR TO TURNING
		_finch.stopWheels();
		
		// .3 SECOND BUFFER
		this.doFor(0);
		
		// THIS ROTATES THE FINCH, BASED ON THE DEGREES ARGUMENT
		if (degrees >= 0){
			int amount = 9 * degrees;
			_finch.setWheelVelocities(-109, 102, amount);
		}
		else{
			int amount = -9 * degrees;
			_finch.setWheelVelocities(102, -103, amount);
		}
		
		_finch.stopWheels();
		this.doFor(10);
	}
	
	
	
	
	
	
	/**
	 * This method commands the finch to move underneath the brightest spot in the birdcage
	 * @return integer of the value of the finch's light sensors when the program is completed
	 */
	public int findBrightLight(){
		
		// SETS THE BEAK COLOR TO GREEN
		_finch.setLED(0, 200, 0);
		
		// THIS IS THE VLAUE THAT THE LIGHT SENSOR NEEDS TO REACH IN ORDER TO STOP THE FINCH
		int maxLightSensorValue = 234;
		
		 // WHILE WE'RE NOT UNDER THE BRIGHT LIGHT
		while (_finch.getLeftLightSensor() < maxLightSensorValue || _finch.getRightLightSensor() < maxLightSensorValue){
		
			// IF THERE'S AN OBSTACLE, PERFORM A CERTAIN ACTION (BACK AWAY). THIS IS A PRIVATE HELPER METHOD CALL
			if (_finch.isObstacle()){
				_finch.setWheelVelocities(-150,-150);
				this.doFor(400);
			}
			
			// ELSE, MOVE TOWARDS THE LIGHT
			else{
				this.findBrightLightOpenSpace();
			}
		}
		
		// MOVES THE FINCH FORWARD TO CENTER IT UNDER THE LIGHT AT THE END
		_finch.setWheelVelocities(100, 106);
		this.doFor(350);
		_finch.stopWheels();
		
		// ONCE THE FINCH HAS FOUND THE BRIGHT LIGHT, SET THE COLOR OF THE BEAK TO BLUE
		_finch.setLED(0, 0, 200);
		return maxLightSensorValue;
	}
	/**
	 * This helper method moves the finch closer to the light source
	 */
	private void findBrightLightOpenSpace(){
		
		// THIS IS THE CODE FOR GETTING THE FINCH TO FACE THE BRIGHT LIGHT
		if (_finch.getLeftLightSensor() < _finch.getRightLightSensor()){
			while (_finch.getLeftLightSensor() < _finch.getRightLightSensor()){
				this.turn(-5);	
			}
		}
		else {
			while (_finch.getLeftLightSensor() > _finch.getRightLightSensor()){
				this.turn(5);
			}
		}
		
		// MOVES THE ROBOT FORWARD A LITTLE BIT ONCE IT'S DONE TURNING SO THAT IT DOESN'T GET STUCK MOVING BACK AND FORTH, THESE VALUES REFLECT THE FACT THAT THE WHEEL VELOCITIES ARE DIFFERENT
		_finch.setWheelVelocities(100, 106);
		this.doFor(250);
		
		// INITIALIZES THE INTEGERS WHICH KEEP TRACK OF THE PREVIOUS LIGHT VALUES
		int leftLightSensorValue = _finch.getLeftLightSensor();
		int rightLightSensorValue = _finch.getRightLightSensor();
		
		// WHILE THE NEW VALUES ARE GREATER THAN THE PREVIOUS ONES, MOVE FORWARD
		while ((_finch.getLeftLightSensor() > leftLightSensorValue) && (_finch.getRightLightSensor() > rightLightSensorValue)){
			
			// IF THERE'S AN OBSTACLE, STOP MOVING FORWARD
			if (_finch.isObstacle()){
				break;
			}
			
			// MOVE THE FINCH FORWARD, THESE VALUES REFLECT THE FACT THAT THE WHEEL VELOCITIES ARE DIFFERENT
			_finch.setWheelVelocities(100,105);
			this.doFor(300);
			
			// UPDATES THE VALUES OF THE LIGHT SENSORS
			leftLightSensorValue = _finch.getLeftLightSensor();
			rightLightSensorValue = _finch.getRightLightSensor();	
		}
		
		_finch.stopWheels();
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
			 _finch.setWheelVelocities(-100, 100);
			 this.doFor(600);
			 _finch.stopWheels();
			 _finch.setLED(200,0,0);
		}
		/**
		 * This is a helper method for the traversePerimeter method.
		 * @return - An int with value 0 if the finch senses a parallel wall, value 1 if it senses a corner
		 */
		private int drawPerimeterHelperMethod(){
			
			int corner = 0;
			
			// PAUSE FOR .5 SECONDS BEOFORE PROGRAM EXECUTION
			this.doFor(500);
			
			_finch.setLED(0, 200, 0);
			_finch.setWheelVelocities(150, 120);
			
			// MOVE FOR AT LEAST .5 SECONDS BEFORE DETECTING A WALL
			this.doFor(500);
			
			// WHILE THERE IS NOT AN OBSTACLE (CHECKS EVERY .1 SECONDS)
			while (!_finch.isObstacle()){
				this.doFor(100);
			}
			
			// AFTER IT SENSES AN OBSTALCLE, THIS DELAY ACTS AS A BUFFER SO THE ROBOT CAN CHECK TO SEE IF 
			// IT BOTH SENSORS DETACT AN OBSTACLE
			this.doFor(300);
			
			// IF IT HITS A CORNER, DO THIS
			if (_finch.isObstacleLeftSide()){
				
				// CHANGE COLOR OF BEAK, STOP, AND WAIT FOR .4 SECONDS
				_finch.stopWheels();
				_finch.setLED(0, 0, 200);
				this.doFor(400);
				
				// BACK UP A LITTLE BIT
				_finch.setWheelVelocities(-100, -150);
				this.doFor(650);
				
				// PERFORM THE TURN
				_finch.setWheelVelocities(-100, 150);
				this.doFor(400);
				corner = corner + 1;
			}
			
			// IF IT'S NOT AT A CORNER, BUT RATHER ALONG A WALL
			else{
				
				// BACK UP A LITTLE BIT
				_finch.setWheelVelocities(-150, -130);
				this.doFor(400);
				
				// TURN AWAY FROM THE WALL
				_finch.setWheelVelocities(-90, 90);
				this.doFor(280);
			}
			
			// RETURNS AN INT VALUE, 0 IF IT HIT A WALL, 1 IF IT HIT A CORNER
			return corner;
		}
		











	//-----------------------------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------
	// -----------------------INSERT FINISHED METHODS ABOVE, UNFINISHED BELOW------------------------------
	//-----------------------------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * This method causes the finch to traverse the entire maze
	 */
	// MAKE SURE TO START THIS METHOD WITH THE FINCHES BACK END AGAINST THE WALL
	
	private void traverseEntireMaze(){
	
	// THIS IS THE STEP SIZE OF THE FINCH
	int moveTime = 100;
	
	// THIS IS THE SPEED AT WHICH THE FINCH MOVES FORWARD
	int leftMoveSpeed = 102;
	
	// THIS IS THE SPEED AT WHICH THE FINCH MOVES FORWARD
	int rightMoveSpeed = 106;
	// THIS INT WILL TELL US WHETHER OR NOT THE FINCH HAS REACHED THE END OF THE CAGE OR NOT
	int reachedEnd = 0;
	
	// THIS FLIP-FLOPPING BOOLEAN TELLS US WHETHER OR NOT TO MOVE TOWARDS OR AWAY FROM THE BOARD
	boolean movingAway = false;
	
	// THIS ORIENTATES THE FINCH SO THAT IT FACES THE PROJECTOR BOARD (LIKE PARKING A CAR)
	_finch.setWheelVelocities(0, 150, 550);
	_finch.setWheelVelocities(-150, -50, 500);
	_finch.setWheelVelocities(-150, -150, 500);
	
	// THIS CODE IS EXECUTED IS THE FINCH HAS NET YET ENCOUNTERED TWO CORNERS (AND THUS HASN'T TRAVELED THE ENTIRE CAGE)
		while (reachedEnd < 2){
			
			// THIS IF STATEMENT ENSURES THAT THE FINCH MOVES TOWARDS THE BOARD AND AWAY FROM THE BOARD
			if (movingAway){
				reachedEnd = reachedEnd + this.traverseEntireMazeAwayFromBoard(moveTime, leftMoveSpeed, rightMoveSpeed);
				movingAway = false;
			}
			else{
				reachedEnd = reachedEnd + this.traverseEntireMazeTowardsBoard(moveTime, leftMoveSpeed, rightMoveSpeed);
				movingAway = true;
			}
		}
	}
	
	
	private int traverseEntireMazeTowardsBoard(int moveTime, int leftMoveSpeed, int rightMoveSpeed){
		
		// THIS IS AN INTEGER THAT WILL (EVENTUALL) KEEP TRACK OF THE DISTANCE COVERED
		int distanceCovered = 0;
		
		// THIS TELLS THE PROGRAM WHETHER OR NOT IT HAS REACHED A CORNER
		int counter = 0;
		
		// BUFFER THAT GIVES THE ROBOT TIME TO RESET BEFORE IT ATTEMPTS TO READ THE VALUES OF THE SENSORS
		this.doFor(500);
		
		// INITIALIZES THE VARIABLE OBSTACLE, WHICH WILL TELL US IF WE'VE REACHED AN OBSTACLE OR NOT
		int obstacle = 0;
		
		// FINCH SHOULD MOVE UNTIL IT REACHES A WALL IN FRONT OF IT
		while(obstacle<2){
			if(_finch.isObstacle()){
				obstacle = obstacle + 1;
			}
			_finch.setWheelVelocities(leftMoveSpeed, rightMoveSpeed);
			this.doFor(moveTime);
			_finch.stopWheels();
			// THIS DISTACNECOVERED IS HOW MANY "GRID" VLAUES THE FINCH HAS GONE
			distanceCovered = distanceCovered + 1;
		}

		
		
		// PRINT OUT THE DISTANCE COVERED AND TURN THE ROBOT 90 DEGRESS
		_finch.setLED(200, 0, 200);
		System.out.println("The distance covered is: " + distanceCovered);
		
		// BACK AWAY FROM THE WALL
		_finch.setWheelVelocities(-100, -100);
		this.doFor(500);
		this.turn(-90);
		
		// MOVE FORWARD AFTER TURNING 90 DEGREES
		_finch.setWheelVelocities(leftMoveSpeed, rightMoveSpeed);
		this.doFor(1800);
		
		// TURN ANOTHER 90 DEGREES SO IT NOW FACES AWAY FROM THE BOARD
		this.turn(-93);
		
		// THIS POSITIONS THE BACK OF THE FINCH UP AGAINST THE WALL
		_finch.setWheelVelocities(-115, -130);
		this.doFor(650);
		_finch.setWheelVelocities(0, -120);
		this.doFor(300);
		_finch.setWheelVelocities(-150, -150);
		this.doFor(250);

		// PAUSES THE ROBOT BEFORE MOVING ONWARD
		_finch.stopWheels();
		this.doFor(500);
		return counter;
	}
	
	private int traverseEntireMazeAwayFromBoard(int moveTime, int leftMoveSpeed, int rightMoveSpeed){
	
		// THIS IS AN INTEGER THAT WILL (EVENTUALL) KEEP TRACK OF THE DISTANCE COVERED
		int distanceCovered = 0;
		
		// THIS TELLS THE PROGRAM WHETHER OR NOT IT HAS REACHED A CORNER
		int counter = 0;
		
		// BUFFER THAT GIVES THE ROBOT TIME TO RESET BEFORE IT ATTEMPTS TO READ THE VALUES OF THE SENSORS
		this.doFor(500);
		
		int obstacle = 0;
		
		// FINCH SHOULD MOVE UNTIL IT REACHES A WALL IN FRONT OF IT
		while(obstacle<2){
			if(_finch.isObstacle()){
				obstacle = obstacle + 1;
			}
			_finch.setWheelVelocities(leftMoveSpeed, rightMoveSpeed);
			this.doFor(moveTime);
			_finch.stopWheels();
			// THIS DISTACNECOVERED IS HOW MANY "GRID" VLAUES THE FINCH HAS GONE
			distanceCovered = distanceCovered + 1;
		}

		// PRINT OUT THE DISTANCE COVERED AND TURN THE ROBOT 90 DEGRESS
		_finch.setLED(200, 200 , 0);
		System.out.println("The distance covered is: " + distanceCovered);
		
		// BACK AWAY FROM THE WALL
		_finch.setWheelVelocities(-100, -100);
		this.doFor(500);
		this.turn(97);
		
		// MOVE FORWARD AFTER TURNING 90 DEGREES
		_finch.setWheelVelocities(leftMoveSpeed, rightMoveSpeed);
		this.doFor(1800);
		
		// TURN ANOTHER 90 DEGREES SO IT NOW FACES AWAY FROM THE BOARD
		this.turn(93);
		
		// THIS POSITIONS THE BACK OF THE FINCH UP AGAINST THE WALL
		_finch.setWheelVelocities(-130, -115);
		this.doFor(650);
		_finch.setWheelVelocities(-120, 0);
		this.doFor(300);
		_finch.setWheelVelocities(-150, -150);
		this.doFor(250);

		// PAUSES THE ROBOT BEFORE MOVING ONWARD
		_finch.stopWheels();
		this.doFor(500);
		return counter;
	}
	
	
	
	/**
	 * This methods moves the finch object forward a certain distance. It does not keep track 
	 * of the finch's motion.
	 * @param distance - The distance that the robot moves forward, in millimeters
	 */
	// WE MIGHT WANT TO CHANGE RETURN TYPE IF WE'RE KEEPING TRACK OF DISTANCE
	private void moveForwardDistanceNotrack(int distance){
		
		// WE NEED TO FIGURE OUT HOW FAR THE ROBOT GOES IN A CERTAIN AMOUNT OF TIME
		// AT A CERTAIN SPEED BERFORE WE CAN DEFINE THIS METHOD
	}
	
	

	
	
	// THIS METHOD IS NOT FINISHED YET
	private void faceWallCounterClockwise(){
		
		int counter = 0;
		_finch.setWheelVelocities(-100, 100);
		
		while (counter < 3){
			if (_finch.isObstacle()){
				counter = counter + 1;
				System.out.println(counter);
		// SLEEP IS A BUFFER SO THAT THE FINCH HAS TIME TO GET A NEW READING AND NOT THE SAME ONE TWICE
				_finch.sleep(100);
			}
		}
		_finch.stopWheels();
	}

	
	
	
	
	


	// Hector and Matt Search randomly
	//Wheels will set velocities to random speed by using the Random Generator Class in Java.

//	setRandomSpeed( ){
//	 while (! isObsticle()){
//	_finch.setVelocities(random,random);
//	}
//	//
//	public void RandomMotion( ){
//	setRandomSpeed();
//	if(isObsticleLeftSide() && ! isObstacleRightSide() ){
//
//	Move back the wheels at a setSpeed.
//	While(isObsticle){
//
//	setWheels(100,0,random time  500 to 2000);
//	setRandomSpeeds();
//	}
//
//	if(isObsticleRightSide() && ! isObsticleRightSide()){
//
//	moveBackwards();
//	while(isObstacle){
//	setWheels(0,100,random time 500 to 2000);
//	}
//	setRandomSpeeds();
//
//	}
//
//	}
//	}
//	}
	
	
// Matt traverse method
//public class travel {
//
//	private int _walls;
//	private FinchInterface _f;
//	
//	 
//	
//	public void travelAround(){
//		_walls =2;
//		while(_walls<8){
//			while(!isObsticleRightSide() && !isObstacleLeftSide()){
//				_f.setWheelVelocities(200,200,2000);
//				_f.doFor(1000);
//				//take light sensor reading
//			}
//			_walls = _walls +1;
//			_f.whichWay(_walls);
//		}
//	}
//	
//	
//	public void whichWay(int i){
//		if(i % 2 ==1){
//			_f.setWheelVelocities(-150,-50,1000);//reverse, turn left 90
//			_f.setWheelVelocities(50,50,1000);//move a bit
//			_f.setWheelVelocities(0,50,1000);//turn 90 again
//		}
//		if(i % 2 == 0){
//			_f.setWheelVelocities(-50,-150,1000);//reverse, turn right 90
//			_f.setWheelVelocities(50,50,1000);// move a bit
//			_f.setWheelVelocities(50,0,1000);//turn right 90
//		}
//	}
//}


}