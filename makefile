JC = javac
.SUFFIXES: .java .class
.java.class:
				$(JC) $*.java

CLASSES = \
				Edge.java \
				Graph.java \
				SmallGraph.java \
				Vertex.java \
				Node.java \
				PakkumanWay.java \
				GetData.java \
				Output.java \
				MainFile.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
				$(RM) *.class
