class Vertex implements Comparable<Vertex>
{
    private final String name;
    private Edge[] adjacencies;
    private double minDistance = Double.POSITIVE_INFINITY;
    private Vertex previous;
    public String toString(){
			return name;
		}

		public Vertex(String argName){
			name = argName;
		}

		public int compareTo(Vertex other)
    {
        return Double.compare(minDistance, other.minDistance);
    }
}
