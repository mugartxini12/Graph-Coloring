public class GraphDecomposer {
	
    /** 
     * @param vertices
     * @param adjacencyMatrix
     * @return SubGraph[]
     */
    public static Graph[] decompose(int vertices, int[][] adjacencyMatrix){
		boolean[] isAssignedToSubgraph = new boolean[vertices];
        int[] subGraphs = new int[vertices];
        int subGraphNumber = 0;

    
        for (int i = 0; i < vertices; i++){
            if (!isAssignedToSubgraph[i]){
            	int completed = 0;
            	int stillToBeChecked = 0;
                int[] neighbors = new int[vertices];
                neighbors[stillToBeChecked++] = i;
                isAssignedToSubgraph[i] = true;
                subGraphs[i] = subGraphNumber;
                
                while (stillToBeChecked != completed){
                	int vertexToBeChecked = neighbors[completed];
                	for(int j = 0; j < vertices; j++){
                		if (adjacencyMatrix[vertexToBeChecked][j] == 1 && !isAssignedToSubgraph[j]){
                			isAssignedToSubgraph[j] = true;
                             subGraphs[j] = subGraphNumber;
                             neighbors[stillToBeChecked++] = j;
                        }
                	}
                	completed++;
                }
                subGraphNumber++;               
            }
        }
        
       

        Graph[] graphArray = new Graph[subGraphNumber];
        for (int i = 0; i < subGraphNumber; i++){
            int numberOfVertices = 0;
            for(int j = 0; j < vertices; j++){
                if (subGraphs[j] == i){
                    numberOfVertices++;
                }
            }

            int[] verticesOfSub = new int[numberOfVertices];
            int count = 0;
            for(int j = 0; j < vertices; j++){
                if(subGraphs[j] == i){
                    verticesOfSub[count++] = j;
                }
            }

            int[][] adjacencyMatrixSub = new int[numberOfVertices][numberOfVertices];
            for (int x = 0; x < numberOfVertices; x++){
                for (int y = 0; y < numberOfVertices; y++){
                    if (adjacencyMatrix[verticesOfSub[x]][verticesOfSub[y]] == 1){
                        adjacencyMatrixSub[x][y] = 1;
                    }
                }
            }



            Graph nextGraph = new Graph(numberOfVertices, adjacencyMatrixSub);
            graphArray[i] = nextGraph;
        }

        // for (int i = 0; i < isDecomposed.length; i++){
        //     System.out.println(isDecomposed[i]);
        // }
    	return graphArray;
	} 
}
