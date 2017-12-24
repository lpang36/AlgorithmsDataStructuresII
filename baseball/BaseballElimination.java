import java.util.Hashtable;
import java.lang.Integer;
import java.util.Iterator;
import java.lang.IllegalArgumentException;
import java.lang.Math;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.ResizingArrayQueue;

public class BaseballElimination {
  private class FlowEdge {
    FlowNode from;
    FlowNode to;
    int capacity;
    int flow;
    FlowEdge(FlowNode f, FlowNode t) {
      this(f,t,Integer.MAX_VALUE);
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
    void addFlow(int x, FlowNode f) {
      if (f==from)
        flow-=x;
      else if (f==to)
        flow+=x;
    }
    void setFlow(int x) {
      flow = x;
      check();
    }
    void check() {
      flow = Math.min(capacity,flow);
      flow = Math.max(0,flow);
    }
    boolean full() {
      return flow==capacity;
    }
    boolean empty() {
      return flow==0;
    }
    FlowNode other(FlowNode f) {
      if (f==to)
        return from;
      else if (f==from)
        return to;
      return null;
    }
    int remainingCapacity(FlowNode f) {
      if (f==from)
        return flow;
      else if (f==to)
        return capacity-flow;
      return 0;
    }
  }
  private class FlowNode {
    ResizingArrayQueue<FlowEdge> in;
    ResizingArrayQueue<FlowEdge> out;
    int val;
    FlowNode(int x) {
      val = x;
      in = new ResizingArrayQueue<FlowEdge>();
      out = new ResizingArrayQueue<FlowEdge>();
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
  private FlowNode[] nodes;
  private boolean[] marked;
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
    nodes = new FlowNode[gameSize+size+1];
    nodes[nodes.length-1] = new FlowNode(nodes.length-1);
    nodes[nodes.length-2] = new FlowNode(nodes.length-2);
    for (int i = 0; i<size; i++) { 
      if (i!=num) {
        FlowNode teamNode = new FlowNode(gameSize+i);
        nodes[gameSize+count] = teamNode;
        teamNode.edgeTo(nodes[nodes.length-1]);
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
            FlowNode game = new FlowNode(count);
            nodes[count] = game;
            nodes[nodes.length-2].edgeTo(game,temp[i][j]);
            game.edgeTo(nodes[gameSize+counti]);
            game.edgeTo(nodes[gameSize+countj]);
            count++;
            countj++;
          }
        }
        counti++;
      }
    }
    count = -1;
    for (FlowEdge edge : nodes[nodes.length-1].in) {
      count++;
      if (count==num)
        count++;
      edge.setCapacity(wins[num]+left[num]-wins[count]);
    }
    ResizingArrayQueue<FlowNode> q = new ResizingArrayQueue<FlowNode>();
    marked = new boolean[nodes.length];
    q.enqueue(nodes[nodes.length-2]);
    marked[nodes.length-2] = true;
    while (!q.isEmpty()) {
      FlowNode n = q.dequeue();
      for (FlowEdge edge : n.out) {
        FlowNode m = edge.to;
        if (!marked[m.val]) {
          edge.setFlow(0);
          marked[m.val] = true;
          q.enqueue(edge.to);
        }
      }
    }
    while (true) {
      q = new ResizingArrayQueue<FlowNode>();
      marked = new boolean[nodes.length];
      FlowEdge[] edgeTo = new FlowEdge[nodes.length];
      while (!q.isEmpty()) {
        FlowNode n = q.dequeue();
        for (FlowEdge edge : n.out) {
          FlowNode m = edge.to;
          if (!marked[m.val]&&!edge.full()) {
            edgeTo[m.val] = edge;
            marked[m.val] = true;
            q.enqueue(m);
          }
        }
        for (FlowEdge edge : n.in) {
          FlowNode m = edge.from;
          if (!marked[m.val]&&!edge.empty()) {
            edgeTo[m.val] = edge;
            marked[m.val] = true;
            q.enqueue(m);
          }
        }
      }
      if (!marked[nodes.length-1])
        break;
      int bottle = Integer.MAX_VALUE;
      FlowNode n = nodes[nodes.length-2];
      for (n = edgeTo[n.val].other(n); n!=nodes[nodes.length-1]; n = edgeTo[n.val].other(n)) 
        bottle = Math.min(bottle,edgeTo[n.val].remainingCapacity(n));
      n = nodes[nodes.length-2];
      for (n = edgeTo[n.val].other(n); n!=nodes[nodes.length-1]; n = edgeTo[n.val].other(n))
        edgeTo[n.val].addFlow(bottle,n);
    }
    for (FlowEdge edge : nodes[nodes.length-2].out) {
      if (!edge.full())
        return false;
    }
    return true;
  }// is given team eliminated?
  public Iterable<String> certificateOfElimination(String team) {
    if (!names.containsKey(team))
      throw new IllegalArgumentException();
    int num = names.get(team);
    isEliminated(team);
    ResizingArrayQueue<String> elims = new ResizingArrayQueue<String>();
    int count = 0;
    for (int i = 0; i<size; i++) {
      if (trivialElimination&&wins[num]+left[num]<wins[i]) 
        elims.enqueue(iterableNames[i]);
      else if (!trivialElimination&&!marked[gameSize+count]&&i!=num) {
        elims.enqueue(iterableNames[i]);
        count++;
      }
    }
    return elims;
  }// subset R of teams that eliminates given team; null if not eliminated
  public static void main(String[] args) {
    BaseballElimination division = new BaseballElimination(args[0]);
    String team = "Philadelphia";
    //for (String team : division.teams()) {
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
    //}
  }
}