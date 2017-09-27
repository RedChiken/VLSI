package opt;

import DataStructure.Node;
import java.util.ArrayList;

public class ThreeOpt implements Opt {
	boolean again = false;
	@Override
	public void Swap(ArrayList<Node> node) {
		int index1 = (int)(Math.random() * node.size());
		int index2 = (int)(Math.random() * node.size());
		int index3 = (int)(Math.random() * node.size());
		if(again){
			swapLeft(node, index1, index2, index3);
		}
		else{
			swapRight(node, index1, index2, index3);
		}
	}

	public void toggleDirection(){
		this.again = !this.again;
	}
	private void swapLeft(ArrayList<Node> node, int index1, int index2, int index3){
		Node temp = node.get(index1);
		node.set(index1, node.get(index2));
		node.set(index2, node.get(index3));
		node.set(index3, temp);
	}
	
	private void swapRight(ArrayList<Node> node, int index1, int index2, int index3){
		Node temp = node.get(index1);
		node.set(index1, node.get(index3));
		node.set(index3, node.get(index2));
		node.set(index2, temp);
	}
}
