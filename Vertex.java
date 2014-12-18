import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.Collections;

public class Vertex implements Comparable<Vertex>{

  private ArrayList<Edge> adjacencies;
  private String type;
	private int coordI;
	private int coordJ;
  private int index;
  private int distToExit;

	public Vertex(String type_, int coordI_, int coordJ_){
		this.type = type_;
		this.coordJ = coordJ_;
		this.coordI = coordI_;
    this.adjacencies = new ArrayList<Edge>();
	}

  public Vertex(){  // constructeur d'un vertex vide
    this.type="empty";
  }

  public boolean isEmpty(){
    return this.type=="empty";
  }

	public Edge newEdge(Vertex other, ArrayList<int[]> way){
    // connecter ce vertex avec le vertex other, avec le chemin de nodes way
		Edge e = new Edge(this,other, way); // edge de ce vertex vers other
		this.addEdge(e);
		Collections.reverse(way);
		other.addEdge( new Edge(other,this, way) ); // edge de other vers ce vertex
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

  public int distanceTo(Vertex v){ // calcule la distance vers le vertex v
    if (v==this) // la distance vers lui-meme = 0
      return 0;
    int i=0;
    while (i<this.adjacencies.size()){// regarder si v est un voisin direct:
      if (this.adjacencies.get(i).getTarget()==v)// trouvé
        return this.adjacencies.get(i).getDistance();
      i++;
    }
    return Integer.MAX_VALUE;// si ils ne sont pas connectés
  }

  public void setIndex(int i){
    this.index=i;
  }

  public int getIndex(){
    return this.index;
  }

  public void setType(String s){
    this.type=s;
  }

  public void setDistanceToExit(int i){
    this.distToExit=i;
  }

  public int getDistanceToExit(){
    return this.distToExit;
  }

  public Vertex getClosestNeighourToExit(){
    int min=Integer.MAX_VALUE;
    Vertex v=new Vertex();
    for (Edge e : this.adjacencies){
      if (e.getTarget().getDistanceToExit()<min){
        v=e.getTarget();
        min=e.getTarget().getDistanceToExit();
      }
    }
    return v;
  }

  public Vertex getClosestType(String type){
    int min=Integer.MAX_VALUE;
    Vertex v=new Vertex();
    for (Edge e: this.adjacencies){
      if (e.getTarget().distanceTo(this)<min && e.getTarget().getType()==type){
          v=e.getTarget();
          min=e.getTarget().distanceTo(this);
      }
    }
    return v;
  }

  public int nbrOfCandyNeighbours(){
    int i=0;
    for (Edge e : this.adjacencies){
      if (e.getTarget().getType()=="B")
        i++;
    }
    return i;
  }

  public int compareTo(Vertex v){
    return Integer.compare(this.distToExit,v.getDistanceToExit());
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
    //txt.append(this.distToExit);
    return txt.toString();

  }
}
