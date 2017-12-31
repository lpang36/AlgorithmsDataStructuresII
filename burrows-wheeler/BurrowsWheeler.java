import java.lang.StringBuilder;
import java.lang.String;
import java.util.ArrayList;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.ResizingArrayQueue;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
  public static void transform() {
    String s = BinaryStdIn.readString();
    CircularSuffixArray csa = new CircularSuffixArray(s);
    for (int i = 0; i<s.length(); i++) {
      if (csa.index(i)==0) {
        //System.out.println(i);
        BinaryStdOut.write(i);
        break;
      }
    }
    StringBuilder sb = new StringBuilder(s.length());
    for (int i = 0; i<s.length(); i++) 
      sb.append(s.charAt((csa.index(i)-1+s.length())%s.length()));
    //System.out.println(sb.toString());
    BinaryStdOut.write(sb.toString());
  }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
  public static void inverseTransform() {
    int start = BinaryStdIn.readInt(); 
    String s = BinaryStdIn.readString();
    ArrayList<ResizingArrayQueue<Integer>> inds = new ArrayList<ResizingArrayQueue<Integer>>();
    for (int i = 0; i<256; i++)
      inds.add(new ResizingArrayQueue<Integer>());
    for (int i = 0; i<s.length(); i++) 
      inds.get((int)s.charAt(i)).enqueue(i);
    char[] sorted = new char[s.length()];
    int count = 0;
    for (int i = 0; i<s.length();) {
      for (int j = 0; j<inds.get(count).size(); j++) {
        sorted[i] = (char)count;
        i++;
      }
      count++;
    }
    int[] next = new int[s.length()];
    for (int i = 0; i<s.length(); i++) 
      next[i] = inds.get((int)sorted[i]).dequeue();
    StringBuilder sb = new StringBuilder(s.length());
    for (int i = start; sb.length()<s.length(); i = next[i]) 
      sb.append(sorted[i]);
    //System.out.println(sb.toString());
    BinaryStdOut.write(sb.toString());
  }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
  public static void main(String[] args) {
    if (args[0].equals("-"))
      BurrowsWheeler.transform();
    else if (args[0].equals("+"))
      BurrowsWheeler.inverseTransform();
  }
}