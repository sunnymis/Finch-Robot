package finchcam.shared;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.rmi.RemoteException;

public class FinchInfo /* extends UnicastRemoteObject */ implements Serializable  {

	public FinchInfo() throws RemoteException {
		super();
	}
	
	private static final long serialVersionUID = 1L;
	//public String identifier;
	//public Integer x;
	//public Integer y;
	//public float angle;
	
	//public String toString() {
	//	return String.format("Id: %s. Loc: (%d, %d). Angle: %f", identifier, x, y, angle);
	//}
	
    public Color _beakColor=Color.red;
    public Point _bodyLocation=new Point(0,0);
    public Point _beakLocation=new Point(0,0);

}