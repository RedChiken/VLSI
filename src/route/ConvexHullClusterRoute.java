package route;

import dataStructure.Node;
import dataStructure.TSPPath;
import opt.Opt;
import opt.TwoOpt;

import java.util.*;

public class ConvexHullClusterRoute implements Route {
	private TSPPath path;
	private Opt opt;
	private ArrayList<Node> convexHull;
	private ArrayList<ArrayList<Node>> closedCurveCluster;
	private Node[][] leftPathNode, rightPathNode;
	
	public ConvexHullClusterRoute(TSPPath path, Opt opt){
		//Initialize
		this.path = path;
		this.opt = opt;
		this.closedCurveCluster = new ArrayList<>();
		
		//Convex Hull
		this.convexHull = ConvexHull();
		System.out.println();
		
		//K-median Clustering
		int clusterSize = KMedianCluster();
		
		//Local Search - Greedy
		for(int i = 0; i < closedCurveCluster.size(); i++){
			closedCurveCluster.set(i, GreedySearch(closedCurveCluster.get(i)));
		}
		
		//Local Search - 2opt
		for(int i = 0; i < closedCurveCluster.size(); i++){
			closedCurveCluster.set(i, opt.Swap(closedCurveCluster.get(i)));
		}
		
		leftPathNode = new Node[clusterSize + 1][clusterSize + 1];
		rightPathNode = new Node[clusterSize + 1][clusterSize + 1];
		
//		for(int i = 0; i < clusterSize; i++){
//			recursiveQuadraticSearch(convexHull, closedCurveCluster.get(i), 0, i + 1);
//			for(int j = i + 1; j < clusterSize; j++){
//				recursiveQuadraticSearch(closedCurveCluster.get(i), closedCurveCluster.get(j), i + 1, j + 1);
//			}
//		}
//
//		for(Node[] n1 : leftPathNode){
//			for(Node n2 : n1){
//				System.out.println(n2.getIndex() + " " + n2.getX() + " " + n2.getY());
//			}
//			System.out.println();
//		}
		
		for(ArrayList<Node> iter : closedCurveCluster){
			for(Node node : iter){
				System.out.println(node.getIndex() + " " + node.getX() + " " + node.getY());
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public ConvexHullClusterRoute(TSPPath path){
		this(path, new TwoOpt());
	}
	
	@Override
	public TSPPath getRoute() {
		return path;
	}
	
	public ArrayList<Node> getConvexHullCluster(){
		return this.convexHull;
	}
	
	public Node getConvexHullNode(int index){
		return this.convexHull.get(index);
	}
	
	private void recursiveQuadraticSearch(ArrayList<Node> c1, ArrayList<Node> c2, int c1Index, int c2Index){
		Node c1Quadratics[] = {c1.get(0), c1.get(c1.size() * 1 / 4), c1.get(c1.size() * 1 / 2), c1.get(c1.size() * 3 / 4), c1.get(c1.size() - 1)};
		Node c2Quadratics[] = {c2.get(0), c2.get(c1.size() * 1 / 4), c2.get(c1.size() * 1 / 2), c2.get(c1.size() * 3 / 4), c2.get(c1.size() - 1)};
		if((c1Quadratics.length == 1) && c2Quadratics.length == 1){
			leftPathNode[c1Index][c2Index] = c1Quadratics[0];
			leftPathNode[c2Index][c2Index] = c2Quadratics[0];
		}
		else{
			int index1 = 0, index2 = 0;
			double distance = Double.MAX_VALUE;
			for(int i = 1; i < 4; i++){
				for(int j = 1; j < 4; j++){
					if(c1Quadratics[i].getDistance(c2Quadratics[j]) < distance){
						index1 = i;
						index2 = j;
					}
				}
			}
			recursiveQuadraticSearch(new ArrayList<Node>(c1.subList(index1 - 1, index1 + 1)),
					new ArrayList<Node>(c2.subList(index2 - 1, index2 + 1)), c1Index, c2Index);
		}
	}
	
	private ArrayList<Node> GreedySearch(ArrayList<Node> node){
		double maxDistance = 0;
		double distance = 0;
		boolean access[] = new boolean[node.size()];
		Arrays.fill(access, true);
		Node minNode;
		ArrayList<Node> ret = new ArrayList<Node>();
		ret.add(node.get(0));
		access[0] = false;
		int index = 0;
		for(int i = 0; i < node.size() - 1; i++){
			maxDistance = Double.MAX_VALUE;
			minNode = node.get(i);
			for(int j = 0; j < node.size(); j++){
				distance = node.get(j).getDistance(node.get(i));
				if((distance > 0) && (distance < maxDistance) && access[j]){
					maxDistance = distance;
					minNode = node.get(j);
					index = j;
				}
			}
			access[index] = false;
			ret.add(minNode);
		}
		return ret;
	}
	
	private int KMedianCluster(){
		int ClusterSize = (int)(Math.sqrt(path.NumOfCity() / 2));
		ArrayList<Node> medianNode = new ArrayList<>();
		ArrayList<ArrayList<Node>> cluster = new ArrayList<>();
		
		// median 초기값 랜덤 설정
		for(int i = 0, index = 0; i < ClusterSize; i++){
			index = (path.NumOfCity() / ClusterSize) * (i + 1);
			medianNode.add(path.getNode(index));
			cluster.add(new ArrayList<>());
		}
		Node newMedian;
		while(medianNode.size() > 0){
			// 노드들을 자신과 가까운 median으로 분류
			for(int i = 0; i < path.NumOfCity(); i++){
				cluster.get(minimumMedianIndex(medianNode, path.getNode(i))).add(path.getNode(i));
			}
			for(int count = 0; count < cluster.size(); count++){
				// 클러스터별로 새로운 median 설정
				newMedian = getMedianNode(cluster.get(count));
				// 기존 median과 현재 median이 같을 경우, 더 이동 할 필요 없으므로 제거
				if(medianNode.get(count).equals(newMedian)){
					// cluster 저장
					closedCurveCluster.add(cluster.get(count));
					// path에서 cluster로 이동 된 노드들 삭제
					for(Node node : cluster.get(count)){
						path.deleteNode(node);
					}
					// 이동 된 cluster의 median, nodes 삭제
					cluster.remove(count);
					medianNode.remove(count);
					count--;
				}
				else{
					medianNode.set(count, newMedian);
				}
			}
			for(int i = 0; i < cluster.size(); i++){
				cluster.get(i).removeAll(cluster.get(i));
			}
		}
		return ClusterSize;
	}
	
	private Node getMedianNode(ArrayList<Node> list){
		int xSum = 0, ySum = 0;
		for(Node node : list){
			xSum += node.getX();
			ySum += node.getY();
		}
		Node node = new Node(-1, Math.round(xSum / list.size()), Math.round(ySum / list.size()));
		Node ret = list.get(0);
		double maxDistance = Double.MAX_VALUE;
		for(int i = 0 ; i < path.NumOfCity(); i++){
			if(path.getNode(i).getDistance(node) < maxDistance){
				maxDistance = path.getNode(i).getDistance(node);
				ret = path.getNode(i);
			}
		}
		return ret;
	}
	
	private int minimumMedianIndex(ArrayList<Node> medianNode, Node node){
		double maxDistance = Double.MAX_VALUE;
		int index = 0;
		for(int i = 0; i < medianNode.size(); i++){
			if(node.getDistance(medianNode.get(i)) < maxDistance){
				maxDistance = node.getDistance(medianNode.get(i));
				index = i;
			}
		}
		return index;
	}
	
	private ArrayList<Node> ConvexHull(){
		ArrayList<ArrayList<Node>> orderedNode = new ArrayList<ArrayList<Node>>();
		ArrayList<Node> ret = new ArrayList<Node>();
		for(int i = 0; i < path.NumOfCity(); i++){
			fillOuterList(orderedNode, i);
		}
		ArrayList<Node>[] orderedNodeArray = moveToArray(orderedNode);
		int xSize = orderedNodeArray.length;
		//가장 왼쪽 추가
		ret.addAll(orderedNodeArray[0]);
		for(Node node : orderedNodeArray[0]){               //  기존 input에서 추가된 부분 제거
			path.deleteNode(node);
		}
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
		for(Node node : orderedNodeArray[xSize - 1]){       // 기존 input에서 추가된 부분 제거
			path.deleteNode(node);
		}
		
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
			for(int count = dStart - 1; count < dEnd - 2; count++){
				gradient = compareGradient(list, count);
				if(gradient){
					path.addNode(list.get(count + 1));
					list.remove(count + 1);
					dEnd--;
					count--;
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
		return y1 * x2 < x1 * y2;
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
			path.deleteNode(list[index].get(list[index].size() - 1));
		}
		return ret;
	}
	
	private ArrayList<Node> addNodetoFirstOfArray(ArrayList<Node> ret, ArrayList<Node> list[], int index){
		if(!(list[index].size() == 0)){
			ret.add(list[index].get(0));
			path.deleteNode(list[index].get(0));
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
