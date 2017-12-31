import java.util.Arrays;
import java.lang.String;
import java.util.Comparator;
import java.lang.Math;
import java.import java.lang.IllegalArgumentException;

public class CircularSuffixArray {
  private int[] index;
  public CircularSuffixArray(String s) {
    if (s==null)
      throw new IllegalArgumentException();
    char[][] temp = new char[s.length()][s.length()+1];
    for (int i = 0; i<s.length(); i++) {
      for (int j = i; j<i+s.length(); j++) 
        temp[i][j-i] = s.charAt(j%s.length());
      temp[i][s.length()] = (char)i;
    }
    Arrays.sort(temp, new Comparator<char[]>() {
      public int compare(char[] a, char[] b) {
        return compare(a,b,0);
      }
      private int compare(char[] a, char[] b, int i) {
        if (a[i]==b[i]) {
          if (i<Math.min(a.length,b.length)-1)
            return compare(a,b,i+1);
          else if (i<a.length-1&&i>=b.length-1)
            return 1;
          else if (i>=a.length-1&&i<b.length-1)
            return -1;
          else
            return 0;
        }
        return a[i]-b[i];
      }
    });
    index = new int[s.length()];
    for (int i = 0; i<s.length(); i++)
      index[i] = (int)temp[i][temp[0].length-1];
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