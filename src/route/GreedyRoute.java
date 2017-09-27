package route;

import DataStructure.Node;
import DataStructure.TSPPath;

import java.util.Arrays;

public class GreedyRoute implements Route {
	private TSPPath path;
	
	public GreedyRoute(TSPPath path){
		this.path = path;
		SearchRoute();
	}
	
	public void SearchRoute(){
		TSPPath temp = new TSPPath();
		boolean gone[] = new boolean[path.NumOfCity()];
		Arrays.fill(gone, true);
		Node start = leftHandedStartNode();
		temp.addNode(start);
		gone[start.getIndex()] = false;
		for(int count = 0; count < path.NumOfCity() - 1; count++){
			int index = temp.getNode(count).getIndex();
			Node minNode = new Node();
			double min = Double.MAX_VALUE;
			Node x = path.getNode(index);
			gone[index] = false;
			for(int i = 0; i < path.NumOfCity(); i++){
				Node y = path.getNode(i);
				if(gone[i] && (x.getDistance(y) < min) && (count != i)){
					min = x.getDistance(y);
					minNode = y;
				}
			}
			temp.addNode(minNode);
			gone[minNode.getIndex()] = false;
		}
		path = temp;
	}
	
	private Node leftHandedStartNode(){
		Node startNode = new Node();
		for(int i = 0; i < path.NumOfCity(); i++){
			double max = 0;
			for(int j = 0; j < path.NumOfCity(); j++){
				Node x = path.getNode(i);
				double distance = x.getDistance(path.getNode(j));
				if((i != j) && (distance > max)){
					startNode = x;
					max = distance;
				}
			}
		}
		return startNode;
	}
	
	@Override
	public TSPPath getRoute() {
		return path;
	}
}
