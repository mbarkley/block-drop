package demo.client.local.game.tools;

public class Pacer {
  
  boolean[] track;
  boolean initial;
  
  public Pacer(int size, boolean initial) {
    track = new boolean[size];
    this.initial = initial;
  }
  
  public Pacer(int size) {
    this(size, true);
  }
  
  public boolean isReady() {
    int i;
    for (i = 0; i < track.length; i++) {
      if (!track[i]) {
        break;
      }
    }
    return i == track.length || (initial && i == 1);
  }
  
  public void clear() {
    for (int i = 0; i < track.length; i++) {
      track[i] = false;
    }
  }
  
  public void increment() {
    int i = 0;
    while (i < track.length && track[i]) {
      i++;
    }

    if (i != track.length) {
      track[i] = true;
    }
  }
  
}
