JC = javac
.SUFFIXES: .java .class
.java.class:
				$(JC) $*.java

CLASSES = \
				Edge.java \
				GetData.java \
				Graph.java \
				SmallGraph.java \
				Vertex.java \
				Node.java \
				PakkumanDijkstra.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
				$(RM) *.class
