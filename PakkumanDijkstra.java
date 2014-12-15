import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Hashtable;
class PakkumanDijkstra{
  private Hashtable<Vertex,Hashtable<Vertex,Integer>> dimTot;
  private Hashtable<Vertex,Hashtable<Vertex,Vertex>> predTot;
  private ArrayList<Vertex> vertices;
  public PakkumanDijkstra(ArrayList<Vertex> vertices_){
    this.vertices=vertices_;
    this.dimTot= new Hashtable<Vertex,Hashtable<Vertex,Integer>>();
    this.predTot= new Hashtable<Vertex,Hashtable<Vertex,Vertex>>();
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
    }
  }
  public void dijkstra(Vertex start){
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
  }
}
