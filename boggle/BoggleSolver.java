import java.lang.String;
import java.lang.StringBuilder;
import java.util.HashSet;
import java.lang.Math;
import java.util.ArrayList;
import edu.princeton.cs.algs4.ResizingArrayQueue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver
{
  private class TrieNode {
    TrieNode[] children;
    TrieNode parent;
    char value;
    boolean end;
    boolean qparent;
    int depth;
    TrieNode(char c, TrieNode t) {
      value = c;
      children = new TrieNode[26];
      for (int i = 0; i<26; i++)
        children[i] = null;
      parent = t;
      if (t!=null) {
        t.children[value-'A'] = this;
        depth = t.depth+1;
      }
      else
        depth = 0;
      if (c=='Q'||(t!=null&&t.qparent))
        qparent = true;
      else
        qparent = false;
      end = false;
    }
  }
  
  private TrieNode dict;
  private int max;
  private HashSet<String> set;
  private boolean[] allowed;
  
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
  public BoggleSolver(String[] dictionary) {
    dict = new TrieNode((char)0,null);
    max = 0;
    for (String word : dictionary) {
      max = Math.max(word.length(),max);
      TrieNode current = dict;
      for (int i = 0; i<word.length(); i++) {
        char c = word.charAt(i);
        if (c=='Q'&&i<word.length()-1&&word.charAt(i+1)=='U')
          i++;
        else if (c=='Q')
          break;
        if (current.children[c-'A']==null) 
          current = new TrieNode(c,current);
        else
          current = current.children[c-'A'];
        if (i==word.length()-1)
          current.end = true;
      }
    }
  }
  
  private StringBuilder buildString(TrieNode t) {
    StringBuilder s;
    if (t.parent==dict) 
      s = new StringBuilder();
    else
      s = buildString(t.parent);
    if (t.value=='Q')
      s.append("QU");
    else
      s.append(t.value);
    return s;
  }
  
  private void getAllValidWords(BoggleBoard board, int b, TrieNode t, int r, int c) {
    if ((t.depth>=3||(t.depth==2&&t.qparent))&&t.end) {
      String s = buildString(t).toString();
      set.add(s);
    }
    if (b>=0&&b<r*c) {
      allowed[b] = false;
      int i = b/c;
      int j = b%c;
      for (int k = -1; k<2; k++) {
        for (int l = -1; l<2; l++) {
          if ((k!=0||l!=0)&&i+k>=0&&i+k<r&&j+l>=0&&j+l<c&&allowed[(i+k)*c+(j+l)]) {
            int ind = board.getLetter(i+k,j+l)-'A';
            if (t.children[ind]!=null)
              getAllValidWords(board,(i+k)*c+(j+l),t.children[ind],r,c);
          }
        }
      }
      allowed[b] = true;
    }
    else {
      for (int i = 0; i<r*c; i++) {
        int ind = board.getLetter(i/c,i%c)-'A';
        getAllValidWords(board,i,t.children[ind],r,c);
      }
    }
  }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
  public Iterable<String> getAllValidWords(BoggleBoard board) {
    allowed = new boolean[board.rows()*board.cols()];
    for (int i = 0; i<board.rows()*board.cols(); i++)
      allowed[i] = true;
    set = new HashSet<String>();
    getAllValidWords(board,-1,dict,board.rows(),board.cols());
    return set;
  }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
  public int scoreOf(String word) {
    if (word.length()<3||word.length()>max)
      return 0;
    TrieNode current = dict;
    for (int i = 0; i<word.length()&&current!=null; i++) {
      current = current.children[word.charAt(i)-'A'];
      if (word.charAt(i)=='Q'&&i<word.length()-1&&word.charAt(i+1)=='U')
        i++;
    }
    if (current==null||!current.end)
      return 0;
    if (word.length()>=8)
      return 11;
    int[] score = new int[] {0,0,0,1,1,2,3,5};
    return score[word.length()];
  }
  
  public static void main(String[] args) {
    In in = new In(args[0]);
    String[] dictionary = in.readAllStrings();
    BoggleSolver solver = new BoggleSolver(dictionary);
    BoggleBoard board = new BoggleBoard(args[1]);
    int score = 0;
    for (String word : solver.getAllValidWords(board)) {
        StdOut.println(word);
        score += solver.scoreOf(word);
    }
    StdOut.println("Score = " + score);
  }
}
