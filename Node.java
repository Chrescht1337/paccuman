import java.util.ArrayList;
import java.util.List;
import java.io.*;
public class Node{
  private String status;
  // ' ' pour libre, 'P' pour Pakkuman, 'B' pour bonbons,'M' pour monstres
  // '#' pour traversé, 'S' pour la sortie
  private List<Node> neighbours;


  public Node(){
    this.status =" ";
    this.neighbours= new ArrayList<Node>();
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

  public int getNbrOfNeighbours(){
    return this.neighbours.size();
  }

  //public void toString(){

  //}
}
