package route;

import dataStructure.Node;
import dataStructure.TSPPath;

import java.util.*;

public class MelkmanRoute implements Route {
	private TSPPath path;
	
	public MelkmanRoute(TSPPath path){
		this.path = path;
		MelkMan();
	}
	@Override
	public TSPPath getRoute() {
		return path;
	}
	
	private ArrayList<Node> MelkMan(){
		ArrayList<ArrayList<Node>> orderedNode = new ArrayList<ArrayList<Node>>();
		ArrayList<Node> ret = new ArrayList<Node>();
		for(int i = 0; i < path.NumOfCity(); i++){
			fillOuterList(orderedNode, i);
		}
		ArrayList<Node>[] orderedNodeArray = moveToArray(orderedNode);
		int xSize = orderedNodeArray.length;
		//가장 왼쪽 추가
		ret.addAll(orderedNodeArray[0]);
		//가장 위쪽 추가
		int dStart = ret.size();
		for(int i = 1; i < xSize - 1; i++){
			addNodetoLastOfArray(ret, orderedNodeArray, i);
		}
		int dEnd = ret.size();
		
		//지붕의 웅덩이 제거
		ret = deleteConcave(ret, dStart, dEnd);
		
		//가장 오른쪽 추가
		Collections.reverse(orderedNodeArray[xSize - 1]);
		ret.addAll(orderedNodeArray[xSize - 1]);
		
		//가장 아래쪽 추가
		int uStart = ret.size();
		for(int i = xSize - 2; i > 0; i--){
			addNodetoFirstOfArray(ret, orderedNodeArray, i);
		}
		int uEnd = ret.size();
		
		//옥상의 봉오리 제거
		ret = deleteConcave(ret, uStart, uEnd);
		
		for(Node node : ret){
			System.out.println(node.getIndex() + " "
					+ node.getX() + " "
					+ node.getY());
		}
		return ret;
	}
	
	private ArrayList<Node> deleteConcave(ArrayList<Node>list, int dStart, int dEnd){
		boolean flag = true;
		boolean gradient;
		while(flag){
			flag = false;
			for(int i = dStart, count = dStart; i < dEnd - 2; i++, count++){
				gradient = compareGradient(list, count);
				if(gradient){
					list.remove(count + 1);
					dEnd--;
				}
				flag = flag || gradient ;
			}
		}
		return list;
	}
	
	private boolean compareGradient(ArrayList<Node>list, int index){
		int x1 = list.get(index + 1).getX() - list.get(index).getX();
		int y1 = list.get(index + 1).getY() - list.get(index).getY();
		int x2 = list.get(index + 2).getX() - list.get(index + 1).getX();
		int y2 = list.get(index + 2).getY() - list.get(index + 1).getY();
		return (y1 / x1) < (y2 / x2);
	}
	
	
	
	private ArrayList<Node>[] moveToArray(ArrayList<ArrayList<Node>> list){
		ArrayList<ArrayList<Node>> temp = (ArrayList<ArrayList<Node>>) list.clone();
		ArrayList<Node>[] ret;
		int count = 0;
		for(ArrayList<Node> iter : list){
			if(iter.size() == 0){
				temp.remove(count);
			}
			else{
				ArrayList<Node> innerTemp = (ArrayList<Node>) iter.clone();
				for(Node inIter : innerTemp){
					if(inIter.getIndex() < 0){
						iter.remove(inIter);
					}
				}
				count++;
			}
		}
		count = 0;
		ret = new ArrayList[temp.size()];
		for(ArrayList<Node> iter : temp){
			ret[count++] = iter;
		}
		return ret;
	}
	
	private ArrayList<Node> addNodetoLastOfArray(ArrayList<Node> ret, ArrayList<Node> list[], int index){
		int retLen = ret.size();
		if(!(list[index].size() == 0)){
			ret.add(list[index].get(list[index].size() - 1));
		}
		return ret;
	}
	
	private ArrayList<Node> addNodetoFirstOfArray(ArrayList<Node> ret, ArrayList<Node> list[], int index){
		if(!(list[index].size() == 0)){
			ret.add(list[index].get(0));
		}
		return ret;
	}
	
	
	private ArrayList<ArrayList<Node>> fillOuterList(ArrayList<ArrayList<Node>> list, int index){
		if(path.getNode(index).getX() > list.size() - 1){
			for(int j = list.size() - 1; j <= path.getNode(index).getX(); j++){
				list.add(new ArrayList<Node>());
			}
		}
		int x = path.getNode(index).getX();
		list.set(x, fillInnerList(list.get(x), index));
		return list;
	}
	
	private ArrayList<Node> fillInnerList(ArrayList<Node> list, int index){
		if(path.getNode(index).getY() > list.size() - 1){
			for(int i = list.size(); i <= path.getNode(index).getY(); i++){
				list.add(new Node(-1, 0, 0));
			}
		}
		list.set(path.getNode(index).getY(), path.getNode(index));
		return list;
	}
}
