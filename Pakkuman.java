class Pakkuman{
  public static void main(String[] args){
    GetData gD=new GetData(args[0]);
    Graph g = new Graph(gD.getM(),gD.getN(),gD.getMonsters(),gD.getCandies(),gD.getLab(),gD.getPositions());
    SmallGraph gg=new SmallGraph(g);

    PakkumanWay pW = new PakkumanWay(gg.getGraphVertices(),gD.getMonsters(),gD.getCandies());
    Output output=new Output(gD);
    output.writeInitialSituation();
    output.writeFinalSituation(pW.getPakkumanWay(),pW.getNbrOfCandiesCollected(),pW.exitFound());
    output.terminalOutput();

  }
}
