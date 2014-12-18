import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Edge{

    private Vertex target;
    private Vertex origin;
    private ArrayList<int[]> way = new ArrayList<int[]>();

    public Edge(Vertex target, Vertex origin_, ArrayList<int[]> way_){
    	this.origin = origin_;
		this.target = target;
		this.way = new ArrayList<int[]>(way_);
	}

	public Edge(){ // emptyEdge
		this.target = new Vertex("emptyEdge",0,0);
		this.way = new ArrayList<int[]>();
	}

	public ArrayList<int[]> getWay(){
		return way;
	}

	public Vertex getTarget(){
		return this.target;
	}

	public Vertex getOrigin(){
		return this.origin;
	}

  public int getDistance(){
    return this.way.size()-1;
  }

	public boolean isEmpty(){
		return way.size() ==0;
	}

  public String toString(){
    StringBuilder txt= new StringBuilder();
    txt.append("origin : ");
    txt.append(this.origin);
    txt.append(" - target : ");
    txt.append(this.target);
    txt.append(" - size : ");
    txt.append(this.way.size());
    return txt.toString();
  }

  private void printtt(ArrayList<int[]> otherWay){

    System.out.println(otherWay);
  }

	public ArrayList<int[]> shareWay(Edge e){
		ArrayList<int[]> otherWay = e.getWay();
		int i = 0;
		while( (otherWay.get(i)[0] == this.way.get(i)[0]) && (otherWay.get(i)[1] == this.way.get(i)[1]) ){
			i++;
		}
		ArrayList<int[]> newWay = new ArrayList<int[]>();
		int j = otherWay.size()-1;
		while (j >= i){
			newWay.add(otherWay.get(j));
			j--;
		}

		while (j < this.way.size() && j >= 0){
			newWay.add(this.way.get(j));
			j++;
		}
		return newWay;
	}
}
