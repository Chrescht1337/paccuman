//  Marius Küpper - Christian Frantzen
import java.io.*;
public class Graph{
  private int monsters;
  private int candies;
  private int n;
  private int m;
  private Node[][] graphNodes;  //le graphe représenté en fonction de noeuds
  private Node exit;
  private int[][] positions;

  public Graph(int m,int n,int monsters,int candies,String[] labyrinth,int[][] positions)  {
    this.m=m;
    this.n=n;
    this.monsters=monsters;
    this.candies=candies;
    this.graphNodes=new Node[this.n][this.m];
     for (int i=0;i<m;i++)
       for (int j=0;j<n;j++)
         this.graphNodes[i][j]=new Node();
    this.exit = new Node();
    this.positions=positions;
    labScan(labyrinth);

    int i=0;
        this.graphNodes[this.positions[0][0]][this.positions[0][1]].setStatus("P");
        i++;
        while(i<1+this.monsters)        {
            this.graphNodes[this.positions[i][0]][this.positions[i][1]].setStatus("M");
            i++;
        }
        while(i<1+this.monsters+this.candies){
            this.graphNodes[this.positions[i][0]][this.positions[i][1]].setStatus("B");
            i++;
        }
  }

  private boolean inRange(int i,int j){
    return ((0<=i) && (i<m) && (0<=j) && (j<n));
  }

  private void labScan(String[] labyrinth){
    int[] indicesI={0,0,-1,1};
    int[] indicesJ = {-2,2,0,0};
    for (int i=1;i<(m*2+1);i+=2){
      for (int j=2;j<n*4;j+=4){
        for (int decal=0;decal<4;decal++){
          if (labyrinth[i+indicesI[decal]].charAt(j+indicesJ[decal])==' '){
            connectNodes(i/2,j/4,decal);
          }
        }
      }
    }
  }
  private void connectNodes(int i,int j,int decal){
    // if ((i==1) && (decal==3)) //premiere ligne sortie
    // {
    //   this.exit.addNeighbour(this.graphNodes[i][j]);
    // }
    // else if ((j==2) && (decal==0)) // sortie = premiere colonne
    // {
    //   this.exit.addNeighbour(this.graphNodes[i][j]);
    // }
    // else if ((i==m-1) && (decal==2)) // derniere ligne
    // {
    //   this.exit.addNeighbour(this.graphNodes[i][j]);
    // }
    // else if ((j==n-1) && (decal==1))//sortie = dernière colonne
    // {
    //   this.exit.addNeighbour(this.graphNodes[i][j]);
    // }
    // else
    // {
      switch(decal){
        case 0://colonne gauche
          // System.out.println(0);
          // System.out.println(i);
          // System.out.println(j);
          if (!(exitFound(i,j-1)))
            this.graphNodes[i][j].addNeighbour(this.graphNodes[i][j-1]);
        break;
        case 1://colonne droite
          // System.out.println(1);
          // System.out.println(i);
          // System.out.println(j);
          if (!(exitFound(i,j+1)))
            this.graphNodes[i][j].addNeighbour(this.graphNodes[i][j+1]);
        break;
        case 2://ligne supérieure
          // System.out.println(2);
          // System.out.println(i);
          // System.out.println(j);
          if (!(exitFound(i-1,j)))
            this.graphNodes[i][j].addNeighbour(this.graphNodes[i-1][j]);
        break;
        case 3://ligne inférieure
          // System.out.println(3);
          // System.out.println(i);
          // System.out.println(j);
          if (!(exitFound(i+1,j)))
            this.graphNodes[i][j].addNeighbour(this.graphNodes[i+1][j]);
        break;
      }
    //}
  }
  private boolean exitFound(int i,int j){
    if (inRange(i,j))
      return false;
		else{
      if (i<0)
        this.exit.addNeighbour(this.graphNodes[i+1][j]);
      else if (i==m)
        this.exit.addNeighbour(this.graphNodes[i-1][j]);
      else if (j<0)
        this.exit.addNeighbour(this.graphNodes[i][j+1]);
      else if (j==n)
        this.exit.addNeighbour(this.graphNodes[i][j-1]);
      return true;
    }
  }

  public void printGraph(){
    for (int i=0;i<this.m;i++)
      for (int j=0;j<this.n;j++)
        System.out.print(this.graphNodes[i][j].getNbrOfNeighbours());
  }
}
