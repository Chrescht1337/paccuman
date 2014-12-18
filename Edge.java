import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Edge{

    private Vertex target;
    private Vertex origin;
    private ArrayList<int[]> way = new ArrayList<int[]>();

    public Edge(Vertex origin_, Vertex target, ArrayList<int[]> way_){
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

  public int getDistance(){// longueur du chemin
    return this.way.size();
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

	public ArrayList<int[]> shareWay(Edge e){ // retourne le chemin combiné de deux edges
		ArrayList<int[]> otherWay = e.getWay();
		int i = 0;
    // on sursaute le chemin commun:
		while( i<otherWay.size() && i<this.way.size() && (otherWay.get(i)[0] == this.way.get(i)[0]) && (otherWay.get(i)[1] == this.way.get(i)[1]) ){
			i++;
		}
		ArrayList<int[]> newWay = new ArrayList<int[]>();
		int j = otherWay.size()-1;
		while (j >= i){ // le chemin qui reste de e + ...
			newWay.add(otherWay.get(j));
			j--;
		}

		while (j < this.way.size() && j >= 0){ // ... le chemin qui reste de this
			newWay.add(this.way.get(j));
			j++;
		}
		return newWay;
	}
}
