import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class SmallGraph{
    private ArrayList<Vertex> vertices;
    //private int[] pakkuman;
    //private int[][] monsters;
    //private int[][] candies;
    private Vertex exit;
    private Graph g;

    public ArrayList<Vertex> getGraphVertices(){
      return this.vertices;
    }
    public Vertex getExit(){
      return this.exit;
    }

    public SmallGraph(Graph g_){
        g = g_;
        vertices = new ArrayList<Vertex>();
        /*this.monsters = new int[G.monsters][2];
        this.pakkuman = new int[1][2];
        this.candies = new int[G.candies][2];
        int i=0;
        this.pakkuman = G.positions[i];
        i++;
        while(i<1+this.monsters){
            this.monsters[i][0] = G.positions[i][0];
            this.monsters[i][1] = G.positions[i][1];
            i++;
        }
        while(i<1+this.monsters+this.candies){
            this.candies[i][0] = G.positions[i][0];
            this.candies[i][1] = G.positions[i][1];
            i++;
        }

        vertices.add(new Vertex("P",this.pakkuman[0],this.pakkuman[1]);*/
        this.exit = new Vertex("E",g.exit.coordI,g.exit.coordJ);
        vertices.add(this.exit);

        parcourGraph(this.exit, g.exit, new ArrayList<int[]>());

        //for(Vertex v:vertices)
        //    System.out.println(v.toString());
    }


    public Edge parcourGraph(Vertex lastVertex, Node currentNode, ArrayList<int[]> oldway){
        ArrayList<Node> neighbours = new ArrayList<Node>();
        neighbours=currentNode.getNeighbours();

        ArrayList<int[]> way = new ArrayList<int[]>();
        way.addAll(oldway);

        Node oldNode = currentNode;
        boolean specialNode = false; // true if found , false if not

        Vertex newVertex = lastVertex;
        Edge newEdge = new Edge(); // we return a empty edge if we don't find a special node

        if ( !currentNode.getStatus().equals(" ") ){ // special Node found
            newVertex = new Vertex(currentNode.getStatus(), currentNode.coordI, currentNode.coordJ); // create new Vertex
            newEdge = newVertex.newEdge(lastVertex, way); // edge from lastVertex to newVertex
            vertices.add(newVertex); // add the new Vertex to the small graph
            specialNode = true;
        }

        currentNode.incCounter(); // visited
        int[] coords = new int[2]; // coords of node
        coords[0] = currentNode.coordI; coords[1] = currentNode.coordJ;
        way.add(coords); // add to way

        while( ( neighbours.size()==2 || ((currentNode.getStatus() == "P") && (neighbours.size() ==1)) ) && !specialNode ){ //go way
            oldNode = currentNode;
            if( !currentNode.nextExists() )
                return newEdge;
            currentNode = currentNode.nextNode();
            if( currentNode.getCounter() > 0){
                oldNode = currentNode;
                currentNode = currentNode.nextNode();
            }
            currentNode.incCounter();
            coords = new int[2];
            coords[0] = currentNode.coordI; coords[1] = currentNode.coordJ;
            way.add(coords);
            neighbours = currentNode.getNeighbours();

            if ( !currentNode.getStatus().equals(" ") ){
                newVertex = new Vertex(currentNode.getStatus(), currentNode.coordI, currentNode.coordJ);
                newEdge = newVertex.newEdge(lastVertex, way);
                vertices.add(newVertex);
                specialNode = true;
            }
        }

        ArrayList<Edge> edges = new ArrayList<Edge>(); // save all the following edges that are created in the recursive calls
        Edge e;
        if( specialNode ){
            way = new ArrayList<int[]>();
            way.add(coords);
        }

        for ( Node neighb: neighbours)
        {
            if( neighb != oldNode && neighb.getCounter() == 0 ){ // we test if we aren't going from were we come
                e = parcourGraph(newVertex, neighb, way); // recursive call
                if( !e.isEmpty() ) // we only add the new edge if it leads to a special node
                    edges.add(e);
            }
        }

        if( neighbours.size() == 2 ) // return an empty edge if the special node is not on a crossover
            newEdge = new Edge();

        for(Edge edge:edges){ // we connect every vertex with every other vertex accessible
            for(Edge otherEdge:edges){
                if( edge != otherEdge)
                    edge.getTarget().newEdge(otherEdge.getTarget(), edge.shareWay(otherEdge));
            }
        }
        return newEdge;//(newVertex == lastVertex) ? newEdge : new Edge("Empty");
    }

  public void printIt(){
    System.out.println(vertices.size());
    for (int i=0;i<vertices.size();i++){
      System.out.println(vertices.get(i));

    }
  }

}
