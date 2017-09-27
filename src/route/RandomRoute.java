package route;

import DataStructure.TSPPath;

import java.util.Arrays;

public class RandomRoute implements Route {
	private TSPPath path;
	
	public RandomRoute(TSPPath path){
		this.path = path;
	}
	
	private void Mix(){
		boolean gone[] = new boolean[path.NumOfCity()];
		Arrays.fill(gone, true);
		TSPPath temp = new TSPPath();
		while(temp.NumOfCity() == path.NumOfCity()){
			int index = (int)(Math.random() * path.NumOfCity());
			if(gone[index]){
				temp.addNode(path.getNode(index));
				gone[index] = false;
			}
		}
		path = temp;
	}
	
	@Override
	public TSPPath getRoute() {
		Mix();
		return this.path;
	}
}
