import java.util.Iterator;
import java.lang.Integer;
import java.lang.IllegalArgumentException;
import edu.princeton.cs.algs4.Digraph;

public class SAP {
  Digraph graph;
  int currentBest;
  int currentAnc;
  int totalMin;
  int totalAnc;
  
   // constructor takes a digraph (not necessarily a DAG)
  public SAP(Digraph G) {
    graph = G; 
  }

  private void DFSHelper(int v, int depth, int[] visitedv, int[] visitedw) {
    visitedv[v] = depth;
    if (visitedw[v]!=-1) {
      int dist = visitedw[v]+depth;
      if (dist<currentBest) { 
        currentBest = dist;
        currentAnc = v;
      }
    }
    Iterable<Integer> adj = graph.adj(v);
    for (int temp : adj) {
      if (visitedv[temp]==-1)
        DFSHelper(temp,depth+1,visitedv,visitedw);
    }
  }
  
   // length of shortest ancestral path between v and w; -1 if no such path
  public int length(int v, int w) {
    if (v<0||v>=graph.V()||w<0||w>=graph.V())
      throw new IllegalArgumentException();
    int[] visitedv = new int[graph.V()];
    int[] visitedw = new int[graph.V()];
    for (int i = 0; i<graph.V(); i++) {
      visitedv[i] = -1;
      visitedw[i] = -1;
    }
    currentBest = Integer.MAX_VALUE;
    currentAnc = -1;
    DFSHelper(v,0,visitedv,visitedw);
    DFSHelper(w,0,visitedw,visitedv);
    if (currentBest==Integer.MAX_VALUE)
      return -1;
    return currentBest;
  }
  
   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
  public int ancestor(int v, int w) {
    if (v<0||v>=graph.V()||w<0||w>=graph.V())
      throw new IllegalArgumentException();
    length(v,w);
    return currentAnc;
  }

   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
  public int length(Iterable<Integer> v, Iterable<Integer> w) {
    totalMin = Integer.MAX_VALUE;
    totalAnc = -1;
    for (int vint : v) {
      Iterable<Integer> copy = w;
      for (int wint : copy) {
        if (vint<0||vint>=graph.V()||wint<0||wint>=graph.V())
          throw new IllegalArgumentException();
        int temp = length(vint,wint);
        if (temp<totalMin) {
          totalAnc = currentAnc;
          totalMin = temp;
        }
      }
    }
    if (totalMin==Integer.MAX_VALUE)
      return -1;
    return totalMin;
  }

   // a common ancestor that participates in shortest ancestral path; -1 if no such path
  public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
    length(v,w);
    return totalAnc;
  }

   // do unit testing of this class
  public static void main(String[] args) {
  }
}