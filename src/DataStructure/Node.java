package DataStructure;

public class Node {
	private int index;
	private int x;
	private int y;
	
	public Node(){
		this(0, 0, 0);
	}
	
	public Node(int index, int x, int y){
		this.index = index;
		this.x = x;
		this.y = y;
	}
	
	public int getIndex(){
		return index - 1;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public double getDistance(Node object){
		int deltaX = this.x - object.getX();
		int deltaY = this.y - object.getY();
		return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
	}
}
