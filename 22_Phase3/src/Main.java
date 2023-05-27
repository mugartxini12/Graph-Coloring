import java.util.Arrays;

public class Main {

    public static int[] greedyLB;
    public static int[] greedyUB;
    public static int[] exactCN;

    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        String filename = args[0];
        Graph graph = new Graph(filename);
        //graph.callingUpperGreedy();
        
        Graph[] subGraphs = GraphDecomposer.decompose(graph.vertices, graph.adjacencyMatrix);
        //set the correct length of array to store subValues for each subGraph
        exactCN = new int[subGraphs.length];
        greedyUB = new int[subGraphs.length];
        greedyLB = new int[subGraphs.length];
        //System.out.println("SUBGRAPHS: "+subGraphs.length);

        for (int i = 0; i<subGraphs.length; i++){
            Graph subGraph = subGraphs[i];
            //check for hidden structure
            if (subGraph.edgelessGraph()) {
                exactCN[i] = 1;
                greedyLB[i] = 1;
                greedyUB[i] = 1;
                break;
            }
            if (subGraph.isBipartite()) {
                exactCN[i] = 2;
                greedyLB[i] = 2;
                greedyUB[i] = 2;
                break;
            }
            if (subGraph.completeGraph()) {
                exactCN[i] = subGraph.vertices;
                greedyLB[i] = subGraph.vertices;
                greedyUB[i] = subGraph.vertices;
                break;
            }
            //UPPER BOUND
            int[] orderArray = subGraph.getGreedyOrder();
            int greedySubValue =subGraph.greedyColor(orderArray);
            int thousandGreedy = subGraph.greedythousand(subGraph.vertices, subGraph.adjacencyMatrix);
            
            if(greedySubValue < thousandGreedy){
                greedyUB[i] = greedySubValue;
            }
            else if (greedySubValue >= thousandGreedy){
               greedyUB[i] = thousandGreedy;
            }
            //PRINT OUT NEW UPPER BOUND
            Arrays.sort(greedyUB);
            System.out.println("NEW BEST UPPER BOUND = "+greedyUB[greedyUB.length-1]);

            //LOWER BOUND
            int lowerBound = subGraph.BronKLower();
            greedyLB[i] = lowerBound;

            //IF LOWER BOUND = UPPER BOUND --> Chromatic Nuber
            if (greedyLB[i] == greedyUB[i]) {
                exactCN[i] = greedyLB[i];
                break;
            }
            //PRINT OUT NEW LOWER BOUND
            Arrays.sort(greedyLB);
            System.out.println("NEW BEST LOWER BOUND = "+greedyLB[greedyLB.length-1]);

            //IF Graph is not to big calculate exect CN
            if (subGraph.vertices <= 20) {
                exactCN[i] = subGraph.computeChromaticNumber(lowerBound,greedyUB[i],false);
            }
        }

        Arrays.sort(greedyLB);
        Arrays.sort(greedyUB);
        //IF UPPER BOUND == LOWER BOUND --> Chromatic Number
        if (greedyUB[greedyUB.length-1] == greedyLB[greedyLB.length-1]) {
            System.out.println("CHROMATIC NUMBER = "+greedyUB[greedyUB.length-1]);
        }
        //ELSE calculate the missing Chromatic Numbers
        else {
            graph.computeChromaticNumber(greedyLB[greedyLB.length - 1], greedyUB[greedyUB.length - 1], true);
            
        }
    }
}
