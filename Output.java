import java.util.ArrayList;
import java.io.*;


public class Output{
	private String[] labyrinthe;
	private int[][] positions;
	private int m;
	private int n;
	private int candies;
	private int monsters;
	private int monsterCntr = 0;
//	private String way = "Déplacements de M. Pakkuman:\n";
	private int newLine = 0;
	private ArrayList<int[]> wayPos;
	private String wayStr = "";
	private int length;


	public Output(GetData data_){
		//this.data = data_;
		this.labyrinthe = data_.getLab();
		this.positions = data_.getPositions();
		this.wayPos = new ArrayList<int[]>();
		this.m = data_.getM();
		this.n = data_.getN();;
		this.monsters = data_.getMonsters();
		this.candies = data_.getCandies();
		this.wayStr = "";
		this.length=0;

	}

	private ArrayList<Integer> findPosition(int j){
		ArrayList<Integer> res = new ArrayList<Integer>();
		for(int i=0; i<1+monsters+candies; i++){
			if( positions[i][0] == j )
				res.add(i);
		}
		return res;
	}

	public void writeInitialSituation(){
		try {
			PrintWriter writer = new PrintWriter(new File("initialSituation.txt"));
			writer.println("Situation de départ:");
			int i = 0, j, k;
			while( i < ((m*2)+1) ){
				if( i%2 == 1){
					j = i/2;
					ArrayList<Integer> specials = findPosition(j);
					while(specials.size() > 0){
						j = specials.remove(0);
						String type;
						if (j==0)
							type = "P";
						else if (j < 1+monsters){
							type = "M";
						}
						else
							type = "o";
						k = (positions[j][1]*4) + 2;
						labyrinthe[i] = labyrinthe[i].substring( 0, k )+type+labyrinthe[i].substring( ++k );
					}
				}
				writer.println(labyrinthe[i]);
				System.out.println(labyrinthe[i]);
				i++;
			}
			writer.close();
		}
        catch(FileNotFoundException ex) {
            System.out.println( "Unable to open file ");
        }
    }

    private void addToWay(int i, int j){
    	this.wayStr += getStringCoords(i,j);
    	int[] pos = {i,j};
    	this.wayPos.add(pos);
    	this.newLine++;
    	if( this.newLine%13 == 0 )
    		this.wayStr += "\n";
    }

    public void writeFinalSituation(ArrayList<Vertex> way, int candiesCollected, boolean foundExit){
    	try {
			PrintWriter writer = new PrintWriter(new File("finalSituation.txt"));
			writer.println("Situation finale:");

			int coordI, coordJ;
			Vertex currentV =new Vertex();
  		Vertex nextV =new Vertex();
  		Edge currentEdge = new Edge();
  		//ArrayList<Edge> nextV_edges;
  		String type;
  		int i, j;


			for (int k=0;k<way.size()-1;k++){
				currentV=way.get(k);
				nextV=way.get(k+1);
				System.out.println(currentV);
				System.out.println(nextV);
				//nextV_edges = nextV.getAdjacencies();
				for( Edge edge:currentV.getAdjacencies() ){
					if( edge.getTarget()==nextV ){
						currentEdge = edge;
						System.out.print("edge : ");
						System.out.println(edge);
						System.out.print("currentEdge : ");
						System.out.println(currentEdge);
						System.out.println();
					}
						break;
				}

					//System.out.println(currentEdge);
					//System.out.println();
				for( int[] coords:currentEdge.getWay() ){
					length++;
					i = coords[0]; j = coords[1];
					/*System.out.print(" - ");
					System.out.println(j);*/
					addToWay(i, j);
					i = (i*2)+1;
					j = (j*4)+2;
					if ((i-1)/2<this.n && (j-2)/4<this.m){
						type = Character.toString( labyrinthe[i].charAt(j) );
						if( type.equals(" ") ){
							labyrinthe[i] = labyrinthe[i].substring( 0, j )+"#"+labyrinthe[i].substring( ++j );
						}
					}
				}
			}

			for(String line:this.labyrinthe)
				writer.println(line);

			if( foundExit ){
				writer.println("Trouvé un plus court chemin de longueur "+length+".");
			}
			else
				writer.println("Il n'y a pas moyen de sortir.");
			writer.println("M. Pakkuman à pris "+candiesCollected+" Bonbons!");
			writer.print(this.wayStr);
			writer.close();

		}
        catch(FileNotFoundException e) {
            System.out.println( "Unable to open file ");
        }

    }

		private String getStringCoords(int i, int j){
    	return " ("+Integer.toString(i)+","+Integer.toString(j)+")";
    }

    private String getDirection(int[] oldPos, int[] newPos){
    	if( oldPos[0] != newPos[0] ){
    		if( oldPos[0] == newPos[0]+1 )
    			return " nord";
    		else
    			return " sud";
    	}
    	else{
    		if( oldPos[1] == newPos[1]+1 )
    			return " est";
    		else
    			return " ouest";
    	}
    }

		public void terminalOutput(){
    	int i =0;
    	String line;
    	System.out.println("\nLe labyrinthe a un dimension "+m+" fois "+n);
    	System.out.println("Il contient "+monsters+" monstres et "+candies+" bonbons.");
    	System.out.println("M. Pakkuman se trouve en position:"+getStringCoords(positions[i][0], positions[i][1]) );
    	line = "Les monstres se trouvent en position:";
    	while( i<1+monsters ){
    		line += getStringCoords(positions[i][0], positions[i][1]);
    		i++;
    	}
    	System.out.println(line);
    	line = "Les bonbons se trouvent en position:";
    	while( i<1+monsters+candies ){
    		line += getStringCoords(positions[i][0], positions[i][1]);
    		i++;
    	}
    	System.out.println(line);
    	System.out.println("\nDéplacements de M. Pakkuman:");
    	i=0;
    	int[] currentPos = wayPos.get(i);
    	int[] oldPos;
    	int mCntr = 0, cCntr = 0;
    	while( i<wayPos.size() ){
    		oldPos = currentPos;
    		currentPos = wayPos.get(i);

    		if( i == 0 )
    			line = " Départ";
    		else if( i == wayPos.size()-1 )
    			line = " Sortie!\n";
    		else{
    			line = getDirection(oldPos, currentPos);
    			if( Character.toString( labyrinthe[currentPos[0]].charAt(currentPos[1]) ) == "o" ){
    				line += ", bonbon récolté";
    				cCntr++;
    			}
    			else if( Character.toString( labyrinthe[currentPos[0]].charAt(currentPos[1]) ) == "M" ){
    				line += ", bonbon donné au petit monstre";
    				mCntr++;
    			}
    		}
    		i++;

    		System.out.println(Integer.toString(i)+"."+getStringCoords(currentPos[0],currentPos[1])+line);
    	}
    	System.out.println("Trouvé un plus court chemin de longueur "+length+".");
    	System.out.println("M. Pakkuman a récolté "+cCntr+" bonbons et rencontré "+mCntr+" monstres.");

    }

    public static void main(String[] args){
    	Output o = new Output(new GetData(args[0]));
    	o.writeInitialSituation();
    }
}
