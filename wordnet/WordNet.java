import java.util.Iterator;
import java.lang.IllegalArgumentException;
import java.lang.String;
import java.lang.Integer;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.BST;
import edu.princeton.cs.algs4.ResizingArrayQueue;

public class WordNet {
  private String[] glosses;
  private String[] synsets;
  //BST<String,int> nouns;
  private BST<String,ResizingArrayQueue<Integer>> nouns;
  private Digraph DAG;
  private SAP sap;
  private int size;

   // constructor takes the name of the two input files
  public WordNet(String synset, String hypernyms) {
    if (synset==null||hypernyms==null)
      throw new IllegalArgumentException();
    In in = new In(synset);
    int i = 0;
    while (in.readLine()!=null)
      i++;
    size = i;
    glosses = new String[size];
    synsets = new String[size];
    //nouns = new BST<String,int>();
    nouns = new BST<String,ResizingArrayQueue<Integer>>();
    DAG = new Digraph(size);
    in = new In(synset);
    i = 0;
    do {
      String line = in.readLine();
      String[] splitLine = line.split(",",3);
      synsets[i] = splitLine[1];
      glosses[i] = splitLine[2];
      String[] splitNouns = splitLine[1].split(" ");
      for (int j = 0; j<splitNouns.length; j++) {
        if (nouns.contains(splitNouns[j])) 
          nouns.get(splitNouns[j]).enqueue(i);
        else {
          ResizingArrayQueue<Integer> q = new ResizingArrayQueue<Integer>();
          q.enqueue(i);
          nouns.put(splitNouns[j],q);
        }
        //nouns.put(splitNouns[j],i);
      }
      i++;
    } while (in.hasNextLine());
    in = new In(hypernyms);
    do {
      String line = in.readLine();
      String[] splitLine = line.split(",");
      int first = Integer.parseInt(splitLine[0]);
      for (i = 1; i<splitLine.length; i++) {
        int last = Integer.parseInt(splitLine[i]);
        DAG.addEdge(first,last);
      }
    } while (in.hasNextLine());
    sap = new SAP(DAG);
  }

   // returns all WordNet nouns
  public Iterable<String> nouns() {
    return nouns.keys();
  }

   // is the word a WordNet noun?
  public boolean isNoun(String word) {
    if (word==null)
      throw new IllegalArgumentException();
    return nouns.contains(word);
  }

   // distance between nounA and nounB (defined below)
  public int distance(String nounA, String nounB) {
    if (nounA==null||nounB==null||!isNoun(nounA)||!isNoun(nounB))
      throw new IllegalArgumentException();
    Iterable<Integer> iterA = new Iterable<Integer>() {
      public Iterator<Integer> iterator() {
        return nouns.get(nounA).iterator();
      }
    };
    Iterable<Integer> iterB = new Iterable<Integer>() {
      public Iterator<Integer> iterator() {
        return nouns.get(nounB).iterator();
      }
    };
    return sap.length(iterA,iterB);
  }

   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
   // in a shortest ancestral path (defined below)
  public String sap(String nounA, String nounB) {
    if (nounA==null||nounB==null||!isNoun(nounA)||!isNoun(nounB))
      throw new IllegalArgumentException();
    Iterable<Integer> iterA = new Iterable<Integer>() {
      public Iterator<Integer> iterator() {
        return nouns.get(nounA).iterator();
      }
    };
    Iterable<Integer> iterB = new Iterable<Integer>() {
      public Iterator<Integer> iterator() {
        return nouns.get(nounB).iterator();
      }
    };
    return synsets[sap.ancestor(iterA,iterB)];
  }
  
   // do unit testing of this class
  public static void main(String[] args) {
  }
}