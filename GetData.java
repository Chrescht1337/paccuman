import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.Hashtable;


public class GetData {
    public static String[] initLab;
    public static int monsters;
    public static int candies;
    public static int m;
    public static int n;
    public static int[][] positions;

    private static void saveInfos(BufferedReader reader){
        try {
            // This will reference one line at a time
            String line = reader.readLine();

            String[] parts = line.split(" fois ");
            m = Integer.parseInt(parts[0].substring(12));
            n = Integer.parseInt(parts[1]);

            int nbrLines = (m*2) + 1;
            initLab = new String[nbrLines];

            int k = 0;
            while((line = reader.readLine()) != null && k < nbrLines) {
                System.out.println(line);
                initLab[k] = line;
                k++;
            }
            //enregrister monstres, bonbons et pakkuman:
            line = reader.readLine();
            monsters = Integer.parseInt(line.substring(11));
             line = reader.readLine();
            candies = Integer.parseInt(line.substring(10)); line = reader.readLine();

            positions = new int[1+monsters+candies][2];
            int j = 0, i = 0;
            while((line = reader.readLine()) != null)
            {
                i=0;
                for(String retval: line.split("\\("))
                {
                    if (i>0)
                    {
                        String[] parts2 = retval.split(",");
                        positions[j][0] = Integer.parseInt(parts2[0]);
                        positions[j][1] = Integer.parseInt(parts2[1].substring(0,1));
                        j++;
                    }
                    i++;
                }
            }
        }
        catch(IOException ex) {
            System.out.println( "Error reading file ");
        }
    }


    public static void main(String [] args) {

        // The name of the file to open.
        String fileName = args[0];

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            saveInfos(bufferedReader);

            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println( "Unable to open file '" + fileName + "'");
        }
        catch(IOException ex) {
            System.out.println( "Error reading file '" + fileName + "'");
        }

        Graph g = new Graph(m,n,monsters,candies,initLab,positions);
        //g.printGraph();
        SmallGraph gg=new SmallGraph(g);

        //gg.printEdges();
        PakkumanWay pW = new PakkumanWay(gg.getGraphVertices(),monsters,candies);
        //Hashtable<Vertex,Hashtable<Vertex,Integer>> dimTot= new Hashtable<Vertex,Hashtable<Vertex,Integer>>();
        //Hashtable<Vertex,Hashtable<Vertex,Vertex>> predTot= new Hashtable<Vertex,Hashtable<Vertex,Vertex>>();
        //Hashtable<Vertex,Integer> dim = new Hashtable<Vertex,Integer>();
        //Hashtable<Vertex,Vertex> pred = new Hashtable<Vertex,Vertex>();
        //ArrayList<Vertex> graphVertices= gg.getGraphVertices();
        /*for (int i=0;i<graphVertices.size();i++){
          Hashtable<Vertex,Integer> dim = new Hashtable<Vertex,Integer>();
          Hashtable<Vertex,Vertex> pred = new Hashtable<Vertex,Vertex>();
          pD.dijkstra(graphVertices.get(i),graphVertices,dim,pred);
          dimTot.put(graphVertices.get(i),dim);
          predTot.put(graphVertices.get(i),pred);
        //gg.printIt();
        }

        for (int i=0;i<graphVertices.size();i++){
          for (int j=0;j<graphVertices.size();j++){
            System.out.print(graphVertices.get(i));
            System.out.print(" - distance to : ");
            System.out.print(graphVertices.get(j));
            System.out.print(" = ");
            System.out.println(dimTot.get(graphVertices.get(i)).get(graphVertices.get(j)));

          }

        }
        */
    }
}
