import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Node{
  private String status;
  // ' ' pour libre, 'P' pour Pakkuman, 'B' pour bonbons,'M' pour monstres
  // '#' pour traversé, 'S' pour la sortie
  private ArrayList<Node> neighbours;

  public int coordI;
  public int coordJ;
  public int counter;


  public Node(int i, int j){
    this.status =" ";
    this.neighbours= new ArrayList<Node>();
    this.coordJ=j;
    this.coordI=i;
  }

  public void addNeighbour(Node n){
    if (!(this.neighbours.contains(n))){
      this.neighbours.add(n);
      n.addNeighbour(this);
    }
  }

  public void setStatus(String status){
    this.status=status;
  }

  public String getStatus(){
    return this.status;
  }

  public int getNbrOfNeighbours(){
    return this.neighbours.size();
  }
/*
  public int arc(Node n){
    return 1;
  }*/

  public boolean nextExists(){
  	return (this.neighbours.size() == 1) ? false : true;
  }

  public Node nextNode()
  {
     return (this.neighbours.get(0).getCounter() > 0) ? this.neighbours.get(1) : this.neighbours.get(0);
  }

  public ArrayList<Node> getNeighbours()
  {
      return this.neighbours;
  }
  public void incCounter()
  {
      this.counter++;
  }
  public void decCounter()
  {
      this.counter--;
  }
  public int getCounter()
  {
      return this.counter;
  }

  public String toString(){
    StringBuilder txt = new StringBuilder();
    txt.append(this.coordI);
    txt.append(" - ");
    txt.append(this.coordJ);
    txt.append("\n");
    txt.append(this.status);
    return txt.toString();
  }

}
