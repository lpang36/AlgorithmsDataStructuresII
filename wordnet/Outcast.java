import java.lang.Integer;

public class Outcast {
  WordNet net;
  public Outcast(WordNet wordnet) {
    net = wordnet;
  }// constructor takes a WordNet object
  public String outcast(String[] nouns) {
    int min = Integer.MAX_VALUE;
    String minString = "";
    for (int i = 0; i<nouns.length; i++) {
      int sum = 0;
      for (int j = 0; j<nouns.length; j++) {
        if (i!=j) {
          int temp = net.distance(nouns[i],nouns[j]);
          if (temp!=-1)
            sum+=temp;
        }
      }
      if (sum<min) {
        min = sum;
        minString = nouns[i];
      }
    }
    return minString;
  }// given an array of WordNet nouns, return an outcast
  public static void main(String[] args) {
  }// see test client below
}