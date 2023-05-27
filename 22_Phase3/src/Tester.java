import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Tester {
	
	/** 
	 * @param args
	 */
	public static void main(String[] args) {
		// test1();
		test5();
		//test2();
	}

	// private static void test1() {
	// 	int vertices = 10;
	//	int[][] a = new int[vertices][vertices];
	//	a[0][7] = 1; a[7][0] =1;
	//	SubGraph[] subGraphs = GraphDecomposer.decompose(vertices, a);
	//	printSubGraphs(subGraphs);
	// }
	private static void test5() {
		Graph graph = new Graph("graphs/phase3/phase3_2020_graph15.txt");
		//graph.printAdj();
		graph.BronKLower();
	}
	private static void test3() {
		List<Integer> results = new ArrayList<Integer>();
		File[] files = new File("graphs/phase1").listFiles();
		for (File file : files) {
			if (file.isFile() && !file.getName().equals("graph16_2020.txt")) {
				Graph graph = new Graph("graphs/phase1/" + file.getName());
				Evolution evo = new Evolution(graph);
				results.add(evo.start());
			}
		}
		for (File file : files) {
			if (file.isFile()) {
				System.out.println(file.getName());
			}
		}
		for (Integer res : results) {
			System.out.println(res);

		}

	}

	private static void test4() {
		ArrayList<Integer> upper = new ArrayList<Integer>();
		ArrayList<Integer> lower = new ArrayList<Integer>();
		String dir = "graphs/phase3/";
		File[] files = new File(dir).listFiles();
		for (File file : files) {
			if (file.isFile()) {
				System.out.println("Working on File: "+dir+file.getName());
				Graph graph = new Graph(dir + file.getName());
				int[] orderArray = graph.getGreedyOrder();
				upper.add(graph.greedyColor(orderArray));
				lower.add(graph.BronKLower());
			}
		}
		for (File file : files) {
			if (file.isFile()) {
				System.out.println(file.getName());
			}
		}
		for (int i = 0; i < upper.size(); i++) {
			System.out.println("L: "+lower.get(i)+", U:"+upper.get(i));
		}

	}

	private static void test2() {
		Graph graph = new Graph("graphs/graph03_2020.txt");
		Graph[] subGraphs = GraphDecomposer.decompose(graph.vertices, graph.adjacencyMatrix);
		printSubGraphs(subGraphs);
		for (Graph graph2 : subGraphs) {
			int[] currentOrderArray = graph2.getGreedyOrder();
			graph2.greedyColor(currentOrderArray);
		}
	}

	
	/** 
	 * @param subGraphs
	 */
	private static void printSubGraphs(Graph[] subGraphs) {
		// print all the subGraphs
		System.out.println("number of subgraphs is " + subGraphs.length);
		for (int i = 0; i < subGraphs.length; i++) {
			Graph subGraph = subGraphs[i];
			int vertices = subGraph.vertices;
			StringBuffer s = new StringBuffer();
			for (int u = 0; u < vertices; u++) {
				for (int v = u + 1; v < vertices; v++) {
					if (subGraph.adjacencyMatrix[u][v] == 1) {
						s.append("(" + u + "," + v + ")");
					}
				}
			}
			System.out.println("subGraph " + i + ", vertices: " + vertices + ", edges: " + s);
		}
	}
}