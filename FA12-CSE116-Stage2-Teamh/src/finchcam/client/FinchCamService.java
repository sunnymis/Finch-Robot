package finchcam.client;

import java.awt.Point;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.List;
import finchcam.shared.FinchInfo;
import finchcam.shared.FinchTracker;

public class FinchCamService {

	private FinchTracker _server;
	
	public FinchCamService(String HOST) {
		Registry r = null;
		try {
			System.out.print("Looking for RMI registry running on host "+HOST+" on port "+FinchTracker.PORT+"...");
			r = LocateRegistry.getRegistry(HOST, FinchTracker.PORT);
			System.out.println("success!  I found the registry.");
		}
		catch (RemoteException e) {
			System.err.println("failure due to RemoteException: "+e.getMessage()+".  I could not find the RMI registry.  Sorry.");
			System.exit(1);
		}
		try {
			System.out.print("Looking for "+FinchTracker.SERVER_NAME+"...");
//			System.out.println("Names in registry: ");
//			for (String s : r.list()) {
//				System.out.println("\t NAME = "+s);
//			}
			_server = (FinchTracker) r.lookup(FinchTracker.SERVER_NAME);
//			System.out.println("_server = '"+_server+"'.");
//			System.out.println("SERVER'S NAME: " + _server.getName());
			System.out.println("success!  I found the server.");
		}
		catch (NotBoundException e) {
			System.err.println("failure due to NotBoundException: "+e.getMessage()+".  "+FinchTracker.SERVER_NAME+" is not bound in the RMI registry.  Sorry.");
			e.printStackTrace();
			System.exit(1);
		}
		catch (AccessException e) {
			System.err.println("failure due to AccessException: "+e.getMessage()+".  Sorry.");
			e.printStackTrace();
			System.exit(1);
		}
		catch (RemoteException e) {
			System.err.println("failure due to RemoteException: "+e.getMessage()+".  Sorry.");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public HashMap<String, FinchInfo> objects() {
		if (_server == null) {
			System.err.println("You called the finches() method, but I am not connected to the remote Finch Cam Service.  Sorry.");
			return null;
		}
		else {
			try {
				return _server.objects();
			}
			catch (RemoteException e) {
				System.err.println("failure due to RemoteException: "+e.getMessage()+".  Sorry.");
				e.printStackTrace();
				System.exit(1);
				return null;
			}
		}
	}
	public List<FinchInfo> finches() {
		if (_server == null) {
			System.err.println("You called the finches() method, but I am not connected to the remote Finch Cam Service.  Sorry.");
			return null;
		}
		else {
			try {
				return _server.finches();
			} catch (RemoteException e) {
				System.err.println("I could not find the remote Finch Cam Service.  Sorry.");
				e.printStackTrace();
				return null;
			}
		}
	}

	public List<Point> nonFinches() {
		if (_server == null) {
			System.err.println("You called the nonFinches() method, but I am not connected to the remote Finch Cam Service.  Sorry.");
			return null;
		}
		else {
			try {
				return _server.nonFinches();
			} catch (RemoteException e) {
				System.err.println("I could not find the remote Finch Cam Service.  Sorry.");
				return null;
			}
		}
	}
}
