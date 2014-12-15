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
    this.wayOut();
    //this.printMat();
    System.out.println();
    this.fastestWay();
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

  private void backToInitialMonster(Stack<Vertex>way,Stack<Vertex>tmpStack,Vertex tmpV,Vertex nextV,Vertex currentV){
    way.push(tmpV);
    Vertex closeCandy=tmpV.getClosestCandy();
    closeCandy.setType("o");
    way.push(closeCandy);
    way.push(tmpV);
    while (!tmpStack.empty())
      way.push(tmpStack.pop());
    this.candies--;
    nextV=currentV.getClosestNeighourToExit(); // visiting the monster
    nextV.setType("X"); //feeding it
  }

  public void fastestWay(){
    Vertex currentV =this.vertices.get(this.paccuman);
    System.out.println(currentV);
    while (currentV.getType()!="E"){
      currentV=currentV.getClosestNeighourToExit();
    System.out.println(currentV);
    }
  }

  public void wayOut(){
    Vertex currentV = this.vertices.get(this.paccuman);
    //System.out.println(this.paccuman);
    Vertex nextV =currentV.getClosestNeighourToExit();
    Vertex tmpV= new Vertex();
    int gotCandy=0;
    //ArrayList<Vertex> way = new ArrayList<Vertex>();
    //way.add(currentV);
    Stack<Vertex> way=new Stack<Vertex>();
    way.push(currentV);
    while (currentV.getType()!="E" && !currentV.isEmpty()){
      if (nextV.getType()=="M"){ //if monster
        if (gotCandy>0){  //if we have at least one candy in our pockets
          gotCandy--;
          nextV.setType("X");
        }
        else if (this.candies!=0){   // there are still free candies we can use
          tmpV=nextV;
          nextV=currentV.getClosestCandy();
          if (!nextV.isEmpty()){
          // we take the closest free candy from the current vertex, even if that
          // means that we'll have to go "the extra mile"
            tmpV.setType("X");
            nextV.setType("o");
            this.candies--;
          }
          else{ // we have to go back on our way to find a free candy
            Stack<Vertex> tmpStack=new Stack<Vertex>();
            tmpStack.push(way.pop());
            if (!way.empty()){
            // if Pakkuman has at least one candy as direct neighbour
              tmpV=way.pop();
              while (tmpV.getClosestCandy().isEmpty() && !way.empty()){
              //we're looking for a free candy which is a direct neighbour to one
              // of the previously visited vertices
                tmpStack.push(tmpV);
                tmpV=way.pop();
              }
            }
            if (!way.empty()) {
            // a candy as a neighbour to one of our previously visited vertices
            // has been found, we change our previous way to take it and return
            // to the initial monster
              this.backToInitialMonster(way,tmpStack,tmpV,nextV,currentV);
              /*way.push(tmpV);
              Vertex closeCandy=tmpV.getClosestCandy();
              closeCandy.setType("o");
              way.push(closeCandy);
              way.push(tmpV);
              while (!tmpStack.empty())
                way.push(tmpStack.pop());
              this.candies--;
              nextV=currentV.getClosestNeighourToExit();
              nextV.setType("X");*/
            }
            else{
              System.out.println("no candy reachable");
              tmpStack.push(tmpV);
              while (!tmpStack.empty()){
                way.push(tmpStack.pop());
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
        currentV=nextV;
        way.push(currentV);
        if (currentV.getType()=="B"){
          gotCandy++;
          this.candies--;
          currentV.setType("o");
        }
        nextV=currentV.getClosestNeighourToExit();
      }
    }
    while (!way.empty())
      System.out.println(way.pop());
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
