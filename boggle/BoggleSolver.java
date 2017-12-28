import java.lang.String;
import java.util.HashSet;
import java.lang.Math;
import edu.princeton.cs.algs4.ResizingArrayQueue;

public class BoggleSolver
{
  private class TrieNode {
    TrieNode[] children;
    char value;
    TrieNode(char c, TrieNode t) {
      value = c;
      children = new TrieNode[26];
      for (int i = 0; i<26; i++)
        children[i] = null;
      if (t!=null)
        t.children[value-'A'] = this;
    }
  }
  
  private class BoardNode {
    int value;
    int depth;
    boolean[] allowed;
    boolean end;
    ResizingArrayQueue<BoardNode> children;
    BoardNode(int v, BoardNode p) {
      value = v;
      children = new ResizingArrayQueue<BoardNode>();
      if (p!=null) {
        depth = p.depth+1;
        p.children.enqueue(this);
        p.deadEnd = false;
        allowed = p.allowed.clone();
      }
      else {
        depth = 0;
        allowed = new boolean[16];
        for (int i = 0; i<16; i++)
          allowed[i] = true;
      }
      allowed[value] = false;
      end = false;
    }
    void makeBoardGraph(ResizingArrayQueue<Integer>[] adj, int max) {
      if (depth==max)
        return;
      if (value>=0&&value<16) {
        for (int i : adj[value]) {
          if (allowed[i]) 
            BoardNode temp = new BoardNode(i,this);
        }
      }
      else {
        for (int i = 0; i<16; i++) {
          if (allowed[i]) 
            BoardNode temp = new BoardNode(i,this);
        }
      }
      for (BoardNode child : children)
        child.makeBoardGraph(adj,max);
    }
  }
  
  private BoardNode root;
  private TrieNode dict;
  private int max;
  
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
  public BoggleSolver(String[] dictionary) {
    root = new BoardNode(-1,null);
    dict = new TrieNode((char)0,null);
    max = 0;
    for (String word : dictionary) {
      max = Math.max(word.length(),max);
      TrieNode current = dict;
      for (int i = 0; i<word.length(); i++) {
        char c = word.charAt(i);
        if (c=='Q'&&i<word.length()-1&&word.charAt(i+1)=='U')
          i++;
        if (current.children[c-'A']==null) 
          TrieNode temp = new TrieNode(c,current);
        current = temp;
        if (i==word.length()-1)
          current.end = true;
      }
    }
    ResizingArrayQueue<Integer>[] adj = new ResizingArrayQueue<Integer>[16];
    for (int i = 0; i<4; i++) {
      for (int j = 0; j<4; j++) {
        for (int k = -1; k<2; k++) {
          for (int l = -1; l<2; l++) {
            if ((k!=0||l!=0)&&i+k>=0&&i+k<4&&j+l>=0&&j+l<4)
              adj[4*i+j].enqueue(4*(i+k)+j+l);
          }
        }
      }
    }
    root.makeBoardGraph(adj,max);
  }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
  public Iterable<String> getAllValidWords(BoggleBoard board) {
  }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
  public int scoreOf(String word) {
    if (word.length()<3||word.length()>max)
      return 0;
    TrieNode current = root;
    for (int i = 0; i<word.length()&&current!=null; i++) {
      current = current.children[word.charAt(i)-'A'];
    }
    if (current==null||!current.end)
      return 0;
    if (word.length()>=8)
      return 11;
    int[] score = new int[] {0,0,0,1,1,2,3,5};
    return score[word.length()];
  }
}
