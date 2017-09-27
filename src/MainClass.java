import DataStructure.TSPPath;
import route.GreedyRoute;
import route.Route;

public class MainClass {
	public static void main(String[] args){
		DataManager manager = new DataManager("pbd984.tsp");
		Route gr = new GreedyRoute(manager.getData());
		long start = System.currentTimeMillis();
		//calculate SE
		TSPPath temp = SE(gr.getRoute(), 100, 0.99);
		manager.setData(temp);
		long end = System.currentTimeMillis();
		//print and write result to file
		System.out.println((end - start) / 1000.0);
		manager.writeResult();
		System.out.println(temp.getDistance());
	}
	
	public static TSPPath SE(TSPPath currentPath, double temperature, double deltaTemperature){
		TSPPath best = new TSPPath(currentPath);
		
		while(temperature > 1) {
			TSPPath now = new TSPPath(best);
			now.swap();
			
			double currentEnergy = currentPath.getDistance();
			double neighbourEnergy = now.getDistance();
			
			if (acceptanceProbability(currentEnergy, neighbourEnergy, temperature) > Math.random()) {
				currentPath = new TSPPath(now);
			}
			
			if(currentPath.getDistance() < best.getDistance()){
				best = new TSPPath(currentPath);
			}
			
			temperature *= deltaTemperature;
		}
		return best;
	}
	
	public static double acceptanceProbability(double cEnergy, double nEnergy, double temperature){
		if(nEnergy < cEnergy){
			return 1.0;
		}
		return Math.exp((cEnergy - nEnergy) / temperature);
	}
}
