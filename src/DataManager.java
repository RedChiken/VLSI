import DataStructure.Node;
import DataStructure.TSPPath;

import java.io.*;

public class DataManager {
	private TSPPath path;
	
	public DataManager(String filePath){
		String s;
		path = new TSPPath();
		try{
			BufferedReader in = new BufferedReader(new FileReader(filePath));
			s= in.readLine();
			while(s != null){
				String[] splited = s.split(" ");
				path.addNode(new Node(Integer.parseInt(splited[0]),
						Integer.parseInt(splited[1]),
						Integer.parseInt(splited[2])));
				s= in.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setData(TSPPath input){
		this.path = input;
	}
	
	public TSPPath getData(){
		return this.path;
	}
	
	public void writeResult(){
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter("Result.txt"));
			out.write(Double.toString(path.getDistance()) + ", path: ");
			for(int i = 0; i < path.NumOfCity(); i++){
				//System.out.println(Integer.toString(path.getNode(i).getIndex()) + "\t");
				out.write(Integer.toString(path.getNode(i).getIndex()) + "\t");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
