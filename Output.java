import java.util.ArrayList;
import java.io.*;
import java.util.Collections;

public class Output{
	private String[] labyrinthe;
	private int[][] positions; // positions de pakkuman, bonbons et monstres
	private int m; // hauteur
	private int n; // longueur
	private int candies;
	private int monsters;
	private int monsterCntr;
	private int candiesCntr;
	private int newLine;
	private ArrayList<int[]> wayPos;
	private String wayStr;
	private int lengthWay; // longueur du chemin trouvé
	private boolean foundExit;


	public Output(GetData data_){
		this.labyrinthe = data_.getLab();
		this.positions = data_.getPositions();
		this.m = data_.getM();
		this.n = data_.getN();;
		this.monsters = data_.getMonsters();
		this.candies = data_.getCandies();
		this.monsterCntr = 0;
		this.candiesCntr = 0;
		this.newLine = 0;
		this.wayStr = "";
		this.wayPos = new ArrayList<int[]>();
		this.lengthWay=0;

	}

	private ArrayList<Integer> findPosition(int j){ // teste si des pakkuman/bonbons/monstres ce trouve dans la ligne j du lab
		ArrayList<Integer> res = new ArrayList<Integer>();
		for(int i=0; i<1+monsters+candies; i++){
			if( positions[i][0] == j )
				res.add(i);
		}
		return res;
	}

	public void writeInitialSituation(){ // écrit la situation de départ dans un fichier texte
		try {
			PrintWriter writer = new PrintWriter(new File("initialSituation.txt"));
			writer.println("Situation de départ:");
			int i = 0, j, k;
			while( i < ((m*2)+1) ){ // marquer les specials (monstre/bonbon/pakkuman)
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
							type = "o"; //bonbon
						k = (positions[j][1]*4) + 2;
						labyrinthe[i] = labyrinthe[i].substring( 0, k )+type+labyrinthe[i].substring( ++k ); // remplacer
					}
				}
				writer.println(labyrinthe[i]); // écrire dans le fichier
				i++;
			}
			writer.close();
		}
		catch(FileNotFoundException ex) {
			System.out.println( "Unable to open file "+ex);
		}
	}

  private void addToWay(int i, int j){ // ajouter les coordonées au chemin
  	this.wayStr += getStringCoords(i,j);
  	int[] pos = {i,j};
  	this.wayPos.add(pos);
  	this.newLine++;
  	if( this.newLine%13 == 0 ) // à la ligne
  	this.wayStr += "\n";
  }

    public void writeFinalSituation(ArrayList<Vertex> way, int candiesCollected, boolean foundExit){
    // écrire la situtation final dans un fichier texte
			this.foundExit=foundExit;
    	try {
    		PrintWriter writer = new PrintWriter(new File("finalSituation.txt"));
    		writer.println("Situation finale:");

    		int coordI, coordJ;
    		Vertex currentV =new Vertex();
    		Vertex nextV =new Vertex();
    		Edge currentEdge = new Edge();
    		String type;
    		int i, j;

    		for (int k=0;k<way.size()-1;k++){
    			currentV=way.get(k);
    			nextV=way.get(k+1);
    			for( Edge edge:currentV.getAdjacencies() ){
    				if( edge.getTarget()==nextV ){
    					currentEdge = edge;
    					break;
    				}
    			}
					ArrayList<int[]> tmp = currentEdge.getWay();
					Collections.reverse(tmp);
					for( int[] coords: tmp){ // marquer le chemin de pakkuman dans le labyrinthe
						lengthWay++;
						i = coords[0]; j = coords[1];
						addToWay(i, j);
						i = (i*2)+1;
						j = (j*4)+2;
						if ((i-1)/2<this.n && (j-2)/4<this.m){
							type = Character.toString( labyrinthe[i].charAt(j) );
							if( type.equals(" ") ){ // si pas de monstre/pakkuman/bonbon
								labyrinthe[i] = labyrinthe[i].substring( 0, j )+"#"+labyrinthe[i].substring( ++j );
							}
						}
					}
					lengthWay--;
			}
			for(String line:this.labyrinthe)
				writer.println(line);

			if( foundExit ){
				writer.println("Trouvé un plus court chemin de longueur "+lengthWay+".");
			}
			else
				writer.println("Il n'y a pas moyen de sortir.");
			writer.println("M. Pakkuman à pris "+candiesCollected+" Bonbons!");
			writer.print(this.wayStr);
			writer.close();
		}
		catch(FileNotFoundException ex) {
			System.out.println( "Unable to open file "+ex);
		}
	}

	private String getStringCoords(int i, int j){ // ex:" (i,j)"
		return " ("+Integer.toString(i)+","+Integer.toString(j)+")";
	}

   private String getDirection(int[] oldPos, int[] newPos){ // compare old et new Position et retourne le mouvement de old vers new
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

	public void terminalOutput(){ // affiche le resultat de l'algorithme dans le terminal

		int i =0;
		String line;
		System.out.println("\nLe labyrinthe a un dimension "+m+" fois "+n);
		System.out.println("Il contient "+monsters+" monstres et "+candies+" bonbons.");
		System.out.println("M. Pakkuman se trouve en position:"+getStringCoords(positions[i][0], positions[i][1]) );
		line = "Les monstres se trouvent en position:";
		while( i<1+monsters ){ // afficher les coordonnées de tous les monstres
			line += getStringCoords(positions[i][0], positions[i][1]);
			i++;
		}
		System.out.println(line);
		line = "Les bonbons se trouvent en position:";
		while( i<1+monsters+candies ){ // afficher les coordonnées de tous les bonbons
			line += getStringCoords(positions[i][0], positions[i][1]);
			i++;
		}
		if( foundExit ){ // si pakku a trouvé la sortie:
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
					if (currentPos[0]<this.m && currentPos[1]<this.n){
						line = getDirection(oldPos, currentPos);
						if( Character.toString( labyrinthe[currentPos[0]*2+1].charAt(currentPos[1]*4+2) ).equals("o") ){
							line += ", bonbon récolté";
							cCntr++;
						}
						else if( Character.toString( labyrinthe[currentPos[0]*2+1].charAt(currentPos[1]*4+2) ).equals("M") ){
							line += ", bonbon donné au petit monstre";
							mCntr++;
						}
					}
				}
				i++;

				System.out.println(Integer.toString(i)+"."+getStringCoords(currentPos[0],currentPos[1])+line);
			}
			System.out.println("Trouvé un plus court chemin de longueur "+(i)+".");
			System.out.println("M. Pakkuman a récolté "+cCntr+" bonbons et rencontré "+mCntr+" monstres.");
		}
		else if(wayPos.size()>0){
			int[] last = wayPos.get(wayPos.size()-1);
			System.out.println("Il n'y a pas moyen de sortir vive du labyrinthe\n car le monstre "+getStringCoords(last[0], last[1])+" nous empêche de sortir.");
		}
	}
}
