package dataStructure;

import opt.Opt;
import opt.ThreeOpt;

import java.util.ArrayList;

public class TSPPath {
	private ArrayList<Node> path;
	private Opt opt;
	
	public TSPPath(TSPPath node, Opt opt){
		this.setPath(node.path);
		this.opt = opt;
	}
	
	public TSPPath(){
		this(new ThreeOpt());
	}
	
	public TSPPath(TSPPath node){
		this(node, new ThreeOpt());
	}
	
	public TSPPath(Opt opt){
		this.opt = opt;
		path = new ArrayList<>();
	}
	
	public void addNode(Node node){
		path.add(node);
	}
	
	public void deleteNode(Node node){
		path.remove(node);
	}
	
	public Node getNode(int index){
		return path.get(index);
	}
	
	public void setPath(ArrayList<Node> node){
		this.path = node;
	}
	
	public void setOpt(Opt opt){
		this.opt = opt;
	}
	
	public int NumOfCity(){
		return path.size();
	}
	
	public double getDistance(){
		double distance = 0;
		for(int i = 0; i < path.size() - 1; i++){
			distance += path.get(i).getDistance(path.get(i + 1));
		}
		return distance;
	}
	
	public void swap(){
		path = opt.Swap(path);
	}
}
