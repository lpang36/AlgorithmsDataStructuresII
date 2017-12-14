import java.lang.IllegalArgumentException;
import java.awt.Color;
import java.lang.Math;
import edu.princeton.cs.algs4.Picture;
//import edu.princeton.cs.algs4.Digraph;

public class SeamCarver {
  private int[][][] I;
  private double[][] energy;
  private int w;
  private int h;
  public SeamCarver(Picture picture) {
    if (picture==null)
      throw new IllegalArgumentException();
    w = picture.width();
    h = picture.height();
    I = new int[w][h][3];
    energy = new double[w][h];
    for (int i = 0; i<w; i++) {
      for (int j = 0; j<h; j++) {
        Color c = picture.get(i,j);
        I[i][j][0] = c.getRed();
        I[i][j][1] = c.getGreen();
        I[i][j][2] = c.getBlue();
      }
    }
    for (int i = 0; i<w; i++) {
      for (int j = 0; j<h; j++) {
        if (i==0||i==w-1||j==0||j==h-1)
          energy[i][j] = 1000;
        else {
          double xgrad = 0;
          for (int k = 0; k<3; k++)
            xgrad+=Math.pow((I[i-1][j][k]-I[i+1][j][k]),2);
          double ygrad = 0;
          for (int k = 0; k<3; k++)
            ygrad+=Math.pow((I[i][j-1][k]-I[i][j+1][k]),2);
          energy[i][j] = Math.sqrt(xgrad+ygrad);
        }
      }
    }
  }// create a seam carver object based on the given picture
  public Picture picture() {
    Picture P = new Picture(w,h);
    for (int i = 0; i<w; i++) {
      for (int j = 0; j<h; j++)
        P.set(i,j,new Color(I[i][j][0],I[i][j][1],I[i][j][2]));
    }
    return P;
  }// current picture
  public     int width() {
    return w;
  }// width of current picture
  public     int height() {
    return h;
  }// height of current picture
  public  double energy(int x, int y) {
    if (x<0||x>=w||y<0||h>=h)
      throw new IllegalArgumentException();
    return energy[x][y];
  }// energy of pixel at column x and row y
  public   int[] findHorizontalSeam() {
  }// sequence of indices for horizontal seam
  public   int[] findVerticalSeam() {
  }// sequence of indices for vertical seam
  public    void removeHorizontalSeam(int[] seam) {
    if (seam==null||seam.length!=w||w<=1)
      throw new IllegalArgumentException();
    w--;
  }// remove horizontal seam from current picture
  public    void removeVerticalSeam(int[] seam) {
    if (seam==null||seam.length!=h||h<=1)
      throw new IllegalArgumentException();
    h--;
  }// remove vertical seam from current picture
}