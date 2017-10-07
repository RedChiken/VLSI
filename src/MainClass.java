import cooling.*;
import dataStructure.TSPPath;
import opt.ThreeOpt;
import opt.TwoOpt;
import route.GreedyRoute;
import route.RandomRoute;
import route.Route;

public class MainClass {
	public static void main(String[] args){
		DataManager manager = new DataManager("pbd984.tsp");
		Route gr = new GreedyRoute(manager.getData());
		//gr.getRoute().setOpt(new TwoOpt());
		long start = System.currentTimeMillis();
		CoolingFunction coolingFunction[] = {new ExponentialCooling(),
				new LinearCooling(),
				new LogarithmicalCooling(),
				new QuadraticCooling()};
		//calculate SE
		SimulatedAnnealing SA = new SimulatedAnnealing(100, 0.99, coolingFunction[0]);
		TSPPath temp = SA.run(gr.getRoute());
		manager.setData(temp);
		long end = System.currentTimeMillis();
		//print and write result to file
		System.out.println("\n" + (end - start) / 1000.0);
		manager.writeResult();
		System.out.println(temp.getDistance());
	}
}
