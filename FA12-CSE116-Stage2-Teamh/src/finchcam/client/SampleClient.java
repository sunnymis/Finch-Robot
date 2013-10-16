package finchcam.client;

import java.util.HashMap;

import finchcam.shared.FinchInfo;
import finchcam.shared.FinchTracker;

public class SampleClient {

	public static void main(String[] args) {
		FinchCamService fcs = new FinchCamService(FinchTracker.HOST);
		while(true) {
			try {
				System.out.println("DATA FROM FinchCamService: ");
				HashMap<String,FinchInfo> map = fcs.objects();
				System.out.println(map.keySet());
				for(String f: map.keySet()) {
					FinchInfo v = map.get(f);
					System.out.println(String.format("%s --> \tLocation: %d x %d",f, v._beakLocation.x, v._beakLocation.y));
				}
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
