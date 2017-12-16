import java.lang.IllegalArgumentException;
import java.awt.Color;
import java.lang.Math;
import java.lang.Integer;
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
    I = new int[h][w][3];
    energy = new double[h][w];
    for (int i = 0; i<h; i++) {
      for (int j = 0; j<w; j++) {
        Color c = picture.get(j,i);
        I[i][j][0] = c.getRed();
        I[i][j][1] = c.getGreen();
        I[i][j][2] = c.getBlue();
      }
    }
    for (int i = 0; i<h; i++) {
      for (int j = 0; j<w; j++) {
        if (i==0||i==h-1||j==0||j==w-1)
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
    for (int i = 0; i<h; i++) {
      for (int j = 0; j<w; j++)
        P.set(j,i,new Color(I[i][j][0],I[i][j][1],I[i][j][2]));
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
    if (x<0||x>=w||y<0||y>=h)
      throw new IllegalArgumentException();
    return energy[y][x];
  }// energy of pixel at column x and row y
  public   int[] findHorizontalSeam() {
    double[] dist = new double[w*h+1];
    int[] from = new int[w*h+1];
    for (int i = 0; i<dist.length; i++)
      dist[i] = Integer.MAX_VALUE;
    for (int i = 0; i<h; i++) {
      dist[i] = energy[i][0];
      from[i] = 0;
    }
    for (int i = 0; i<w-1; i++) {
      for (int j = 0; j<h; j++) {
        for (int k = -1; k<=1; k++) {
          if (j+k>=0&&j+k<h&&dist[i*h+j]+energy[j+k][i+1]<dist[(i+1)*h+j+k]) {
            dist[(i+1)*h+j+k] = dist[i*h+j]+energy[j+k][i+1];
            from[(i+1)*h+j+k] = i*h+j;
          }
        }
      }
    }
    for (int i = 0; i<h; i++) {
      if (dist[(w-1)*h+i]+energy[i][w-1]<dist[dist.length-1]) {
        dist[dist.length-1] = dist[(w-1)*h+i]+energy[i][w-1];
        from[from.length-1] = (w-1)*h+i;
      }
    }
    int[] out = new int[w];
    int count = w-1;
    int current = from[from.length-1];
    while (current!=0&&count>=0) {
      out[count] = current%h;
      count--;
      current = from[current];
    }
    return out;
  }// sequence of indices for horizontal seam
  public   int[] findVerticalSeam() {
    double[] dist = new double[w*h+1];
    int[] from = new int[w*h+1];
    for (int i = 0; i<dist.length; i++)
      dist[i] = Integer.MAX_VALUE;
    for (int i = 0; i<w; i++) {
      dist[i] = energy[0][i];
      from[i] = 0;
    }
    for (int i = 0; i<h-1; i++) {
      for (int j = 0; j<w; j++) {
        for (int k = -1; k<=1; k++) {
          if (j+k>=0&&j+k<w&&dist[i*w+j]+energy[i+1][j+k]<dist[(i+1)*w+j+k]) {
            dist[(i+1)*w+j+k] = dist[i*w+j]+energy[i+1][j+k];
            from[(i+1)*w+j+k] = i*w+j;
          }
        }
      }
    }
    for (int i = 0; i<w; i++) {
      if (dist[(h-1)*w+i]+energy[h-1][i]<dist[dist.length-1]) {
        dist[dist.length-1] = dist[(h-1)*w+i]+energy[h-1][i];
        from[from.length-1] = (h-1)*w+i;
      }
    }
    int[] out = new int[h];
    int count = h-1;
    int current = from[from.length-1];
    while (current!=0&&count>=0) {
      out[count] = current%w;
      count--;
      current = from[current];
    }
    return out;
  }// sequence of indices for vertical seam
  public    void removeHorizontalSeam(int[] seam) {
    if (seam==null||seam.length!=w||w<1)
      throw new IllegalArgumentException();
    int last = seam[0];
    for (int i = 0; i<w; i++) {
      if (seam[i]<0||seam[i]>=h||Math.abs(seam[i]-last)>1)
        throw new IllegalArgumentException();
      last = seam[i];
    }
    h--;
    int[][][] ICopy = new int[h][w][3];
    energy = new double[h][w];
    for (int i = 0; i<w; i++) {
      int count = 0;
      for (int j = 0; j<h+1; j++) {
        if (j!=seam[i]) {
          ICopy[count][i] = I[j][i];
          count++;
        }
      }
    }
    I = ICopy;
    for (int i = 0; i<h; i++) {
      for (int j = 0; j<w; j++) {
        if (i==0||i==h-1||j==0||j==w-1)
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
  }// remove horizontal seam from current picture
  public    void removeVerticalSeam(int[] seam) {
    if (seam==null||seam.length!=h||h<1)
      throw new IllegalArgumentException();
    int last = seam[0];
    for (int i = 0; i<h; i++) {
      if (seam[i]<0||seam[i]>=w||Math.abs(seam[i]-last)>1)
        throw new IllegalArgumentException();
      last = seam[i];
    }
    w--;
    int[][][] ICopy = new int[h][w][3];
    energy = new double[h][w];
    for (int i = 0; i<h; i++) {
      int count = 0;
      for (int j = 0; j<w+1; j++) {
        if (j!=seam[i]) {
          ICopy[i][count] = I[i][j];
          count++;
        }
      }
    }
    I = ICopy;
    for (int i = 0; i<h; i++) {
      for (int j = 0; j<w; j++) {
        if (i==0||i==h-1||j==0||j==w-1)
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
  }// remove vertical seam from current picture
}