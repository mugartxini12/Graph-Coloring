import java.io.*;


import java.util.*;

import java.lang.Math;


class ColEdge {
    int u;
    int v;
}

public class Graph {

    public ColEdge[] e;
    public  int vertices; // n
    public int edges; // m
    public int[][] adjacencyMatrix;
    // used for the game logic
    public int[] colourArray;
    // usd for our computed graph coloring
    public int[] finalColourArray;
    public static final boolean DEBUG = false;
    public final String COMMENT = "//";
    public int chromaticNumber;
    public int[][] edgeArray;
    boolean DEBUG2 = false;
    ArrayList<Integer> P = new ArrayList<Integer>();
    ArrayList<Integer> R = new ArrayList<Integer>();
    ArrayList<Integer> X = new ArrayList<Integer>();
    List<List<Integer>> cliquesList = new ArrayList<List<Integer>>();
    
    
    //Graph file constructor 
    public Graph(String filename) {

        String inputfile = filename;

        boolean seen[] = null;

        // ! n is the number of vertices in the graph
        vertices = -1;

        // ! m is the number of edges in the graph
        edges = -1;

        // ! e will contain the edges of the graph
        e = null;

        try {
            FileReader fr = new FileReader(inputfile);
            BufferedReader br = new BufferedReader(fr);

            String record = new String();

            // ! The first few lines of the file are allowed to be comments, staring with a
            // // symbol.
            // ! These comments are only allowed at the top of the file.

            // ! -----------------------------------------
            while ((record = br.readLine()) != null) {
                if (record.startsWith("//"))
                    continue;
                break; // Saw a line that did not start with a comment -- time to start reading the
                // data in!
            }

            if (record.startsWith("VERTICES = ")) {
                vertices = Integer.parseInt(record.substring(11));
                if (DEBUG)
                    System.out.println(COMMENT + " Number of vertices = " + vertices);
            }

            seen = new boolean[vertices + 1];

            record = br.readLine();

            if (record.startsWith("EDGES = ")) {
                edges = Integer.parseInt(record.substring(8));
                if (DEBUG)
                    System.out.println(COMMENT + " Expected number of edges = " + edges);
            }

            e = new ColEdge[edges];

            for (int d = 0; d < edges; d++) {
                if (DEBUG)
                    System.out.println(COMMENT + " Reading edge " + (d + 1));
                record = br.readLine();
                String data[] = record.split(" ");
                if (data.length != 2) {
                    System.out.println("Error! Malformed edge line: " + record);
                    System.exit(0);
                }
                e[d] = new ColEdge();

                e[d].u = Integer.parseInt(data[0]);
                e[d].v = Integer.parseInt(data[1]);

                seen[e[d].u] = true;
                seen[e[d].v] = true;

                if (DEBUG)
                    System.out.println(COMMENT + " Edge: " + e[d].u + " " + e[d].v);

            }

            String surplus = br.readLine();
            if (surplus != null) {
                if (surplus.length() >= 2)
                    if (DEBUG)
                        System.out.println(
                                COMMENT + " Warning: there appeared to be data in your file after the last edge: '"
                                        + surplus + "'");
            }
            br.close();
        } catch (IOException ex) {
            // catch possible io errors from readLine()
            System.out.println("Error! Problem reading file " + inputfile);
            System.exit(0);
        }

        for (int x = 1; x <= vertices; x++) {
            if (!seen[x]) {
                if (DEBUG)
                    System.out.println(COMMENT + " Warning: vertex " + x
                            + " didn't appear in any edge : it will be considered a disconnected vertex on its own.");
            }
        }
        adjacencyMatrix = createAdjacencyMatrix(e, vertices);
        colourArray = new int[vertices];
    }
    // adjacency matrix constructor to generate sugraphs from part of the graph 
    public Graph(int vertices, int[][] adjacencyMatrix) {
        this.vertices = vertices;
        this.adjacencyMatrix = adjacencyMatrix;
        int sum = 0;
        for (int[] is : adjacencyMatrix) {
            for (int is2 : is) {
                sum += is2;
            }
        }
        this.edges = sum / 2;
        e = new ColEdge[edges];
        int countRow = 0;
        for (int j = 0; j < vertices - 1; j++) {
            for (int k = j + 1; k < vertices; k++) {
                if (adjacencyMatrix[j][k] == 1) {
                    ColEdge eh = new ColEdge();
                    eh.v = j+1;
                    eh.u = k+1;
                    e[countRow] = eh;
                    countRow++;
                }
            }
        }
    }

    /**
     * @param e
     * @param n
     * @return int[][]
     * Creates the adjacency matrix  for the graph
     */
    public static int[][] createAdjacencyMatrix(ColEdge[] e, int n) {
        int[][] adjacent = new int[n][n];
        for (int i = 0; i < e.length; i++) {
            adjacent[e[i].u - 1][e[i].v - 1] = 1;
            adjacent[e[i].v - 1][e[i].u - 1] = 1;
        }
        return adjacent;
    }



    // This detects an Edgeless graph
    public boolean edgelessGraph() {
        if (edges == 0) {

            if (DEBUG)System.out.println("The graph is an Edgeless Graph, therefore the chromatic number is 1.");
            return true;
        } else
            return false;
    }

    // detecting a tree structure in a graph
    public boolean treeStructure() {
        if (edges == vertices - 1) {
            if (DEBUG)
                System.out.println("This graph is a tree, therefore the chromatic number is 2.");
            return true;
        } else
            return false;
    }
    // check wether the graph is complete
    public boolean completeGraph() {
    if (edges == vertices*(vertices-1)/2){
        return true;
        }
    return false;
    }

    //EXACT CHROMATIC NUMBER ALGORITHM
    //Computes the exact Chromatic Number for the Graph via backtracking (algorithm from phase 1)





    /**
     * @param from 
     * @param to
     * @param printCN whether to print updates to CN during execution 
     * @return int
     *  tries to find the chromatic number in between the bounds using backtracking 
     */
    public int computeChromaticNumber(int from,int to  ,boolean printCN) {
        chromaticNumber = to-1;
        finalColourArray = new int[vertices];

        while (!graphColour(0)||chromaticNumber<from) {
            chromaticNumber--;
            if (printCN)
                System.out.println("NEW BEST UPPER BOUND = " + chromaticNumber);
        }
        if (DEBUG) {
            System.out.println("There is a solution for Chromatic number = " + chromaticNumber
                    + " This solution colors the Vertices as follows:");
            for (int i = 0; i < finalColourArray.length; i++) {
                System.out.print("" + finalColourArray[i] + ", ");
            }
        }
        return chromaticNumber;
    }

    /**
     * @param vertex starting vertex 
     * @return boolean
     */
    // Backtracking algorithm from Phase 1 
    public boolean graphColour(int vertex) {
        boolean res;

        for (int color = 1; color <= chromaticNumber; color++) {
            // Iterate over the available colors and check wether or not that color can
            // color the k'th vertex
            if (isSafe(vertex, color)) {
                // If we can use this color for this vertex assign it to be that way
                finalColourArray[vertex] = color;
                if (vertex + 1 == vertices) {
                    // If we are at the last vertex return True
                    return true;
                } else {
                    // If we arent at the last vertex return a new call of graphColor to color the
                    // next vertex
                    res = graphColour(vertex + 1);
                    if (res) {
                        return res;
                    }
                }
            }
        }
        // If we run out of colors return False
        return false;
    }

    /**
     * @param vertex
     * @param color
     * @return boolean
     * checks wether or not color can color vertex 
     */
    public boolean isSafe(int vertex, int color) {
        // Tests if we can use this color to assign to this vertex
        for (int i = 0; i < vertex; i++) {
            if (adjacencyMatrix[vertex][i] == 1 && color == finalColourArray[i]) {
                return false;
                // Return false if we find any adjacent edge of the same color
            }
        }
        return true;
    }

    // UPPER BOUND (k+1)
    public void upperBound() {
        int maxverticedegree = 0;
        for (int i = 0; i < vertices; i++) {
            int verticedegree = 0;
            for (int d2 = 0; d2 < edges; d2++) {
                if (e[d2].u == i) {
                    verticedegree++;
                }
            }
            for (int d2 = 0; d2 < edges; d2++) {
                if (e[d2].v == i) {
                    verticedegree++;
                }
            }
            maxverticedegree = Math.max(verticedegree, maxverticedegree);
        }
        maxverticedegree++;
        System.out.println("The Mathematical Upper Bound is " + maxverticedegree);
    }



    // create an array with the vertices in a random order
    public static int[] createRandomOrder(int vertices){
        int [] newArray = new int[vertices];
        for (int i = 0; i < newArray.length; i++){
            newArray[i] = i;
        }
        for (int i = 0; i < newArray.length; i++){
            int j = (int) (Math.random() * vertices);
            // swap i and j
            int temp = newArray[i];
            newArray[i] = newArray[j];
            newArray[j] = temp;
        }
        return newArray;
    }  

    // method that does the greedy algorithm a 1000 times with a random order
    // it returns the smallest upper bound found
    public int greedythousand(int vertices, int[][] adjacencyMatrix) {
        int[] currentOrderArray = new int[vertices];
        int upperbound;
        int minimumAmountOfColors = vertices;

        for (int i = 0; i < 10000; i++) {
            currentOrderArray = createRandomOrder(vertices);
            upperbound = greedyColor(currentOrderArray);

            if (upperbound < minimumAmountOfColors) {
                minimumAmountOfColors = upperbound;

            }

        }
        return minimumAmountOfColors;
    }

    
    // generates the degenracy ordering for the greedy algorithm 
    public  int[] getGreedyOrder() {


        int[] degreeArray = new int[vertices];
        for (int i = 0; i < vertices; i++) {
            int degree = 0;
            for (int j = 0; j < vertices; j++) {
                if (adjacencyMatrix[i][j] == 1){
                    degree++;
                }
            }
            degreeArray[i] = degree;
        }

        int[] orderArray = new int[degreeArray.length];
        for (int i = 0; i < orderArray.length; i++){
            orderArray[i] = i;
        }

        int temp = 0;
        for (int i = 0; i < degreeArray.length; i++) {
            for (int j = i+1; j < degreeArray.length; j++) {
               if(degreeArray[i] < degreeArray[j]) {
                   temp = degreeArray[i];
                   degreeArray[i] = degreeArray[j];
                   degreeArray[j] = temp;

                   temp = orderArray[i];
                   orderArray[i] = orderArray[j];
                   orderArray[j] = temp;

               }
            }
        }

        return orderArray;
    }

    // colors the graph using the greedy algorithm given an ordering 
    public int greedyColor(int[] currentOrderArray) {
       int result[] = new int[vertices];
        // Set all values of result to -1, so they are unassigned to any colors
        for (int i = 0; i < vertices; i++) {
            result[i] = -1;
        }

        // The color of the first vertex is always 0
        result[0] = 1;

        // create a new temporary array that stores which colors are available for a
        // vertex
        boolean safeColor[] = new boolean[vertices];

        // Set all values of safeColor to true
        for (int i = 0; i < vertices; i++) {
            safeColor[i] = true;
        }



        // Assign colors to remaining V-1 vertices
        for (int u = 0 ; u < vertices; u++) {

            // Set all values of safeColor to true
            for (int i = 0; i < vertices; i++) {
                safeColor[i] = true;
            }
            // check if two vertices are adjacent and if so, set safeColor to false
            for (int j = 0; j < u; j++) {
                if (adjacencyMatrix[currentOrderArray[u]] [currentOrderArray[j]] == 1) {
                    safeColor[result[j]] = false;
                }
            }

            // Find the first color that is available
            int color;
            for (color = 1; color < vertices; color++) {
                if (safeColor[color])
                    break;
            }

            // set the color for the vertices to the assigned color
            result[u] = color;

        }

        if(DEBUG)
            System.out.println("Our upper bound for the chromatic number using the greedy algorithm is " + maxValue(result));
        return maxValue(result);
    }




    //getting the maximum value of an array - PART OF THE GREEDY ALGORITHM
    public static int maxValue(int array[]) {
        return Arrays.stream(array).max().getAsInt();
    }
    //approximates the Lower Bound using a Probabalistic model
    public int probabilityLower() {
        //maxEdges = edges in a fully connected graph with n vertices
        double maxEdges = (vertices * (vertices - 1)) / 2;
        // p = graphs edges over maxEdges
        double p = edges / maxEdges;
        //x = largest clique
        int x = 2;
        double P = 1 - Math.pow(1 - Math.pow(p, binomi(x, 2)), binomi(vertices, x));
        while (P > 0.99 && x <= vertices) {
            //increase the largest clique until the probability of a clique of this size being in our graph drops below 0.99
            x++;
            P = 1 - Math.pow(1 - Math.pow(p, binomi(x, 2)), binomi(vertices, x));
            //System.out.println(""+P+", "+x);
        }
        System.out.println("Our lower bound using a probabilistic method is " + (x - 1));
        return x-1;
    }

    //calculates n over k recursively
    public static long binomi(int n, int k) {
        if ((n == k) || (k == 0))
            return 1;
        else
            return binomi(n - 1, k) + binomi(n - 1, k - 1);
    }
    //prints the adjecancy matrix 
    public void printAdj() {

        System.out.println("adjacency matrix:");
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                System.out.print(adjacencyMatrix[i][j]);
            }
            System.out.println();
        }

    }

    
     
    //starts the BronKerbosch Algoritm with the degeneracy ordering as the startring order 
    public int BronKLower() {

        //creating a new 2D Array for the edges
        edgeArray = new int[edges][2];
        // writing the edges of a graph into a 2D array
        for (int i = 0; i < edges; i++) {
            edgeArray[i][0] = e[i].v-1;
            edgeArray[i][1] = e[i].u-1;
        }
        if (DEBUG2) {
            System.out.println("This should be the edge array: ");
            for (int i = 0; i < edgeArray.length; i++) {
                for (int j = 0; j < edgeArray[0].length; j++) {
                    System.out.print(edgeArray[i][j] + " ");
                } System.out.println();
            }
        }

        //getting an array of the pivots from the highest degree to the lowest
        int[] maxPivotsArray = getGreedyOrder();
        if (DEBUG2) {
            System.out.println("This should be the vertices in greedy order: ");
            for (int i = 0; i < maxPivotsArray.length; i++) {
                    System.out.println(maxPivotsArray[i] + " ");
            }
        }

        //transferring the max degree vertices array into an Array List
        int [] pTemp = maxPivotsArray;
        for (int i = 0; i < pTemp.length; i++ ){
            P.add(pTemp[i]);
        }
        if (DEBUG2) {
            System.out.println("This should be the vertices in greedy order in an array: ");
                System.out.println(P.toString() + " ");
        }

        //starting the recursive function
        BronKerboschWithPivot(R, P, X);

        //printing the maximal clique
        int max = 0;
        for (List<Integer> clique : cliquesList) {
            if(clique.size()>max)
                max = clique.size();
        }
        //System.out.println();
        //System.out.println("The chromatic number with the Bron Kerbosch lower Bound: " + max);
        return max;
    }

    // getting the highest degree pivot from the list P
    public int getMaxDegreePivot(ArrayList<Integer> P){
        // a high degree pivot
        int maxPivot = (int) P.get(0);
        if (DEBUG2) {System.out.println("This should be P :" + P.toString());}
        
        return maxPivot;
    }
    
    /**
    * @param R
    * @param P
    * @param X
    * @return boolean
    * finds a lower bound for the chromatic number by finding maximal ciques recursively 
    */
    public void BronKerboschWithPivot(ArrayList<Integer> R, ArrayList<Integer> P, ArrayList<Integer> X) {
        if(DEBUG){System.out.println("R:"+R.toString()+" P:"+P.toString()+" X:"+X.toString());}
        //if P and X are both empty then report R as a maximal clique
        if (P.size() == 0 && X.size() == 0) {
            cliquesList.add(R);
            if(DEBUG)System.out.println("This is the maximal clique: " + R.toString());
            return;
        }
        int u = getMaxDegreePivot(union(P, X));
        ArrayList<Integer> S = difference(P, getNeighbours(u));
        for (Integer v : S) {
            BronKerboschWithPivot(union(R, List.of(v)), intersect(P, getNeighbours((int) v)),
                    intersect(X, getNeighbours((int) v)));
            P.remove(v);
            X.add(v);
        }
        
    }

    // getting the neighbouring vertices from the pivot U
    public ArrayList<Integer> getNeighbours(int pivotU){
        ArrayList<Integer> NeighboursList = new ArrayList<Integer>();

        // adding the to be deleted vertices to a list
        for(int j = 0; j < edgeArray.length; j ++) {
            if (edgeArray[j][0] == pivotU) {
                NeighboursList.add(edgeArray[j][1]);
            }
            if (edgeArray[j][1] == pivotU) {
                NeighboursList.add(edgeArray[j][0]);
            }
        }return NeighboursList;
    }

    // this method gives the intersection of two sets
    public ArrayList<Integer> intersect(List<Integer> firstList, List<Integer> secondList) {
        ArrayList<Integer> intersectList = new ArrayList<Integer>(firstList);
        intersectList.retainAll(secondList);
        return intersectList;
    }

    // this method gives Union of two sets
    public ArrayList<Integer> union(List<Integer> firstList, List<Integer> secondList) {
        ArrayList<Integer> unionSet = new ArrayList<Integer>(firstList);
        unionSet.addAll(secondList);
        return unionSet;
    }
    //this method sudstracts one set from another 
    public ArrayList<Integer> difference(List<Integer> firstList, List<Integer> secondList) {
        ArrayList<Integer> unionSet = new ArrayList<Integer> (firstList);
        unionSet.removeAll(secondList);
        return unionSet;
    }

    
    //checks wether the graph is Bipartite 
    public boolean isBipartite() {
        /*
         * if the chart is bipartite, the result is going to be true. Otherwise, the
         * result is goint to be false
         */

        int coloursArray[] = new int[vertices];

        for (int i = 0; i < vertices; i++)
            coloursArray[i] = -1; // -1--------> no colour

        int pos = 0;

        coloursArray[pos] = 1;

        // FIFO First In First Out
        Queue<Integer> queue;

        queue = new LinkedList<Integer>();

        queue.add(pos);

        boolean bipartite = true;

        // BFS (similar)
        while (!queue.isEmpty() && bipartite) {

            int vert1 = queue.poll();
            // false if there is a self-loop
            if (adjacencyMatrix[vert1][vert1] == 1) {
                // there is a self-loop
                bipartite = false;
            } else {
                int vert2 = 0;
                // try to find the adjacent vertices that are not colored
                while (vert2 < vertices && bipartite) {
                    if (adjacencyMatrix[vert1][vert2] == 1 && coloursArray[vert2] == -1) {
                        coloursArray[vert2] = 1 - coloursArray[vert1];
                        queue.add(vert2);

                    } else if (adjacencyMatrix[vert1][vert2] == 1 && coloursArray[vert2] == coloursArray[vert1])
                         //two adjacent vertices are colored with the same color
                        bipartite = false;
                    vert2++;
                }
            }
        }

        return bipartite;
    }
}
