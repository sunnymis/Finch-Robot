package finchcam.shared;

import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

public interface FinchTracker extends Remote {
	public static final int PORT = 50000;
	public static final String SERVER_NAME = "FinchCamService";
	public static final String HOST = "ROBOT.cse.buffalo.edu";
	public String getName() throws RemoteException;
    public List<FinchInfo> finches() throws RemoteException;
    public List<Point> nonFinches() throws RemoteException;
    public HashMap<String, FinchInfo> objects() throws RemoteException;
    //public Collection<FinchInfo> object_list() throws RemoteException;
}