import java.util.Hashtable;
import java.lang.Integer;
import java.util.Iterator;
import java.lang.IllegalArgumentException;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ResizingArrayQueue;

public class BaseballElimination {
  private class FlowEdge {
    FlowNode from;
    FlowNode to;
    int capacity;
    int flow;
    FlowEdge(FlowNode f, FlowNode t) {
      FlowEdge(f,t,Integer.MAX_VALUE);
    }
    FlowEdge(FlowNode f, FlowNode t, int c) {
      from = f;
      to = t;
      capacity = c;
      flow = 0;
    }
    void setCapacity(int x) {
      capacity = x;
    }
    void addFlow(int x) {
      flow+=x;
      check();
    }
    void setFlow(int x) {
      flow = x;
      check();
    }
    void check() {
      flow = min(capacity,flow);
      flow = max(0,flow);
    }
    boolean full() {
      return flow==capacity;
    }
    boolean empty() {
      return flow==0;
    }
  }
  private class FlowNode {
    ResizingArrayQueue<FlowEdge> in;
    ResizingArrayQueue<FlowEdge> out;
    FlowNode() {
    }
    void edgeTo(FlowNode to) {
      edgeTo(to,Integer.MAX_VALUE);
    }
    void edgeTo(FlowNode to, int c) {
      FlowEdge edge = new FlowEdge(this,to,c);
      out.enqueue(edge);
      to.in.enqueue(edge);
    }
  }
  private Hashtable<String,Integer> names;
  private String[] iterableNames;
  private FlowNode source;
  private FlowNode sink;
  private FlowNode[] games;
  private FlowNode[] teams;
  private int[] wins;
  private int[] losses;
  private int[] left;
  private int[][] temp;
  private int size;
  public BaseballElimination(String filename) {
    In in = new In(filename);
    size = in.readInt();
    names = new Hashtable<String,Integer>;
    iterableNames = new String[size];
    wins = new int[size];
    losses = new int[size];
    left = new int[size];
    source = new FlowNode();
    sink = new FlowNode();
    games = new FlowNode[size*(size-1)];
    teams = new FlowNode[size];
    temp = new int[size][size];
    for (int i = 0; i<size; i++) {
      String name = in.readString();
      names.put(name,i);
      iterableNames[i] = name;
      wins[i] = in.readInt();
      losses[i] = in.readInt();
      left[i] = in.readInt();
      for (int j = 0; j<size; j++) 
        temp[i][j] = in.readInt();
    }
    int count = 0;
    for (int i = 0; i<size; i++) { 
      team = new FlowNode();
      teams[i] = team;
      team.edgeTo(sink);
    }
    for (int i = 0; i<size; i++) {
      for (int j = 0; j<i; j++) {
        if (i!=j) {
          FlowNode game = new FlowNode();
          games[count] = game;
          source.edgeTo(game,temp[i][j]);
          game.edgeTo(teams[i]);
          game.edgeTo(teams[j]);
          count++;
        }
      }
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
    if (!names.containsKey(team1))||!names.containsKey(team2))
      throw new IllegalArgumentException();
    return temp[names.get(team1)][names.get(team2)];
  }// number of remaining games between team1 and team2
  public          boolean isEliminated(String team) {
  }// is given team eliminated?
  public Iterable<String> certificateOfElimination(String team) {
  }// subset R of teams that eliminates given team; null if not eliminated
}