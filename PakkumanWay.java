import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

class PakkumanWay{
  //private Hashtable<Vertex,Hashtable<Vertex,Integer>> dimTot;
  //private Hashtable<Vertex,Hashtable<Vertex,Vertex>> predTot;
  private ArrayList<Vertex> vertices;
  private int nbrOfVertices;
  private int monsters;
  private int candies;
  private int paccuman;
  private int exit;

  private ArrayList<Vertex> way;
  private ArrayList<Vertex> fastestWay;
  private ArrayList<Vertex> goodForNothing;
  private Vertex currentV;
  private Vertex nextV;
  private int gotCandy;
  private int candiesCollected;
  private boolean foundExit;

  public PakkumanWay(ArrayList<Vertex> vertices_,int monsters_,int candies_){
    this.vertices=vertices_;
    this.nbrOfVertices=this.vertices.size();
    this.monsters=monsters_;
    this.candies=candies_;
    this.exit=0; //index of exit in vertices is always 0
    this.candiesCollected=0;
    this.foundExit=false;
    this.dijkstra();
    goodForNothing=new ArrayList<Vertex>();
    for (int i=0;i<this.nbrOfVertices;i++){
      if (this.vertices.get(i).getType()=="P")
        this.paccuman=i;
      this.vertices.get(i).setIndex(i);
      //System.out.println(this.vertices.get(i));
    }
    this.wayOut();
    System.out.println();
    //this.fastestWay();

  }

  private void fastestWay(){
    this.fastestWay=new ArrayList<Vertex>();
    System.out.println("Fastest Way : ");
    this.currentV=this.vertices.get(this.paccuman);
    System.out.println(this.currentV);
    while (this.currentV.getType()!="E"){
      this.currentV=this.currentV.getClosestNeighourToExit();
    System.out.println(this.currentV);
    }
  }

  private void feedInitialMonster(int i){
    this.way.add(i+1,this.way.get(i).getClosestType("B"));
    this.candiesCollected++;
    this.way.get(i+1).setType("o");
    this.way.add(i+2,this.way.get(i));
    this.handleMonster();
    this.moveOn();
  }


  private void moveOn(){
    this.currentV=nextV;
    this.way.add(currentV);
    this.nextV=currentV.getClosestNeighourToExit();
  }

  private void handleMonster(){
    this.nextV.setType("X");
  }

  public int getNbrOfCandiesCollected(){
    return this.candiesCollected;
  }

  public ArrayList<Vertex> getPakkumanWay(){
    return this.way;
  }

  public boolean exitFound(){
    return this.foundExit;
  }

  private void reset(){

  }

  //private boolean

  private  void /* ArrayList<Vertex> */ findHiddenCandies(int progress){
    int candiesSoFar=0;
    for (int i=0;i<progress;i++){
      if (this.way.get(i).getType()=="o")
      //candy we could use to "free" additional candies
        candiesSoFar++;
      else
        candiesSoFar--;
      }
    }


  private void wayOut(){
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
            this.way.add(currentV);
            this.handleMonster();
            this.moveOn();
            this.candiesCollected++;
            this.candies--;
          }
          else{ // we have to go back on our way to find a free candy
            ArrayList<Vertex> tmpStack=new ArrayList<Vertex>();
            int i=this.way.size()-1;

            if (this.way.size()!=1)
            // if Pakkuman has at least one candy as a direct neighbour
              while (i>=0 && this.way.get(i).getClosestType("B").isEmpty())
                i--;
            if (this.way.size()!=1 && i>0) {
            // a candy as a neighbour to one of our previously visited vertices
            // has been found, we change our previous way to take it and return
            // to feed the initial monster
              this.feedInitialMonster(i);
            }
            else{
              System.out.println("no candy reachable");
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
          this.candiesCollected++;
          this.candies--;
          this.currentV.setType("o");
        }
      }
    }
    if (currentV.getType()=="E")
      this.foundExit=true;
    //for (int i=0;i<this.way.size();i++)
    //  System.out.println(way.get(i));
  }

  public void printWay(){
    for (int i=0;i<this.way.size();i++){
      System.out.println(this.way.get(i));
    }
  }

  public void dijkstra(){
    for (int i=0;i<this.nbrOfVertices;i++)
      this.vertices.get(i).setDistanceToExit(Integer.MAX_VALUE);
    this.vertices.get(this.exit).setDistanceToExit(0);
    for (Edge e : this.vertices.get(this.exit).getAdjacencies()){
      Vertex v = e.getTarget();
      v.setDistanceToExit(v.distanceTo(this.vertices.get(this.exit)));
    }
    PriorityQueue<Vertex> q = new PriorityQueue<Vertex>();
    for (int i=1;i<this.nbrOfVertices;i++)
      q.add(this.vertices.get(i));
    while (!q.isEmpty()) {
      Vertex m = q.poll();
      if (m.getDistanceToExit()==Integer.MAX_VALUE){
        q.clear();
      }
      else{
        for (Edge e : m.getAdjacencies()){
          Vertex v = e.getTarget();
          if (q.contains(v)){
            int dist=m.getDistanceToExit() + v.distanceTo(m);
            if (dist<v.getDistanceToExit()){
              q.remove(v);
              v.setDistanceToExit(dist);
              q.add(v);
            }
          }
        }
      }
    }
  }

}
