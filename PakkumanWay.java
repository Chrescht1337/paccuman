import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

class PakkumanWay{
  //private Hashtable<Vertex,Hashtable<Vertex,Integer>> dimTot;
  //private Hashtable<Vertex,Hashtable<Vertex,Vertex>> predTot;
  private int[][] matTrans;
  private ArrayList<Vertex> vertices;
  private int nbrOfVertices;
  private int monsters;
  private int candies;
  private int paccuman;
  private int exit;

  private ArrayList<Vertex> way;
  private Vertex currentV;
  private Vertex nextV;
  private int gotCandy;

  public PakkumanWay(ArrayList<Vertex> vertices_,int monsters_,int candies_){
    this.vertices=vertices_;
    this.nbrOfVertices=this.vertices.size();
    this.matTrans=new int[this.nbrOfVertices][this.nbrOfVertices];
    this.monsters=monsters_;
    this.candies=candies_;
    this.exit=0; //index of exit in vertices is always 0
    this.matTransitive();
    for (int i=0;i<this.nbrOfVertices;i++){
      if (this.vertices.get(i).getType()=="P")
        this.paccuman=i;
      this.vertices.get(i).setIndex(i);
      this.vertices.get(i).setDistanceToExit(this.matTrans[i][0]);
    }
    this.fastestWay();
    System.out.println();
    this.wayOut();
    //this.printMat();
  }

  private void matTransitive(){
    for (int i=0;i<this.nbrOfVertices;i++)
      for (int j=0;j<this.nbrOfVertices;j++)
        this.matTrans[i][j]=this.vertices.get(i).distanceTo(this.vertices.get(j));

    for (int k=0;k<this.nbrOfVertices;k++)
      for (int x=0;x<this.nbrOfVertices;x++)
        if (this.matTrans[x][k]<Integer.MAX_VALUE)
          for (int y=0;y<this.nbrOfVertices;y++)
            if (this.matTrans[k][y]<Integer.MAX_VALUE)
              if (this.matTrans[x][k]+this.matTrans[k][y]<this.matTrans[x][y])
                this.matTrans[x][y]=this.matTrans[x][k]+this.matTrans[k][y]-1;
                //-1 because the intermediary Node is counted twice in the edge
  }

  public void fastestWay(){
    Vertex currentV =this.vertices.get(this.paccuman);
    System.out.println("Fastest Way : ");
    System.out.println(currentV);
    while (currentV.getType()!="E"){
      currentV=currentV.getClosestNeighourToExit();
    System.out.println(currentV);
    }
  }

  private void feedInitialMonster(ArrayList<Vertex>tmpStack,Vertex tmpV){
    this.way.add(tmpV);
    Vertex closeCandy=tmpV.getClosestType("B");
    closeCandy.setType("o");
    this.way.add(closeCandy);
    this.way.add(tmpV);
    while (tmpStack.size()!=0)
      this.way.add(tmpStack.remove(tmpStack.size()-1));
    this.candies--;
    this.handleMonster();
    this.moveOn();
  }


  public void moveOn(){
    this.currentV=nextV;
    this.way.add(currentV);
    this.nextV=currentV.getClosestNeighourToExit();
  }

  public void handleMonster(){
    this.nextV.setType("X");
  }

  public void wayOut(){
    this.currentV = this.vertices.get(this.paccuman);
    this.nextV =this.currentV.getClosestNeighourToExit();
    Vertex tmpV= new Vertex();
    this.gotCandy=0;
    this.way=new ArrayList<Vertex>();
    this.way.add(this.currentV);
    while (this.currentV.getType()!="E" && !this.currentV.isEmpty()){
      if (this.nextV.getType()=="M"){ //if monster
        if (this.gotCandy>0){  //if we have at least one candy in our pockets
          this.gotCandy--;
          this.handleMonster();
          this.moveOn();
        }
        else if (this.candies!=0){   // there are still free candies we can use
          tmpV=this.currentV.getClosestType("B"); // search for a candy-neighbour 
          if (!tmpV.isEmpty()){
          // we take the closest free candy from the current vertex, even if that
          // means that we'll have to go "the extra mile"
            tmpV.setType("o");  //grab candy
            this.way.add(tmpV);
            this.handleMonster();
            this.moveOn();
            this.candies--;
          }
          else{ // we have to go back on our way to find a free candy
            ArrayList<Vertex> tmpStack=new ArrayList<Vertex>();
            tmpStack.add(way.remove(this.way.size()-1));
            if (this.way.size()!=0){
            // if Pakkuman has at least one candy as direct neighbour
              tmpV=way.remove(this.way.size()-1);
              while (tmpV.getClosestType("B").isEmpty() && way.size()!=0){
              //we're looking for a free candy which is a direct neighbour to one
              // of the previously visited vertices
                tmpStack.add(tmpV);
                tmpV=way.remove(this.way.size()-1);
              }
            }
            if (this.way.size()!=0) {
            // a candy as a neighbour to one of our previously visited vertices
            // has been found, we change our previous way to take it and return
            // to feed the initial monster
              this.feedInitialMonster(tmpStack,tmpV);
            }
            else{
              System.out.println("no candy reachable");
              if (!tmpV.isEmpty())
                this.way.add(tmpV);
              while (tmpStack.size()!=0){
                this.way.add(tmpStack.remove(tmpStack.size()-1));
              }
              break;
            }
          }
        }
        else{
          System.out.println("no more free candies");
          break;
        }
      }
      else{
        this.moveOn();
        if (this.currentV.getType()=="B"){ //grab candy
          this.gotCandy++;
          this.candies--;
          this.currentV.setType("o");
        }
      }
    }
    while (way.size()!=0)
      System.out.println(way.remove(this.way.size()-1));
  }

  public void printMat(){
    for (int i=0;i<this.nbrOfVertices;i++)
      for (int j=0;j<this.nbrOfVertices;j++){
        System.out.print(this.vertices.get(i));
        System.out.print(" - distance to : ");
        System.out.print(this.vertices.get(j));
        System.out.print(" matTrans : ");
        System.out.println(this.matTrans[i][j]);
      }
  }

  public void dijkstra(Vertex start){

    /*

    //start.setShortestDistance(0);
    Hashtable<Vertex,Integer> dim = new Hashtable<Vertex,Integer>();
    Hashtable<Vertex,Vertex> pred = new Hashtable<Vertex,Vertex>();
    PriorityQueue<Vertex> q = new PriorityQueue<Vertex>();

    dim.put(start,0);
    for (int i=1;i<this.vertices.size();i++){
      //vertices.get(i).setShortestDistance(start.distanceTo(vertices.get(i)));
      //vertices.get(i).setPredecessor(start);
      dim.put(this.vertices.get(i),start.distanceTo(this.vertices.get(i)));
      pred.put(this.vertices.get(i),start);
      q.add(this.vertices.get(i));
    }
    while (!q.isEmpty()) {
      Vertex m = q.poll();
      if (dim.get(m)==Integer.MAX_VALUE){
        q.clear();
      }
      else{
        for (Edge e : m.getAdjacencies()){
          Vertex v = e.getTarget();
          if (q.contains(v)){
            int dist=dim.get(m) + v.distanceTo(m);
            if (dist<dim.get(v)){
              q.remove(v);
              pred.put(v,m);
              dim.put(v,dist);
              q.add(v);
            }
          }
        }
      }
    }
    this.dimTot.put(start,dim);
    this.predTot.put(start,pred);
  */
  }

}
