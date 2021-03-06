import java.util.Hashtable;
import java.lang.Integer;
import java.util.Iterator;
import java.lang.IllegalArgumentException;
import java.lang.Math;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.ResizingArrayQueue;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;

public class BaseballElimination {
  private Hashtable<String,Integer> names;
  private String[] iterableNames;
  private FordFulkerson FF;
  private int[] wins;
  private int[] losses;
  private int[] left;
  private int[][] temp;
  private int size;
  private int maxWins;
  private boolean trivialElimination;
  private int gameSize;
  public BaseballElimination(String filename) {
    In in = new In(filename);
    size = in.readInt();
    names = new Hashtable<String,Integer>();
    iterableNames = new String[size];
    wins = new int[size];
    losses = new int[size];
    left = new int[size];
    temp = new int[size][size];
    maxWins = 0;
    gameSize = (size-1)*(size-2)/2;
    for (int i = 0; i<size; i++) {
      String name = in.readString();
      names.put(name,i);
      iterableNames[i] = name;
      wins[i] = in.readInt();
      maxWins = Math.max(maxWins,wins[i]);
      losses[i] = in.readInt();
      left[i] = in.readInt();
      for (int j = 0; j<size; j++) 
        temp[i][j] = in.readInt();
    }
  }// create a baseball division from given filename in format specified below
  public              int numberOfTeams() {
    return size;
  }// number of teams
  public Iterable<String> teams() {
    return new Iterable<String>() {
      public Iterator<String> iterator() {
        return new Iterator<String>() {
          int pos = 0;
          public boolean hasNext() {
            return pos<size;
          }
          public String next() {
            pos++;
            return iterableNames[pos-1];
          }
        };
      }
    };
  }// all teams
  public              int wins(String team) {
    if (!names.containsKey(team))
      throw new IllegalArgumentException();
    return wins[names.get(team)];
  }// number of wins for given team
  public              int losses(String team) {
    if (!names.containsKey(team))
      throw new IllegalArgumentException();
    return losses[names.get(team)];
  }// number of losses for given team
  public              int remaining(String team) {
    if (!names.containsKey(team))
      throw new IllegalArgumentException();
    return left[names.get(team)];
  }// number of remaining games for given team
  public              int against(String team1, String team2) {
    if (!names.containsKey(team1)||!names.containsKey(team2))
      throw new IllegalArgumentException();
    return temp[names.get(team1)][names.get(team2)];
  }// number of remaining games between team1 and team2
  public          boolean isEliminated(String team) {
    if (!names.containsKey(team))
      throw new IllegalArgumentException();
    int num = names.get(team);
    trivialElimination = true;
    if (wins[num]+left[num]<maxWins)
      return true;
    trivialElimination = false;
    int count = 0;
    FlowNetwork nodes = new FlowNetwork(gameSize+size+1);
    for (int i = 0; i<size; i++) { 
      if (i!=num) {
        FlowEdge edge = new FlowEdge(gameSize+count,nodes.V()-1,wins[num]+left[num]-wins[i]);
        nodes.addEdge(edge);
        count++;
      }
    }
    count = 0;
    int counti = 0;
    for (int i = 0; i<size; i++) {
      int countj = counti+1;
      if (i!=num) {
        for (int j = i+1; j<size; j++) {
          if (j!=num) {
            FlowEdge edge1 = new FlowEdge(nodes.V()-2,count,temp[i][j]);
            FlowEdge edge2 = new FlowEdge(count,gameSize+counti,Integer.MAX_VALUE);
            FlowEdge edge3 = new FlowEdge(count,gameSize+countj,Integer.MAX_VALUE);
            nodes.addEdge(edge1);
            nodes.addEdge(edge2);
            nodes.addEdge(edge3);
            count++;
            countj++;
          }
        }
        counti++;
      }
    }
    FF = new FordFulkerson(nodes,nodes.V()-2,nodes.V()-1);
    for (FlowEdge edge : nodes.adj(nodes.V()-2)) {
      if (edge.capacity()!=edge.flow())
        return true;
    }
    return false;
  }// is given team eliminated?
  public Iterable<String> certificateOfElimination(String team) {
    if (!names.containsKey(team))
      throw new IllegalArgumentException();
    int num = names.get(team);
    if (isEliminated(team)) {
      ResizingArrayQueue<String> elims = new ResizingArrayQueue<String>();
      int count = 0;
      for (int i = 0; i<size; i++) {
        if (trivialElimination&&wins[num]+left[num]<wins[i]) 
          elims.enqueue(iterableNames[i]);
        else if (!trivialElimination&&i!=num) {
          if (FF.inCut(gameSize+count))
            elims.enqueue(iterableNames[i]);
          count++;
        }
      }
      return elims;
    }
    return null;
  }// subset R of teams that eliminates given team; null if not eliminated
  public static void main(String[] args) {
    BaseballElimination division = new BaseballElimination(args[0]);
    for (String team : division.teams()) {
        if (division.isEliminated(team)) {
            StdOut.print(team + " is eliminated by the subset R = { ");
            for (String t : division.certificateOfElimination(team)) {
                StdOut.print(t + " ");
            }
            StdOut.println("}");
        }
        else {
            StdOut.println(team + " is not eliminated");
        }
    }
  }
}