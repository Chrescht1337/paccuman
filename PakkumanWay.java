import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Hashtable;
class PakkumanWay{
  //private Hashtable<Vertex,Hashtable<Vertex,Integer>> dimTot;
  //private Hashtable<Vertex,Hashtable<Vertex,Vertex>> predTot;
  private int[][] matTrans;
  private ArrayList<Vertex> vertices;
  private int nbrOfVertices;
  public PakkumanWay(ArrayList<Vertex> vertices_){
    this.vertices=vertices_;
    this.nbrOfVertices=this.vertices.size();
    this.matTrans=new int[this.nbrOfVertices][this.nbrOfVertices];
    this.matTransitive();
    this.printMat();



    //this.dimTot= new Hashtable<Vertex,Hashtable<Vertex,Integer>>();
    //this.predTot= new Hashtable<Vertex,Hashtable<Vertex,Vertex>>();

    /*
    for (int i=0;i<this.vertices.size();i++)
      this.dijkstra(this.vertices.get(i));
    //print results :

    for (int i=0;i<this.vertices.size();i++){
      for (int j=0;j<this.vertices.size();j++){
        System.out.print(this.vertices.get(i));
        System.out.print(" distance to : ");
        System.out.print(this.vertices.get(j));
        System.out.print(" = ");
        System.out.println(this.dimTot.get(this.vertices.get(i)).get(this.vertices.get(j)));
      }
    }*/
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
                this.matTrans[x][y]=this.matTrans[x][k]+this.matTrans[k][y];

  }

  public void printMat(){
    for (int i=0;i<this.nbrOfVertices;i++)
      for (int j=0;j<this.nbrOfVertices;j++){
        System.out.print(this.vertices.get(i));
        System.out.print(" - distance to : ");
        System.out.print(this.vertices.get(j));
        System.out.print(" matTrans : ");
        System.out.print(this.matTrans[i][j]);
        System.out.print(" distanceTo : ");
        System.out.println(this.vertices.get(i).distanceTo(this.vertices.get(j)));
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
