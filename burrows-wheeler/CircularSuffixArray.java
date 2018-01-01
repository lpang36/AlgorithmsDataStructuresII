import java.util.Arrays;
import java.lang.String;
import java.util.Comparator;
import java.lang.Math;
import java.lang.Integer;
import java.lang.IllegalArgumentException;

public class CircularSuffixArray {
  private Integer[] index;
  public CircularSuffixArray(String s) {
    if (s==null)
      throw new IllegalArgumentException();
    index = new Integer[s.length()];
    for (int i = 0; i<s.length(); i++) 
      index[i] = new Integer(i);
    Arrays.sort(index, new Comparator<Integer>() {
      public int compare(Integer a, Integer b) {
        return compare(a,b,0);
      }
      private int compare(Integer a, Integer b, int i) {
        char A = s.charAt((a+i)%s.length());
        char B = s.charAt((b+i)%s.length());
        if (A==B) {
          if (i<s.length()-1)
            return compare(a,b,i+1);
          else
            return 0;
        }
        return A-B;
      }
    });
    /*
    for (int i = 0; i<s.length(); i++) {
      for (int j = 0; j<s.length(); j++) 
        System.out.print(csa[i][j]+" ");
      System.out.println();
    }
    for (int i = 0; i<s.length(); i++)
      System.out.print(index[i]+" ");
      */
  }// circular suffix array of s
  public int length() {
    return index.length;
  }// length of s
  public int index(int i) {
    if (i<0||i>=index.length)
      throw new IllegalArgumentException();
    return index[i];
  }// returns index of ith sorted suffix
  public static void main(String[] args) {
    
  }// unit testing (required)
}