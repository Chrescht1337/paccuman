import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.Collections;

public class Vertex implements Comparable<Vertex>{

  private ArrayList<Edge> adjacencies = new ArrayList<Edge>();
  private String type;
	private int coordI;
	private int coordJ;
  private int shortestDistance;
  private Vertex predecessor;

	public Vertex(String type_, int coordI_, int coordJ_){
		this.type = type_;
		this.coordJ = coordJ_;
		this.coordI = coordI_;
    this.shortestDistance=Integer.MAX_VALUE;
    this.predecessor=new Vertex();
	}

  public Vertex(){
    this.type="empty";
  }

	public Edge newEdge(Vertex other, ArrayList<int[]> way){//n.newEdge(toConnect, way);
		Edge e = new Edge(other,this, way);
		this.addEdge(e);
		Collections.reverse(way);
		other.addEdge( new Edge(this,other, way) );
		return e;
	}

  public String getType(){
  	return this.type;
  }

  public void addEdge(Edge e){
  	this.adjacencies.add(e);
  }

  public int getCoordI(){
    return this.coordI;
  }

  public int getCoordJ(){
    return this.coordJ;
  }

  public ArrayList<Edge> getAdjacencies(){
    return this.adjacencies;
  }

  public int getNbrOfNeighbours(){
    return this.adjacencies.size();
  }

  public int distanceTo(Vertex v){
    int i=0;
    while (i<this.adjacencies.size()){
      if (this.adjacencies.get(i).getTarget()==v)
        return this.adjacencies.get(i).getDistance();
      i++;
    }
    return Integer.MAX_VALUE;
  }

  public int getShortestDistance(){
    return this.shortestDistance;
  }

  public void setShortestDistance(int i){
    this.shortestDistance=i;
  }

  public void setPredecessor(Vertex v){
    this.predecessor=v;
  }

  public int compareTo(Vertex v){
    return Integer.compare(this.shortestDistance,v.getShortestDistance());
  }

  public String toString(){
    StringBuilder txt= new StringBuilder();
    txt.append(this.type);
    txt.append(" - ");
    txt.append("[ ");
    txt.append(this.coordI);
    txt.append(" , ");
    txt.append(this.coordJ);
    txt.append(" ]");
    //txt.append(" - ");
    //txt.append("shortest distance to exit : ");
    //txt.append(this.shortestDistance);
    return txt.toString();

  }
}
