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


	public Output(GetData data_){
		//this.data = data_;
		this.labyrinthe = data_.getLab();
		this.positions = data_.getPositions();
		this.m = data_.getM();
		this.n = data_.getN();;
		this.monsters = data_.getMonsters();
		this.candies = data_.getCandies();

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
			int i = 0, j, k;
			while( i < ((m*2)+1) ){
				String line = labyrinthe[i];
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
						line = line.substring( 0, k )+type+line.substring( ++k );
					}
				}
				writer.println(line);
				System.out.println(line);
				i++;
			}
			writer.close();
		}
        catch(FileNotFoundException ex) {
            System.out.println( "Unable to open file ");
        }
    }

    public static void main(String[] args){
    	Output o = new Output(new GetData(args[0]));
    	o.writeInitialSituation(); 
    }
}