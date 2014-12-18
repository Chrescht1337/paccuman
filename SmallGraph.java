import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class SmallGraph{
    private ArrayList<Vertex> vertices; // liste de tous les vertex
    private Vertex exit;
    private Graph g;


    public SmallGraph(Graph g_){
      g = g_; // graph des nodes
      vertices = new ArrayList<Vertex>();
      this.exit = new Vertex("E",g.getExit().getCoordI(),g.getExit().getCoordJ());
      vertices.add(this.exit);

      parcourGraph(this.exit, g.getExit(), new ArrayList<int[]>()); // créer les vertex et les edges
    }

    public ArrayList<Vertex> getGraphVertices(){
      return this.vertices;
    }

    public Vertex getExit(){
      return this.exit;
    }

    public Edge parcourGraph(Vertex lastVertex, Node currentNode, ArrayList<int[]> oldway){
      ArrayList<Node> neighbours = new ArrayList<Node>(); // voisins du node courant
      neighbours=currentNode.getNeighbours();

      ArrayList<int[]> way = new ArrayList<int[]>();
      way.addAll(oldway);

      Node oldNode = currentNode;
      boolean specialNode = false; // true si trouvé un monstr/bonbon/pakkuman, false si pas

      Vertex newVertex = lastVertex;
      Edge newEdge = new Edge(); // on returne une edge vide si on ne trouve pas de specialNode

      if ( !currentNode.getStatus().equals(" ") ){ // special Node trouvé
        newVertex = new Vertex(currentNode.getStatus(), currentNode.getCoordI(), currentNode.getCoordJ()); // créer new Vertex
        newEdge = newVertex.newEdge(lastVertex, way); // edge de lastVertex à newVertex
        vertices.add(newVertex);
        specialNode = true;
      }

      currentNode.incCounter(); // examiner
      int[] coords = new int[2]; // coords du node
      coords[0] = currentNode.getCoordI();
      coords[1] = currentNode.getCoordJ();
      way.add(coords); // ajouter au chemin

      while( ( neighbours.size()==2 || ((currentNode.getStatus() == "P") && (neighbours.size() ==1)) ) && !specialNode ){ //go way
        oldNode = currentNode;
        if( !currentNode.nextExists() ) // impasse
          return newEdge;
        currentNode = currentNode.nextNode();
        if( currentNode.getCounter() > 0){ // si déja examiné
          oldNode = currentNode;
          currentNode = currentNode.nextNode();
        }
        currentNode.incCounter(); // examiner
        coords = new int[2];
        coords[0] = currentNode.getCoordI();
        coords[1] = currentNode.getCoordJ();
        way.add(coords); // ajouter au chemin
        neighbours = currentNode.getNeighbours();

        if ( !currentNode.getStatus().equals(" ") ){// special Node trouvé
          newVertex = new Vertex(currentNode.getStatus(), currentNode.coordI, currentNode.coordJ); // créer new Vertex
          newEdge = newVertex.newEdge(lastVertex, way); // edge de lastVertex à newVertex
          vertices.add(newVertex);
          specialNode = true;
        }
      }

      ArrayList<Edge> edges = new ArrayList<Edge>(); // enregrister tous les edges qui sont créee durant les appels récursives
      Edge e;
      if( specialNode ){ // si on a trouvé un monstre/bonbon/pakkuman
        way = new ArrayList<int[]>(); // on vide la liste
        way.add(coords);
      }

      for ( Node neighb: neighbours){
        if( neighb != oldNode && neighb.getCounter() == 0 ){ // we test if we aren't going from were we come
          e = parcourGraph(newVertex, neighb, way); // recursive call
          if( !e.isEmpty() ) // we only add the new edge if it leads to a special node
            edges.add(e);
        }
      }

      if( neighbours.size() == 2 ) // retour d'un edge vide si le dernier node n'est pas un carrefour
        newEdge = new Edge();

      for(Edge edge:edges){ // on connecte les vertex retourné entre eux
        for(Edge otherEdge:edges){
          if( edge != otherEdge)
            edge.getTarget().newEdge(otherEdge.getTarget(), edge.shareWay(otherEdge));
        }
      }
      return newEdge;
    }

  }
