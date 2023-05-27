
public class RingStructure {
  
  /** 
   * @param g
   * @return boolean
   */
  public boolean checkForRingTopology(Graph g) {

    //1. number of edges should be equal to number of vertices
    if (g.vertices != g.edges) {
      return false;
    }
    //2. should have more then 2 vertices
    if (g.vertices <= 2) {
      return false;
    }

    //3. check all vertices have degree 2
    //before that remove the vertices which have only 1 connection (i.e in the .txt file they are only once)

   
    //count the connections to other vertices
    int[] checkDegree = new int[g.vertices];
    for (int i= 0; i<g.e.length; i++) {
      int tmp1 = g.e[i].u;
      int tmp2 = g.e[i].v;

      checkDegree[tmp1-1]++;
      checkDegree[tmp2-1]++;
    }
    //get rid of the vertices with only one connection

    for (int i= 0; i<checkDegree.length; i++) {
      if (checkDegree[i] == 0) {
        checkDegree[i] = -1;
      }
      if (checkDegree[i] == 1) {
        //Reduce the degree of the connected vertex by 1
        for (int j= 0; j<g.e.length; j++) {
          if (g.e[j].u == i+1) {
            int y = g.e[j].v;
            checkDegree[y-1]--;
          }
          if (g.e[j].v == i+1) {
            int y = g.e[j].u;
            checkDegree[y-1]--;
          }
        }
        checkDegree[i] = -1;
      }
    }

    int numberVertices = g.vertices;
    int counter = 0;
    for (int i= 0; i < checkDegree.length; i++) {
      if (checkDegree[i] == -1) {
        numberVertices--;
      }
      if (checkDegree[i] == 2) {
        counter ++;
      }
    }

    if (counter == numberVertices) {
      return true;
    }
    else {
      return false;
    }
  }
  
}
