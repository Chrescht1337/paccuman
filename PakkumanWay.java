import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

class PakkumanWay{
  private ArrayList<Vertex> vertices;
  private int nbrOfVertices;
  private int monsters;
  private int candies;
  private int paccuman;
  private int exit;

  private ArrayList<Vertex> way;
  //private ArrayList<Vertex> fastestWay;
  private ArrayList<Vertex> goodForNothing;
  private Vertex currentV;
  private Vertex nextV;
  private int gotCandy;
  private int candiesCollected;
  private boolean foundExit;
  private boolean succesfulHiddenSearch;

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
      if (this.vertices.get(i).getType()=="P"){
        this.paccuman=i;
        i=nbrOfVertices;
      }
    }
    //this.wayOut();
  }
  /*
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
  */
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

  private void profitableVisits(int progress){
    ArrayList<Vertex> verticesEvaluated=new ArrayList<Vertex>();
    for (int i=0;i<=progress;i++){
      if (!verticesEvaluated.contains(this.way.get(i))){
        verticesEvaluated.add(this.way.get(i));
        for (Edge e:this.way.get(i).getAdjacencies()){
          Vertex v= e.getTarget();
          if (v.getType()=="M" && !verticesEvaluated.contains(v)){
            this.succesfulHiddenSearch=false;
            if (v.nbrOfCandyNeighbours()>1){
              this.way.add(v);
              v.setType("X");
              for (int j=0;j<2;j++){
              //on en collecte 2 bonbons pour rendre le déplacement rentable
                Vertex tmpC=v.getClosestType("B");
                this.way.add(tmpC);
                tmpC.setType("o");
                this.candies--;
                this.candiesCollected++;
                this.way.add(v);
              }
              this.gotCandy++;
              this.handleMonster();
              this.way.add(this.way.get(i));
              this.succesfulHiddenSearch=true;
              i=progress+1;
              break;
            }
          }
        }
      }
    }
  }

  private  void findHiddenCandies(int progress){
    int candiesSoFar=0;
    ArrayList<Vertex> verticesEvaluated=new ArrayList<Vertex>();
    int i=0;
    while (i<progress){
      if (this.way.get(i).getType()=="o" && !verticesEvaluated.contains(this.way.get(i))){
      //candy we could use to "free" additional candies
          verticesEvaluated.add(this.way.get(i));
          candiesSoFar++;
      }
      else if (this.way.get(i).getType()=="X" && !verticesEvaluated.contains(this.way.get(i)))
        verticesEvaluated.add(this.way.get(i));
        candiesSoFar--;
      i++;
    }

    while (this.way.get(i).getType()!="X"){
      if (this.way.get(i).getType()=="o" && !verticesEvaluated.contains(this.way.get(i))){
        verticesEvaluated.add(this.way.get(i));
        i++;
      }
    }
    if (candiesSoFar>=1)
      this.profitableVisits(progress);
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
              if (this.succesfulHiddenSearch){
                ArrayList<Vertex> verticesEvaluated=new ArrayList<Vertex>();
                for (int k=1;k<this.way.size();k++){
                  if (this.way.get(k).getType()!="o" && !this.way.contains(this.way.get(k))){
                    verticesEvaluated.add(this.way.get(k));
                    this.findHiddenCandies(k);
                  }
                }
              }
              else{
                System.out.println("no candy reachable");
                break;
              }
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
  }
  /*
  public void printWay(){
    for (int i=0;i<this.way.size();i++){
      System.out.println(this.way.get(i));
    }
  }*/

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
