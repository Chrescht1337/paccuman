import java.util.ArrayList;
import java.util.List;
import java.io.*;
public class Node{
  private String status;
  // ' ' pour libre, 'P' pour Pakkuman, 'B' pour bonbons,'M' pour monstres
  // '#' pour traversé, 'S' pour la sortie
  private List<Node> neighbours;

  private int coordI;
  private int coordJ;


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

  public int arc(Node n){
    return 1;
  }

  //public void toString(){

  //}
}
