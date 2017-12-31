import java.util.ArrayList;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.BinaryStdIn;

public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
  public static void encode() {
    int[] last = new int[256];
    for (int i = 0; i<last.length; i++)
      last[i] = -1;
    ArrayList<Integer> s = new ArrayList<Integer>();
    int count = 0;
    while(!BinaryStdIn.isEmpty()) {
      int c = (int)BinaryStdIn.readChar();
      int pos = c;
      int start = 0;
      if (last[c]!=-1) {
        pos = 0;
        start = last[c];
      }
      for (int i = start+1; i<s.size(); i++) {
        if (s.get(i)>pos)
          pos++;
      }
      last[c] = count;
      s.add(pos);
      count++;
      //System.out.println((char)pos);
      BinaryStdOut.write(pos,8);
    }
  }

    // apply move-to-front decoding, reading from standard input and writing to standard output
  public static void decode() {
    ArrayList<Integer> s = new ArrayList<Integer>();
    ArrayList<Character> chars = new ArrayList<Character>();
    int count = 0;
    while(!BinaryStdIn.isEmpty()) {
      int c = BinaryStdIn.readInt(8);
      int pos = c;
      char out;
      int i;
      for (i = s.size()-1; i>=0&&pos!=0; i--) {
        if (s.get(i)>=pos)
          pos--;
      }
      if (i<0) 
        out = (char)pos;
      else
        out = chars.get(i);
      s.add(c);
      chars.add(out);
      count++;
      //System.out.println((char)out);
      BinaryStdOut.write(out,8);
    }
  }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
  public static void main(String[] args) {
    if (args[0].equals("-"))
      MoveToFront.encode();
    else if (args[0].equals("+"))
      MoveToFront.decode();
  }
}